package com.mna.spells.components.boss;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.effects.EffectInit;
import com.mna.spells.components.PotionEffectComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class ComponentChill extends PotionEffectComponent {

    public ComponentChill(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.CHILL, new AttributeValuePair(Attribute.MAGNITUDE, 0.0F, 0.0F, 5.0F, 1.0F, 20.0F), new AttributeValuePair(Attribute.DURATION, 10.0F, 5.0F, 60.0F, 5.0F, 20.0F), new AttributeValuePair(Attribute.RADIUS, 3.0F, 1.0F, 15.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 20.0F, 1.0F, 5.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity() && ((LivingEntity) target.getEntity()).hasEffect(EffectInit.HEATWAVE.get())) {
            ((LivingEntity) target.getEntity()).removeEffect(EffectInit.HEATWAVE.get());
            context.getServerLevel().m_6249_(source.getCaster(), target.getEntity().getBoundingBox().inflate((double) modificationData.getValue(Attribute.RADIUS)), e -> e instanceof LivingEntity && !this.isTargetFriendlyToCaster(source, e)).stream().map(e -> (LivingEntity) e).forEach(e -> e.hurt(e.m_269291_().freeze(), modificationData.getValue(Attribute.DAMAGE)));
            return ComponentApplicationResult.SUCCESS;
        } else {
            return super.ApplyEffect(source, target, modificationData, context);
        }
    }

    @Override
    public int requiredXPForRote() {
        return 999;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ICE;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean canBeOnRandomStaff() {
        return false;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public boolean isCraftable(SpellCraftingContext context) {
        return false;
    }

    @Override
    public boolean isSilverSpell() {
        return true;
    }
}