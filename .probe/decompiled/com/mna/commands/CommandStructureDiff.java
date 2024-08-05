package com.mna.commands;

import com.google.gson.JsonObject;
import com.mna.ManaAndArtifice;
import com.mna.recipes.multiblock.MultiblockConfiguration;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class CommandStructureDiff {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("structurediff").then(Commands.argument("baseline", StringArgumentType.string()).then(Commands.argument("variation", StringArgumentType.string()).executes(context -> computeDiff((CommandSourceStack) context.getSource(), StringArgumentType.getString(context, "baseline"), StringArgumentType.getString(context, "variation"))))));
    }

    private static int computeDiff(CommandSourceStack source, String firstStructure, String secondStructure) {
        ServerLevel sw = source.getLevel();
        ArrayList<BlockState> blockStates = new ArrayList();
        MultiblockConfiguration first = MultiblockConfiguration.loadStructure(sw.getStructureManager(), new ResourceLocation(firstStructure), blockStates);
        MultiblockConfiguration second = MultiblockConfiguration.loadStructure(sw.getStructureManager(), new ResourceLocation(secondStructure), blockStates);
        if (!first.getIsValid()) {
            source.sendFailure(Component.literal("Unable to locate first structure"));
            return 0;
        } else if (!second.getIsValid()) {
            source.sendFailure(Component.literal("Unable to locate second structure"));
            return 0;
        } else {
            JsonObject diff = first.computeRecipeDiff(second, blockStates);
            Supplier<Component> response = () -> Component.literal(diff.toString());
            ManaAndArtifice.LOGGER.always().log(diff.toString());
            source.sendSuccess(response, true);
            return 1;
        }
    }
}