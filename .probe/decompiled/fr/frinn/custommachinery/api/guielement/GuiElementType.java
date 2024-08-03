package fr.frinn.custommachinery.api.guielement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class GuiElementType<T extends IGuiElement> {

    public static final ResourceKey<Registry<GuiElementType<? extends IGuiElement>>> REGISTRY_KEY = ResourceKey.createRegistryKey(ICustomMachineryAPI.INSTANCE.rl("gui_element_type"));

    private final NamedCodec<T> codec;

    public static <T extends IGuiElement> GuiElementType<T> create(NamedCodec<T> codec) {
        return new GuiElementType<>(codec);
    }

    private GuiElementType(NamedCodec<T> codec) {
        this.codec = codec;
    }

    public NamedCodec<T> getCodec() {
        return this.codec;
    }

    public ResourceLocation getId() {
        return ICustomMachineryAPI.INSTANCE.guiElementRegistrar().getId(this);
    }

    public Component getTranslatedName() {
        if (this.getId() == null) {
            throw new IllegalStateException("Trying to get the registry name of an unregistered GuiElementType");
        } else {
            return Component.translatable(this.getId().getNamespace() + ".machine.guielement." + this.getId().getPath());
        }
    }
}