package dev.xkmc.l2library.capability.conditionals;

import dev.xkmc.l2serial.serialization.SerialClass;
import net.minecraft.world.entity.player.Player;

@SerialClass
public class ConditionalToken {

    public boolean tick(Player player) {
        return false;
    }
}