package dev.architectury.registry.registries.options;

import net.minecraft.resources.ResourceLocation;

public record DefaultIdRegistrarOption(ResourceLocation defaultId) implements RegistrarOption {
}