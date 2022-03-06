use `starbux-db`;

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
	expirationDate datetime null,
    expiresAt datetime not null,
    createdAt datetime not null,
    updatedAt datetime null,
    constraint stb_cart_pk primary key (id)
);


SELECT 
	@profile := 'ADMIN-ROLE';

insert into STB_PROFILE(name)
select
	@profile
from dual
where not exists (
	select 
		'*'
	from STB_PROFILE
    where name = @profile
);


insert into STB_USER(EMAIL, PASSWORD)
select 
	'joao.dias@bestseller.com',
    '$2a$10$Fc7hWIicAdVTjPVCz3KVY.ywt3G3tLYZwy8yLfJtGmXzeMI3N58Ra'
from dual
where not exists (
	select
		'*'
	from STB_USER
    where email = 'joao.dias@bestseller.com'
)
union all
select 
	'tl@bestseller.com',
    '$2a$10$0HHGDTeTrW.4F1c08y8H9e9qoOxWfOCuvVJ37mPOv5LiW9r2EkCve'
from dual
where not exists (
	select
		'*'
	from STB_USER
    where email = 'tl@bestseller.com'
)
union all
select 
	'admin@bestseller.com',
    '$2a$10$dk9YZefWunRuy.jlXfLUf.3tzub25Wp7JMYrHGE9yqqZ.yuUL9diS'
from dual
where not exists (
	select
		'*'
	from STB_USER
    where email = 'admin@bestseller.com'
);

insert into STB_USER_PROFILE(userid, profileid)
select
	STB_USER.id,
    STB_PROFILE.id
from STB_USER, STB_PROFILE
where not exists (
	select 
		'*'
	from STB_USER_PROFILE
    where STB_USER_PROFILE.userid = STB_USER.id
)
	and STB_PROFILE.name = 'ADMIN-ROLE';

