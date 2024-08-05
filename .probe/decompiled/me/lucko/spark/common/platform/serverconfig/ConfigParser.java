package me.lucko.spark.common.platform.serverconfig;

import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Map;

public interface ConfigParser {

    JsonElement load(String var1, ExcludedConfigFilter var2) throws IOException;

    default Map<String, Object> parse(Path file) throws IOException {
        if (!Files.exists(file, new LinkOption[0])) {
            return null;
        } else {
            BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
            Map var3;
            try {
                var3 = this.parse(reader);
            } catch (Throwable var6) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (reader != null) {
                reader.close();
            }
            return var3;
        }
    }

    Map<String, Object> parse(BufferedReader var1) throws IOException;
}