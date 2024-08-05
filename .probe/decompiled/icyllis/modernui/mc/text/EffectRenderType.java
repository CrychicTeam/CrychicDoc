package icyllis.modernui.mc.text;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.opengl.GLBackendFormat;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import java.nio.ByteBuffer;
import java.util.Objects;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class EffectRenderType extends RenderType {

    private static GLTexture WHITE;

    private static final ImmutableList<RenderStateShard> STATES = ImmutableList.of(TextRenderType.RENDERTYPE_MODERN_TEXT_NORMAL, f_110139_, f_110113_, f_110158_, f_110152_, f_110155_, f_110117_, f_110123_, f_110148_, f_110114_, f_110130_);

    private static final ImmutableList<RenderStateShard> SEE_THROUGH_STATES = ImmutableList.of(f_173088_, f_110139_, f_110111_, f_110158_, f_110152_, f_110155_, f_110117_, f_110123_, f_110148_, f_110115_, f_110130_);

    private static final EffectRenderType TYPE = new EffectRenderType("modern_text_effect", 256, () -> {
        STATES.forEach(RenderStateShard::m_110185_);
        RenderSystem.setShaderTexture(0, WHITE.getHandle());
    }, () -> STATES.forEach(RenderStateShard::m_110188_));

    private static final EffectRenderType SEE_THROUGH_TYPE = new EffectRenderType("modern_text_effect_see_through", 256, () -> {
        SEE_THROUGH_STATES.forEach(RenderStateShard::m_110185_);
        RenderSystem.setShaderTexture(0, WHITE.getHandle());
    }, () -> SEE_THROUGH_STATES.forEach(RenderStateShard::m_110188_));

    private EffectRenderType(String name, int bufferSize, Runnable setupState, Runnable clearState) {
        super(name, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, bufferSize, false, true, setupState, clearState);
    }

    @Nonnull
    @RenderThread
    public static EffectRenderType getRenderType(boolean seeThrough) {
        if (WHITE == null) {
            makeWhiteTexture();
        }
        return seeThrough ? SEE_THROUGH_TYPE : TYPE;
    }

    @Nonnull
    @RenderThread
    public static EffectRenderType getRenderType(Font.DisplayMode mode) {
        throw new IllegalStateException();
    }

    private static void makeWhiteTexture() {
        DirectContext dContext = Core.requireDirectContext();
        GLBackendFormat format = GLBackendFormat.make(32856);
        int width = 2;
        int height = 2;
        WHITE = (GLTexture) dContext.getResourceProvider().createTexture(width, height, format, 1, 0, "MCTextEffect");
        Objects.requireNonNull(WHITE);
        MemoryStack stack = MemoryStack.stackPush();
        try {
            int colorType = 6;
            int bpp = ImageInfo.bytesPerPixel(colorType);
            ByteBuffer pixels = stack.malloc(width * height * bpp);
            while (pixels.hasRemaining()) {
                pixels.put((byte) -1);
            }
            pixels.flip();
            dContext.getDevice().writePixels(WHITE, 0, 0, width, height, colorType, colorType, width * bpp, MemoryUtil.memAddress(pixels));
        } catch (Throwable var9) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
            }
            throw var9;
        }
        if (stack != null) {
            stack.close();
        }
    }
}