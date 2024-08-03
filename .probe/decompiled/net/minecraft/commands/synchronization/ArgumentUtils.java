package net.minecraft.commands.synchronization;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import com.mojang.logging.LogUtils;
import java.util.Collection;
import java.util.Set;
import net.minecraft.core.registries.BuiltInRegistries;
import org.slf4j.Logger;

public class ArgumentUtils {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final byte NUMBER_FLAG_MIN = 1;

    private static final byte NUMBER_FLAG_MAX = 2;

    public static int createNumberFlags(boolean boolean0, boolean boolean1) {
        int $$2 = 0;
        if (boolean0) {
            $$2 |= 1;
        }
        if (boolean1) {
            $$2 |= 2;
        }
        return $$2;
    }

    public static boolean numberHasMin(byte byte0) {
        return (byte0 & 1) != 0;
    }

    public static boolean numberHasMax(byte byte0) {
        return (byte0 & 2) != 0;
    }

    private static <A extends ArgumentType<?>> void serializeCap(JsonObject jsonObject0, ArgumentTypeInfo.Template<A> argumentTypeInfoTemplateA1) {
        serializeCap(jsonObject0, argumentTypeInfoTemplateA1.type(), argumentTypeInfoTemplateA1);
    }

    private static <A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> void serializeCap(JsonObject jsonObject0, ArgumentTypeInfo<A, T> argumentTypeInfoAT1, ArgumentTypeInfo.Template<A> argumentTypeInfoTemplateA2) {
        argumentTypeInfoAT1.serializeToJson((T) argumentTypeInfoTemplateA2, jsonObject0);
    }

    private static <T extends ArgumentType<?>> void serializeArgumentToJson(JsonObject jsonObject0, T t1) {
        ArgumentTypeInfo.Template<T> $$2 = ArgumentTypeInfos.unpack(t1);
        jsonObject0.addProperty("type", "argument");
        jsonObject0.addProperty("parser", BuiltInRegistries.COMMAND_ARGUMENT_TYPE.getKey($$2.type()).toString());
        JsonObject $$3 = new JsonObject();
        serializeCap($$3, $$2);
        if ($$3.size() > 0) {
            jsonObject0.add("properties", $$3);
        }
    }

    public static <S> JsonObject serializeNodeToJson(CommandDispatcher<S> commandDispatcherS0, CommandNode<S> commandNodeS1) {
        JsonObject $$2 = new JsonObject();
        if (commandNodeS1 instanceof RootCommandNode) {
            $$2.addProperty("type", "root");
        } else if (commandNodeS1 instanceof LiteralCommandNode) {
            $$2.addProperty("type", "literal");
        } else if (commandNodeS1 instanceof ArgumentCommandNode<?, ?> $$3) {
            serializeArgumentToJson($$2, $$3.getType());
        } else {
            LOGGER.error("Could not serialize node {} ({})!", commandNodeS1, commandNodeS1.getClass());
            $$2.addProperty("type", "unknown");
        }
        JsonObject $$4 = new JsonObject();
        for (CommandNode<S> $$5 : commandNodeS1.getChildren()) {
            $$4.add($$5.getName(), serializeNodeToJson(commandDispatcherS0, $$5));
        }
        if ($$4.size() > 0) {
            $$2.add("children", $$4);
        }
        if (commandNodeS1.getCommand() != null) {
            $$2.addProperty("executable", true);
        }
        if (commandNodeS1.getRedirect() != null) {
            Collection<String> $$6 = commandDispatcherS0.getPath(commandNodeS1.getRedirect());
            if (!$$6.isEmpty()) {
                JsonArray $$7 = new JsonArray();
                for (String $$8 : $$6) {
                    $$7.add($$8);
                }
                $$2.add("redirect", $$7);
            }
        }
        return $$2;
    }

    public static <T> Set<ArgumentType<?>> findUsedArgumentTypes(CommandNode<T> commandNodeT0) {
        Set<CommandNode<T>> $$1 = Sets.newIdentityHashSet();
        Set<ArgumentType<?>> $$2 = Sets.newHashSet();
        findUsedArgumentTypes(commandNodeT0, $$2, $$1);
        return $$2;
    }

    private static <T> void findUsedArgumentTypes(CommandNode<T> commandNodeT0, Set<ArgumentType<?>> setArgumentType1, Set<CommandNode<T>> setCommandNodeT2) {
        if (setCommandNodeT2.add(commandNodeT0)) {
            if (commandNodeT0 instanceof ArgumentCommandNode<?, ?> $$3) {
                setArgumentType1.add($$3.getType());
            }
            commandNodeT0.getChildren().forEach(p_235426_ -> findUsedArgumentTypes(p_235426_, setArgumentType1, setCommandNodeT2));
            CommandNode<T> $$4 = commandNodeT0.getRedirect();
            if ($$4 != null) {
                findUsedArgumentTypes($$4, setArgumentType1, setCommandNodeT2);
            }
        }
    }
}