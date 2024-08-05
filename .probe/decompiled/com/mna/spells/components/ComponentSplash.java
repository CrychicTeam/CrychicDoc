package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.effects.EffectInit;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ComponentSplash extends SpellEffect {

    public ComponentSplash(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 20.0F, 5.0F, 60.0F, 5.0F, 5.0F), new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 5.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isLivingEntity()) {
            target.getLivingEntity().addEffect(new MobEffectInstance(EffectInit.SOAKED.get(), (int) modificationData.getValue(Attribute.DURATION) * 20, 0, false, true));
        } else if (target.isBlock()) {
            if (context.countAffectedBlocks(this) > 0) {
                return ComponentApplicationResult.FAIL;
            }
            BlockPos block = target.getBlock();
            BlockState targetState = context.getServerLevel().m_8055_(block);
            if (targetState.m_60734_() == Blocks.CAULDRON) {
                context.getLevel().setBlock(block, (BlockState) Blocks.WATER_CAULDRON.defaultBlockState().m_61124_(LayeredCauldronBlock.LEVEL, 3), 3);
                context.addAffectedBlock(this, block);
                return ComponentApplicationResult.SUCCESS;
            }
            if (targetState.m_60734_() == Blocks.WATER_CAULDRON) {
                context.getLevel().setBlock(block, (BlockState) targetState.m_61124_(LayeredCauldronBlock.LEVEL, 3), 3);
                context.addAffectedBlock(this, block);
                return ComponentApplicationResult.SUCCESS;
            }
            if (modificationData.getValue(Attribute.PRECISION) == 1.0F) {
                return ComponentApplicationResult.FAIL;
            }
            if (context.getServerLevel().m_6042_().ultraWarm()) {
                if (context.countAffectedBlocks(this) == 0) {
                    context.addAffectedBlock(this, block);
                    context.getServerLevel().m_5594_(null, block, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (context.getServerLevel().m_213780_().nextFloat() - context.getServerLevel().m_213780_().nextFloat()) * 0.8F);
                }
                return ComponentApplicationResult.FAIL;
            }
            context.addAffectedBlock(this, block);
            if (!this.setBlockWaterlogged(context.getServerLevel(), block, targetState)) {
                block = block.offset(target.getBlockFace(this).getNormal());
                if (context.getServerLevel().m_46859_(block)) {
                    context.addAffectedBlock(this, block);
                    context.getServerLevel().m_7731_(block, Blocks.WATER.defaultBlockState(), 3);
                    if (!GeneralConfigValues.SplashCreatesSources) {
                        DelayedEventQueue.pushEvent(context.getLevel(), new TimedDelayedEvent<>("water_fix", 20, block, (s, i) -> context.getServerLevel().m_7731_(i, (BlockState) Blocks.WATER.defaultBlockState().m_61124_(LiquidBlock.LEVEL, 15), 3)));
                    }
                    context.getServerLevel().m_186460_(block, Blocks.WATER, 3);
                } else if (!this.setBlockWaterlogged(context.getServerLevel(), block, targetState)) {
                    return ComponentApplicationResult.FAIL;
                }
            }
        }
        return ComponentApplicationResult.SUCCESS;
    }

    private boolean setBlockWaterlogged(Level world, BlockPos pos, BlockState state) {
        if (state.m_61138_(BlockStateProperties.WATERLOGGED) && !(Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, true), 3);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WATER;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }
}