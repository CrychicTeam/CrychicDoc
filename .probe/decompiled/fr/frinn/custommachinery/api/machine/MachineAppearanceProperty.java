package fr.frinn.custommachinery.api.machine;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class MachineAppearanceProperty<T> {

    public static final ResourceKey<Registry<MachineAppearanceProperty<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ICustomMachineryAPI.INSTANCE.rl("appearance_property"));

    private final NamedCodec<T> codec;

    private final T defaultValue;

    public static <T> MachineAppearanceProperty<T> create(NamedCodec<T> codec, T defaultValue) {
        return new MachineAppearanceProperty<>(codec, defaultValue);
    }

    private MachineAppearanceProperty(NamedCodec<T> codec, T defaultValue) {
        this.codec = codec;
        this.defaultValue = defaultValue;
    }

    public NamedCodec<T> getCodec() {
        return this.codec;
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public ResourceLocation getId() {
        return ICustomMachineryAPI.INSTANCE.appearancePropertyRegistrar().getId(this);
    }
}