package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EnderChestBlockEntity extends BlockEntity implements LidBlockEntity {

    private final ChestLidController chestLidController = new ChestLidController();

    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(Level p_155531_, BlockPos p_155532_, BlockState p_155533_) {
            p_155531_.playSound(null, (double) p_155532_.m_123341_() + 0.5, (double) p_155532_.m_123342_() + 0.5, (double) p_155532_.m_123343_() + 0.5, SoundEvents.ENDER_CHEST_OPEN, SoundSource.BLOCKS, 0.5F, p_155531_.random.nextFloat() * 0.1F + 0.9F);
        }

        @Override
        protected void onClose(Level p_155541_, BlockPos p_155542_, BlockState p_155543_) {
            p_155541_.playSound(null, (double) p_155542_.m_123341_() + 0.5, (double) p_155542_.m_123342_() + 0.5, (double) p_155542_.m_123343_() + 0.5, SoundEvents.ENDER_CHEST_CLOSE, SoundSource.BLOCKS, 0.5F, p_155541_.random.nextFloat() * 0.1F + 0.9F);
        }

        @Override
        protected void openerCountChanged(Level p_155535_, BlockPos p_155536_, BlockState p_155537_, int p_155538_, int p_155539_) {
            p_155535_.blockEvent(EnderChestBlockEntity.this.f_58858_, Blocks.ENDER_CHEST, 1, p_155539_);
        }

        @Override
        protected boolean isOwnContainer(Player p_155529_) {
            return p_155529_.getEnderChestInventory().isActiveChest(EnderChestBlockEntity.this);
        }
    };

    public EnderChestBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.ENDER_CHEST, blockPos0, blockState1);
    }

    public static void lidAnimateTick(Level level0, BlockPos blockPos1, BlockState blockState2, EnderChestBlockEntity enderChestBlockEntity3) {
        enderChestBlockEntity3.chestLidController.tickLid();
    }

    @Override
    public boolean triggerEvent(int int0, int int1) {
        if (int0 == 1) {
            this.chestLidController.shouldBeOpen(int1 > 0);
            return true;
        } else {
            return super.triggerEvent(int0, int1);
        }
    }

    public void startOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openersCounter.incrementOpeners(player0, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    public void stopOpen(Player player0) {
        if (!this.f_58859_ && !player0.isSpectator()) {
            this.openersCounter.decrementOpeners(player0, this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    public boolean stillValid(Player player0) {
        return Container.stillValidBlockEntity(this, player0);
    }

    public void recheckOpen() {
        if (!this.f_58859_) {
            this.openersCounter.recheckOpeners(this.m_58904_(), this.m_58899_(), this.m_58900_());
        }
    }

    @Override
    public float getOpenNess(float float0) {
        return this.chestLidController.getOpenness(float0);
    }
}