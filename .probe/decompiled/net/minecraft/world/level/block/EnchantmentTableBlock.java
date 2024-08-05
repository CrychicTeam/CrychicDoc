package net.minecraft.world.level.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.EnchantmentTableBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EnchantmentTableBlock extends BaseEntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0);

    public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-2, 0, -2, 2, 1, 2).filter(p_207914_ -> Math.abs(p_207914_.m_123341_()) == 2 || Math.abs(p_207914_.m_123343_()) == 2).map(BlockPos::m_7949_).toList();

    protected EnchantmentTableBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    public static boolean isValidBookShelf(Level level0, BlockPos blockPos1, BlockPos blockPos2) {
        return level0.getBlockState(blockPos1.offset(blockPos2)).m_204336_(BlockTags.ENCHANTMENT_POWER_PROVIDER) && level0.getBlockState(blockPos1.offset(blockPos2.m_123341_() / 2, blockPos2.m_123342_(), blockPos2.m_123343_() / 2)).m_204336_(BlockTags.ENCHANTMENT_POWER_TRANSMITTER);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        super.m_214162_(blockState0, level1, blockPos2, randomSource3);
        for (BlockPos $$4 : BOOKSHELF_OFFSETS) {
            if (randomSource3.nextInt(16) == 0 && isValidBookShelf(level1, blockPos2, $$4)) {
                level1.addParticle(ParticleTypes.ENCHANT, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 2.0, (double) blockPos2.m_123343_() + 0.5, (double) ((float) $$4.m_123341_() + randomSource3.nextFloat()) - 0.5, (double) ((float) $$4.m_123342_() - randomSource3.nextFloat() - 1.0F), (double) ((float) $$4.m_123343_() + randomSource3.nextFloat()) - 0.5);
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new EnchantmentTableBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return level0.isClientSide ? m_152132_(blockEntityTypeT2, BlockEntityType.ENCHANTING_TABLE, EnchantmentTableBlockEntity::m_155503_) : null;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            player3.openMenu(blockState0.m_60750_(level1, blockPos2));
            return InteractionResult.CONSUME;
        }
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        BlockEntity $$3 = level1.getBlockEntity(blockPos2);
        if ($$3 instanceof EnchantmentTableBlockEntity) {
            Component $$4 = ((Nameable) $$3).getDisplayName();
            return new SimpleMenuProvider((p_207906_, p_207907_, p_207908_) -> new EnchantmentMenu(p_207906_, p_207907_, ContainerLevelAccess.create(level1, blockPos2)), $$4);
        } else {
            return null;
        }
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, LivingEntity livingEntity3, ItemStack itemStack4) {
        if (itemStack4.hasCustomHoverName()) {
            BlockEntity $$5 = level0.getBlockEntity(blockPos1);
            if ($$5 instanceof EnchantmentTableBlockEntity) {
                ((EnchantmentTableBlockEntity) $$5).setCustomName(itemStack4.getHoverName());
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}