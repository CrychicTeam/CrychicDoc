package fr.frinn.custommachinery.api.component.variant;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.IMachineComponent;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class RegisterComponentVariantEvent {

    public static final Event<RegisterComponentVariantEvent.Register> EVENT = EventFactory.createLoop();

    private final Map<MachineComponentType<? extends IMachineComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> componentVariants = new HashMap();

    public <C extends IMachineComponent> void register(MachineComponentType<C> type, ResourceLocation id, NamedCodec<? extends IComponentVariant> codec) {
        if (((Map) this.componentVariants.computeIfAbsent(type, t -> new HashMap())).containsKey(id)) {
            throw new IllegalArgumentException("Component variant " + id + " already registered for type: " + type.getId());
        } else {
            ((Map) this.componentVariants.get(type)).put(id, codec);
        }
    }

    public Map<MachineComponentType<? extends IMachineComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> getComponentVariants() {
        Builder<MachineComponentType<? extends IMachineComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> builder = ImmutableMap.builder();
        this.componentVariants.forEach((type, map) -> builder.put(type, ImmutableMap.copyOf(map)));
        return builder.build();
    }

    public interface Register {

        void registerComponentVariant(RegisterComponentVariantEvent var1);
    }
}