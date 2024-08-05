package net.mehvahdjukaar.amendments.integration;

import net.mehvahdjukaar.amendments.common.block.CeilingBannerBlock;
import net.mehvahdjukaar.amendments.common.tile.LiquidCauldronBlockTile;
import net.mehvahdjukaar.amendments.events.behaviors.CauldronConversion;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidStack;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidTank;
import net.mehvahdjukaar.supplementaries.client.ModMaterials;
import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.EndermanSkullBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GunpowderBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceLeverBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.SconceWallBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StickBlock;
import net.mehvahdjukaar.supplementaries.common.block.faucet.FaucetTarget;
import net.mehvahdjukaar.supplementaries.common.block.tiles.FaucetBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.BlockUtil;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BannerPatternItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

public class SuppCompat {

    public static void setup() {
        FaucetBlockTile.registerInteraction(new SuppCompat.FaucetCauldronConversion());
    }

    public static boolean canBannerAttachToRope(BlockState state, BlockState above) {
        if (above.m_60734_() instanceof RopeBlock && !(Boolean) above.m_61143_(RopeBlock.DOWN)) {
            Direction dir = (Direction) state.m_61143_(CeilingBannerBlock.FACING);
            return (Boolean) above.m_61143_((Property) RopeBlock.FACING_TO_PROPERTY_MAP.get(dir.getClockWise())) && (Boolean) above.m_61143_((Property) RopeBlock.FACING_TO_PROPERTY_MAP.get(dir.getCounterClockWise()));
        } else {
            return false;
        }
    }

    public static boolean isVerticalStick(BlockState state, Direction facing) {
        return state.m_60734_() instanceof StickBlock && (facing.getAxis() == Direction.Axis.X ? !(Boolean) state.m_61143_(StickBlock.AXIS_X) : !(Boolean) state.m_61143_(StickBlock.AXIS_Z));
    }

    public static boolean isRope(Block block) {
        return block instanceof RopeBlock;
    }

    public static void spawnCakeParticles(Level level, BlockPos pos, RandomSource rand) {
        if (MiscUtils.FESTIVITY.isStValentine() && (double) rand.nextFloat() > 0.8) {
            double d0 = (double) pos.m_123341_() + 0.5 + ((double) rand.nextFloat() - 0.5);
            double d1 = (double) pos.m_123342_() + 0.5 + ((double) rand.nextFloat() - 0.5);
            double d2 = (double) pos.m_123343_() + 0.5 + ((double) rand.nextFloat() - 0.5);
            level.addParticle(ParticleTypes.HEART, d0, d1, d2, 0.0, 0.0, 0.0);
        }
    }

    public static void addOptionalOwnership(Level world, BlockPos pos, @Nullable LivingEntity entity) {
        BlockUtil.addOptionalOwnership(entity, world, pos);
    }

    public static float getSignColorMult() {
        return ClientConfigs.getSignColorMult();
    }

    public static boolean isSupportingCeiling(BlockState upState, BlockPos pos, LevelReader world) {
        return IRopeConnection.isSupportingCeiling(upState, pos, world);
    }

    public static void createMiniExplosion(Level level, BlockPos pos, boolean b) {
        GunpowderBlock.createMiniExplosion(level, pos, b);
    }

    public static boolean canConnectDown(BlockState neighborState) {
        return IRopeConnection.canConnectDown(neighborState);
    }

    public static boolean isEndermanHead(SkullBlock skull) {
        return skull.m_48754_() == EndermanSkullBlock.TYPE;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public static Material getFlagMaterial(BannerPatternItem bannerPatternItem) {
        return ModMaterials.getFlagMaterialForPatternItem(bannerPatternItem);
    }

    public static boolean isSconce(Block block) {
        return block instanceof SconceLeverBlock ? true : block instanceof SconceBlock && !(block instanceof SconceWallBlock);
    }

    public static boolean isCandleHolder(Block block) {
        return block instanceof CandleHolderBlock;
    }

    public static class FaucetCauldronConversion implements FaucetTarget.BlState {

        public Integer fill(Level level, BlockPos pos, BlockState target, SoftFluidStack fluid, int minAmount) {
            if (target.m_60713_(Blocks.CAULDRON)) {
                BlockState newState = CauldronConversion.getNewState(pos, level, fluid);
                if (newState != null) {
                    level.setBlockAndUpdate(pos, newState);
                    if (level.getBlockEntity(pos) instanceof LiquidCauldronBlockTile te) {
                        SoftFluidTank tank = te.getSoftFluidTank();
                        tank.setFluid(fluid.copyWithCount(minAmount));
                        return minAmount;
                    }
                }
            }
            return null;
        }
    }
}