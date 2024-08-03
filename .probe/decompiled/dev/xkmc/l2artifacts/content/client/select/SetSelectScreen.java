package dev.xkmc.l2artifacts.content.client.select;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.l2artifacts.content.core.BaseArtifact;
import dev.xkmc.l2artifacts.init.L2Artifacts;
import dev.xkmc.l2artifacts.init.data.LangData;
import dev.xkmc.l2artifacts.init.registrate.entries.SetEntry;
import dev.xkmc.l2library.base.menu.base.SpriteManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class SetSelectScreen extends AbstractSelectScreen {

    private static final SpriteManager MANAGER = new SpriteManager("l2artifacts", "set_select");

    public SetSelectScreen() {
        super(LangData.TITLE_SELECT_SET.get(), MANAGER, "grid");
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mx, int my) {
        g.drawString(this.f_96547_, LangData.TITLE_SELECT_SET.get(), 8, 6, 4210752, false);
    }

    @Override
    protected ItemStack getStack(String comp, int x, int y) {
        int ind = x + y * 9;
        if (ind >= L2Artifacts.REGISTRATE.SET_LIST.size()) {
            return ItemStack.EMPTY;
        } else {
            ItemEntry<BaseArtifact>[] arr = ((SetEntry) L2Artifacts.REGISTRATE.SET_LIST.get(ind)).items[0];
            return arr[arr.length - 1].asStack();
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
            } else {
                int ind = result.x() + result.y() * 9;
                if (ind >= L2Artifacts.REGISTRATE.SET_LIST.size()) {
                    return false;
                } else {
                    Minecraft.getInstance().setScreen(new SlotSelectScreen(ind));
                    return true;
                }
            }
        }
    }
}