package com.simibubi.create.content.contraptions.glue;

import com.simibubi.create.content.contraptions.BlockMovementChecks;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SuperGlueSelectionHelper {

    public static Set<BlockPos> searchGlueGroup(Level level, BlockPos startPos, BlockPos endPos, boolean includeOther) {
        if (endPos != null && startPos != null) {
            AABB bb = SuperGlueEntity.span(startPos, endPos);
            List<BlockPos> frontier = new ArrayList();
            Set<BlockPos> visited = new HashSet();
            Set<BlockPos> attached = new HashSet();
            Set<SuperGlueEntity> cachedOther = new HashSet();
            visited.add(startPos);
            frontier.add(startPos);
            while (!frontier.isEmpty()) {
                BlockPos currentPos = (BlockPos) frontier.remove(0);
                attached.add(currentPos);
                for (Direction d : Iterate.directions) {
                    BlockPos offset = currentPos.relative(d);
                    boolean gluePresent = includeOther && SuperGlueEntity.isGlued(level, currentPos, d, cachedOther);
                    boolean alreadySticky = includeOther && SuperGlueEntity.isSideSticky(level, currentPos, d) || SuperGlueEntity.isSideSticky(level, offset, d.getOpposite());
                    if ((alreadySticky || gluePresent || bb.contains(Vec3.atCenterOf(offset))) && BlockMovementChecks.isMovementNecessary(level.getBlockState(offset), level, offset) && SuperGlueEntity.isValidFace(level, currentPos, d) && SuperGlueEntity.isValidFace(level, offset, d.getOpposite()) && visited.add(offset)) {
                        frontier.add(offset);
                    }
                }
            }
            return attached.size() < 2 && attached.contains(endPos) ? null : attached;
        } else {
            return null;
        }
    }

    public static boolean collectGlueFromInventory(Player player, int requiredAmount, boolean simulate) {
        if (player.getAbilities().instabuild) {
            return true;
        } else if (requiredAmount == 0) {
            return true;
        } else {
            NonNullList<ItemStack> items = player.getInventory().items;
            for (int i = -1; i < items.size(); i++) {
                int slot = i == -1 ? player.getInventory().selected : i;
                ItemStack stack = items.get(slot);
                if (!stack.isEmpty() && stack.isDamageableItem() && stack.getItem() instanceof SuperGlueItem) {
                    int charges = Math.min(requiredAmount, stack.getMaxDamage() - stack.getDamageValue());
                    if (!simulate) {
                        stack.hurtAndBreak(charges, player, i == -1 ? SuperGlueItem::onBroken : $ -> {
                        });
                    }
                    requiredAmount -= charges;
                    if (requiredAmount <= 0) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}