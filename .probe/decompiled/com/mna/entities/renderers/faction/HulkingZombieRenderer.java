package com.mna.entities.renderers.faction;

import com.mna.entities.faction.HulkingZombie;
import com.mna.entities.models.faction.HulkingZombieModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class HulkingZombieRenderer extends MAGeckoRenderer<HulkingZombie> {

    public HulkingZombieRenderer(EntityRendererProvider.Context context) {
        super(context, new HulkingZombieModel());
    }
}