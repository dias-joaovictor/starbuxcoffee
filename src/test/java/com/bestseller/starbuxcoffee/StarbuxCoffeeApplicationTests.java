package com.bestseller.starbuxcoffee;

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

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
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

import com.bestseller.starbuxcoffee.dto.ProductDTO;
import com.bestseller.starbuxcoffee.dto.ProductsDTO;
import com.bestseller.starbuxcoffee.repository.PriceRepository;
import com.bestseller.starbuxcoffee.security.dto.LoginDTO;
import com.bestseller.starbuxcoffee.security.dto.TokenDTO;
import com.bestseller.starbuxcoffee.security.model.User;
import com.bestseller.starbuxcoffee.security.repository.UserRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class StarbuxCoffeeApplicationTests {

	@Autowired
	UserRepository userRepository;

	@LocalServerPort
	int localServerPort;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PriceRepository priceRepository;

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
		ProductsDTO products = this.getProducts(token);

		assertNotNull(products);
		assertTrue(products.getProducts().isEmpty());

		this.populate(token);

		products = this.getProducts(token);

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

		products = this.getProducts(token);

		assertNotNull(products);
		assertFalse(products.getProducts().isEmpty());
		assertEquals(5, products.getProducts().size());

		ProductDTO product = this.getProductById(1, token);

		assertNotNull(product);
		assertEquals(1, product.getId());
		assertEquals("DRINK 1 description", product.getDescription());
		assertEquals("DRINK 1", product.getName());
		assertEquals(10.19, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertEquals("DRINK", product.getProductType());

		this.addProduct(this.getProductDTO("TO Delete ITEM description", "TO DELETE DRINK", "DRINK", 12.80, 0), token);
		product = this.getProductById(6, token);

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

		product = this.getProductById(6, token);

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

		product = this.getProductById(6, token);

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

		product = this.getProductById(6, token);

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

		product = this.getProductById(6, token);

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

		product = this.getProductById(6, token);

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

		product = this.getProductById(6, token);

		assertNotNull(product);
		assertEquals(6, product.getId());
		assertEquals("TO Delete ITEM description", product.getDescription());
		assertEquals("TO DELETE DRINK", product.getName());
		assertEquals(22.50, product.getPrice(), 0d);
		assertEquals(0, product.getPriority());
		assertNotEquals(0, product.getPriceId());
		assertEquals("DRINK", product.getProductType());

		this.deleteProduct(6, token);

		product = this.getProductById(6, token);
		assertNull(product);

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

	private ProductsDTO getProducts(final TokenDTO token) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + token.getToken());
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		final ResponseEntity<ProductsDTO> responseEntity = this.restTemplate.exchange(//
				this.getServerUrl().concat("/admin/products"), //
				HttpMethod.GET, //
				new HttpEntity<>(null, httpHeaders), //
				ProductsDTO.class);

		return responseEntity.getBody();
	}

	private ProductDTO getProductById(final int id, final TokenDTO token) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", "Bearer " + token.getToken());
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		final ResponseEntity<ProductDTO> responseEntity = this.restTemplate.exchange(//
				this.getServerUrl().concat("/admin/products/" + id), //
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
