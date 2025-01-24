create sequence users_seq increment BY 1 start with 10;
create sequence product_type_seq increment BY 1 start with 10;
create sequence products_seq increment BY 1 start with 10;
create sequence promotions_seq increment BY 1 start with 10;
create sequence orders_seq increment BY 1 start with 10;

create table if not exists users
(
    id       bigint default nextval('users_seq') primary key,
    username varchar(20) not null UNIQUE,
    password varchar(10) not null
);

create table if not exists user_contacts
(
    user_id      bigint primary key,
    email        varchar(255) UNIQUE,
    phone_number varchar(11) UNIQUE,
    CONSTRAINT fk_user FOREIGN KEY (user_id) references users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table if not exists product_type
(
    id    bigint default nextval('product_type_seq') primary key,
    title varchar(20) not null
);

create table if not exists products
(
    id       bigint default nextval('products_seq') primary key,
    title    varchar(20)    not null,
    price    numeric(10, 2) not null CHECK (price > 0),
    quantity int            not null CHECK (quantity >= 0),
    type_id  bigint,
    CONSTRAINT fk_product_type FOREIGN KEY (type_id) references product_type (id) ON UPDATE CASCADE ON DELETE SET NULL
);

create table if not exists promotions
(
    id       bigint DEFAULT nextval('promotions_seq') primary key,
    title    varchar(100)  not null,
    discount numeric(3, 1) not null CHECK (discount <= 100 and discount > 0)
);

create table if not exists products_promotions
(
    product_id    bigint,
    promotions_id bigint,
    CONSTRAINT pk_product_promotion PRIMARY KEY (product_id, promotions_id),
    CONSTRAINT fk_products FOREIGN KEY (product_id) REFERENCES products (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_products_promotions FOREIGN KEY (promotions_id) REFERENCES promotions (id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table if not exists orders
(
    id         bigint default nextval('orders_seq') primary key,
    user_id    bigint,
    order_date timestamp default now(),
    total_amount numeric(10, 2) not null,
    CONSTRAINT fk_users FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
);

create table if not exists orders_products
(
    order_id     bigint,
    product_id   bigint,
    quantity     int            not null CHECK (quantity >= 1),
    order_price  numeric(10, 2) not null,
    price_type   varchar(20)    not null,
    promotion_id bigint,
    CONSTRAINT pk_order_product PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_orders FOREIGN KEY (order_id) REFERENCES orders (id) ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_orders_products FOREIGN KEY (product_id) REFERENCES products (id) ON UPDATE CASCADE ON DELETE SET NULL,
    CONSTRAINT fk_orders_promotions FOREIGN KEY (promotion_id) REFERENCES promotions (id) ON UPDATE CASCADE ON DELETE SET NULL
);

