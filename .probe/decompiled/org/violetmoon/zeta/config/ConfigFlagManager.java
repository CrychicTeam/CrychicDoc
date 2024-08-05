package org.violetmoon.zeta.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.recipe.FlagIngredient;
import org.violetmoon.zeta.registry.CraftingExtensionsRegistry;

public final class ConfigFlagManager {

    public final Zeta zeta;

    private final Set<String> allFlags = new HashSet();

    private final Map<String, Boolean> flags = new HashMap();

    public final FlagIngredient.Serializer flagIngredientSerializer = new FlagIngredient.Serializer(this);

    public ConfigFlagManager(Zeta zeta) {
        this.zeta = zeta;
        zeta.loadBus.subscribe(this);
    }

    @LoadEvent
    public void onRegister(ZRegister event) {
        CraftingExtensionsRegistry ext = event.getCraftingExtensionsRegistry();
        ext.registerConditionSerializer(new FlagCondition.Serializer(this, new ResourceLocation(this.zeta.modid, "flag")));
        ext.registerConditionSerializer(new FlagCondition.Serializer(this, new ResourceLocation(this.zeta.modid, "advancement_flag"), () -> ZetaGeneralConfig.enableModdedAdvancements));
        FlagLootCondition.FlagSerializer flagSerializer = new FlagLootCondition.FlagSerializer(this);
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation(this.zeta.modid, "flag"), flagSerializer.selfType);
        ext.registerIngredientSerializer(new ResourceLocation(this.zeta.modid, "flag"), this.flagIngredientSerializer);
        SyncedFlagHandler.setupFlagManager(this);
    }

    public void clear() {
        this.flags.clear();
    }

    public void putFlag(ZetaModule module, String flag, boolean value) {
        this.flags.put(flag, value && module.enabled);
        if (!this.allFlags.contains(flag)) {
            this.allFlags.add(flag);
        }
    }

    public void putModuleFlag(ZetaModule module) {
        this.putFlag(module, module.lowercaseName, true);
    }

    public boolean isValidFlag(String flag) {
        return this.flags.containsKey(flag);
    }

    public boolean getFlag(String flag) {
        Boolean obj = (Boolean) this.flags.get(flag);
        return obj != null && obj;
    }

    public Set<String> getAllFlags() {
        return this.allFlags;
    }
}