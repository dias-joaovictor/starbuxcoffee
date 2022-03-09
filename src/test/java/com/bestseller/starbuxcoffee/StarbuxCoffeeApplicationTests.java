package com.bestseller.starbuxcoffee;

import static com.bestseller.starbuxcoffee.core.BasicMathOperations.truncateDecimal;
import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bestseller.starbuxcoffee.core.BasicMathOperations;
import com.bestseller.starbuxcoffee.dto.CartDTO;
import com.bestseller.starbuxcoffee.dto.ComboDTO;
import com.bestseller.starbuxcoffee.dto.ComboItemDTO;
import com.bestseller.starbuxcoffee.dto.CustomerInfoDTO;
import com.bestseller.starbuxcoffee.dto.ProductDTO;
import com.bestseller.starbuxcoffee.dto.ProductsDTO;
import com.bestseller.starbuxcoffee.repository.PriceRepository;
import com.bestseller.starbuxcoffee.security.dto.LoginDTO;
import com.bestseller.starbuxcoffee.security.dto.TokenDTO;
import com.bestseller.starbuxcoffee.security.model.User;
import com.bestseller.starbuxcoffee.security.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class StarbuxCoffeeApplicationTests {

	private static final String CUSTOMER_1 = "14228098875";

	private static final String CUSTOMER_2 = "14228098848";

	private static final String CUSTOMER_DUMMY = "DUMMY";

	@Autowired
	UserRepository userRepository;

	@LocalServerPort
	int localServerPort;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PriceRepository priceRepository;

	public static final String DRINK_1 = "DRINK 1";
	public static final String DRINK_2 = "DRINK 2";
	public static final String DRINK_3 = "DRINK 3";
	public static final String TOPPING_1 = "TOPPING 1";
	public static final String TOPPING_2 = "TOPPING 2";

	private String getServerUrl() {
		return MessageFormat.format("http://localhost:{0,number,#}/api", this.localServerPort);
	}

	@Test
	@Order(0)
	void productsTest() {
		final List<User> users = new ArrayList<>();
		this.userRepository.findAll().forEach(users::add);
		assertThat(users.size()).isEqualTo(3);
		final TokenDTO token = this.getToken();
		ProductsDTO products = this.getProducts();

		assertNotNull(products);
		assertTrue(products.getProducts().isEmpty());

		this.populate(token);

		products = this.getProducts();

		assertNotNull(products);
		assertFalse(products.getProducts().isEmpty());
		assertEquals(5, products.getProducts().size());

		try {
			this.addProduct(this.getProductDTO("TOPPING 2 description", "TOPPING 2", "TOPPING", 2.20, 0), token);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Product with Type: TOPPING and Name: TOPPING 2 already exists"));
		}

		try {
			this.addProduct(this.getProductDTO("TOPPING 2 description", "TOPPING 2", "TOPPING", 2.20, 5), token);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Product Already exists. Use the PUT method to update."));
		}

		try {
			this.addProduct(this.getProductDTO("TOPPING 2 description", "TOPPING 2", "TOPPING", 0, 5), token);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Price must be Greater than Zero"));
		}

		try {
			this.addProduct(this.getProductDTO("TOPPING 2 description", "TOPPING 2", "TOPPING", -15, 5), token);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Price must be Greater than Zero"));
		}

		products = this.getProducts();

		assertNotNull(products);
		assertFalse(products.getProducts().isEmpty());
		assertEquals(5, products.getProducts().size());

		ProductDTO product = this.getProductById(1);

		assertNotNull(product);
		assertEquals(1, product.getId());
		assertEquals("DRINK 1 description", product.getDescription());
		assertEquals("DRINK 1", product.getName());
		assertEquals(10.19, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertEquals("DRINK", product.getProductType());

		this.addProduct(this.getProductDTO("TO Delete ITEM description", "TO DELETE DRINK", "DRINK", 12.80, 0), token);
		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(12.80, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		product.setPrice(19.80);

		this.updateProduct(product, token);

		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(19.80, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		product.setPrice(19.90);

		this.updateProduct(product, token);

		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(19.90, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		product.setPrice(22.50);

		this.priceRepository.findAll().stream().filter(item -> item.getProduct().getId() == 6).forEach(item -> {
			item.setExpirationDate(null);
			this.priceRepository.saveAndFlush(item);
		});

		this.updateProduct(product, token);

		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(22.50, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		product.setName("DRINK 1");

		try {
			this.updateProduct(product, token);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Product with Type: DRINK and Name: DRINK 1 already exists"));
		}

		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(22.50, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		product.setProductType("TOPPING");

		try {
			this.updateProduct(product, token);
		} catch (final Exception e) {
			assertTrue(e.getMessage()
					.contains("It's impossible to change the product type. You must to create a new Product."));
		}

		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(22.50, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		product.setProductType("TOPPINGS");

		try {
			this.updateProduct(product, token);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Product type is invalid. Valid Types: "));
		}

		product = this.getProductById(6);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(22.50, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		this.deleteProduct(6, token);

		product = this.getProductById(6);
		assertNull(product);

	}

	@Test
	@Order(1)
	void cartTest1() {
		CartDTO cart = this.openCart(CUSTOMER_1);
		final String cartId = cart.getCartId();
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(0d, cart.getOrder().getFinalPrice(), 0d);
		assertEquals(0d, cart.getOrder().getTotalPrice(), 0d);

		cart = this.getCurrentCart(cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(0d, cart.getOrder().getFinalPrice(), 0d);
		assertEquals(0d, cart.getOrder().getTotalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		final Map<String, ProductDTO> products = this.getProducts()//
				.getProducts()//
				.stream() //
				.collect(toMap(ProductDTO::getName, Function.identity()));

		assertNotNull(products);
		assertFalse(products.isEmpty());
		assertEquals(5, products.size());

		ComboDTO combo = new ComboDTO();
		combo.setPrincipalComboItem(new ComboItemDTO());
		combo.getPrincipalComboItem().setProduct(products.get(DRINK_1));

		cart = this.addItem(cartId, combo);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(1, cart.getOrder().getCombos().size());
		assertNotNull(cart.getOrder().getCombos().get(0).getPrincipalComboItem());
		assertNotNull(cart.getOrder().getCombos().get(0).getPrincipalComboItem().getId());
		assertNotNull(cart.getOrder().getCombos().get(0).getPrincipalComboItem().getProduct());
		assertEquals(DRINK_1, cart.getOrder().getCombos().get(0).getPrincipalComboItem().getProduct().getName());
		assertTrue(cart.getOrder().getCombos().get(0).getSideComboItens().isEmpty());
		assertEquals(10.19, cart.getOrder().getFinalPrice(), 0d);
		assertEquals(10.19, cart.getOrder().getTotalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		combo = new ComboDTO();
		combo.setPrincipalComboItem(new ComboItemDTO());
		combo.getPrincipalComboItem().setProduct(products.get(DRINK_2));

		cart = this.addItem(cartId, combo);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(2, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_1)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_1, item.getPrincipalComboItem().getProduct().getName());
			} else {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			}
			assertTrue(item.getSideComboItens().isEmpty());
		});

		assertEquals(BasicMathOperations.truncateDecimal(10.19 + 20.19, 2), cart.getOrder().getTotalPrice(), 0d);
		assertEquals(BasicMathOperations.truncateDecimal((10.19 + 20.19) * 0.75, 2), cart.getOrder().getFinalPrice(),
				0d);
		assertEquals(cartId, cart.getCartId());

		combo = new ComboDTO();
		combo.setPrincipalComboItem(new ComboItemDTO());
		combo.getPrincipalComboItem().setProduct(products.get(DRINK_3));

		cart = this.addItem(cartId, combo);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_1)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_1, item.getPrincipalComboItem().getProduct().getName());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_2)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_3)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_3, item.getPrincipalComboItem().getProduct().getName());
			}
			assertTrue(item.getSideComboItens().isEmpty());
		});

		assertEquals(BasicMathOperations.truncateDecimal(10.19 + 20.19 + 1.19, 2), cart.getOrder().getTotalPrice(), 0d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((10.19 + 20.19 + 1.19) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		cart = this.getCurrentCart(cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_1)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_1, item.getPrincipalComboItem().getProduct().getName());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_2)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_3)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_3, item.getPrincipalComboItem().getProduct().getName());
			}
			assertTrue(item.getSideComboItens().isEmpty());
		});

		assertEquals(BasicMathOperations.truncateDecimal(10.19 + 20.19 + 1.19, 2), cart.getOrder().getTotalPrice(), 0d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((10.19 + 20.19 + 1.19) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		try {
			cart = this.checkout(CUSTOMER_DUMMY, cartId);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Your customer id is invalid. It's impossible to checkout"));
		}

		cart.getOrder().getCombos().stream().forEach(item -> {
			if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_3)) {
				ComboItemDTO comboItem = new ComboItemDTO();
				comboItem.setProduct(products.get(TOPPING_1));
				item.getSideComboItens().add(comboItem);
				item.getSideComboItens().add(comboItem);
				item.getSideComboItens().add(comboItem);

				comboItem = new ComboItemDTO();
				comboItem.setProduct(products.get(TOPPING_2));
				item.getSideComboItens().add(comboItem);

				this.addItem(cartId, item);

			}
		});

		cart = this.getCurrentCart(cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_1)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_1, item.getPrincipalComboItem().getProduct().getName());
				assertTrue(item.getSideComboItens().isEmpty());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_2)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
				assertTrue(item.getSideComboItens().isEmpty());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_3)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_3, item.getPrincipalComboItem().getProduct().getName());
				assertEquals(4, item.getSideComboItens().size());
			}

		});

		assertEquals(BasicMathOperations.truncateDecimal(10.19 + 20.19 + 1.19 + 5 + 5 + 5 + 2.2, 2),
				cart.getOrder().getTotalPrice(), 0.2d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((10.19 + 20.19 + 1.19 + 5 + 5 + 5 + 2.2) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		cart = this.checkout(CUSTOMER_1, cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_1)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_1, item.getPrincipalComboItem().getProduct().getName());
				assertTrue(item.getSideComboItens().isEmpty());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_2)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
				assertTrue(item.getSideComboItens().isEmpty());
			} else if (item.getPrincipalComboItem().getProduct().getName().equals(DRINK_3)) {
				assertNotNull(item.getPrincipalComboItem());
				assertNotNull(item.getPrincipalComboItem().getId());
				assertNotNull(item.getPrincipalComboItem().getProduct());
				assertEquals(DRINK_3, item.getPrincipalComboItem().getProduct().getName());
				assertEquals(4, item.getSideComboItens().size());
			}
		});

		assertEquals(BasicMathOperations.truncateDecimal(10.19 + 20.19 + 1.19 + 5 + 5 + 5 + 2.2, 2),
				cart.getOrder().getTotalPrice(), 0.2d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((10.19 + 20.19 + 1.19 + 5 + 5 + 5 + 2.2) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		try {
			cart = this.getCurrentCart(cartId);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Invalid CartClientId"));
		}

	}

	@Test
	@Order(2)
	void cartTest2() {
		CartDTO cart = this.openCart(CUSTOMER_2);
		final String cartId = cart.getCartId();
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(0d, cart.getOrder().getFinalPrice(), 0d);
		assertEquals(0d, cart.getOrder().getTotalPrice(), 0d);

		cart = this.getCurrentCart(cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(0d, cart.getOrder().getFinalPrice(), 0d);
		assertEquals(0d, cart.getOrder().getTotalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		final Map<String, ProductDTO> products = this.getProducts()//
				.getProducts()//
				.stream() //
				.collect(toMap(ProductDTO::getName, Function.identity()));

		assertNotNull(products);
		assertFalse(products.isEmpty());
		assertEquals(5, products.size());

		ComboDTO combo = new ComboDTO();
		combo.setPrincipalComboItem(new ComboItemDTO());
		combo.getPrincipalComboItem().setProduct(products.get(DRINK_2));

		cart = this.addItem(cartId, combo);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(1, cart.getOrder().getCombos().size());
		assertNotNull(cart.getOrder().getCombos().get(0).getPrincipalComboItem());
		assertNotNull(cart.getOrder().getCombos().get(0).getPrincipalComboItem().getId());
		assertNotNull(cart.getOrder().getCombos().get(0).getPrincipalComboItem().getProduct());
		assertEquals(DRINK_2, cart.getOrder().getCombos().get(0).getPrincipalComboItem().getProduct().getName());
		assertTrue(cart.getOrder().getCombos().get(0).getSideComboItens().isEmpty());
		assertEquals(20.19, cart.getOrder().getTotalPrice(), 0d);
		assertEquals(truncateDecimal(20.19 * 0.75, 2), cart.getOrder().getFinalPrice(), 0d);
		assertEquals(cartId, cart.getCartId());

		combo = new ComboDTO();
		combo.setPrincipalComboItem(new ComboItemDTO());
		combo.getPrincipalComboItem().setProduct(products.get(DRINK_2));

		cart = this.addItem(cartId, combo);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(2, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			assertNotNull(item.getPrincipalComboItem());
			assertNotNull(item.getPrincipalComboItem().getId());
			assertNotNull(item.getPrincipalComboItem().getProduct());
			assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			assertTrue(item.getSideComboItens().isEmpty());
		});

		assertEquals(BasicMathOperations.truncateDecimal(20.19 + 20.19, 2), cart.getOrder().getTotalPrice(), 0d);
		assertEquals(BasicMathOperations.truncateDecimal((20.19 + 20.19) * 0.75, 2), cart.getOrder().getFinalPrice(),
				0d);
		assertEquals(cartId, cart.getCartId());

		combo = new ComboDTO();
		combo.setPrincipalComboItem(new ComboItemDTO());
		combo.getPrincipalComboItem().setProduct(products.get(DRINK_2));

		cart = this.addItem(cartId, combo);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			assertNotNull(item.getPrincipalComboItem());
			assertNotNull(item.getPrincipalComboItem().getId());
			assertNotNull(item.getPrincipalComboItem().getProduct());
			assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			assertTrue(item.getSideComboItens().isEmpty());
		});

		assertEquals(BasicMathOperations.truncateDecimal(20.19 + 20.19 + 20.19, 2), cart.getOrder().getTotalPrice(),
				0d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((20.19 + 20.19 + 20.19) - 20.19, 2),
				cart.getOrder().getFinalPrice(), 1d);
		assertEquals(cartId, cart.getCartId());

		cart = this.getCurrentCart(cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			assertNotNull(item.getPrincipalComboItem());
			assertNotNull(item.getPrincipalComboItem().getId());
			assertNotNull(item.getPrincipalComboItem().getProduct());
			assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			assertTrue(item.getSideComboItens().isEmpty());
		});

		assertEquals(BasicMathOperations.truncateDecimal(20.19 + 20.19 + 20.19, 2), cart.getOrder().getTotalPrice(),
				0d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((20.19 + 20.19 + 20.19) - 20.19, 2),
				cart.getOrder().getFinalPrice(), 1d);
		assertEquals(cartId, cart.getCartId());

		try {
			cart = this.checkout(CUSTOMER_DUMMY, cartId);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Your customer id is invalid. It's impossible to checkout"));
		}

		cart.getOrder().getCombos().stream().findAny().ifPresent(item -> {
			ComboItemDTO comboItem = new ComboItemDTO();
			comboItem.setProduct(products.get(TOPPING_1));
			item.getSideComboItens().add(comboItem);
			item.getSideComboItens().add(comboItem);
			item.getSideComboItens().add(comboItem);

			comboItem = new ComboItemDTO();
			comboItem.setProduct(products.get(TOPPING_2));
			item.getSideComboItens().add(comboItem);

			this.addItem(cartId, item);

		});

		cart = this.getCurrentCart(cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			assertNotNull(item.getPrincipalComboItem());
			assertNotNull(item.getPrincipalComboItem().getId());
			assertNotNull(item.getPrincipalComboItem().getProduct());
			assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			if (!item.getSideComboItens().isEmpty()) {
				assertEquals(4, item.getSideComboItens().size());
			}
		});

		assertEquals(BasicMathOperations.truncateDecimal(20.19 + 20.19 + 20.19 + 5 + 5 + 5 + 2.2, 2),
				cart.getOrder().getTotalPrice(), 0d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((20.19 + 20.19 + 20.19 + 5 + 5 + 5 + 2.2) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 1d);
		assertEquals(cartId, cart.getCartId());

		cart = this.syncCart(cartId, cart);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			assertNotNull(item.getPrincipalComboItem());
			assertNotNull(item.getPrincipalComboItem().getId());
			assertNotNull(item.getPrincipalComboItem().getProduct());
			assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());
			if (!item.getSideComboItens().isEmpty()) {
				assertEquals(4, item.getSideComboItens().size());
			}
		});

		assertEquals(BasicMathOperations.truncateDecimal(20.19 + 20.19 + 20.19 + 5 + 5 + 5 + 2.2, 2),
				cart.getOrder().getTotalPrice(), 0d);
		// Same discount logic
		assertEquals(BasicMathOperations.truncateDecimal((20.19 + 20.19 + 20.19 + 5 + 5 + 5 + 2.2) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 1d);
		assertEquals(cartId, cart.getCartId());

		cart = this.checkout(CUSTOMER_2, cartId);
		assertNotNull(cart);
		assertNotNull(cart.getCartId());
		assertNotNull(cart.getExpiresAt());
		assertNotEquals(0l, cart.getRemainingTime());
		assertNotNull(cart.getOrder());
		assertNotNull(cart.getOrder().getId());
		assertNotNull(cart.getOrder().getCombos());
		assertEquals(3, cart.getOrder().getCombos().size());

		cart.getOrder().getCombos().stream().forEach(item -> {
			assertNotNull(item.getPrincipalComboItem());
			assertNotNull(item.getPrincipalComboItem().getId());
			assertNotNull(item.getPrincipalComboItem().getProduct());
			assertEquals(DRINK_2, item.getPrincipalComboItem().getProduct().getName());

			if (!item.getSideComboItens().isEmpty()) {
				assertEquals(4, item.getSideComboItens().size());
			}
		});

		assertEquals(BasicMathOperations.truncateDecimal(20.19 + 20.19 + 20.19 + 5 + 5 + 5 + 2.2, 2),
				cart.getOrder().getTotalPrice(), 0d);
		assertEquals(BasicMathOperations.truncateDecimal((20.19 + 20.19 + 20.19 + 5 + 5 + 5 + 2.2) * 0.75, 2),
				cart.getOrder().getFinalPrice(), 1d);
		assertEquals(cartId, cart.getCartId());

		try {
			cart = this.getCurrentCart(cartId);
		} catch (final Exception e) {
			assertTrue(e.getMessage().contains("Invalid CartClientId"));
		}

	}

	private CartDTO syncCart(final String cartId, final CartDTO cart) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return this.restTemplate.exchange(//
				this.getServerUrl().concat(format("/cart/{0}/sync", cartId)), //
				HttpMethod.PUT, //
				new HttpEntity<>(cart, httpHeaders), //
				CartDTO.class).getBody();
	}

	private CartDTO addItem(final String cartId, final ComboDTO combo) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return this.restTemplate.exchange(//
				this.getServerUrl().concat(format("/cart/{0}/item", cartId)), //
				HttpMethod.PUT, //
				new HttpEntity<>(combo, httpHeaders), //
				CartDTO.class).getBody();
	}

	private CartDTO checkout(final String clientId, final String cartId) {
		final CustomerInfoDTO customerInfo = new CustomerInfoDTO(clientId);
		return this.restTemplate.postForEntity(//
				this.getServerUrl().concat(format("/cart/{0}/checkout", cartId)), //
				customerInfo, //
				CartDTO.class)//
				.getBody();
	}

	private CartDTO openCart(final String clientId) {
		final CustomerInfoDTO customerInfo = new CustomerInfoDTO(clientId);
		return this.restTemplate.postForEntity(this.getServerUrl().concat("/cart/open"), customerInfo, CartDTO.class)
				.getBody();
	}

	private CartDTO getCurrentCart(final String cartId) {
		return this.restTemplate.getForEntity(this.getServerUrl().concat("/cart/" + cartId), CartDTO.class).getBody();
	}

	private void addProduct(final ProductDTO productDTO, final TokenDTO token) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + token.getToken());
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		this.restTemplate.postForLocation(//
				this.getServerUrl().concat("/admin/products"), //
				new HttpEntity<>(productDTO, httpHeaders));

	}

	private void updateProduct(final ProductDTO productDTO, final TokenDTO token) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + token.getToken());
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		this.restTemplate.put(//
				this.getServerUrl().concat("/admin/products"), //
				new HttpEntity<>(productDTO, httpHeaders));

	}

	private void deleteProduct(final int id, final TokenDTO token) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + token.getToken());
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		this.restTemplate.exchange(//
				this.getServerUrl().concat("/admin/products/" + id), //
				HttpMethod.DELETE, //
				new HttpEntity<>(null, httpHeaders), //
				Object.class);

//		this.restTemplate.delete(//
//				this.getServerUrl().concat("/admin/products/" + id), //
//				new HttpEntity<>(new Object(), httpHeaders));

	}

	private ProductDTO getProductDTO(final String description, final String name, final String productType,
			final double price, final int id) {
		final ProductDTO dto = new ProductDTO();
		dto.setId(id);
		dto.setDescription(description);
		dto.setName(name);
		dto.setProductType(productType);
		dto.setPrice(price);

		return dto;
	}

	private ProductsDTO getProducts() {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		final ResponseEntity<ProductsDTO> responseEntity = this.restTemplate.exchange(//
				this.getServerUrl().concat("/products"), //
				HttpMethod.GET, //
				new HttpEntity<>(null, httpHeaders), //
				ProductsDTO.class);

		return responseEntity.getBody();
	}

	private ProductDTO getProductById(final int id) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		final ResponseEntity<ProductDTO> responseEntity = this.restTemplate.exchange(//
				this.getServerUrl().concat("/products/" + id), //
				HttpMethod.GET, //
				new HttpEntity<>(null, httpHeaders), //
				ProductDTO.class);

		return responseEntity.getBody();
	}

	private void populate(final TokenDTO token) {
		this.populateProduct(token);

	}

	private void populateProduct(final TokenDTO token) {
		this.addProduct(this.getProductDTO("DRINK 1 description", "DRINK 1", "DRINK", 10.19, 0), token);
		this.addProduct(this.getProductDTO("DRINK 2 description", "DRINK 2", "DRINK", 20.19, 0), token);
		this.addProduct(this.getProductDTO("DRINK 3 description", "DRINK 3", "DRINK", 1.19, 0), token);
		this.addProduct(this.getProductDTO("TOPPING 1 description", "TOPPING 1", "TOPPING", 5, 0), token);
		this.addProduct(this.getProductDTO("TOPPING 2 description", "TOPPING 2", "TOPPING", 2.20, 0), token);
	}

	private TokenDTO getToken() {
		final LoginDTO loginDTO = new LoginDTO();
		loginDTO.setLogin("joao.dias@bestseller.com");
		loginDTO.setPassword("diasadmin");
		return this.restTemplate.postForEntity(this.getServerUrl().concat("/auth"), loginDTO, TokenDTO.class).getBody();
	}

}
