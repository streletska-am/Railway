package dao;

import model.entity.User;

import java.util.List;

public interface UserDAO {
    /**
     * Find all USERS in DB
     */
    List<User> findAll();

    /**
     * Find USER by ID
     */
    User findById(String id);

    /**
     * Find USER by login
     */
    User findByEmail(String login);

    /**
     * Insert new USER
     */
    User create(User user);

    /**
     * Update USER
     */
    User update(User user);

    /**
     * Delete USER
     */
    void delete(User user);
}
