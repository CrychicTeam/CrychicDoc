package org.embeddedt.embeddium.api.service;

import java.util.function.Consumer;
import java.util.function.Function;

public interface FlawlessFramesService {

    void acceptController(Function<String, Consumer<Boolean>> var1);
}