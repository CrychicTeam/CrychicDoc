package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.datafixers.util.Either;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ResourceOrTagArgument<T> implements ArgumentType<ResourceOrTagArgument.Result<T>> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons");

    private static final Dynamic2CommandExceptionType ERROR_UNKNOWN_TAG = new Dynamic2CommandExceptionType((p_250953_, p_249704_) -> Component.translatable("argument.resource_tag.not_found", p_250953_, p_249704_));

    private static final Dynamic3CommandExceptionType ERROR_INVALID_TAG_TYPE = new Dynamic3CommandExceptionType((p_250188_, p_252173_, p_251453_) -> Component.translatable("argument.resource_tag.invalid_type", p_250188_, p_252173_, p_251453_));

    private final HolderLookup<T> registryLookup;

    final ResourceKey<? extends Registry<T>> registryKey;

    public ResourceOrTagArgument(CommandBuildContext commandBuildContext0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1) {
        this.registryKey = resourceKeyExtendsRegistryT1;
        this.registryLookup = commandBuildContext0.holderLookup(resourceKeyExtendsRegistryT1);
    }

    public static <T> ResourceOrTagArgument<T> resourceOrTag(CommandBuildContext commandBuildContext0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1) {
        return new ResourceOrTagArgument<>(commandBuildContext0, resourceKeyExtendsRegistryT1);
    }

    public static <T> ResourceOrTagArgument.Result<T> getResourceOrTag(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, ResourceKey<Registry<T>> resourceKeyRegistryT2) throws CommandSyntaxException {
        ResourceOrTagArgument.Result<?> $$3 = (ResourceOrTagArgument.Result<?>) commandContextCommandSourceStack0.getArgument(string1, ResourceOrTagArgument.Result.class);
        Optional<ResourceOrTagArgument.Result<T>> $$4 = $$3.cast(resourceKeyRegistryT2);
        return (ResourceOrTagArgument.Result<T>) $$4.orElseThrow(() -> (CommandSyntaxException) $$3.unwrap().map(p_252340_ -> {
            ResourceKey<?> $$2 = p_252340_.key();
            return ResourceArgument.ERROR_INVALID_RESOURCE_TYPE.create($$2.location(), $$2.registry(), resourceKeyRegistryT2.location());
        }, p_250301_ -> {
            TagKey<?> $$2 = p_250301_.key();
            return ERROR_INVALID_TAG_TYPE.create($$2.location(), $$2.registry(), resourceKeyRegistryT2.location());
        }));
    }

    public ResourceOrTagArgument.Result<T> parse(StringReader stringReader0) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '#') {
            int $$1 = stringReader0.getCursor();
            try {
                stringReader0.skip();
                ResourceLocation $$2 = ResourceLocation.read(stringReader0);
                TagKey<T> $$3 = TagKey.create(this.registryKey, $$2);
                HolderSet.Named<T> $$4 = (HolderSet.Named<T>) this.registryLookup.m_254901_($$3).orElseThrow(() -> ERROR_UNKNOWN_TAG.create($$2, this.registryKey.location()));
                return new ResourceOrTagArgument.TagResult<>($$4);
            } catch (CommandSyntaxException var6) {
                stringReader0.setCursor($$1);
                throw var6;
            }
        } else {
            ResourceLocation $$6 = ResourceLocation.read(stringReader0);
            ResourceKey<T> $$7 = ResourceKey.create(this.registryKey, $$6);
            Holder.Reference<T> $$8 = (Holder.Reference<T>) this.registryLookup.m_254902_($$7).orElseThrow(() -> ResourceArgument.ERROR_UNKNOWN_RESOURCE.create($$6, this.registryKey.location()));
            return new ResourceOrTagArgument.ResourceResult<>($$8);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        SharedSuggestionProvider.suggestResource(this.registryLookup.listTagIds().map(TagKey::f_203868_), suggestionsBuilder1, "#");
        return SharedSuggestionProvider.suggestResource(this.registryLookup.listElementIds().map(ResourceKey::m_135782_), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info<T> implements ArgumentTypeInfo<ResourceOrTagArgument<T>, ResourceOrTagArgument.Info<T>.Template> {

        public void serializeToNetwork(ResourceOrTagArgument.Info<T>.Template resourceOrTagArgumentInfoTTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            friendlyByteBuf1.writeResourceLocation(resourceOrTagArgumentInfoTTemplate0.registryKey.location());
        }

        public ResourceOrTagArgument.Info<T>.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            ResourceLocation $$1 = friendlyByteBuf0.readResourceLocation();
            return new ResourceOrTagArgument.Info.Template(ResourceKey.createRegistryKey($$1));
        }

        public void serializeToJson(ResourceOrTagArgument.Info<T>.Template resourceOrTagArgumentInfoTTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("registry", resourceOrTagArgumentInfoTTemplate0.registryKey.location().toString());
        }

        public ResourceOrTagArgument.Info<T>.Template unpack(ResourceOrTagArgument<T> resourceOrTagArgumentT0) {
            return new ResourceOrTagArgument.Info.Template(resourceOrTagArgumentT0.registryKey);
        }

        public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagArgument<T>> {

            final ResourceKey<? extends Registry<T>> registryKey;

            Template(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
                this.registryKey = resourceKeyExtendsRegistryT0;
            }

            public ResourceOrTagArgument<T> instantiate(CommandBuildContext commandBuildContext0) {
                return new ResourceOrTagArgument<>(commandBuildContext0, this.registryKey);
            }

            @Override
            public ArgumentTypeInfo<ResourceOrTagArgument<T>, ?> type() {
                return Info.this;
            }
        }
    }

    static record ResourceResult<T>(Holder.Reference<T> f_243689_) implements ResourceOrTagArgument.Result<T> {

        private final Holder.Reference<T> value;

        ResourceResult(Holder.Reference<T> f_243689_) {
            this.value = f_243689_;
        }

        @Override
        public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() {
            return Either.left(this.value);
        }

        @Override
        public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> p_250007_) {
            return this.value.key().isFor(p_250007_) ? Optional.of(this) : Optional.empty();
        }

        public boolean test(Holder<T> p_249230_) {
            return p_249230_.equals(this.value);
        }

        @Override
        public String asPrintable() {
            return this.value.key().location().toString();
        }
    }

    public interface Result<T> extends Predicate<Holder<T>> {

        Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap();

        <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> var1);

        String asPrintable();
    }

    static record TagResult<T>(HolderSet.Named<T> f_244078_) implements ResourceOrTagArgument.Result<T> {

        private final HolderSet.Named<T> tag;

        TagResult(HolderSet.Named<T> f_244078_) {
            this.tag = f_244078_;
        }

        @Override
        public Either<Holder.Reference<T>, HolderSet.Named<T>> unwrap() {
            return Either.right(this.tag);
        }

        @Override
        public <E> Optional<ResourceOrTagArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> p_250945_) {
            return this.tag.key().isFor(p_250945_) ? Optional.of(this) : Optional.empty();
        }

        public boolean test(Holder<T> p_252187_) {
            return this.tag.contains(p_252187_);
        }

        @Override
        public String asPrintable() {
            return "#" + this.tag.key().location();
        }
    }
}