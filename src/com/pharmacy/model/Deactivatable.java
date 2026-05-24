package com.pharmacy.model;

public interface Deactivatable {
    boolean isActive();
    void    setActive(boolean active);
    default void deactivate() { setActive(false); }
    default void activate()   { setActive(true);  }
}
