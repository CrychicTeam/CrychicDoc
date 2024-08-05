package net.mehvahdjukaar.supplementaries.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.api.IQuiverEntity;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundCycleQuiverPacket;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class QuiverArrowSelectGui extends Gui {

    private static final ResourceLocation TEXTURE = Supplementaries.res("textures/gui/quiver_select.png");

    private static boolean usingItem;

    private static boolean usingKeyAndHasItem = false;

    private static double lastCumulativeMouseDx = 0.0;

    protected final Minecraft minecraft;

    public static boolean isActive() {
        return usingItem || usingKeyAndHasItem;
    }

    public static void setUsingItem(boolean on) {
        usingItem = on;
    }

    public static boolean isUsingKey() {
        return usingKeyAndHasItem;
    }

    public static void setUsingKeybind(boolean on) {
        usingKeyAndHasItem = on && Minecraft.getInstance().player instanceof IQuiverEntity qe && qe.supplementaries$hasQuiver();
    }

    protected QuiverArrowSelectGui(Minecraft minecraft, ItemRenderer itemRenderer) {
        super(minecraft, itemRenderer);
        this.minecraft = minecraft;
    }

    public static void onPlayerRotated(double yRotIncrease) {
        int slotsMoved = (int) (yRotIncrease * 0.2);
        if (slotsMoved != 0) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ServerBoundCycleQuiverPacket.Slot s = getQuiverSlot(player);
                ModNetwork.CHANNEL.sendToServer(new ServerBoundCycleQuiverPacket(slotsMoved, s));
            }
        }
    }

    public static void ohMouseMoved(double deltaX) {
        double scale = Minecraft.getInstance().options.sensitivity().get() * 0.02;
        int oldI = (int) (lastCumulativeMouseDx * scale);
        lastCumulativeMouseDx += deltaX;
        int slotsMoved = (int) (lastCumulativeMouseDx * scale) - oldI;
        if (slotsMoved != 0) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ServerBoundCycleQuiverPacket.Slot s = getQuiverSlot(player);
                ModNetwork.CHANNEL.sendToServer(new ServerBoundCycleQuiverPacket(slotsMoved, s));
            }
        }
    }

    public static boolean onKeyPressed(int key, int action, int modifiers) {
        if (action == 1) {
            Player player = Minecraft.getInstance().player;
            switch(key) {
                case 262:
                    ModNetwork.CHANNEL.sendToServer(new ServerBoundCycleQuiverPacket(1, getQuiverSlot(player)));
                    return true;
                case 263:
                    ModNetwork.CHANNEL.sendToServer(new ServerBoundCycleQuiverPacket(-1, getQuiverSlot(player)));
                    return true;
                default:
                    int number = key - 48;
                    if (number >= 1 && number <= 9) {
                        if (number <= (Integer) CommonConfigs.Tools.QUIVER_SLOTS.get()) {
                            ModNetwork.CHANNEL.sendToServer(new ServerBoundCycleQuiverPacket(number - 1, getQuiverSlot(player), true));
                        }
                        return true;
                    }
            }
        }
        return false;
    }

    public static boolean onMouseScrolled(double scrollDelta) {
        Player player = Minecraft.getInstance().player;
        ModNetwork.CHANNEL.sendToServer(new ServerBoundCycleQuiverPacket(scrollDelta > 0.0 ? -1 : 1, getQuiverSlot(player)));
        return true;
    }

    private void renderSlot(GuiGraphics graphics, int pX, int pY, ItemStack pStack, int seed) {
        if (!pStack.isEmpty()) {
            graphics.renderItem(pStack, pX, pY, seed);
            RenderSystem.setShader(GameRenderer::m_172811_);
            graphics.renderItemDecorations(this.minecraft.font, pStack, pX, pY);
        }
    }

    public void renderQuiverContent(GuiGraphics graphics, float partialTicks, int screenWidth, int screenHeight) {
        if (this.minecraft.getCameraEntity() instanceof Player player) {
            ItemStack quiver = getCurrentlyUsedQuiver(player);
            if (quiver.getItem() == ModRegistry.QUIVER_ITEM.get()) {
                PoseStack poseStack = graphics.pose();
                poseStack.pushPose();
                QuiverItem.Data data = QuiverItem.getQuiverData(quiver);
                int selected = data.getSelectedSlot();
                List<ItemStack> items = data.getContentView();
                int slots = items.size();
                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                int centerX = screenWidth / 2;
                poseStack.pushPose();
                poseStack.translate(0.0F, 0.0F, -90.0F);
                int uWidth = slots * 20 + 2;
                int px = uWidth / 2;
                int py = screenHeight / 2 - 40;
                px += ClientConfigs.Items.QUIVER_GUI_X.get();
                py += ClientConfigs.Items.QUIVER_GUI_Y.get();
                graphics.blit(TEXTURE, centerX - px, py, 0, 0, uWidth - 1, 22);
                graphics.blit(TEXTURE, centerX + px - 1, py, 0, 0, 1, 22);
                graphics.blit(TEXTURE, centerX - px - 1 + selected * 20, py - 1, 24, 22, 24, 24);
                poseStack.popPose();
                int i1 = 1;
                for (int i = 0; i < slots; i++) {
                    int kx = centerX - px + 3 + i * 20;
                    this.renderSlot(graphics, kx, py + 3, (ItemStack) items.get(i), i1++);
                }
                RenderSystem.disableBlend();
                ItemStack selectedArrow = (ItemStack) items.get(selected);
                if (!selectedArrow.isEmpty()) {
                    this.drawHighlight(graphics, screenWidth, py, selectedArrow);
                }
                poseStack.popPose();
                setUsingItem(true);
                return;
            }
        }
        setUsingItem(false);
    }

    @NotNull
    private static ServerBoundCycleQuiverPacket.Slot getQuiverSlot(Player player) {
        return usingKeyAndHasItem ? ServerBoundCycleQuiverPacket.Slot.INVENTORY : (player.m_7655_() == InteractionHand.MAIN_HAND ? ServerBoundCycleQuiverPacket.Slot.MAIN_HAND : ServerBoundCycleQuiverPacket.Slot.OFF_HAND);
    }

    private static ItemStack getCurrentlyUsedQuiver(Player player) {
        return usingKeyAndHasItem ? ((IQuiverEntity) player).supplementaries$getQuiver() : player.m_21211_();
    }

    protected abstract void drawHighlight(GuiGraphics var1, int var2, int var3, ItemStack var4);
}