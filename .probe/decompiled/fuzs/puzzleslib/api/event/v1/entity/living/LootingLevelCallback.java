package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface LootingLevelCallback {

    EventInvoker<LootingLevelCallback> EVENT = EventInvoker.lookup(LootingLevelCallback.class);

    void onLootingLevel(LivingEntity var1, @Nullable DamageSource var2, MutableInt var3);
}