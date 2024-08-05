package com.blamejared.controlling.api;

import com.blamejared.controlling.client.NewKeyBindsList;
import com.blamejared.controlling.mixin.AccessKeyMapping;
import java.util.function.Predicate;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.controls.KeyBindsList;

public enum DisplayMode {

    ALL(keyEntry -> true), NONE(keyEntry -> keyEntry.getKey().isUnbound()), CONFLICTING(keyEntry -> {
        for (KeyMapping key : Minecraft.getInstance().options.keyMappings) {
            if (!key.getName().equals(keyEntry.getKey().getName()) && !key.isUnbound() && ((AccessKeyMapping) key).controlling$getKey().getValue() == ((AccessKeyMapping) keyEntry.getKey()).controlling$getKey().getValue()) {
                return true;
            }
        }
        return false;
    });

    private final Predicate<NewKeyBindsList.KeyEntry> predicate;

    private DisplayMode(Predicate<NewKeyBindsList.KeyEntry> predicate) {
        this.predicate = predicate;
    }

    public Predicate<KeyBindsList.Entry> getPredicate() {
        return entry -> {
            if (entry instanceof NewKeyBindsList.KeyEntry keyEntry && this.predicate.test(keyEntry)) {
                return true;
            }
            return false;
        };
    }
}