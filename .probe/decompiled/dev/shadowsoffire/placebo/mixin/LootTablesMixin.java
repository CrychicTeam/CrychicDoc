package dev.shadowsoffire.placebo.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonElement;
import dev.shadowsoffire.placebo.Placebo;
import dev.shadowsoffire.placebo.loot.LootSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LootDataManager.class })
public class LootTablesMixin {

    @Shadow
    private Map<LootDataId<?>, ?> elements;

    @Shadow
    private Multimap<LootDataType<?>, ResourceLocation> typeKeys;

    @Inject(method = { "apply(Ljava/util/Map;)V" }, at = { @At("TAIL") }, require = 1)
    protected void apply(Map<ResourceLocation, JsonElement> pObject, CallbackInfo ci) {
        if (!LootSystem.PLACEBO_TABLES.isEmpty()) {
            this.elements = new HashMap(this.elements);
            this.typeKeys = HashMultimap.create(this.typeKeys);
            LootSystem.PLACEBO_TABLES.forEach((key, val) -> {
                if (!this.elements.containsKey(key)) {
                    this.elements.put(key, val);
                    this.typeKeys.put(LootDataType.TABLE, key.location());
                }
            });
            Placebo.LOGGER.info("Registered {} additional loot tables.", LootSystem.PLACEBO_TABLES.size());
        }
    }
}