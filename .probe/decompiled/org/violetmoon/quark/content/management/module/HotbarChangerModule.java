package org.violetmoon.quark.content.management.module;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.ChangeHotbarMessage;
import org.violetmoon.zeta.client.event.load.ZKeyMapping;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.client.event.play.ZInput;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZPhase;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "management")
public class HotbarChangerModule extends ZetaModule {

    private static final ResourceLocation WIDGETS = new ResourceLocation("textures/gui/widgets.png");

    private static final int ANIMATION_TIME = 10;

    private static final int MAX_HEIGHT = 90;

    private static final int ANIM_PER_TICK = 9;

    public static int height = 0;

    public static int currentHeldItem = -1;

    public static boolean animating;

    public static boolean keyDown;

    public static boolean hotbarChangeOpen;

    public static boolean shifting;

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends HotbarChangerModule {

        private static KeyMapping changeHotbarKey;

        @LoadEvent
        public void registerKeybinds(ZKeyMapping event) {
            changeHotbarKey = event.init("quark.keybind.change_hotbar", "z", "quark.gui.keygroup.misc");
        }

        @PlayEvent
        public void onMouseInput(ZInput.MouseButton event) {
            this.acceptInput(-1);
        }

        @PlayEvent
        public void onKeyInput(ZInput.Key event) {
            this.acceptInput(event.getKey());
        }

        @PlayEvent
        public void hudHeathPre(ZRenderGuiOverlay.PlayerHealth.Pre event) {
            float shift = -this.getRealHeight(event.getPartialTick()) + 22.0F;
            if (shift < 0.0F) {
                event.getGuiGraphics().pose().translate(0.0F, shift, 0.0F);
                shifting = true;
            }
        }

        @PlayEvent
        public void hudDebugTextPre(ZRenderGuiOverlay.DebugText.Pre event) {
            this.hudOverlay(event);
        }

        @PlayEvent
        public void hudPotionIconsPre(ZRenderGuiOverlay.PotionIcons.Pre event) {
            this.hudOverlay(event);
        }

        public void hudOverlay(ZRenderGuiOverlay event) {
            float shift = -this.getRealHeight(event.getPartialTick()) + 22.0F;
            if (shifting) {
                event.getGuiGraphics().pose().translate(0.0F, -shift, 0.0F);
                shifting = false;
            }
        }

        @PlayEvent
        public void hudPost(ZRenderGuiOverlay.Hotbar.Post event) {
            if (height > 0) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                GuiGraphics guiGraphics = event.getGuiGraphics();
                PoseStack matrix = guiGraphics.pose();
                Window res = event.getWindow();
                float realHeight = this.getRealHeight(event.getPartialTick());
                float xStart = (float) res.getGuiScaledWidth() / 2.0F - 91.0F;
                float yStart = (float) res.getGuiScaledHeight() - realHeight;
                ItemRenderer render = mc.getItemRenderer();
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(770, 771);
                RenderSystem.setShader(GameRenderer::m_172817_);
                for (int i = 0; i < 3; i++) {
                    matrix.pushPose();
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.75F);
                    matrix.translate(xStart, yStart + (float) (i * 21), 0.0F);
                    guiGraphics.blit(HotbarChangerModule.WIDGETS, 0, 0, 0, 0, 182, 22);
                    matrix.popPose();
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                for (int i = 0; i < 3; i++) {
                    String draw = Integer.toString(i + 1);
                    KeyMapping key = mc.options.keyHotbarSlots[i];
                    if (!key.isUnbound()) {
                        draw = key.getTranslatedKeyMessage().getString();
                    }
                    draw = ChatFormatting.BOLD + draw;
                    guiGraphics.drawString(mc.font, draw, xStart - (float) mc.font.width(draw) - 2.0F, yStart + (float) (i * 21) + 7.0F, 16777215, true);
                }
                for (int i = 0; i < 27; i++) {
                    ItemStack invStack = player.getInventory().getItem(i + 9);
                    int x = (int) (xStart + (float) (i % 9 * 20) + 3.0F);
                    int y = (int) (yStart + (float) (i / 9 * 21) + 3.0F);
                    guiGraphics.renderItem(invStack, x, y);
                    guiGraphics.renderItemDecorations(mc.font, invStack, x, y);
                }
            }
        }

        @PlayEvent
        public void clientTick(ZClientTick event) {
            if (event.getPhase() == ZPhase.END) {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    Inventory inventory = player.getInventory();
                    if (currentHeldItem != -1 && inventory.selected != currentHeldItem) {
                        inventory.selected = currentHeldItem;
                        currentHeldItem = -1;
                    }
                }
                if (hotbarChangeOpen && height < 90) {
                    height += 9;
                    animating = true;
                } else if (!hotbarChangeOpen && height > 0) {
                    height -= 9;
                    animating = true;
                } else {
                    animating = false;
                }
            }
        }

        private void acceptInput(int currInput) {
            Minecraft mc = Minecraft.getInstance();
            boolean down = changeHotbarKey.isDown();
            boolean wasDown = keyDown;
            keyDown = down;
            if (mc.isWindowActive()) {
                if (down && !wasDown) {
                    hotbarChangeOpen = !hotbarChangeOpen;
                } else if (hotbarChangeOpen) {
                    for (int i = 0; i < 3; i++) {
                        if (this.isKeyDownOrFallback(mc.options.keyHotbarSlots[i], 49 + i, currInput)) {
                            QuarkClient.ZETA_CLIENT.sendToServer(new ChangeHotbarMessage(i + 1));
                            hotbarChangeOpen = false;
                            currentHeldItem = mc.player.m_150109_().selected;
                            return;
                        }
                    }
                }
            }
        }

        private boolean isKeyDownOrFallback(KeyMapping key, int input, int currInput) {
            return !key.isUnbound() ? key.isDown() : currInput != -1 && input == currInput;
        }

        private float getRealHeight(float part) {
            return !animating ? (float) height : (float) height + part * 9.0F * (float) (hotbarChangeOpen ? 1 : -1);
        }
    }
}