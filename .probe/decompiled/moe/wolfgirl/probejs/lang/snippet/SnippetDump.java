package moe.wolfgirl.probejs.lang.snippet;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import moe.wolfgirl.probejs.ProbeJS;
import moe.wolfgirl.probejs.plugin.ProbeJSPlugin;

public class SnippetDump {

    public List<Snippet> snippets = new ArrayList();

    public Snippet snippet(String name) {
        Snippet snippet = new Snippet(name);
        this.snippets.add(snippet);
        return snippet;
    }

    public void fromDocs() {
        ProbeJSPlugin.forEachPlugin(plugin -> plugin.addVSCodeSnippets(this));
    }

    public void writeTo(Path path) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(path);
        try {
            JsonWriter jsonWriter = ProbeJS.GSON_WRITER.newJsonWriter(writer);
            jsonWriter.setIndent("    ");
            JsonObject compiled = new JsonObject();
            for (Snippet snippet : this.snippets) {
                compiled.add(snippet.name, snippet.compile());
            }
            ProbeJS.GSON_WRITER.toJson(compiled, JsonObject.class, jsonWriter);
        } catch (Throwable var8) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if (writer != null) {
            writer.close();
        }
    }
}