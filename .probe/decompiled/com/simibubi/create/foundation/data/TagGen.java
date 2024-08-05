package com.simibubi.create.foundation.data;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.recipe.Mods;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagBuilder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class TagGen {

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOrPickaxe() {
        return b -> b.tag(new TagKey[] { BlockTags.MINEABLE_WITH_AXE }).tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE });
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> axeOnly() {
        return b -> b.tag(new TagKey[] { BlockTags.MINEABLE_WITH_AXE });
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, BlockBuilder<T, P>> pickaxeOnly() {
        return b -> b.tag(new TagKey[] { BlockTags.MINEABLE_WITH_PICKAXE });
    }

    public static <T extends Block, P> NonNullFunction<BlockBuilder<T, P>, ItemBuilder<BlockItem, BlockBuilder<T, P>>> tagBlockAndItem(String... path) {
        return b -> {
            for (String p : path) {
                b.tag(new TagKey[] { AllTags.forgeBlockTag(p) });
            }
            ItemBuilder<BlockItem, BlockBuilder<T, P>> item = b.item();
            for (String p : path) {
                item.tag(new TagKey[] { AllTags.forgeItemTag(p) });
            }
            return item;
        };
    }

    public static <T extends TagsProvider.TagAppender<?>> T addOptional(T appender, Mods mod, String id) {
        appender.addOptional(mod.asResource(id));
        return appender;
    }

    public static <T extends TagsProvider.TagAppender<?>> T addOptional(T appender, Mods mod, String... ids) {
        for (String id : ids) {
            appender.addOptional(mod.asResource(id));
        }
        return appender;
    }

    // $VF: Couldn't be decompiled
    // Please report this to the Vineflower issue tracker, at https://github.com/Vineflower/vineflower/issues with a copy of the class file (if you have the rights to distribute it!)
    // java.lang.RuntimeException: Constructor net/minecraft/data/tags/TagsProvider$TagAppender.<init>(Lnet/minecraft/tags/TagBuilder;Ljava/lang/String;)V not found
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExprUtil.getSyntheticParametersMask(ExprUtil.java:49)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.modules.decompiler.exps.ExprUtil.getSyntheticParametersMask(ExprUtil.java:35)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.InitializerProcessor.hideEmptySuper(InitializerProcessor.java:110)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.InitializerProcessor.extractInitializers(InitializerProcessor.java:51)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.invokeProcessors(ClassWriter.java:97)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeClass(ClassWriter.java:348)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassWriter.writeClass(ClassWriter.java:492)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.ClassesProcessor.writeClass(ClassesProcessor.java:474)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.main.Fernflower.getClassContent(Fernflower.java:191)
    //   at TRANSFORMER/vineflower@1.10.1/org.jetbrains.java.decompiler.struct.ContextUnit.lambda$save$3(ContextUnit.java:187)
    public static class CreateTagsProvider<T> {

        private RegistrateTagsProvider<T> provider;

        private Function<T, ResourceKey<T>> keyExtractor;

        public CreateTagsProvider(RegistrateTagsProvider<T> provider, Function<T, Holder.Reference<T>> refExtractor) {
            this.provider = provider;
            this.keyExtractor = refExtractor.andThen(Holder.Reference::m_205785_);
        }

        public TagGen.CreateTagAppender<T> tag(TagKey<T> tag) {
            TagBuilder tagbuilder = this.getOrCreateRawBuilder(tag);
            return new TagGen.CreateTagAppender<>(tagbuilder, this.keyExtractor, "create");
        }

        public TagBuilder getOrCreateRawBuilder(TagKey<T> tag) {
            return this.provider.addTag(tag).getInternalBuilder();
        }
    }
}