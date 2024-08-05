package com.simibubi.create.infrastructure.debugInfo.element;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.infrastructure.debugInfo.DebugInformation;
import com.simibubi.create.infrastructure.debugInfo.InfoProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public record DebugInfoSection(String name, ImmutableList<InfoElement> elements) implements InfoElement {

    @Override
    public void write(Player player, FriendlyByteBuf buffer) {
        buffer.writeBoolean(true);
        buffer.writeUtf(this.name);
        buffer.writeCollection(this.elements, (buf, element) -> element.write(player, buf));
    }

    public DebugInfoSection.Builder builder() {
        return builder(this.name).putAll(this.elements);
    }

    @Override
    public void print(int depth, @Nullable Player player, Consumer<String> lineConsumer) {
        String indent = DebugInformation.getIndent(depth);
        lineConsumer.accept(indent + this.name + ":");
        this.elements.forEach(element -> element.print(depth + 1, player, lineConsumer));
    }

    public static DebugInfoSection read(FriendlyByteBuf buffer) {
        String name = buffer.readUtf();
        ArrayList<InfoElement> elements = buffer.readCollection(ArrayList::new, InfoElement::read);
        return new DebugInfoSection(name, ImmutableList.copyOf(elements));
    }

    public static DebugInfoSection readDirect(FriendlyByteBuf buf) {
        buf.readBoolean();
        return read(buf);
    }

    public static DebugInfoSection.Builder builder(String name) {
        return new DebugInfoSection.Builder(null, name);
    }

    public static DebugInfoSection of(String name, Collection<DebugInfoSection> children) {
        return builder(name).putAll(children).build();
    }

    public static class Builder {

        private final DebugInfoSection.Builder parent;

        private final String name;

        private final com.google.common.collect.ImmutableList.Builder<InfoElement> elements;

        public Builder(DebugInfoSection.Builder parent, String name) {
            this.parent = parent;
            this.name = name;
            this.elements = ImmutableList.builder();
        }

        public DebugInfoSection.Builder put(InfoElement element) {
            this.elements.add(element);
            return this;
        }

        public DebugInfoSection.Builder put(String key, InfoProvider provider) {
            return this.put(new InfoEntry(key, provider));
        }

        public DebugInfoSection.Builder put(String key, Supplier<String> value) {
            return this.put(key, (InfoProvider) (player -> (String) value.get()));
        }

        public DebugInfoSection.Builder put(String key, String value) {
            return this.put(key, (InfoProvider) (player -> value));
        }

        public DebugInfoSection.Builder putAll(Collection<? extends InfoElement> elements) {
            elements.forEach(this::put);
            return this;
        }

        public DebugInfoSection.Builder section(String name) {
            return new DebugInfoSection.Builder(this, name);
        }

        public DebugInfoSection.Builder finishSection() {
            if (this.parent == null) {
                throw new IllegalStateException("Cannot finish the root section");
            } else {
                this.parent.elements.add(this.build());
                return this.parent;
            }
        }

        public DebugInfoSection build() {
            return new DebugInfoSection(this.name, this.elements.build());
        }

        public void buildTo(Consumer<DebugInfoSection> consumer) {
            consumer.accept(this.build());
        }
    }
}