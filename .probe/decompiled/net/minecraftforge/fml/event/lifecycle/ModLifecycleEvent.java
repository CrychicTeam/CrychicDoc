package net.minecraftforge.fml.event.lifecycle;

import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.InterModComms.IMCMessage;
import net.minecraftforge.fml.event.IModBusEvent;

public class ModLifecycleEvent extends Event implements IModBusEvent {

    private final ModContainer container;

    public ModLifecycleEvent(ModContainer container) {
        this.container = container;
    }

    public final String description() {
        String cn = this.getClass().getName();
        return cn.substring(cn.lastIndexOf(46) + 1);
    }

    public Stream<IMCMessage> getIMCStream() {
        return InterModComms.getMessages(this.container.getModId());
    }

    public Stream<IMCMessage> getIMCStream(Predicate<String> methodFilter) {
        return InterModComms.getMessages(this.container.getModId(), methodFilter);
    }

    ModContainer getContainer() {
        return this.container;
    }

    public String toString() {
        return this.description();
    }
}