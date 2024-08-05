package snownee.kiwi.mixin;

import net.minecraft.core.Registry;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ TagsProvider.class })
public interface TagsProviderAccess<T> {

    @Accessor
    ResourceKey<? extends Registry<T>> getRegistryKey();

    @Accessor(remap = false)
    String getModId();

    @Invoker
    TagBuilder callGetOrCreateRawBuilder(TagKey<T> var1);
}