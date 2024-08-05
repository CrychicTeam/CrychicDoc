package com.simibubi.create.infrastructure.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.Collections;
import java.util.function.Predicate;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLLoader;

public class AllCommands {

    public static final Predicate<CommandSourceStack> SOURCE_IS_PLAYER = cs -> cs.getEntity() instanceof Player;

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> util = buildUtilityCommands();
        LiteralArgumentBuilder<CommandSourceStack> root = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("create").requires(cs -> cs.hasPermission(0))).then(new ToggleDebugCommand().register())).then(FabulousWarningCommand.register())).then(OverlayConfigCommand.register())).then(DumpRailwaysCommand.register())).then(FixLightingCommand.register())).then(DebugInfoCommand.register())).then(HighlightCommand.register())).then(KillTrainCommand.register())).then(PassengerCommand.register())).then(CouplingCommand.register())).then(ConfigCommand.register())).then(PonderCommand.register())).then(CloneCommand.register())).then(GlueCommand.register())).then(util);
        if (!FMLLoader.isProduction() && FMLLoader.getDist() == Dist.CLIENT) {
            root.then(CreateTestCommand.register());
        }
        LiteralCommandNode<CommandSourceStack> createRoot = dispatcher.register(root);
        createRoot.addChild(buildRedirect("u", util));
        CommandNode<CommandSourceStack> c = dispatcher.findNode(Collections.singleton("c"));
        if (c == null) {
            dispatcher.getRoot().addChild(buildRedirect("c", createRoot));
        }
    }

    private static LiteralCommandNode<CommandSourceStack> buildUtilityCommands() {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("util").then(ReplaceInCommandBlocksCommand.register())).then(ClearBufferCacheCommand.register())).then(CameraDistanceCommand.register())).then(CameraAngleCommand.register())).then(FlySpeedCommand.register())).build();
    }

    public static LiteralCommandNode<CommandSourceStack> buildRedirect(String alias, LiteralCommandNode<CommandSourceStack> destination) {
        LiteralArgumentBuilder<CommandSourceStack> builder = (LiteralArgumentBuilder<CommandSourceStack>) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) LiteralArgumentBuilder.literal(alias).requires(destination.getRequirement())).forward(destination.getRedirect(), destination.getRedirectModifier(), destination.isFork())).executes(destination.getCommand());
        for (CommandNode<CommandSourceStack> child : destination.getChildren()) {
            builder.then(child);
        }
        return builder.build();
    }
}