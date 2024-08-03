package net.minecraftforge.client.event;

import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RegisterShadersEvent extends Event implements IModBusEvent {

    private final ResourceProvider resourceProvider;

    private final List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList;

    @Internal
    public RegisterShadersEvent(ResourceProvider resourceProvider, List<Pair<ShaderInstance, Consumer<ShaderInstance>>> shaderList) {
        this.resourceProvider = resourceProvider;
        this.shaderList = shaderList;
    }

    public ResourceProvider getResourceProvider() {
        return this.resourceProvider;
    }

    public void registerShader(ShaderInstance shaderInstance, Consumer<ShaderInstance> onLoaded) {
        this.shaderList.add(Pair.of(shaderInstance, onLoaded));
    }
}