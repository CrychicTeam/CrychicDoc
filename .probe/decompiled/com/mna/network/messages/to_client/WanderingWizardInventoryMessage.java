package com.mna.network.messages.to_client;

import com.mna.ManaAndArtifice;
import com.mna.network.messages.BaseMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.trading.MerchantOffers;

public class WanderingWizardInventoryMessage extends BaseMessage {

    private int containerId;

    private MerchantOffers offers;

    private int level;

    private int xp;

    private boolean hasXPBar;

    private boolean canRestock;

    private boolean isFinalized;

    public WanderingWizardInventoryMessage() {
        this.messageIsValid = false;
    }

    public WanderingWizardInventoryMessage(int id, MerchantOffers offersIn, int levelIn, int xpIn, boolean hasXPBar, boolean canRestock, boolean isFinal) {
        this.containerId = id;
        this.offers = offersIn;
        this.level = levelIn;
        this.xp = xpIn;
        this.hasXPBar = hasXPBar;
        this.canRestock = canRestock;
        this.isFinalized = isFinal;
        this.messageIsValid = true;
    }

    public static WanderingWizardInventoryMessage decode(FriendlyByteBuf buf) {
        WanderingWizardInventoryMessage msg = new WanderingWizardInventoryMessage();
        try {
            msg.containerId = buf.readVarInt();
            msg.offers = MerchantOffers.createFromStream(buf);
            msg.level = buf.readVarInt();
            msg.xp = buf.readVarInt();
            msg.hasXPBar = buf.readBoolean();
            msg.canRestock = buf.readBoolean();
            msg.isFinalized = buf.readBoolean();
        } catch (IndexOutOfBoundsException | IllegalArgumentException var3) {
            ManaAndArtifice.LOGGER.error("Exception while reading WanderingWizardInventoryMessage: " + var3);
            return msg;
        }
        msg.messageIsValid = true;
        return msg;
    }

    public static void encode(WanderingWizardInventoryMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.containerId);
        message.offers.writeToStream(buf);
        buf.writeVarInt(message.level);
        buf.writeVarInt(message.xp);
        buf.writeBoolean(message.hasXPBar);
        buf.writeBoolean(message.canRestock);
        buf.writeBoolean(message.isFinalized);
    }

    public int getContainerId() {
        return this.containerId;
    }

    public MerchantOffers getOffers() {
        return this.offers;
    }

    public int getLevel() {
        return this.level;
    }

    public int getExp() {
        return this.xp;
    }

    public boolean getHasXPBar() {
        return this.hasXPBar;
    }

    public boolean getCanRestock() {
        return this.canRestock;
    }

    public boolean getIsFinal() {
        return this.isFinalized;
    }
}