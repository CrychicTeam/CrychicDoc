package net.minecraft.core.dispenser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

public class ShearsDispenseItemBehavior extends OptionalDispenseItemBehavior {

    @Override
    protected ItemStack execute(BlockSource blockSource0, ItemStack itemStack1) {
        ServerLevel $$2 = blockSource0.getLevel();
        if (!$$2.m_5776_()) {
            BlockPos $$3 = blockSource0.getPos().relative((Direction) blockSource0.getBlockState().m_61143_(DispenserBlock.FACING));
            this.m_123573_(tryShearBeehive($$2, $$3) || tryShearLivingEntity($$2, $$3));
            if (this.m_123570_() && itemStack1.hurt(1, $$2.m_213780_(), null)) {
                itemStack1.setCount(0);
            }
        }
        return itemStack1;
    }

    private static boolean tryShearBeehive(ServerLevel serverLevel0, BlockPos blockPos1) {
        BlockState $$2 = serverLevel0.m_8055_(blockPos1);
        if ($$2.m_204338_(BlockTags.BEEHIVES, p_202454_ -> p_202454_.m_61138_(BeehiveBlock.HONEY_LEVEL) && p_202454_.getBlock() instanceof BeehiveBlock)) {
            int $$3 = (Integer) $$2.m_61143_(BeehiveBlock.HONEY_LEVEL);
            if ($$3 >= 5) {
                serverLevel0.m_5594_(null, blockPos1, SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
                BeehiveBlock.dropHoneycomb(serverLevel0, blockPos1);
                ((BeehiveBlock) $$2.m_60734_()).releaseBeesAndResetHoneyLevel(serverLevel0, $$2, blockPos1, null, BeehiveBlockEntity.BeeReleaseStatus.BEE_RELEASED);
                serverLevel0.m_142346_(null, GameEvent.SHEAR, blockPos1);
                return true;
            }
        }
        return false;
    }

    private static boolean tryShearLivingEntity(ServerLevel serverLevel0, BlockPos blockPos1) {
        for (LivingEntity $$3 : serverLevel0.m_6443_(LivingEntity.class, new AABB(blockPos1), EntitySelector.NO_SPECTATORS)) {
            if ($$3 instanceof Shearable $$4 && $$4.readyForShearing()) {
                $$4.shear(SoundSource.BLOCKS);
                serverLevel0.m_142346_(null, GameEvent.SHEAR, blockPos1);
                return true;
            }
        }
        return false;
    }
}