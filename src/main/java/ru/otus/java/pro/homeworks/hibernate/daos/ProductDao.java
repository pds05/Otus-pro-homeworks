package ru.otus.java.pro.homeworks.hibernate.daos;

import ru.otus.java.pro.homeworks.hibernate.entities.Product;
import ru.otus.java.pro.homeworks.hibernate.entities.ProductType;

import java.util.List;
import java.util.Optional;

public interface ProductDao extends CrudDao<Product> {

    List<Product> findByProductType(String productType);

    Optional<Product> findByTitle(String title);

    List<ProductType> findProductTypes();

    Optional<ProductType> findProductTypeByTitle(String title);

}
