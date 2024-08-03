package net.minecraft.commands.arguments;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ResourceArgument<T> implements ArgumentType<Holder.Reference<T>> {

    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");

    private static final DynamicCommandExceptionType ERROR_NOT_SUMMONABLE_ENTITY = new DynamicCommandExceptionType(p_248875_ -> Component.translatable("entity.not_summonable", p_248875_));

    public static final Dynamic2CommandExceptionType ERROR_UNKNOWN_RESOURCE = new Dynamic2CommandExceptionType((p_248525_, p_251552_) -> Component.translatable("argument.resource.not_found", p_248525_, p_251552_));

    public static final Dynamic3CommandExceptionType ERROR_INVALID_RESOURCE_TYPE = new Dynamic3CommandExceptionType((p_250883_, p_249983_, p_249882_) -> Component.translatable("argument.resource.invalid_type", p_250883_, p_249983_, p_249882_));

    final ResourceKey<? extends Registry<T>> registryKey;

    private final HolderLookup<T> registryLookup;

    public ResourceArgument(CommandBuildContext commandBuildContext0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1) {
        this.registryKey = resourceKeyExtendsRegistryT1;
        this.registryLookup = commandBuildContext0.holderLookup(resourceKeyExtendsRegistryT1);
    }

    public static <T> ResourceArgument<T> resource(CommandBuildContext commandBuildContext0, ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT1) {
        return new ResourceArgument<>(commandBuildContext0, resourceKeyExtendsRegistryT1);
    }

    public static <T> Holder.Reference<T> getResource(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, ResourceKey<Registry<T>> resourceKeyRegistryT2) throws CommandSyntaxException {
        Holder.Reference<T> $$3 = (Holder.Reference<T>) commandContextCommandSourceStack0.getArgument(string1, Holder.Reference.class);
        ResourceKey<?> $$4 = $$3.key();
        if ($$4.isFor(resourceKeyRegistryT2)) {
            return $$3;
        } else {
            throw ERROR_INVALID_RESOURCE_TYPE.create($$4.location(), $$4.registry(), resourceKeyRegistryT2.location());
        }
    }

    public static Holder.Reference<Attribute> getAttribute(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getResource(commandContextCommandSourceStack0, string1, Registries.ATTRIBUTE);
    }

    public static Holder.Reference<ConfiguredFeature<?, ?>> getConfiguredFeature(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getResource(commandContextCommandSourceStack0, string1, Registries.CONFIGURED_FEATURE);
    }

    public static Holder.Reference<Structure> getStructure(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getResource(commandContextCommandSourceStack0, string1, Registries.STRUCTURE);
    }

    public static Holder.Reference<EntityType<?>> getEntityType(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getResource(commandContextCommandSourceStack0, string1, Registries.ENTITY_TYPE);
    }

    public static Holder.Reference<EntityType<?>> getSummonableEntityType(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        Holder.Reference<EntityType<?>> $$2 = getResource(commandContextCommandSourceStack0, string1, Registries.ENTITY_TYPE);
        if (!$$2.value().canSummon()) {
            throw ERROR_NOT_SUMMONABLE_ENTITY.create($$2.key().location().toString());
        } else {
            return $$2;
        }
    }

    public static Holder.Reference<MobEffect> getMobEffect(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getResource(commandContextCommandSourceStack0, string1, Registries.MOB_EFFECT);
    }

    public static Holder.Reference<Enchantment> getEnchantment(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        return getResource(commandContextCommandSourceStack0, string1, Registries.ENCHANTMENT);
    }

    public Holder.Reference<T> parse(StringReader stringReader0) throws CommandSyntaxException {
        ResourceLocation $$1 = ResourceLocation.read(stringReader0);
        ResourceKey<T> $$2 = ResourceKey.create(this.registryKey, $$1);
        return (Holder.Reference<T>) this.registryLookup.m_254902_($$2).orElseThrow(() -> ERROR_UNKNOWN_RESOURCE.create($$1, this.registryKey.location()));
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContextS0, SuggestionsBuilder suggestionsBuilder1) {
        return SharedSuggestionProvider.suggestResource(this.registryLookup.listElementIds().map(ResourceKey::m_135782_), suggestionsBuilder1);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Info<T> implements ArgumentTypeInfo<ResourceArgument<T>, ResourceArgument.Info<T>.Template> {

        public void serializeToNetwork(ResourceArgument.Info<T>.Template resourceArgumentInfoTTemplate0, FriendlyByteBuf friendlyByteBuf1) {
            friendlyByteBuf1.writeResourceLocation(resourceArgumentInfoTTemplate0.registryKey.location());
        }

        public ResourceArgument.Info<T>.Template deserializeFromNetwork(FriendlyByteBuf friendlyByteBuf0) {
            ResourceLocation $$1 = friendlyByteBuf0.readResourceLocation();
            return new ResourceArgument.Info.Template(ResourceKey.createRegistryKey($$1));
        }

        public void serializeToJson(ResourceArgument.Info<T>.Template resourceArgumentInfoTTemplate0, JsonObject jsonObject1) {
            jsonObject1.addProperty("registry", resourceArgumentInfoTTemplate0.registryKey.location().toString());
        }

        public ResourceArgument.Info<T>.Template unpack(ResourceArgument<T> resourceArgumentT0) {
            return new ResourceArgument.Info.Template(resourceArgumentT0.registryKey);
        }

        public final class Template implements ArgumentTypeInfo.Template<ResourceArgument<T>> {

            final ResourceKey<? extends Registry<T>> registryKey;

            Template(ResourceKey<? extends Registry<T>> resourceKeyExtendsRegistryT0) {
                this.registryKey = resourceKeyExtendsRegistryT0;
            }

            public ResourceArgument<T> instantiate(CommandBuildContext commandBuildContext0) {
                return new ResourceArgument<>(commandBuildContext0, this.registryKey);
            }

            @Override
            public ArgumentTypeInfo<ResourceArgument<T>, ?> type() {
                return Info.this;
            }
        }
    }
}