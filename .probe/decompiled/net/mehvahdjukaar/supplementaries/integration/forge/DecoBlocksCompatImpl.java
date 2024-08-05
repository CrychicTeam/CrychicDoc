package net.mehvahdjukaar.supplementaries.integration.forge;

import java.util.List;
import java.util.function.Supplier;
import lilypuree.decorative_blocks.blocks.ChandelierBlock;
import lilypuree.decorative_blocks.blocks.PalisadeBlock;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AbstractRopeBlock;
import net.mehvahdjukaar.supplementaries.integration.CompatHandler;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.Lazy;

public class DecoBlocksCompatImpl {

    public static final Supplier<Block> CHANDELIER_ROPE = RegHelper.registerBlock(Supplementaries.res("rope_chandelier"), () -> new DecoBlocksCompatImpl.RopeChandelierBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(0.3F).sound(SoundType.WOOD).noOcclusion().lightLevel(state -> 15), CompatObjects.CHANDELIER, () -> ParticleTypes.FLAME));

    public static final Supplier<Block> SOUL_CHANDELIER_ROPE = RegHelper.registerBlock(Supplementaries.res("rope_soul_chandelier"), () -> new DecoBlocksCompatImpl.RopeChandelierBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(0.3F).sound(SoundType.WOOD).noOcclusion().lightLevel(state -> 11), CompatObjects.SOUL_CHANDELIER, () -> ParticleTypes.SOUL_FIRE_FLAME));

    public static final Supplier<Block> ENDER_CHANDELIER_ROPE;

    public static final Supplier<Block> GLOW_CHANDELIER_ROPE;

    public static boolean isPalisade(BlockState state) {
        return state.m_60734_() instanceof PalisadeBlock;
    }

    public static void tryConvertingRopeChandelier(BlockState facingState, LevelAccessor world, BlockPos facingPos) {
        Block b = facingState.m_60734_();
        if (b == CompatObjects.CHANDELIER.get()) {
            world.m_7731_(facingPos, ((Block) CHANDELIER_ROPE.get()).defaultBlockState(), 3);
        } else if (b == CompatObjects.SOUL_CHANDELIER.get()) {
            world.m_7731_(facingPos, ((Block) SOUL_CHANDELIER_ROPE.get()).defaultBlockState(), 3);
        } else if (b == CompatObjects.ENDER_CHANDELIER.get()) {
            world.m_7731_(facingPos, ((Block) ENDER_CHANDELIER_ROPE.get()).defaultBlockState(), 3);
        } else if (b == CompatObjects.GLOW_CHANDELIER.get()) {
            world.m_7731_(facingPos, ((Block) GLOW_CHANDELIER_ROPE.get()).defaultBlockState(), 3);
        }
    }

    public static void init() {
    }

    public static void setupClient() {
        if (CHANDELIER_ROPE != null) {
            ClientHelper.registerRenderType((Block) CHANDELIER_ROPE.get(), RenderType.cutout());
        }
        if (SOUL_CHANDELIER_ROPE != null) {
            ClientHelper.registerRenderType((Block) SOUL_CHANDELIER_ROPE.get(), RenderType.cutout());
        }
        if (CompatHandler.DECO_BLOCKS_ABNORMALS && ENDER_CHANDELIER_ROPE != null) {
            ClientHelper.registerRenderType((Block) ENDER_CHANDELIER_ROPE.get(), RenderType.cutout());
        }
        if (CompatHandler.MUCH_MORE_MOD_COMPAT && GLOW_CHANDELIER_ROPE != null) {
            ClientHelper.registerRenderType((Block) GLOW_CHANDELIER_ROPE.get(), RenderType.cutout());
        }
    }

    static {
        if (CompatHandler.DECO_BLOCKS_ABNORMALS) {
            ENDER_CHANDELIER_ROPE = RegHelper.registerBlock(Supplementaries.res("rope_ender_chandelier"), () -> new DecoBlocksCompatImpl.RopeChandelierBlock(BlockBehaviour.Properties.of().strength(0.3F).mapColor(MapColor.WOOD).sound(SoundType.WOOD).noOcclusion().lightLevel(state -> 15), CompatObjects.ENDER_CHANDELIER, CompatObjects.ENDER_FLAME));
        } else {
            ENDER_CHANDELIER_ROPE = null;
        }
        if (CompatHandler.MUCH_MORE_MOD_COMPAT) {
            GLOW_CHANDELIER_ROPE = RegHelper.registerBlock(Supplementaries.res("rope_glow_chandelier"), () -> new DecoBlocksCompatImpl.RopeChandelierBlock(BlockBehaviour.Properties.of().strength(0.3F).mapColor(MapColor.WOOD).sound(SoundType.WOOD).noOcclusion().lightLevel(state -> 15), CompatObjects.GLOW_CHANDELIER, CompatObjects.GLOW_FLAME));
        } else {
            GLOW_CHANDELIER_ROPE = null;
        }
    }

    public static class RopeChandelierBlock extends ChandelierBlock {

        private final Supplier<Block> mimic;

        private final Lazy<BlockState> defMimic;

        protected final Lazy<SimpleParticleType> particleData;

        public <T extends ParticleType<?>> RopeChandelierBlock(BlockBehaviour.Properties properties, Supplier<Block> chandelier, Supplier<T> particleData) {
            super(properties, false);
            this.mimic = chandelier;
            this.defMimic = Lazy.of(() -> ((Block) this.mimic.get()).defaultBlockState());
            this.particleData = Lazy.of(() -> {
                SimpleParticleType data = (SimpleParticleType) particleData.get();
                if (data == null) {
                    data = ParticleTypes.FLAME;
                }
                return data;
            });
        }

        public MutableComponent getName() {
            return ((Block) this.mimic.get()).getName();
        }

        public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
            return ((Block) this.mimic.get()).getCloneItemStack((BlockState) this.defMimic.get(), target, world, pos, player);
        }

        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            return ((Block) this.mimic.get()).m_49635_((BlockState) this.defMimic.get(), builder);
        }

        public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
            return facing == Direction.UP && !(facingState.m_60734_() instanceof AbstractRopeBlock) ? (BlockState) this.defMimic.get() : stateIn;
        }

        public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
            double d0 = (double) pos.m_123341_() + 0.5;
            double d1 = (double) pos.m_123342_() + 0.7;
            double d2 = (double) pos.m_123343_() + 0.5;
            double off1 = 0.1875;
            double off2 = 0.3125;
            double off3 = 0.0625;
            worldIn.addParticle(ParticleTypes.SMOKE, d0 - off1, d1, d2 - off2, 0.0, 0.0, 0.0);
            worldIn.addParticle(ParticleTypes.SMOKE, d0 - off2 - off3, d1, d2 + off1 - off3, 0.0, 0.0, 0.0);
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + off1 - off3, d1, d2 + off2 + off3, 0.0, 0.0, 0.0);
            worldIn.addParticle(ParticleTypes.SMOKE, d0 + off2, d1, d2 - off1, 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0 - off1, d1, d2 - off2, 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0 - off2 - off3, d1, d2 + off1 - off3, 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0 + off1 - off3, d1, d2 + off2 + off3, 0.0, 0.0, 0.0);
            worldIn.addParticle((ParticleOptions) this.particleData.get(), d0 + off2, d1, d2 - off1, 0.0, 0.0, 0.0);
        }
    }
}