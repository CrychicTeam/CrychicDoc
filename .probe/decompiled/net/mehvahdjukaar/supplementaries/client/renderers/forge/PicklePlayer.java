package net.mehvahdjukaar.supplementaries.client.renderers.forge;

import java.util.UUID;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.JarredRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.PickleData;
import net.mehvahdjukaar.supplementaries.client.renderers.entities.funny.PickleRenderer;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.PicklePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = "supplementaries", value = { Dist.CLIENT })
public class PicklePlayer {

    private static boolean jarvis = false;

    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        PickleData.onPlayerLogOff();
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        PickleData.onPlayerLogin(event.getEntity());
    }

    public static boolean onChatEvent(String m) {
        UUID id = Minecraft.getInstance().player.m_36316_().getId();
        if (m.startsWith("/jarman")) {
            jarvis = !jarvis;
            if (jarvis) {
                Minecraft.getInstance().player.displayClientMessage(Component.literal("I am Jarman"), true);
            }
            return true;
        } else {
            boolean jar = m.startsWith("/jar");
            if (PickleData.isDev(id, jar)) {
                boolean pick = m.startsWith("/pickle");
                if (pick || jar) {
                    boolean turnOn = !PickleData.isActive(id);
                    if (turnOn && pick) {
                        Minecraft.getInstance().player.displayClientMessage(Component.literal("I turned myself into a pickle!"), true);
                    }
                    PickleData.set(id, turnOn, jar);
                    ModNetwork.CHANNEL.sendToServer(new PicklePacket(id, turnOn, jar));
                    return true;
                }
            }
            return false;
        }
    }

    @SubscribeEvent
    public static void onRenderPlayerPre(RenderPlayerEvent.Pre event) {
        Player player = event.getEntity();
        UUID id = player.getGameProfile().getId();
        if (PickleData.isActiveAndTick(id, event.getRenderer()) && PickleRenderer.INSTANCE != null) {
            event.setCanceled(true);
            float rot = Mth.rotLerp(player.f_19859_, player.m_146908_(), event.getPartialTick());
            PickleRenderer.INSTANCE.render((AbstractClientPlayer) player, rot, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        } else if (jarvis && id.equals(Minecraft.getInstance().player.m_20148_()) && JarredRenderer.INSTANCE != null) {
            event.setCanceled(true);
            float rot = Mth.rotLerp(player.f_19859_, player.m_146908_(), event.getPartialTick());
            JarredRenderer.INSTANCE.render((AbstractClientPlayer) player, rot, event.getPartialTick(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
        }
    }
}