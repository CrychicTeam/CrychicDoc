package com.mna.items.renderers;

import com.mna.items.armor.BoneArmorItem;
import com.mna.items.armor.models.BoneArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BoneArmorRenderer extends GeoArmorRenderer<BoneArmorItem> {

    public BoneArmorRenderer() {
        super(new BoneArmorModel());
    }
}