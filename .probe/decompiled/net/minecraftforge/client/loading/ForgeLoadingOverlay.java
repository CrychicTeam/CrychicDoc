package net.minecraftforge.client.loading;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.BufferVertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.LoadingOverlay;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadInstance;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.StartupMessageManager;
import net.minecraftforge.fml.earlydisplay.DisplayWindow;
import net.minecraftforge.fml.earlydisplay.ColourScheme.Colour;
import net.minecraftforge.fml.loading.progress.ProgressMeter;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30C;

public class ForgeLoadingOverlay extends LoadingOverlay {

    private final Minecraft minecraft;

    private final ReloadInstance reload;

    private final Consumer<Optional<Throwable>> onFinish;

    private final DisplayWindow displayWindow;

    private final ProgressMeter progress;

    private long fadeOutStart = -1L;

    public ForgeLoadingOverlay(Minecraft mc, ReloadInstance reloader, Consumer<Optional<Throwable>> errorConsumer, DisplayWindow displayWindow) {
        super(mc, reloader, errorConsumer, false);
        this.minecraft = mc;
        this.reload = reloader;
        this.onFinish = errorConsumer;
        this.displayWindow = displayWindow;
        displayWindow.addMojangTexture(mc.getTextureManager().getTexture(new ResourceLocation("textures/gui/title/mojangstudios.png")).getId());
        this.progress = StartupMessageManager.prependProgressBar("Minecraft Progress", 100);
    }

    public static Supplier<LoadingOverlay> newInstance(Supplier<Minecraft> mc, Supplier<ReloadInstance> ri, Consumer<Optional<Throwable>> handler, DisplayWindow window) {
        return () -> new ForgeLoadingOverlay((Minecraft) mc.get(), (ReloadInstance) ri.get(), handler, window);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        long millis = Util.getMillis();
        float fadeouttimer = this.fadeOutStart > -1L ? (float) (millis - this.fadeOutStart) / 1000.0F : -1.0F;
        this.progress.setAbsolute(Mth.clamp((int) (this.reload.getActualProgress() * 100.0F), 0, 100));
        float fade = 1.0F - Mth.clamp(fadeouttimer - 1.0F, 0.0F, 1.0F);
        Colour colour = this.displayWindow.context().colourScheme().background();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, fade);
        if (fadeouttimer >= 1.0F) {
            if (this.minecraft.screen != null) {
                this.minecraft.screen.render(graphics, 0, 0, partialTick);
            }
            this.displayWindow.render(255);
        } else {
            GlStateManager._clearColor(colour.redf(), colour.greenf(), colour.bluef(), 1.0F);
            GlStateManager._clear(16384, Minecraft.ON_OSX);
            this.displayWindow.render(255);
        }
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 771);
        int fbWidth = this.minecraft.getWindow().getWidth();
        int fbHeight = this.minecraft.getWindow().getHeight();
        GL30C.glViewport(0, 0, fbWidth, fbHeight);
        int twidth = this.displayWindow.context().width();
        int theight = this.displayWindow.context().height();
        float wscale = (float) fbWidth / (float) twidth;
        float hscale = (float) fbHeight / (float) theight;
        float scale = (float) this.displayWindow.context().scale() * Math.min(wscale, hscale) / 2.0F;
        float wleft = Mth.clamp((float) fbWidth * 0.5F - scale * (float) twidth, 0.0F, (float) fbWidth);
        float wtop = Mth.clamp((float) fbHeight * 0.5F - scale * (float) theight, 0.0F, (float) fbHeight);
        float wright = Mth.clamp((float) fbWidth * 0.5F + scale * (float) twidth, 0.0F, (float) fbWidth);
        float wbottom = Mth.clamp((float) fbHeight * 0.5F + scale * (float) theight, 0.0F, (float) fbHeight);
        GlStateManager.glActiveTexture(33984);
        RenderSystem.disableCull();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, fade);
        RenderSystem.getModelViewMatrix().identity();
        RenderSystem.setProjectionMatrix(new Matrix4f().setOrtho(0.0F, (float) fbWidth, 0.0F, (float) fbHeight, 0.1F, -0.1F), VertexSorting.ORTHOGRAPHIC_Z);
        RenderSystem.setShader(GameRenderer::m_172811_);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        addQuad(bufferbuilder, 0.0F, (float) fbWidth, wtop, (float) fbHeight, colour, fade);
        addQuad(bufferbuilder, 0.0F, (float) fbWidth, 0.0F, wtop, colour, fade);
        addQuad(bufferbuilder, 0.0F, wleft, wtop, wbottom, colour, fade);
        addQuad(bufferbuilder, wright, (float) fbWidth, wtop, wbottom, colour, fade);
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 771);
        RenderSystem.setShader(GameRenderer::m_172820_);
        RenderSystem.setShaderTexture(0, this.displayWindow.getFramebufferTextureId());
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.m_5483_((double) wleft, (double) wbottom, 0.0).uv(0.0F, 0.0F).color(1.0F, 1.0F, 1.0F, fade).endVertex();
        bufferbuilder.m_5483_((double) wright, (double) wbottom, 0.0).uv(1.0F, 0.0F).color(1.0F, 1.0F, 1.0F, fade).endVertex();
        bufferbuilder.m_5483_((double) wright, (double) wtop, 0.0).uv(1.0F, 1.0F).color(1.0F, 1.0F, 1.0F, fade).endVertex();
        bufferbuilder.m_5483_((double) wleft, (double) wtop, 0.0).uv(0.0F, 1.0F).color(1.0F, 1.0F, 1.0F, fade).endVertex();
        GL30C.glTexParameterIi(3553, 10241, 9728);
        GL30C.glTexParameterIi(3553, 10240, 9728);
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        if (fadeouttimer >= 2.0F) {
            this.minecraft.setOverlay(null);
            this.displayWindow.close();
        }
        if (this.fadeOutStart == -1L && this.reload.isDone()) {
            this.progress.complete();
            this.fadeOutStart = Util.getMillis();
            try {
                this.reload.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable var23) {
                this.onFinish.accept(Optional.of(var23));
            }
            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, this.minecraft.getWindow().getGuiScaledWidth(), this.minecraft.getWindow().getGuiScaledHeight());
            }
        }
    }

    private static void addQuad(BufferVertexConsumer bufferbuilder, float x0, float x1, float y0, float y1, Colour colour, float fade) {
        bufferbuilder.vertex((double) x0, (double) y0, 0.0).color(colour.redf(), colour.greenf(), colour.bluef(), fade).endVertex();
        bufferbuilder.vertex((double) x0, (double) y1, 0.0).color(colour.redf(), colour.greenf(), colour.bluef(), fade).endVertex();
        bufferbuilder.vertex((double) x1, (double) y1, 0.0).color(colour.redf(), colour.greenf(), colour.bluef(), fade).endVertex();
        bufferbuilder.vertex((double) x1, (double) y0, 0.0).color(colour.redf(), colour.greenf(), colour.bluef(), fade).endVertex();
    }
}