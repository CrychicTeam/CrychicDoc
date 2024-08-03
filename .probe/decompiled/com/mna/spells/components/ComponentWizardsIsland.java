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
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedEvent;
import com.mna.entities.utility.RisingBlock;
import com.mna.tools.BlockUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentWizardsIsland extends SpellEffect {

    public ComponentWizardsIsland(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 3.0F, 1.0F, 5.0F, 1.0F, 1.0F), new AttributeValuePair(Attribute.SPEED, 1.0F, 1.0F, 20.0F, 1.0F, 1.0F));
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isBlock()) {
            BlockPos pos = target.getBlock();
            return this.raiseBlock(0, pos, source, target, modificationData, context);
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    private ComponentApplicationResult raiseBlock(int attempt, BlockPos pos, SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!context.getServerLevel().m_46859_(pos) && context.getServerLevel().m_8055_(pos).m_60819_().isEmpty()) {
            if (!context.getServerLevel().m_46859_(pos.above()) && context.getServerLevel().m_6425_(pos.above()).isEmpty()) {
                if (attempt < 2) {
                    DelayedEventQueue.pushEvent(context.getLevel(), new TimedDelayedEvent<>("Delayed Earthsky", 2, attempt++, (s, i) -> this.raiseBlock(i, pos, source, target, modificationData, context)));
                }
                return ComponentApplicationResult.DELAYED;
            }
            BlockState state = context.getServerLevel().m_8055_(pos);
            if (!(state.m_60734_() instanceof EntityBlock) && state.m_60734_() != Blocks.BEDROCK) {
                Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
                if (BlockUtils.canDestroyBlock(player, context.getServerLevel(), target.getBlock(), Tiers.IRON)) {
                    RisingBlock fbe = new RisingBlock(context.getServerLevel(), (double) ((float) pos.m_123341_() + 0.5F), (double) pos.m_123342_(), (double) ((float) pos.m_123343_() + 0.5F), state);
                    fbe.setStartPos(pos);
                    fbe.setMaxAge((int) (modificationData.getValue(Attribute.DURATION) * 20.0F));
                    fbe.setSpeed(modificationData.getValue(Attribute.SPEED) / 20.0F);
                    context.getServerLevel().addFreshEntity(fbe);
                }
            }
        }
        return ComponentApplicationResult.SUCCESS;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return true;
    }

    @Override
    public boolean targetsEntities() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.WATER, Affinity.EARTH, Affinity.WIND, Affinity.ICE);
    }
}