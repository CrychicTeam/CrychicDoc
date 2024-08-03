package net.minecraft.server.packs.resources;

import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.util.GsonHelper;

public interface ResourceMetadata {

    ResourceMetadata EMPTY = new ResourceMetadata() {

        @Override
        public <T> Optional<T> getSection(MetadataSectionSerializer<T> p_215584_) {
            return Optional.empty();
        }
    };

    IoSupplier<ResourceMetadata> EMPTY_SUPPLIER = () -> EMPTY;

    static ResourceMetadata fromJsonStream(InputStream inputStream0) throws IOException {
        BufferedReader $$1 = new BufferedReader(new InputStreamReader(inputStream0, StandardCharsets.UTF_8));
        ResourceMetadata var3;
        try {
            final JsonObject $$2 = GsonHelper.parse($$1);
            var3 = new ResourceMetadata() {

                @Override
                public <T> Optional<T> getSection(MetadataSectionSerializer<T> p_215589_) {
                    String $$1 = p_215589_.getMetadataSectionName();
                    return $$2.has($$1) ? Optional.of(p_215589_.fromJson(GsonHelper.getAsJsonObject($$2, $$1))) : Optional.empty();
                }
            };
        } catch (Throwable var5) {
            try {
                $$1.close();
            } catch (Throwable var4) {
                var5.addSuppressed(var4);
            }
            throw var5;
        }
        $$1.close();
        return var3;
    }

    <T> Optional<T> getSection(MetadataSectionSerializer<T> var1);
}