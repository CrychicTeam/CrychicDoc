package io.github.apace100.origins.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.capabilities.IOriginContainer;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.api.registry.OriginsBuiltinRegistries;
import io.github.edwinmindcraft.origins.common.OriginsCommon;
import io.github.edwinmindcraft.origins.common.network.S2COpenOriginScreen;
import io.github.edwinmindcraft.origins.common.registry.OriginRegisters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.IForgeRegistry;

public class OriginCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("origin").requires(cs -> cs.hasPermission(2))).then(Commands.literal("set").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("layer", LayerArgumentType.layer()).then(Commands.argument("origin", OriginArgumentType.origin()).executes(OriginCommand::setOrigin)))))).then(Commands.literal("has").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("layer", LayerArgumentType.layer()).then(Commands.argument("origin", OriginArgumentType.origin()).executes(OriginCommand::hasOrigin)))))).then(Commands.literal("get").then(Commands.argument("target", EntityArgument.player()).then(Commands.argument("layer", LayerArgumentType.layer()).executes(OriginCommand::getOrigin))))).then(((LiteralArgumentBuilder) Commands.literal("gui").executes(command -> openMultipleLayerScreens(command, OriginCommand.TargetType.INVOKER))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(command -> openMultipleLayerScreens(command, OriginCommand.TargetType.SPECIFY))).then(Commands.argument("layer", LayerArgumentType.layer()).executes(OriginCommand::openSingleLayerScreen))))).then(((LiteralArgumentBuilder) Commands.literal("random").executes(command -> randomizeOrigins(command, OriginCommand.TargetType.INVOKER))).then(((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(command -> randomizeOrigins(command, OriginCommand.TargetType.SPECIFY))).then(Commands.argument("layer", LayerArgumentType.layer()).executes(OriginCommand::randomizeOrigin)))));
    }

    private static int setOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
        ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        ResourceKey<Origin> origin = OriginArgumentType.getOrigin(commandContext, "origin");
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        int processedTargets = 0;
        Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);
        Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);
        if (layerHolder.isPresent() && ((Holder.Reference) layerHolder.get()).isBound() && originHolder.isPresent() && ((Holder.Reference) originHolder.get()).isBound() && (origin.equals(OriginRegisters.EMPTY.getKey()) || ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).contains(origin.location()))) {
            for (ServerPlayer target : targets) {
                setOrigin(target, originLayer, origin);
                processedTargets++;
            }
            if (processedTargets == 1) {
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.set.success.single", ((ServerPlayer) targets.iterator().next()).m_5446_().getString(), ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).name(), ((Origin) ((Holder.Reference) originHolder.get()).value()).getName()), true);
            } else {
                int finalProcessedTargets = processedTargets;
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.set.success.multiple", finalProcessedTargets, ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).name(), ((Origin) ((Holder.Reference) originHolder.get()).value()).getName()), true);
            }
        } else {
            serverCommandSource.sendFailure(Component.translatable("commands.origin.unregistered_in_layer", origin.location(), originLayer.location()));
        }
        return processedTargets;
    }

    private static void setOrigin(Player player, ResourceKey<OriginLayer> layer, ResourceKey<Origin> origin) {
        IOriginContainer.get(player).ifPresent(container -> {
            container.setOrigin(layer, origin);
            container.synchronize();
            container.onChosen(origin, container.hadAllOrigins());
        });
    }

    private static int hasOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
        ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        ResourceKey<Origin> origin = OriginArgumentType.getOrigin(commandContext, "origin");
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);
        Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);
        int processedTargets = 0;
        if (layerHolder.isPresent() && ((Holder.Reference) layerHolder.get()).isBound() && originHolder.isPresent() && ((Holder.Reference) originHolder.get()).isBound() && (origin.equals(OriginRegisters.EMPTY.getKey()) || ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).contains(origin))) {
            for (ServerPlayer target : targets) {
                LazyOptional<IOriginContainer> originContainer = IOriginContainer.get(target);
                if (originContainer.resolve().isPresent() && (origin.equals(((IForgeRegistry) OriginsBuiltinRegistries.ORIGINS.get()).getResourceKey(Origin.EMPTY).get()) || ((IOriginContainer) originContainer.resolve().get()).hasOrigin(originLayer)) && hasOrigin(target, originLayer, origin)) {
                    processedTargets++;
                }
            }
            if (processedTargets == 0) {
                serverCommandSource.sendFailure(Component.translatable("commands.execute.conditional.fail"));
            } else if (processedTargets == 1) {
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.execute.conditional.pass"), true);
            } else {
                int finalProcessedTargets = processedTargets;
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.execute.conditional.pass_count", finalProcessedTargets), true);
            }
        } else {
            serverCommandSource.sendFailure(Component.translatable("commands.origin.unregistered_in_layer", origin.location(), originLayer.location()));
        }
        return processedTargets;
    }

    private static boolean hasOrigin(Player player, ResourceKey<OriginLayer> layer, ResourceKey<Origin> origin) {
        return (Boolean) IOriginContainer.get(player).map(x -> Objects.equals(x.getOrigin(layer), origin)).orElse(false);
    }

    private static int getOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        ServerPlayer target = EntityArgument.getPlayer(commandContext, "target");
        ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);
        IOriginContainer.get(target).ifPresent(container -> {
            ResourceKey<Origin> origin = container.getOrigin(originLayer);
            Optional<Holder.Reference<Origin>> originHolder = OriginsAPI.getOriginsRegistry().getHolder(origin);
            serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.get.result", target.m_5446_().getString(), ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).name(), ((Origin) ((Holder.Reference) originHolder.get()).value()).getName(), origin.location()), true);
        });
        return 1;
    }

    private static int openSingleLayerScreen(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
        ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        OriginsAPI.getLayersRegistry().getHolder(originLayer).ifPresent(holder -> {
            for (ServerPlayer target : targets) {
                openLayerScreen(target, List.of(holder));
            }
            serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.gui.layer", targets.size(), ((OriginLayer) holder.value()).name()), true);
        });
        return targets.size();
    }

    private static int openMultipleLayerScreens(CommandContext<CommandSourceStack> commandContext, OriginCommand.TargetType targetType) throws CommandSyntaxException {
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        List<ServerPlayer> targets = new ArrayList();
        List<Holder<OriginLayer>> originLayers = OriginsAPI.getActiveLayers().stream().map(reference -> reference).toList();
        switch(targetType) {
            case INVOKER:
                targets.add(serverCommandSource.getPlayerOrException());
                break;
            case SPECIFY:
                targets.addAll(EntityArgument.getPlayers(commandContext, "targets"));
        }
        for (ServerPlayer target : targets) {
            openLayerScreen(target, originLayers);
        }
        serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.gui.all", targets.size()), false);
        return targets.size();
    }

    private static int randomizeOrigin(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(commandContext, "targets");
        ResourceKey<OriginLayer> originLayer = LayerArgumentType.getLayer(commandContext, "layer");
        Optional<Holder.Reference<OriginLayer>> layerHolder = OriginsAPI.getLayersRegistry().getHolder(originLayer);
        if (!layerHolder.isPresent() || !((Holder.Reference) layerHolder.get()).isBound()) {
            return 0;
        } else if (!((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).allowRandom()) {
            serverCommandSource.sendFailure(Component.translatable("commands.origin.random.not_allowed", ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).name()));
            return 0;
        } else {
            Holder<Origin> origin = null;
            for (ServerPlayer target : targets) {
                origin = getRandomOrigin(target, (Holder<OriginLayer>) layerHolder.get());
            }
            if (targets.size() > 1) {
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.random.success.multiple", targets.size(), ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).name()), true);
            } else if (targets.size() == 1) {
                Holder<Origin> finalOrigin = origin;
                serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.random.success.single", ((ServerPlayer) targets.iterator().next()).m_5446_().getString(), finalOrigin.value().getName(), ((OriginLayer) ((Holder.Reference) layerHolder.get()).value()).name()), false);
            }
            return targets.size();
        }
    }

    private static int randomizeOrigins(CommandContext<CommandSourceStack> commandContext, OriginCommand.TargetType targetType) throws CommandSyntaxException {
        CommandSourceStack serverCommandSource = (CommandSourceStack) commandContext.getSource();
        List<ServerPlayer> targets = new ArrayList();
        List<Holder.Reference<OriginLayer>> originLayers = OriginsAPI.getActiveLayers().stream().filter(originLayerReference -> originLayerReference.isBound() && ((OriginLayer) originLayerReference.value()).allowRandom()).toList();
        switch(targetType) {
            case INVOKER:
                targets.add(serverCommandSource.getPlayerOrException());
                break;
            case SPECIFY:
                targets.addAll(EntityArgument.getPlayers(commandContext, "targets"));
        }
        for (ServerPlayer target : targets) {
            for (Holder.Reference<OriginLayer> originLayer : originLayers) {
                getRandomOrigin(target, originLayer);
            }
        }
        serverCommandSource.sendSuccess(() -> Component.translatable("commands.origin.random.all", targets.size(), originLayers.size()), false);
        return targets.size();
    }

    private static void openLayerScreen(ServerPlayer target, List<Holder<OriginLayer>> originLayers) {
        LazyOptional<IOriginContainer> originContainer = IOriginContainer.get(target);
        originContainer.ifPresent(container -> {
            for (Holder<OriginLayer> layer : originLayers) {
                container.setOrigin((ResourceKey<OriginLayer>) layer.unwrapKey().orElseThrow(), OriginRegisters.EMPTY.getKey());
            }
            container.synchronize();
            container.checkAutoChoosingLayers(false);
            OriginsCommon.CHANNEL.send(PacketDistributor.PLAYER.with(() -> target), new S2COpenOriginScreen(false));
        });
    }

    private static Holder<Origin> getRandomOrigin(ServerPlayer target, Holder<OriginLayer> originLayer) {
        List<Holder<Origin>> origins = originLayer.value().randomOrigins(target);
        Holder<Origin> origin = (Holder<Origin>) origins.get(new Random().nextInt(origins.size()));
        LazyOptional<IOriginContainer> originContainer = IOriginContainer.get(target);
        originContainer.ifPresent(container -> {
            boolean hadOriginBefore = container.hadAllOrigins();
            boolean hadAllOrigins = container.hasAllOrigins();
            container.setOrigin(originLayer, origin);
            container.checkAutoChoosingLayers(false);
            container.synchronize();
            if (container.hasAllOrigins() && !hadAllOrigins) {
                container.onChosen(hadOriginBefore);
            }
            if (!origin.unwrapKey().isEmpty() && !originLayer.unwrapKey().isEmpty()) {
                Origins.LOGGER.info("Player {} was randomly assigned the origin {} for layer {}", target.m_5446_().getString(), ((ResourceKey) origin.unwrapKey().get()).location(), ((ResourceKey) originLayer.unwrapKey().get()).location());
            }
        });
        return origin;
    }

    private static enum TargetType {

        INVOKER, SPECIFY
    }
}