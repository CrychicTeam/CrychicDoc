package net.minecraft.world.item.context;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class BlockPlaceContext extends UseOnContext {

    private final BlockPos relativePos;

    protected boolean replaceClicked = true;

    public BlockPlaceContext(Player player0, InteractionHand interactionHand1, ItemStack itemStack2, BlockHitResult blockHitResult3) {
        this(player0.m_9236_(), player0, interactionHand1, itemStack2, blockHitResult3);
    }

    public BlockPlaceContext(UseOnContext useOnContext0) {
        this(useOnContext0.getLevel(), useOnContext0.getPlayer(), useOnContext0.getHand(), useOnContext0.getItemInHand(), useOnContext0.getHitResult());
    }

    protected BlockPlaceContext(Level level0, @Nullable Player player1, InteractionHand interactionHand2, ItemStack itemStack3, BlockHitResult blockHitResult4) {
        super(level0, player1, interactionHand2, itemStack3, blockHitResult4);
        this.relativePos = blockHitResult4.getBlockPos().relative(blockHitResult4.getDirection());
        this.replaceClicked = level0.getBlockState(blockHitResult4.getBlockPos()).m_60629_(this);
    }

    public static BlockPlaceContext at(BlockPlaceContext blockPlaceContext0, BlockPos blockPos1, Direction direction2) {
        return new BlockPlaceContext(blockPlaceContext0.m_43725_(), blockPlaceContext0.m_43723_(), blockPlaceContext0.m_43724_(), blockPlaceContext0.m_43722_(), new BlockHitResult(new Vec3((double) blockPos1.m_123341_() + 0.5 + (double) direction2.getStepX() * 0.5, (double) blockPos1.m_123342_() + 0.5 + (double) direction2.getStepY() * 0.5, (double) blockPos1.m_123343_() + 0.5 + (double) direction2.getStepZ() * 0.5), direction2, blockPos1, false));
    }

    @Override
    public BlockPos getClickedPos() {
        return this.replaceClicked ? super.getClickedPos() : this.relativePos;
    }

    public boolean canPlace() {
        return this.replaceClicked || this.m_43725_().getBlockState(this.getClickedPos()).m_60629_(this);
    }

    public boolean replacingClickedOnBlock() {
        return this.replaceClicked;
    }

    public Direction getNearestLookingDirection() {
        return Direction.orderedByNearest(this.m_43723_())[0];
    }

    public Direction getNearestLookingVerticalDirection() {
        return Direction.getFacingAxis(this.m_43723_(), Direction.Axis.Y);
    }

    public Direction[] getNearestLookingDirections() {
        Direction[] $$0 = Direction.orderedByNearest(this.m_43723_());
        if (this.replaceClicked) {
            return $$0;
        } else {
            Direction $$1 = this.m_43719_();
            int $$2 = 0;
            while ($$2 < $$0.length && $$0[$$2] != $$1.getOpposite()) {
                $$2++;
            }
            if ($$2 > 0) {
                System.arraycopy($$0, 0, $$0, 1, $$2);
                $$0[0] = $$1.getOpposite();
            }
            return $$0;
        }
    }
}