package io.redspace.ironsspellbooks.entity.mobs.wizards.pyromancer;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PyromancerRenderer extends AbstractSpellCastingMobRenderer {

    public PyromancerRenderer(EntityRendererProvider.Context context) {
        super(context, new PyromancerModel());
    }
}