package fuzs.puzzleslib.api.event.v1.entity.player;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedValue;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import fuzs.puzzleslib.api.event.v1.data.MutableValue;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class GrindstoneEvents {

    public static final EventInvoker<GrindstoneEvents.Update> UPDATE = EventInvoker.lookup(GrindstoneEvents.Update.class);

    public static final EventInvoker<GrindstoneEvents.Use> USE = EventInvoker.lookup(GrindstoneEvents.Use.class);

    private GrindstoneEvents() {
    }

    public interface Update {

        EventResult onGrindstoneUpdate(ItemStack var1, ItemStack var2, MutableValue<ItemStack> var3, MutableInt var4, Player var5);
    }

    public interface Use {

        void onGrindstoneUse(DefaultedValue<ItemStack> var1, DefaultedValue<ItemStack> var2, Player var3);
    }
}