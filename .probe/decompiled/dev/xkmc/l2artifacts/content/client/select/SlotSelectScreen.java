package dev.xkmc.l2artifacts.content.client.select;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class SlotSelectScreen extends AbstractSelectScreen {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "slot_select");

    private final int set;

    protected SlotSelectScreen(int set) {
        super(LangData.TITLE_SELECT_SLOT.get(), MANAGER, "set", "slot");
        this.set = set;
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mx, int my) {
        g.drawString(this.f_96547_, LangData.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
        g.drawString(this.f_96547_, LangData.TITLE_SELECT_SLOT.get(), 8, 37, 4210752, false);
    }

    @Override
    protected ItemStack getStack(String comp, int x, int y) {
        SetEntry<?> setEntry = (SetEntry<?>) L2Artifacts.REGISTRATE.SET_LIST.get(this.set);
        if (comp.equals("set")) {
            return setEntry.items[0][setEntry.items[0].length - 1].asStack();
        } else {
            int n = setEntry.items.length;
            return x < n ? setEntry.items[x][setEntry.items[x].length - 1].asStack() : ItemStack.EMPTY;
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        if (super.m_6375_(mx, my, button)) {
            return true;
        } else {
            AbstractSelectScreen.SlotResult result = this.findSlot(mx, my);
            if (result == null) {
                return false;
            } else if (result.name().equals("set")) {
                Minecraft.getInstance().setScreen(new SetSelectScreen());
                return true;
            } else {
                int ind = result.x();
                SetEntry<?> setEntry = (SetEntry<?>) L2Artifacts.REGISTRATE.SET_LIST.get(this.set);
                int n = setEntry.items.length;
                if (ind >= n) {
                    return false;
                } else {
                    Minecraft.getInstance().setScreen(new RankSelectScreen(this.set, ind));
                    return true;
                }
            }
        }
    }
}