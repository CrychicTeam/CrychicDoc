package net.minecraft.client.gui.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

public class LoadingOverlay extends Overlay {

    static final ResourceLocation MOJANG_STUDIOS_LOGO_LOCATION = new ResourceLocation("textures/gui/title/mojangstudios.png");

    private static final int LOGO_BACKGROUND_COLOR = FastColor.ARGB32.color(255, 239, 50, 61);

    private static final int LOGO_BACKGROUND_COLOR_DARK = FastColor.ARGB32.color(255, 0, 0, 0);

    private static final IntSupplier BRAND_BACKGROUND = () -> Minecraft.getInstance().options.darkMojangStudiosBackground().get() ? LOGO_BACKGROUND_COLOR_DARK : LOGO_BACKGROUND_COLOR;

    private static final int LOGO_SCALE = 240;

    private static final float LOGO_QUARTER_FLOAT = 60.0F;

    private static final int LOGO_QUARTER = 60;

    private static final int LOGO_HALF = 120;

    private static final float LOGO_OVERLAP = 0.0625F;

    private static final float SMOOTHING = 0.95F;

    public static final long FADE_OUT_TIME = 1000L;

    public static final long FADE_IN_TIME = 500L;

    private final Minecraft minecraft;

    private final ReloadInstance reload;

    private final Consumer<Optional<Throwable>> onFinish;

    private final boolean fadeIn;

    private float currentProgress;

    private long fadeOutStart = -1L;

    private long fadeInStart = -1L;

    public LoadingOverlay(Minecraft minecraft0, ReloadInstance reloadInstance1, Consumer<Optional<Throwable>> consumerOptionalThrowable2, boolean boolean3) {
        this.minecraft = minecraft0;
        this.reload = reloadInstance1;
        this.onFinish = consumerOptionalThrowable2;
        this.fadeIn = boolean3;
    }

    public static void registerTextures(Minecraft minecraft0) {
        minecraft0.getTextureManager().register(MOJANG_STUDIOS_LOGO_LOCATION, new LoadingOverlay.LogoTexture());
    }

    private static int replaceAlpha(int int0, int int1) {
        return int0 & 16777215 | int1 << 24;
    }

    @Override
    public void render(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = guiGraphics0.guiWidth();
        int $$5 = guiGraphics0.guiHeight();
        long $$6 = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = $$6;
        }
        float $$7 = this.fadeOutStart > -1L ? (float) ($$6 - this.fadeOutStart) / 1000.0F : -1.0F;
        float $$8 = this.fadeInStart > -1L ? (float) ($$6 - this.fadeInStart) / 500.0F : -1.0F;
        float $$10;
        if ($$7 >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(guiGraphics0, 0, 0, float3);
            }
            int $$9 = Mth.ceil((1.0F - Mth.clamp($$7 - 1.0F, 0.0F, 1.0F)) * 255.0F);
            guiGraphics0.fill(RenderType.guiOverlay(), 0, 0, $$4, $$5, replaceAlpha(BRAND_BACKGROUND.getAsInt(), $$9));
            $$10 = 1.0F - Mth.clamp($$7 - 1.0F, 0.0F, 1.0F);
        } else if (this.fadeIn) {
            if (this.minecraft.screen != null && $$8 < 1.0F) {
                this.minecraft.screen.render(guiGraphics0, int1, int2, float3);
            }
            int $$11 = Mth.ceil(Mth.clamp((double) $$8, 0.15, 1.0) * 255.0);
            guiGraphics0.fill(RenderType.guiOverlay(), 0, 0, $$4, $$5, replaceAlpha(BRAND_BACKGROUND.getAsInt(), $$11));
            $$10 = Mth.clamp($$8, 0.0F, 1.0F);
        } else {
            int $$13 = BRAND_BACKGROUND.getAsInt();
            float $$14 = (float) ($$13 >> 16 & 0xFF) / 255.0F;
            float $$15 = (float) ($$13 >> 8 & 0xFF) / 255.0F;
            float $$16 = (float) ($$13 & 0xFF) / 255.0F;
            GlStateManager._clearColor($$14, $$15, $$16, 1.0F);
            GlStateManager._clear(16384, Minecraft.ON_OSX);
            $$10 = 1.0F;
        }
        int $$18 = (int) ((double) guiGraphics0.guiWidth() * 0.5);
        int $$19 = (int) ((double) guiGraphics0.guiHeight() * 0.5);
        double $$20 = Math.min((double) guiGraphics0.guiWidth() * 0.75, (double) guiGraphics0.guiHeight()) * 0.25;
        int $$21 = (int) ($$20 * 0.5);
        double $$22 = $$20 * 4.0;
        int $$23 = (int) ($$22 * 0.5);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, $$10);
        guiGraphics0.blit(MOJANG_STUDIOS_LOGO_LOCATION, $$18 - $$23, $$19 - $$21, $$23, (int) $$20, -0.0625F, 0.0F, 120, 60, 120, 120);
        guiGraphics0.blit(MOJANG_STUDIOS_LOGO_LOCATION, $$18, $$19 - $$21, $$23, (int) $$20, 0.0625F, 60.0F, 120, 60, 120, 120);
        guiGraphics0.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        int $$24 = (int) ((double) guiGraphics0.guiHeight() * 0.8325);
        float $$25 = this.reload.getActualProgress();
        this.currentProgress = Mth.clamp(this.currentProgress * 0.95F + $$25 * 0.050000012F, 0.0F, 1.0F);
        if ($$7 < 1.0F) {
            this.drawProgressBar(guiGraphics0, $$4 / 2 - $$23, $$24 - 5, $$4 / 2 + $$23, $$24 + 5, 1.0F - Mth.clamp($$7, 0.0F, 1.0F));
        }
        if ($$7 >= 2.0F) {
            this.minecraft.setOverlay(null);
        }
        if (this.fadeOutStart == -1L && this.reload.isDone() && (!this.fadeIn || $$8 >= 2.0F)) {
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable var23) {
                this.onFinish.accept(Optional.of(var23));
            }
            this.fadeOutStart = Util.getMillis();
            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, guiGraphics0.guiWidth(), guiGraphics0.guiHeight());
            }
        }
    }

    private void drawProgressBar(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, float float5) {
        int $$6 = Mth.ceil((float) (int3 - int1 - 2) * this.currentProgress);
        int $$7 = Math.round(float5 * 255.0F);
        int $$8 = FastColor.ARGB32.color($$7, 255, 255, 255);
        guiGraphics0.fill(int1 + 2, int2 + 2, int1 + $$6, int4 - 2, $$8);
        guiGraphics0.fill(int1 + 1, int2, int3 - 1, int2 + 1, $$8);
        guiGraphics0.fill(int1 + 1, int4, int3 - 1, int4 - 1, $$8);
        guiGraphics0.fill(int1, int2, int1 + 1, int4, $$8);
        guiGraphics0.fill(int3, int2, int3 - 1, int4, $$8);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    static class LogoTexture extends SimpleTexture {

        public LogoTexture() {
            super(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
        }

        @Override
        protected SimpleTexture.TextureImage getTextureImage(ResourceManager resourceManager0) {
            VanillaPackResources $$1 = Minecraft.getInstance().getVanillaPackResources();
            IoSupplier<InputStream> $$2 = $$1.getResource(PackType.CLIENT_RESOURCES, LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION);
            if ($$2 == null) {
                return new SimpleTexture.TextureImage(new FileNotFoundException(LoadingOverlay.MOJANG_STUDIOS_LOGO_LOCATION.toString()));
            } else {
                try {
                    InputStream $$3 = $$2.get();
                    SimpleTexture.TextureImage var5;
                    try {
                        var5 = new SimpleTexture.TextureImage(new TextureMetadataSection(true, true), NativeImage.read($$3));
                    } catch (Throwable var8) {
                        if ($$3 != null) {
                            try {
                                $$3.close();
                            } catch (Throwable var7) {
                                var8.addSuppressed(var7);
                            }
                        }
                        throw var8;
                    }
                    if ($$3 != null) {
                        $$3.close();
                    }
                    return var5;
                } catch (IOException var9) {
                    return new SimpleTexture.TextureImage(var9);
                }
            }
        }
    }
}