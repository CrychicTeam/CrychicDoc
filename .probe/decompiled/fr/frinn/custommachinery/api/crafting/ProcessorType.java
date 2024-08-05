package fr.frinn.custommachinery.api.crafting;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ProcessorType<T extends IProcessor> {

    public static final ResourceKey<Registry<ProcessorType<? extends IProcessor>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ICustomMachineryAPI.INSTANCE.rl("processor_type"));

    private final NamedCodec<? extends IProcessorTemplate<T>> codec;

    public static <T extends IProcessor> ProcessorType<T> create(NamedCodec<? extends IProcessorTemplate<T>> codec) {
        return new ProcessorType<>(codec);
    }

    private ProcessorType(NamedCodec<? extends IProcessorTemplate<T>> codec) {
        this.codec = codec;
    }

    public NamedCodec<? extends IProcessorTemplate<T>> getCodec() {
        return this.codec;
    }

    public ResourceLocation getId() {
        return ICustomMachineryAPI.INSTANCE.processorRegistrar().getId(this);
    }
}