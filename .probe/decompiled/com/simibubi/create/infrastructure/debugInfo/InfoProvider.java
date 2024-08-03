package com.simibubi.create.infrastructure.debugInfo;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;

@FunctionalInterface
public interface InfoProvider {

    @Nullable
    String getInfo(@Nullable Player var1);

    default String getInfoSafe(Player player) {
        try {
            return Objects.toString(this.getInfo(player));
        } catch (Throwable var8) {
            StringBuilder builder = new StringBuilder("Error getting information!");
            builder.append(' ').append(var8.getMessage());
            for (StackTraceElement element : var8.getStackTrace()) {
                builder.append('\n').append("\t").append(element.toString());
            }
            return builder.toString();
        }
    }
}