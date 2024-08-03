package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.blocks.tileentities.TransitoryTunnelTile;
import com.mna.tools.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ComponentTransitoryTunnel extends SpellEffect {

    public ComponentTransitoryTunnel(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 5.0F, 1.0F, 15.0F, 1.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 2000;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isBlock()) {
            int duration = (int) MathUtils.clamp(modificationData.getValue(Attribute.DURATION), 1.0F, 15.0F);
            BlockPos pos = target.getBlock();
            if (!context.getServerLevel().m_46859_(pos) && context.getServerLevel().m_8055_(pos).m_60819_().isEmpty()) {
                BlockState state = context.getServerLevel().m_8055_(pos);
                if (!(state.m_60734_() instanceof EntityBlock) && state.m_60734_() != Blocks.BEDROCK) {
                    context.getServerLevel().m_7731_(pos, BlockInit.TRANSITORY_TUNNEL.get().m_49966_(), 1);
                    BlockEntity te = context.getServerLevel().m_7702_(pos);
                    if (te != null && te instanceof TransitoryTunnelTile) {
                        ((TransitoryTunnelTile) te).setDurationAndPreviousState(duration * 20, state);
                    }
                    return ComponentApplicationResult.SUCCESS;
                }
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}