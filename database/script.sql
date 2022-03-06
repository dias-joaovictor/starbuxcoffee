use `starbux-db`;

create table stb_user(
	ID int auto_increment,
    EMAIL varchar(50) not null,
    PASSWORD varchar(255) not null,
    constraint stb_user_pk primary key (ID),
    constraint stb_user_un unique (EMAIL)
);

create table stb_profile(
	ID int auto_increment,
    NAME varchar(50) not null,
    constraint stb_profile_pk primary key (ID),
    constraint stb_profile_un unique (NAME)
);

create table stb_user_profile(
	userid int,
    profileid int,
    constraint stb_user_profile_pk primary key (userid, profileid),
    constraint stb_user_profile_usr_fk foreign key (userid) references stb_user(id),
    constraint stb_user_profile_prf_fk foreign key (profileid) references stb_profile(id)
);


SELECT 
	@profile := 'ADMIN-ROLE';

insert into stb_profile(name)
select
	@profile
from dual
where not exists (
	select 
		'*'
	from stb_profile
    where name = @profile
);


insert into stb_user(EMAIL, PASSWORD)
select 
	'joao.dias@bestseller.com',
    '$2a$10$Fc7hWIicAdVTjPVCz3KVY.ywt3G3tLYZwy8yLfJtGmXzeMI3N58Ra'
from dual
where not exists (
	select
		'*'
	from stb_user
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
	from stb_user
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
	from stb_user
    where email = 'admin@bestseller.com'
);

insert into stb_user_profile(userid, profileid)
select
	stb_user.id,
    stb_profile.id
from stb_user, stb_profile
where not exists (
	select 
		'*'
	from stb_user_profile
    where stb_user_profile.userid = stb_user.id
)
	and stb_profile.name = 'ADMIN-ROLE';

