package journeymap.client.ui.component;

import java.util.Collection;
import java.util.List;
import journeymap.client.ui.option.SlotMetadata;
import net.minecraft.client.gui.components.AbstractSelectionList;

public abstract class Slot extends AbstractSelectionList.Entry {

    public abstract Collection<SlotMetadata> getMetadata();

    @Override
    public abstract boolean charTyped(char var1, int var2);

    @Override
    public abstract boolean keyPressed(int var1, int var2, int var3);

    public abstract List<? extends Slot> getChildSlots(int var1, int var2);

    public abstract SlotMetadata getLastPressed();

    public abstract SlotMetadata getCurrentTooltip();

    public abstract void setEnabled(boolean var1);

    public abstract int getColumnWidth();

    public abstract boolean contains(SlotMetadata var1);

    public void displayHover(boolean enabled) {
    }
}