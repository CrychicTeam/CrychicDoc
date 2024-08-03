package com.mna.items.renderers;

import com.mna.items.armor.CouncilArmorItem;
import com.mna.items.armor.models.CouncilArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class CouncilArmorRenderer extends GeoArmorRenderer<CouncilArmorItem> {

    public CouncilArmorRenderer() {
        super(new CouncilArmorModel());
    }
}