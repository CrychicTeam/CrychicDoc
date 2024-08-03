package cristelknight.wwoo.config.jankson;

import org.jetbrains.annotations.Nullable;

public interface Comments {

    void addComment(String var1, String var2);

    @Nullable
    String getComment(String var1);
}