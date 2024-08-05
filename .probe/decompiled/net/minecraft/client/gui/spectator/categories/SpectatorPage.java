package net.minecraft.client.gui.spectator.categories;

import com.google.common.base.MoreObjects;
import java.util.List;
import net.minecraft.client.gui.spectator.SpectatorMenu;
import net.minecraft.client.gui.spectator.SpectatorMenuItem;

public class SpectatorPage {

    public static final int NO_SELECTION = -1;

    private final List<SpectatorMenuItem> items;

    private final int selection;

    public SpectatorPage(List<SpectatorMenuItem> listSpectatorMenuItem0, int int1) {
        this.items = listSpectatorMenuItem0;
        this.selection = int1;
    }

    public SpectatorMenuItem getItem(int int0) {
        return int0 >= 0 && int0 < this.items.size() ? (SpectatorMenuItem) MoreObjects.firstNonNull((SpectatorMenuItem) this.items.get(int0), SpectatorMenu.EMPTY_SLOT) : SpectatorMenu.EMPTY_SLOT;
    }

    public int getSelectedSlot() {
        return this.selection;
    }
}