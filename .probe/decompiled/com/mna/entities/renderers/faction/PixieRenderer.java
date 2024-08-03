package com.mna.entities.renderers.faction;

import com.mna.entities.faction.Pixie;
import com.mna.entities.models.faction.PixieModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PixieRenderer extends MAGeckoFactionRenderer<Pixie> {

    public PixieRenderer(EntityRendererProvider.Context context) {
        super(context, new PixieModel());
    }
}