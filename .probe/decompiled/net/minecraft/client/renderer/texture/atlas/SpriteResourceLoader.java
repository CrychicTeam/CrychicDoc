package net.minecraft.client.renderer.texture.atlas;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

public class SpriteResourceLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final FileToIdConverter ATLAS_INFO_CONVERTER = new FileToIdConverter("atlases", ".json");

    private final List<SpriteSource> sources;

    private SpriteResourceLoader(List<SpriteSource> listSpriteSource0) {
        this.sources = listSpriteSource0;
    }

    public List<Supplier<SpriteContents>> list(ResourceManager resourceManager0) {
        final Map<ResourceLocation, SpriteSource.SpriteSupplier> $$1 = new HashMap();
        SpriteSource.Output $$2 = new SpriteSource.Output() {

            @Override
            public void add(ResourceLocation p_262067_, SpriteSource.SpriteSupplier p_261936_) {
                SpriteSource.SpriteSupplier $$2 = (SpriteSource.SpriteSupplier) $$1.put(p_262067_, p_261936_);
                if ($$2 != null) {
                    $$2.discard();
                }
            }

            @Override
            public void removeAll(Predicate<ResourceLocation> p_261939_) {
                Iterator<Entry<ResourceLocation, SpriteSource.SpriteSupplier>> $$1 = $$1.entrySet().iterator();
                while ($$1.hasNext()) {
                    Entry<ResourceLocation, SpriteSource.SpriteSupplier> $$2 = (Entry<ResourceLocation, SpriteSource.SpriteSupplier>) $$1.next();
                    if (p_261939_.test((ResourceLocation) $$2.getKey())) {
                        ((SpriteSource.SpriteSupplier) $$2.getValue()).discard();
                        $$1.remove();
                    }
                }
            }
        };
        this.sources.forEach(p_261747_ -> p_261747_.run(resourceManager0, $$2));
        Builder<Supplier<SpriteContents>> $$3 = ImmutableList.builder();
        $$3.add(MissingTextureAtlasSprite::m_246104_);
        $$3.addAll($$1.values());
        return $$3.build();
    }

    public static SpriteResourceLoader load(ResourceManager resourceManager0, ResourceLocation resourceLocation1) {
        ResourceLocation $$2 = ATLAS_INFO_CONVERTER.idToFile(resourceLocation1);
        List<SpriteSource> $$3 = new ArrayList();
        for (Resource $$4 : resourceManager0.getResourceStack($$2)) {
            try {
                BufferedReader $$5 = $$4.openAsReader();
                try {
                    Dynamic<JsonElement> $$6 = new Dynamic(JsonOps.INSTANCE, JsonParser.parseReader($$5));
                    $$3.addAll((Collection) SpriteSources.FILE_CODEC.parse($$6).getOrThrow(false, LOGGER::error));
                } catch (Throwable var10) {
                    if ($$5 != null) {
                        try {
                            $$5.close();
                        } catch (Throwable var9) {
                            var10.addSuppressed(var9);
                        }
                    }
                    throw var10;
                }
                if ($$5 != null) {
                    $$5.close();
                }
            } catch (Exception var11) {
                LOGGER.warn("Failed to parse atlas definition {} in pack {}", new Object[] { $$2, $$4.sourcePackId(), var11 });
            }
        }
        return new SpriteResourceLoader($$3);
    }
}