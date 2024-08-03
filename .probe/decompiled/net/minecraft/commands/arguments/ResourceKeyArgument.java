package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class ResourceKeyArgument<T> implements ArgumentType<ResourceKey<T>> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");

    private static final DynamicCommandExceptionType ERROR_INVALID_FEATURE = new DynamicCommandExceptionType(p_212392_ -> Component.translatable("commands.place.feature.invalid", p_212392_));

    private static final DynamicCommandExceptionType ERROR_INVALID_STRUCTURE = new DynamicCommandExceptionType(p_212385_ -> Component.translatable("commands.place.structure.invalid", p_212385_));

    private static final DynamicCommandExceptionType ERROR_INVALID_TEMPLATE_POOL = new DynamicCommandExceptionType(p_233264_ -> Component.translatable("commands.place.jigsaw.invalid", p_233264_));

    final ResourceKey<? extends Registry<T>> registryKey;

    public ResourceKeyArgument(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
        this.registryKey = resourceKeyExtendsRegistryT0;
    }

    public static <T> ResourceKeyArgument<T> key(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
        return new ResourceKeyArgument<>(resourceKeyExtendsRegistryT0);
    }

    private static <T> ResourceKey<T> getRegistryKey(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, ResourceKey<Registry<T>> resourceKeyRegistryT2, DynamicCommandExceptionType dynamicCommandExceptionType3) throws CommandSyntaxException {
        ResourceKey<?> $$4 = (ResourceKey<?>) commandContextCommandSourceStack0.getArgument(string1, ResourceKey.class);
        Optional<ResourceKey<T>> $$5 = $$4.cast(resourceKeyRegistryT2);
        return (ResourceKey<T>) $$5.orElseThrow(() -> dynamicCommandExceptionType3.create($$4));
    }

    private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1) {
        return ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).getServer().registryAccess().m_175515_(resourceKeyExtendsRegistryT1);
    }

    private static <T> Holder.Reference<T> resolveKey(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, ResourceKey<Registry<T>> resourceKeyRegistryT2, DynamicCommandExceptionType dynamicCommandExceptionType3) throws CommandSyntaxException {
        ResourceKey<T> $$4 = getRegistryKey(commandContextCommandSourceStack0, string1, resourceKeyRegistryT2, dynamicCommandExceptionType3);
        return (Holder.Reference<T>) getRegistry(commandContextCommandSourceStack0, resourceKeyRegistryT2).getHolder($$4).orElseThrow(() -> dynamicCommandExceptionType3.create($$4.location()));
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return resolveKey(commandContextCommandSourceStack0, string1, Registries.CONFIGURED_FEATURE, ERROR_INVALID_FEATURE);
    }

    public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return resolveKey(commandContextCommandSourceStack0, string1, Registries.STRUCTURE, ERROR_INVALID_STRUCTURE);
    }

    public static Holder.Reference<StructureTemplatePool> getStructureTemplatePool(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return resolveKey(commandContextCommandSourceStack0, string1, Registries.TEMPLATE_POOL, ERROR_INVALID_TEMPLATE_POOL);
    }

    public ResourceKey<T> parse(StringReader stringReader0) throws CommandSyntaxException {
        ResourceLocation $$1 = ResourceLocation.read(stringReader0);
        return ResourceKey.create(this.registryKey, $$1);
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return commandContextS0.getSource() instanceof SharedSuggestionProvider $$2 ? $$2.suggestRegistryElements(this.registryKey, SharedSuggestionProvider.ElementSuggestionType.ELEMENTS, suggestionsBuilder1, commandContextS0) : suggestionsBuilder1.buildFuture();
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info<T> implements ArgumentTypeInfo<ResourceKeyArgument<T>, ResourceKeyArgument.Info<T>.Template> {

        public void serializeToNetwork(ResourceKeyArgument.Info<T>.Template resourceKeyArgumentInfoTTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            friendlyByteBuf1.writeResourceLocation(resourceKeyArgumentInfoTTemplate0.registryKey.location());
        }

        public ResourceKeyArgument.Info<T>.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            ResourceLocation $$1 = friendlyByteBuf0.readResourceLocation();
            return new ResourceKeyArgument.Info.Template(ResourceKey.createRegistryKey($$1));
        }

        public void serializeToJson(ResourceKeyArgument.Info<T>.Template resourceKeyArgumentInfoTTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("registry", resourceKeyArgumentInfoTTemplate0.registryKey.location().toString());
        }

        public ResourceKeyArgument.Info<T>.Template unpack(ResourceKeyArgument<T> resourceKeyArgumentT0) {
            return new ResourceKeyArgument.Info.Template(resourceKeyArgumentT0.registryKey);
        }

        public final class Template implements ArgumentTypeInfo.Template<ResourceKeyArgument<T>> {

            final ResourceKey<? extends Registry<T>> registryKey;

            Template(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
                this.registryKey = resourceKeyExtendsRegistryT0;
            }

            public ResourceKeyArgument<T> instantiate(CommandBuildContext commandBuildContext0) {
                return new ResourceKeyArgument<>(this.registryKey);
            }

            @Override
            public ArgumentTypeInfo<ResourceKeyArgument<T>, ?> type() {
                return Info.this;
            }
        }
    }
}