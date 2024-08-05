package moe.wolfgirl.probejs.lang.snippet;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import moe.wolfgirl.probejs.lang.snippet.parts.Choice;
import moe.wolfgirl.probejs.lang.snippet.parts.Enumerable;
import moe.wolfgirl.probejs.lang.snippet.parts.Literal;
import moe.wolfgirl.probejs.lang.snippet.parts.SnippetPart;
import moe.wolfgirl.probejs.lang.snippet.parts.TabStop;
import moe.wolfgirl.probejs.lang.snippet.parts.Variable;
import moe.wolfgirl.probejs.utils.JsonUtils;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

public class Snippet {

    public final String name;

    final List<String> prefixes = new ArrayList();

    final List<List<SnippetPart>> allParts = new ArrayList();

    String description;

    public Snippet(String name) {
        this.name = name;
        this.allParts.add(new ArrayList());
    }

    public Snippet prefix(String prefix) {
        this.prefixes.add(prefix);
        return this;
    }

    public Snippet newline() {
        this.allParts.add(new ArrayList());
        return this;
    }

    public Snippet description(String description) {
        this.description = description;
        return this;
    }

    public Snippet literal(String content) {
        this.getRecent().add(new Literal(content));
        return this;
    }

    public Snippet tabStop() {
        return this.tabStop(-1);
    }

    public Snippet tabStop(int enumeration) {
        return this.tabStop(enumeration, null);
    }

    public Snippet tabStop(int enumeration, String defaultValue) {
        TabStop stop = new TabStop(defaultValue);
        stop.enumeration = enumeration;
        this.getRecent().add(stop);
        return this;
    }

    public Snippet choices(Collection<String> choices) {
        return this.choices(-1, choices);
    }

    public Snippet choices(int enumeration, Collection<String> choices) {
        Choice choice = new Choice(choices);
        choice.enumeration = enumeration;
        this.getRecent().add(choice);
        return this;
    }

    public Snippet variable(Variable variable) {
        this.getRecent().add(variable);
        return this;
    }

    public <T> Snippet registry(ResourceKey<Registry<T>> registry) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return this;
        } else {
            RegistryAccess access = server.registryAccess();
            Registry<T> reg = (Registry<T>) access.registry(registry).orElse(null);
            return reg == null ? this : this.choices((Collection<String>) reg.keySet().stream().map(ResourceLocation::toString).collect(Collectors.toSet()));
        }
    }

    private List<SnippetPart> getRecent() {
        return (List<SnippetPart>) this.allParts.get(this.allParts.size() - 1);
    }

    public JsonObject compile() {
        Set<Integer> indexes = new IntArraySet(256);
        List<Enumerable> toBeIndexed = new ArrayList(64);
        for (List<SnippetPart> parts : this.allParts) {
            for (SnippetPart part : parts) {
                if (part instanceof Enumerable) {
                    Enumerable enumerable = (Enumerable) part;
                    if (enumerable.enumeration == -1) {
                        toBeIndexed.add(enumerable);
                    } else {
                        indexes.add(enumerable.enumeration);
                    }
                }
            }
        }
        int start = 1;
        for (Enumerable enumerable : toBeIndexed) {
            while (indexes.contains(start)) {
                start++;
            }
            enumerable.enumeration = start++;
        }
        List<String> lines = new ArrayList();
        for (List<SnippetPart> parts : this.allParts) {
            StringBuilder content = new StringBuilder();
            for (SnippetPart partx : parts) {
                content.append(partx.format());
            }
            lines.add(content.toString());
        }
        if (!indexes.contains(0)) {
            int last = lines.size() - 1;
            lines.set(last, (String) lines.get(last) + "$0");
        }
        JsonObject object = new JsonObject();
        if (this.prefixes.isEmpty()) {
            throw new RuntimeException("Must have at least one prefix for the snippet %s!".formatted(this.name));
        } else {
            object.add("prefix", JsonUtils.asStringArray(this.prefixes));
            object.add("body", JsonUtils.asStringArray(lines));
            if (this.description != null) {
                object.addProperty("description", this.description);
            }
            return object;
        }
    }

    public List<String> getPrefixes() {
        return this.prefixes;
    }
}