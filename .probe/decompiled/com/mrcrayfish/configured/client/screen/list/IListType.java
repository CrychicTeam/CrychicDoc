package com.mrcrayfish.configured.client.screen.list;

import java.util.function.Function;
import net.minecraft.network.chat.Component;

public interface IListType<T> {

    Function<T, String> getStringParser();

    Function<String, T> getValueParser();

    Component getHint();
}