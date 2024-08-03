package net.minecraft.client.gui.narration;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.Component;

public interface NarrationElementOutput {

    default void add(NarratedElementType narratedElementType0, Component component1) {
        this.add(narratedElementType0, NarrationThunk.from(component1.getString()));
    }

    default void add(NarratedElementType narratedElementType0, String string1) {
        this.add(narratedElementType0, NarrationThunk.from(string1));
    }

    default void add(NarratedElementType narratedElementType0, Component... component1) {
        this.add(narratedElementType0, NarrationThunk.from(ImmutableList.copyOf(component1)));
    }

    void add(NarratedElementType var1, NarrationThunk<?> var2);

    NarrationElementOutput nest();
}