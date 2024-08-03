package dev.xkmc.l2library.capability.conditionals;

import net.minecraft.resources.ResourceLocation;

public record TokenKey<T extends ConditionalToken>(String type, String id) {

    public static <T extends ConditionalToken> TokenKey<T> of(ResourceLocation id) {
        return new TokenKey<>(id.getNamespace(), id.getPath());
    }

    public ResourceLocation asLocation() {
        return new ResourceLocation(this.type, this.id);
    }
}