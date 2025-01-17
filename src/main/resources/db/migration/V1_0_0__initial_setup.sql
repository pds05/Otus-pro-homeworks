CREATE TABLE IF NOT EXISTS PRODUCT_CATEGORIES
(
    ID    serial primary key,
    TITLE varchar(50) not null
);
CREATE TABLE IF NOT EXISTS PRODUCTS
(
    ID          serial primary key,
    CATEGORY_ID serial      not null,
    TITLE       varchar(50) not null,
    PRICE       numeric(10, 2) check (PRICE > 0),
    CONSTRAINT fk_product_category FOREIGN KEY (CATEGORY_ID) references PRODUCT_CATEGORIES (ID) ON UPDATE CASCADE ON DELETE SET NULL
);
CREATE TABLE IF NOT EXISTS PRODUCT_DETAILS
(
    PRODUCT_ID    serial,
    DESCRIPTION   varchar(255),
    PROVIDER      varchar(50),
    DELIVERY_DATE timestamp default now(),
    CONSTRAINT fk_product FOREIGN KEY (PRODUCT_ID) references PRODUCTS (ID) ON UPDATE CASCADE ON DELETE CASCADE
);
