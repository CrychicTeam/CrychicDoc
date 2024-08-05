package org.violetmoon.quark.content.client.module;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.experimental.module.OverlayShaderModule;
import org.violetmoon.zeta.client.event.load.ZKeyMapping;
import org.violetmoon.zeta.client.event.play.ZEarlyRender;
import org.violetmoon.zeta.client.event.play.ZInput;
import org.violetmoon.zeta.client.event.play.ZScreenshot;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "client")
public class CameraModule extends ZetaModule {

    @Config(description = "Date format that will be displayed in screenshots. Must be a valid one (i.e. MM/dd/yyyy)")
    @Config.Condition(CameraModule.DatePredicate.class)
    private static String dateFormat = "MM/dd/yyyy";

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends CameraModule {

        private static final int RULER_COLOR = 855638016;

        private static final int RULERS = 4;

        private static final int BORERS = 6;

        private static final int OVERLAYS = 5;

        private static final ResourceLocation[] SHADERS = new ResourceLocation[] { null, new ResourceLocation("quark", "shaders/post/grayscale.json"), new ResourceLocation("quark", "shaders/post/sepia.json"), new ResourceLocation("quark", "shaders/post/desaturate.json"), new ResourceLocation("quark", "shaders/post/oversaturate.json"), new ResourceLocation("quark", "shaders/post/cool.json"), new ResourceLocation("quark", "shaders/post/warm.json"), new ResourceLocation("quark", "shaders/post/conjugate.json"), new ResourceLocation("quark", "shaders/post/redfocus.json"), new ResourceLocation("quark", "shaders/post/greenfocus.json"), new ResourceLocation("quark", "shaders/post/bluefocus.json"), new ResourceLocation("quark", "shaders/post/yellowfocus.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/creeper.json"), new ResourceLocation("quark", "shaders/post/enderman.json"), new ResourceLocation("quark", "shaders/post/bits.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("quark", "shaders/post/watercolor.json"), new ResourceLocation("quark", "shaders/post/monochrome.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("quark", "shaders/post/colorblind/deuteranopia.json"), new ResourceLocation("quark", "shaders/post/colorblind/protanopia.json"), new ResourceLocation("quark", "shaders/post/colorblind/tritanopia.json"), new ResourceLocation("quark", "shaders/post/colorblind/achromatopsia.json") };

        private static KeyMapping cameraModeKey;

        private static int currentHeldItem = -1;

        private static int currShader = 0;

        private static int currRulers = 0;

        private static int currBorders = 0;

        private static int currOverlay = 0;

        private static boolean queuedRefresh = false;

        private static boolean queueScreenshot = false;

        private static boolean screenshotting = false;

        private static boolean cameraMode;

        @LoadEvent
        public void registerKeybinds(ZKeyMapping event) {
            cameraModeKey = event.init("quark.keybind.camera_mode", "f12", "quark.gui.keygroup.misc");
        }

        @PlayEvent
        public void screenshotTaken(ZScreenshot event) {
            screenshotting = false;
        }

        @PlayEvent
        public void keystroke(ZInput.Key event) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null && event.getAction() == 1) {
                if (cameraModeKey.isDown()) {
                    cameraMode = !cameraMode;
                    queuedRefresh = true;
                    return;
                }
                if (cameraMode && mc.screen == null) {
                    int key = event.getKey();
                    boolean affected = false;
                    boolean sneak = mc.player.m_20163_();
                    switch(key) {
                        case 49:
                            currShader = cycle(currShader, SHADERS.length, sneak);
                            affected = true;
                            break;
                        case 50:
                            currRulers = cycle(currRulers, 4, sneak);
                            affected = true;
                            break;
                        case 51:
                            currBorders = cycle(currBorders, 6, sneak);
                            affected = true;
                            break;
                        case 52:
                            currOverlay = cycle(currOverlay, 5, sneak);
                            affected = true;
                            break;
                        case 53:
                            if (sneak) {
                                currShader = 0;
                                currRulers = 0;
                                currBorders = 0;
                                currOverlay = 0;
                                affected = true;
                            }
                            break;
                        case 257:
                            if (!queueScreenshot && !screenshotting) {
                                mc.getSoundManager().play(SimpleSoundInstance.forUI(QuarkSounds.ITEM_CAMERA_SHUTTER, 1.0F));
                            }
                            queueScreenshot = true;
                    }
                    if (affected) {
                        queuedRefresh = true;
                        currentHeldItem = mc.player.m_150109_().selected;
                    }
                }
            }
        }

        @PlayEvent
        public void renderTick(ZEarlyRender event) {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player != null) {
                Inventory inventory = player.getInventory();
                if (currentHeldItem != -1 && inventory.selected != currentHeldItem) {
                    inventory.selected = currentHeldItem;
                    currentHeldItem = -1;
                }
            }
            if (mc.level == null) {
                cameraMode = false;
                queuedRefresh = true;
            } else if (queuedRefresh) {
                refreshShader();
            }
            if (cameraMode && mc.screen == null) {
                if (queueScreenshot) {
                    screenshotting = true;
                }
                renderCameraHUD(mc, event.guiGraphics());
                if (queueScreenshot) {
                    queueScreenshot = false;
                    Screenshot.grab(mc.gameDirectory, mc.getMainRenderTarget(), msg -> mc.execute(() -> mc.gui.getChat().addMessage(msg)));
                }
            }
        }

        private static void renderCameraHUD(Minecraft mc, GuiGraphics guiGraphics) {
            PoseStack matrix = guiGraphics.pose();
            Window mw = mc.getWindow();
            int twidth = mw.getGuiScaledWidth();
            int theight = mw.getGuiScaledHeight();
            int paddingHoriz = 0;
            int paddingVert = 0;
            int paddingColor = -16777216;
            double targetAspect = -1.0;
            switch(currBorders) {
                case 1:
                    targetAspect = 1.0;
                    break;
                case 2:
                    targetAspect = 1.3333333333333333;
                    break;
                case 3:
                    targetAspect = 1.7777777777777777;
                    break;
                case 4:
                    targetAspect = 2.3333333333333335;
                    break;
                case 5:
                    int border = (int) (20.0 * ((double) (twidth * theight) / 518400.0));
                    paddingHoriz = border;
                    paddingVert = border;
                    paddingColor = -1;
            }
            if (targetAspect > 0.0) {
                double currAspect = (double) twidth / (double) theight;
                if (currAspect > targetAspect) {
                    int desiredWidth = (int) ((double) theight * targetAspect);
                    paddingHoriz = (twidth - desiredWidth) / 2;
                } else if (currAspect < targetAspect) {
                    int desiredHeight = (int) ((double) twidth / targetAspect);
                    paddingVert = (theight - desiredHeight) / 2;
                }
            }
            int width = twidth - paddingHoriz * 2;
            int height = theight - paddingVert * 2;
            if (paddingHoriz > 0) {
                guiGraphics.fill(0, 0, paddingHoriz, theight, paddingColor);
                guiGraphics.fill(twidth - paddingHoriz, 0, twidth, theight, paddingColor);
            }
            if (paddingVert > 0) {
                guiGraphics.fill(0, 0, twidth, paddingVert, paddingColor);
                guiGraphics.fill(0, theight - paddingVert, twidth, theight, paddingColor);
            }
            String overlayText = "";
            boolean overlayShadow = true;
            double overlayScale = 2.0;
            int overlayColor = -1;
            int overlayX = -1;
            int overlayY = -1;
            switch(currOverlay) {
                case 1:
                    overlayText = new SimpleDateFormat(CameraModule.dateFormat).format(new Date(System.currentTimeMillis()));
                    overlayColor = 16217856;
                    break;
                case 2:
                    String worldName = "N/A";
                    if (mc.getSingleplayerServer() != null) {
                        worldName = mc.getSingleplayerServer().m_7326_();
                    } else if (mc.getCurrentServer() != null) {
                        worldName = mc.getCurrentServer().name;
                    }
                    overlayText = I18n.get("quark.camera.greetings", worldName);
                    overlayX = paddingHoriz + 20;
                    overlayY = paddingVert + 20;
                    overlayScale = 3.0;
                    overlayColor = 15684645;
                    break;
                case 3:
                    overlayText = mc.player.m_36316_().getName();
                    overlayScale = 6.0;
                    overlayShadow = false;
                    overlayColor = 1140850688;
                    break;
                case 4:
                    overlayText = mc.player.m_21205_().getHoverName().getString();
                    overlayX = twidth / 2 - mc.font.width(overlayText);
                    overlayY = paddingVert + 40;
            }
            if (overlayX == -1) {
                overlayX = twidth - paddingHoriz - mc.font.width(overlayText) * (int) overlayScale - 40;
            }
            if (overlayY == -1) {
                overlayY = theight - paddingVert - 10 - 10 * (int) overlayScale;
            }
            if (!overlayText.isEmpty()) {
                matrix.pushPose();
                matrix.translate((float) overlayX, (float) overlayY, 0.0F);
                matrix.scale((float) overlayScale, (float) overlayScale, 1.0F);
                guiGraphics.drawString(mc.font, overlayText, 0, 0, overlayColor, overlayShadow);
                matrix.popPose();
            }
            if (!screenshotting) {
                matrix.pushPose();
                matrix.translate((float) paddingHoriz, (float) paddingVert, 0.0F);
                switch(currRulers) {
                    case 1:
                        vruler(guiGraphics, width / 3, height);
                        vruler(guiGraphics, width / 3 * 2, height);
                        hruler(guiGraphics, height / 3, width);
                        hruler(guiGraphics, height / 3 * 2, width);
                        break;
                    case 2:
                        double phi1 = 0.3831417624521073;
                        double phi2 = 0.6168582375478928;
                        vruler(guiGraphics, (int) ((double) width * phi1), height);
                        vruler(guiGraphics, (int) ((double) width * phi2), height);
                        hruler(guiGraphics, (int) ((double) height * phi1), width);
                        hruler(guiGraphics, (int) ((double) height * phi2), width);
                        break;
                    case 3:
                        vruler(guiGraphics, width / 2, height);
                        hruler(guiGraphics, height / 2, width);
                }
                matrix.popPose();
                int left = 30;
                int top = theight - 65;
                ResourceLocation shader = SHADERS[currShader];
                String text = "none";
                if (shader != null) {
                    text = shader.getPath().replaceAll(".+/(.+)\\.json", "$1");
                }
                text = ChatFormatting.BOLD + "[1] " + ChatFormatting.RESET + I18n.get("quark.camera.filter") + ChatFormatting.GOLD + I18n.get("quark.camera.filter." + text);
                guiGraphics.drawString(mc.font, text, left, top, 16777215, true);
                text = ChatFormatting.BOLD + "[2] " + ChatFormatting.RESET + I18n.get("quark.camera.rulers") + ChatFormatting.GOLD + I18n.get("quark.camera.rulers" + currRulers);
                guiGraphics.drawString(mc.font, text, left, top + 12, 16777215, true);
                text = ChatFormatting.BOLD + "[3] " + ChatFormatting.RESET + I18n.get("quark.camera.borders") + ChatFormatting.GOLD + I18n.get("quark.camera.borders" + currBorders);
                guiGraphics.drawString(mc.font, text, left, top + 24, 16777215, true);
                text = ChatFormatting.BOLD + "[4] " + ChatFormatting.RESET + I18n.get("quark.camera.overlay") + ChatFormatting.GOLD + I18n.get("quark.camera.overlay" + currOverlay);
                guiGraphics.drawString(mc.font, text, left, top + 36, 16777215, true);
                text = ChatFormatting.BOLD + "[5] " + ChatFormatting.RESET + I18n.get("quark.camera.reset");
                guiGraphics.drawString(mc.font, text, left, top + 48, 16777215, true);
                text = ChatFormatting.AQUA + I18n.get("quark.camera.header");
                guiGraphics.drawString(mc.font, text, (float) (twidth / 2 - mc.font.width(text) / 2), 6.0F, 16777215, true);
                text = I18n.get("quark.camera.info", Component.keybind("quark.keybind.camera_mode").getString());
                guiGraphics.drawString(mc.font, text, (float) (twidth / 2 - mc.font.width(text) / 2), 16.0F, 16777215, true);
                ResourceLocation CAMERA_TEXTURE = new ResourceLocation("quark", "textures/misc/camera.png");
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.blit(CAMERA_TEXTURE, left - 22, top + 18, 0, 0.0F, 0.0F, 16, 16, 16, 16);
            }
        }

        private static void refreshShader() {
            if (queuedRefresh) {
                queuedRefresh = false;
            }
            Minecraft mc = Minecraft.getInstance();
            GameRenderer render = mc.gameRenderer;
            mc.options.hideGui = cameraMode;
            if (cameraMode) {
                ResourceLocation shader = SHADERS[currShader];
                if (shader != null) {
                    render.loadEffect(shader);
                    return;
                }
            } else {
                OverlayShaderModule shaderModule = Quark.ZETA.modules.get(OverlayShaderModule.class);
                if (shaderModule != null && shaderModule.enabled) {
                    for (ResourceLocation l : SHADERS) {
                        if (l != null && l.getPath().contains(shaderModule.shader + ".json")) {
                            render.loadEffect(l);
                            return;
                        }
                    }
                }
            }
            render.checkEntityPostEffect(null);
        }

        private static void vruler(GuiGraphics guiGraphics, int x, int height) {
            guiGraphics.fill(x, 0, x + 1, height, 855638016);
        }

        private static void hruler(GuiGraphics guiGraphics, int y, int width) {
            guiGraphics.fill(0, y, width, y + 1, 855638016);
        }

        private static int cycle(int curr, int max, boolean neg) {
            int val = curr + (neg ? -1 : 1);
            if (val < 0) {
                val = max - 1;
            } else if (val >= max) {
                val = 0;
            }
            return val;
        }
    }

    private static class DatePredicate implements Predicate<Object> {

        public boolean test(Object o) {
            if (o instanceof String s) {
                try {
                    new SimpleDateFormat(s);
                    return true;
                } catch (IllegalArgumentException var4) {
                }
            }
            return false;
        }
    }
}