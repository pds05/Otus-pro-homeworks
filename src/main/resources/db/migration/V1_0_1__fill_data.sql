INSERT INTO PRODUCT_CATEGORIES (TITLE)
VALUES ('Молочные продукты'),
       ('Хлеб и выпечка'),
       ('Овощи и фрукты');
INSERT INTO PRODUCTS (CATEGORY_ID, TITLE, PRICE)
VALUES (1, 'Молоко', 85.5),
       (1, 'Кефир', 90.2),
       (2, 'Батон', 45),
       (2, 'Пряник', 120),
       (3, 'Картофель', 36.4),
       (3, 'Помидоры', 322.5);
INSERT INTO PRODUCT_DETAILS (PRODUCT_ID, PROVIDER)
VALUES ((SELECT ID FROM PRODUCTS WHERE TITLE = 'Молоко'), 'Простоквашино'),
       ((SELECT ID FROM PRODUCTS WHERE TITLE = 'Кефир'), 'Домик в деревне'),
       ((SELECT ID FROM PRODUCTS WHERE TITLE = 'Батон'), 'Хлебный завод'),
       ((SELECT ID FROM PRODUCTS WHERE TITLE = 'Пряник'), 'Хлебный завод'),
       ((SELECT ID FROM PRODUCTS WHERE TITLE = 'Картофель'), 'Беларусь'),
       ((SELECT ID FROM PRODUCTS WHERE TITLE = 'Помидоры'), 'Беларусь');