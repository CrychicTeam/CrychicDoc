package org.violetmoon.zeta.util;

import com.google.common.collect.ImmutableList.Builder;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.Zeta;
import org.violetmoon.zeta.block.IZetaBlock;

public class RegistryUtil {

    protected final Zeta z;

    public static <T> List<T> massRegistryGet(Collection<String> coll, Registry<T> reg) {
        Builder<T> builder = new Builder();
        for (String s : coll) {
            if (s.startsWith("#")) {
                TagKey<T> tag = TagKey.create(reg.key(), new ResourceLocation(s.substring(1)));
                reg.getTagOrEmpty(tag).forEach(tHolder -> builder.add(tHolder.value()));
            } else {
                reg.getOptional(new ResourceLocation(s)).ifPresent(builder::add);
            }
        }
        return builder.build();
    }

    public static <T> List<T> getTagValues(RegistryAccess access, TagKey<T> tag) {
        return (List<T>) access.registryOrThrow(tag.registry()).getTag(tag).map(holderset -> holderset.m_203614_().map(Holder::m_203334_).toList()).orElseGet(Collections::emptyList);
    }

    public RegistryUtil(Zeta z) {
        this.z = z;
    }

    @Nullable
    public String inheritQuark(IZetaBlock parent, String format) {
        return this.inherit(parent.getBlock(), format);
    }

    @Nullable
    public String inherit(Block parent, String format) {
        ResourceLocation parentName = this.z.registry.getRegistryName(parent, BuiltInRegistries.BLOCK);
        return parentName == null ? null : String.format(String.format("%s:%s", this.z.modid, format), parentName.getPath());
    }

    @Nullable
    public String inherit(Block parent, Function<String, String> fun) {
        ResourceLocation parentName = this.z.registry.getRegistryName(parent, BuiltInRegistries.BLOCK);
        return parentName == null ? null : String.format(String.format("%s:%s", this.z.modid, fun.apply(parentName.getPath())));
    }
}