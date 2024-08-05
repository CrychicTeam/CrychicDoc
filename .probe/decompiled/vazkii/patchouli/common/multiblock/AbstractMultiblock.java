package vazkii.patchouli.common.multiblock;

import com.mojang.datafixers.util.Pair;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.IMultiblock;
import vazkii.patchouli.api.TriPredicate;
import vazkii.patchouli.common.util.RotationUtil;

public abstract class AbstractMultiblock implements IMultiblock, BlockAndTintGetter {

    public ResourceLocation id;

    protected int offX;

    protected int offY;

    protected int offZ;

    protected int viewOffX;

    protected int viewOffY;

    protected int viewOffZ;

    private boolean symmetrical;

    Level world;

    private final transient Map<BlockPos, BlockEntity> teCache = new HashMap();

    @Override
    public IMultiblock offset(int x, int y, int z) {
        return this.setOffset(this.offX + x, this.offY + y, this.offZ + z);
    }

    public IMultiblock setOffset(int x, int y, int z) {
        this.offX = x;
        this.offY = y;
        this.offZ = z;
        return this.setViewOffset(x, y, z);
    }

    void setViewOffset() {
        this.setViewOffset(this.offX, this.offY, this.offZ);
    }

    @Override
    public IMultiblock offsetView(int x, int y, int z) {
        return this.setViewOffset(this.viewOffX + x, this.viewOffY + y, this.viewOffZ + z);
    }

    public IMultiblock setViewOffset(int x, int y, int z) {
        this.viewOffX = x;
        this.viewOffY = y;
        this.viewOffZ = z;
        return this;
    }

    @Override
    public IMultiblock setSymmetrical(boolean symmetrical) {
        this.symmetrical = symmetrical;
        return this;
    }

    @Override
    public ResourceLocation getID() {
        return this.id;
    }

    @Override
    public IMultiblock setId(ResourceLocation res) {
        this.id = res;
        return this;
    }

    @Override
    public void place(Level world, BlockPos pos, Rotation rotation) {
        this.setWorld(world);
        ((Collection) this.simulate(world, pos, rotation, false).getSecond()).forEach(r -> {
            BlockPos placePos = r.getWorldPosition();
            BlockState targetState = r.getStateMatcher().getDisplayedState(world.getGameTime()).m_60717_(rotation);
            if (!targetState.m_60795_() && targetState.m_60710_(world, placePos) && world.getBlockState(placePos).m_247087_()) {
                world.setBlockAndUpdate(placePos, targetState);
            }
        });
    }

    @Override
    public Rotation validate(Level world, BlockPos pos) {
        if (this.isSymmetrical() && this.validate(world, pos, Rotation.NONE)) {
            return Rotation.NONE;
        } else {
            for (Rotation rot : Rotation.values()) {
                if (this.validate(world, pos, rot)) {
                    return rot;
                }
            }
            return null;
        }
    }

    @Override
    public boolean validate(Level world, BlockPos pos, Rotation rotation) {
        this.setWorld(world);
        Pair<BlockPos, Collection<IMultiblock.SimulateResult>> sim = this.simulate(world, pos, rotation, false);
        return ((Collection) sim.getSecond()).stream().allMatch(r -> {
            BlockPos checkPos = r.getWorldPosition();
            TriPredicate<BlockGetter, BlockPos, BlockState> pred = r.getStateMatcher().getStatePredicate();
            BlockState state = world.getBlockState(checkPos).m_60717_(RotationUtil.fixHorizontal(rotation));
            return pred.test(world, checkPos, state);
        });
    }

    @Override
    public boolean isSymmetrical() {
        return this.symmetrical;
    }

    public void setWorld(Level world) {
        this.world = world;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        BlockState state = this.m_8055_(pos);
        return state.m_60734_() instanceof EntityBlock ? (BlockEntity) this.teCache.computeIfAbsent(pos.immutable(), p -> ((EntityBlock) state.m_60734_()).newBlockEntity(pos, state)) : null;
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public abstract Vec3i getSize();

    @Override
    public float getShade(Direction direction, boolean shaded) {
        return 1.0F;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return null;
    }

    @Override
    public int getBlockTint(BlockPos pos, ColorResolver color) {
        Biome plains = this.world.registryAccess().registryOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS);
        return color.getColor(plains, (double) pos.m_123341_(), (double) pos.m_123343_());
    }

    @Override
    public int getBrightness(LightLayer type, BlockPos pos) {
        return 15;
    }

    @Override
    public int getRawBrightness(BlockPos pos, int ambientDarkening) {
        return 15 - ambientDarkening;
    }

    @Override
    public int getHeight() {
        return 255;
    }

    @Override
    public int getMinBuildHeight() {
        return 0;
    }
}