package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.gui.stats.sorting.IStatSorter;
import se.mickelus.tetra.gui.stats.sorting.StatSorters;
import se.mickelus.tetra.module.schematic.OutcomePreview;

@ParametersAreNonnullByDefault
public class HoloVariantListGui extends GuiElement {

    private final GuiHorizontalScrollable groupsScroll;

    private final GuiHorizontalLayoutGroup groups;

    private final Consumer<OutcomePreview> onVariantHover;

    private final Consumer<OutcomePreview> onVariantBlur;

    private final Consumer<OutcomePreview> onVariantSelect;

    private boolean filterCategory = false;

    private String filter = "";

    private IStatSorter sorter = StatSorters.none;

    private OutcomePreview[] previews;

    public HoloVariantListGui(int x, int y, int width, Consumer<OutcomePreview> onVariantHover, Consumer<OutcomePreview> onVariantBlur, Consumer<OutcomePreview> onVariantSelect) {
        super(x, y, width, 50);
        this.groupsScroll = new GuiHorizontalScrollable(0, 0, width, this.height).setGlobal(true);
        this.addChild(this.groupsScroll);
        this.groups = new GuiHorizontalLayoutGroup(0, 0, this.height, 12);
        this.groupsScroll.addChild(this.groups);
        this.onVariantHover = onVariantHover;
        this.onVariantBlur = onVariantBlur;
        this.onVariantSelect = onVariantSelect;
    }

    public void update(OutcomePreview[] previews) {
        this.previews = previews;
        this.filter = "";
        this.sorter = StatSorters.none;
        this.update();
    }

    private void update() {
        this.groups.clearChildren();
        Player player = Minecraft.getInstance().player;
        boolean isDevelopment = ConfigHandler.development.get();
        Map<String, List<OutcomePreview>> result = (Map<String, List<OutcomePreview>>) Arrays.stream(this.previews).filter(preview -> isDevelopment || preview.materials.length != 0).filter(this::filter).collect(Collectors.groupingBy(preview -> preview.category, LinkedHashMap::new, Collectors.toList()));
        if (this.sorter != StatSorters.none) {
            result.values().forEach(category -> category.sort(this.sorter.compare(player, preview -> preview.itemStack)));
        }
        int offset = 0;
        for (Entry<String, List<OutcomePreview>> entry : result.entrySet()) {
            this.groups.addChild(new HoloVariantGroupGui(0, 0, (String) entry.getKey(), (List<OutcomePreview>) entry.getValue(), offset, this.sorter, player, this.onVariantHover, this.onVariantBlur, this.onVariantSelect));
            offset += ((List) entry.getValue()).size();
        }
        this.groupsScroll.markDirty();
    }

    public void updateSelection(OutcomePreview outcome) {
        this.groups.getChildren(HoloVariantGroupGui.class).forEach(group -> group.updateSelection(outcome));
    }

    @Override
    protected void onShow() {
        this.groups.getChildren(HoloVariantGroupGui.class).forEach(HoloVariantGroupGui::animateIn);
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
        return this.isVisible() ? super.onMouseScroll(mouseX, mouseY, distance) : false;
    }

    @Override
    public boolean onCharType(char character, int modifiers) {
        return character == 'f';
    }

    private boolean filter(OutcomePreview preview) {
        if (this.filter.isEmpty()) {
            return true;
        } else {
            return this.filterCategory ? preview.category.contains(this.filter) : preview.variantName.toLowerCase().contains(this.filter);
        }
    }

    public void updateFilter(String newValue) {
        this.filter = newValue.toLowerCase();
        this.filterCategory = this.filter.startsWith("#");
        if (this.filterCategory) {
            this.filter = this.filter.substring(1);
        }
        this.update();
    }

    public void changeSorting(IStatSorter sorter) {
        this.sorter = sorter;
        this.update();
    }
}