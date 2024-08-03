package icyllis.modernui.mc;

import com.mojang.blaze3d.vertex.VertexConsumer;
import icyllis.modernui.ModernUI;
import icyllis.modernui.animation.ColorEvaluator;
import icyllis.modernui.mc.mixin.AccessPostChain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import org.apache.commons.lang3.StringUtils;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

public enum BlurHandler {

    INSTANCE;

    private static final ResourceLocation BLUR_POST_EFFECT = new ResourceLocation("shaders/post/blur_fast.json");

    public static volatile boolean sBlurEffect;

    public static volatile boolean sBlurWithBackground;

    public static volatile int sBlurRadius;

    public static volatile int sBackgroundDuration;

    public static volatile int[] sBackgroundColor = new int[4];

    public static volatile int sFramerateInactive;

    public static volatile int sFramerateMinimized;

    public static volatile float sMasterVolumeInactive = 1.0F;

    public static volatile float sMasterVolumeMinimized = 1.0F;

    private final Minecraft minecraft = Minecraft.getInstance();

    private volatile ArrayList<Class<? extends Screen>> mBlacklist = new ArrayList();

    private final int[] mBackgroundColor = new int[4];

    private boolean mFadingIn;

    private boolean mBlurring;

    private boolean mBlurLoaded;

    private float mBlurRadius;

    private boolean mHasScreen;

    private float mVolumeMultiplier = 1.0F;

    public void blur(@Nullable Screen nextScreen) {
        if (this.minecraft.level != null) {
            boolean hasScreen = nextScreen != null;
            boolean blocked = false;
            if (hasScreen && sBlurEffect) {
                if (nextScreen instanceof MuiScreen screen) {
                    ScreenCallback callback = screen.getCallback();
                    if (callback != null) {
                        blocked = !callback.shouldBlurBackground();
                    }
                } else {
                    Class<?> t = nextScreen.getClass();
                    for (Class<?> c : this.mBlacklist) {
                        if (c.isAssignableFrom(t)) {
                            blocked = true;
                            break;
                        }
                    }
                }
            }
            if (blocked && this.mBlurring) {
                if (this.mBlurLoaded) {
                    this.minecraft.gameRenderer.shutdownEffect();
                }
                this.mFadingIn = false;
                this.mBlurring = false;
                this.mBlurLoaded = false;
            }
            GameRenderer gr = this.minecraft.gameRenderer;
            if (hasScreen && !this.mHasScreen) {
                if (!blocked && sBlurEffect && !this.mBlurring && gr.currentEffect() == null && sBlurRadius >= 1) {
                    this.mBlurring = true;
                    if (sBackgroundDuration > 0 && sBlurWithBackground) {
                        this.updateRadius(1.0F);
                    } else {
                        MuiModApi.get().loadEffect(this.minecraft.gameRenderer, BLUR_POST_EFFECT);
                        this.updateRadius((float) sBlurRadius);
                        this.mBlurLoaded = true;
                    }
                }
                if (sBackgroundDuration > 0) {
                    this.mFadingIn = true;
                    Arrays.fill(this.mBackgroundColor, 0);
                } else {
                    this.mFadingIn = false;
                    System.arraycopy(sBackgroundColor, 0, this.mBackgroundColor, 0, 4);
                }
            } else if (!hasScreen) {
                if (this.mBlurring) {
                    if (this.mBlurLoaded) {
                        gr.shutdownEffect();
                    }
                    this.mBlurLoaded = false;
                    this.mBlurring = false;
                }
                this.mFadingIn = false;
            }
            this.mHasScreen = hasScreen;
        }
    }

    public void forceBlur() {
        if (sBlurEffect) {
            if (this.minecraft.level != null && this.mBlurring) {
                GameRenderer gr = this.minecraft.gameRenderer;
                if (gr.currentEffect() == null) {
                    MuiModApi.get().loadEffect(gr, BLUR_POST_EFFECT);
                    this.mFadingIn = true;
                    this.mBlurring = true;
                    this.mBlurLoaded = true;
                } else {
                    this.mBlurLoaded = false;
                }
            }
        }
    }

    public void loadBlacklist(@Nullable List<? extends String> names) {
        ArrayList<Class<? extends Screen>> blacklist = new ArrayList();
        if (names != null) {
            for (String s : names) {
                if (!StringUtils.isEmpty(s)) {
                    try {
                        Class<?> clazz = Class.forName(s, false, ModernUIMod.class.getClassLoader());
                        blacklist.add(clazz);
                    } catch (ClassNotFoundException var6) {
                        ModernUI.LOGGER.warn(ModernUI.MARKER, "Failed to add blur blacklist {}: make sure class name exists", s, var6);
                    } catch (ClassCastException var7) {
                        ModernUI.LOGGER.warn(ModernUI.MARKER, "Failed to add blur blacklist {}: make sure class is a valid subclass of Screen", s, var7);
                    }
                }
            }
            blacklist.trimToSize();
        }
        this.mBlacklist = blacklist;
    }

    public void onRenderTick(long elapsedTimeMillis) {
        if (this.mFadingIn) {
            float p = Math.min((float) elapsedTimeMillis / (float) sBackgroundDuration, 1.0F);
            if (this.mBlurring) {
                this.updateRadius(Math.max(p * (float) sBlurRadius, 1.0F));
            }
            for (int i = 0; i < 4; i++) {
                this.mBackgroundColor[i] = ColorEvaluator.evaluate(p, 0, sBackgroundColor[i]);
            }
            if (p == 1.0F) {
                this.mFadingIn = false;
            }
        }
    }

    private void updateRadius(float radius) {
        this.mBlurRadius = radius;
        PostChain effect = this.minecraft.gameRenderer.currentEffect();
        if (effect != null) {
            for (PostPass s : ((AccessPostChain) effect).getPasses()) {
                s.getEffect().safeGetUniform("Progress").set(radius);
            }
        }
    }

    public void onClientTick() {
        float targetVolumeMultiplier;
        if (this.minecraft.isWindowActive()) {
            targetVolumeMultiplier = 1.0F;
        } else if (sMasterVolumeMinimized < sMasterVolumeInactive && GLFW.glfwGetWindowAttrib(this.minecraft.getWindow().getWindow(), 131074) != 0) {
            targetVolumeMultiplier = sMasterVolumeMinimized;
        } else {
            targetVolumeMultiplier = sMasterVolumeInactive;
        }
        if (this.mVolumeMultiplier != targetVolumeMultiplier) {
            if (this.mVolumeMultiplier < targetVolumeMultiplier) {
                this.mVolumeMultiplier = Math.min(this.mVolumeMultiplier + 0.5F, targetVolumeMultiplier);
            } else {
                this.mVolumeMultiplier = Math.max(this.mVolumeMultiplier - 0.05F, targetVolumeMultiplier);
            }
            float volume = this.minecraft.options.getSoundSourceVolume(SoundSource.MASTER);
            this.minecraft.getSoundManager().updateSourceVolume(SoundSource.MASTER, volume * this.mVolumeMultiplier);
        }
    }

    public void drawScreenBackground(@Nonnull GuiGraphics gr, int x1, int y1, int x2, int y2) {
        VertexConsumer consumer = gr.bufferSource().getBuffer(RenderType.gui());
        Matrix4f pose = gr.pose().last().pose();
        int z = 0;
        if (this.minecraft.level == null) {
            consumer.vertex(pose, (float) x2, (float) y1, (float) z).color(25, 25, 25, 255).endVertex();
            consumer.vertex(pose, (float) x1, (float) y1, (float) z).color(25, 25, 25, 255).endVertex();
            consumer.vertex(pose, (float) x1, (float) y2, (float) z).color(25, 25, 25, 255).endVertex();
            consumer.vertex(pose, (float) x2, (float) y2, (float) z).color(25, 25, 25, 255).endVertex();
        } else {
            if (this.mBlurring && !this.mBlurLoaded) {
                MuiModApi.get().loadEffect(this.minecraft.gameRenderer, BLUR_POST_EFFECT);
                this.updateRadius(this.mBlurRadius);
                this.mBlurLoaded = true;
            }
            int color = this.mBackgroundColor[1];
            consumer.vertex(pose, (float) x2, (float) y1, (float) z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >>> 24).endVertex();
            color = this.mBackgroundColor[0];
            consumer.vertex(pose, (float) x1, (float) y1, (float) z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >>> 24).endVertex();
            color = this.mBackgroundColor[3];
            consumer.vertex(pose, (float) x1, (float) y2, (float) z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >>> 24).endVertex();
            color = this.mBackgroundColor[2];
            consumer.vertex(pose, (float) x2, (float) y2, (float) z).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, color >>> 24).endVertex();
        }
        gr.flush();
    }
}