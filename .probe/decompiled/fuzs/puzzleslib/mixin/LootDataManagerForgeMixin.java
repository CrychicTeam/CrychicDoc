package fuzs.puzzleslib.mixin;

import fuzs.puzzleslib.impl.event.LootTableModifyEvent;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootDataId;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LootDataManager.class })
abstract class LootDataManagerForgeMixin {

    @Shadow
    private Map<LootDataId<?>, ?> elements;

    @Inject(method = { "apply" }, at = { @At("TAIL") })
    private void apply(Map<LootDataType<?>, Map<ResourceLocation, ?>> map, CallbackInfo callback) {
        for (Entry<LootDataId<?>, ?> entry : this.elements.entrySet()) {
            if (((LootDataId) entry.getKey()).type() == LootDataType.TABLE) {
                MinecraftForge.EVENT_BUS.post(new LootTableModifyEvent((LootDataManager) LootDataManager.class.cast(this), ((LootDataId) entry.getKey()).location(), (LootTable) entry.getValue()));
            }
        }
    }
}