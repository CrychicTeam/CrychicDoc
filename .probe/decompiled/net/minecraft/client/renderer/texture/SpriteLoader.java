package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.renderer.texture.atlas.SpriteResourceLoader;
import net.minecraft.client.resources.metadata.animation.AnimationMetadataSection;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.Mth;
import org.slf4j.Logger;

public class SpriteLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ResourceLocation location;

    private final int maxSupportedTextureSize;

    private final int minWidth;

    private final int minHeight;

    public SpriteLoader(ResourceLocation resourceLocation0, int int1, int int2, int int3) {
        this.location = resourceLocation0;
        this.maxSupportedTextureSize = int1;
        this.minWidth = int2;
        this.minHeight = int3;
    }

    public static SpriteLoader create(TextureAtlas textureAtlas0) {
        return new SpriteLoader(textureAtlas0.location(), textureAtlas0.maxSupportedTextureSize(), textureAtlas0.getWidth(), textureAtlas0.getHeight());
    }

    public SpriteLoader.Preparations stitch(List<SpriteContents> listSpriteContents0, int int1, Executor executor2) {
        int $$3 = this.maxSupportedTextureSize;
        Stitcher<SpriteContents> $$4 = new Stitcher<>($$3, $$3, int1);
        int $$5 = Integer.MAX_VALUE;
        int $$6 = 1 << int1;
        for (SpriteContents $$7 : listSpriteContents0) {
            $$5 = Math.min($$5, Math.min($$7.width(), $$7.height()));
            int $$8 = Math.min(Integer.lowestOneBit($$7.width()), Integer.lowestOneBit($$7.height()));
            if ($$8 < $$6) {
                LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] { $$7.name(), $$7.width(), $$7.height(), Mth.log2($$6), Mth.log2($$8) });
                $$6 = $$8;
            }
            $$4.registerSprite($$7);
        }
        int $$9 = Math.min($$5, $$6);
        int $$10 = Mth.log2($$9);
        int $$11;
        if ($$10 < int1) {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] { this.location, int1, $$10, $$9 });
            $$11 = $$10;
        } else {
            $$11 = int1;
        }
        try {
            $$4.stitch();
        } catch (StitcherException var16) {
            CrashReport $$14 = CrashReport.forThrowable(var16, "Stitching");
            CrashReportCategory $$15 = $$14.addCategory("Stitcher");
            $$15.setDetail("Sprites", var16.getAllSprites().stream().map(p_249576_ -> String.format(Locale.ROOT, "%s[%dx%d]", p_249576_.name(), p_249576_.width(), p_249576_.height())).collect(Collectors.joining(",")));
            $$15.setDetail("Max Texture Size", $$3);
            throw new ReportedException($$14);
        }
        int $$16 = Math.max($$4.getWidth(), this.minWidth);
        int $$17 = Math.max($$4.getHeight(), this.minHeight);
        Map<ResourceLocation, TextureAtlasSprite> $$18 = this.getStitchedSprites($$4, $$16, $$17);
        TextureAtlasSprite $$19 = (TextureAtlasSprite) $$18.get(MissingTextureAtlasSprite.getLocation());
        CompletableFuture<Void> $$20;
        if ($$11 > 0) {
            $$20 = CompletableFuture.runAsync(() -> $$18.values().forEach(p_251202_ -> p_251202_.contents().increaseMipLevel($$11)), executor2);
        } else {
            $$20 = CompletableFuture.completedFuture(null);
        }
        return new SpriteLoader.Preparations($$16, $$17, $$11, $$19, $$18, $$20);
    }

    public static CompletableFuture<List<SpriteContents>> runSpriteSuppliers(List<Supplier<SpriteContents>> listSupplierSpriteContents0, Executor executor1) {
        List<CompletableFuture<SpriteContents>> $$2 = listSupplierSpriteContents0.stream().map(p_261395_ -> CompletableFuture.supplyAsync(p_261395_, executor1)).toList();
        return Util.sequence($$2).thenApply(p_252234_ -> p_252234_.stream().filter(Objects::nonNull).toList());
    }

    public CompletableFuture<SpriteLoader.Preparations> loadAndStitch(ResourceManager resourceManager0, ResourceLocation resourceLocation1, int int2, Executor executor3) {
        return CompletableFuture.supplyAsync(() -> SpriteResourceLoader.load(resourceManager0, resourceLocation1).list(resourceManager0), executor3).thenCompose(p_261390_ -> runSpriteSuppliers(p_261390_, executor3)).thenApply(p_261393_ -> this.stitch(p_261393_, int2, executor3));
    }

    @Nullable
    public static SpriteContents loadSprite(ResourceLocation resourceLocation0, Resource resource1) {
        AnimationMetadataSection $$2;
        try {
            $$2 = (AnimationMetadataSection) resource1.metadata().getSection(AnimationMetadataSection.SERIALIZER).orElse(AnimationMetadataSection.EMPTY);
        } catch (Exception var8) {
            LOGGER.error("Unable to parse metadata from {}", resourceLocation0, var8);
            return null;
        }
        NativeImage $$6;
        try {
            InputStream $$5 = resource1.open();
            try {
                $$6 = NativeImage.read($$5);
            } catch (Throwable var9) {
                if ($$5 != null) {
                    try {
                        $$5.close();
                    } catch (Throwable var7) {
                        var9.addSuppressed(var7);
                    }
                }
                throw var9;
            }
            if ($$5 != null) {
                $$5.close();
            }
        } catch (IOException var10) {
            LOGGER.error("Using missing texture, unable to load {}", resourceLocation0, var10);
            return null;
        }
        FrameSize $$10 = $$2.calculateFrameSize($$6.getWidth(), $$6.getHeight());
        if (Mth.isMultipleOf($$6.getWidth(), $$10.width()) && Mth.isMultipleOf($$6.getHeight(), $$10.height())) {
            return new SpriteContents(resourceLocation0, $$10, $$6, $$2);
        } else {
            LOGGER.error("Image {} size {},{} is not multiple of frame size {},{}", new Object[] { resourceLocation0, $$6.getWidth(), $$6.getHeight(), $$10.width(), $$10.height() });
            $$6.close();
            return null;
        }
    }

    private Map<ResourceLocation, TextureAtlasSprite> getStitchedSprites(Stitcher<SpriteContents> stitcherSpriteContents0, int int1, int int2) {
        Map<ResourceLocation, TextureAtlasSprite> $$3 = new HashMap();
        stitcherSpriteContents0.gatherSprites((p_251421_, p_250533_, p_251913_) -> $$3.put(p_251421_.name(), new TextureAtlasSprite(this.location, p_251421_, int1, int2, p_250533_, p_251913_)));
        return $$3;
    }

    public static record Preparations(int f_243669_, int f_244632_, int f_244353_, TextureAtlasSprite f_243912_, Map<ResourceLocation, TextureAtlasSprite> f_243807_, CompletableFuture<Void> f_244415_) {

        private final int width;

        private final int height;

        private final int mipLevel;

        private final TextureAtlasSprite missing;

        private final Map<ResourceLocation, TextureAtlasSprite> regions;

        private final CompletableFuture<Void> readyForUpload;

        public Preparations(int f_243669_, int f_244632_, int f_244353_, TextureAtlasSprite f_243912_, Map<ResourceLocation, TextureAtlasSprite> f_243807_, CompletableFuture<Void> f_244415_) {
            this.width = f_243669_;
            this.height = f_244632_;
            this.mipLevel = f_244353_;
            this.missing = f_243912_;
            this.regions = f_243807_;
            this.readyForUpload = f_244415_;
        }

        public CompletableFuture<SpriteLoader.Preparations> waitForUpload() {
            return this.readyForUpload.thenApply(p_249056_ -> this);
        }
    }
}