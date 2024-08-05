package io.redspace.ironsspellbooks.entity.mobs.debug_wizard;

import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobModel;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMobRenderer;
import io.redspace.ironsspellbooks.render.DebugWizardSpellName;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class DebugWizardRenderer extends AbstractSpellCastingMobRenderer {

    public DebugWizardRenderer(EntityRendererProvider.Context context) {
        super(context, new AbstractSpellCastingMobModel() {

            @Override
            public ResourceLocation getTextureResource(AbstractSpellCastingMob mob) {
                return AbstractSpellCastingMob.textureResource;
            }
        });
        this.addRenderLayer(new DebugWizardSpellName(this));
    }
}