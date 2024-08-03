package moe.wolfgirl.probejs.events;

import dev.latvian.mods.kubejs.event.EventJS;
import java.util.function.Consumer;
import moe.wolfgirl.probejs.lang.snippet.Snippet;
import moe.wolfgirl.probejs.lang.snippet.SnippetDump;

public class SnippetGenerationEventJS extends EventJS {

    private final SnippetDump dump;

    public SnippetGenerationEventJS(SnippetDump dump) {
        this.dump = dump;
    }

    public void create(String name, Consumer<Snippet> handler) {
        handler.accept(this.dump.snippet(name));
    }
}