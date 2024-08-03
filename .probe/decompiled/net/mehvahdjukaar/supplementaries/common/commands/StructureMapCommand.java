package net.mehvahdjukaar.supplementaries.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.entities.trades.AdventurerMapsHandler;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceOrTagKeyArgument;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureMapCommand {

    private static final DynamicCommandExceptionType ERROR_STRUCTURE_INVALID = new DynamicCommandExceptionType(object -> Component.translatable("commands.locate.structure.invalid", object));

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        return ((LiteralArgumentBuilder) Commands.literal("structure_map").requires(cs -> cs.hasPermission(2))).then(((RequiredArgumentBuilder) Commands.argument("structure", ResourceOrTagKeyArgument.resourceOrTagKey(Registries.STRUCTURE)).executes(c -> giveMap(c, 2))).then(Commands.argument("zoom", IntegerArgumentType.integer()).executes(c -> giveMap(c, IntegerArgumentType.getInteger(c, "zoom")))));
    }

    public static int giveMap(CommandContext<CommandSourceStack> context, int zoom) throws CommandSyntaxException {
        CommandSourceStack source = (CommandSourceStack) context.getSource();
        ServerLevel level = source.getLevel();
        ResourceOrTagKeyArgument.Result<Structure> structure = ResourceOrTagKeyArgument.getResourceOrTagKey(context, "structure", Registries.STRUCTURE, ERROR_STRUCTURE_INVALID);
        Registry<Structure> registry = level.m_9598_().registryOrThrow(Registries.STRUCTURE);
        HolderSet<Structure> holderSet = (HolderSet<Structure>) getHolders(structure, registry).orElseThrow(() -> ERROR_STRUCTURE_INVALID.create(structure.asPrintable()));
        ServerPlayer p = source.getPlayer();
        if (p != null) {
            ItemStack item = AdventurerMapsHandler.createMapOrQuill(level, p.m_20097_(), holderSet, 150, true, zoom, null, null, 0);
            p.m_36356_(item);
        }
        return 0;
    }

    private static Optional<? extends HolderSet<Structure>> getHolders(ResourceOrTagKeyArgument.Result<Structure> structure, Registry<Structure> structureRegistry) {
        return (Optional<? extends HolderSet<Structure>>) structure.unwrap().map(res -> structureRegistry.getHolder(res).map(xva$0 -> HolderSet.direct(xva$0)), structureRegistry::m_203431_);
    }
}