package net.mehvahdjukaar.supplementaries.integration;

import com.teamabode.cave_enhancements.common.block.entity.SpectacleCandleBlockEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.misc.RegSupplier;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.common.block.blocks.CandleHolderBlock;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import org.jetbrains.annotations.Nullable;

public class CaveEnhancementsCompat {

    private static final List<Supplier<? extends Block>> SPECTACLE_CANDLE_HOLDERS = new ArrayList();

    private static Supplier<BlockEntityType<CaveEnhancementsCompat.SpectacleCandleHolderTile>> tile;

    public static void tick(Level level, BlockPos pos, BlockState state) {
        tick(level, pos, state, null);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CaveEnhancementsCompat.SpectacleCandleHolderTile e) {
        SpectacleCandleBlockEntity.tick(level, pos, state);
    }

    public static void registerCandle(ResourceLocation id) {
        String name = id.getPath() + "_spectacle";
        ResourceLocation res = new ResourceLocation(id.getNamespace(), name);
        RegSupplier<CaveEnhancementsCompat.SpectacleCandleHolder> b = RegHelper.registerBlockWithItem(res, () -> new CaveEnhancementsCompat.SpectacleCandleHolder(null, BlockBehaviour.Properties.copy((BlockBehaviour) ModRegistry.SCONCE.get()), () -> ParticleTypes.SMALL_FLAME));
        SPECTACLE_CANDLE_HOLDERS.add(b);
        tile = RegHelper.registerBlockEntityType(res, () -> PlatHelper.newBlockEntityType(CaveEnhancementsCompat.SpectacleCandleHolderTile::new, (Block[]) SPECTACLE_CANDLE_HOLDERS.stream().map(Supplier::get).toArray(Block[]::new)));
    }

    public static void setupClient() {
        SPECTACLE_CANDLE_HOLDERS.forEach(b -> ClientHelper.registerRenderType((Block) b.get(), RenderType.cutout()));
    }

    private static class SpectacleCandleHolder extends CandleHolderBlock implements EntityBlock {

        public SpectacleCandleHolder(DyeColor color, BlockBehaviour.Properties properties, Supplier<ParticleType<? extends ParticleOptions>> particle) {
            super(color, properties, particle);
        }

        @Nullable
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new CaveEnhancementsCompat.SpectacleCandleHolderTile(pos, state);
        }

        @Nullable
        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
            return level.isClientSide ? null : Utils.getTicker(blockEntityType, (BlockEntityType) CaveEnhancementsCompat.tile.get(), CaveEnhancementsCompat::tick);
        }

        @Override
        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            return super.m_49635_(state, builder);
        }
    }

    private static class SpectacleCandleHolderTile extends BlockEntity {

        public SpectacleCandleHolderTile(BlockPos blockPos, BlockState blockState) {
            super((BlockEntityType<?>) CaveEnhancementsCompat.tile.get(), blockPos, blockState);
        }
    }
}