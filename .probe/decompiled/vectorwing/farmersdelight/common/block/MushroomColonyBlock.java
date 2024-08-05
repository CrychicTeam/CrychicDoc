package vectorwing.farmersdelight.common.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;
import vectorwing.farmersdelight.common.tag.ModTags;

public class MushroomColonyBlock extends BushBlock implements BonemealableBlock {

    public static final int PLACING_LIGHT_LEVEL = 13;

    public final Supplier<Item> mushroomType;

    public static final IntegerProperty COLONY_AGE = BlockStateProperties.AGE_3;

    protected static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0), Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0), Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0), Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0) };

    public MushroomColonyBlock(BlockBehaviour.Properties properties, Supplier<Item> mushroomType) {
        super(properties);
        this.mushroomType = mushroomType;
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(COLONY_AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.m_61143_(this.getAgeProperty())];
    }

    public IntegerProperty getAgeProperty() {
        return COLONY_AGE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_60804_(level, pos);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos floorPos = pos.below();
        BlockState floorState = level.m_8055_(floorPos);
        return floorState.m_204336_(BlockTags.MUSHROOM_GROW_BLOCK) ? true : level.m_45524_(pos, 0) < 13 && floorState.canSustainPlant(level, floorPos, Direction.UP, this);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = (Integer) state.m_61143_(COLONY_AGE);
        ItemStack heldStack = player.m_21120_(hand);
        if (age > 0 && heldStack.is(Tags.Items.SHEARS)) {
            m_49840_(level, pos, this.getCloneItemStack(level, pos, state));
            level.playSound(null, pos, SoundEvents.MOOSHROOM_SHEAR, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, (BlockState) state.m_61124_(COLONY_AGE, age - 1), 2);
            if (!level.isClientSide) {
                heldStack.hurtAndBreak(1, player, playerIn -> playerIn.m_21190_(hand));
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    public int getMaxAge() {
        return 3;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int age = (Integer) state.m_61143_(COLONY_AGE);
        BlockState groundState = level.m_8055_(pos.below());
        if (age < this.getMaxAge() && groundState.m_204336_(ModTags.MUSHROOM_COLONY_GROWABLE_ON) && ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt(4) == 0)) {
            level.m_7731_(pos, (BlockState) state.m_61124_(COLONY_AGE, age + 1), 2);
            ForgeHooks.onCropsGrowPost(level, pos, state);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack((ItemLike) this.mushroomType.get());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(COLONY_AGE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return (Integer) state.m_61143_(this.getAgeProperty()) < this.getMaxAge();
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 1, 2);
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int age = Math.min(this.getMaxAge(), (Integer) state.m_61143_(COLONY_AGE) + this.getBonemealAgeIncrease(level));
        level.m_7731_(pos, (BlockState) state.m_61124_(COLONY_AGE, age), 2);
    }
}