package com.pharmacy.dao;

import com.pharmacy.model.Role;
import com.pharmacy.model.User;
import com.pharmacy.util.PasswordUtil;

import java.util.List;
import java.util.Optional;

/**
 * User DAO — handles authentication and user management.
 * OOP: Singleton, Generics, File persistence
 */
public class UserDAO extends FileRepository<User> implements SearchableRepository<User> {

    private static UserDAO instance;

    private UserDAO() {
        super("users");
        seedIfEmpty();
    }

    public static synchronized UserDAO getInstance() {
        if (instance == null) instance = new UserDAO();
        return instance;
    }

    public Optional<User> authenticate(String username, String password) {
        return store.values().stream()
            .filter(u -> u.isActive()
                      && u.getUsername().equalsIgnoreCase(username.trim())
                      && PasswordUtil.verify(password, u.getPasswordHash()))
            .findFirst();
    }

    public Optional<User> findByUsername(String username) {
        return store.values().stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username))
            .findFirst();
    }

    public User createUser(User user, String plainPassword) {
        user.setPasswordHash(PasswordUtil.hash(plainPassword));
        return save(user);
    }

    public void changePassword(int userId, String newPlain) {
        findById(userId).ifPresent(u -> {
            u.setPasswordHash(PasswordUtil.hash(newPlain));
            update(u);
        });
    }

    @Override
    public List<User> search(String keyword) { return filter(u -> u.matches(keyword)); }

    public List<User> findActive() { return filter(User::isActive); }

    private void seedIfEmpty() {
        if (!store.isEmpty()) return;

        createUser(makeUser("admin",      "Administrator",  Role.ADMIN,       "admin@pharmacy.com"),      "admin123");
        createUser(makeUser("pharmacist", "Dr. Sara Khan",  Role.PHARMACIST,  "sara@pharmacy.com"),       "pharma123");
        createUser(makeUser("cashier",    "Ahmed Ali",      Role.CASHIER,     "ahmed@pharmacy.com"),      "cashier123");
    }

    private static User makeUser(String username, String fullName, Role role, String email) {
        User u = new User();
        u.setUsername(username);
        u.setFullName(fullName);
        u.setRole(role);
        u.setEmail(email);
        return u;
    }
}
