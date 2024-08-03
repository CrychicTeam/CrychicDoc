package journeymap.client.properties;

import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import net.minecraft.client.Minecraft;

public class FullMapProperties extends InGameMapProperties {

    public final BooleanField showKeys = new BooleanField(Category.Inherit, "jm.common.show_keys", true);

    public final BooleanField showPlayerLoc = new BooleanField(Category.Inherit, "jm.common.show_player_loc", true);

    public final BooleanField showMouseLoc = new BooleanField(Category.Inherit, "jm.common.show_mouse_loc", true);

    @Override
    public void postLoad(boolean isNew) {
        super.postLoad(isNew);
        if (isNew && Minecraft.getInstance() != null && Minecraft.getInstance().font.isBidirectional()) {
            super.fontScale.set(Float.valueOf(2.0F));
        }
    }

    @Override
    public String getName() {
        return "fullmap";
    }
}