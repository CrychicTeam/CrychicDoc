package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import java.util.function.Consumer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@FunctionalInterface
@OnlyIn(Dist.CLIENT)
public interface ClientReloadShadersEvent {

    Event<ClientReloadShadersEvent> EVENT = EventFactory.createLoop();

    void reload(ResourceProvider var1, ClientReloadShadersEvent.ShadersSink var2);

    @FunctionalInterface
    public interface ShadersSink {

        void registerShader(ShaderInstance var1, Consumer<ShaderInstance> var2);
    }
}