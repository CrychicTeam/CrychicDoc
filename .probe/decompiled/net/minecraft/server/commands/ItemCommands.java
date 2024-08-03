package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class ItemCommands {

    static final Dynamic3CommandExceptionType ERROR_TARGET_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((p_180355_, p_180356_, p_180357_) -> Component.translatable("commands.item.target.not_a_container", p_180355_, p_180356_, p_180357_));

    private static final Dynamic3CommandExceptionType ERROR_SOURCE_NOT_A_CONTAINER = new Dynamic3CommandExceptionType((p_180347_, p_180348_, p_180349_) -> Component.translatable("commands.item.source.not_a_container", p_180347_, p_180348_, p_180349_));

    static final DynamicCommandExceptionType ERROR_TARGET_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(p_180361_ -> Component.translatable("commands.item.target.no_such_slot", p_180361_));

    private static final DynamicCommandExceptionType ERROR_SOURCE_INAPPLICABLE_SLOT = new DynamicCommandExceptionType(p_180353_ -> Component.translatable("commands.item.source.no_such_slot", p_180353_));

    private static final DynamicCommandExceptionType ERROR_TARGET_NO_CHANGES = new DynamicCommandExceptionType(p_180342_ -> Component.translatable("commands.item.target.no_changes", p_180342_));

    private static final Dynamic2CommandExceptionType ERROR_TARGET_NO_CHANGES_KNOWN_ITEM = new Dynamic2CommandExceptionType((p_180344_, p_180345_) -> Component.translatable("commands.item.target.no_changed.known_item", p_180344_, p_180345_));

    private static final SuggestionProvider<CommandSourceStack> SUGGEST_MODIFIER = (p_278910_, p_278911_) -> {
        LootDataManager $$2 = ((CommandSourceStack) p_278910_.getSource()).getServer().getLootData();
        return SharedSuggestionProvider.suggestResource($$2.getKeys(LootDataType.MODIFIER), p_278911_);
    };

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("item").requires(p_180256_ -> p_180256_.hasPermission(2))).then(((LiteralArgumentBuilder) Commands.literal("replace").then(Commands.literal("block").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("slot", SlotArgument.slot()).then(Commands.literal("with").then(((RequiredArgumentBuilder) Commands.argument("item", ItemArgument.item(commandBuildContext1)).executes(p_180383_ -> setBlockItem((CommandSourceStack) p_180383_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180383_, "pos"), SlotArgument.getSlot(p_180383_, "slot"), ItemArgument.getItem(p_180383_, "item").createItemStack(1, false)))).then(Commands.argument("count", IntegerArgumentType.integer(1, 64)).executes(p_180381_ -> setBlockItem((CommandSourceStack) p_180381_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180381_, "pos"), SlotArgument.getSlot(p_180381_, "slot"), ItemArgument.getItem(p_180381_, "item").createItemStack(IntegerArgumentType.getInteger(p_180381_, "count"), true))))))).then(((LiteralArgumentBuilder) Commands.literal("from").then(Commands.literal("block").then(Commands.argument("source", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("sourceSlot", SlotArgument.slot()).executes(p_180379_ -> blockToBlock((CommandSourceStack) p_180379_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180379_, "source"), SlotArgument.getSlot(p_180379_, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(p_180379_, "pos"), SlotArgument.getSlot(p_180379_, "slot")))).then(Commands.argument("modifier", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIER).executes(p_180377_ -> blockToBlock((CommandSourceStack) p_180377_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180377_, "source"), SlotArgument.getSlot(p_180377_, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(p_180377_, "pos"), SlotArgument.getSlot(p_180377_, "slot"), ResourceLocationArgument.getItemModifier(p_180377_, "modifier")))))))).then(Commands.literal("entity").then(Commands.argument("source", EntityArgument.entity()).then(((RequiredArgumentBuilder) Commands.argument("sourceSlot", SlotArgument.slot()).executes(p_180375_ -> entityToBlock((CommandSourceStack) p_180375_.getSource(), EntityArgument.getEntity(p_180375_, "source"), SlotArgument.getSlot(p_180375_, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(p_180375_, "pos"), SlotArgument.getSlot(p_180375_, "slot")))).then(Commands.argument("modifier", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIER).executes(p_180373_ -> entityToBlock((CommandSourceStack) p_180373_.getSource(), EntityArgument.getEntity(p_180373_, "source"), SlotArgument.getSlot(p_180373_, "sourceSlot"), BlockPosArgument.getLoadedBlockPos(p_180373_, "pos"), SlotArgument.getSlot(p_180373_, "slot"), ResourceLocationArgument.getItemModifier(p_180373_, "modifier")))))))))))).then(Commands.literal("entity").then(Commands.argument("targets", EntityArgument.entities()).then(((RequiredArgumentBuilder) Commands.argument("slot", SlotArgument.slot()).then(Commands.literal("with").then(((RequiredArgumentBuilder) Commands.argument("item", ItemArgument.item(commandBuildContext1)).executes(p_180371_ -> setEntityItem((CommandSourceStack) p_180371_.getSource(), EntityArgument.getEntities(p_180371_, "targets"), SlotArgument.getSlot(p_180371_, "slot"), ItemArgument.getItem(p_180371_, "item").createItemStack(1, false)))).then(Commands.argument("count", IntegerArgumentType.integer(1, 64)).executes(p_180369_ -> setEntityItem((CommandSourceStack) p_180369_.getSource(), EntityArgument.getEntities(p_180369_, "targets"), SlotArgument.getSlot(p_180369_, "slot"), ItemArgument.getItem(p_180369_, "item").createItemStack(IntegerArgumentType.getInteger(p_180369_, "count"), true))))))).then(((LiteralArgumentBuilder) Commands.literal("from").then(Commands.literal("block").then(Commands.argument("source", BlockPosArgument.blockPos()).then(((RequiredArgumentBuilder) Commands.argument("sourceSlot", SlotArgument.slot()).executes(p_180367_ -> blockToEntities((CommandSourceStack) p_180367_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180367_, "source"), SlotArgument.getSlot(p_180367_, "sourceSlot"), EntityArgument.getEntities(p_180367_, "targets"), SlotArgument.getSlot(p_180367_, "slot")))).then(Commands.argument("modifier", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIER).executes(p_180365_ -> blockToEntities((CommandSourceStack) p_180365_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180365_, "source"), SlotArgument.getSlot(p_180365_, "sourceSlot"), EntityArgument.getEntities(p_180365_, "targets"), SlotArgument.getSlot(p_180365_, "slot"), ResourceLocationArgument.getItemModifier(p_180365_, "modifier")))))))).then(Commands.literal("entity").then(Commands.argument("source", EntityArgument.entity()).then(((RequiredArgumentBuilder) Commands.argument("sourceSlot", SlotArgument.slot()).executes(p_180363_ -> entityToEntities((CommandSourceStack) p_180363_.getSource(), EntityArgument.getEntity(p_180363_, "source"), SlotArgument.getSlot(p_180363_, "sourceSlot"), EntityArgument.getEntities(p_180363_, "targets"), SlotArgument.getSlot(p_180363_, "slot")))).then(Commands.argument("modifier", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIER).executes(p_180359_ -> entityToEntities((CommandSourceStack) p_180359_.getSource(), EntityArgument.getEntity(p_180359_, "source"), SlotArgument.getSlot(p_180359_, "sourceSlot"), EntityArgument.getEntities(p_180359_, "targets"), SlotArgument.getSlot(p_180359_, "slot"), ResourceLocationArgument.getItemModifier(p_180359_, "modifier"))))))))))))).then(((LiteralArgumentBuilder) Commands.literal("modify").then(Commands.literal("block").then(Commands.argument("pos", BlockPosArgument.blockPos()).then(Commands.argument("slot", SlotArgument.slot()).then(Commands.argument("modifier", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIER).executes(p_180351_ -> modifyBlockItem((CommandSourceStack) p_180351_.getSource(), BlockPosArgument.getLoadedBlockPos(p_180351_, "pos"), SlotArgument.getSlot(p_180351_, "slot"), ResourceLocationArgument.getItemModifier(p_180351_, "modifier")))))))).then(Commands.literal("entity").then(Commands.argument("targets", EntityArgument.entities()).then(Commands.argument("slot", SlotArgument.slot()).then(Commands.argument("modifier", ResourceLocationArgument.id()).suggests(SUGGEST_MODIFIER).executes(p_180251_ -> modifyEntityItem((CommandSourceStack) p_180251_.getSource(), EntityArgument.getEntities(p_180251_, "targets"), SlotArgument.getSlot(p_180251_, "slot"), ResourceLocationArgument.getItemModifier(p_180251_, "modifier")))))))));
    }

    private static int modifyBlockItem(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, LootItemFunction lootItemFunction3) throws CommandSyntaxException {
        Container $$4 = getContainer(commandSourceStack0, blockPos1, ERROR_TARGET_NOT_A_CONTAINER);
        if (int2 >= 0 && int2 < $$4.getContainerSize()) {
            ItemStack $$5 = applyModifier(commandSourceStack0, lootItemFunction3, $$4.getItem(int2));
            $$4.setItem(int2, $$5);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.item.block.set.success", blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_(), $$5.getDisplayName()), true);
            return 1;
        } else {
            throw ERROR_TARGET_INAPPLICABLE_SLOT.create(int2);
        }
    }

    private static int modifyEntityItem(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, int int2, LootItemFunction lootItemFunction3) throws CommandSyntaxException {
        Map<Entity, ItemStack> $$4 = Maps.newHashMapWithExpectedSize(collectionExtendsEntity1.size());
        for (Entity $$5 : collectionExtendsEntity1) {
            SlotAccess $$6 = $$5.getSlot(int2);
            if ($$6 != SlotAccess.NULL) {
                ItemStack $$7 = applyModifier(commandSourceStack0, lootItemFunction3, $$6.get().copy());
                if ($$6.set($$7)) {
                    $$4.put($$5, $$7);
                    if ($$5 instanceof ServerPlayer) {
                        ((ServerPlayer) $$5).f_36096_.broadcastChanges();
                    }
                }
            }
        }
        if ($$4.isEmpty()) {
            throw ERROR_TARGET_NO_CHANGES.create(int2);
        } else {
            if ($$4.size() == 1) {
                Entry<Entity, ItemStack> $$8 = (Entry<Entity, ItemStack>) $$4.entrySet().iterator().next();
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", ((Entity) $$8.getKey()).getDisplayName(), ((ItemStack) $$8.getValue()).getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", $$4.size()), true);
            }
            return $$4.size();
        }
    }

    private static int setBlockItem(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, ItemStack itemStack3) throws CommandSyntaxException {
        Container $$4 = getContainer(commandSourceStack0, blockPos1, ERROR_TARGET_NOT_A_CONTAINER);
        if (int2 >= 0 && int2 < $$4.getContainerSize()) {
            $$4.setItem(int2, itemStack3);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.item.block.set.success", blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_(), itemStack3.getDisplayName()), true);
            return 1;
        } else {
            throw ERROR_TARGET_INAPPLICABLE_SLOT.create(int2);
        }
    }

    private static Container getContainer(CommandSourceStack commandSourceStack0, BlockPos blockPos1, Dynamic3CommandExceptionType dynamicCommandExceptionType2) throws CommandSyntaxException {
        BlockEntity $$3 = commandSourceStack0.getLevel().m_7702_(blockPos1);
        if (!($$3 instanceof Container)) {
            throw dynamicCommandExceptionType2.create(blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_());
        } else {
            return (Container) $$3;
        }
    }

    private static int setEntityItem(CommandSourceStack commandSourceStack0, Collection<? extends Entity> collectionExtendsEntity1, int int2, ItemStack itemStack3) throws CommandSyntaxException {
        List<Entity> $$4 = Lists.newArrayListWithCapacity(collectionExtendsEntity1.size());
        for (Entity $$5 : collectionExtendsEntity1) {
            SlotAccess $$6 = $$5.getSlot(int2);
            if ($$6 != SlotAccess.NULL && $$6.set(itemStack3.copy())) {
                $$4.add($$5);
                if ($$5 instanceof ServerPlayer) {
                    ((ServerPlayer) $$5).f_36096_.broadcastChanges();
                }
            }
        }
        if ($$4.isEmpty()) {
            throw ERROR_TARGET_NO_CHANGES_KNOWN_ITEM.create(itemStack3.getDisplayName(), int2);
        } else {
            if ($$4.size() == 1) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.single", ((Entity) $$4.iterator().next()).getDisplayName(), itemStack3.getDisplayName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.item.entity.set.success.multiple", $$4.size(), itemStack3.getDisplayName()), true);
            }
            return $$4.size();
        }
    }

    private static int blockToEntities(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, Collection<? extends Entity> collectionExtendsEntity3, int int4) throws CommandSyntaxException {
        return setEntityItem(commandSourceStack0, collectionExtendsEntity3, int4, getBlockItem(commandSourceStack0, blockPos1, int2));
    }

    private static int blockToEntities(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, Collection<? extends Entity> collectionExtendsEntity3, int int4, LootItemFunction lootItemFunction5) throws CommandSyntaxException {
        return setEntityItem(commandSourceStack0, collectionExtendsEntity3, int4, applyModifier(commandSourceStack0, lootItemFunction5, getBlockItem(commandSourceStack0, blockPos1, int2)));
    }

    private static int blockToBlock(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, BlockPos blockPos3, int int4) throws CommandSyntaxException {
        return setBlockItem(commandSourceStack0, blockPos3, int4, getBlockItem(commandSourceStack0, blockPos1, int2));
    }

    private static int blockToBlock(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, BlockPos blockPos3, int int4, LootItemFunction lootItemFunction5) throws CommandSyntaxException {
        return setBlockItem(commandSourceStack0, blockPos3, int4, applyModifier(commandSourceStack0, lootItemFunction5, getBlockItem(commandSourceStack0, blockPos1, int2)));
    }

    private static int entityToBlock(CommandSourceStack commandSourceStack0, Entity entity1, int int2, BlockPos blockPos3, int int4) throws CommandSyntaxException {
        return setBlockItem(commandSourceStack0, blockPos3, int4, getEntityItem(entity1, int2));
    }

    private static int entityToBlock(CommandSourceStack commandSourceStack0, Entity entity1, int int2, BlockPos blockPos3, int int4, LootItemFunction lootItemFunction5) throws CommandSyntaxException {
        return setBlockItem(commandSourceStack0, blockPos3, int4, applyModifier(commandSourceStack0, lootItemFunction5, getEntityItem(entity1, int2)));
    }

    private static int entityToEntities(CommandSourceStack commandSourceStack0, Entity entity1, int int2, Collection<? extends Entity> collectionExtendsEntity3, int int4) throws CommandSyntaxException {
        return setEntityItem(commandSourceStack0, collectionExtendsEntity3, int4, getEntityItem(entity1, int2));
    }

    private static int entityToEntities(CommandSourceStack commandSourceStack0, Entity entity1, int int2, Collection<? extends Entity> collectionExtendsEntity3, int int4, LootItemFunction lootItemFunction5) throws CommandSyntaxException {
        return setEntityItem(commandSourceStack0, collectionExtendsEntity3, int4, applyModifier(commandSourceStack0, lootItemFunction5, getEntityItem(entity1, int2)));
    }

    private static ItemStack applyModifier(CommandSourceStack commandSourceStack0, LootItemFunction lootItemFunction1, ItemStack itemStack2) {
        ServerLevel $$3 = commandSourceStack0.getLevel();
        LootParams $$4 = new LootParams.Builder($$3).withParameter(LootContextParams.ORIGIN, commandSourceStack0.getPosition()).withOptionalParameter(LootContextParams.THIS_ENTITY, commandSourceStack0.getEntity()).create(LootContextParamSets.COMMAND);
        LootContext $$5 = new LootContext.Builder($$4).create(null);
        $$5.pushVisitedElement(LootContext.createVisitedEntry(lootItemFunction1));
        return (ItemStack) lootItemFunction1.apply(itemStack2, $$5);
    }

    private static ItemStack getEntityItem(Entity entity0, int int1) throws CommandSyntaxException {
        SlotAccess $$2 = entity0.getSlot(int1);
        if ($$2 == SlotAccess.NULL) {
            throw ERROR_SOURCE_INAPPLICABLE_SLOT.create(int1);
        } else {
            return $$2.get().copy();
        }
    }

    private static ItemStack getBlockItem(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2) throws CommandSyntaxException {
        Container $$3 = getContainer(commandSourceStack0, blockPos1, ERROR_SOURCE_NOT_A_CONTAINER);
        if (int2 >= 0 && int2 < $$3.getContainerSize()) {
            return $$3.getItem(int2).copy();
        } else {
            throw ERROR_SOURCE_INAPPLICABLE_SLOT.create(int2);
        }
    }
}