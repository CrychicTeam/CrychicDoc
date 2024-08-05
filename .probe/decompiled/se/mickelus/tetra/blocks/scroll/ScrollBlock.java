package se.mickelus.tetra.blocks.scroll;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.util.TileEntityOptional;
import se.mickelus.tetra.blocks.ICraftingEffectProviderBlock;
import se.mickelus.tetra.blocks.ISchematicProviderBlock;
import se.mickelus.tetra.blocks.TetraBlock;
import se.mickelus.tetra.blocks.workbench.AbstractWorkbenchBlock;

@ParametersAreNonnullByDefault
public class ScrollBlock extends TetraBlock implements EntityBlock, ISchematicProviderBlock, ICraftingEffectProviderBlock {

    public static final SoundType sound = new SoundType(0.8F, 1.3F, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN, SoundEvents.BOOK_PAGE_TURN);

    private final ScrollBlock.Arrangement arrangement;

    public ScrollBlock(ScrollBlock.Arrangement arrangement) {
        super(BlockBehaviour.Properties.of().sound(sound));
        this.arrangement = arrangement;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST));
    }

    public ScrollBlock.Arrangement getArrangement() {
        return this.arrangement;
    }

    @Override
    public boolean canUnlockSchematics(Level world, BlockPos pos, BlockPos targetPos) {
        boolean isIntricate = (Boolean) TileEntityOptional.from(world, pos, ScrollTile.class).map(ScrollTile::isIntricate).orElse(false);
        return !isIntricate || targetPos.above().equals(pos);
    }

    @Override
    public ResourceLocation[] getSchematics(Level world, BlockPos pos, BlockState blockState) {
        return (ResourceLocation[]) TileEntityOptional.from(world, pos, ScrollTile.class).map(ScrollTile::getSchematics).orElseGet(() -> new ResourceLocation[0]);
    }

    @Override
    public boolean canUnlockCraftingEffects(Level world, BlockPos pos, BlockPos targetPos) {
        boolean isIntricate = (Boolean) TileEntityOptional.from(world, pos, ScrollTile.class).map(ScrollTile::isIntricate).orElse(false);
        return !isIntricate || targetPos.above().equals(pos);
    }

    @Override
    public ResourceLocation[] getCraftingEffects(Level world, BlockPos pos, BlockState blockState) {
        return (ResourceLocation[]) TileEntityOptional.from(world, pos, ScrollTile.class).map(ScrollTile::getCraftingEffects).orElseGet(() -> new ResourceLocation[0]);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (this.arrangement == ScrollBlock.Arrangement.open) {
            BlockState offsetState = world.getBlockState(pos.below());
            if (offsetState.m_60734_() instanceof AbstractWorkbenchBlock) {
                return offsetState.m_60664_(world, player, hand, new BlockHitResult(Vec3.ZERO, Direction.UP, pos.below(), true));
            }
        }
        return super.m_6227_(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction facing = Direction.UP;
        if (this.getArrangement() == ScrollBlock.Arrangement.wall) {
            facing = (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
        }
        BlockPos offsetPos = pos.relative(facing.getOpposite());
        BlockState offsetState = world.m_8055_(offsetPos);
        return this.getArrangement() == ScrollBlock.Arrangement.open ? offsetState.m_60734_() instanceof AbstractWorkbenchBlock : offsetState.m_60783_(world, offsetPos, facing);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (!blockState.m_60710_(world, currentPos)) {
            if (!world.m_5776_() && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && world instanceof Level) {
                this.dropScrolls((Level) world, currentPos);
            }
            return Blocks.AIR.defaultBlockState();
        } else {
            return blockState;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
        super.m_5707_(world, pos, state, player);
        if (!world.isClientSide && !player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            this.dropScrolls(world, pos);
        }
    }

    public void dropScrolls(Level world, BlockPos pos) {
        TileEntityOptional.from(world, pos, ScrollTile.class).ifPresent(tile -> {
            for (CompoundTag nbt : tile.getItemTags()) {
                ItemStack itemStack = new ItemStack(ScrollItem.instance);
                itemStack.addTagElement("BlockEntityTag", nbt);
                ItemEntity entity = new ItemEntity(world, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, itemStack);
                entity.setDefaultPickUpDelay();
                world.m_7967_(entity);
            }
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ScrollTile(blockPos, blockState);
    }

    public static enum Arrangement {

        wall, open, rolled
    }
}