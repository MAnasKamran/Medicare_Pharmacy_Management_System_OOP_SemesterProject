package com.pharmacy.util;

import com.pharmacy.model.User;

public final class SessionManager {
    private static volatile SessionManager instance;

    private User    currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) instance = new SessionManager();
            }
        }
        return instance;
    }

    public void login(User user) {
        this.currentUser = user;
        AuditLogger.log("LOGIN", user.getUsername() + " logged in");
    }

    public void logout() {
        if (currentUser != null) {
            AuditLogger.log("LOGOUT", currentUser.getUsername() + " logged out");
        }
        currentUser = null;
    }

    public boolean isLoggedIn()    { return currentUser != null; }
    public User    getCurrentUser(){ return currentUser; }
    public boolean isAdmin()       { return currentUser != null && currentUser.isAdmin(); }
    public boolean isPharmacist()  { return currentUser != null && currentUser.isPharmacist(); }

    public void requireLogin() {
        if (!isLoggedIn()) throw new SecurityException("Not logged in");
    }

    public void requireAdmin() {
        requireLogin();
        if (!isAdmin()) throw new SecurityException("Administrator access required");
    }

//    public void requirePharmacistOrAdmin() {
//        requireLogin();
//        if (!isAdmin() && !isPharmacist())
//            throw new SecurityException("Pharmacist or admin access required");
//    }
}
