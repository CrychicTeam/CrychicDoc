package portb.biggerstacks.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.PacketDistributor;
import org.slf4j.Logger;
import portb.biggerstacks.Constants;
import portb.biggerstacks.net.ClientboundConfigureScreenOpenPacket;
import portb.biggerstacks.net.PacketHandler;
import portb.configlib.template.ConfigTemplate;
import portb.configlib.template.TemplateOptions;

public class ConfigCommand {

    public static final Logger LOGGER = LogUtils.getLogger();

    public static void register(RegisterCommandsEvent event) {
        LiteralArgumentBuilder<CommandSourceStack> cmd = (LiteralArgumentBuilder<CommandSourceStack>) Commands.literal("biggerstacks").then(Commands.literal("quicksetup").executes(context -> {
            try {
                boolean hasCustomExistingFile = false;
                TemplateOptions template = new TemplateOptions(64, 1, 1);
                if (Files.exists(Constants.RULESET_FILE, new LinkOption[0])) {
                    try {
                        template = ConfigTemplate.readParametersFromTemplate(new String(Files.readAllBytes(Constants.RULESET_FILE), StandardCharsets.UTF_8));
                    } catch (Throwable var4) {
                        LOGGER.debug("Error reading template file", var4);
                        hasCustomExistingFile = true;
                    }
                }
                ServerPlayer player = ((CommandSourceStack) context.getSource()).getPlayerOrException();
                PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new ClientboundConfigureScreenOpenPacket(hasCustomExistingFile, template.getNormalStackLimit(), template.getPotionStackLimit(), template.getEnchBookLimit()));
                return 1;
            } catch (CommandSyntaxException var5) {
                ((CommandSourceStack) context.getSource()).sendFailure(Component.translatable("biggerstacks.player.expected"));
                return 0;
            }
        }));
        if (FMLEnvironment.dist.isDedicatedServer()) {
            cmd.requires(commandSourceStack -> commandSourceStack.hasPermission(4));
        }
        event.getDispatcher().register(cmd);
    }
}