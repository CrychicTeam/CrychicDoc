package io.redspace.ironsspellbooks.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.server.command.EnumArgument;

public class IronsDebugCommand {

    public static void register(CommandDispatcher<CommandSourceStack> pDispatcher) {
        pDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("ironsDebug").requires(p_138819_ -> p_138819_.hasPermission(2))).then(Commands.argument("dataType", EnumArgument.enumArgument(IronsDebugCommand.IronsDebugCommandTypes.class)).executes(commandContext -> getDataForType((CommandSourceStack) commandContext.getSource(), (IronsDebugCommand.IronsDebugCommandTypes) commandContext.getArgument("dataType", IronsDebugCommand.IronsDebugCommandTypes.class))))).then(Commands.literal("spellCount").executes(commandContext -> {
            int i = SpellRegistry.getEnabledSpells().size();
            ((CommandSourceStack) commandContext.getSource()).sendSuccess(() -> Component.literal(String.valueOf(i)), true);
            return i;
        })));
    }

    public static int getDataForType(CommandSourceStack source, IronsDebugCommand.IronsDebugCommandTypes ironsDebugCommandTypes) {
        switch(ironsDebugCommandTypes) {
            case RECASTING:
                getReacstingData(source);
            default:
                return 1;
        }
    }

    public static void getReacstingData(CommandSourceStack source) {
        ServerPlayer serverPlayer = source.getPlayer();
        MagicData magicData = MagicData.getPlayerMagicData(serverPlayer);
        writeResults(source, magicData.getPlayerRecasts().toString());
    }

    private static void writeResults(CommandSourceStack source, String results) {
        try {
            File file = new File("irons_debug.txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(results);
            writer.close();
            Component component = Component.literal(file.getName()).withStyle(ChatFormatting.UNDERLINE).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file.getAbsolutePath())));
            source.sendSuccess(() -> Component.translatable("commands.irons_spellbooks.irons_debug_command.success", component), true);
        } catch (Exception var5) {
        }
    }

    public static enum IronsDebugCommandTypes {

        RECASTING
    }
}