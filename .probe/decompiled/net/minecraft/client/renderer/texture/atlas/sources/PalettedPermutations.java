package net.minecraft.client.renderer.texture.atlas.sources;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.atlas.SpriteSource;
import net.minecraft.client.renderer.texture.atlas.SpriteSourceType;
import net.minecraft.client.renderer.texture.atlas.SpriteSources;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import org.slf4j.Logger;

public class PalettedPermutations implements SpriteSource {

    static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<PalettedPermutations> CODEC = RecordCodecBuilder.create(p_266838_ -> p_266838_.group(Codec.list(ResourceLocation.CODEC).fieldOf("textures").forGetter(p_267300_ -> p_267300_.textures), ResourceLocation.CODEC.fieldOf("palette_key").forGetter(p_266732_ -> p_266732_.paletteKey), Codec.unboundedMap(Codec.STRING, ResourceLocation.CODEC).fieldOf("permutations").forGetter(p_267234_ -> p_267234_.permutations)).apply(p_266838_, PalettedPermutations::new));

    private final List<ResourceLocation> textures;

    private final Map<String, ResourceLocation> permutations;

    private final ResourceLocation paletteKey;

    private PalettedPermutations(List<ResourceLocation> listResourceLocation0, ResourceLocation resourceLocation1, Map<String, ResourceLocation> mapStringResourceLocation2) {
        this.textures = listResourceLocation0;
        this.permutations = mapStringResourceLocation2;
        this.paletteKey = resourceLocation1;
    }

    @Override
    public void run(ResourceManager resourceManager0, SpriteSource.Output spriteSourceOutput1) {
        Supplier<int[]> $$2 = Suppliers.memoize(() -> loadPaletteEntryFromImage(resourceManager0, this.paletteKey));
        Map<String, Supplier<IntUnaryOperator>> $$3 = new HashMap();
        this.permutations.forEach((p_267108_, p_266969_) -> $$3.put(p_267108_, Suppliers.memoize(() -> createPaletteMapping((int[]) $$2.get(), loadPaletteEntryFromImage(resourceManager0, p_266969_)))));
        for (ResourceLocation $$4 : this.textures) {
            ResourceLocation $$5 = f_266012_.idToFile($$4);
            Optional<Resource> $$6 = resourceManager0.m_213713_($$5);
            if ($$6.isEmpty()) {
                LOGGER.warn("Unable to find texture {}", $$5);
            } else {
                LazyLoadedImage $$7 = new LazyLoadedImage($$5, (Resource) $$6.get(), $$3.size());
                for (Entry<String, Supplier<IntUnaryOperator>> $$8 : $$3.entrySet()) {
                    ResourceLocation $$9 = $$4.withSuffix("_" + (String) $$8.getKey());
                    spriteSourceOutput1.add($$9, new PalettedPermutations.PalettedSpriteSupplier($$7, (Supplier<IntUnaryOperator>) $$8.getValue(), $$9));
                }
            }
        }
    }

    private static IntUnaryOperator createPaletteMapping(int[] int0, int[] int1) {
        if (int1.length != int0.length) {
            LOGGER.warn("Palette mapping has different sizes: {} and {}", int0.length, int1.length);
            throw new IllegalArgumentException();
        } else {
            Int2IntMap $$2 = new Int2IntOpenHashMap(int1.length);
            for (int $$3 = 0; $$3 < int0.length; $$3++) {
                int $$4 = int0[$$3];
                if (FastColor.ABGR32.alpha($$4) != 0) {
                    $$2.put(FastColor.ABGR32.transparent($$4), int1[$$3]);
                }
            }
            return p_267899_ -> {
                int $$2x = FastColor.ABGR32.alpha(p_267899_);
                if ($$2x == 0) {
                    return p_267899_;
                } else {
                    int $$3x = FastColor.ABGR32.transparent(p_267899_);
                    int $$4x = $$2.getOrDefault($$3x, FastColor.ABGR32.opaque($$3x));
                    int $$5 = FastColor.ABGR32.alpha($$4x);
                    return FastColor.ABGR32.color($$2x * $$5 / 255, $$4x);
                }
            };
        }
    }

    public static int[] loadPaletteEntryFromImage(ResourceManager resourceManager0, ResourceLocation resourceLocation1) {
        Optional<Resource> $$2 = resourceManager0.m_213713_(f_266012_.idToFile(resourceLocation1));
        if ($$2.isEmpty()) {
            LOGGER.error("Failed to load palette image {}", resourceLocation1);
            throw new IllegalArgumentException();
        } else {
            try {
                InputStream $$3 = ((Resource) $$2.get()).open();
                int[] var5;
                try (NativeImage $$4 = NativeImage.read($$3)) {
                    var5 = $$4.getPixelsRGBA();
                } catch (Throwable var10) {
                    if ($$3 != null) {
                        try {
                            $$3.close();
                        } catch (Throwable var7) {
                            var10.addSuppressed(var7);
                        }
                    }
                    throw var10;
                }
                if ($$3 != null) {
                    $$3.close();
                }
                return var5;
            } catch (Exception var11) {
                LOGGER.error("Couldn't load texture {}", resourceLocation1, var11);
                throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public SpriteSourceType type() {
        return SpriteSources.PALETTED_PERMUTATIONS;
    }

    static record PalettedSpriteSupplier(LazyLoadedImage f_266004_, Supplier<IntUnaryOperator> f_266059_, ResourceLocation f_265892_) implements SpriteSource.SpriteSupplier {

        private final LazyLoadedImage baseImage;

        private final Supplier<IntUnaryOperator> palette;

        private final ResourceLocation permutationLocation;

        PalettedSpriteSupplier(LazyLoadedImage f_266004_, Supplier<IntUnaryOperator> f_266059_, ResourceLocation f_265892_) {
            this.baseImage = f_266004_;
            this.palette = f_266059_;
            this.permutationLocation = f_265892_;
        }

        @Nullable
        public SpriteContents get() {
            Object var2;
            try {
                NativeImage $$0 = this.baseImage.get().mappedCopy((IntUnaryOperator) this.palette.get());
                return new SpriteContents(this.permutationLocation, new FrameSize($$0.getWidth(), $$0.getHeight()), $$0, AnimationMetadataSection.EMPTY);
            } catch (IllegalArgumentException | IOException var6) {
                PalettedPermutations.LOGGER.error("unable to apply palette to {}", this.permutationLocation, var6);
                var2 = null;
            } finally {
                this.baseImage.release();
            }
            return (SpriteContents) var2;
        }

        @Override
        public void discard() {
            this.baseImage.release();
        }
    }
}