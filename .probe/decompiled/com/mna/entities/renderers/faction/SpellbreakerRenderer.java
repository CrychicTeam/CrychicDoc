package com.mna.entities.renderers.faction;

import com.mna.entities.faction.Spellbreaker;
import com.mna.entities.models.faction.SpellbreakerModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class SpellbreakerRenderer extends MAGeckoRenderer<Spellbreaker> {

    public SpellbreakerRenderer(EntityRendererProvider.Context context) {
        super(context, new SpellbreakerModel());
    }
}