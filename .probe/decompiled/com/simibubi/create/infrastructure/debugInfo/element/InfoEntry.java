package com.simibubi.create.infrastructure.debugInfo.element;

import com.simibubi.create.infrastructure.debugInfo.DebugInformation;
import com.simibubi.create.infrastructure.debugInfo.InfoProvider;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record InfoEntry(String name, InfoProvider provider) implements InfoElement {

    public InfoEntry(String name, String info) {
        this(name, (InfoProvider) (player -> info));
    }

    @Override
    public void write(Player player, FriendlyByteBuf buffer) {
        buffer.writeBoolean(false);
        buffer.writeUtf(this.name);
        buffer.writeUtf(this.provider.getInfoSafe(player));
    }

    @Override
    public void print(int depth, @Nullable Player player, Consumer<String> lineConsumer) {
        String value = this.provider.getInfoSafe(player);
        String indent = DebugInformation.getIndent(depth);
        if (value.contains("\n")) {
            String[] lines = value.split("\n");
            String firstLine = lines[0];
            String lineStart = this.name + ": ";
            lineConsumer.accept(indent + lineStart + firstLine);
            String extraIndent = (String) Stream.generate(() -> " ").limit((long) lineStart.length()).collect(Collectors.joining());
            for (int i = 1; i < lines.length; i++) {
                lineConsumer.accept(indent + extraIndent + lines[i]);
            }
        } else {
            lineConsumer.accept(indent + this.name + ": " + value);
        }
    }

    public static InfoEntry read(FriendlyByteBuf buffer) {
        String name = buffer.readUtf();
        String value = buffer.readUtf();
        return new InfoEntry(name, value);
    }
}