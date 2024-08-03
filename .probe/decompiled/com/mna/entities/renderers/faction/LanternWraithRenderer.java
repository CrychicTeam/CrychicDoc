package com.mna.entities.renderers.faction;

import com.mna.entities.faction.LanternWraith;
import com.mna.entities.models.faction.LanternWraithModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class LanternWraithRenderer extends MAGeckoRenderer<LanternWraith> {

    public LanternWraithRenderer(EntityRendererProvider.Context context) {
        super(context, new LanternWraithModel());
    }
}