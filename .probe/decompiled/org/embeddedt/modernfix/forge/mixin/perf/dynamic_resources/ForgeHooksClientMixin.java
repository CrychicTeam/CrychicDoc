package org.embeddedt.modernfix.forge.mixin.perf.dynamic_resources;

import com.google.common.base.Stopwatch;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.forge.dynresources.ModelBakeEventHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ForgeHooksClient.class })
public class ForgeHooksClientMixin {

    @Redirect(method = { "onModifyBakingResult" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/ModLoader;postEvent(Lnet/minecraftforge/eventbus/api/Event;)V"), remap = false)
    private static void postNamespacedKeySetEvent(ModLoader loader, Event event) {
        if (ModLoader.isLoadingStateValid()) {
            ModelEvent.ModifyBakingResult bakeEvent = (ModelEvent.ModifyBakingResult) event;
            ModelBakeEventHelper helper = new ModelBakeEventHelper(bakeEvent.getModels());
            Method acceptEv = ObfuscationReflectionHelper.findMethod(ModContainer.class, "acceptEvent", new Class[] { Event.class });
            ModList.get().forEachModContainer((id, mc) -> {
                Map<ResourceLocation, BakedModel> newRegistry = helper.wrapRegistry(id);
                ModelEvent.ModifyBakingResult postedEvent = new ModelEvent.ModifyBakingResult(newRegistry, bakeEvent.getModelBakery());
                Stopwatch timer = Stopwatch.createStarted();
                try {
                    acceptEv.invoke(mc, postedEvent);
                } catch (ReflectiveOperationException var9) {
                    var9.printStackTrace();
                }
                timer.stop();
                if (timer.elapsed(TimeUnit.SECONDS) >= 1L) {
                    ModernFix.LOGGER.warn("Mod '{}' took {} in the model bake event", id, timer);
                }
            });
        }
    }
}