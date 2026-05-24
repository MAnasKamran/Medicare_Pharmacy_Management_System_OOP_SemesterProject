package com.pharmacy.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class FileStore {
    private static final Logger LOG = Logger.getLogger(FileStore.class.getName());
    private static final String DATA_DIR = "data";

    private FileStore() {}

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Cannot create data directory", e);
        }
    }

    public static <T extends Serializable> void save(String name, Collection<T> data) {
        Path path = Paths.get(DATA_DIR, name + ".dat");
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(path.toFile())))) {
            oos.writeObject(new ArrayList<>(data));
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not save " + name, e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> List<T> load(String name) {
        Path path = Paths.get(DATA_DIR, name + ".dat");
        if (!Files.exists(path)) return new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(path.toFile())))) {
            Object obj = ois.readObject();
            if (obj instanceof List<?>) return (List<T>) obj;
        } catch (IOException | ClassNotFoundException e) {
            LOG.log(Level.WARNING, "Could not load " + name, e);
        }
        return new ArrayList<>();
    }

    public static void appendLine(String name, String line) {
        Path path = Paths.get(DATA_DIR, name + ".log");
        try (PrintWriter pw = new PrintWriter(new FileWriter(path.toFile(), true))) {
            pw.println(line);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not append to " + name, e);
        }
    }

    public static List<String> readLines(String name) {
        Path path = Paths.get(DATA_DIR, name + ".log");
        if (!Files.exists(path)) return new ArrayList<>();
        try {
            return Files.readAllLines(path);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Could not read " + name, e);
            return new ArrayList<>();
        }
    }
}
