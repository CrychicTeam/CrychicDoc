package github.pitbox46.fightnbtintegration;

import github.pitbox46.fightnbtintegration.network.ClientProxy;
import github.pitbox46.fightnbtintegration.network.CommonProxy;
import github.pitbox46.fightnbtintegration.network.PacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("fightnbtintegration")
public class FightNBTIntegration {

    private static final Logger LOGGER = LogManager.getLogger();

    public static CommonProxy PROXY;

    public FightNBTIntegration() {
        PROXY = (CommonProxy) DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        Config.init(event.getServer().getWorldPath(new LevelResource("epicfightnbt")));
    }

    @SubscribeEvent
    public void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer) {
            PacketHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), Config.configFileToSSyncConfig());
        }
    }
}