package snownee.jade.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import snownee.jade.Jade;
import snownee.jade.gui.HomeConfigScreen;
import snownee.jade.util.DumpGenerator;

public class JadeClientCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("jadec").then(Commands.literal("handlers").executes(context -> {
            File file = new File("jade_handlers.md");
            try {
                FileWriter writer = new FileWriter(file);
                byte var3;
                try {
                    writer.write(DumpGenerator.generateInfoDump());
                    ((CommandSourceStack) context.getSource()).sendSuccess(() -> Component.translatable("command.jade.dump.success"), false);
                    var3 = 1;
                } catch (Throwable var6) {
                    try {
                        writer.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                    throw var6;
                }
                writer.close();
                return var3;
            } catch (IOException var7) {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.literal(var7.getClass().getSimpleName() + ": " + var7.getMessage()));
                return 0;
            }
        }))).then(Commands.literal("config").executes(context -> {
            Minecraft.getInstance().m_6937_(() -> {
                Jade.CONFIG.invalidate();
                Minecraft.getInstance().setScreen(new HomeConfigScreen(null));
            });
            return 1;
        })));
    }
}