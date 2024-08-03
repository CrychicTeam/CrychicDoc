package dev.latvian.mods.kubejs.item.creativetab;

import dev.latvian.mods.kubejs.platform.MiscPlatformHelper;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

public class CreativeTabBuilder extends BuilderBase<CreativeModeTab> {

    public transient CreativeTabIconSupplier icon = CreativeTabIconSupplier.DEFAULT;

    public transient CreativeTabContentSupplier content = CreativeTabContentSupplier.DEFAULT;

    public CreativeTabBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.CREATIVE_MODE_TAB;
    }

    public CreativeModeTab createObject() {
        return MiscPlatformHelper.get().creativeModeTab((Component) (this.displayName == null ? Component.translatable(this.getBuilderTranslationKey()) : this.displayName), new CreativeTabIconSupplier.Wrapper(this.icon), new CreativeTabContentSupplier.Wrapper(this.content));
    }

    public CreativeTabBuilder icon(CreativeTabIconSupplier icon) {
        this.icon = icon;
        return this;
    }

    public CreativeTabBuilder content(CreativeTabContentSupplier content) {
        this.content = content;
        return this;
    }
}