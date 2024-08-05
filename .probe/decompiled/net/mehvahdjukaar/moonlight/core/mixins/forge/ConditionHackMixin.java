package net.mehvahdjukaar.moonlight.core.mixins.forge;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.forge.MoonlightForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ SimplePreparableReloadListener.class })
public abstract class ConditionHackMixin {

    @Inject(at = { @At("HEAD") }, method = { "lambda$reload$1", "m_10789_", "method_18790" })
    private void applyResourceConditions(ResourceManager resourceManager, ProfilerFiller profiler, Object object, CallbackInfo ci) {
        if (this instanceof SimpleJsonResourceReloadListener) {
            ICondition.IContext context = MoonlightForge.getConditionContext();
            if (context == null) {
                return;
            }
            Iterator<Entry<ResourceLocation, JsonElement>> it = ((Map) object).entrySet().iterator();
            while (it.hasNext()) {
                Entry<ResourceLocation, JsonElement> entry = (Entry<ResourceLocation, JsonElement>) it.next();
                JsonElement resourceData = (JsonElement) entry.getValue();
                if (resourceData.isJsonObject()) {
                    JsonObject obj = resourceData.getAsJsonObject();
                    if (!CraftingHelper.processConditions(obj, "global_conditions", context)) {
                        it.remove();
                    }
                }
            }
        }
    }
}