package moe.wolfgirl.probejs;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.latvian.mods.kubejs.KubeJS;
import java.util.function.Consumer;
import moe.wolfgirl.probejs.lang.linter.Linter;
import moe.wolfgirl.probejs.utils.GameUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class GameEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void playerJoined(ClientPlayerNetworkEvent.LoggingIn event) {
        LocalPlayer player = event.getPlayer();
        ProbeConfig config = ProbeConfig.INSTANCE;
        if (config.enabled.get()) {
            if (config.modHash.get() == -1L) {
                player.sendSystemMessage(Component.translatable("probejs.hello").kjs$gold());
            }
            if (config.registryHash.get() != GameUtils.registryHash()) {
                new Thread(() -> {
                    ProbeDump dump = new ProbeDump();
                    dump.defaultScripts();
                    try {
                        dump.trigger(player::m_213846_);
                        Linter.defaultLint(player::m_213846_);
                    } catch (Throwable var3) {
                        ProbeJS.LOGGER.error(var3.getMessage());
                        throw new RuntimeException(var3);
                    }
                }).start();
            } else {
                player.sendSystemMessage(Component.translatable("probejs.enabled_warning").append(Component.literal("/probejs disable").kjs$clickSuggestCommand("/probejs disable").kjs$aqua()));
                Linter.defaultLint(player::m_213846_);
            }
            player.sendSystemMessage(Component.translatable("probejs.wiki").append(Component.literal("Github Page").kjs$aqua().kjs$underlined().kjs$clickOpenUrl("https://kubejs.com/wiki/addons/third-party/probejs").kjs$hover(Component.literal("https://kubejs.com/wiki/addons/third-party/probejs"))));
        }
    }

    @SubscribeEvent
    public static void registerCommand(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("probejs").then(((LiteralArgumentBuilder) Commands.literal("dump").requires(source -> ProbeConfig.INSTANCE.enabled.get() && source.hasPermission(2))).executes(context -> {
            KubeJS.PROXY.reloadClientInternal();
            ProbeDump dump = new ProbeDump();
            dump.defaultScripts();
            try {
                Consumer<Component> reportProgress = component -> ((CommandSourceStack) context.getSource()).sendSystemMessage(component);
                dump.trigger(reportProgress);
                return 1;
            } catch (Throwable var3) {
                throw new RuntimeException(var3);
            }
        }))).then(((LiteralArgumentBuilder) Commands.literal("disable").requires(source -> ProbeConfig.INSTANCE.enabled.get() && source.hasPermission(2))).executes(context -> {
            ProbeConfig.INSTANCE.enabled.set(false);
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.translatable("probejs.bye_bye").kjs$gold());
            return 1;
        }))).then(((LiteralArgumentBuilder) Commands.literal("enable").requires(source -> source.hasPermission(2))).executes(context -> {
            ProbeConfig.INSTANCE.enabled.set(true);
            ((CommandSourceStack) context.getSource()).sendSystemMessage(Component.translatable("probejs.hello_again").kjs$aqua());
            return 1;
        }))).then(((LiteralArgumentBuilder) Commands.literal("scope_isolation").requires(source -> source.hasPermission(2))).executes(context -> {
            boolean flag = !ProbeConfig.INSTANCE.isolatedScopes.get();
            ProbeConfig.INSTANCE.isolatedScopes.set(flag);
            ((CommandSourceStack) context.getSource()).sendSystemMessage(flag ? Component.translatable("probejs.isolation").kjs$aqua() : Component.translatable("probejs.no_isolation").kjs$aqua());
            return 1;
        }))).then(((LiteralArgumentBuilder) Commands.literal("lint").requires(source -> ProbeConfig.INSTANCE.enabled.get() && source.hasPermission(2))).executes(context -> {
            Linter.defaultLint(((CommandSourceStack) context.getSource())::m_243053_);
            return 1;
        })));
    }
}