create table if not exists service_menu
(
    id                  int auto_increment
        primary key,
    name                varchar(255) null,
    price_per_session   double       null,
    total_sessions      int          null,
    schedule            varchar(255) null,
    duration_in_minutes int          null,
    description         text         null
);

create table if not exists exercises
(
    id                  int auto_increment
        primary key,
    name                varchar(255) null,
    duration_in_minutes int          null,
    description         text         null,
    service_menu_id     int          null,
    constraint exercises_ibfk_1
        foreign key (service_menu_id) references service_menu (id)
);

create index service_menu_id
    on exercises (service_menu_id);

create table if not exists users
(
    email            varchar(255) not null,
    password         varchar(255) null,
    name             varchar(255) null,
    phone_number     varchar(255) null,
    is_verified      tinyint(1)   null,
    credit_card_info varchar(255) null,
    token            varchar(255) null,
    token_expired_at bigint       null,
    primary key (email)
);

create table if not exists subscriptions
(
    id                 int auto_increment
        primary key,
    active             tinyint(1) default 0 not null,
    start_date         datetime             null,
    end_date           datetime             null,
    remaining_sessions int                  null,
    user_id            varchar(100)         null,
    service_menu_id    int                  null,
    total_sessions     int                  null,
    constraint subscriptions_ibfk_1
        foreign key (user_id) references users (email),
    constraint subscriptions_ibfk_2
        foreign key (service_menu_id) references service_menu (id)
);

create table if not exists payments
(
    id              varchar(255) not null,
    paid            tinyint(1)   null,
    payment_time    timestamp    null,
    otp_code        varchar(255) null,
    otp_expiration  bigint       null,
    amount          double       null,
    user_id         varchar(100) null,
    service_menu_id int          null,
    subscription_id int          null,
    primary key (id),
    constraint payments_ibfk_1
        foreign key (user_id) references users (email),
    constraint payments_ibfk_2
        foreign key (service_menu_id) references service_menu (id),
    constraint payments_ibfk_3
        foreign key (subscription_id) references subscriptions (id)
);

create index service_menu_id
    on payments (service_menu_id);

create index subscription_id
    on payments (subscription_id);

create index user_id
    on payments (user_id);

create index service_menu_id
    on subscriptions (service_menu_id);

create index user_id
    on subscriptions (user_id);


