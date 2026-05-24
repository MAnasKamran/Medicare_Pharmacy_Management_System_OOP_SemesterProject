package com.pharmacy.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public abstract class Entity implements Serializable, Comparable<Entity> {
    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Entity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Entity(int id) {
        this();
        this.id = id;
    }

    public abstract String getDisplayName();
    public abstract boolean isValid();

    public final String getSummary() {
        return "[" + getClass().getSimpleName() + " #" + id + "] " + getDisplayName();
    }

    @Override
    public int compareTo(Entity other) {
        return Integer.compare(this.id, other.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Entity other)) return false;
        return this.id == other.id && this.getClass() == other.getClass();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    public int           getId()          { return id; }
    public void          setId(int id)    { this.id = id; this.updatedAt = LocalDateTime.now(); }
    public LocalDateTime getCreatedAt()   { return createdAt; }
    public void          setCreatedAt(LocalDateTime v) { this.createdAt = v; }
    public LocalDateTime getUpdatedAt()   { return updatedAt; }
    public void          setUpdatedAt(LocalDateTime v) { this.updatedAt = v; }
}
