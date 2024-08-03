package journeymap.client.ui.waypoint;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import journeymap.client.Constants;
import journeymap.client.data.WorldData;
import journeymap.client.ui.component.DropDownButton;
import journeymap.client.ui.component.DropDownItem;
import journeymap.client.waypoint.WaypointStore;
import net.minecraft.client.gui.components.Button;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimensionsDropDownButton extends DropDownButton {

    static WorldData.DimensionProvider currentWorldProvider;

    final List<WorldData.DimensionProvider> dimensionProviders = WorldData.getDimensionProviders(WaypointStore.INSTANCE.getLoadedDimensions());

    private final Map<String, DropDownItem> itemMap = Maps.newHashMap();

    private final DropDownItem all = new DropDownItem(this, null, Constants.getString("jm.waypoint.dimension", Constants.getString("jm.waypoint.dimension_all")), Constants.getString("jm.waypoint.dimension_all"));

    public DimensionsDropDownButton(Button.OnPress onPress) {
        super("", onPress);
        this.setItems(this.createListItems());
        this.setSelected(currentWorldProvider);
    }

    private List<DropDownItem> createListItems() {
        List<DropDownItem> list = Lists.newArrayList();
        list.add(this.all);
        this.addVanillaDim("minecraft:overworld", list);
        this.addVanillaDim("minecraft:the_nether", list);
        this.addVanillaDim("minecraft:the_end", list);
        for (WorldData.DimensionProvider item : this.dimensionProviders) {
            if (!"minecraft:the_nether".equals(item.getDimensionId()) && !"minecraft:the_end".equals(item.getDimensionId()) && !"minecraft:overworld".equals(item.getDimensionId())) {
                list.add(this.dropDown(item));
            }
        }
        return list;
    }

    private void addVanillaDim(String name, List<DropDownItem> list) {
        this.dimensionProviders.forEach(provider -> {
            if (name.equals(provider.getDimensionId())) {
                list.add(this.dropDown(provider));
            }
        });
    }

    private DropDownItem dropDown(WorldData.DimensionProvider dim) {
        DropDownItem dropDownItem = new DropDownItem(this, dim, dim.getName(), dim.getDimensionId());
        this.itemMap.put(dim.getDimensionId(), dropDownItem);
        return dropDownItem;
    }

    public void setDim(ResourceKey<Level> dimension) {
        if (dimension != null) {
            WorldData.DimensionProvider provider = (WorldData.DimensionProvider) this.dimensionProviders.stream().filter(dimensionProvider -> dimension.equals(dimensionProvider.getDimension())).findFirst().orElse(null);
            currentWorldProvider = provider;
            this.setSelected((DropDownItem) this.itemMap.get(currentWorldProvider.getDimensionId()));
        } else {
            currentWorldProvider = null;
            this.setSelected(this.all);
        }
    }

    private void setSelected(WorldData.DimensionProvider provider) {
        DropDownItem selected = provider == null ? this.all : (DropDownItem) this.itemMap.get(provider.getDimensionId());
        currentWorldProvider = selected != null ? (WorldData.DimensionProvider) selected.getId() : null;
        this.setSelected(selected);
    }

    @Override
    public void setSelected(DropDownItem selectedButton) {
        this.selected = selectedButton;
        if (this.selected == null && currentWorldProvider == null) {
            super.setSelected(this.all);
        } else {
            currentWorldProvider = this.selected != null ? (WorldData.DimensionProvider) this.selected.getId() : null;
            super.setSelected(this.selected);
        }
    }

    private class DimSorter implements Comparator<DropDownItem> {

        public int compare(DropDownItem o1, DropDownItem o2) {
            String s1 = o1.getLabel();
            String s2 = o2.getLabel();
            boolean b1 = o1.equals(DimensionsDropDownButton.this.all) || o1.getLabel().equals("Overworld") || o1.getLabel().equals("The Nether") || o1.getLabel().equals("The End");
            boolean b2 = o2.equals(DimensionsDropDownButton.this.all) || o2.getLabel().equals("Overworld") || o2.getLabel().equals("The Nether") || o2.getLabel().equals("The End");
            return b1 ? (b2 ? 0 : 1) : (b2 ? -1 : s1.compareTo(s2));
        }
    }
}