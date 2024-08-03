package forge.me.thosea.badoptimizations.mixin.renderer.entity;

import forge.me.thosea.badoptimizations.interfaces.EntityMethods;
import forge.me.thosea.badoptimizations.interfaces.EntityTypeMethods;
import forge.me.thosea.badoptimizations.other.PlayerModelRendererHolder;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { EntityRenderDispatcher.class }, priority = 700)
public abstract class MixinEntityRendererDispatcher {

    @Shadow
    private Map<EntityType<?>, EntityRenderer<?>> renderers;

    @Shadow
    private Map<String, EntityRenderer<? extends Player>> playerRenderers;

    @Overwrite
    public <T extends Entity & EntityMethods> EntityRenderer<? super T> getRenderer(T entity) {
        return entity.bo$getRenderer();
    }

    @Inject(method = { "reload" }, at = { @At("RETURN") })
    private void afterReload(ResourceManager manager, CallbackInfo ci) {
        for (Entry<EntityType<?>, EntityRenderer<?>> entry : this.renderers.entrySet()) {
            ((EntityTypeMethods) entry.getKey()).bo$setRenderer((EntityRenderer<?>) entry.getValue());
        }
        PlayerModelRendererHolder.WIDE_RENDERER = (EntityRenderer<? extends Player>) this.playerRenderers.get("default");
        PlayerModelRendererHolder.SLIM_RENDERER = (EntityRenderer<? extends Player>) this.playerRenderers.get("slim");
    }
}