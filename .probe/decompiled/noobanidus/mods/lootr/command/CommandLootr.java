package noobanidus.mods.lootr.command;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.server.ServerLifecycleHooks;
import noobanidus.mods.lootr.api.blockentity.ILootBlockEntity;
import noobanidus.mods.lootr.block.LootrBarrelBlock;
import noobanidus.mods.lootr.block.LootrChestBlock;
import noobanidus.mods.lootr.block.LootrShulkerBlock;
import noobanidus.mods.lootr.block.entities.LootrInventoryBlockEntity;
import noobanidus.mods.lootr.config.ConfigManager;
import noobanidus.mods.lootr.data.DataStorage;
import noobanidus.mods.lootr.entity.LootrChestMinecartEntity;
import noobanidus.mods.lootr.init.ModBlocks;
import noobanidus.mods.lootr.util.ChestUtil;

public class CommandLootr {

    private static final Map<String, UUID> profileMap = new HashMap();

    private static List<ResourceLocation> tables = null;

    private static List<String> tableNames = null;

    private final CommandDispatcher<CommandSourceStack> dispatcher;

    public CommandLootr(CommandDispatcher<CommandSourceStack> dispatcher) {
        this.dispatcher = dispatcher;
    }

    private static List<ResourceLocation> getTables() {
        if (tables == null) {
            tables = new ArrayList(BuiltInLootTables.all());
            tableNames = (List<String>) tables.stream().map(ResourceLocation::toString).collect(Collectors.toList());
        }
        return tables;
    }

    private static List<String> getProfiles() {
        return Lists.newArrayList(ServerLifecycleHooks.getCurrentServer().getProfileCache().profilesByName.keySet());
    }

    private static List<String> getTableNames() {
        getTables();
        return tableNames;
    }

    public static void createBlock(CommandSourceStack c, @Nullable Block block, @Nullable ResourceLocation incomingTable) {
        Level world = c.getLevel();
        Vec3 incomingPos = c.getPosition();
        BlockPos pos = new BlockPos((int) incomingPos.x, (int) incomingPos.y, (int) incomingPos.z);
        ResourceLocation table;
        if (incomingTable == null) {
            table = (ResourceLocation) getTables().get(world.getRandom().nextInt(getTables().size()));
        } else {
            table = incomingTable;
        }
        if (block == null) {
            LootrChestMinecartEntity cart = new LootrChestMinecartEntity(world, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5);
            Entity e = c.getEntity();
            if (e != null) {
                cart.m_146922_(e.getYRot());
            }
            cart.m_38236_(table, world.getRandom().nextLong());
            world.m_7967_(cart);
            c.sendSuccess(() -> Component.translatable("lootr.commands.summon", ComponentUtils.wrapInSquareBrackets(Component.translatable("lootr.commands.blockpos", pos.m_123341_(), pos.m_123342_(), pos.m_123343_()).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN)).withBold(true))), table.toString()), false);
        } else {
            BlockState placementState = block.defaultBlockState();
            Entity e = c.getEntity();
            if (e != null) {
                EnumProperty<Direction> prop = null;
                Direction dir = Direction.orderedByNearest(e)[0].getOpposite();
                if (placementState.m_61138_(LootrBarrelBlock.f_49042_)) {
                    prop = LootrBarrelBlock.f_49042_;
                } else if (placementState.m_61138_(LootrChestBlock.f_51478_)) {
                    prop = LootrChestBlock.f_51478_;
                    dir = e.getDirection().getOpposite();
                } else if (placementState.m_61138_(LootrShulkerBlock.f_56183_)) {
                    prop = LootrShulkerBlock.f_56183_;
                }
                if (prop != null) {
                    placementState = (BlockState) placementState.m_61124_(prop, dir);
                }
            }
            world.setBlock(pos, placementState, 2);
            RandomizableContainerBlockEntity.setLootTable(world, world.getRandom(), pos, table);
            c.sendSuccess(() -> Component.translatable("lootr.commands.create", Component.translatable(block.getDescriptionId()), ComponentUtils.wrapInSquareBrackets(Component.translatable("lootr.commands.blockpos", pos.m_123341_(), pos.m_123342_(), pos.m_123343_()).setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GREEN)).withBold(true))), table.toString()), false);
        }
    }

    public CommandLootr register() {
        this.dispatcher.register(this.builder((LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("lootr").requires(p -> p.hasPermission(2))));
        return this;
    }

    private RequiredArgumentBuilder<CommandSourceStack, ResourceLocation> suggestTables() {
        return Commands.argument("table", ResourceLocationArgument.id()).suggests((c, build) -> SharedSuggestionProvider.suggest(getTableNames(), build));
    }

    private RequiredArgumentBuilder<CommandSourceStack, String> suggestProfiles() {
        return Commands.argument("profile", StringArgumentType.string()).suggests((c, build) -> SharedSuggestionProvider.suggest(getProfiles(), build));
    }

    public LiteralArgumentBuilder<CommandSourceStack> builder(LiteralArgumentBuilder<CommandSourceStack> builder) {
        builder.executes(c -> {
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.translatable("lootr.commands.usage"), false);
            return 1;
        });
        builder.then(((LiteralArgumentBuilder) Commands.literal("barrel").executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.BARREL.get(), null);
            return 1;
        })).then(this.suggestTables().executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.BARREL.get(), ResourceLocationArgument.getId(c, "table"));
            return 1;
        })));
        builder.then(((LiteralArgumentBuilder) Commands.literal("trapped_chest").executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.TRAPPED_CHEST.get(), null);
            return 1;
        })).then(this.suggestTables().executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.TRAPPED_CHEST.get(), ResourceLocationArgument.getId(c, "table"));
            return 1;
        })));
        builder.then(((LiteralArgumentBuilder) Commands.literal("chest").executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.CHEST.get(), null);
            return 1;
        })).then(this.suggestTables().executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.CHEST.get(), ResourceLocationArgument.getId(c, "table"));
            return 1;
        })));
        builder.then(((LiteralArgumentBuilder) Commands.literal("shulker").executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.SHULKER.get(), null);
            return 1;
        })).then(this.suggestTables().executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), ModBlocks.SHULKER.get(), ResourceLocationArgument.getId(c, "table"));
            return 1;
        })));
        builder.then(((LiteralArgumentBuilder) Commands.literal("clear").executes(c -> {
            ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Must provide player name."), true);
            return 1;
        })).then(this.suggestProfiles().executes(c -> {
            String playerName = StringArgumentType.getString(c, "profile");
            Optional<GameProfile> opt_profile = ((CommandSourceStack) c.getSource()).getServer().getProfileCache().get(playerName);
            if (!opt_profile.isPresent()) {
                ((CommandSourceStack) c.getSource()).sendFailure(Component.literal("Invalid player name: " + playerName + ", profile not found in the cache."));
                return 0;
            } else {
                GameProfile profile = (GameProfile) opt_profile.get();
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal(DataStorage.clearInventories(profile.getId()) ? "Cleared stored inventories for " + playerName : "No stored inventories for " + playerName + " to clear"), true);
                return 1;
            }
        })));
        builder.then(((LiteralArgumentBuilder) Commands.literal("cart").executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), null, null);
            return 1;
        })).then(this.suggestTables().executes(c -> {
            createBlock((CommandSourceStack) c.getSource(), null, ResourceLocationArgument.getId(c, "table"));
            return 1;
        })));
        builder.then(Commands.literal("custom").executes(c -> {
            BlockPos pos = BlockPos.containing(((CommandSourceStack) c.getSource()).getPosition());
            Level world = ((CommandSourceStack) c.getSource()).getLevel();
            BlockState state = world.getBlockState(pos);
            if (!state.m_60713_(Blocks.CHEST) && !state.m_60713_(Blocks.BARREL)) {
                pos = pos.below();
                state = world.getBlockState(pos);
            }
            if (!state.m_60713_(Blocks.CHEST) && !state.m_60713_(Blocks.BARREL)) {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Please stand on the chest or barrel you wish to convert."), false);
            } else {
                NonNullList<ItemStack> reference;
                BlockState newState;
                if (state.m_60713_(Blocks.CHEST)) {
                    reference = ((ChestBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).items;
                    newState = (BlockState) ((BlockState) ModBlocks.INVENTORY.get().m_49966_().m_61124_(ChestBlock.FACING, (Direction) state.m_61143_(ChestBlock.FACING))).m_61124_(ChestBlock.WATERLOGGED, (Boolean) state.m_61143_(ChestBlock.WATERLOGGED));
                } else {
                    Direction facing = (Direction) state.m_61143_(BarrelBlock.FACING);
                    if (facing == Direction.UP || facing == Direction.DOWN) {
                        facing = Direction.NORTH;
                    }
                    reference = ((BarrelBlockEntity) Objects.requireNonNull(world.getBlockEntity(pos))).items;
                    newState = (BlockState) ModBlocks.INVENTORY.get().m_49966_().m_61124_(ChestBlock.FACING, facing);
                }
                NonNullList<ItemStack> custom = ChestUtil.copyItemList(reference);
                world.removeBlockEntity(pos);
                world.setBlockAndUpdate(pos, newState);
                if (world.getBlockEntity(pos) instanceof LootrInventoryBlockEntity inventory) {
                    inventory.setCustomInventory(custom);
                    inventory.m_6596_();
                } else {
                    ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Unable to convert chest, BlockState is not a Lootr Inventory block."), false);
                }
            }
            return 1;
        }));
        builder.then(Commands.literal("id").executes(c -> {
            BlockPos pos = BlockPos.containing(((CommandSourceStack) c.getSource()).getPosition());
            Level world = ((CommandSourceStack) c.getSource()).getLevel();
            BlockEntity te = world.getBlockEntity(pos);
            if (!(te instanceof ILootBlockEntity)) {
                pos = pos.below();
                te = world.getBlockEntity(pos);
            }
            if (te instanceof ILootBlockEntity ibe) {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("The ID of this inventory is: " + ibe.getTileId().toString()), false);
            } else {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Please stand on a valid Lootr container."), false);
            }
            return 1;
        }));
        builder.then(Commands.literal("refresh").executes(c -> {
            BlockPos pos = BlockPos.containing(((CommandSourceStack) c.getSource()).getPosition());
            Level level = ((CommandSourceStack) c.getSource()).getLevel();
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof ILootBlockEntity)) {
                pos = pos.below();
                be = level.getBlockEntity(pos);
            }
            if (be instanceof ILootBlockEntity ibe) {
                DataStorage.setRefreshing(((ILootBlockEntity) be).getTileId(), ConfigManager.REFRESH_VALUE.get());
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Container with ID " + ibe.getTileId() + " has been set to refresh with a delay of " + ConfigManager.REFRESH_VALUE.get()), false);
            } else {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Please stand on a valid Lootr container."), false);
            }
            return 1;
        }));
        builder.then(Commands.literal("decay").executes(c -> {
            BlockPos pos = BlockPos.containing(((CommandSourceStack) c.getSource()).getPosition());
            Level level = ((CommandSourceStack) c.getSource()).getLevel();
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof ILootBlockEntity)) {
                pos = pos.below();
                be = level.getBlockEntity(pos);
            }
            if (be instanceof ILootBlockEntity ibe) {
                DataStorage.setDecaying(ibe.getTileId(), ConfigManager.DECAY_VALUE.get());
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Container with ID " + ibe.getTileId() + " has been set to decay with a delay of " + ConfigManager.DECAY_VALUE.get()), false);
            } else {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Please stand on a valid Lootr container."), false);
            }
            return 1;
        }));
        builder.then(Commands.literal("openers").then(Commands.argument("location", Vec3Argument.vec3()).executes(c -> {
            BlockPos position = Vec3Argument.getCoordinates(c, "location").getBlockPos((CommandSourceStack) c.getSource());
            Level world = ((CommandSourceStack) c.getSource()).getLevel();
            BlockEntity tile = world.getBlockEntity(position);
            if (tile instanceof ILootBlockEntity ibe) {
                Set<UUID> openers = ((ILootBlockEntity) tile).getOpeners();
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("Tile at location " + position + " has " + openers.size() + " openers. UUIDs as follows:"), true);
                for (UUID uuid : openers) {
                    Optional<GameProfile> prof = ((CommandSourceStack) c.getSource()).getServer().getProfileCache().get(uuid);
                    ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("UUID: " + uuid.toString() + ", user profile: " + (prof.isPresent() ? ((GameProfile) prof.get()).getName() : "null")), true);
                }
            } else {
                ((CommandSourceStack) c.getSource()).sendSuccess(() -> Component.literal("No Lootr tile exists at location: " + position), false);
            }
            return 1;
        })));
        return builder;
    }
}