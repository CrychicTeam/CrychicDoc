package net.minecraft.server.commands.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.NbtPathArgument;
import net.minecraft.commands.arguments.NbtTagArgument;
import net.minecraft.nbt.CollectionTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NumericTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class DataCommands {

    private static final SimpleCommandExceptionType ERROR_MERGE_UNCHANGED = new SimpleCommandExceptionType(Component.translatable("commands.data.merge.failed"));

    private static final DynamicCommandExceptionType ERROR_GET_NOT_NUMBER = new DynamicCommandExceptionType(p_139491_ -> Component.translatable("commands.data.get.invalid", p_139491_));

    private static final DynamicCommandExceptionType ERROR_GET_NON_EXISTENT = new DynamicCommandExceptionType(p_139481_ -> Component.translatable("commands.data.get.unknown", p_139481_));

    private static final SimpleCommandExceptionType ERROR_MULTIPLE_TAGS = new SimpleCommandExceptionType(Component.translatable("commands.data.get.multiple"));

    private static final DynamicCommandExceptionType ERROR_EXPECTED_OBJECT = new DynamicCommandExceptionType(p_139448_ -> Component.translatable("commands.data.modify.expected_object", p_139448_));

    private static final DynamicCommandExceptionType ERROR_EXPECTED_VALUE = new DynamicCommandExceptionType(p_264853_ -> Component.translatable("commands.data.modify.expected_value", p_264853_));

    private static final Dynamic2CommandExceptionType ERROR_INVALID_SUBSTRING = new Dynamic2CommandExceptionType((p_288740_, p_288741_) -> Component.translatable("commands.data.modify.invalid_substring", p_288740_, p_288741_));

    public static final List<Function<String, DataCommands.DataProvider>> ALL_PROVIDERS = ImmutableList.of(EntityDataAccessor.PROVIDER, BlockDataAccessor.PROVIDER, StorageDataAccessor.PROVIDER);

    public static final List<DataCommands.DataProvider> TARGET_PROVIDERS = (List<DataCommands.DataProvider>) ALL_PROVIDERS.stream().map(p_139450_ -> (DataCommands.DataProvider) p_139450_.apply("target")).collect(ImmutableList.toImmutableList());

    public static final List<DataCommands.DataProvider> SOURCE_PROVIDERS = (List<DataCommands.DataProvider>) ALL_PROVIDERS.stream().map(p_139410_ -> (DataCommands.DataProvider) p_139410_.apply("source")).collect(ImmutableList.toImmutableList());

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        LiteralArgumentBuilder<CommandSourceStack> $$1 = (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("data").requires(p_139381_ -> p_139381_.hasPermission(2));
        for (DataCommands.DataProvider $$2 : TARGET_PROVIDERS) {
            ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) $$1.then($$2.wrap(Commands.literal("merge"), p_139471_ -> p_139471_.then(Commands.argument("nbt", CompoundTagArgument.compoundTag()).executes(p_142857_ -> mergeData((CommandSourceStack) p_142857_.getSource(), $$2.access(p_142857_), CompoundTagArgument.getCompoundTag(p_142857_, "nbt"))))))).then($$2.wrap(Commands.literal("get"), p_139453_ -> p_139453_.executes(p_142849_ -> getData((CommandSourceStack) p_142849_.getSource(), $$2.access(p_142849_))).then(((RequiredArgumentBuilder) Commands.argument("path", NbtPathArgument.nbtPath()).executes(p_142841_ -> getData((CommandSourceStack) p_142841_.getSource(), $$2.access(p_142841_), NbtPathArgument.getPath(p_142841_, "path")))).then(Commands.argument("scale", DoubleArgumentType.doubleArg()).executes(p_142833_ -> getNumeric((CommandSourceStack) p_142833_.getSource(), $$2.access(p_142833_), NbtPathArgument.getPath(p_142833_, "path"), DoubleArgumentType.getDouble(p_142833_, "scale")))))))).then($$2.wrap(Commands.literal("remove"), p_139413_ -> p_139413_.then(Commands.argument("path", NbtPathArgument.nbtPath()).executes(p_142820_ -> removeData((CommandSourceStack) p_142820_.getSource(), $$2.access(p_142820_), NbtPathArgument.getPath(p_142820_, "path"))))))).then(decorateModification((p_139368_, p_139369_) -> p_139368_.then(Commands.literal("insert").then(Commands.argument("index", IntegerArgumentType.integer()).then(p_139369_.create((p_142859_, p_142860_, p_142861_, p_142862_) -> p_142861_.insert(IntegerArgumentType.getInteger(p_142859_, "index"), p_142860_, p_142862_))))).then(Commands.literal("prepend").then(p_139369_.create((p_142851_, p_142852_, p_142853_, p_142854_) -> p_142853_.insert(0, p_142852_, p_142854_)))).then(Commands.literal("append").then(p_139369_.create((p_142843_, p_142844_, p_142845_, p_142846_) -> p_142845_.insert(-1, p_142844_, p_142846_)))).then(Commands.literal("set").then(p_139369_.create((p_142835_, p_142836_, p_142837_, p_142838_) -> p_142837_.set(p_142836_, (Tag) Iterables.getLast(p_142838_))))).then(Commands.literal("merge").then(p_139369_.create((p_142822_, p_142823_, p_142824_, p_142825_) -> {
                CompoundTag $$4 = new CompoundTag();
                for (Tag $$5 : p_142825_) {
                    if (NbtPathArgument.NbtPath.isTooDeep($$5, 0)) {
                        throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
                    }
                    if (!($$5 instanceof CompoundTag $$6)) {
                        throw ERROR_EXPECTED_OBJECT.create($$5);
                    }
                    $$4.merge($$6);
                }
                Collection<Tag> $$7 = p_142824_.getOrCreate(p_142823_, CompoundTag::new);
                int $$8 = 0;
                for (Tag $$9 : $$7) {
                    if (!($$9 instanceof CompoundTag $$10)) {
                        throw ERROR_EXPECTED_OBJECT.create($$9);
                    }
                    CompoundTag $$12 = $$10.copy();
                    $$10.merge($$4);
                    $$8 += $$12.equals($$10) ? 0 : 1;
                }
                return $$8;
            })))));
        }
        commandDispatcherCommandSourceStack0.register($$1);
    }

    private static String getAsText(Tag tag0) throws CommandSyntaxException {
        if (tag0.getType().isValue()) {
            return tag0.getAsString();
        } else {
            throw ERROR_EXPECTED_VALUE.create(tag0);
        }
    }

    private static List<Tag> stringifyTagList(List<Tag> listTag0, DataCommands.StringProcessor dataCommandsStringProcessor1) throws CommandSyntaxException {
        List<Tag> $$2 = new ArrayList(listTag0.size());
        for (Tag $$3 : listTag0) {
            String $$4 = getAsText($$3);
            $$2.add(StringTag.valueOf(dataCommandsStringProcessor1.process($$4)));
        }
        return $$2;
    }

    private static ArgumentBuilder<CommandSourceStack, ?> decorateModification(BiConsumer<ArgumentBuilder<CommandSourceStack, ?>, DataCommands.DataManipulatorDecorator> biConsumerArgumentBuilderCommandSourceStackDataCommandsDataManipulatorDecorator0) {
        LiteralArgumentBuilder<CommandSourceStack> $$1 = Commands.literal("modify");
        for (DataCommands.DataProvider $$2 : TARGET_PROVIDERS) {
            $$2.wrap($$1, p_264816_ -> {
                ArgumentBuilder<CommandSourceStack, ?> $$3 = Commands.argument("targetPath", NbtPathArgument.nbtPath());
                for (DataCommands.DataProvider $$4 : SOURCE_PROVIDERS) {
                    biConsumerArgumentBuilderCommandSourceStackDataCommandsDataManipulatorDecorator0.accept($$3, (DataCommands.DataManipulatorDecorator) p_142807_ -> $$4.wrap(Commands.literal("from"), p_142812_ -> p_142812_.executes(p_264829_ -> manipulateData(p_264829_, $$2, p_142807_, getSingletonSource(p_264829_, $$4))).then(Commands.argument("sourcePath", NbtPathArgument.nbtPath()).executes(p_264842_ -> manipulateData(p_264842_, $$2, p_142807_, resolveSourcePath(p_264842_, $$4))))));
                    biConsumerArgumentBuilderCommandSourceStackDataCommandsDataManipulatorDecorator0.accept($$3, (DataCommands.DataManipulatorDecorator) p_264836_ -> $$4.wrap(Commands.literal("string"), p_287357_ -> p_287357_.executes(p_288732_ -> manipulateData(p_288732_, $$2, p_264836_, stringifyTagList(getSingletonSource(p_288732_, $$4), p_264813_ -> p_264813_))).then(((RequiredArgumentBuilder) Commands.argument("sourcePath", NbtPathArgument.nbtPath()).executes(p_288737_ -> manipulateData(p_288737_, $$2, p_264836_, stringifyTagList(resolveSourcePath(p_288737_, $$4), p_264821_ -> p_264821_)))).then(((RequiredArgumentBuilder) Commands.argument("start", IntegerArgumentType.integer()).executes(p_288753_ -> manipulateData(p_288753_, $$2, p_264836_, stringifyTagList(resolveSourcePath(p_288753_, $$4), p_287353_ -> substring(p_287353_, IntegerArgumentType.getInteger(p_288753_, "start")))))).then(Commands.argument("end", IntegerArgumentType.integer()).executes(p_288749_ -> manipulateData(p_288749_, $$2, p_264836_, stringifyTagList(resolveSourcePath(p_288749_, $$4), p_287359_ -> substring(p_287359_, IntegerArgumentType.getInteger(p_288749_, "start"), IntegerArgumentType.getInteger(p_288749_, "end"))))))))));
                }
                biConsumerArgumentBuilderCommandSourceStackDataCommandsDataManipulatorDecorator0.accept($$3, (DataCommands.DataManipulatorDecorator) p_142799_ -> Commands.literal("value").then(Commands.argument("value", NbtTagArgument.nbtTag()).executes(p_142803_ -> {
                    List<Tag> $$3x = Collections.singletonList(NbtTagArgument.getNbtTag(p_142803_, "value"));
                    return manipulateData(p_142803_, $$2, p_142799_, $$3x);
                })));
                return p_264816_.then($$3);
            });
        }
        return $$1;
    }

    private static String validatedSubstring(String string0, int int1, int int2) throws CommandSyntaxException {
        if (int1 >= 0 && int2 <= string0.length() && int1 <= int2) {
            return string0.substring(int1, int2);
        } else {
            throw ERROR_INVALID_SUBSTRING.create(int1, int2);
        }
    }

    private static String substring(String string0, int int1, int int2) throws CommandSyntaxException {
        int $$3 = string0.length();
        int $$4 = getOffset(int1, $$3);
        int $$5 = getOffset(int2, $$3);
        return validatedSubstring(string0, $$4, $$5);
    }

    private static String substring(String string0, int int1) throws CommandSyntaxException {
        int $$2 = string0.length();
        return validatedSubstring(string0, getOffset(int1, $$2), $$2);
    }

    private static int getOffset(int int0, int int1) {
        return int0 >= 0 ? int0 : int1 + int0;
    }

    private static List<Tag> getSingletonSource(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, DataCommands.DataProvider dataCommandsDataProvider1) throws CommandSyntaxException {
        DataAccessor $$2 = dataCommandsDataProvider1.access(commandContextCommandSourceStack0);
        return Collections.singletonList($$2.getData());
    }

    private static List<Tag> resolveSourcePath(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, DataCommands.DataProvider dataCommandsDataProvider1) throws CommandSyntaxException {
        DataAccessor $$2 = dataCommandsDataProvider1.access(commandContextCommandSourceStack0);
        NbtPathArgument.NbtPath $$3 = NbtPathArgument.getPath(commandContextCommandSourceStack0, "sourcePath");
        return $$3.get($$2.getData());
    }

    private static int manipulateData(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, DataCommands.DataProvider dataCommandsDataProvider1, DataCommands.DataManipulator dataCommandsDataManipulator2, List<Tag> listTag3) throws CommandSyntaxException {
        DataAccessor $$4 = dataCommandsDataProvider1.access(commandContextCommandSourceStack0);
        NbtPathArgument.NbtPath $$5 = NbtPathArgument.getPath(commandContextCommandSourceStack0, "targetPath");
        CompoundTag $$6 = $$4.getData();
        int $$7 = dataCommandsDataManipulator2.modify(commandContextCommandSourceStack0, $$6, $$5, listTag3);
        if ($$7 == 0) {
            throw ERROR_MERGE_UNCHANGED.create();
        } else {
            $$4.setData($$6);
            ((CommandSourceStack) commandContextCommandSourceStack0.getSource()).sendSuccess(() -> $$4.getModifiedSuccess(), true);
            return $$7;
        }
    }

    private static int removeData(CommandSourceStack commandSourceStack0, DataAccessor dataAccessor1, NbtPathArgument.NbtPath nbtPathArgumentNbtPath2) throws CommandSyntaxException {
        CompoundTag $$3 = dataAccessor1.getData();
        int $$4 = nbtPathArgumentNbtPath2.remove($$3);
        if ($$4 == 0) {
            throw ERROR_MERGE_UNCHANGED.create();
        } else {
            dataAccessor1.setData($$3);
            commandSourceStack0.sendSuccess(() -> dataAccessor1.getModifiedSuccess(), true);
            return $$4;
        }
    }

    private static Tag getSingleTag(NbtPathArgument.NbtPath nbtPathArgumentNbtPath0, DataAccessor dataAccessor1) throws CommandSyntaxException {
        Collection<Tag> $$2 = nbtPathArgumentNbtPath0.get(dataAccessor1.getData());
        Iterator<Tag> $$3 = $$2.iterator();
        Tag $$4 = (Tag) $$3.next();
        if ($$3.hasNext()) {
            throw ERROR_MULTIPLE_TAGS.create();
        } else {
            return $$4;
        }
    }

    private static int getData(CommandSourceStack commandSourceStack0, DataAccessor dataAccessor1, NbtPathArgument.NbtPath nbtPathArgumentNbtPath2) throws CommandSyntaxException {
        Tag $$3 = getSingleTag(nbtPathArgumentNbtPath2, dataAccessor1);
        int $$4;
        if ($$3 instanceof NumericTag) {
            $$4 = Mth.floor(((NumericTag) $$3).getAsDouble());
        } else if ($$3 instanceof CollectionTag) {
            $$4 = ((CollectionTag) $$3).size();
        } else if ($$3 instanceof CompoundTag) {
            $$4 = ((CompoundTag) $$3).size();
        } else {
            if (!($$3 instanceof StringTag)) {
                throw ERROR_GET_NON_EXISTENT.create(nbtPathArgumentNbtPath2.toString());
            }
            $$4 = $$3.getAsString().length();
        }
        commandSourceStack0.sendSuccess(() -> dataAccessor1.getPrintSuccess($$3), false);
        return $$4;
    }

    private static int getNumeric(CommandSourceStack commandSourceStack0, DataAccessor dataAccessor1, NbtPathArgument.NbtPath nbtPathArgumentNbtPath2, double double3) throws CommandSyntaxException {
        Tag $$4 = getSingleTag(nbtPathArgumentNbtPath2, dataAccessor1);
        if (!($$4 instanceof NumericTag)) {
            throw ERROR_GET_NOT_NUMBER.create(nbtPathArgumentNbtPath2.toString());
        } else {
            int $$5 = Mth.floor(((NumericTag) $$4).getAsDouble() * double3);
            commandSourceStack0.sendSuccess(() -> dataAccessor1.getPrintSuccess(nbtPathArgumentNbtPath2, double3, $$5), false);
            return $$5;
        }
    }

    private static int getData(CommandSourceStack commandSourceStack0, DataAccessor dataAccessor1) throws CommandSyntaxException {
        CompoundTag $$2 = dataAccessor1.getData();
        commandSourceStack0.sendSuccess(() -> dataAccessor1.getPrintSuccess($$2), false);
        return 1;
    }

    private static int mergeData(CommandSourceStack commandSourceStack0, DataAccessor dataAccessor1, CompoundTag compoundTag2) throws CommandSyntaxException {
        CompoundTag $$3 = dataAccessor1.getData();
        if (NbtPathArgument.NbtPath.isTooDeep(compoundTag2, 0)) {
            throw NbtPathArgument.ERROR_DATA_TOO_DEEP.create();
        } else {
            CompoundTag $$4 = $$3.copy().merge(compoundTag2);
            if ($$3.equals($$4)) {
                throw ERROR_MERGE_UNCHANGED.create();
            } else {
                dataAccessor1.setData($$4);
                commandSourceStack0.sendSuccess(() -> dataAccessor1.getModifiedSuccess(), true);
                return 1;
            }
        }
    }

    @FunctionalInterface
    interface DataManipulator {

        int modify(CommandContext<CommandSourceStack> var1, CompoundTag var2, NbtPathArgument.NbtPath var3, List<Tag> var4) throws CommandSyntaxException;
    }

    @FunctionalInterface
    interface DataManipulatorDecorator {

        ArgumentBuilder<CommandSourceStack, ?> create(DataCommands.DataManipulator var1);
    }

    public interface DataProvider {

        DataAccessor access(CommandContext<CommandSourceStack> var1) throws CommandSyntaxException;

        ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> var1, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> var2);
    }

    @FunctionalInterface
    interface StringProcessor {

        String process(String var1) throws CommandSyntaxException;
    }
}