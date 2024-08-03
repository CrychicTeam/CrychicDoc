package portb.biggerstacks.event;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.loading.FMLEnvironment;
import portb.biggerstacks.Constants;
import portb.biggerstacks.config.StackSizeRules;
import portb.biggerstacks.util.ConfigCommand;

@EventBusSubscriber(modid = "biggerstacks")
public class CommonForgeEvents {

    private static final Style SETUP_COMMAND_SHORTCUT = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/biggerstacks quicksetup")).withColor(ChatFormatting.BLUE).withUnderlined(true);

    private static final Style WIKI_LINK = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://codeberg.org/PORTB/BiggerStacks/wiki")).withColor(ChatFormatting.BLUE).withUnderlined(true);

    private static final Component BULLET_POINT = Component.literal("\n> ").withStyle(ChatFormatting.WHITE);

    @SubscribeEvent
    public static void warnIfNoRulesetExists(PlayerEvent.PlayerLoggedInEvent event) {
        if (!Files.exists(Constants.RULESET_FILE, new LinkOption[0]) && StackSizeRules.maxRegisteredItemStackSize == 64) {
            if (FMLEnvironment.dist.isDedicatedServer() && !event.getEntity().m_20310_(4)) {
                return;
            }
            event.getEntity().displayClientMessage(Component.literal("Biggerstacks is installed, but you have not configured it and there are no other mods installed that use it.").append(BULLET_POINT).append("Run ").append(Component.literal("/biggerstacks quicksetup").withStyle(SETUP_COMMAND_SHORTCUT)).append(" and the mod will generate a simple ruleset for you").append(BULLET_POINT).append("Or click ").append(Component.literal("here").withStyle(WIKI_LINK)).append(" to see how to create a custom ruleset").withStyle(ChatFormatting.GOLD), false);
        }
    }

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        ConfigCommand.register(event);
    }
}