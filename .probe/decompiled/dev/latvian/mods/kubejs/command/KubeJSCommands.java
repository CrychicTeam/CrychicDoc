package dev.latvian.mods.kubejs.command;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.KubeJSPaths;
import dev.latvian.mods.kubejs.bindings.event.ServerEvents;
import dev.latvian.mods.kubejs.core.WithPersistentData;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.net.DisplayClientErrorsMessage;
import dev.latvian.mods.kubejs.net.DisplayServerErrorsMessage;
import dev.latvian.mods.kubejs.net.PaintMessage;
import dev.latvian.mods.kubejs.net.ReloadStartupScriptsMessage;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.kubejs.script.ConsoleLine;
import dev.latvian.mods.kubejs.script.ScriptManager;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.script.data.ExportablePackResources;
import dev.latvian.mods.kubejs.server.CustomCommandEventJS;
import dev.latvian.mods.kubejs.server.DataExport;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.JavaMembers;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Stream;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.PackResources;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.scores.Objective;
import org.apache.commons.io.FileUtils;

public class KubeJSCommands {

    private static final char UNICODE_TICK = '✔';

    private static final char UNICODE_CROSS = '✘';

    public static final DynamicCommandExceptionType NO_REGISTRY = new DynamicCommandExceptionType(id -> Component.literal("No builtin or static registry found for " + id));

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        Predicate<CommandSourceStack> spOrOP = source -> source.getServer().isSingleplayer() || source.hasPermission(2);
        LiteralCommandNode<CommandSourceStack> cmd = dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("kubejs").then(Commands.literal("help").executes(context -> help((CommandSourceStack) context.getSource())))).then(Commands.literal("custom_command").then(Commands.argument("id", StringArgumentType.word()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(ServerEvents.CUSTOM_COMMAND.findUniqueExtraIds(ScriptType.SERVER).stream().map(String::valueOf), builder)).executes(context -> customCommand((CommandSourceStack) context.getSource(), StringArgumentType.getString(context, "id")))))).then(Commands.literal("hand").executes(context -> hand(((CommandSourceStack) context.getSource()).getPlayerOrException(), InteractionHand.MAIN_HAND)))).then(Commands.literal("offhand").executes(context -> hand(((CommandSourceStack) context.getSource()).getPlayerOrException(), InteractionHand.OFF_HAND)))).then(Commands.literal("inventory").executes(context -> inventory(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(Commands.literal("hotbar").executes(context -> hotbar(((CommandSourceStack) context.getSource()).getPlayerOrException())))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("errors").then(((LiteralArgumentBuilder) Commands.literal("startup").requires(spOrOP)).executes(context -> errors((CommandSourceStack) context.getSource(), ScriptType.STARTUP)))).then(((LiteralArgumentBuilder) Commands.literal("server").requires(spOrOP)).executes(context -> errors((CommandSourceStack) context.getSource(), ScriptType.SERVER)))).then(((LiteralArgumentBuilder) Commands.literal("client").requires(source -> true)).executes(context -> errors((CommandSourceStack) context.getSource(), ScriptType.CLIENT))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("reload").then(((LiteralArgumentBuilder) Commands.literal("config").requires(spOrOP)).executes(context -> reloadConfig((CommandSourceStack) context.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("startup_scripts").requires(spOrOP)).executes(context -> reloadStartup((CommandSourceStack) context.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("server_scripts").requires(spOrOP)).executes(context -> reloadServer((CommandSourceStack) context.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("client_scripts").requires(source -> true)).executes(context -> reloadClient((CommandSourceStack) context.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("textures").requires(source -> true)).executes(context -> reloadTextures((CommandSourceStack) context.getSource())))).then(((LiteralArgumentBuilder) Commands.literal("lang").requires(source -> true)).executes(context -> reloadLang((CommandSourceStack) context.getSource()))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("export").requires(spOrOP)).executes(context -> export((CommandSourceStack) context.getSource()))).then(Commands.literal("pack_zips").executes(context -> exportPacks((CommandSourceStack) context.getSource(), true)))).then(Commands.literal("pack_folders").executes(context -> exportPacks((CommandSourceStack) context.getSource(), false))))).then(Commands.literal("list_tag").then(((RequiredArgumentBuilder) Commands.argument("registry", ResourceLocationArgument.id()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(((CommandSourceStack) ctx.getSource()).registryAccess().registries().map(entry -> entry.key().location().toString()), builder)).executes(ctx -> listTagsFor((CommandSourceStack) ctx.getSource(), registry(ctx, "registry")))).then(Commands.argument("tag", ResourceLocationArgument.id()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(allTags((CommandSourceStack) ctx.getSource(), registry(ctx, "registry")).map(TagKey::f_203868_).map(ResourceLocation::toString), builder)).executes(ctx -> tagObjects((CommandSourceStack) ctx.getSource(), TagKey.create(registry(ctx, "registry"), ResourceLocationArgument.getId(ctx, "tag")))))))).then(Commands.literal("dump_registry").then(Commands.argument("registry", ResourceLocationArgument.id()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(((CommandSourceStack) ctx.getSource()).registryAccess().registries().map(entry -> entry.key().location().toString()), builder)).executes(ctx -> dumpRegistry((CommandSourceStack) ctx.getSource(), registry(ctx, "registry")))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("stages").requires(spOrOP)).then(Commands.literal("add").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.string()).executes(context -> addStage((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), StringArgumentType.getString(context, "stage"))))))).then(Commands.literal("remove").then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("stage", StringArgumentType.string()).executes(context -> removeStage((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), StringArgumentType.getString(context, "stage"))))))).then(Commands.literal("clear").then(Commands.argument("player", EntityArgument.players()).executes(context -> clearStages((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player")))))).then(Commands.literal("list").then(Commands.argument("player", EntityArgument.players()).executes(context -> listStages((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"))))))).then(((LiteralArgumentBuilder) Commands.literal("painter").requires(spOrOP)).then(Commands.argument("player", EntityArgument.players()).then(Commands.argument("object", CompoundTagArgument.compoundTag()).executes(context -> painter((CommandSourceStack) context.getSource(), EntityArgument.getPlayers(context, "player"), CompoundTagArgument.getCompoundTag(context, "object"))))))).then(((LiteralArgumentBuilder) Commands.literal("generate_typings").requires(spOrOP)).executes(context -> generateTypings((CommandSourceStack) context.getSource())))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("packmode").requires(spOrOP)).executes(context -> packmode((CommandSourceStack) context.getSource(), ""))).then(Commands.argument("name", StringArgumentType.word()).executes(context -> packmode((CommandSourceStack) context.getSource(), StringArgumentType.getString(context, "name")))))).then(Commands.literal("dump_internals").then(((LiteralArgumentBuilder) Commands.literal("events").requires(spOrOP)).executes(context -> dumpEvents((CommandSourceStack) context.getSource()))))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("persistent_data").requires(spOrOP)).then(addPersistentDataCommands(Commands.literal("server"), ctx -> Set.of(((CommandSourceStack) ctx.getSource()).getServer())))).then(((LiteralArgumentBuilder) Commands.literal("dimension").then(addPersistentDataCommands(Commands.literal("*"), ctx -> (Collection<? extends WithPersistentData>) ((CommandSourceStack) ctx.getSource()).getServer().getAllLevels()))).then(addPersistentDataCommands(Commands.argument("dimension", DimensionArgument.dimension()), ctx -> Set.of(DimensionArgument.getDimension(ctx, "dimension")))))).then(Commands.literal("entity").then(addPersistentDataCommands(Commands.argument("entity", EntityArgument.entities()), ctx -> EntityArgument.getEntities(ctx, "entity"))))));
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("kjs").redirect(cmd));
    }

    private static int dumpEvents(CommandSourceStack source) {
        Map<String, EventGroup> groups = EventGroup.getGroups();
        Path output = KubeJSPaths.LOCAL.resolve("event_groups");
        for (Entry<String, EventGroup> entry : groups.entrySet()) {
            String groupName = (String) entry.getKey();
            EventGroup group = (EventGroup) entry.getValue();
            Path groupFolder = output.resolve(groupName);
            try {
                Files.createDirectories(groupFolder);
                FileUtils.cleanDirectory(groupFolder.toFile());
            } catch (IOException var35) {
                ConsoleJS.SERVER.error("Failed to create folder for event group " + groupName, var35);
                source.sendFailure(Component.literal("Failed to create folder for event group " + groupName));
                return 0;
            }
            for (Entry<String, EventHandler> handlerEntry : group.getHandlers().entrySet()) {
                String handlerName = (String) handlerEntry.getKey();
                EventHandler handler = (EventHandler) handlerEntry.getValue();
                Path handlerFile = groupFolder.resolve(handlerName + ".md");
                String fullName = "%s.%s".formatted(groupName, handlerName);
                Class<? extends EventJS> eventType = (Class<? extends EventJS>) handler.eventType.get();
                StringBuilder builder = new StringBuilder();
                builder.append("# ").append(fullName).append("\n\n");
                builder.append("## Basic info\n\n");
                builder.append("- Valid script types: ").append(handler.scriptTypePredicate.getValidTypes()).append("\n\n");
                builder.append("- Has result? ").append((char) (handler.getHasResult() ? '✔' : '✘')).append("\n\n");
                builder.append("- Event class: ");
                if (eventType.getPackageName().startsWith("dev.latvian.mods.kubejs")) {
                    builder.append('[').append(UtilsJS.toMappedTypeString(eventType)).append(']').append('(').append("https://github.com/KubeJS-Mods/KubeJS/tree/").append(2001).append("/common/src/main/java/").append(eventType.getPackageName().replace('.', '/')).append('/').append(eventType.getSimpleName()).append(".java").append(')');
                } else {
                    builder.append(UtilsJS.toMappedTypeString(eventType)).append(" (third-party)");
                }
                builder.append("\n\n");
                Info classInfo = (Info) eventType.getAnnotation(Info.class);
                if (classInfo != null) {
                    builder.append("```\n").append(classInfo.value()).append("```");
                    builder.append("\n\n");
                }
                ScriptManager scriptManager = (ScriptManager) ScriptType.SERVER.manager.get();
                Context cx = scriptManager.context;
                JavaMembers members = JavaMembers.lookupClass(cx, scriptManager.topLevelScope, eventType, null, false);
                boolean hasDocumentedMembers = false;
                StringBuilder documentedMembers = new StringBuilder("### Documented members:\n\n");
                builder.append("### Available fields:\n\n");
                builder.append("| Name | Type | Static? |\n");
                builder.append("| ---- | ---- | ------- |\n");
                for (JavaMembers.FieldInfo field : members.getAccessibleFields(cx, false)) {
                    if (field.field.getDeclaringClass() != Object.class) {
                        String typeName = UtilsJS.toMappedTypeString(field.field.getGenericType());
                        builder.append("| ").append(field.name).append(" | ").append(typeName).append(" | ");
                        builder.append((char) (Modifier.isStatic(field.field.getModifiers()) ? '✔' : '✘')).append(" |\n");
                        Info info = (Info) field.field.getAnnotation(Info.class);
                        if (info != null) {
                            hasDocumentedMembers = true;
                            documentedMembers.append("- `").append(typeName).append(' ').append(field.name).append("`\n");
                            documentedMembers.append("```\n");
                            String desc = info.value();
                            documentedMembers.append(desc);
                            if (!desc.endsWith("\n")) {
                                documentedMembers.append("\n");
                            }
                            documentedMembers.append("```\n\n");
                        }
                    }
                }
                builder.append("\n").append("Note: Even if no fields are listed above, some methods are still available as fields through *beans*.\n\n");
                builder.append("### Available methods:\n\n");
                builder.append("| Name | Parameters | Return type | Static? |\n");
                builder.append("| ---- | ---------- | ----------- | ------- |\n");
                for (JavaMembers.MethodInfo method : members.getAccessibleMethods(cx, false)) {
                    if (!method.hidden && method.method.getDeclaringClass() != Object.class) {
                        builder.append("| ").append(method.name).append(" | ");
                        Type[] params = method.method.getGenericParameterTypes();
                        String[] paramTypes = new String[params.length];
                        for (int i = 0; i < params.length; i++) {
                            paramTypes[i] = UtilsJS.toMappedTypeString(params[i]);
                        }
                        builder.append(String.join(", ", paramTypes)).append(" | ");
                        String returnType = UtilsJS.toMappedTypeString(method.method.getGenericReturnType());
                        builder.append(" | ").append(returnType).append(" | ");
                        builder.append((char) (Modifier.isStatic(method.method.getModifiers()) ? '✔' : '✘')).append(" |\n");
                        Info info = (Info) method.method.getAnnotation(Info.class);
                        if (info != null) {
                            hasDocumentedMembers = true;
                            documentedMembers.append("- ").append('`');
                            if (Modifier.isStatic(method.method.getModifiers())) {
                                documentedMembers.append("static ");
                            }
                            documentedMembers.append(returnType).append(' ').append(method.name).append('(');
                            Param[] namedParams = info.params();
                            String[] paramNames = new String[params.length];
                            String[] signature = new String[params.length];
                            for (int i = 0; i < params.length; i++) {
                                String name = "var" + i;
                                if (namedParams.length > i) {
                                    String name1 = namedParams[i].name();
                                    if (!Strings.isNullOrEmpty(name1)) {
                                        name = name1;
                                    }
                                }
                                paramNames[i] = name;
                                signature[i] = paramTypes[i] + " " + name;
                            }
                            documentedMembers.append(String.join(", ", signature)).append(')').append('`').append("\n");
                            if (params.length > 0) {
                                documentedMembers.append("\n  Parameters:\n");
                                for (int i = 0; i < params.length; i++) {
                                    documentedMembers.append("  - ").append(paramNames[i]).append(": ").append(paramTypes[i]).append(namedParams.length > i ? "- " + namedParams[i].value() : "").append("\n");
                                }
                                documentedMembers.append("\n");
                            }
                            documentedMembers.append("```\n");
                            String desc = info.value();
                            documentedMembers.append(desc);
                            if (!desc.endsWith("\n")) {
                                documentedMembers.append("\n");
                            }
                            documentedMembers.append("```\n\n");
                        }
                    }
                }
                builder.append("\n\n");
                if (hasDocumentedMembers) {
                    builder.append(documentedMembers).append("\n\n");
                }
                builder.append("### Example script:\n\n");
                builder.append("```js\n");
                builder.append(fullName).append('(');
                if (handler.extra != null) {
                    builder.append(handler.extra.required ? "extra_id, " : "/* extra_id (optional), */ ");
                }
                builder.append("(event) => {\n");
                builder.append("\t// This space (un)intentionally left blank\n");
                builder.append("});\n");
                builder.append("```\n\n");
                try {
                    Files.writeString(handlerFile, builder.toString());
                } catch (IOException var34) {
                    ConsoleJS.SERVER.error("Failed to write file for event handler " + fullName, var34);
                    source.sendFailure(Component.literal("Failed to write file for event handler " + fullName));
                    return 0;
                }
            }
        }
        source.sendSystemMessage(Component.literal("Successfully dumped event groups to " + output));
        return 1;
    }

    private static <T> ResourceKey<Registry<T>> registry(CommandContext<CommandSourceStack> ctx, String arg) {
        return ResourceKey.createRegistryKey(ResourceLocationArgument.getId(ctx, arg));
    }

    private static <T> Stream<TagKey<T>> allTags(CommandSourceStack source, ResourceKey<Registry<T>> registry) throws CommandSyntaxException {
        return ((Registry) source.registryAccess().registry(registry).orElseThrow(() -> NO_REGISTRY.create(registry.location()))).getTagNames();
    }

    private static Component copy(String s, ChatFormatting col, String info) {
        return copy(Component.literal(s).withStyle(col), info);
    }

    private static Component copy(Component c, String info) {
        return Component.literal("- ").withStyle(ChatFormatting.GRAY).withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, c.getString()))).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(info + " (Click to copy)")))).append(c);
    }

    private static void link(CommandSourceStack source, ChatFormatting color, String name, String url) {
        source.sendSystemMessage(Component.literal("• ").append(Component.literal(name).withStyle(color).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)))));
    }

    private static int help(CommandSourceStack source) {
        link(source, ChatFormatting.GOLD, "Wiki", "https://kubejs.com/?" + KubeJS.QUERY);
        link(source, ChatFormatting.GREEN, "Support", "https://kubejs.com/support?" + KubeJS.QUERY);
        link(source, ChatFormatting.BLUE, "Changelog", "https://kubejs.com/changelog?" + KubeJS.QUERY);
        return 1;
    }

    private static int customCommand(CommandSourceStack source, String id) {
        if (ServerEvents.CUSTOM_COMMAND.hasListeners()) {
            EventResult result = ServerEvents.CUSTOM_COMMAND.post(new CustomCommandEventJS(source.getLevel(), source.getEntity(), BlockPos.containing(source.getPosition()), id), id);
            if (result.type() == EventResult.Type.ERROR) {
                source.sendFailure(Component.literal(result.value().toString()));
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    private static int hand(ServerPlayer player, InteractionHand hand) {
        player.sendSystemMessage(Component.literal("Item in hand:"));
        ItemStack stack = player.m_21120_(hand);
        Holder<Item> holder = stack.getItemHolder();
        player.sendSystemMessage(copy(ItemStackJS.toItemString(stack), ChatFormatting.GREEN, "Item ID"));
        for (TagKey<Item> tag : holder.tags().toList()) {
            String id = "'#%s'".formatted(tag.location());
            Integer size = (Integer) BuiltInRegistries.ITEM.m_203431_(tag).map(HolderSet::m_203632_).orElse(0);
            player.sendSystemMessage(copy(id, ChatFormatting.YELLOW, "Item Tag [" + size + " items]"));
        }
        player.sendSystemMessage(copy("'@" + stack.kjs$getMod() + "'", ChatFormatting.AQUA, "Mod [" + IngredientPlatformHelper.get().mod(stack.kjs$getMod()).kjs$getStacks().size() + " items]"));
        if (stack.getItem() instanceof BlockItem blockItem) {
            player.sendSystemMessage(Component.literal("Held block:"));
            Block block = blockItem.getBlock();
            Holder.Reference<Block> blockHolder = block.builtInRegistryHolder();
            player.sendSystemMessage(copy(block.kjs$getId(), ChatFormatting.GREEN, "Block ID"));
            for (TagKey<Block> tag : blockHolder.tags().toList()) {
                String id = "'#%s'".formatted(tag.location());
                Integer size = (Integer) BuiltInRegistries.BLOCK.m_203431_(tag).map(HolderSet::m_203632_).orElse(0);
                player.sendSystemMessage(copy(id, ChatFormatting.YELLOW, "Block Tag [" + size + " items]"));
            }
        }
        if (stack.getItem() instanceof BucketItem bucket) {
            player.sendSystemMessage(Component.literal("Held fluid:"));
            Fluid fluid = bucket.arch$getFluid();
            Holder.Reference<Fluid> fluidHolder = fluid.builtInRegistryHolder();
            player.sendSystemMessage(copy(fluidHolder.key().location().toString(), ChatFormatting.GREEN, "Fluid ID"));
            for (TagKey<Fluid> tag : fluidHolder.tags().toList()) {
                String id = "'#%s'".formatted(tag.location());
                Integer size = (Integer) BuiltInRegistries.FLUID.m_203431_(tag).map(HolderSet::m_203632_).orElse(0);
                player.sendSystemMessage(copy(id, ChatFormatting.YELLOW, "Fluid Tag [" + size + " items]"));
            }
        }
        return 1;
    }

    private static int inventory(ServerPlayer player) {
        return dump(player.m_150109_().items, player, "Inventory");
    }

    private static int hotbar(ServerPlayer player) {
        return dump(player.m_150109_().items.subList(0, 9), player, "Hotbar");
    }

    private static int dump(List<ItemStack> stacks, ServerPlayer player, String name) {
        List<String> dump = stacks.stream().filter(is -> !is.isEmpty()).map(ItemStackJS::toItemString).toList();
        player.sendSystemMessage(copy(dump.toString(), ChatFormatting.WHITE, name + " Item List"));
        return 1;
    }

    private static int errors(CommandSourceStack source, ScriptType type) throws CommandSyntaxException {
        if (type == ScriptType.CLIENT) {
            ServerPlayer player = source.getPlayerOrException();
            new DisplayClientErrorsMessage().sendTo(player);
            return 1;
        } else if (type.console.errors.isEmpty() && type.console.warnings.isEmpty()) {
            source.sendSystemMessage(Component.literal("No errors or warnings found!").withStyle(ChatFormatting.GREEN));
            return 0;
        } else if (source.getServer().isSingleplayer()) {
            KubeJS.PROXY.openErrors(type);
            return 1;
        } else {
            ServerPlayer player = source.getPlayerOrException();
            ArrayList<ConsoleLine> errors = new ArrayList(type.console.errors);
            ArrayList<ConsoleLine> warnings = new ArrayList(type.console.warnings);
            player.sendSystemMessage(Component.literal("You need KubeJS on client side!").withStyle(ChatFormatting.RED), true);
            new DisplayServerErrorsMessage(type, errors, warnings).sendTo(player);
            return 1;
        }
    }

    private static int reloadConfig(CommandSourceStack source) {
        KubeJS.PROXY.reloadConfig();
        source.sendSystemMessage(Component.literal("Done!"));
        return 1;
    }

    private static int reloadStartup(CommandSourceStack source) {
        KubeJS.getStartupScriptManager().reload(null);
        source.sendSystemMessage(Component.literal("Done!"));
        new ReloadStartupScriptsMessage(source.getServer().isDedicatedServer()).sendToAll(source.getServer());
        return 1;
    }

    private static int reloadServer(CommandSourceStack source) {
        ServerScriptManager.instance.reload(source.getServer().kjs$getReloadableResources().resourceManager());
        source.sendSuccess(() -> Component.literal("Done! To reload recipes, tags, loot tables and other datapack things, run ").append(Component.literal("'/reload'").kjs$clickRunCommand("/reload").kjs$hover(Component.literal("Click to run"))), false);
        return 1;
    }

    private static int reloadClient(CommandSourceStack source) {
        KubeJS.PROXY.reloadClientInternal();
        source.sendSystemMessage(Component.literal("Done! To reload textures, models and other assets, press F3 + T"));
        return 1;
    }

    private static int reloadTextures(CommandSourceStack source) {
        KubeJS.PROXY.reloadTextures();
        return 1;
    }

    private static int reloadLang(CommandSourceStack source) {
        KubeJS.PROXY.reloadLang();
        return 1;
    }

    private static int export(CommandSourceStack source) {
        if (DataExport.export != null) {
            return 0;
        } else {
            DataExport.export = new DataExport();
            DataExport.export.source = source;
            source.sendSuccess(() -> Component.literal("Reloading server and exporting data..."), true);
            source.getServer().kjs$runCommand("reload");
            return 1;
        }
    }

    private static void afterReload(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal("Reloaded!"), true);
    }

    private static int exportPacks(CommandSourceStack source, boolean exportZip) {
        ArrayList<ExportablePackResources> packs = new ArrayList();
        for (PackResources pack : source.getServer().getResourceManager().listPacks().toList()) {
            if (pack instanceof ExportablePackResources e) {
                packs.add(e);
            }
        }
        KubeJS.PROXY.export(packs);
        int success = 0;
        for (ExportablePackResources packx : packs) {
            try {
                if (exportZip) {
                    Path path = KubeJSPaths.EXPORTED_PACKS.resolve(packx.m_5542_() + ".zip");
                    Files.deleteIfExists(path);
                    FileSystem fs = FileSystems.newFileSystem(path, Map.of("create", true));
                    try {
                        packx.export(fs.getPath("."));
                    } catch (Throwable var11) {
                        if (fs != null) {
                            try {
                                fs.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }
                        }
                        throw var11;
                    }
                    if (fs != null) {
                        fs.close();
                    }
                } else {
                    Path path = KubeJSPaths.EXPORTED_PACKS.resolve(packx.m_5542_());
                    if (Files.exists(path, new LinkOption[0])) {
                        Files.walk(path).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                    }
                    Files.createDirectories(path);
                    packx.export(path);
                }
                source.sendSuccess(() -> Component.literal("Successfully exported ").withStyle(ChatFormatting.GREEN).append(Component.literal(pack.m_5542_()).withStyle(ChatFormatting.BLUE)), false);
                success++;
            } catch (IOException var12) {
                var12.printStackTrace();
                source.sendFailure(Component.literal("Failed to export %s!".formatted(packx)).withStyle(style -> style.withColor(ChatFormatting.RED).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(var12.getMessage())))));
            }
        }
        int success1 = success;
        if (source.getServer().isSingleplayer() && !source.getServer().isPublished()) {
            source.sendSuccess(() -> Component.literal("Exported " + success1 + " packs").kjs$clickOpenFile(KubeJSPaths.EXPORTED_PACKS.toAbsolutePath().toString()), false);
        } else {
            source.sendSuccess(() -> Component.literal("Exported " + success1 + " packs"), false);
        }
        return success;
    }

    private static int outputRecipes(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("WIP!"));
        return 1;
    }

    private static int inputRecipes(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("WIP!"));
        return 1;
    }

    private static int checkRecipeConflicts(ServerPlayer player) {
        player.sendSystemMessage(Component.literal("WIP!"));
        return 1;
    }

    private static <T> int listTagsFor(CommandSourceStack source, ResourceKey<Registry<T>> registry) throws CommandSyntaxException {
        Stream<TagKey<T>> tags = allTags(source, registry);
        source.sendSystemMessage(Component.empty());
        source.sendSystemMessage(Component.literal("List of all Tags for " + registry.location() + ":"));
        source.sendSystemMessage(Component.empty());
        long size = tags.map(TagKey::f_203868_).map(tag -> Component.literal("- %s".formatted(tag)).withStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs list_tag %s %s".formatted(registry.location(), tag))).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("[Show all entries for %s]".formatted(tag)))))).mapToLong(msg -> {
            source.sendSystemMessage(msg);
            return 1L;
        }).sum();
        source.sendSystemMessage(Component.empty());
        source.sendSystemMessage(Component.literal("Total: %d tags".formatted(size)));
        source.sendSystemMessage(Component.literal("(Click on any of the above tags to list their contents!)"));
        source.sendSystemMessage(Component.empty());
        return 1;
    }

    private static <T> int tagObjects(CommandSourceStack source, TagKey<T> key) throws CommandSyntaxException {
        Registry<T> registry = (Registry<T>) source.registryAccess().registry(key.registry()).orElseThrow(() -> NO_REGISTRY.create(key.registry().location()));
        Optional<HolderSet.Named<T>> tag = registry.getTag(key);
        if (tag.isEmpty()) {
            source.sendFailure(Component.literal("Tag not found or empty!"));
            return 0;
        } else {
            source.sendSystemMessage(Component.empty());
            source.sendSystemMessage(Component.literal("Contents of #" + key.location() + " [" + key.registry().location() + "]:"));
            source.sendSystemMessage(Component.empty());
            HolderSet.Named<T> items = (HolderSet.Named<T>) tag.get();
            for (Holder<T> holder : items) {
                String id = (String) holder.unwrap().map(o -> o.location().toString(), o -> o + " (unknown ID)");
                source.sendSystemMessage(Component.literal("- " + id));
            }
            source.sendSystemMessage(Component.empty());
            source.sendSystemMessage(Component.literal("Total: " + items.m_203632_() + " elements"));
            source.sendSystemMessage(Component.empty());
            return 1;
        }
    }

    private static <T> int dumpRegistry(CommandSourceStack source, ResourceKey<Registry<T>> registry) throws CommandSyntaxException {
        Stream<Holder.Reference<T>> ids = ((Registry) source.registryAccess().registry(registry).orElseThrow(() -> NO_REGISTRY.create(registry.location()))).holders();
        source.sendSystemMessage(Component.empty());
        source.sendSystemMessage(Component.literal("List of all entries for registry " + registry.location() + ":"));
        source.sendSystemMessage(Component.empty());
        long size = ids.map(holder -> {
            ResourceLocation id = holder.key().location();
            return Component.literal("- %s".formatted(id)).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("%s [%s]".formatted(holder.value(), holder.value().getClass().getName())))));
        }).mapToLong(msg -> {
            source.sendSystemMessage(msg);
            return 1L;
        }).sum();
        source.sendSystemMessage(Component.empty());
        source.sendSystemMessage(Component.literal("Total: %d entries".formatted(size)));
        source.sendSystemMessage(Component.empty());
        return 1;
    }

    private static int addStage(CommandSourceStack source, Collection<ServerPlayer> players, String stage) {
        for (ServerPlayer p : players) {
            if (p.kjs$getStages().add(stage)) {
                source.sendSuccess(() -> Component.literal("Added '" + stage + "' stage for " + p.m_6302_()), true);
            }
        }
        return 1;
    }

    private static int removeStage(CommandSourceStack source, Collection<ServerPlayer> players, String stage) {
        for (ServerPlayer p : players) {
            if (p.kjs$getStages().remove(stage)) {
                source.sendSuccess(() -> Component.literal("Removed '" + stage + "' stage for " + p.m_6302_()), true);
            }
        }
        return 1;
    }

    private static int clearStages(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer p : players) {
            if (p.kjs$getStages().clear()) {
                source.sendSuccess(() -> Component.literal("Cleared stages for " + p.m_6302_()), true);
            }
        }
        return 1;
    }

    private static int listStages(CommandSourceStack source, Collection<ServerPlayer> players) {
        for (ServerPlayer p : players) {
            source.sendSystemMessage(Component.literal(p.m_6302_() + " stages:"));
            p.kjs$getStages().getAll().stream().sorted().forEach(s -> source.sendSystemMessage(Component.literal("- " + s)));
        }
        return 1;
    }

    private static int painter(CommandSourceStack source, Collection<ServerPlayer> players, CompoundTag object) {
        new PaintMessage(object).sendTo(players);
        return 1;
    }

    private static int generateTypings(CommandSourceStack source) {
        if (!source.getServer().isSingleplayer()) {
            source.sendFailure(Component.literal("You can only run this command in singleplayer!"));
            return 0;
        } else {
            KubeJS.PROXY.generateTypings(source);
            return 1;
        }
    }

    private static int packmode(CommandSourceStack source, String packmode) {
        if (packmode.isEmpty()) {
            source.sendSuccess(() -> Component.literal("Current packmode: " + CommonProperties.get().packMode), false);
        } else {
            CommonProperties.get().setPackMode(packmode);
            source.sendSuccess(() -> Component.literal("Set packmode to: " + packmode), true);
        }
        return 1;
    }

    private static ArgumentBuilder<CommandSourceStack, ?> addPersistentDataCommands(ArgumentBuilder<CommandSourceStack, ?> cmd, KubeJSCommands.PersistentDataFactory factory) {
        cmd.then(((LiteralArgumentBuilder) Commands.literal("get").then(Commands.literal("*").executes(ctx -> {
            Collection<? extends WithPersistentData> objects = factory.getAll(ctx);
            for (WithPersistentData o : objects) {
                Component dataStr = NbtUtils.toPrettyComponent(o.kjs$getPersistentData());
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.literal("").append(Component.literal("").withStyle(ChatFormatting.YELLOW).append(o.kjs$getDisplayName())).append(": ").append(dataStr), false);
            }
            return objects.size();
        }))).then(Commands.argument("key", StringArgumentType.string()).executes(ctx -> {
            Collection<? extends WithPersistentData> objects = factory.getAll(ctx);
            String key = StringArgumentType.getString(ctx, "key");
            for (WithPersistentData o : objects) {
                Tag data = (Tag) (key.equals("*") ? o.kjs$getPersistentData() : o.kjs$getPersistentData().get(key));
                Component dataStr = (Component) (data == null ? Component.literal("null").withStyle(ChatFormatting.RED) : NbtUtils.toPrettyComponent(data));
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.literal("").append(Component.literal("").withStyle(ChatFormatting.YELLOW).append(o.kjs$getDisplayName())).append(": ").append(dataStr), false);
            }
            return objects.size();
        })));
        cmd.then(Commands.literal("merge").then(Commands.argument("nbt", CompoundTagArgument.compoundTag()).executes(ctx -> {
            Collection<? extends WithPersistentData> objects = factory.getAll(ctx);
            CompoundTag tag = CompoundTagArgument.getCompoundTag(ctx, "nbt");
            for (WithPersistentData o : objects) {
                o.kjs$getPersistentData().merge(tag);
                ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> Component.literal("").append(Component.literal("").withStyle(ChatFormatting.YELLOW).append(o.kjs$getDisplayName())).append(" updated"), false);
            }
            return objects.size();
        })));
        cmd.then(((LiteralArgumentBuilder) Commands.literal("remove").then(Commands.literal("*").executes(ctx -> {
            Collection<? extends WithPersistentData> objects = factory.getAll(ctx);
            for (WithPersistentData o : objects) {
                o.kjs$getPersistentData().getAllKeys().removeIf(UtilsJS.ALWAYS_TRUE);
            }
            return objects.size();
        }))).then(Commands.argument("key", StringArgumentType.string()).executes(ctx -> {
            Collection<? extends WithPersistentData> objects = factory.getAll(ctx);
            String key = StringArgumentType.getString(ctx, "key");
            for (WithPersistentData o : objects) {
                o.kjs$getPersistentData().remove(key);
            }
            return objects.size();
        })));
        cmd.then(((LiteralArgumentBuilder) Commands.literal("scoreboard").then(Commands.literal("import").then(Commands.argument("key", StringArgumentType.string()).then(Commands.argument("target", ScoreHolderArgument.scoreHolder()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).executes(ctx -> {
            ServerScoreboard scoreboard = ((CommandSourceStack) ctx.getSource()).getServer().getScoreboard();
            Collection<? extends WithPersistentData> objects = factory.getAll(ctx);
            String key = StringArgumentType.getString(ctx, "key");
            String target = ScoreHolderArgument.getName(ctx, "target");
            Objective objective = ObjectiveArgument.getObjective(ctx, "objective");
            int score = scoreboard.m_83461_(target, objective) ? scoreboard.m_83471_(target, objective).getScore() : 0;
            for (WithPersistentData o : objects) {
                o.kjs$getPersistentData().putInt(key, score);
            }
            return objects.size();
        })))))).then(Commands.literal("export").then(Commands.argument("key", StringArgumentType.string()).then(Commands.argument("targets", ScoreHolderArgument.scoreHolders()).suggests(ScoreHolderArgument.SUGGEST_SCORE_HOLDERS).then(Commands.argument("objective", ObjectiveArgument.objective()).executes(ctx -> {
            ServerScoreboard scoreboard = ((CommandSourceStack) ctx.getSource()).getServer().getScoreboard();
            WithPersistentData object = factory.getOne(ctx);
            String key = StringArgumentType.getString(ctx, "key");
            Collection<String> targets = ScoreHolderArgument.getNames(ctx, "targets");
            Objective objective = ObjectiveArgument.getObjective(ctx, "objective");
            int score = object.kjs$getPersistentData().getInt(key);
            for (String target : targets) {
                scoreboard.m_83471_(target, objective).setScore(score);
            }
            return 1;
        }))))));
        return cmd;
    }

    @FunctionalInterface
    private interface PersistentDataFactory {

        SimpleCommandExceptionType EMPTY_LIST = new SimpleCommandExceptionType(Component.literal("Expected at least one target"));

        Collection<? extends WithPersistentData> apply(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;

        default Collection<? extends WithPersistentData> getAll(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
            Collection<? extends WithPersistentData> list = this.apply(ctx);
            if (list.isEmpty()) {
                throw EMPTY_LIST.create();
            } else {
                return list;
            }
        }

        default WithPersistentData getOne(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
            Collection<? extends WithPersistentData> list = this.apply(ctx);
            if (list.isEmpty()) {
                throw EMPTY_LIST.create();
            } else {
                return (WithPersistentData) list.iterator().next();
            }
        }
    }
}