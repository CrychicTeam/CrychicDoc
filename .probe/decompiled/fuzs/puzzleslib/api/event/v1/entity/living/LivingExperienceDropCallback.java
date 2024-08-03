package fuzs.puzzleslib.api.event.v1.entity.living;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface LivingExperienceDropCallback {

    EventInvoker<LivingExperienceDropCallback> EVENT = EventInvoker.lookup(LivingExperienceDropCallback.class);

    EventResult onLivingExperienceDrop(LivingEntity var1, @Nullable Player var2, DefaultedInt var3);
}