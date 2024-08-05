package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import java.util.Collection;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;

public interface LivingDropsCallback {

    EventInvoker<LivingDropsCallback> EVENT = EventInvoker.lookup(LivingDropsCallback.class);

    EventResult onLivingDrops(LivingEntity var1, DamageSource var2, Collection<ItemEntity> var3, int var4, boolean var5);
}