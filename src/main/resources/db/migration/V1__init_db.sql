create table role (
    id int8 not null,
    name varchar(255),
    primary key (id)
);

create table user_history (
     user_username varchar(255) not null,
     history varchar(255),
     history_key timestamp not null,
     primary key (user_username, history_key)
);

create table usr (
    username varchar(255) not null,
    first_name varchar(255),
    last_name varchar(255),
    password varchar(255),
    primary key (username)
);

create table usr_roles (
    user_username varchar(255) not null,
    roles_id int8 not null,
    primary key (user_username, roles_id)
);

alter table if exists user_history
    add constraint FKqd5ltc017ulkb1ckps62vvtf2
    foreign key (user_username) references usr;

alter table if exists usr_roles
    add constraint FKatlsh4dmfdkl5m7rdvhrls5if
    foreign key (roles_id) references role;

alter table if exists usr_roles
    add constraint FK408fxn12u9e7wa1s62ay77gkl
    foreign key (user_username) references usr;

insert into role
    values (1, 'ROLE_USER'),
           (2, 'ROLE_ADMIN');