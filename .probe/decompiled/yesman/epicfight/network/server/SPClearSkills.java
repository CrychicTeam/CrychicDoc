package yesman.epicfight.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;

public class SPClearSkills {

    public static SPClearSkills fromBytes(FriendlyByteBuf buf) {
        return new SPClearSkills();
    }

    public static void toBytes(SPClearSkills msg, FriendlyByteBuf buf) {
    }

    public static void handle(SPClearSkills msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            PlayerPatch<?> playerpatch = (PlayerPatch<?>) mc.player.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null);
            if (playerpatch != null) {
                playerpatch.getSkillCapability().clear();
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}