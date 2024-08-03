package harmonised.pmmo.config.readers;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ExecutableListener extends SimplePreparableReloadListener<Boolean> {

    private Runnable executor;

    public ExecutableListener(Runnable executor) {
        this.executor = executor;
    }

    protected Boolean prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        return false;
    }

    protected void apply(Boolean pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        this.executor.run();
    }

    public <PACKET> ExecutableListener subscribeAsSyncable(SimpleChannel channel, Supplier<PACKET> packetFactory) {
        MinecraftForge.EVENT_BUS.addListener(this.getDatapackSyncListener(channel, packetFactory));
        return this;
    }

    private <PACKET> Consumer<OnDatapackSyncEvent> getDatapackSyncListener(SimpleChannel channel, Supplier<PACKET> packetFactory) {
        return event -> {
            ServerPlayer player = event.getPlayer();
            PacketDistributor.PacketTarget target = player == null ? PacketDistributor.ALL.noArg() : PacketDistributor.PLAYER.with(() -> player);
            channel.send(target, packetFactory.get());
        };
    }
}