package net.minecraft.world.level.timers;

import net.minecraft.commands.CommandFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerFunctionManager;

public class FunctionTagCallback implements TimerCallback<MinecraftServer> {

    final ResourceLocation tagId;

    public FunctionTagCallback(ResourceLocation resourceLocation0) {
        this.tagId = resourceLocation0;
    }

    public void handle(MinecraftServer minecraftServer0, TimerQueue<MinecraftServer> timerQueueMinecraftServer1, long long2) {
        ServerFunctionManager $$3 = minecraftServer0.getFunctions();
        for (CommandFunction $$5 : $$3.getTag(this.tagId)) {
            $$3.execute($$5, $$3.getGameLoopSender());
        }
    }

    public static class Serializer extends TimerCallback.Serializer<MinecraftServer, FunctionTagCallback> {

        public Serializer() {
            super(new ResourceLocation("function_tag"), FunctionTagCallback.class);
        }

        public void serialize(CompoundTag compoundTag0, FunctionTagCallback functionTagCallback1) {
            compoundTag0.putString("Name", functionTagCallback1.tagId.toString());
        }

        public FunctionTagCallback deserialize(CompoundTag compoundTag0) {
            ResourceLocation $$1 = new ResourceLocation(compoundTag0.getString("Name"));
            return new FunctionTagCallback($$1);
        }
    }
}