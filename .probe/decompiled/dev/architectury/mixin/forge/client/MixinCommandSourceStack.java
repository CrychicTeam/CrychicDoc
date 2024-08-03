package dev.architectury.mixin.forge.client;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import java.util.function.Supplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ CommandSourceStack.class })
public abstract class MixinCommandSourceStack implements ClientCommandRegistrationEvent.ClientCommandSourceStack {

    @Override
    public void arch$sendSuccess(Supplier<Component> message, boolean broadcastToAdmins) {
        ((CommandSourceStack) this).sendSuccess(message, broadcastToAdmins);
    }

    @Override
    public void arch$sendFailure(Component message) {
        ((CommandSourceStack) this).sendFailure(message);
    }

    @Override
    public LocalPlayer arch$getPlayer() {
        try {
            return (LocalPlayer) ((CommandSourceStack) this).getEntityOrException();
        } catch (CommandSyntaxException var2) {
            throw new RuntimeException(var2);
        }
    }

    @Override
    public Vec3 arch$getPosition() {
        return ((CommandSourceStack) this).getPosition();
    }

    @Override
    public Vec2 arch$getRotation() {
        return ((CommandSourceStack) this).getRotation();
    }

    @Override
    public ClientLevel arch$getLevel() {
        return (ClientLevel) ((CommandSourceStack) this).getUnsidedLevel();
    }
}