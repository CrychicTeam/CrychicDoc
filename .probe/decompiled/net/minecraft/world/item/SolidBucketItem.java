package net.minecraft.world.item;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;

public class SolidBucketItem extends BlockItem implements DispensibleContainerItem {

    private final SoundEvent placeSound;

    public SolidBucketItem(Block block0, SoundEvent soundEvent1, Item.Properties itemProperties2) {
        super(block0, itemProperties2);
        this.placeSound = soundEvent1;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        InteractionResult $$1 = super.useOn(useOnContext0);
        Player $$2 = useOnContext0.getPlayer();
        if ($$1.consumesAction() && $$2 != null && !$$2.isCreative()) {
            InteractionHand $$3 = useOnContext0.getHand();
            $$2.m_21008_($$3, Items.BUCKET.getDefaultInstance());
        }
        return $$1;
    }

    @Override
    public String getDescriptionId() {
        return this.m_41467_();
    }

    @Override
    protected SoundEvent getPlaceSound(BlockState blockState0) {
        return this.placeSound;
    }

    @Override
    public boolean emptyContents(@Nullable Player player0, Level level1, BlockPos blockPos2, @Nullable BlockHitResult blockHitResult3) {
        if (level1.isInWorldBounds(blockPos2) && level1.m_46859_(blockPos2)) {
            if (!level1.isClientSide) {
                level1.setBlock(blockPos2, this.m_40614_().defaultBlockState(), 3);
            }
            level1.m_142346_(player0, GameEvent.FLUID_PLACE, blockPos2);
            level1.playSound(player0, blockPos2, this.placeSound, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }
}