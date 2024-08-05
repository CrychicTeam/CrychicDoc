package dev.latvian.mods.kubejs.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.packs.resources.Resource;

@FunctionalInterface
public interface ScriptSource {

    List<String> readSource(ScriptFileInfo var1) throws IOException;

    public interface FromPath extends ScriptSource {

        Path getPath(ScriptFileInfo var1);

        @Override
        default List<String> readSource(ScriptFileInfo info) throws IOException {
            return Files.readAllLines(this.getPath(info));
        }
    }

    public interface FromResource extends ScriptSource {

        Resource getResource(ScriptFileInfo var1) throws IOException;

        @Override
        default List<String> readSource(ScriptFileInfo info) throws IOException {
            ArrayList<String> list = new ArrayList();
            BufferedReader reader = this.getResource(info).openAsReader();
            ArrayList var5;
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line);
                }
                var5 = list;
            } catch (Throwable var7) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if (reader != null) {
                reader.close();
            }
            return var5;
        }
    }
}