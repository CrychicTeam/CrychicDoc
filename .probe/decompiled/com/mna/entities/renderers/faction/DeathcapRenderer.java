package com.mna.entities.renderers.faction;

import com.mna.entities.faction.Deathcap;
import com.mna.entities.models.faction.DeathcapModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class DeathcapRenderer extends MAGeckoFactionRenderer<Deathcap> {

    public DeathcapRenderer(EntityRendererProvider.Context context) {
        super(context, new DeathcapModel());
        this.enableEmissive();
    }
}