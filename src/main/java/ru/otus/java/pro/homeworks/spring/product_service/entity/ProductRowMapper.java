package ru.otus.java.pro.homeworks.spring.product_service.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getLong("ID"))
                .title(rs.getString("TITLE"))
                .categoryId(rs.getLong("CATEGORY_ID"))
                .category(new ProductCategory(
                        rs.getLong("CATEGORY_ID"),
                        rs.getString("CATEGORY_TITLE")))
                .price(rs.getBigDecimal("PRICE"))
                .details(new ProductDetails(
                        rs.getLong("ID"),
                        rs.getString("DETAILS_DESCRIPTION"),
                        rs.getString("DETAILS_PROVIDER"),
                        rs.getTimestamp("DETAILS_DELIVERY_DATE").toLocalDateTime()
                ))
                .build();
    }
}
