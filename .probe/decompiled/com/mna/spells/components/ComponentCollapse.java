package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.tools.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentCollapse extends SpellEffect {

    public ComponentCollapse(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (target.isBlock()) {
            BlockPos pos = target.getBlock();
            if (context.getServerLevel().m_46859_(pos.below())) {
                BlockState state = context.getServerLevel().m_8055_(pos);
                if (!(state.m_60734_() instanceof EntityBlock) && state.m_60734_() != Blocks.BEDROCK) {
                    Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
                    if (BlockUtils.canDestroyBlock(player, context.getServerLevel(), target.getBlock(), Tiers.IRON)) {
                        FallingBlockEntity fbe = FallingBlockEntity.fall(context.getServerLevel(), pos, state);
                        fbe.setStartPos(pos);
                        fbe.dropItem = false;
                        context.getServerLevel().addFreshEntity(fbe);
                        return ComponentApplicationResult.SUCCESS;
                    }
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
        return Affinity.EARTH;
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
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}