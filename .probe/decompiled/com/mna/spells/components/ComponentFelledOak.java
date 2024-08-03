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
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.FakePlayerFactory;

public class ComponentFelledOak extends SpellEffect {

    public ComponentFelledOak(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        ComponentApplicationResult result = ComponentApplicationResult.FAIL;
        if (!target.isBlock()) {
            return result;
        } else {
            Player player = (Player) (source.isPlayerCaster() ? source.getPlayer() : FakePlayerFactory.getMinecraft(context.getServerLevel()));
            BlockPos sourceBlock = target.getBlock();
            Pair<Boolean, Boolean> captureRedirect = InventoryUtilities.getCaptureAndRedirect(player);
            if (BlockUtils.isLogOrLeaf(context.getServerLevel().m_8055_(sourceBlock)) == BlockUtils.TreeBlockTypes.LOG) {
                while (BlockUtils.isLogOrLeaf(context.getServerLevel().m_8055_(sourceBlock)) == BlockUtils.TreeBlockTypes.LOG && sourceBlock.m_123342_() > 0) {
                    sourceBlock = sourceBlock.below();
                }
                sourceBlock = sourceBlock.above();
                Pair<Boolean, List<ItemStack>> treeResult = BlockUtils.breakTreeRecursive(player, context.getServerLevel(), sourceBlock, (Boolean) captureRedirect.getFirst());
                if ((Boolean) treeResult.getFirst()) {
                    if (source.isPlayerCaster()) {
                        InventoryUtilities.redirectCaptureOrDrop(player, context.getServerLevel(), (List<ItemStack>) treeResult.getSecond(), (Boolean) captureRedirect.getSecond());
                    }
                    result = ComponentApplicationResult.SUCCESS;
                }
            }
            return result;
        }
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
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public float initialComplexity() {
        return 15.0F;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}