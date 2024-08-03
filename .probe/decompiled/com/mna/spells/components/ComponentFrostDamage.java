package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IDamageComponent;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.config.GeneralConfig;
import com.mna.effects.EffectInit;
import com.mna.tools.BlockUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ComponentFrostDamage extends SpellEffect implements IDamageComponent {

    public ComponentFrostDamage(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 20.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.DURATION, 3.0F, 0.0F, 10.0F, 1.0F, 2.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isEntity()) {
            if (target.isLivingEntity()) {
                if (target.getEntity().canFreeze()) {
                    int addedTicks = (int) (20.0F * modificationData.getValue(Attribute.DAMAGE));
                    if (target.getLivingEntity().hasEffect(EffectInit.SOAKED.get())) {
                        addedTicks *= 2;
                    }
                    target.getEntity().setTicksFrozen(target.getEntity().getTicksFrozen() + addedTicks);
                }
                float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
                target.getEntity().hurt(DamageHelper.createSourcedType(DamageHelper.FROST, context.getLevel().registryAccess(), source.getCaster()), damage);
                if (target.isLivingEntity()) {
                    target.getLivingEntity().addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, (int) modificationData.getValue(Attribute.DURATION) * 20));
                }
                return ComponentApplicationResult.SUCCESS;
            }
        } else if (target.isBlock()) {
            BlockState state = context.getServerLevel().m_8055_(target.getBlock());
            if (state.m_60734_() == Blocks.WATER && state.m_61138_(LiquidBlock.LEVEL)) {
                if ((Integer) state.m_61143_(LiquidBlock.LEVEL) == 0) {
                    BlockUtils.placeBlock(context.getServerLevel(), target.getBlock(), Direction.UP, modificationData.getValue(Attribute.DAMAGE) >= 10.0F ? Blocks.ICE.defaultBlockState() : Blocks.FROSTED_ICE.defaultBlockState(), source.getPlayer());
                    return ComponentApplicationResult.SUCCESS;
                }
            } else {
                if (state.m_60734_() == Blocks.LAVA && state.m_61138_(LiquidBlock.LEVEL) && modificationData.getValue(Attribute.DAMAGE) >= 10.0F) {
                    if ((Integer) state.m_61143_(LiquidBlock.LEVEL) == 0) {
                        BlockUtils.placeBlock(context.getServerLevel(), target.getBlock(), Direction.UP, Blocks.OBSIDIAN.defaultBlockState(), source.getPlayer());
                    } else {
                        BlockUtils.placeBlock(context.getServerLevel(), target.getBlock(), Direction.UP, Blocks.COBBLESTONE.defaultBlockState(), source.getPlayer());
                    }
                    return ComponentApplicationResult.SUCCESS;
                }
                if (state.m_60734_() == Blocks.FIRE && BlockUtils.destroyBlock(source.getCaster(), context.getServerLevel(), target.getBlock(), false, Tiers.STONE)) {
                    BlockUtils.updateBlockState(context.getServerLevel(), target.getBlock());
                    return ComponentApplicationResult.SUCCESS;
                }
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.ICE;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ICE;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        Random rndm = new Random(1234L);
        if (age < 5) {
            for (int i = 0; i < 25; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FROST.get()), caster), impact_position.x + (double) rndm.nextFloat() - 0.5, impact_position.y + (double) rndm.nextFloat() - 0.5, impact_position.z + (double) rndm.nextFloat() - 0.5, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.WATER, Affinity.ICE);
    }
}