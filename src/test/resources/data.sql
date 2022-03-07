insert into STB_PROFILE(name)
select
	'ADMIN-ROLE'
from dual
where not exists (
	select 
		'*'
	from STB_PROFILE
    where name = 'ADMIN-ROLE'
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