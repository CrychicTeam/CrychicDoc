package com.blamejared.searchables.mixin;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.gui.components.EditBox;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ EditBox.class })
public interface AccessEditBox {

    @Accessor("filter")
    Predicate<String> searchables$getFilter();

    @Accessor("responder")
    @Nullable
    Consumer<String> searchables$getResponder();
}