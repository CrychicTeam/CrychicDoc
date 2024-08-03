package journeymap.client.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.nio.IntBuffer;
import java.util.function.Supplier;
import journeymap.client.JourneymapClient;
import journeymap.client.log.JMLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11C;

public class RenderWrapper {

    public static final int GL_TEXTURE_2D = 3553;

    public static final int GL_TEXTURE_MIN_FILTER = 10241;

    public static final int GL_LINEAR = 9729;

    public static final int GL_TEXTURE_MAG_FILTER = 10240;

    public static final int GL_TEXTURE_WRAP_S = 10242;

    public static final int GL_REPEAT = 10497;

    public static final int GL_TEXTURE_WRAP_T = 10243;

    public static final int GL_SRC_ALPHA = 770;

    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;

    public static final int GL_ZERO = 0;

    public static final int GL_DEPTH_BUFFER_BIT = 256;

    public static final int GL_GREATER = 516;

    public static final int GL_NEAREST = 9728;

    public static final int GL_NO_ERROR = 0;

    public static final int GL_LEQUAL = 515;

    public static final int GL_GEQUAL = 518;

    public static final int GL_VIEWPORT = 2978;

    public static final int GL_BGRA = 32993;

    public static final int GL_RGBA = 6408;

    public static final int GL_CLAMP_TO_EDGE = 33071;

    public static final int GL_MIRRORED_REPEAT = 33648;

    public static final boolean errorCheck = JourneymapClient.getInstance().getCoreProperties().glErrorChecking.get();

    public static void setColor4f(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
        checkGLError("setColor4f");
    }

    public static void clearColor(float red, float green, float blue, float alpha) {
        RenderSystem.clearColor(red, green, blue, alpha);
        checkGLError("clearColor");
    }

    public static void enableBlend() {
        RenderSystem.enableBlend();
        checkGLError("enableBlend");
    }

    public static void disableBlend() {
        RenderSystem.disableBlend();
        checkGLError("disableBlend");
    }

    public static void defaultBlendFunc() {
        RenderSystem.defaultBlendFunc();
        checkGLError("defaultBlendFunc");
    }

    public static void enableTexture() {
        checkGLError("enableTexture");
    }

    public static void disableTexture() {
        checkGLError("disableTexture");
    }

    public static void enableDepthTest() {
        RenderSystem.enableDepthTest();
        checkGLError("enableDepthTest");
    }

    public static void disableDepthTest() {
        RenderSystem.disableDepthTest();
        checkGLError("disableDepthTest");
    }

    public static Matrix4f getProjectionMatrix() {
        Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
        checkGLError("getProjectionMatrix");
        return projectionMatrix;
    }

    public static void enableCull() {
        RenderSystem.enableCull();
        checkGLError("enableCull");
    }

    public static void disableCull() {
        RenderSystem.disableCull();
        checkGLError("disableCull");
    }

    public static int getError() {
        return GlStateManager._getError();
    }

    public static void depthMask(boolean flag) {
        RenderSystem.depthMask(flag);
        checkGLError("depthMask");
    }

    public static void getIntegerv(int pname, IntBuffer params) {
        GL11C.glGetIntegerv(pname, params);
        checkGLError("getIntegerv");
    }

    public static void texParameter(int target, int pname, int param) {
        RenderSystem.texParameter(target, pname, param);
        checkGLError("texParameter");
    }

    public static void blendFuncSeparate(int sfactorRGB, int dfactorRGB, int sfactorAlpha, int dfactorAlpha) {
        RenderSystem.blendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
        checkGLError("blendFuncSeparate");
    }

    public static void blendFunc(int sfactor, int dfactor) {
        RenderSystem.blendFunc(sfactor, dfactor);
        checkGLError("blendFunc");
    }

    public static void bindFramebuffer(int target, int framebuffer) {
        GlStateManager._glBindFramebuffer(target, framebuffer);
        checkGLError("bindFramebuffer");
    }

    public static void blitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
        GlStateManager._glBlitFrameBuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter);
        checkGLError("blitFramebuffer");
    }

    public static void setShader(Supplier<ShaderInstance> shaderInstanceSupplier) {
        RenderSystem.setShader(shaderInstanceSupplier);
        checkGLError("setShader");
    }

    public static void setShaderTexture(int index, int id) {
        RenderSystem.setShaderTexture(index, id);
        checkGLError("setShaderTexture");
    }

    public static void setShaderTexture(int index, ResourceLocation resourceLocation) {
        RenderSystem.setShaderTexture(index, resourceLocation);
        checkGLError("setShaderTexture");
    }

    public static void activeTexture(int texture) {
        RenderSystem.activeTexture(texture);
        checkGLError("activeTexture");
    }

    public static void bindTextureForSetup(int id) {
        RenderSystem.bindTextureForSetup(id);
        checkGLError("bindTextureForSetup");
    }

    public static void bindTexture(int id) {
        RenderSystem.bindTexture(id);
        checkGLError("bindTexture");
    }

    public static void clear(int mask) {
        RenderSystem.clear(mask, Minecraft.ON_OSX);
        checkGLError("clear");
    }

    public static void depthFunc(int func) {
        RenderSystem.depthFunc(func);
        checkGLError("depthFunc");
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        RenderSystem.colorMask(red, green, blue, alpha);
        checkGLError("colorMask");
    }

    public static void setProjectionMatrix(Matrix4f matrix4f) {
        RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
        checkGLError("setProjectionMatrix");
    }

    public static PoseStack getModelViewStack() {
        PoseStack stack = RenderSystem.getModelViewStack();
        checkGLError("getModelViewStack");
        return stack;
    }

    public static void lineWidth(float stroke) {
        RenderSystem.lineWidth(stroke);
        checkGLError("lineWidth");
    }

    public static void pixelStore(int pname, int param) {
        RenderSystem.pixelStore(pname, param);
        checkGLError("pixelStore");
    }

    public static void texImage2D(int target, int level, int internalFormat, int width, int height, int border, int format, int type, IntBuffer pixels) {
        GlStateManager._texImage2D(target, level, internalFormat, width, height, border, format, type, pixels);
        checkGLError("texImage2D");
    }

    public static void texSubImage2D(int target, int level, int xoffset, int yoffset, int width, int height, int format, int type, long pixels) {
        GlStateManager._texSubImage2D(target, level, xoffset, yoffset, width, height, format, type, pixels);
        checkGLError("texSubImage2D");
    }

    public static boolean checkGLError(String method) {
        if (!errorCheck) {
            return false;
        } else {
            boolean hasError = false;
            int err;
            while ((err = getError()) != 0) {
                hasError = true;
                JMLogger.logOnce("GL Error in method: " + method + " ERROR: " + err);
            }
            return hasError;
        }
    }
}