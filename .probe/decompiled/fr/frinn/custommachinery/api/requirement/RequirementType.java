package fr.frinn.custommachinery.api.requirement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RequirementType<T extends IRequirement<?>> {

    public static final ResourceKey<Registry<RequirementType<? extends IRequirement<?>>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ICustomMachineryAPI.INSTANCE.rl("requirement_type"));

    private final NamedCodec<T> codec;

    private final boolean isWorldRequirement;

    public static <T extends IRequirement<?>> RequirementType<T> world(NamedCodec<T> codec) {
        return new RequirementType<>(codec, true);
    }

    public static <T extends IRequirement<?>> RequirementType<T> inventory(NamedCodec<T> codec) {
        return new RequirementType<>(codec, false);
    }

    private RequirementType(NamedCodec<T> codec, boolean isWorldRequirement) {
        this.codec = codec;
        this.isWorldRequirement = isWorldRequirement;
    }

    public NamedCodec<T> getCodec() {
        return this.codec;
    }

    public boolean isWorldRequirement() {
        return this.isWorldRequirement;
    }

    public ResourceLocation getId() {
        return ICustomMachineryAPI.INSTANCE.requirementRegistrar().getId(this);
    }

    public Component getName() {
        return this.getId() == null ? Component.literal("unknown") : Component.translatable("requirement." + this.getId().getNamespace() + "." + this.getId().getPath());
    }
}