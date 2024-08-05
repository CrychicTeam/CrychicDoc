package net.cristellib.builtinpacks;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackSource;
import org.jetbrains.annotations.NotNull;

public class BuiltinResourcePackSource implements PackSource {

    @Override
    public boolean shouldAddAutomatically() {
        return true;
    }

    @NotNull
    @Override
    public Component decorate(@NotNull Component packName) {
        return Component.translatable("cristellib.nameAndSource", packName, Component.translatable("cristellib.builtinPack")).withStyle(ChatFormatting.GRAY);
    }
}