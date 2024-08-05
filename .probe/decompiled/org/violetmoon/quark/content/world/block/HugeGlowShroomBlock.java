package org.violetmoon.quark.content.world.block;

import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;
import org.violetmoon.zeta.block.IZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.MiscUtil;

public class HugeGlowShroomBlock extends HugeMushroomBlock implements IZetaBlock {

    private final ZetaModule module;

    private final boolean glowing;

    public HugeGlowShroomBlock(String name, ZetaModule module, boolean glowing) {
        super(BlockBehaviour.Properties.copy(Blocks.RED_MUSHROOM_BLOCK).lightLevel(b -> glowing ? 12 : 0).hasPostProcess((a, b, c) -> glowing).emissiveRendering((a, b, c) -> glowing).randomTicks().noOcclusion());
        this.module = module;
        this.glowing = glowing;
        module.zeta.registry.registerBlock(this, name, true);
        CreativeTabManager.addToCreativeTab(CreativeModeTabs.NATURAL_BLOCKS, this);
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return false;
    }

    @Override
    public int getFlammabilityZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 0;
    }

    @Override
    public int getFireSpreadSpeedZeta(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 0;
    }

    @Override
    public void animateTick(@NotNull BlockState stateIn, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        super.m_214162_(stateIn, worldIn, pos, rand);
        BlockState below = worldIn.getBlockState(pos.below());
        if (this.glowing && rand.nextInt(10) == 0 && (below.m_60795_() || below.m_60734_() == GlimmeringWealdModule.glow_shroom_ring)) {
            worldIn.addParticle(ParticleTypes.END_ROD, (double) pos.m_123341_() + rand.nextDouble(), (double) pos.m_123342_(), (double) pos.m_123343_() + rand.nextDouble(), 0.0, -0.05 - Math.random() * 0.05, 0.0);
        }
    }

    public static boolean place(LevelAccessor worldIn, RandomSource rand, BlockPos pos) {
        Block block = worldIn.m_8055_(pos.below()).m_60734_();
        if (block != Blocks.DEEPSLATE) {
            return false;
        } else {
            BlockPos placePos = pos;
            BlockState stem = GlimmeringWealdModule.glow_shroom_stem.defaultBlockState();
            BlockState ring = GlimmeringWealdModule.glow_shroom_ring.defaultBlockState();
            BlockState cap = (BlockState) GlimmeringWealdModule.glow_shroom_block.defaultBlockState().m_61124_(f_54132_, false);
            int stemHeight1 = 2;
            int stemHeight2 = rand.nextInt(4);
            boolean hasBigCap = rand.nextDouble() < 0.6;
            int totalHeight = stemHeight1 + stemHeight2 + (hasBigCap ? 2 : 1);
            int horizCheck = 2;
            for (int i = -horizCheck; i < horizCheck + 1; i++) {
                for (int j = -horizCheck; j < horizCheck + 1; j++) {
                    for (int k = 1; k < totalHeight; k++) {
                        if (!worldIn.m_8055_(placePos.offset(i, k, j)).m_60795_()) {
                            return false;
                        }
                    }
                }
            }
            for (int i = 0; i < stemHeight1; i++) {
                worldIn.m_7731_(placePos, stem, 2);
                placePos = placePos.above();
            }
            if (stemHeight2 > 0) {
                Direction dir = MiscUtil.HORIZONTALS[rand.nextInt(MiscUtil.HORIZONTALS.length)];
                placePos = placePos.relative(dir);
            }
            for (int i = 0; i < stemHeight2; i++) {
                worldIn.m_7731_(placePos, stem, 2);
                placePos = placePos.above();
            }
            int ringHeight = Math.min(2, stemHeight2);
            for (int i = 0; i < ringHeight; i++) {
                for (Direction ringDir : MiscUtil.HORIZONTALS) {
                    worldIn.m_7731_(placePos.relative(ringDir).relative(Direction.DOWN, i + 1), (BlockState) ring.m_61124_(GlowShroomRingBlock.FACING, ringDir), 2);
                }
            }
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    worldIn.m_7731_(placePos.offset(i, 0, j), cap, 2);
                }
            }
            if (hasBigCap) {
                worldIn.m_7731_(placePos.above(), cap, 2);
            }
            return true;
        }
    }

    @Override
    public ZetaModule getModule() {
        return this.module;
    }

    public IZetaBlock setCondition(BooleanSupplier condition) {
        return this;
    }

    @Override
    public boolean doesConditionApply() {
        return true;
    }
}