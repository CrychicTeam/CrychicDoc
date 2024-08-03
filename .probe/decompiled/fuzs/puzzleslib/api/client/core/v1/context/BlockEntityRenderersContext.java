package fuzs.puzzleslib.api.client.core.v1.context;

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@FunctionalInterface
public interface BlockEntityRenderersContext {

    <T extends BlockEntity> void registerBlockEntityRenderer(BlockEntityType<? extends T> var1, BlockEntityRendererProvider<T> var2);
}