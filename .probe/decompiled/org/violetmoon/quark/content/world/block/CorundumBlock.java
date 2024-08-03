package org.violetmoon.quark.content.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.world.module.CorundumModule;
import org.violetmoon.quark.content.world.module.SpiralSpiresModule;
import org.violetmoon.zeta.block.ZetaGlassBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class CorundumBlock extends ZetaGlassBlock {

    public final float[] colorComponents;

    public final boolean waxed;

    public CorundumClusterBlock cluster;

    public CorundumBlock(String regname, int color, @Nullable ZetaModule module, MapColor mapColor, boolean waxed) {
        super(regname, module, true, BlockBehaviour.Properties.of().mapColor(mapColor).strength(0.3F, 0.0F).sound(SoundType.AMETHYST).lightLevel(bx -> 11).requiresCorrectToolForDrops().randomTicks().noOcclusion());
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        this.colorComponents = new float[] { r, g, b };
        this.waxed = waxed;
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.COLORED_BLOCKS);
        }
    }

    private boolean canGrow(Level world, BlockPos pos) {
        if (!this.waxed && CorundumModule.caveCrystalGrowthChance >= 1 && pos.m_123342_() < 24 && world.m_46859_(pos.above())) {
            int i = 1;
            while (world.getBlockState(pos.below(i)).m_60734_() == this) {
                i++;
            }
            return i < 4;
        } else {
            return false;
        }
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (this.canGrow(worldIn, pos) && random.nextInt(CorundumModule.caveCrystalGrowthChance) == 0) {
            BlockState down = worldIn.m_8055_(pos.below());
            BlockPos up = pos.above();
            worldIn.m_46597_(up, state);
            if (down.m_60734_() == SpiralSpiresModule.myalite_crystal && Quark.ZETA.modules.isEnabled(SpiralSpiresModule.class) && SpiralSpiresModule.renewableMyalite) {
                worldIn.m_46597_(pos, SpiralSpiresModule.myalite_crystal.defaultBlockState());
            } else {
                for (Direction d : Direction.values()) {
                    BlockPos offPos = up.relative(d);
                    if (worldIn.m_46859_(offPos) && random.nextInt(3) == 0) {
                        worldIn.m_46597_(offPos, (BlockState) this.cluster.m_49966_().m_61124_(CorundumClusterBlock.FACING, d));
                    }
                }
            }
        }
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        if (this.canGrow(worldIn, pos)) {
            double x = (double) pos.m_123341_() + rand.nextDouble();
            double y = (double) pos.m_123342_() + rand.nextDouble();
            double z = (double) pos.m_123343_() + rand.nextDouble();
            worldIn.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y, z, (double) this.colorComponents[0], (double) this.colorComponents[1], (double) this.colorComponents[2]);
        }
        if (!this.waxed) {
            for (int i = 0; i < 4; i++) {
                double range = 5.0;
                double ox = rand.nextDouble() * range - range / 2.0;
                double oy = rand.nextDouble() * range - range / 2.0;
                double oz = rand.nextDouble() * range - range / 2.0;
                double x = (double) pos.m_123341_() + 0.5 + ox;
                double y = (double) pos.m_123342_() + 0.5 + oy;
                double z = (double) pos.m_123343_() + 0.5 + oz;
                float size = 0.4F + rand.nextFloat() * 0.5F;
                if (rand.nextDouble() < 0.1) {
                    double ol = (ox * ox + oy * oy + oz * oz) * -2.0;
                    if (ol == 0.0) {
                        ol = 1.0E-4;
                    }
                    worldIn.addParticle(ParticleTypes.END_ROD, x, y, z, ox / ol, oy / ol, oz / ol);
                }
                worldIn.addParticle(new DustParticleOptions(new Vector3f(this.colorComponents[0], this.colorComponents[1], this.colorComponents[2]), size), x, y, z, 0.0, 0.0, 0.0);
            }
        }
    }

    @Nullable
    @Override
    public float[] getBeaconColorMultiplierZeta(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
        return this.colorComponents;
    }
}