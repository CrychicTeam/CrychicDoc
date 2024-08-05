package vectorwing.farmersdelight.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.ForgeEventFactory;
import vectorwing.farmersdelight.common.registry.ModItems;

public class BuddingBushBlock extends BushBlock {

    public static final int MAX_AGE = 3;

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 4);

    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] { Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 10.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0) };

    public BuddingBushBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.m_61143_(this.getAgeProperty())];
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.m_60713_(Blocks.FARMLAND);
    }

    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.CROP;
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 3;
    }

    protected int getAge(BlockState state) {
        return (Integer) state.m_61143_(this.getAgeProperty());
    }

    public BlockState getStateForAge(int age) {
        return (BlockState) this.m_49966_().m_61124_(this.getAgeProperty(), age);
    }

    public boolean isMaxAge(BlockState state) {
        return (Integer) state.m_61143_(this.getAgeProperty()) >= this.getMaxAge();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return this.canGrowPastMaxAge() || !this.isMaxAge(state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            if (level.m_45524_(pos, 0) >= 9) {
                int age = this.getAge(state);
                if (age <= this.getMaxAge()) {
                    float growthSpeed = getGrowthSpeed(this, level, pos);
                    if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthSpeed) + 1) == 0)) {
                        if (this.isMaxAge(state)) {
                            this.growPastMaxAge(state, level, pos, random);
                        } else {
                            level.m_46597_(pos, this.getStateForAge(age + 1));
                        }
                        ForgeHooks.onCropsGrowPost(level, pos, state);
                    }
                }
            }
        }
    }

    public boolean canGrowPastMaxAge() {
        return false;
    }

    public void growPastMaxAge(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
    }

    protected static float getGrowthSpeed(Block block, BlockGetter level, BlockPos pos) {
        float speed = 1.0F;
        BlockPos posBelow = pos.below();
        for (int posX = -1; posX <= 1; posX++) {
            for (int posZ = -1; posZ <= 1; posZ++) {
                float speedBonus = 0.0F;
                BlockState stateBelow = level.getBlockState(posBelow.offset(posX, 0, posZ));
                if (stateBelow.canSustainPlant(level, posBelow.offset(posX, 0, posZ), Direction.UP, (IPlantable) block)) {
                    speedBonus = 1.0F;
                    if (stateBelow.isFertile(level, pos.offset(posX, 0, posZ))) {
                        speedBonus = 3.0F;
                    }
                }
                if (posX != 0 || posZ != 0) {
                    speedBonus /= 4.0F;
                }
                speed += speedBonus;
            }
        }
        BlockPos posNorth = pos.north();
        BlockPos posSouth = pos.south();
        BlockPos posWest = pos.west();
        BlockPos posEast = pos.east();
        boolean matchesEastWestRow = level.getBlockState(posWest).m_60713_(block) || level.getBlockState(posEast).m_60713_(block);
        boolean matchesNorthSouthRow = level.getBlockState(posNorth).m_60713_(block) || level.getBlockState(posSouth).m_60713_(block);
        if (matchesEastWestRow && matchesNorthSouthRow) {
            speed /= 2.0F;
        } else {
            boolean matchesDiagonalRows = level.getBlockState(posWest.north()).m_60713_(block) || level.getBlockState(posEast.north()).m_60713_(block) || level.getBlockState(posEast.south()).m_60713_(block) || level.getBlockState(posWest.south()).m_60713_(block);
            if (matchesDiagonalRows) {
                speed /= 2.0F;
            }
        }
        return speed;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return (level.m_45524_(pos, 0) >= 8 || level.m_45527_(pos)) && super.canSurvive(state, level, pos);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Ravager && ForgeEventFactory.getMobGriefingEvent(level, entity)) {
            level.m_46953_(pos, true, entity);
        }
        super.m_7892_(state, level, pos, entity);
    }

    protected ItemLike getBaseSeedId() {
        return ModItems.TOMATO_SEEDS.get();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getBaseSeedId());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }
}