package com.mna.entities.renderers.faction;

import com.mna.entities.faction.Chatterer;
import com.mna.entities.models.faction.ChattererModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChattererRenderer extends MAGeckoFactionRenderer<Chatterer> {

    public ChattererRenderer(EntityRendererProvider.Context context) {
        super(context, new ChattererModel());
        this.enableEmissive();
    }
}