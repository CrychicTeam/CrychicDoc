package vectorwing.farmersdelight.common.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModBlocks;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;

public class TomatoVineBlock extends CropBlock {

    public static final IntegerProperty VINE_AGE = BlockStateProperties.AGE_3;

    public static final BooleanProperty ROPELOGGED = BooleanProperty.create("ropelogged");

    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public TomatoVineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(this.getAgeProperty(), 0)).m_61124_(ROPELOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        int age = (Integer) state.m_61143_(this.getAgeProperty());
        boolean isMature = age == this.getMaxAge();
        if (!isMature && player.m_21120_(hand).is(Items.BONE_MEAL)) {
            return InteractionResult.PASS;
        } else if (isMature) {
            int quantity = 1 + level.random.nextInt(2);
            m_49840_(level, pos, new ItemStack(ModItems.TOMATO.get(), quantity));
            if ((double) level.random.nextFloat() < 0.05) {
                m_49840_(level, pos, new ItemStack(ModItems.ROTTEN_TOMATO.get()));
            }
            level.playSound(null, pos, ModSounds.ITEM_TOMATO_PICK_FROM_BUSH.get(), SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F);
            level.setBlock(pos, (BlockState) state.m_61124_(this.getAgeProperty(), 0), 2);
            return InteractionResult.SUCCESS;
        } else {
            return super.m_6227_(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            if (level.m_45524_(pos, 0) >= 9) {
                int age = this.m_52305_(state);
                if (age < this.getMaxAge()) {
                    float speed = m_52272_(this, level, pos);
                    if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / speed) + 1) == 0)) {
                        level.m_7731_(pos, (BlockState) state.m_61124_(this.getAgeProperty(), age + 1), 2);
                        ForgeHooks.onCropsGrowPost(level, pos, state);
                    }
                }
                this.attemptRopeClimb(level, pos, random);
            }
        }
    }

    public void attemptRopeClimb(ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextFloat() < 0.3F) {
            BlockPos posAbove = pos.above();
            BlockState stateAbove = level.m_8055_(posAbove);
            boolean canClimb = Configuration.ENABLE_TOMATO_VINE_CLIMBING_TAGGED_ROPES.get() ? stateAbove.m_204336_(ModTags.ROPES) : stateAbove.m_60713_(ModBlocks.ROPE.get());
            if (canClimb) {
                int vineHeight = 1;
                while (level.m_8055_(pos.below(vineHeight)).m_60713_(this)) {
                    vineHeight++;
                }
                if (vineHeight < 3) {
                    level.m_46597_(posAbove, (BlockState) this.m_49966_().m_61124_(ROPELOGGED, true));
                }
            }
        }
    }

    @Override
    public BlockState getStateForAge(int age) {
        return (BlockState) this.m_49966_().m_61124_(this.getAgeProperty(), age);
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return VINE_AGE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public int getMaxAge() {
        return 3;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.TOMATO_SEEDS.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VINE_AGE, ROPELOGGED);
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return super.getBonemealAgeIncrease(level) / 2;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int newAge = this.m_52305_(state) + this.getBonemealAgeIncrease(level);
        int maxAge = this.getMaxAge();
        if (newAge > maxAge) {
            newAge = maxAge;
        }
        level.m_46597_(pos, (BlockState) state.m_61124_(this.getAgeProperty(), newAge));
        this.attemptRopeClimb(level, pos, random);
    }

    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return (Boolean) state.m_61143_(ROPELOGGED) && state.m_204336_(BlockTags.CLIMBABLE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.m_8055_(belowPos);
        return !state.m_61143_(ROPELOGGED) ? super.canSurvive(state, level, pos) : belowState.m_60713_(ModBlocks.TOMATO_CROP.get()) && this.hasGoodCropConditions(level, pos);
    }

    public boolean hasGoodCropConditions(LevelReader level, BlockPos pos) {
        return level.m_45524_(pos, 0) >= 8 || level.m_45527_(pos);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        boolean isRopelogged = (Boolean) state.m_61143_(ROPELOGGED);
        super.m_6240_(level, player, pos, state, blockEntity, stack);
        if (isRopelogged) {
            destroyAndPlaceRope(level, pos);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (!state.m_60710_(level, currentPos)) {
            level.scheduleTick(currentPos, this, 1);
        }
        return state;
    }

    public static void destroyAndPlaceRope(Level level, BlockPos pos) {
        Block configuredRopeBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(Configuration.DEFAULT_TOMATO_VINE_ROPE.get()));
        Block finalRopeBlock = configuredRopeBlock != null ? configuredRopeBlock : ModBlocks.ROPE.get();
        level.setBlockAndUpdate(pos, finalRopeBlock.defaultBlockState());
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.m_60710_(level, pos)) {
            level.m_46961_(pos, true);
            if ((Boolean) state.m_61143_(ROPELOGGED)) {
                destroyAndPlaceRope(level, pos);
            }
        }
    }
}