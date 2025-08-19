package framework.utils;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Downloads {
    private final Path dir;

    public Downloads(Path dir) {
        this.dir = dir;
    }

    public void clear() {
        try {
            if (Files.exists(dir)) {
                Files.list(dir).forEach(p -> {
                    try { Files.delete(p); } catch (IOException ignored) {}
                });
            } else {
                Files.createDirectories(dir);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Path waitForFile(String name, long timeoutSeconds) {
        Path file = dir.resolve(name);
        long end = System.currentTimeMillis() + timeoutSeconds * 1000;
        while (System.currentTimeMillis() < end) {
            if (Files.exists(file)) {
                return file;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ignored) {
            }
        }
        throw new RuntimeException("File not found: " + name);
    }

    public List<String[]> readCsv(Path file) {
        List<String[]> rows = new ArrayList<>();
        try {
            Files.lines(file).forEach(line -> rows.add(line.split(",")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return rows;
    }

    public String readText(Path file) {
        try {
            return FileUtils.readFileToString(file.toFile(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
