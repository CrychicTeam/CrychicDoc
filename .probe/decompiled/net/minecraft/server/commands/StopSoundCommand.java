package net.minecraft.server.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.Collection;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundStopSoundPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;

public class StopSoundCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcherCommandSourceStack0) {
        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> $$1 = (RequiredArgumentBuilder<CommandSourceStack, EntitySelector>) ((RequiredArgumentBuilder) Commands.argument("targets", EntityArgument.players()).executes(p_138809_ -> stopSound((CommandSourceStack) p_138809_.getSource(), EntityArgument.getPlayers(p_138809_, "targets"), null, null))).then(Commands.literal("*").then(Commands.argument("sound", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_SOUNDS).executes(p_138797_ -> stopSound((CommandSourceStack) p_138797_.getSource(), EntityArgument.getPlayers(p_138797_, "targets"), null, ResourceLocationArgument.getId(p_138797_, "sound")))));
        for (SoundSource $$2 : SoundSource.values()) {
            $$1.then(((LiteralArgumentBuilder) Commands.literal($$2.getName()).executes(p_138807_ -> stopSound((CommandSourceStack) p_138807_.getSource(), EntityArgument.getPlayers(p_138807_, "targets"), $$2, null))).then(Commands.argument("sound", ResourceLocationArgument.id()).suggests(SuggestionProviders.AVAILABLE_SOUNDS).executes(p_138793_ -> stopSound((CommandSourceStack) p_138793_.getSource(), EntityArgument.getPlayers(p_138793_, "targets"), $$2, ResourceLocationArgument.getId(p_138793_, "sound")))));
        }
        commandDispatcherCommandSourceStack0.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("stopsound").requires(p_138799_ -> p_138799_.hasPermission(2))).then($$1));
    }

    private static int stopSound(CommandSourceStack commandSourceStack0, Collection<ServerPlayer> collectionServerPlayer1, @Nullable SoundSource soundSource2, @Nullable ResourceLocation resourceLocation3) {
        ClientboundStopSoundPacket $$4 = new ClientboundStopSoundPacket(resourceLocation3, soundSource2);
        for (ServerPlayer $$5 : collectionServerPlayer1) {
            $$5.connection.send($$4);
        }
        if (soundSource2 != null) {
            if (resourceLocation3 != null) {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.stopsound.success.source.sound", resourceLocation3, soundSource2.getName()), true);
            } else {
                commandSourceStack0.sendSuccess(() -> Component.translatable("commands.stopsound.success.source.any", soundSource2.getName()), true);
            }
        } else if (resourceLocation3 != null) {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.stopsound.success.sourceless.sound", resourceLocation3), true);
        } else {
            commandSourceStack0.sendSuccess(() -> Component.translatable("commands.stopsound.success.sourceless.any"), true);
        }
        return collectionServerPlayer1.size();
    }
}