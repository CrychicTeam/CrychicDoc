package com.mna.entities.renderers.boss;

import com.mna.entities.boss.PigDragon;
import com.mna.entities.models.boss.PigDragonModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PigDragonRenderer extends MAGeckoRenderer<PigDragon> {

    public PigDragonRenderer(EntityRendererProvider.Context context) {
        super(context, new PigDragonModel());
    }
}