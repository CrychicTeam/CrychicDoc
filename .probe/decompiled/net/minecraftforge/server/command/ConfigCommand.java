package net.minecraftforge.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import java.io.File;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ConfigCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) Commands.literal("config").then(ConfigCommand.ShowFile.register()));
    }

    public static class ShowFile {

        static ArgumentBuilder<CommandSourceStack, ?> register() {
            return ((LiteralArgumentBuilder) Commands.literal("showfile").requires(cs -> cs.hasPermission(0))).then(Commands.argument("mod", ModIdArgument.modIdArgument()).then(Commands.argument("type", EnumArgument.enumArgument(Type.class)).executes(ConfigCommand.ShowFile::showFile)));
        }

        private static int showFile(CommandContext<CommandSourceStack> context) {
            String modId = (String) context.getArgument("mod", String.class);
            Type type = (Type) context.getArgument("type", Type.class);
            String configFileName = ConfigTracker.INSTANCE.getConfigFileName(modId, type);
            if (configFileName != null) {
                File f = new File(configFileName);
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("commands.config.getwithtype", modId, type, Component.literal(f.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, f.getAbsolutePath())))), true);
            } else {
                ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("commands.config.noconfig", modId, type), true);
            }
            return 0;
        }
    }
}