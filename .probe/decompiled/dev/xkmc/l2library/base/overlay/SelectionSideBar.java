package dev.xkmc.l2library.base.overlay;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public abstract class SelectionSideBar<T, S extends SideBar.Signature<S>> extends SideBar<S> implements IGuiOverlay {

    public SelectionSideBar(float duration, float ease) {
        super(duration, ease);
    }

    public abstract Pair<List<T>, Integer> getItems();

    public abstract boolean isAvailable(T var1);

    public abstract boolean onCenter();

    public void initRender() {
    }

    @Override
    public void render(ForgeGui gui, GuiGraphics g, float partialTick, int width, int height) {
        if (this.ease((float) gui.m_93079_() + partialTick)) {
            this.initRender();
            gui.setupOverlayRenderState(true, false);
            int x0 = this.getXOffset(width);
            int y0 = this.getYOffset(height);
            SelectionSideBar.Context ctx = new SelectionSideBar.Context(gui, g, partialTick, Minecraft.getInstance().font, x0, y0);
            this.renderContent(ctx);
        }
    }

    public void renderContent(SelectionSideBar.Context ctx) {
        Pair<List<T>, Integer> content = this.getItems();
        List<T> list = (List<T>) content.getFirst();
        for (int i = 0; i < list.size(); i++) {
            this.renderEntry(ctx, (T) list.get(i), i, (Integer) content.getSecond());
        }
    }

    protected abstract void renderEntry(SelectionSideBar.Context var1, T var2, int var3, int var4);

    public static record Context(ForgeGui gui, GuiGraphics g, float pTick, Font font, int x0, int y0) {

        public void renderItem(ItemStack stack, int x, int y) {
            if (!stack.isEmpty()) {
                this.g.renderItem(stack, x, y);
                this.g.renderItemDecorations(this.font, stack, x, y);
            }
        }
    }
}