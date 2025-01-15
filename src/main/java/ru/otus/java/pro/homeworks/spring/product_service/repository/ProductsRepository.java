package ru.otus.java.pro.homeworks.spring.product_service.repository;


import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.java.pro.homeworks.spring.product_service.entity.Product;
import ru.otus.java.pro.homeworks.spring.product_service.entity.ProductRowMapper;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends ListCrudRepository<Product, Long> {

    Optional<Product> findByTitle(String title);

    @Query(value = "select distinct " +
            "p.*, " +
            "pd.PROVIDER as DETAILS_PROVIDER, " +
            "pd.DESCRIPTION as DETAILS_DESCRIPTION," +
            "pd.PROVIDER as DETAILS_PROVIDER," +
            "pd.DELIVERY_DATE as DETAILS_DELIVERY_DATE," +
            "pc.ID as CATEGORY_ID," +
            "pc.TITLE as CATEGORY_TITLE " +
            "from products p " +
            "join product_details pd ON pd.product_id = p.ID " +
            "left join product_categories pc ON pc.id = p.category_id ",
            rowMapperClass = ProductRowMapper.class)
    List<Product> findAllWithDetails();

    @Query(value = "select distinct " +
            "p.*, " +
            "pd.PROVIDER as DETAILS_PROVIDER, " +
            "pd.DESCRIPTION as DETAILS_DESCRIPTION," +
            "pd.PROVIDER as DETAILS_PROVIDER," +
            "pd.DELIVERY_DATE as DETAILS_DELIVERY_DATE," +
            "pc.ID as CATEGORY_ID," +
            "pc.TITLE as CATEGORY_TITLE " +
            "from products p " +
            "join product_details pd ON pd.product_id = p.ID " +
            "left join product_categories pc ON pc.id = p.category_id " +
            "where p.ID = :id",
            rowMapperClass = ProductRowMapper.class)
    Optional<Product> findByIdWithDetail(@Param("id") Long productId);

    @Query(value = "select distinct " +
            "p.*, " +
            "pd.PROVIDER as DETAILS_PROVIDER, " +
            "pd.DESCRIPTION as DETAILS_DESCRIPTION," +
            "pd.PROVIDER as DETAILS_PROVIDER," +
            "pd.DELIVERY_DATE as DETAILS_DELIVERY_DATE," +
            "pc.ID as CATEGORY_ID," +
            "pc.TITLE as CATEGORY_TITLE " +
            "from products p " +
            "join product_details pd ON pd.product_id = p.ID " +
            "left join product_categories pc ON pc.id = p.category_id " +
            "where p.title = :title",
            rowMapperClass = ProductRowMapper.class)
    Optional<Product> findByTitleWithDetail(@Param("title") String title);
}
