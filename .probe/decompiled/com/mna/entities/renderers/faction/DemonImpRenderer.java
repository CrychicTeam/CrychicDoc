package com.mna.entities.renderers.faction;

import com.mna.entities.faction.DemonImp;
import com.mna.entities.models.faction.DemonImpModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DemonImpRenderer extends MAGeckoRenderer<DemonImp> {

    public DemonImpRenderer(EntityRendererProvider.Context context) {
        super(context, new DemonImpModel());
    }
}