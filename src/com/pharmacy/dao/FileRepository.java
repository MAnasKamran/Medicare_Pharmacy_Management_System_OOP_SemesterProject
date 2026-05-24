package com.pharmacy.dao;

import com.pharmacy.model.Entity;
import com.pharmacy.util.FileStore;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class FileRepository<T extends Entity> implements Repository<T, Integer> {

    protected final Map<Integer, T> store = new LinkedHashMap<>();

    private final AtomicInteger idSeq    = new AtomicInteger(1);
    private final String        fileName;

    protected FileRepository(String fileName) {
        this.fileName = fileName;
        load();
    }

    @Override
    public T save(T entity) {
        if (entity == null) throw new IllegalArgumentException("Entity cannot be null");
        if (!entity.isValid()) throw new IllegalArgumentException("Invalid entity: " + entity.getDisplayName());
        int id = idSeq.getAndIncrement();
        entity.setId(id);
        store.put(id, entity);
        persist();
        return entity;
    }

    @Override
    public void update(T entity) {
        if (entity == null || entity.getId() <= 0)
            throw new IllegalArgumentException("Entity has no ID — call save() first");
        if (!store.containsKey(entity.getId()))
            throw new NoSuchElementException("Entity not found: id=" + entity.getId());
        store.put(entity.getId(), entity);
        persist();
    }

    @Override
    public Optional<T> findById(Integer id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<T> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(store.values()));
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean removed = store.remove(id) != null;
        if (removed) persist();
        return removed;
    }

    @Override
    public int count() {
        return store.size();
    }

    // ── Helpers ───────────────────────────────────────────────

    protected void persist() {
        FileStore.save(fileName, store.values());
    }

    @SuppressWarnings("unchecked")
    protected void load() {
        List<?> raw = FileStore.load(fileName);
        for (Object obj : raw) {
            T entity = (T) obj;
            store.put(entity.getId(), entity);
            if (entity.getId() >= idSeq.get()) {
                idSeq.set(entity.getId() + 1);
            }
        }
    }

    /** Filter the in-memory store with a predicate. */
    protected List<T> filter(Predicate<T> predicate) {
        return store.values().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
}
