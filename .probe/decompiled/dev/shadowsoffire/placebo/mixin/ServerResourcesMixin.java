package dev.shadowsoffire.placebo.mixin;

import dev.shadowsoffire.placebo.recipe.RecipeHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Deprecated
@Mixin({ ReloadableServerResources.class })
public class ServerResourcesMixin {

    @Inject(method = { "listeners()Ljava/util/List;" }, at = { @At("RETURN") }, cancellable = true)
    public void placebo_listeners(CallbackInfoReturnable<List<PreparableReloadListener>> ci) {
        List<PreparableReloadListener> listeners = new ArrayList((Collection) ci.getReturnValue());
        listeners.add(RecipeHelper.getReloader(((ReloadableServerResources) this).getRecipeManager()));
        ci.setReturnValue(listeners);
    }
}