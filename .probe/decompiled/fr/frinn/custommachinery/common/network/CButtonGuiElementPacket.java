package fr.frinn.custommachinery.common.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.utils.Env;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.guielement.ButtonGuiElement;
import fr.frinn.custommachinery.common.init.CustomMachineContainer;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.TaskDelayer;
import net.minecraft.network.FriendlyByteBuf;

public class CButtonGuiElementPacket extends BaseC2SMessage {

    private final String id;

    private final boolean toogle;

    public CButtonGuiElementPacket(String id, boolean toogle) {
        this.id = id;
        this.toogle = toogle;
    }

    @Override
    public MessageType getType() {
        return PacketManager.UPDATE_MACHINE_DATA;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.id);
        buf.writeBoolean(this.toogle);
    }

    public static CButtonGuiElementPacket read(FriendlyByteBuf buf) {
        return new CButtonGuiElementPacket(buf.readUtf(), buf.readBoolean());
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        if (context.getEnvironment() == Env.SERVER && context.getPlayer().m_20194_() != null && context.getPlayer().containerMenu instanceof CustomMachineContainer container) {
            int holdTime = (Integer) container.getTile().getGuiElements().stream().filter(element -> {
                if (element instanceof ButtonGuiElement button && button.getId().equals(this.id)) {
                    return true;
                }
                return false;
            }).findFirst().map(element -> ((ButtonGuiElement) element).getHoldTime()).orElse(-1);
            if (holdTime == -1) {
                return;
            }
            container.getTile().getComponentManager().getComponent((MachineComponentType) Registration.DATA_MACHINE_COMPONENT.get()).ifPresent(component -> {
                if (this.toogle) {
                    component.getData().putBoolean(this.id, !component.getData().getBoolean(this.id));
                } else {
                    component.getData().putBoolean(this.id, true);
                    component.getManager().markDirty();
                    TaskDelayer.enqueue(holdTime, () -> {
                        component.getData().putBoolean(this.id, false);
                        component.getManager().markDirty();
                    });
                }
            });
        }
    }
}