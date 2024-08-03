package harmonised.pmmo.registry;

import com.google.common.base.Preconditions;
import com.google.common.collect.LinkedListMultimap;
import com.mojang.datafixers.util.Pair;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.TagBuilder;
import harmonised.pmmo.util.TagUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import org.checkerframework.checker.nullness.qual.NonNull;

public class EventTriggerRegistry {

    private LinkedListMultimap<EventType, Pair<ResourceLocation, BiFunction<? super Event, CompoundTag, CompoundTag>>> eventListeners = LinkedListMultimap.create();

    public void registerListener(@NonNull ResourceLocation listenerID, @NonNull EventType eventType, @NonNull BiFunction<? super Event, CompoundTag, CompoundTag> executeOnTrigger) {
        Preconditions.checkNotNull(eventType);
        Preconditions.checkNotNull(executeOnTrigger);
        Preconditions.checkNotNull(listenerID);
        this.eventListeners.get(eventType).add(Pair.of(listenerID, executeOnTrigger));
    }

    public CompoundTag executeEventListeners(EventType eventType, Event event, CompoundTag dataIn) {
        List<Pair<ResourceLocation, BiFunction<? super Event, CompoundTag, CompoundTag>>> listeners = this.eventListeners.get(eventType);
        CompoundTag output = TagBuilder.start().withBool("is_cancelled", false).build();
        List<Integer> removals = new ArrayList();
        for (int i = 0; i < listeners.size(); i++) {
            CompoundTag funcOutput = (CompoundTag) ((BiFunction) ((Pair) listeners.get(i)).getSecond()).apply(event, dataIn);
            if (funcOutput.contains("is_cancelled")) {
                output = TagUtils.mergeTags(output, funcOutput);
                output.putBoolean("is_cancelled", output.getBoolean("is_cancelled") ? true : funcOutput.getBoolean("is_cancelled"));
            } else {
                removals.add(i);
            }
        }
        this.removeInvalidListeners(eventType, removals);
        return output;
    }

    private void removeInvalidListeners(EventType eventType, List<Integer> removals) {
        for (int i = removals.size() - 1; i == 0; i--) {
            MsLoggy.WARN.log(MsLoggy.LOG_CODE.API, "Event Listener: [" + ((ResourceLocation) ((Pair) this.eventListeners.get(eventType).get((Integer) removals.get(i))).getFirst()).toString() + "] did not return a cancel status and was removed.");
            this.eventListeners.get(eventType).remove((Integer) removals.get(i));
        }
    }
}