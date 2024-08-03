package net.minecraft.server.commands;

import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Collection;
import java.util.List;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.SlotArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootDataType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class LootCommand {

    public static final SuggestionProvider<CommandSourceStack> SUGGEST_LOOT_TABLE = (p_278916_, p_278917_) -> {
        LootDataManager $$2 = ((CommandSourceStack) p_278916_.getSource()).getServer().getLootData();
        return SharedSuggestionProvider.suggestResource($$2.getKeys(LootDataType.TABLE), p_278917_);
    };

    private static final DynamicCommandExceptionType ERROR_NO_HELD_ITEMS = new DynamicCommandExceptionType(p_137999_ -> Component.translatable("commands.drop.no_held_items", p_137999_));

    private static final DynamicCommandExceptionType ERROR_NO_LOOT_TABLE = new DynamicCommandExceptionType(p_137977_ -> Component.translatable("commands.drop.no_loot_table", p_137977_));

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0, CommandBuildContext commandBuildContext1) {
        commandDispatcherCommandSourceStack0.register(addTargets((LiteralArgumentBuilder) Commands.literal("loot").requires(p_137937_ -> p_137937_.hasPermission(2)), (p_214520_, p_214521_) -> p_214520_.then(Commands.literal("fish").then(Commands.argument("loot_table", ResourceLocationArgument.id()).suggests(SUGGEST_LOOT_TABLE).then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_180421_ -> dropFishingLoot(p_180421_, ResourceLocationArgument.getId(p_180421_, "loot_table"), BlockPosArgument.getLoadedBlockPos(p_180421_, "pos"), ItemStack.EMPTY, p_214521_))).then(Commands.argument("tool", ItemArgument.item(commandBuildContext1)).executes(p_180418_ -> dropFishingLoot(p_180418_, ResourceLocationArgument.getId(p_180418_, "loot_table"), BlockPosArgument.getLoadedBlockPos(p_180418_, "pos"), ItemArgument.getItem(p_180418_, "tool").createItemStack(1, false), p_214521_)))).then(Commands.literal("mainhand").executes(p_180415_ -> dropFishingLoot(p_180415_, ResourceLocationArgument.getId(p_180415_, "loot_table"), BlockPosArgument.getLoadedBlockPos(p_180415_, "pos"), getSourceHandItem((CommandSourceStack) p_180415_.getSource(), EquipmentSlot.MAINHAND), p_214521_)))).then(Commands.literal("offhand").executes(p_180412_ -> dropFishingLoot(p_180412_, ResourceLocationArgument.getId(p_180412_, "loot_table"), BlockPosArgument.getLoadedBlockPos(p_180412_, "pos"), getSourceHandItem((CommandSourceStack) p_180412_.getSource(), EquipmentSlot.OFFHAND), p_214521_)))))).then(Commands.literal("loot").then(Commands.argument("loot_table", ResourceLocationArgument.id()).suggests(SUGGEST_LOOT_TABLE).executes(p_180409_ -> dropChestLoot(p_180409_, ResourceLocationArgument.getId(p_180409_, "loot_table"), p_214521_)))).then(Commands.literal("kill").then(Commands.argument("target", EntityArgument.entity()).executes(p_180406_ -> dropKillLoot(p_180406_, EntityArgument.getEntity(p_180406_, "target"), p_214521_)))).then(Commands.literal("mine").then(((RequiredArgumentBuilder) ((RequiredArgumentBuilder) ((RequiredArgumentBuilder) Commands.argument("pos", BlockPosArgument.blockPos()).executes(p_180403_ -> dropBlockLoot(p_180403_, BlockPosArgument.getLoadedBlockPos(p_180403_, "pos"), ItemStack.EMPTY, p_214521_))).then(Commands.argument("tool", ItemArgument.item(commandBuildContext1)).executes(p_180400_ -> dropBlockLoot(p_180400_, BlockPosArgument.getLoadedBlockPos(p_180400_, "pos"), ItemArgument.getItem(p_180400_, "tool").createItemStack(1, false), p_214521_)))).then(Commands.literal("mainhand").executes(p_180397_ -> dropBlockLoot(p_180397_, BlockPosArgument.getLoadedBlockPos(p_180397_, "pos"), getSourceHandItem((CommandSourceStack) p_180397_.getSource(), EquipmentSlot.MAINHAND), p_214521_)))).then(Commands.literal("offhand").executes(p_180394_ -> dropBlockLoot(p_180394_, BlockPosArgument.getLoadedBlockPos(p_180394_, "pos"), getSourceHandItem((CommandSourceStack) p_180394_.getSource(), EquipmentSlot.OFFHAND), p_214521_)))))));
    }

    private static <T extends ArgumentBuilder<CommandSourceStack, T>> T addTargets(T t0, LootCommand.TailProvider lootCommandTailProvider1) {
        return (T) t0.then(((LiteralArgumentBuilder) Commands.literal("replace").then(Commands.literal("entity").then(Commands.argument("entities", EntityArgument.entities()).then(lootCommandTailProvider1.construct(Commands.argument("slot", SlotArgument.slot()), (p_138032_, p_138033_, p_138034_) -> entityReplace(EntityArgument.getEntities(p_138032_, "entities"), SlotArgument.getSlot(p_138032_, "slot"), p_138033_.size(), p_138033_, p_138034_)).then(lootCommandTailProvider1.construct(Commands.argument("count", IntegerArgumentType.integer(0)), (p_138025_, p_138026_, p_138027_) -> entityReplace(EntityArgument.getEntities(p_138025_, "entities"), SlotArgument.getSlot(p_138025_, "slot"), IntegerArgumentType.getInteger(p_138025_, "count"), p_138026_, p_138027_))))))).then(Commands.literal("block").then(Commands.argument("targetPos", BlockPosArgument.blockPos()).then(lootCommandTailProvider1.construct(Commands.argument("slot", SlotArgument.slot()), (p_138018_, p_138019_, p_138020_) -> blockReplace((CommandSourceStack) p_138018_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138018_, "targetPos"), SlotArgument.getSlot(p_138018_, "slot"), p_138019_.size(), p_138019_, p_138020_)).then(lootCommandTailProvider1.construct(Commands.argument("count", IntegerArgumentType.integer(0)), (p_138011_, p_138012_, p_138013_) -> blockReplace((CommandSourceStack) p_138011_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138011_, "targetPos"), IntegerArgumentType.getInteger(p_138011_, "slot"), IntegerArgumentType.getInteger(p_138011_, "count"), p_138012_, p_138013_))))))).then(Commands.literal("insert").then(lootCommandTailProvider1.construct(Commands.argument("targetPos", BlockPosArgument.blockPos()), (p_138004_, p_138005_, p_138006_) -> blockDistribute((CommandSourceStack) p_138004_.getSource(), BlockPosArgument.getLoadedBlockPos(p_138004_, "targetPos"), p_138005_, p_138006_)))).then(Commands.literal("give").then(lootCommandTailProvider1.construct(Commands.argument("players", EntityArgument.players()), (p_137992_, p_137993_, p_137994_) -> playerGive(EntityArgument.getPlayers(p_137992_, "players"), p_137993_, p_137994_)))).then(Commands.literal("spawn").then(lootCommandTailProvider1.construct(Commands.argument("targetPos", Vec3Argument.vec3()), (p_137918_, p_137919_, p_137920_) -> dropInWorld((CommandSourceStack) p_137918_.getSource(), Vec3Argument.getVec3(p_137918_, "targetPos"), p_137919_, p_137920_))));
    }

    private static Container getContainer(CommandSourceStack commandSourceStack0, BlockPos blockPos1) throws CommandSyntaxException {
        BlockEntity $$2 = commandSourceStack0.getLevel().m_7702_(blockPos1);
        if (!($$2 instanceof Container)) {
            throw ItemCommands.ERROR_TARGET_NOT_A_CONTAINER.create(blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_());
        } else {
            return (Container) $$2;
        }
    }

    private static int blockDistribute(CommandSourceStack commandSourceStack0, BlockPos blockPos1, List<ItemStack> listItemStack2, LootCommand.Callback lootCommandCallback3) throws CommandSyntaxException {
        Container $$4 = getContainer(commandSourceStack0, blockPos1);
        List<ItemStack> $$5 = Lists.newArrayListWithCapacity(listItemStack2.size());
        for (ItemStack $$6 : listItemStack2) {
            if (distributeToContainer($$4, $$6.copy())) {
                $$4.setChanged();
                $$5.add($$6);
            }
        }
        lootCommandCallback3.accept($$5);
        return $$5.size();
    }

    private static boolean distributeToContainer(Container container0, ItemStack itemStack1) {
        boolean $$2 = false;
        for (int $$3 = 0; $$3 < container0.getContainerSize() && !itemStack1.isEmpty(); $$3++) {
            ItemStack $$4 = container0.getItem($$3);
            if (container0.canPlaceItem($$3, itemStack1)) {
                if ($$4.isEmpty()) {
                    container0.setItem($$3, itemStack1);
                    $$2 = true;
                    break;
                }
                if (canMergeItems($$4, itemStack1)) {
                    int $$5 = itemStack1.getMaxStackSize() - $$4.getCount();
                    int $$6 = Math.min(itemStack1.getCount(), $$5);
                    itemStack1.shrink($$6);
                    $$4.grow($$6);
                    $$2 = true;
                }
            }
        }
        return $$2;
    }

    private static int blockReplace(CommandSourceStack commandSourceStack0, BlockPos blockPos1, int int2, int int3, List<ItemStack> listItemStack4, LootCommand.Callback lootCommandCallback5) throws CommandSyntaxException {
        Container $$6 = getContainer(commandSourceStack0, blockPos1);
        int $$7 = $$6.getContainerSize();
        if (int2 >= 0 && int2 < $$7) {
            List<ItemStack> $$8 = Lists.newArrayListWithCapacity(listItemStack4.size());
            for (int $$9 = 0; $$9 < int3; $$9++) {
                int $$10 = int2 + $$9;
                ItemStack $$11 = $$9 < listItemStack4.size() ? (ItemStack) listItemStack4.get($$9) : ItemStack.EMPTY;
                if ($$6.canPlaceItem($$10, $$11)) {
                    $$6.setItem($$10, $$11);
                    $$8.add($$11);
                }
            }
            lootCommandCallback5.accept($$8);
            return $$8.size();
        } else {
            throw ItemCommands.ERROR_TARGET_INAPPLICABLE_SLOT.create(int2);
        }
    }

    private static boolean canMergeItems(ItemStack itemStack0, ItemStack itemStack1) {
        return itemStack0.getCount() <= itemStack0.getMaxStackSize() && ItemStack.isSameItemSameTags(itemStack0, itemStack1);
    }

    private static int playerGive(Collection<ServerPlayer> collectionServerPlayer0, List<ItemStack> listItemStack1, LootCommand.Callback lootCommandCallback2) throws CommandSyntaxException {
        List<ItemStack> $$3 = Lists.newArrayListWithCapacity(listItemStack1.size());
        for (ItemStack $$4 : listItemStack1) {
            for (ServerPlayer $$5 : collectionServerPlayer0) {
                if ($$5.m_150109_().add($$4.copy())) {
                    $$3.add($$4);
                }
            }
        }
        lootCommandCallback2.accept($$3);
        return $$3.size();
    }

    private static void setSlots(Entity entity0, List<ItemStack> listItemStack1, int int2, int int3, List<ItemStack> listItemStack4) {
        for (int $$5 = 0; $$5 < int3; $$5++) {
            ItemStack $$6 = $$5 < listItemStack1.size() ? (ItemStack) listItemStack1.get($$5) : ItemStack.EMPTY;
            SlotAccess $$7 = entity0.getSlot(int2 + $$5);
            if ($$7 != SlotAccess.NULL && $$7.set($$6.copy())) {
                listItemStack4.add($$6);
            }
        }
    }

    private static int entityReplace(Collection<? extends Entity> collectionExtendsEntity0, int int1, int int2, List<ItemStack> listItemStack3, LootCommand.Callback lootCommandCallback4) throws CommandSyntaxException {
        List<ItemStack> $$5 = Lists.newArrayListWithCapacity(listItemStack3.size());
        for (Entity $$6 : collectionExtendsEntity0) {
            if ($$6 instanceof ServerPlayer $$7) {
                setSlots($$6, listItemStack3, int1, int2, $$5);
                $$7.f_36096_.broadcastChanges();
            } else {
                setSlots($$6, listItemStack3, int1, int2, $$5);
            }
        }
        lootCommandCallback4.accept($$5);
        return $$5.size();
    }

    private static int dropInWorld(CommandSourceStack commandSourceStack0, Vec3 vec1, List<ItemStack> listItemStack2, LootCommand.Callback lootCommandCallback3) throws CommandSyntaxException {
        ServerLevel $$4 = commandSourceStack0.getLevel();
        listItemStack2.forEach(p_137884_ -> {
            ItemEntity $$3 = new ItemEntity($$4, vec1.x, vec1.y, vec1.z, p_137884_.copy());
            $$3.setDefaultPickUpDelay();
            $$4.addFreshEntity($$3);
        });
        lootCommandCallback3.accept(listItemStack2);
        return listItemStack2.size();
    }

    private static void callback(CommandSourceStack commandSourceStack0, List<ItemStack> listItemStack1) {
        if (listItemStack1.size() == 1) {
            ItemStack $$2 = (ItemStack) listItemStack1.get(0);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.drop.success.single", $$2.getCount(), $$2.getDisplayName()), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.drop.success.multiple", listItemStack1.size()), false);
        }
    }

    private static void callback(CommandSourceStack commandSourceStack0, List<ItemStack> listItemStack1, ResourceLocation resourceLocation2) {
        if (listItemStack1.size() == 1) {
            ItemStack $$3 = (ItemStack) listItemStack1.get(0);
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.drop.success.single_with_table", $$3.getCount(), $$3.getDisplayName(), resourceLocation2), false);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.drop.success.multiple_with_table", listItemStack1.size(), resourceLocation2), false);
        }
    }

    private static ItemStack getSourceHandItem(CommandSourceStack commandSourceStack0, EquipmentSlot equipmentSlot1) throws CommandSyntaxException {
        Entity $$2 = commandSourceStack0.getEntityOrException();
        if ($$2 instanceof LivingEntity) {
            return ((LivingEntity) $$2).getItemBySlot(equipmentSlot1);
        } else {
            throw ERROR_NO_HELD_ITEMS.create($$2.getDisplayName());
        }
    }

    private static int dropBlockLoot(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, BlockPos blockPos1, ItemStack itemStack2, LootCommand.DropConsumer lootCommandDropConsumer3) throws CommandSyntaxException {
        CommandSourceStack $$4 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
        ServerLevel $$5 = $$4.getLevel();
        BlockState $$6 = $$5.m_8055_(blockPos1);
        BlockEntity $$7 = $$5.m_7702_(blockPos1);
        LootParams.Builder $$8 = new LootParams.Builder($$5).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos1)).withParameter(LootContextParams.BLOCK_STATE, $$6).withOptionalParameter(LootContextParams.BLOCK_ENTITY, $$7).withOptionalParameter(LootContextParams.THIS_ENTITY, $$4.getEntity()).withParameter(LootContextParams.TOOL, itemStack2);
        List<ItemStack> $$9 = $$6.m_287290_($$8);
        return lootCommandDropConsumer3.accept(commandContextCommandSourceStack0, $$9, p_278915_ -> callback($$4, p_278915_, $$6.m_60734_().m_60589_()));
    }

    private static int dropKillLoot(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, Entity entity1, LootCommand.DropConsumer lootCommandDropConsumer2) throws CommandSyntaxException {
        if (!(entity1 instanceof LivingEntity)) {
            throw ERROR_NO_LOOT_TABLE.create(entity1.getDisplayName());
        } else {
            ResourceLocation $$3 = ((LivingEntity) entity1).getLootTable();
            CommandSourceStack $$4 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
            LootParams.Builder $$5 = new LootParams.Builder($$4.getLevel());
            Entity $$6 = $$4.getEntity();
            if ($$6 instanceof Player $$7) {
                $$5.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, $$7);
            }
            $$5.withParameter(LootContextParams.DAMAGE_SOURCE, entity1.damageSources().magic());
            $$5.withOptionalParameter(LootContextParams.DIRECT_KILLER_ENTITY, $$6);
            $$5.withOptionalParameter(LootContextParams.KILLER_ENTITY, $$6);
            $$5.withParameter(LootContextParams.THIS_ENTITY, entity1);
            $$5.withParameter(LootContextParams.ORIGIN, $$4.getPosition());
            LootParams $$8 = $$5.create(LootContextParamSets.ENTITY);
            LootTable $$9 = $$4.getServer().getLootData().m_278676_($$3);
            List<ItemStack> $$10 = $$9.getRandomItems($$8);
            return lootCommandDropConsumer2.accept(commandContextCommandSourceStack0, $$10, p_137975_ -> callback($$4, p_137975_, $$3));
        }
    }

    private static int dropChestLoot(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ResourceLocation resourceLocation1, LootCommand.DropConsumer lootCommandDropConsumer2) throws CommandSyntaxException {
        CommandSourceStack $$3 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
        LootParams $$4 = new LootParams.Builder($$3.getLevel()).withOptionalParameter(LootContextParams.THIS_ENTITY, $$3.getEntity()).withParameter(LootContextParams.ORIGIN, $$3.getPosition()).create(LootContextParamSets.CHEST);
        return drop(commandContextCommandSourceStack0, resourceLocation1, $$4, lootCommandDropConsumer2);
    }

    private static int dropFishingLoot(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ResourceLocation resourceLocation1, BlockPos blockPos2, ItemStack itemStack3, LootCommand.DropConsumer lootCommandDropConsumer4) throws CommandSyntaxException {
        CommandSourceStack $$5 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
        LootParams $$6 = new LootParams.Builder($$5.getLevel()).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos2)).withParameter(LootContextParams.TOOL, itemStack3).withOptionalParameter(LootContextParams.THIS_ENTITY, $$5.getEntity()).create(LootContextParamSets.FISHING);
        return drop(commandContextCommandSourceStack0, resourceLocation1, $$6, lootCommandDropConsumer4);
    }

    private static int drop(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, ResourceLocation resourceLocation1, LootParams lootParams2, LootCommand.DropConsumer lootCommandDropConsumer3) throws CommandSyntaxException {
        CommandSourceStack $$4 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
        LootTable $$5 = $$4.getServer().getLootData().m_278676_(resourceLocation1);
        List<ItemStack> $$6 = $$5.getRandomItems(lootParams2);
        return lootCommandDropConsumer3.accept(commandContextCommandSourceStack0, $$6, p_137997_ -> callback($$4, p_137997_));
    }

    @FunctionalInterface
    interface Callback {

        void accept(List<ItemStack> var1) throws CommandSyntaxException;
    }

    @FunctionalInterface
    interface DropConsumer {

        int accept(CommandContext<CommandSourceStack> var1, List<ItemStack> var2, LootCommand.Callback var3) throws CommandSyntaxException;
    }

    @FunctionalInterface
    interface TailProvider {

        ArgumentBuilder<CommandSourceStack, ?> construct(ArgumentBuilder<CommandSourceStack, ?> var1, LootCommand.DropConsumer var2);
    }
}