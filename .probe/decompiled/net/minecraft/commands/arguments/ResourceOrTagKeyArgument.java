package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
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
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

public class ResourceOrTagKeyArgument<T> implements ArgumentType<ResourceOrTagKeyArgument.Result<T>> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012", "#skeletons", "#minecraft:skeletons");

    final ResourceKey<? extends Registry<T>> registryKey;

    public ResourceOrTagKeyArgument(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
        this.registryKey = resourceKeyExtendsRegistryT0;
    }

    public static <T> ResourceOrTagKeyArgument<T> resourceOrTagKey(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
        return new ResourceOrTagKeyArgument<>(resourceKeyExtendsRegistryT0);
    }

    public static <T> ResourceOrTagKeyArgument.Result<T> getResourceOrTagKey(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, ResourceKey<Registry<T>> resourceKeyRegistryT2, DynamicCommandExceptionType dynamicCommandExceptionType3) throws CommandSyntaxException {
        ResourceOrTagKeyArgument.Result<?> $$4 = (ResourceOrTagKeyArgument.Result<?>) commandContextCommandSourceStack0.getArgument(string1, ResourceOrTagKeyArgument.Result.class);
        Optional<ResourceOrTagKeyArgument.Result<T>> $$5 = $$4.cast(resourceKeyRegistryT2);
        return (ResourceOrTagKeyArgument.Result<T>) $$5.orElseThrow(() -> dynamicCommandExceptionType3.create($$4));
    }

    public ResourceOrTagKeyArgument.Result<T> parse(StringReader stringReader0) throws CommandSyntaxException {
        if (stringReader0.canRead() && stringReader0.peek() == '#') {
            int $$1 = stringReader0.getCursor();
            try {
                stringReader0.skip();
                ResourceLocation $$2 = ResourceLocation.read(stringReader0);
                return new ResourceOrTagKeyArgument.TagResult<>(TagKey.create(this.registryKey, $$2));
            } catch (CommandSyntaxException var4) {
                stringReader0.setCursor($$1);
                throw var4;
            }
        } else {
            ResourceLocation $$4 = ResourceLocation.read(stringReader0);
            return new ResourceOrTagKeyArgument.ResourceResult<>(ResourceKey.create(this.registryKey, $$4));
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return commandContextS0.getSource() instanceof SharedSuggestionProvider $$2 ? $$2.suggestRegistryElements(this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ALL, suggestionsBuilder1, commandContextS0) : suggestionsBuilder1.buildFuture();
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info<T> implements ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ResourceOrTagKeyArgument.Info<T>.Template> {

        public void serializeToNetwork(ResourceOrTagKeyArgument.Info<T>.Template resourceOrTagKeyArgumentInfoTTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            friendlyByteBuf1.writeResourceLocation(resourceOrTagKeyArgumentInfoTTemplate0.registryKey.location());
        }

        public ResourceOrTagKeyArgument.Info<T>.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            ResourceLocation $$1 = friendlyByteBuf0.readResourceLocation();
            return new ResourceOrTagKeyArgument.Info.Template(ResourceKey.createRegistryKey($$1));
        }

        public void serializeToJson(ResourceOrTagKeyArgument.Info<T>.Template resourceOrTagKeyArgumentInfoTTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("registry", resourceOrTagKeyArgumentInfoTTemplate0.registryKey.location().toString());
        }

        public ResourceOrTagKeyArgument.Info<T>.Template unpack(ResourceOrTagKeyArgument<T> resourceOrTagKeyArgumentT0) {
            return new ResourceOrTagKeyArgument.Info.Template(resourceOrTagKeyArgumentT0.registryKey);
        }

        public final class Template implements ArgumentTypeInfo.Template<ResourceOrTagKeyArgument<T>> {

            final ResourceKey<? extends Registry<T>> registryKey;

            Template(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
                this.registryKey = resourceKeyExtendsRegistryT0;
            }

            public ResourceOrTagKeyArgument<T> instantiate(CommandBuildContext commandBuildContext0) {
                return new ResourceOrTagKeyArgument<>(this.registryKey);
            }

            @Override
            public ArgumentTypeInfo<ResourceOrTagKeyArgument<T>, ?> type() {
                return Info.this;
            }
        }
    }

    static record ResourceResult<T>(ResourceKey<T> f_243909_) implements ResourceOrTagKeyArgument.Result<T> {

        private final ResourceKey<T> key;

        ResourceResult(ResourceKey<T> f_243909_) {
            this.key = f_243909_;
        }

        @Override
        public Either<ResourceKey<T>, TagKey<T>> unwrap() {
            return Either.left(this.key);
        }

        @Override
        public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> p_251369_) {
            return this.key.cast(p_251369_).map(ResourceOrTagKeyArgument.ResourceResult::new);
        }

        public boolean test(Holder<T> p_250257_) {
            return p_250257_.is(this.key);
        }

        @Override
        public String asPrintable() {
            return this.key.location().toString();
        }
    }

    public interface Result<T> extends Predicate<Holder<T>> {

        Either<ResourceKey<T>, TagKey<T>> unwrap();

        <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> var1);

        String asPrintable();
    }

    static record TagResult<T>(TagKey<T> f_244059_) implements ResourceOrTagKeyArgument.Result<T> {

        private final TagKey<T> key;

        TagResult(TagKey<T> f_244059_) {
            this.key = f_244059_;
        }

        @Override
        public Either<ResourceKey<T>, TagKey<T>> unwrap() {
            return Either.right(this.key);
        }

        @Override
        public <E> Optional<ResourceOrTagKeyArgument.Result<E>> cast(ResourceKey<? extends Registry<E>> p_251833_) {
            return this.key.cast(p_251833_).map(ResourceOrTagKeyArgument.TagResult::new);
        }

        public boolean test(Holder<T> p_252238_) {
            return p_252238_.is(this.key);
        }

        @Override
        public String asPrintable() {
            return "#" + this.key.location();
        }
    }
}