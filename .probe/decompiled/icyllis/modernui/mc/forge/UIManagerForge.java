package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.NativeImage;
import icyllis.arc3d.opengl.GLCore;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MuiScreen;
import icyllis.modernui.mc.OptiFineIntegration;
import icyllis.modernui.mc.TooltipRenderer;
import icyllis.modernui.mc.UIManager;
import icyllis.modernui.mc.mixin.AccessNativeImage;
import icyllis.modernui.mc.testforge.TestPauseFragment;
import icyllis.modernui.mc.text.TextLayoutEngine;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.view.KeyEvent;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.LoadingErrorScreen;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.lwjgl.glfw.GLFW;

@Internal
public final class UIManagerForge extends UIManager implements LifecycleOwner {

    public static final KeyMapping OPEN_CENTER_KEY = new KeyMapping("key.modernui.openCenter", KeyConflictContext.UNIVERSAL, KeyModifier.CONTROL, InputConstants.Type.KEYSYM, 75, "Modern UI");

    public static final KeyMapping ZOOM_KEY = new KeyMapping("key.modernui.zoom", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputConstants.Type.KEYSYM, 67, "Modern UI");

    public static final Field BY_PATH = ObfuscationReflectionHelper.findField(TextureManager.class, "f_118468_");

    public static final Field TEXTURES_BY_NAME = ObfuscationReflectionHelper.findField(TextureAtlas.class, "f_118264_");

    public static final Field TEXTURE_ID = ObfuscationReflectionHelper.findField(AbstractTexture.class, "f_117950_");

    private UIManagerForge() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @RenderThread
    static void initialize() {
        Core.checkRenderThread();
        assert sInstance == null;
        sInstance = new UIManagerForge();
        ModernUI.LOGGER.info(MARKER, "UI manager initialized");
    }

    @MainThread
    @Override
    protected void open(@Nonnull Fragment fragment) {
        if (!this.minecraft.m_18695_()) {
            throw new IllegalStateException("Not called from main thread");
        } else {
            this.minecraft.setScreen(new SimpleScreen(this, fragment, null, null, null));
        }
    }

    @SubscribeEvent
    void onScreenOpen(@Nonnull ScreenEvent.Opening event) {
        Screen newScreen = event.getNewScreen();
        if (!this.mFirstScreenOpened && !(newScreen instanceof LoadingErrorScreen)) {
            if (sDingEnabled) {
                GLFW.glfwRequestWindowAttention(this.minecraft.getWindow().getWindow());
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0F));
            }
            if (ModernUIMod.isOptiFineLoaded() && ModernUIMod.isTextEngineEnabled()) {
                OptiFineIntegration.setFastRender(false);
                ModernUI.LOGGER.info(MARKER, "Disabled OptiFine Fast Render");
            }
            Config.CLIENT.mLastWindowMode.apply();
            this.mFirstScreenOpened = true;
        }
        if (newScreen == null) {
            this.removed();
        } else {
            if (this.mScreen != newScreen && newScreen instanceof MuiScreen) {
                this.mElapsedTimeMillis = 0L;
            }
            if (this.mScreen != newScreen && this.mScreen != null) {
                this.onHoverMove(false);
            }
            if (this.mScreen == null && this.minecraft.screen == null) {
                this.mElapsedTimeMillis = 0L;
            }
        }
    }

    @SubscribeEvent
    void onPostMouseInput(@Nonnull InputEvent.MouseButton.Post event) {
        super.onPostMouseInput(event.getButton(), event.getAction(), event.getModifiers());
    }

    @SubscribeEvent
    void onPostKeyInput(@Nonnull InputEvent.Key event) {
        if (this.mScreen != null) {
            int action = event.getAction() == 0 ? 1 : 0;
            KeyEvent keyEvent = KeyEvent.obtain(Core.timeNanos(), action, event.getKey(), 0, event.getModifiers(), event.getScanCode(), 0);
            this.mRoot.enqueueInputEvent(keyEvent);
        }
        if (event.getAction() == 1 && (this.minecraft.screen == null || this.minecraft.screen.shouldCloseOnEsc() || this.minecraft.screen instanceof TitleScreen)) {
            InputConstants.Key key = InputConstants.getKey(event.getKey(), event.getScanCode());
            if (OPEN_CENTER_KEY.isActiveAndMatches(key)) {
                this.open(new CenterFragment2());
                return;
            }
        }
        if (Screen.hasControlDown() && Screen.hasShiftDown() && ModernUIMod.isDeveloperMode()) {
            if (event.getAction() == 1) {
                switch(event.getKey()) {
                    case 70:
                        System.gc();
                        break;
                    case 71:
                        GlyphManager.getInstance().debug();
                    case 72:
                    case 73:
                    case 75:
                    case 76:
                    case 81:
                    case 82:
                    case 83:
                    case 84:
                    case 87:
                    case 88:
                    default:
                        break;
                    case 74:
                        this.open(new TestPauseFragment());
                        break;
                    case 77:
                        this.changeRadialBlur();
                        break;
                    case 78:
                        this.mDecor.postInvalidate();
                        break;
                    case 79:
                        this.mNoRender = !this.mNoRender;
                        break;
                    case 80:
                        this.dump();
                        break;
                    case 85:
                        this.mClearNextMainTarget = true;
                        break;
                    case 86:
                        if (ModernUIMod.isTextEngineEnabled()) {
                            TextLayoutEngine.getInstance().dumpBitmapFonts();
                        }
                        break;
                    case 89:
                        this.takeScreenshot();
                }
            }
        }
    }

    @Override
    public void dump(@NotNull PrintWriter pw, boolean fragments) {
        super.dump(pw, fragments);
        Map<ResourceLocation, AbstractTexture> textureMap = null;
        try {
            textureMap = (Map<ResourceLocation, AbstractTexture>) BY_PATH.get(this.minecraft.getTextureManager());
        } catch (Exception var26) {
        }
        if (textureMap != null && this.mDevice.getCaps().hasDSASupport()) {
            long gpuSize = 0L;
            long cpuSize = 0L;
            int dynamicTextures = 0;
            int textureAtlases = 0;
            int atlasSprites = 0;
            for (AbstractTexture texture : textureMap.values()) {
                try {
                    int tex = TEXTURE_ID.getInt(texture);
                    if (GLCore.glIsTexture(tex)) {
                        int internalFormat = GLCore.glGetTextureLevelParameteri(tex, 0, 4099);
                        long width = (long) GLCore.glGetTextureLevelParameteri(tex, 0, 4096);
                        long height = (long) GLCore.glGetTextureLevelParameteri(tex, 0, 4097);
                        int maxLevel = GLCore.glGetTextureParameteri(tex, 33085);
                        int bpp = switch(internalFormat) {
                            case 6403, 33321 ->
                                1;
                            case 6407, 6408, 32849, 32856 ->
                                4;
                            case 33319, 33323 ->
                                2;
                            default ->
                                0;
                        };
                        long size = width * height * (long) bpp;
                        if (maxLevel > 0) {
                            size = (size - (size >> (maxLevel + 1 << 1)) << 2) / 3L;
                        }
                        gpuSize += size;
                    }
                } catch (Exception var25) {
                }
                if (texture instanceof DynamicTexture dynamicTexture) {
                    NativeImage image = dynamicTexture.getPixels();
                    try {
                        if (image != null && ((AccessNativeImage) image).getPixels() != 0L) {
                            cpuSize += (long) image.getWidth() * (long) image.getHeight() * (long) image.format().components();
                        }
                    } catch (Exception var24) {
                    }
                    dynamicTextures++;
                }
                if (texture instanceof TextureAtlas textureAtlas) {
                    try {
                        Map<ResourceLocation, TextureAtlasSprite> textures = (Map<ResourceLocation, TextureAtlasSprite>) TEXTURES_BY_NAME.get(textureAtlas);
                        atlasSprites += textures.size();
                    } catch (Exception var23) {
                    }
                    textureAtlases++;
                }
            }
            pw.print("Minecraft's TextureManager: ");
            pw.print("Textures=" + textureMap.size());
            pw.print(", DynamicTextures=" + dynamicTextures);
            pw.print(", Atlases=" + textureAtlases);
            pw.print(", Sprites=" + atlasSprites);
            pw.print(", GPUMemory=" + TextUtils.binaryCompact(gpuSize) + " (" + gpuSize + " bytes)");
            pw.println(", CPUMemory=" + TextUtils.binaryCompact(cpuSize) + " (" + cpuSize + " bytes)");
        }
    }

    @SubscribeEvent
    void onRenderGameOverlayLayer(@Nonnull RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type() && this.mScreen != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    void onRenderTooltipH(@Nonnull RenderTooltipEvent.Pre event) {
        if (TooltipRenderer.sTooltip) {
            this.drawExtTooltip(event.getItemStack(), event.getGraphics(), event.getComponents(), event.getX(), event.getY(), event.getFont(), event.getScreenWidth(), event.getScreenHeight(), event.getTooltipPositioner());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    void onRenderTooltipL(@Nonnull RenderTooltipEvent.Pre event) {
        if (TooltipRenderer.sTooltip) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    void onRenderTick(@Nonnull TickEvent.RenderTickEvent event) {
        super.onRenderTick(event.phase == TickEvent.Phase.END);
    }

    @SubscribeEvent
    void onClientTick(@Nonnull TickEvent.ClientTickEvent event) {
        super.onClientTick(event.phase == TickEvent.Phase.END);
    }

    @SubscribeEvent
    void onChangeFov(@Nonnull ViewportEvent.ComputeFov event) {
        boolean zoomActive = false;
        if (sZoomEnabled && this.minecraft.screen == null) {
            zoomActive = ZOOM_KEY.isDown();
        }
        if (zoomActive) {
            if (!this.mZoomMode) {
                this.mZoomMode = true;
                this.mZoomSmoothCamera = this.minecraft.options.smoothCamera;
                this.minecraft.options.smoothCamera = true;
                this.minecraft.levelRenderer.needsUpdate();
            }
            event.setFOV(event.getFOV() * 0.25);
        } else if (this.mZoomMode) {
            this.mZoomMode = false;
            this.minecraft.options.smoothCamera = this.mZoomSmoothCamera;
            this.minecraft.levelRenderer.needsUpdate();
        }
    }
}