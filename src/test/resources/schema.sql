create table STB_USER(
	ID int auto_increment,
    EMAIL varchar(50) not null,
    PASSWORD varchar(255) not null,
    constraint stb_user_pk primary key (ID),
    constraint stb_user_un unique (EMAIL)
);

create table STB_PROFILE(
	ID int auto_increment,
    NAME varchar(50) not null,
    constraint stb_profile_pk primary key (ID),
    constraint stb_profile_un unique (NAME)
);

create table STB_USER_PROFILE(
	userid int,
    profileid int,
    constraint stb_user_profile_pk primary key (userid, profileid),
    constraint stb_user_profile_usr_fk foreign key (userid) references STB_USER(id),
    constraint stb_user_profile_prf_fk foreign key (profileid) references STB_PROFILE(id)
);


create table STB_PRODUCT(
	id int auto_increment,
    type varchar(50) not null,
    name varchar(100) not null,
    description varchar(255) not null,
    priority int not null,
    deletedAt datetime null,
    createdAt datetime not null,
    updatedAt datetime null,
    constraint stb_product_pk primary key (id)
);

create table STB_PRICE(
	id int auto_increment,
    productId int not null,
    value decimal(19,2) not null,
    startValidDate datetime not null,
	expirationDate datetime null,
    createdAt datetime not null,
    updatedAt datetime null,
    constraint stb_price_pk primary key (id),
    constraint stb_price_fk foreign key (productId) references STB_PRODUCT(id)
);

create table STB_CART(
	id varchar(36) not null,
    customerId varchar(255) not null,
    expiresAt datetime not null,
    checkoutAt datetime null,
    createdAt datetime not null,
    updatedAt datetime null,
    constraint stb_cart_pk primary key (id)
);


create table STB_ORDER(
	id varchar(36) not null,
    cartId varchar(36) not null,
	totalPrice decimal(19,2) not null,
    finalPrice decimal(19,2) not null,
    createdAt datetime not null,
    updatedAt datetime null,
    constraint stb_order_pk primary key (id),
    constraint stb_order_cart_fk foreign key (cartId) references STB_CART(id)
);

create table STB_COMBO(
	id varchar(36) not null,
    orderId varchar(36) not null,
    priceId int not null,
    principalComboId varchar(36) null,
    createdAt datetime not null,
    updatedAt datetime null,
    constraint stb_combo_pk primary key (id),
    constraint stb_combo_order_fk foreign key (orderId) references STB_ORDER(id),
    constraint stb_combo_autoref_fk foreign key (principalComboId) references STB_COMBO(id)
);