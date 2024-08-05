package journeymap.client.ui.waypoint;

import java.util.List;
import journeymap.client.Constants;
import journeymap.client.data.WorldData;
import journeymap.client.ui.component.Button;
import journeymap.client.waypoint.WaypointStore;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

class DimensionsButton extends Button {

    static boolean needInit = true;

    static WorldData.DimensionProvider currentWorldProvider;

    final List<WorldData.DimensionProvider> dimensionProviders = WorldData.getDimensionProviders(WaypointStore.INSTANCE.getLoadedDimensions());

    static int index = -1;

    public DimensionsButton(net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(0, 0, "", onPress);
        if (needInit) {
            currentWorldProvider = null;
            needInit = false;
        }
        this.updateLabel();
        this.fitWidth(Minecraft.getInstance().font);
    }

    public void setDim(ResourceKey<Level> dim) {
        index = -1;
        WorldData.DimensionProvider provider = (WorldData.DimensionProvider) this.dimensionProviders.stream().filter(dimensionProvider -> dim.equals(dimensionProvider.getDimension())).findFirst().orElse(null);
        for (WorldData.DimensionProvider p : this.dimensionProviders) {
            index++;
            this.getProvider();
            if (p.equals(provider)) {
                return;
            }
        }
    }

    @Override
    protected void updateLabel() {
        String dimName;
        if (currentWorldProvider != null) {
            dimName = currentWorldProvider.getName();
        } else {
            dimName = Constants.getString("jm.waypoint.dimension_all");
        }
        this.m_93666_(Constants.getStringTextComponent(Constants.getString("jm.waypoint.dimension", dimName)));
    }

    @Override
    public int getFitWidth(Font fr) {
        int maxWidth = 0;
        for (WorldData.DimensionProvider dimensionProvider : this.dimensionProviders) {
            String name = Constants.getString("jm.waypoint.dimension", WorldData.getSafeDimensionName(dimensionProvider));
            maxWidth = Math.max(maxWidth, Minecraft.getInstance().font.width(name));
        }
        return maxWidth + 12;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if (clicked && button == 0) {
            index++;
            this.getProvider();
        } else if (clicked && button == 1) {
            index--;
            this.getProvider();
        }
        if (clicked) {
            super.onPress();
        }
        return clicked;
    }

    @Override
    public void onPress() {
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    private void getProvider() {
        if (index >= this.dimensionProviders.size()) {
            index = -1;
        } else if (index < -1) {
            index = this.dimensionProviders.size() - 1;
        }
        if (index < this.dimensionProviders.size() && index >= 0) {
            currentWorldProvider = (WorldData.DimensionProvider) this.dimensionProviders.get(index);
        } else {
            currentWorldProvider = null;
        }
        this.updateLabel();
    }
}