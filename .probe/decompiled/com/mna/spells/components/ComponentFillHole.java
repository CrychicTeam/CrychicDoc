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
import com.mna.entities.utility.FillHole;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ComponentFillHole extends SpellEffect {

    public ComponentFillHole(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 3.0F, 1.0F, 7.0F, 1.0F, 1.0F), new AttributeValuePair(Attribute.HEIGHT, 3.0F, 1.0F, 7.0F, 1.0F, 1.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 50;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster() && target.isBlock()) {
            BlockPos targetPos = target.getBlock().relative(target.getBlockFace(this));
            Player caster = source.getPlayer();
            Block toPlace = null;
            ItemStack offhandItem = caster.m_21120_(source.getHand() == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
            if (!offhandItem.isEmpty() && offhandItem.getItem() instanceof BlockItem) {
                toPlace = ((BlockItem) offhandItem.getItem()).getBlock();
            } else {
                BlockState impactState = context.getServerLevel().m_8055_(target.getBlock());
                if (impactState != null) {
                    toPlace = impactState.m_60734_();
                    if (toPlace instanceof EntityBlock) {
                        return ComponentApplicationResult.FAIL;
                    }
                }
            }
            if (toPlace == null) {
                return ComponentApplicationResult.FAIL;
            } else {
                byte height = (byte) ((int) modificationData.getValue(Attribute.HEIGHT));
                byte radius = (byte) ((int) modificationData.getValue(Attribute.RADIUS));
                FillHole efh = new FillHole(context.getServerLevel(), toPlace, targetPos, caster, height, radius);
                context.getServerLevel().addFreshEntity(efh);
                return ComponentApplicationResult.TARGET_ENTITY_SPAWNED;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
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