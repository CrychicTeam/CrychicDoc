package fr.frinn.custommachinery.api.component;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import fr.frinn.custommachinery.api.component.handler.IComponentHandler;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class MachineComponentType<T extends IMachineComponent> {

    public static final ResourceKey<Registry<MachineComponentType<? extends IMachineComponent>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ICustomMachineryAPI.INSTANCE.rl("component_type"));

    private NamedCodec<? extends IMachineComponentTemplate<T>> codec;

    private boolean isSingle = true;

    private BiFunction<IMachineComponentManager, List<T>, IComponentHandler<T>> handlerBuilder;

    private boolean defaultComponent = false;

    private Function<IMachineComponentManager, T> defaultComponentBuilder;

    public static <T extends IMachineComponent> MachineComponentType<T> create(NamedCodec<? extends IMachineComponentTemplate<T>> codec) {
        return new MachineComponentType<>(codec);
    }

    public static <T extends IMachineComponent> MachineComponentType<T> create(Function<IMachineComponentManager, T> defaultComponentBuilder) {
        return new MachineComponentType<>(defaultComponentBuilder);
    }

    public static <T extends IMachineComponent> MachineComponentType<T> create(NamedCodec<? extends IMachineComponentTemplate<T>> codec, Function<IMachineComponentManager, T> defaultComponentBuilder) {
        return new MachineComponentType<>(codec, defaultComponentBuilder);
    }

    private MachineComponentType(NamedCodec<? extends IMachineComponentTemplate<T>> codec) {
        this.codec = codec;
    }

    private MachineComponentType(Function<IMachineComponentManager, T> defaultComponentBuilder) {
        this.defaultComponent = true;
        this.defaultComponentBuilder = defaultComponentBuilder;
    }

    private MachineComponentType(NamedCodec<? extends IMachineComponentTemplate<T>> codec, Function<IMachineComponentManager, T> defaultComponentBuilder) {
        this.codec = codec;
        this.defaultComponent = true;
        this.defaultComponentBuilder = defaultComponentBuilder;
    }

    public MachineComponentType<T> setNotSingle(BiFunction<IMachineComponentManager, List<T>, IComponentHandler<T>> handlerBuilder) {
        this.isSingle = false;
        this.handlerBuilder = handlerBuilder;
        return this;
    }

    public NamedCodec<? extends IMachineComponentTemplate<T>> getCodec() {
        if (this.codec != null) {
            return this.codec;
        } else {
            throw new RuntimeException("Error while trying to serialize or deserialize Machine Component template: " + this.getId() + ", Codec not present !");
        }
    }

    public boolean isSingle() {
        return this.isSingle;
    }

    public IComponentHandler<T> getHandler(IMachineComponentManager manager, List<T> components) {
        return !this.isSingle && this.handlerBuilder != null ? (IComponentHandler) this.handlerBuilder.apply(manager, components) : null;
    }

    public boolean isDefaultComponent() {
        return this.defaultComponent;
    }

    public Function<IMachineComponentManager, T> getDefaultComponentBuilder() {
        return this.defaultComponentBuilder;
    }

    public ResourceLocation getId() {
        return ICustomMachineryAPI.INSTANCE.componentRegistrar().getId(this);
    }

    public Component getTranslatedName() {
        if (this.getId() == null) {
            throw new IllegalStateException("Trying to get the registry name of an unregistered MachineComponentType");
        } else {
            return Component.translatable(this.getId().getNamespace() + ".machine.component." + this.getId().getPath());
        }
    }
}