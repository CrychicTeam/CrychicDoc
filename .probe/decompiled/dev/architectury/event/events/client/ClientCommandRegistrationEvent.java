package dev.architectury.event.events.client;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public interface ClientCommandRegistrationEvent {

    Event<ClientCommandRegistrationEvent> EVENT = EventFactory.createLoop();

    void register(CommandDispatcher<ClientCommandRegistrationEvent.ClientCommandSourceStack> var1, CommandBuildContext var2);

    static LiteralArgumentBuilder<ClientCommandRegistrationEvent.ClientCommandSourceStack> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    static <T> RequiredArgumentBuilder<ClientCommandRegistrationEvent.ClientCommandSourceStack, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public interface ClientCommandSourceStack extends SharedSuggestionProvider {

        void arch$sendSuccess(Supplier<Component> var1, boolean var2);

        void arch$sendFailure(Component var1);

        LocalPlayer arch$getPlayer();

        Vec3 arch$getPosition();

        Vec2 arch$getRotation();

        ClientLevel arch$getLevel();
    }
}