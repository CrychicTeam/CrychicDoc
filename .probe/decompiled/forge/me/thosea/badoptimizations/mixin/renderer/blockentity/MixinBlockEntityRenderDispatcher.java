package forge.me.thosea.badoptimizations.mixin.renderer.blockentity;

import forge.me.thosea.badoptimizations.interfaces.BlockEntityTypeMethods;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { BlockEntityRenderDispatcher.class }, priority = 700)
public abstract class MixinBlockEntityRenderDispatcher {

    @Shadow
    private Map<BlockEntityType<?>, BlockEntityRenderer<?>> renderers;

    @Overwrite
    @Nullable
    public <E extends BlockEntity> BlockEntityRenderer<E> getRenderer(E blockEntity) {
        return ((BlockEntityTypeMethods) blockEntity.getType()).bo$getRenderer();
    }

    @Inject(method = { "reload" }, at = { @At("RETURN") })
    private void afterReload(ResourceManager manager, CallbackInfo ci) {
        for (Entry<BlockEntityType<?>, BlockEntityRenderer<?>> entry : this.renderers.entrySet()) {
            ((BlockEntityTypeMethods) entry.getKey()).bo$setRenderer((BlockEntityRenderer<?>) entry.getValue());
        }
    }
}