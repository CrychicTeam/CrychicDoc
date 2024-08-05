package net.minecraft.world.level.timers;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;

public class FunctionCallback implements TimerCallback<MinecraftServer> {

    final ResourceLocation functionId;

    public FunctionCallback(ResourceLocation resourceLocation0) {
        this.functionId = resourceLocation0;
    }

    public void handle(MinecraftServer minecraftServer0, TimerQueue<MinecraftServer> timerQueueMinecraftServer1, long long2) {
        ServerFunctionManager $$3 = minecraftServer0.getFunctions();
        $$3.get(this.functionId).ifPresent(p_82177_ -> $$3.execute(p_82177_, $$3.getGameLoopSender()));
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, FunctionCallback> {

        public Serializer() {
            super(new ResourceLocation("function"), FunctionCallback.class);
        }

        public void serialize(CompoundTag compoundTag0, FunctionCallback functionCallback1) {
            compoundTag0.putString("Name", functionCallback1.functionId.toString());
        }

        public FunctionCallback deserialize(CompoundTag compoundTag0) {
            ResourceLocation $$1 = new ResourceLocation(compoundTag0.getString("Name"));
            return new FunctionCallback($$1);
        }
    }
}