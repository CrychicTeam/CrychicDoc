package journeymap.client.ui.dialog;

import com.google.common.collect.Maps;
import java.util.Map;
import journeymap.client.Constants;
import journeymap.client.api.impl.OptionsDisplayFactory;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import net.minecraft.client.gui.screens.Screen;

public class AddonOptionsManager extends OptionsManager {

    public AddonOptionsManager(Screen returnDisplay) {
        super(Constants.getString("jm.common.addon_options"), returnDisplay);
    }

    @Override
    protected Map<Category, PropertiesBase> getSlots() {
        Map<Category, PropertiesBase> slotMap = Maps.newHashMap();
        OptionsDisplayFactory.PROPERTIES_REGISTRY.forEach((modId, prop) -> slotMap.put(prop.getParentCategory(), prop));
        return slotMap;
    }

    @Override
    public void init() {
        super.init();
        this.buttonAddons.setEnabled(false);
        this.clientOptions.setEnabled(true);
    }
}