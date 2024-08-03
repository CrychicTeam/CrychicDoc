package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.tools.SummonUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ComponentCauterize extends SpellEffect {

    public ComponentCauterize(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 2.0F, 1.0F, 40.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity()) {
            boolean hasSet = ItemInit.DEMON_ARMOR_CHEST.get().isSetEquipped(target.getLivingEntity());
            if (!target.getLivingEntity().m_5825_() && target.getLivingEntity().getEffect(MobEffects.FIRE_RESISTANCE) == null) {
                int magnitude = (int) modificationData.getValue(Attribute.MAGNITUDE);
                if (source.isPlayerCaster() && target.isEntity() && target.getEntity() != source.getCaster() && !SummonUtils.isTargetFriendly(target.getEntity(), source.getCaster())) {
                    magnitude = 1;
                }
                int damage = 2 << magnitude - 1;
                boolean shouldHeal = !hasSet ? target.getLivingEntity().hurt(DamageHelper.createSourcedType(DamageTypes.ON_FIRE, context.getLevel().registryAccess(), source.getCaster()), (float) damage) : false;
                if (!shouldHeal && target.getLivingEntity() instanceof Player) {
                    Player playerTarget = (Player) target.getLivingEntity();
                    boolean hasBracelet = CuriosInterop.IsItemInCurioSlot(ItemInit.EMBERGLOW_BRACELET.get(), playerTarget, SlotTypePreset.BRACELET);
                    if (hasBracelet && !hasSet) {
                        shouldHeal = ItemInit.EMBERGLOW_BRACELET.get().isEquippedAndHasMana(playerTarget, (float) (damage * 4), true);
                    } else if (!hasBracelet && hasSet) {
                        playerTarget.getArmorSlots().forEach(is -> is.hurtAndBreak(damage * 4, playerTarget, e -> {
                        }));
                        shouldHeal = true;
                    } else if (hasBracelet && hasSet) {
                        shouldHeal = true;
                    }
                }
                if (shouldHeal) {
                    target.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, magnitude - 1, false, false));
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.FIRE;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10) {
            Random rndm = new Random();
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FLAME.get()), caster), impact_position.x + (double) rndm.nextFloat() - 0.5, impact_position.y + (double) rndm.nextFloat() - 0.5, impact_position.z + (double) rndm.nextFloat() - 0.5, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.DEMONS;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.FRIENDLY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.FIRE, Affinity.LIGHTNING);
    }
}