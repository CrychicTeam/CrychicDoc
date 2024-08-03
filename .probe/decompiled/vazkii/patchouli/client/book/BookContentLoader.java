package vazkii.patchouli.client.book;

import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public interface BookContentLoader {

    void findFiles(Book var1, String var2, List<ResourceLocation> var3);

    @Nullable
    BookContentLoader.LoadResult loadJson(Book var1, ResourceLocation var2);

    static JsonElement streamToJson(InputStream stream) throws IOException {
        Reader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        JsonElement var2;
        try {
            var2 = GsonHelper.fromJson(BookRegistry.GSON, reader, JsonElement.class);
        } catch (Throwable var5) {
            try {
                reader.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }
            throw var5;
        }
        reader.close();
        return var2;
    }

    public static record LoadResult(JsonElement json, @Nullable String addedBy) {
    }
}