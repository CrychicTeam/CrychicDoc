package org.violetmoon.quark.mixin.mixins.self;

import java.util.Locale;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.extensions.IForgeBlock;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.violetmoon.quark.addons.oddities.block.MatrixEnchantingTableBlock;
import org.violetmoon.quark.content.automation.block.IronRodBlock;
import org.violetmoon.quark.content.building.block.HedgeBlock;
import org.violetmoon.quark.content.building.block.QuarkVerticalSlabBlock;
import org.violetmoon.quark.content.building.block.VariantChestBlock;
import org.violetmoon.quark.content.building.block.VariantFurnaceBlock;
import org.violetmoon.quark.content.building.block.VariantLadderBlock;
import org.violetmoon.quark.content.building.block.VariantTrappedChestBlock;
import org.violetmoon.quark.content.building.block.VerticalSlabBlock;
import org.violetmoon.quark.content.world.block.HugeGlowShroomBlock;
import org.violetmoon.zeta.block.ext.IZetaBlockExtensions;

@Mixin({ HedgeBlock.class, HugeGlowShroomBlock.class, IronRodBlock.class, MatrixEnchantingTableBlock.class, QuarkVerticalSlabBlock.class, VariantChestBlock.class, VariantFurnaceBlock.class, VariantLadderBlock.class, VariantTrappedChestBlock.class, VerticalSlabBlock.class })
public class IZetaBlockMixin_FAKE implements IZetaBlockExtensions, IForgeBlock {

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return this.getLightEmissionZeta(state, level, pos);
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return this.isLadderZeta(state, level, pos, entity);
    }

    @Override
    public boolean makesOpenTrapdoorAboveClimbable(BlockState state, LevelReader level, BlockPos pos, BlockState trapdoorState) {
        return this.makesOpenTrapdoorAboveClimbableZeta(state, level, pos, trapdoorState);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter level, BlockPos pos, Direction facing, IPlantable plantable) {
        return this.canSustainPlantZeta(state, level, pos, facing, plantable.getPlantType(level, pos).getName().toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean isConduitFrame(BlockState state, LevelReader level, BlockPos pos, BlockPos conduit) {
        return this.isConduitFrameZeta(state, level, pos, conduit);
    }

    @Override
    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return this.getEnchantPowerBonusZeta(state, level, pos);
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
        return this.getSoundTypeZeta(state, level, pos, entity);
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplier(BlockState state, LevelReader level, BlockPos pos, BlockPos beaconPos) {
        return this.getBeaconColorMultiplierZeta(state, level, pos, beaconPos);
    }

    @Override
    public boolean isStickyBlock(BlockState state) {
        return this.isStickyBlockZeta(state);
    }

    @Override
    public boolean canStickTo(BlockState state, BlockState other) {
        return this.canStickToZeta(state, other);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.getFlammabilityZeta(state, level, pos, direction);
    }

    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.isFlammableZeta(state, level, pos, direction);
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return this.getFireSpreadSpeedZeta(state, level, pos, direction);
    }

    @Override
    public boolean collisionExtendsVertically(BlockState state, BlockGetter level, BlockPos pos, Entity collidingEntity) {
        return this.collisionExtendsVerticallyZeta(state, level, pos, collidingEntity);
    }

    @Override
    public boolean shouldDisplayFluidOverlay(BlockState state, BlockAndTintGetter level, BlockPos pos, FluidState fluidState) {
        return this.shouldDisplayFluidOverlayZeta(state, level, pos, fluidState);
    }

    @Nullable
    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context, ToolAction toolAction, boolean simulate) {
        String toolActionName = toolAction.name();
        return this.getToolModifiedStateZeta(state, context, toolActionName, simulate);
    }

    @Override
    public boolean isScaffolding(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return this.isScaffoldingZeta(state, level, pos, entity);
    }
}