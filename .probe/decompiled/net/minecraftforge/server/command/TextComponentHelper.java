package net.minecraftforge.server.command;

import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.commands.CommandSource;
import net.minecraft.locale.Language;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraftforge.network.ConnectionType;
import net.minecraftforge.network.NetworkHooks;

public class TextComponentHelper {

    private TextComponentHelper() {
    }

    public static MutableComponent createComponentTranslation(CommandSource source, String translation, Object... args) {
        return isVanillaClient(source) ? Component.literal(String.format(Locale.ENGLISH, Language.getInstance().getOrDefault(translation), args)) : Component.translatable(translation, args);
    }

    private static boolean isVanillaClient(CommandSource sender) {
        if (sender instanceof ServerPlayer playerMP) {
            ServerGamePacketListenerImpl channel = playerMP.connection;
            return NetworkHooks.getConnectionType((Supplier<Connection>) (() -> channel.connection)) == ConnectionType.VANILLA;
        } else {
            return false;
        }
    }
}