package moe.wolfgirl.probejs.utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.function.Consumer;

public class FileUtils {

    public static void forEachFile(Path basePath, Consumer<Path> callback) throws IOException {
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(basePath);
        try {
            for (Path path : dirStream) {
                if (Files.isDirectory(path, new LinkOption[0])) {
                    forEachFile(path, callback);
                } else {
                    callback.accept(path);
                }
            }
        } catch (Throwable var6) {
            if (dirStream != null) {
                try {
                    dirStream.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if (dirStream != null) {
            dirStream.close();
        }
    }
}