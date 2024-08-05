package team.lodestar.lodestone.systems.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import team.lodestar.lodestone.systems.blockentity.LodestoneBlockEntity;

public class LodestoneEntityBlock<T extends LodestoneBlockEntity> extends Block implements EntityBlock {

    protected Supplier<BlockEntityType<T>> blockEntityType = null;

    protected BlockEntityTicker<T> ticker = null;

    public LodestoneEntityBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public LodestoneEntityBlock<T> setBlockEntity(Supplier<BlockEntityType<T>> type) {
        this.blockEntityType = type;
        this.ticker = (l, p, s, t) -> t.tick();
        return this;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.hasTileEntity(state) ? ((BlockEntityType) this.blockEntityType.get()).create(pos, state) : null;
    }

    public boolean hasTileEntity(BlockState state) {
        return this.blockEntityType != null;
    }

    @Override
    public <Y extends BlockEntity> BlockEntityTicker<Y> getTicker(Level level, BlockState state, BlockEntityType<Y> type) {
        return this.ticker;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        if (this.hasTileEntity(pState) && pLevel.getBlockEntity(pPos) instanceof LodestoneBlockEntity simpleBlockEntity) {
            simpleBlockEntity.onPlace(pPlacer, pStack);
        }
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        if (this.hasTileEntity(state) && world.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
            ItemStack stack = simpleBlockEntity.onClone(state, target, world, pos, player);
            if (!stack.isEmpty()) {
                return stack;
            }
        }
        return super.getCloneItemStack(state, target, world, pos, player);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        this.onBlockBroken(state, level, pos, player);
        super.playerWillDestroy(level, pos, state, player);
    }

    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        this.onBlockBroken(state, level, pos, null);
        super.onBlockExploded(state, level, pos, explosion);
    }

    public void onBlockBroken(BlockState state, BlockGetter level, BlockPos pos, @Nullable Player player) {
        if (this.hasTileEntity(state) && level.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity) {
            simpleBlockEntity.onBreak(player);
        }
    }

    @Override
    public void entityInside(BlockState pState, Level pLevel, BlockPos pPos, Entity pEntity) {
        if (this.hasTileEntity(pState) && pLevel.getBlockEntity(pPos) instanceof LodestoneBlockEntity simpleBlockEntity) {
            simpleBlockEntity.onEntityInside(pState, pLevel, pPos, pEntity);
        }
        super.m_7892_(pState, pLevel, pPos, pEntity);
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (this.hasTileEntity(pState) && pLevel.getBlockEntity(pPos) instanceof LodestoneBlockEntity simpleBlockEntity) {
            simpleBlockEntity.onNeighborUpdate(pState, pPos, pFromPos);
        }
        super.m_6861_(pState, pLevel, pPos, pBlock, pFromPos, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        return this.hasTileEntity(state) && level.getBlockEntity(pos) instanceof LodestoneBlockEntity simpleBlockEntity ? simpleBlockEntity.onUse(player, hand) : super.m_6227_(state, level, pos, player, hand, ray);
    }
}