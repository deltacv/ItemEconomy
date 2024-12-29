package ru.themixray.itemeconomy;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class UnrealConfig extends HashMap<String, Object> {
    private static final Yaml yaml = new Yaml();

    private final File file;

    public UnrealConfig(Object plugin, File dataDirectory, String filename) {
        this(plugin, dataDirectory.toPath(), filename, filename);
    }

    public UnrealConfig(Object plugin, File dataDirectory, String filename, String default_filename) {
        this(plugin, dataDirectory.toPath(), filename, default_filename);
    }

    public UnrealConfig(Object plugin, Path dataDirectory, String filename) {
        this(plugin, dataDirectory, filename, filename);
    }

    public UnrealConfig(Object plugin, Path dataDirectory, String filename, String default_filename) {
        file = Paths.get(dataDirectory.toFile().getPath(), filename).toFile();

        if (!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdir();
        }

        if (!file.exists()) {
            try (InputStream stream = plugin.getClass().getClassLoader().getResourceAsStream(default_filename)) {
                assert stream != null;
                Files.copy(stream, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        reload();
    }

    public void reload() {
        try {
            clear();
            putAll(yaml.load(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            yaml.dump(this,new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String,Object> clone() {
        return new HashMap<>(this);
    }
}
