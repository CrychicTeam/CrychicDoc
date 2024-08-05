package journeymap.client.ui.component;

import journeymap.client.Constants;
import journeymap.common.properties.catagory.Category;

public class ResetButton extends Button {

    public final Category category;

    public ResetButton(Category category) {
        super(Constants.getString("jm.config.reset"));
        this.category = category;
        this.setTooltip(new String[] { Constants.getString("jm.config.reset.tooltip") });
        this.setDrawBackground(false);
        this.setLabelColors(Integer.valueOf(16711680), Integer.valueOf(16711680), null);
    }

    public ResetButton(Category category, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(Constants.getString("jm.config.reset"), onPress);
        this.category = category;
        this.setTooltip(new String[] { Constants.getString("jm.config.reset.tooltip") });
        this.setDrawBackground(false);
        this.setLabelColors(Integer.valueOf(16711680), Integer.valueOf(16711680), null);
    }
}