package se.mickelus.tetra.items.modular.impl.toolbelt;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;
import se.mickelus.tetra.items.modular.impl.toolbelt.inventory.ToolbeltSlotType;

@ParametersAreNonnullByDefault
public class EquipToolbeltItemPacket extends AbstractPacket {

    private ToolbeltSlotType slotType;

    private int toolbeltItemIndex;

    private InteractionHand hand;

    public EquipToolbeltItemPacket() {
    }

    public EquipToolbeltItemPacket(ToolbeltSlotType inventoryType, int toolbeltSlot, InteractionHand hand) {
        this.slotType = inventoryType;
        this.toolbeltItemIndex = toolbeltSlot;
        this.hand = hand;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeInt(this.slotType.ordinal());
        buffer.writeInt(this.hand.ordinal());
        buffer.writeInt(this.toolbeltItemIndex);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        int typeOrdinal = buffer.readInt();
        if (typeOrdinal < ToolbeltSlotType.values().length) {
            this.slotType = ToolbeltSlotType.values()[typeOrdinal];
        }
        int handOrdinal = buffer.readInt();
        if (handOrdinal < InteractionHand.values().length) {
            this.hand = InteractionHand.values()[handOrdinal];
        }
        this.toolbeltItemIndex = buffer.readInt();
    }

    @Override
    public void handle(Player player) {
        ToolbeltHelper.equipItemFromToolbelt(player, this.slotType, this.toolbeltItemIndex, this.hand);
    }
}