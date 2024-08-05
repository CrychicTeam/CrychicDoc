package net.minecraftforge.common.capabilities;

import java.util.Objects;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.objectweb.asm.Type;

public final class RegisterCapabilitiesEvent extends Event implements IModBusEvent {

    public <T> void register(Class<T> type) {
        Objects.requireNonNull(type, "Attempted to register a capability with invalid type");
        CapabilityManager.INSTANCE.get(Type.getInternalName(type), true);
    }
}