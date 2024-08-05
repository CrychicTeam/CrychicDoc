package net.minecraftforge.server.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.datafixers.util.Pair;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;

class TagsCommand {

    private static final long PAGE_SIZE = 8L;

    private static final ResourceKey<Registry<Registry<?>>> ROOT_REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation("root"));

    private static final DynamicCommandExceptionType UNKNOWN_REGISTRY = new DynamicCommandExceptionType(key -> Component.translatable("commands.forge.tags.error.unknown_registry", key));

    private static final Dynamic2CommandExceptionType UNKNOWN_TAG = new Dynamic2CommandExceptionType((tag, registry) -> Component.translatable("commands.forge.tags.error.unknown_tag", tag, registry));

    private static final Dynamic2CommandExceptionType UNKNOWN_ELEMENT = new Dynamic2CommandExceptionType((tag, registry) -> Component.translatable("commands.forge.tags.error.unknown_element", tag, registry));

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return ((LiteralArgumentBuilder) Commands.literal("tags").requires(cs -> cs.hasPermission(2))).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("registry", ResourceKeyArgument.key(ROOT_REGISTRY_KEY)).suggests(TagsCommand::suggestRegistries).then(((LiteralArgumentBuilder) Commands.literal("list").executes(ctx -> listTags(ctx, 1))).then(Commands.argument("page", IntegerArgumentType.integer(1)).executes(ctx -> listTags(ctx, IntegerArgumentType.getInteger(ctx, "page")))))).then(Commands.literal("get").then(((RequiredArgumentBuilder) Commands.argument("tag", ResourceLocationArgument.id()).suggests(suggestFromRegistry(r -> r.getTagNames().map(TagKey::f_203868_)::iterator)).executes(ctx -> listTagElements(ctx, 1))).then(Commands.argument("page", IntegerArgumentType.integer(1)).executes(ctx -> listTagElements(ctx, IntegerArgumentType.getInteger(ctx, "page"))))))).then(Commands.literal("query").then(((RequiredArgumentBuilder) Commands.argument("element", ResourceLocationArgument.id()).suggests(suggestFromRegistry(Registry::m_6566_)).executes(ctx -> queryElementTags(ctx, 1))).then(Commands.argument("page", IntegerArgumentType.integer(1)).executes(ctx -> queryElementTags(ctx, IntegerArgumentType.getInteger(ctx, "page")))))));
    }

    private static int listTags(CommandContext<CommandSourceStack> ctx, int page) throws CommandSyntaxException {
        ResourceKey<? extends Registry<?>> registryKey = (ResourceKey<? extends Registry<?>>) getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY).orElseThrow();
        Registry<?> registry = (Registry<?>) ((CommandSourceStack) ctx.getSource()).getServer().registryAccess().m_6632_(registryKey).orElseThrow(() -> UNKNOWN_REGISTRY.create(registryKey.location()));
        long tagCount = registry.getTags().count();
        ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> createMessage(Component.translatable("commands.forge.tags.registry_key", Component.literal(registryKey.location().toString()).withStyle(ChatFormatting.GOLD)), "commands.forge.tags.tag_count", "commands.forge.tags.copy_tag_names", tagCount, (long) page, ChatFormatting.DARK_GREEN, () -> registry.getTags().map(Pair::getSecond).map(s -> (String) s.unwrap().map(k -> k.location().toString(), Object::toString))), false);
        return (int) tagCount;
    }

    private static int listTagElements(CommandContext<CommandSourceStack> ctx, int page) throws CommandSyntaxException {
        ResourceKey<? extends Registry<?>> registryKey = (ResourceKey<? extends Registry<?>>) getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY).orElseThrow();
        Registry<?> registry = (Registry<?>) ((CommandSourceStack) ctx.getSource()).getServer().registryAccess().m_6632_(registryKey).orElseThrow(() -> UNKNOWN_REGISTRY.create(registryKey.location()));
        ResourceLocation tagLocation = ResourceLocationArgument.getId(ctx, "tag");
        TagKey<?> tagKey = TagKey.create(cast(registryKey), tagLocation);
        HolderSet.Named<?> tag = (HolderSet.Named<?>) registry.getTag(cast(tagKey)).orElseThrow(() -> UNKNOWN_TAG.create(tagKey.location(), registryKey.location()));
        ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> createMessage(Component.translatable("commands.forge.tags.tag_key", Component.literal(tagKey.registry().location().toString()).withStyle(ChatFormatting.GOLD), Component.literal(tagKey.location().toString()).withStyle(ChatFormatting.DARK_GREEN)), "commands.forge.tags.element_count", "commands.forge.tags.copy_element_names", (long) tag.m_203632_(), (long) page, ChatFormatting.YELLOW, () -> tag.m_203614_().map(s -> (String) s.unwrap().map(k -> k.location().toString(), Object::toString))), false);
        return tag.m_203632_();
    }

    private static int queryElementTags(CommandContext<CommandSourceStack> ctx, int page) throws CommandSyntaxException {
        ResourceKey<? extends Registry<?>> registryKey = (ResourceKey<? extends Registry<?>>) getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY).orElseThrow();
        Registry<?> registry = (Registry<?>) ((CommandSourceStack) ctx.getSource()).getServer().registryAccess().m_6632_(registryKey).orElseThrow(() -> UNKNOWN_REGISTRY.create(registryKey.location()));
        ResourceLocation elementLocation = ResourceLocationArgument.getId(ctx, "element");
        ResourceKey<?> elementKey = ResourceKey.create(cast(registryKey), elementLocation);
        Holder<?> elementHolder = (Holder<?>) registry.getHolder(cast(elementKey)).orElseThrow(() -> UNKNOWN_ELEMENT.create(elementLocation, registryKey.location()));
        long containingTagsCount = elementHolder.tags().count();
        ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> createMessage(Component.translatable("commands.forge.tags.element", Component.literal(registryKey.location().toString()).withStyle(ChatFormatting.GOLD), Component.literal(elementLocation.toString()).withStyle(ChatFormatting.YELLOW)), "commands.forge.tags.containing_tag_count", "commands.forge.tags.copy_tag_names", containingTagsCount, (long) page, ChatFormatting.DARK_GREEN, () -> elementHolder.tags().map(k -> k.location().toString())), false);
        return (int) containingTagsCount;
    }

    private static MutableComponent createMessage(MutableComponent header, String containsText, String copyHoverText, long count, long currentPage, ChatFormatting elementColor, Supplier<Stream<String>> names) {
        String allElementNames = (String) ((Stream) names.get()).sorted().collect(Collectors.joining("\n"));
        long totalPages = (count - 1L) / 8L + 1L;
        long actualPage = (long) Mth.clamp((float) currentPage, 1.0F, (float) totalPages);
        MutableComponent containsComponent = Component.translatable(containsText, count);
        if (count > 0L) {
            containsComponent = ComponentUtils.wrapInSquareBrackets(containsComponent.withStyle(s -> s.withColor(ChatFormatting.GREEN).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, allElementNames)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable(copyHoverText)))));
            containsComponent = Component.translatable("commands.forge.tags.page_info", containsComponent, actualPage, totalPages);
        }
        MutableComponent tagElements = Component.literal("").append(containsComponent);
        ((Stream) names.get()).sorted().skip(8L * (actualPage - 1L)).limit(8L).map(Component::m_237113_).map(t -> t.withStyle(elementColor)).map(t -> Component.translatable("\n - ").append(t)).forEach(tagElements::m_7220_);
        return header.append("\n").append(tagElements);
    }

    private static CompletableFuture<Suggestions> suggestRegistries(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        ((CommandSourceStack) ctx.getSource()).registryAccess().registries().map(RegistryAccess.RegistryEntry::f_206233_).map(ResourceKey::m_135782_).map(ResourceLocation::toString).forEach(builder::suggest);
        return builder.buildFuture();
    }

    private static SuggestionProvider<CommandSourceStack> suggestFromRegistry(Function<Registry<?>, Iterable<ResourceLocation>> namesFunction) {
        return (ctx, builder) -> (CompletableFuture) getResourceKey(ctx, "registry", ROOT_REGISTRY_KEY).flatMap(key -> ((CommandSourceStack) ctx.getSource()).registryAccess().registry(key).map(registry -> {
            SharedSuggestionProvider.suggestResource((Iterable<ResourceLocation>) namesFunction.apply(registry), builder);
            return builder.buildFuture();
        })).orElseGet(builder::buildFuture);
    }

    private static <T> Optional<ResourceKey<T>> getResourceKey(CommandContext<CommandSourceStack> ctx, String name, ResourceKey<Registry<T>> registryKey) {
        ResourceKey<?> key = (ResourceKey<?>) ctx.getArgument(name, ResourceKey.class);
        return key.cast(registryKey);
    }

    private static <O> O cast(Object input) {
        return (O) input;
    }
}