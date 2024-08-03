package com.mna.network.messages.to_server;

import com.mna.ManaAndArtifice;
import com.mna.api.items.inventory.SpellInventory;
import com.mna.gui.containers.item.ContainerRoteBook;
import com.mna.network.messages.BaseMessage;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class RoteSpellsSyncMessageToServer extends BaseMessage {

    private NonNullList<ItemStack> _roteInventory = NonNullList.create();

    public RoteSpellsSyncMessageToServer() {
        this.messageIsValid = false;
    }

    public RoteSpellsSyncMessageToServer(SpellInventory roteInventory) {
        this();
        for (int i = 0; i < roteInventory.m_6643_(); i++) {
            this._roteInventory.add(roteInventory.m_8020_(i).copy());
        }
        this.messageIsValid = true;
    }

    public final NonNullList<ItemStack> getInventory() {
        return this._roteInventory;
    }

    public static final RoteSpellsSyncMessageToServer decode(FriendlyByteBuf buf) {
        RoteSpellsSyncMessageToServer msg = new RoteSpellsSyncMessageToServer();
        try {
            int count = buf.readInt();
            for (int i = 0; i < count; i++) {
                msg._roteInventory.add(buf.readItem());
            }
        } catch (IndexOutOfBoundsException | IllegalArgumentException var4) {
            ManaAndArtifice.LOGGER.error("Exception while reading RoteSpellsSyncMessageToServer: " + var4);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static final void encode(RoteSpellsSyncMessageToServer msg, FriendlyByteBuf buf) {
        buf.writeInt(msg._roteInventory.size());
        for (int i = 0; i < msg._roteInventory.size(); i++) {
            buf.writeItem(msg._roteInventory.get(i));
        }
    }

    public static final RoteSpellsSyncMessageToServer fromRoteBookContainer(ContainerRoteBook container) {
        return new RoteSpellsSyncMessageToServer(container.roteBook);
    }
}