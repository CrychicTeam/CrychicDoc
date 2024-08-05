package dev.xkmc.l2artifacts.content.swap;

import dev.xkmc.l2artifacts.events.ArtifactSel;
import dev.xkmc.l2library.base.overlay.ItemSelSideBar;
import dev.xkmc.l2library.base.overlay.OverlayUtil;
import dev.xkmc.l2library.base.overlay.SelectionSideBar;
import dev.xkmc.l2library.base.overlay.SideBar;
import dev.xkmc.l2library.util.Proxy;
import it.unimi.dsi.fastutil.Pair;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class ArtifactSwapOverlay extends SelectionSideBar<Integer, SideBar.IntSignature> {

    public static final ArtifactSwapOverlay INSTANCE = new ArtifactSwapOverlay();

    private ArtifactSwapOverlay() {
        super(40.0F, 3.0F);
    }

    @Override
    public com.mojang.datafixers.util.Pair<List<Integer>, Integer> getItems() {
        return com.mojang.datafixers.util.Pair.of(List.of(), 0);
    }

    public boolean isAvailable(Integer integer) {
        return true;
    }

    @Override
    public boolean onCenter() {
        return false;
    }

    public SideBar.IntSignature getSignature() {
        LocalPlayer player = Proxy.getClientPlayer();
        assert player != null;
        Pair<ItemStack, ArtifactSwapData> data = ArtifactSel.getData(player);
        return data == null ? new SideBar.IntSignature(0) : new SideBar.IntSignature(((ArtifactSwapData) data.second()).select + 100);
    }

    @Override
    public boolean isScreenOn() {
        if (Minecraft.getInstance().screen != null) {
            return false;
        } else {
            LocalPlayer player = Proxy.getClientPlayer();
            assert player != null;
            return ArtifactSel.INSTANCE.isClientActive(player);
        }
    }

    @Override
    public void renderContent(SelectionSideBar.Context ctx) {
        LocalPlayer player = Proxy.getClientPlayer();
        if (player != null) {
            Pair<ItemStack, ArtifactSwapData> pair = ArtifactSel.getData(player);
            if (pair != null) {
                ArtifactSwapData data = (ArtifactSwapData) pair.second();
                this.idle = 0.0F;
                for (int i = 0; i < 9; i++) {
                    int y = 18 * i + ctx.y0();
                    for (int j = 0; j < 5; j++) {
                        int x = ctx.x0() + 18 * j;
                        ArtifactSwapData.SwapSlot slot = data.contents[j * 9 + i];
                        ItemStack stack = slot.getStack();
                        if (!slot.isLocked()) {
                            this.renderSelection(ctx.g(), x, y, data.select == i ? 128 : 64, true, data.select == i && !stack.isEmpty());
                            ctx.renderItem(stack, x, y);
                        }
                        if (data.select == i) {
                            Optional<IDynamicStackHandler> opt = player.getCapability(CuriosCapability.INVENTORY).resolve().flatMap(cap -> cap.getStacksHandler(slot.slot.getCurioIdentifier())).map(ICurioStacksHandler::getStacks);
                            if (opt.isPresent()) {
                                int cx = ctx.x0() + 108;
                                int cy = ctx.y0() + 36 + j * 18;
                                ItemStack current = ((IDynamicStackHandler) opt.get()).getStackInSlot(0);
                                boolean sel = !slot.isLocked() && (!current.isEmpty() || !stack.isEmpty());
                                this.renderArmorSlot(ctx.g(), cx, cy, 64, sel && !current.isEmpty());
                                ctx.renderItem(current, cx, cy);
                            }
                        }
                    }
                }
            }
        }
    }

    protected void renderEntry(SelectionSideBar.Context context, Integer integer, int i, int i1) {
    }

    public void renderArmorSlot(GuiGraphics g, int x, int y, int a, boolean target) {
        OverlayUtil.fillRect(g, x, y, 16, 16, ItemSelSideBar.color(255, 255, 255, a));
        if (target) {
            OverlayUtil.drawRect(g, x, y, 16, 16, ItemSelSideBar.color(70, 150, 185, 255));
        }
    }

    public void renderSelection(GuiGraphics g, int x, int y, int a, boolean available, boolean selected) {
        if (available) {
            OverlayUtil.fillRect(g, x, y, 16, 16, ItemSelSideBar.color(255, 255, 255, a));
        } else {
            OverlayUtil.fillRect(g, x, y, 16, 16, ItemSelSideBar.color(255, 0, 0, a));
        }
        if (selected) {
            OverlayUtil.drawRect(g, x, y, 16, 16, ItemSelSideBar.color(255, 170, 0, 255));
        }
    }

    @Override
    protected int getXOffset(int width) {
        float progress = (this.max_ease - this.ease_time) / this.max_ease;
        return Math.round(this.onCenter() ? (float) width / 2.0F - 54.0F - 1.0F - progress * (float) width / 2.0F : 2.0F - progress * 20.0F);
    }

    @Override
    protected int getYOffset(int height) {
        return height / 2 - 81 + 1;
    }
}