package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.mutil.gui.impl.GuiHorizontalLayoutGroup;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.module.data.MaterialData;

@ParametersAreNonnullByDefault
public class HoloMaterialListGui extends GuiElement {

    private final GuiHorizontalScrollable groupsScroll;

    private final GuiHorizontalLayoutGroup groups;

    private final HoloMaterialDetailGui detail;

    private final KeyframeAnimation openAnimation;

    private MaterialData selectedItem;

    private MaterialData hoveredItem;

    public HoloMaterialListGui(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.groupsScroll = new GuiHorizontalScrollable(0, 0, width, 75).setGlobal(true);
        this.addChild(this.groupsScroll);
        this.groups = new GuiHorizontalLayoutGroup(0, 0, 75, 12);
        this.groupsScroll.addChild(this.groups);
        this.detail = new HoloMaterialDetailGui(0, 76, width);
        this.detail.setVisible(false);
        this.addChild(this.detail);
        this.openAnimation = new KeyframeAnimation(200, this).applyTo(new Applier.TranslateY((float) (y - 4), (float) y), new Applier.Opacity(0.0F, 1.0F)).withDelay(800);
        this.updateGroups();
    }

    protected void updateGroups() {
        this.groups.clearChildren();
        boolean isDevelopment = ConfigHandler.development.get();
        Map<String, List<MaterialData>> result = (Map<String, List<MaterialData>>) DataManager.instance.materialData.getData().values().stream().filter(data -> data.material != null).filter(data -> !data.hidden).filter(data -> isDevelopment || data.material.getApplicableItemStacks().length > 0).collect(Collectors.groupingBy(data -> data.category, LinkedHashMap::new, Collectors.toList()));
        int offset = 0;
        for (Entry<String, List<MaterialData>> entry : result.entrySet()) {
            this.groups.addChild(new HoloMaterialGroupGui(0, 0, (String) entry.getKey(), (List<MaterialData>) entry.getValue(), offset, this::onHover, this::onBlur, this::onSelect));
            offset += ((List) entry.getValue()).size();
        }
        this.groupsScroll.markDirty();
    }

    @Override
    protected void onShow() {
        this.onHover(null);
        this.groups.getChildren(HoloMaterialGroupGui.class).forEach(HoloMaterialGroupGui::animateIn);
    }

    public void animateOpen() {
        this.openAnimation.start();
    }

    private void onHover(MaterialData material) {
        this.hoveredItem = material;
        this.detail.update(this.selectedItem, this.hoveredItem);
    }

    private void onBlur(MaterialData material) {
        if (material.equals(this.hoveredItem)) {
            this.detail.update(this.selectedItem, null);
        }
    }

    private void onSelect(MaterialData material) {
        this.selectedItem = material;
        this.groups.getChildren(HoloMaterialGroupGui.class).forEach(group -> group.updateSelection(material));
        this.detail.update(this.selectedItem, this.hoveredItem);
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        if (button == 1) {
            this.hoveredItem = null;
            this.onSelect(null);
            return true;
        } else {
            return super.onMouseClick(x, y, button);
        }
    }

    public void reload() {
        this.updateGroups();
        if (this.isVisible()) {
            this.animateOpen();
        }
    }
}