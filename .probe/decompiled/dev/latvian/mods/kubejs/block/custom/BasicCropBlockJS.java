package dev.latvian.mods.kubejs.block.custom;

import dev.latvian.mods.kubejs.block.RandomTickCallbackJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BasicCropBlockJS extends CropBlock {

    private final int age;

    private final ItemBuilder seedItem;

    private final List<VoxelShape> shapeByAge;

    private final boolean dropSeed;

    private final ToDoubleFunction<RandomTickCallbackJS> growSpeedCallback;

    private final ToIntFunction<RandomTickCallbackJS> fertilizerCallback;

    private final CropBlockBuilder.SurviveCallback surviveCallback;

    public BasicCropBlockJS(CropBlockBuilder builder) {
        super(builder.createProperties().sound(SoundType.CROP).randomTicks());
        this.age = builder.age;
        this.seedItem = builder.itemBuilder;
        this.shapeByAge = builder.shapeByAge;
        this.dropSeed = builder.dropSeed;
        this.growSpeedCallback = builder.growSpeedCallback;
        this.fertilizerCallback = builder.fertilizerCallback;
        this.surviveCallback = builder.surviveCallback;
    }

    @Override
    public int getMaxAge() {
        return this.age;
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return (ItemLike) (this.dropSeed ? this.seedItem.get() : Items.AIR);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(this.m_7959_());
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return (VoxelShape) this.shapeByAge.get((Integer) blockState.m_61143_(this.m_7959_()));
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        double f = this.growSpeedCallback == null ? -1.0 : this.growSpeedCallback.applyAsDouble(new RandomTickCallbackJS(new BlockContainerJS(serverLevel, blockPos), random));
        int age = this.m_52305_(blockState);
        if (age < this.getMaxAge()) {
            if (f < 0.0) {
                f = (double) m_52272_(this, serverLevel, blockPos);
            }
            if (f > 0.0 && random.nextInt((int) (25.0 / f) + 1) == 0) {
                serverLevel.m_7731_(blockPos, this.m_52289_(age + 1), 2);
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos blockPos, BlockState blockState) {
        if (this.fertilizerCallback == null) {
            super.growCrops(level, blockPos, blockState);
        } else {
            int effect = this.fertilizerCallback.applyAsInt(new RandomTickCallbackJS(new BlockContainerJS(level, blockPos), level.random));
            if (effect > 0) {
                level.setBlock(blockPos, this.m_52289_(Integer.min(this.m_52305_(blockState) + effect, this.getMaxAge())), 2);
            }
        }
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return this.surviveCallback != null ? this.surviveCallback.survive(blockState, levelReader, blockPos) : super.canSurvive(blockState, levelReader, blockPos);
    }
}