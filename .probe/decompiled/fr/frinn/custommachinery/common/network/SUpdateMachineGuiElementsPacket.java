package fr.frinn.custommachinery.common.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.client.ClientPacketHandler;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class SUpdateMachineGuiElementsPacket extends BaseS2CMessage {

    private final BlockPos pos;

    private final List<IGuiElement> guiElements;

    public SUpdateMachineGuiElementsPacket(BlockPos pos, List<IGuiElement> guiElements) {
        this.pos = pos;
        this.guiElements = guiElements;
    }

    @Override
    public MessageType getType() {
        return PacketManager.UPDATE_MACHINE_GUI_ELEMENTS;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        if (this.guiElements != null && !this.guiElements.isEmpty()) {
            buf.writeBoolean(false);
            IGuiElement.CODEC.listOf().toNetwork(this.guiElements, buf);
        } else {
            buf.writeBoolean(true);
        }
    }

    public static SUpdateMachineGuiElementsPacket read(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        return buf.readBoolean() ? new SUpdateMachineGuiElementsPacket(pos, Collections.emptyList()) : new SUpdateMachineGuiElementsPacket(pos, IGuiElement.CODEC.listOf().fromNetwork(buf));
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.CLIENT) {
            context.queue(() -> ClientPacketHandler.handleUpdateMachineGuiElementsPacket(this.pos, this.guiElements));
        }
    }
}