package com.simibubi.create.infrastructure.debugInfo.element;

import java.util.function.Consumer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public sealed interface InfoElement permits DebugInfoSection, InfoEntry {

    void write(Player var1, FriendlyByteBuf var2);

    void print(int var1, @Nullable Player var2, Consumer<String> var3);

    default void print(@Nullable Player player, Consumer<String> lineConsumer) {
        this.print(0, player, lineConsumer);
    }

    static InfoElement read(FriendlyByteBuf buffer) {
        boolean section = buffer.readBoolean();
        return (InfoElement) (section ? DebugInfoSection.read(buffer) : InfoEntry.read(buffer));
    }
}