package dev.ftb.mods.ftbteams.api.property;

import java.util.function.BiConsumer;
import net.minecraft.network.FriendlyByteBuf;

public interface TeamPropertyCollection {

    <T> void forEach(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> var1);

    TeamPropertyCollection copy();

    void updateFrom(TeamPropertyCollection var1);

    <T> T get(TeamProperty<T> var1);

    <T> void set(TeamProperty<T> var1, T var2);

    void write(FriendlyByteBuf var1);

    void read(FriendlyByteBuf var1);
}