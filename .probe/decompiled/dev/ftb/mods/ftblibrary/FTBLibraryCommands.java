package dev.ftb.mods.ftblibrary;

import com.google.gson.Gson;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.platform.Mod;
import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.RegistrarManager;
import dev.ftb.mods.ftblibrary.net.EditConfigPacket;
import dev.ftb.mods.ftblibrary.net.EditNBTPacket;
import dev.ftb.mods.ftblibrary.ui.misc.UITesting;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.SetEnchantmentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class FTBLibraryCommands {

    public static final Map<UUID, CompoundTag> EDITING_NBT = new HashMap();

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection type) {
        LiteralArgumentBuilder<CommandSourceStack> command = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("ftblibrary").requires(commandSource -> commandSource.hasPermission(2))).then(Commands.literal("gamemode").executes(context -> {
            if (!((CommandSourceStack) context.getSource()).getPlayerOrException().isCreative()) {
                ((CommandSourceStack) context.getSource()).getPlayerOrException().setGameMode(GameType.CREATIVE);
            } else {
                ((CommandSourceStack) context.getSource()).getPlayerOrException().setGameMode(GameType.SURVIVAL);
            }
            return 1;
        }))).then(Commands.literal("rain").executes(context -> {
            if (((CommandSourceStack) context.getSource()).getLevel().m_46471_()) {
                ((CommandSourceStack) context.getSource()).getLevel().setWeatherParameters(6000, 0, false, false);
            } else {
                ((CommandSourceStack) context.getSource()).getLevel().setWeatherParameters(0, 6000, true, false);
            }
            return 1;
        }))).then(Commands.literal("day").executes(context -> {
            long addDay = (24000L - ((CommandSourceStack) context.getSource()).getLevel().m_46468_() % 24000L + 6000L) % 24000L;
            if (addDay != 0L) {
                for (ServerLevel world : ((CommandSourceStack) context.getSource()).getServer().getAllLevels()) {
                    world.setDayTime(world.m_46468_() + addDay);
                }
            }
            return 1;
        }))).then(Commands.literal("night").executes(context -> {
            long addDay = (24000L - ((CommandSourceStack) context.getSource()).getLevel().m_46468_() % 24000L + 18000L) % 24000L;
            if (addDay != 0L) {
                for (ServerLevel world : ((CommandSourceStack) context.getSource()).getServer().getAllLevels()) {
                    world.setDayTime(world.m_46468_() + addDay);
                }
            }
            return 1;
        }))).then(((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("nbtedit").then(Commands.literal("block").then(Commands.argument("pos", BlockPosArgument.blockPos()).executes(context -> editNBT(context, (info, tag) -> {
            BlockPos pos = BlockPosArgument.getSpawnablePos(context, "pos");
            BlockEntity blockEntity = ((CommandSourceStack) context.getSource()).getLevel().m_7702_(pos);
            if (blockEntity != null) {
                info.putString("type", "block");
                info.putInt("x", pos.m_123341_());
                info.putInt("y", pos.m_123342_());
                info.putInt("z", pos.m_123343_());
                tag.merge(blockEntity.saveWithFullMetadata());
                tag.remove("x");
                tag.remove("y");
                tag.remove("z");
                info.putString("id", tag.getString("id"));
                tag.remove("id");
                ListTag list = new ListTag();
                addInfo(list, Component.literal("Class"), Component.literal(blockEntity.getClass().getName()));
                ResourceLocation key = RegistrarManager.getId(blockEntity.getType(), Registries.BLOCK_ENTITY_TYPE);
                addInfo(list, Component.literal("ID"), Component.literal(key == null ? "null" : key.toString()));
                addInfo(list, Component.literal("Block"), Component.literal(String.valueOf(RegistrarManager.getId(blockEntity.getBlockState().m_60734_(), Registries.BLOCK))));
                addInfo(list, Component.literal("Block Class"), Component.literal(blockEntity.getBlockState().m_60734_().getClass().getName()));
                addInfo(list, Component.literal("Position"), Component.literal("[" + pos.m_123341_() + ", " + pos.m_123342_() + ", " + pos.m_123343_() + "]"));
                addInfo(list, Component.literal("Mod"), Component.literal(key == null ? "null" : (String) Platform.getOptionalMod(key.getNamespace()).map(Mod::getName).orElse("Unknown")));
                addInfo(list, Component.literal("Ticking"), Component.literal(blockEntity instanceof TickingBlockEntity ? "true" : "false"));
                info.put("text", list);
                Component title = blockEntity instanceof Nameable ? ((Nameable) blockEntity).getDisplayName() : null;
                if (title == null) {
                    title = Component.literal(blockEntity.getClass().getSimpleName());
                }
                info.putString("title", Component.Serializer.toJson(title));
            }
        }))))).then(Commands.literal("entity").then(Commands.argument("entity", EntityArgument.entity()).executes(context -> editNBT(context, (info, tag) -> {
            Entity entity = EntityArgument.getEntity(context, "entity");
            if (!(entity instanceof Player)) {
                info.putString("type", "entity");
                info.putInt("id", entity.getId());
                entity.save(tag);
                ListTag list = new ListTag();
                addInfo(list, Component.literal("Class"), Component.literal(entity.getClass().getName()));
                ResourceLocation key = RegistrarManager.getId(entity.getType(), Registries.ENTITY_TYPE);
                addInfo(list, Component.literal("ID"), Component.literal(key == null ? "null" : key.toString()));
                addInfo(list, Component.literal("Mod"), Component.literal(key == null ? "null" : (String) Platform.getOptionalMod(key.getNamespace()).map(Mod::getName).orElse("Unknown")));
                info.put("text", list);
                info.putString("title", Component.Serializer.toJson(entity.getDisplayName()));
            }
        }))))).then(Commands.literal("player").then(Commands.argument("player", EntityArgument.player()).executes(context -> editNBT(context, (info, tag) -> {
            ServerPlayer player = EntityArgument.getPlayer(context, "player");
            info.putString("type", "player");
            info.putUUID("id", player.m_20148_());
            player.m_20240_(tag);
            tag.remove("id");
            ListTag list = new ListTag();
            addInfo(list, Component.literal("Name"), player.m_7755_());
            addInfo(list, Component.literal("Display Name"), player.m_5446_());
            addInfo(list, Component.literal("UUID"), Component.literal(player.m_20148_().toString()));
            info.put("text", list);
            info.putString("title", Component.Serializer.toJson(player.m_5446_()));
        }))))).then(Commands.literal("item").executes(context -> editNBT(context, (info, tag) -> {
            ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
            info.putString("type", "item");
            player.m_21120_(InteractionHand.MAIN_HAND).save(tag);
        }))))).then(Commands.literal("generate_loot_tables").executes(FTBLibraryCommands::generateLootTables))).then(((LiteralArgumentBuilder) Commands.literal("clientconfig").requires(CommandSourceStack::m_230897_)).executes(context -> {
            new EditConfigPacket(true).sendTo((ServerPlayer) Objects.requireNonNull(((CommandSourceStack) context.getSource()).getPlayer()));
            return 1;
        }));
        if (Platform.isDevelopmentEnvironment()) {
            command.then(Commands.literal("test_screen").executes(context -> {
                if (((CommandSourceStack) context.getSource()).getServer().isDedicatedServer()) {
                    ((CommandSourceStack) context.getSource()).sendFailure(Component.literal("Can't do this on dedicated server!").withStyle(ChatFormatting.RED));
                } else {
                    UITesting.openTestScreen();
                }
                return 1;
            }));
        }
        dispatcher.register(command);
    }

    @Deprecated
    private static int generateLootTables(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        HitResult pick = player.m_19907_(30.0, 1.0F, true);
        if (pick.getType() != HitResult.Type.BLOCK) {
            source.sendFailure(Component.literal("You must be facing a valid block"));
            return 0;
        } else {
            BlockHitResult trace = (BlockHitResult) pick;
            ServerLevel level = source.getLevel();
            BlockEntity blockEntity = level.m_7702_(trace.getBlockPos());
            if (!(blockEntity instanceof ChestBlockEntity) && !(blockEntity instanceof BarrelBlockEntity)) {
                source.sendFailure(Component.literal("You must be facing a chest or barrel"));
                return 0;
            } else {
                RandomizableContainerBlockEntity chest = (RandomizableContainerBlockEntity) blockEntity;
                ArrayList<ItemStack> items = new ArrayList();
                for (int i = 0; i < chest.m_6643_(); i++) {
                    ItemStack item = chest.getItem(i);
                    if (!item.isEmpty()) {
                        items.add(item);
                    }
                }
                try {
                    LootPool.Builder tablePool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F));
                    for (ItemStack e : items) {
                        LootPoolSingletonContainer.Builder<?> itemBuilder = LootItem.lootTableItem(e.getItem()).setWeight(1);
                        if (e.getCount() > 1) {
                            itemBuilder.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, (float) e.getCount())));
                        }
                        label103: {
                            CompoundTag itemTag;
                            itemTag = e.getOrCreateTag();
                            label80: if (!itemTag.contains("copy") && !(e.getItem() instanceof EnchantedBookItem)) {
                                if (e.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof ShulkerBoxBlock) {
                                    break label80;
                                }
                                if (e.isEnchanted()) {
                                    if (itemTag.contains("level")) {
                                        String range = itemTag.getString("level");
                                        if (range.contains(",")) {
                                            String[] split = range.split(",");
                                            itemBuilder.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(Float.parseFloat(split[0]), Float.parseFloat(split[1]))));
                                        } else {
                                            itemBuilder.apply(EnchantWithLevelsFunction.enchantWithLevels(UniformGenerator.between(0.0F, Float.parseFloat(range))));
                                        }
                                    } else if (itemTag.contains("set")) {
                                        SetEnchantmentsFunction.Builder enchantBuilder = new SetEnchantmentsFunction.Builder();
                                        EnchantmentHelper.getEnchantments(e).forEach((enchant, l) -> enchantBuilder.withEnchantment(enchant, ConstantValue.exactly((float) l.intValue())));
                                        itemBuilder.apply(enchantBuilder);
                                    } else {
                                        itemBuilder.apply(EnchantRandomlyFunction.randomApplicableEnchantment());
                                    }
                                }
                                break label103;
                            }
                            itemBuilder.apply(SetNbtFunction.setTag(itemTag));
                        }
                        tablePool.add(itemBuilder);
                    }
                    LootTable.Builder lootTable = LootTable.lootTable().withPool(tablePool);
                    Gson gson = Deserializers.createLootTableSerializer().setPrettyPrinting().create();
                    String output = gson.toJson(lootTable.build());
                    Path path = source.getServer().getServerDirectory().toPath();
                    Path outputDir = path.resolve("moddata/ftb-library/generated/");
                    String outputFileName = "loot-" + (blockEntity instanceof ChestBlockEntity ? "chest" : "barrel") + "-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME).replaceAll(":|\\.", "_") + ".json";
                    Component customName = chest.m_7770_();
                    if (customName != null && customName.getString().contains("/") && !customName.getString().contains("..")) {
                        String chestPathName = customName.getString();
                        if (chestPathName.chars().filter(c -> c == 47).count() == 2L) {
                            String[] pathParts = chestPathName.split("/");
                            outputFileName = String.format("%s.json", pathParts[2]);
                            outputDir = path.resolve(String.format("kubejs/%s/loot_tables/%s/", pathParts[0], pathParts[1]));
                        }
                    }
                    if (!Files.exists(outputDir, new LinkOption[0])) {
                        Files.createDirectories(outputDir);
                    }
                    Files.writeString(outputDir.resolve(outputFileName), output);
                    Path dir = outputDir.resolve(outputFileName);
                    source.sendSuccess(() -> Component.literal("Loot table stored at " + dir.toString().replace(path.toAbsolutePath().toString(), "")), true);
                    return 1;
                } catch (Exception var19) {
                    source.sendFailure(Component.literal("Something went wrong, check the logs"));
                    FTBLibrary.LOGGER.error(var19);
                    return 0;
                }
            }
        }
    }

    private static void addInfo(ListTag list, Component key, Component value) {
        list.add(StringTag.valueOf(Component.Serializer.toJson(key.copy().withStyle(ChatFormatting.BLUE).append(": ").append(value.copy().withStyle(ChatFormatting.GOLD)))));
    }

    private static int editNBT(CommandContext<CommandSourceStack> context, FTBLibraryCommands.NBTEditCallback data) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
        CompoundTag info = new CompoundTag();
        CompoundTag tag = new CompoundTag();
        data.accept(info, tag);
        if (!info.isEmpty()) {
            EDITING_NBT.put(player.m_20148_(), info);
            new EditNBTPacket(info, tag).sendTo(player);
            return 1;
        } else {
            return 0;
        }
    }

    private interface NBTEditCallback {

        void accept(CompoundTag var1, CompoundTag var2) throws CommandSyntaxException;
    }
}