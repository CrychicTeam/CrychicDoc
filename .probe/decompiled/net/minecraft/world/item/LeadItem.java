package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

public class LeadItem extends Item {

    public LeadItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if ($$3.m_204336_(BlockTags.FENCES)) {
            Player $$4 = useOnContext0.getPlayer();
            if (!$$1.isClientSide && $$4 != null) {
                bindPlayerMobs($$4, $$1, $$2);
            }
            return InteractionResult.sidedSuccess($$1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public static InteractionResult bindPlayerMobs(Player player0, Level level1, BlockPos blockPos2) {
        LeashFenceKnotEntity $$3 = null;
        boolean $$4 = false;
        double $$5 = 7.0;
        int $$6 = blockPos2.m_123341_();
        int $$7 = blockPos2.m_123342_();
        int $$8 = blockPos2.m_123343_();
        for (Mob $$10 : level1.m_45976_(Mob.class, new AABB((double) $$6 - 7.0, (double) $$7 - 7.0, (double) $$8 - 7.0, (double) $$6 + 7.0, (double) $$7 + 7.0, (double) $$8 + 7.0))) {
            if ($$10.getLeashHolder() == player0) {
                if ($$3 == null) {
                    $$3 = LeashFenceKnotEntity.getOrCreateKnot(level1, blockPos2);
                    $$3.playPlacementSound();
                }
                $$10.setLeashedTo($$3, true);
                $$4 = true;
            }
        }
        if ($$4) {
            level1.m_220407_(GameEvent.BLOCK_ATTACH, blockPos2, GameEvent.Context.of(player0));
        }
        return $$4 ? InteractionResult.SUCCESS : InteractionResult.PASS;
    }
}