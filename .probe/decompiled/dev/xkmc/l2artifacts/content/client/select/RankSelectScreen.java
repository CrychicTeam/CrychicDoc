package dev.xkmc.l2artifacts.content.client.select;

import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2artifacts.network.ChooseArtifactToServer;
import dev.xkmc.l2artifacts.network.NetworkManager;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class RankSelectScreen extends AbstractSelectScreen {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "rank_select");

    private final int set;

    private final int slot;

    protected RankSelectScreen(int set, int slot) {
        super(LangData.TITLE_SELECT_SLOT.get(), MANAGER, "set", "slot", "rank");
        this.set = set;
        this.slot = slot;
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mx, int my) {
        g.drawString(this.f_96547_, LangData.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
        g.drawString(this.f_96547_, LangData.TITLE_SELECT_SLOT.get(), 8, 37, 4210752, false);
        g.drawString(this.f_96547_, LangData.TITLE_SELECT_RANK.get(), 8, 68, 4210752, false);
    }

    @Override
    protected ItemStack getStack(String comp, int x, int y) {
        SetEntry<?> setEntry = (SetEntry<?>) L2Artifacts.REGISTRATE.SET_LIST.get(this.set);
        if (comp.equals("set")) {
            return setEntry.items[0][setEntry.items[0].length - 1].asStack();
        } else if (comp.equals("slot")) {
            return setEntry.items[this.slot][setEntry.items[this.slot].length - 1].asStack();
        } else {
            int n = setEntry.items[this.slot].length;
            return x < n ? setEntry.items[this.slot][x].asStack() : ItemStack.EMPTY;
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
            } else if (result.name().equals("slot")) {
                Minecraft.getInstance().setScreen(new SlotSelectScreen(this.set));
                return true;
            } else {
                int ind = result.x();
                SetEntry<?> setEntry = (SetEntry<?>) L2Artifacts.REGISTRATE.SET_LIST.get(this.set);
                int n = setEntry.items[this.slot].length;
                if (ind >= n) {
                    return false;
                } else {
                    Minecraft.getInstance().setScreen(null);
                    NetworkManager.HANDLER.toServer(new ChooseArtifactToServer(this.set, this.slot, ind));
                    return true;
                }
            }
        }
    }
}