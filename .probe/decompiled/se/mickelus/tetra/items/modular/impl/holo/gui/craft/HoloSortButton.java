package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.stats.sorting.IStatSorter;
import se.mickelus.tetra.gui.stats.sorting.StatSorters;
import se.mickelus.tetra.module.schematic.OutcomePreview;

@ParametersAreNonnullByDefault
public class HoloSortButton extends GuiElement {

    private final List<Component> tooltip;

    private final GuiString label;

    private final GuiTexture icon;

    private final HoloSortPopover popover;

    Consumer<IStatSorter> onSelect;

    public HoloSortButton(int x, int y, Consumer<IStatSorter> onSelect) {
        super(x, y, 36, 9);
        this.onSelect = onSelect;
        this.icon = new GuiTexture(-3, -3, 16, 16, 48, 0, GuiTextures.holo);
        this.addChild(this.icon);
        this.label = new GuiString(11, 0, this.width - 11, "");
        this.addChild(this.label);
        this.popover = new HoloSortPopover(0, 11, this::onSelect);
        this.addChild(this.popover);
        this.tooltip = Collections.singletonList(Component.translatable("tetra.holo.craft.variants_sort"));
    }

    public void update(OutcomePreview[] previews) {
        this.label.setString(StatSorters.none.getName());
        if (previews.length > 0) {
            Player player = Minecraft.getInstance().player;
            this.popover.update((IStatSorter[]) StatSorters.sorters.stream().filter(sorter -> Arrays.stream(previews).anyMatch(preview -> sorter.getWeight(player, preview.itemStack) > 0)).toArray(IStatSorter[]::new));
        }
        this.popover.setVisible(false);
        this.icon.setColor(16777215);
        this.label.setColor(16777215);
    }

    private void onSelect(IStatSorter sorter) {
        String name = sorter.getName();
        this.label.setString(name.length() > 4 ? name.substring(0, 4) : name);
        this.icon.setColor(16777215);
        this.label.setColor(16777215);
        this.onSelect.accept(sorter);
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (super.hasFocus()) {
            this.togglePopover(!this.popover.isVisible());
            return true;
        } else {
            return super.onMouseClick(x, y, button);
        }
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() && !this.popover.isVisible() ? this.tooltip : null;
    }

    private void togglePopover(boolean visible) {
        this.icon.setColor(visible ? 16777164 : 16777215);
        this.label.setColor(visible ? 16777164 : 16777215);
        this.popover.setVisible(visible);
    }

    @Override
    public boolean hasFocus() {
        return super.hasFocus() || this.isBlockingFocus();
    }

    public boolean isBlockingFocus() {
        return this.popover.isVisible() && this.popover.hasFocus();
    }

    @Override
    public boolean onCharType(char character, int modifiers) {
        if (character == 's') {
            this.togglePopover(!this.popover.isVisible());
            return true;
        } else {
            return super.onCharType(character, modifiers);
        }
    }

    public void reset() {
        this.label.setString(StatSorters.none.getName());
    }
}