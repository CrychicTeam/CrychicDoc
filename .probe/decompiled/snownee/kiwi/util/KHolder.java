package snownee.kiwi.util;

import net.minecraft.resources.ResourceLocation;

public record KHolder<T>(ResourceLocation key, T value) {
}