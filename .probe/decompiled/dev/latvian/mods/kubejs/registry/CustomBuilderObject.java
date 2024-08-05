package dev.latvian.mods.kubejs.registry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public class CustomBuilderObject extends BuilderBase {

    private final Supplier<Object> object;

    private final RegistryInfo<?> registry;

    public CustomBuilderObject(ResourceLocation i, Supplier<Object> object, RegistryInfo<?> registry) {
        super(i);
        this.object = object;
        this.registry = registry;
        this.translationKey = this.getTranslationKeyGroup() + "." + this.id.getNamespace() + "." + this.id.getPath();
    }

    @Override
    public String getTranslationKeyGroup() {
        return this.getRegistryType() == null ? "If you see this something broke. Please file a bug report." : super.getTranslationKeyGroup();
    }

    @Override
    public RegistryInfo<?> getRegistryType() {
        return this.registry;
    }

    @Override
    public Object createObject() {
        return this.object.get();
    }
}