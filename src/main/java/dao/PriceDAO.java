package dao;

import model.entity.Price;

import java.util.List;


public interface PriceDAO {
    /**
     * Find all PRICES in DB
     */
    List<Price> findAll();

    /**
     * Find PRICE by ID
     */
    Price findById(Long id);

    /**
     * Insert new PRICE
     */
    Price create(Price price);

    /**
     * Update PRICE
     */
    Price update(Price price);

    /**
     * Delete PRICE
     */
    void delete(Price price);
}
