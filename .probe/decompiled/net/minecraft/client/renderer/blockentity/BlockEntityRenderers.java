package net.minecraft.client.renderer.blockentity;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRenderers {

    private static final Map<BlockEntityType<?>, BlockEntityRendererProvider<?>> PROVIDERS = Maps.newHashMap();

    private static <T extends BlockEntity> void register(BlockEntityType<? extends T> blockEntityTypeExtendsT0, BlockEntityRendererProvider<T> blockEntityRendererProviderT1) {
        PROVIDERS.put(blockEntityTypeExtendsT0, blockEntityRendererProviderT1);
    }

    public static Map<BlockEntityType<?>, BlockEntityRenderer<?>> createEntityRenderers(BlockEntityRendererProvider.Context blockEntityRendererProviderContext0) {
        Builder<BlockEntityType<?>, BlockEntityRenderer<?>> $$1 = ImmutableMap.builder();
        PROVIDERS.forEach((p_258150_, p_258151_) -> {
            try {
                $$1.put(p_258150_, p_258151_.create(blockEntityRendererProviderContext0));
            } catch (Exception var5) {
                throw new IllegalStateException("Failed to create model for " + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(p_258150_), var5);
            }
        });
        return $$1.build();
    }

    static {
        register(BlockEntityType.SIGN, SignRenderer::new);
        register(BlockEntityType.HANGING_SIGN, HangingSignRenderer::new);
        register(BlockEntityType.MOB_SPAWNER, SpawnerRenderer::new);
        register(BlockEntityType.PISTON, PistonHeadRenderer::new);
        register(BlockEntityType.CHEST, ChestRenderer::new);
        register(BlockEntityType.ENDER_CHEST, ChestRenderer::new);
        register(BlockEntityType.TRAPPED_CHEST, ChestRenderer::new);
        register(BlockEntityType.ENCHANTING_TABLE, EnchantTableRenderer::new);
        register(BlockEntityType.LECTERN, LecternRenderer::new);
        register(BlockEntityType.END_PORTAL, TheEndPortalRenderer::new);
        register(BlockEntityType.END_GATEWAY, TheEndGatewayRenderer::new);
        register(BlockEntityType.BEACON, BeaconRenderer::new);
        register(BlockEntityType.SKULL, SkullBlockRenderer::new);
        register(BlockEntityType.BANNER, BannerRenderer::new);
        register(BlockEntityType.STRUCTURE_BLOCK, StructureBlockRenderer::new);
        register(BlockEntityType.SHULKER_BOX, ShulkerBoxRenderer::new);
        register(BlockEntityType.BED, BedRenderer::new);
        register(BlockEntityType.CONDUIT, ConduitRenderer::new);
        register(BlockEntityType.BELL, BellRenderer::new);
        register(BlockEntityType.CAMPFIRE, CampfireRenderer::new);
        register(BlockEntityType.BRUSHABLE_BLOCK, BrushableBlockRenderer::new);
        register(BlockEntityType.DECORATED_POT, DecoratedPotRenderer::new);
    }
}