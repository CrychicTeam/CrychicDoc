package yesman.epicfight.network.server;

import io.netty.buffer.Unpooled;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.SkillContainer;

public class SPSkillExecutionFeedback {

    private final int skillSlot;

    private SPSkillExecutionFeedback.FeedbackType feedbackType;

    private final FriendlyByteBuf buffer;

    public SPSkillExecutionFeedback() {
        this(0, SPSkillExecutionFeedback.FeedbackType.EXECUTED);
    }

    public static SPSkillExecutionFeedback executed(int slotIndex) {
        return new SPSkillExecutionFeedback(slotIndex, SPSkillExecutionFeedback.FeedbackType.EXECUTED);
    }

    public static SPSkillExecutionFeedback expired(int slotIndex) {
        return new SPSkillExecutionFeedback(slotIndex, SPSkillExecutionFeedback.FeedbackType.EXPIRED);
    }

    public static SPSkillExecutionFeedback chargingBegin(int slotIndex) {
        return new SPSkillExecutionFeedback(slotIndex, SPSkillExecutionFeedback.FeedbackType.CHARGING_BEGIN);
    }

    private SPSkillExecutionFeedback(int slotIndex, SPSkillExecutionFeedback.FeedbackType feedbackType) {
        this.skillSlot = slotIndex;
        this.feedbackType = feedbackType;
        this.buffer = new FriendlyByteBuf(Unpooled.buffer());
    }

    public FriendlyByteBuf getBuffer() {
        return this.buffer;
    }

    public void setFeedbackType(SPSkillExecutionFeedback.FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public static SPSkillExecutionFeedback fromBytes(FriendlyByteBuf buf) {
        SPSkillExecutionFeedback msg = new SPSkillExecutionFeedback(buf.readInt(), SPSkillExecutionFeedback.FeedbackType.values()[buf.readInt()]);
        while (buf.isReadable()) {
            msg.buffer.writeByte(buf.readByte());
        }
        return msg;
    }

    public static void toBytes(SPSkillExecutionFeedback msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.skillSlot);
        buf.writeInt(msg.feedbackType.ordinal());
        while (msg.buffer.isReadable()) {
            buf.writeByte(msg.buffer.readByte());
        }
    }

    public static void handle(SPSkillExecutionFeedback msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            LocalPlayerPatch playerpatch = ClientEngine.getInstance().getPlayerPatch();
            if (playerpatch != null) {
                switch(msg.feedbackType) {
                    case EXECUTED:
                        playerpatch.getSkill(msg.skillSlot).getSkill().executeOnClient(playerpatch, msg.getBuffer());
                        break;
                    case CHARGING_BEGIN:
                        SkillContainer skillContainer = playerpatch.getSkill(msg.skillSlot);
                        if (skillContainer.getSkill() instanceof ChargeableSkill chargeableSkill) {
                            playerpatch.startSkillCharging(chargeableSkill);
                            ClientEngine.getInstance().controllEngine.setChargingKey(skillContainer.getSlot(), chargeableSkill.getKeyMapping());
                        }
                        break;
                    case EXPIRED:
                        playerpatch.getSkill(msg.skillSlot).getSkill().cancelOnClient(playerpatch, msg.getBuffer());
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    public static enum FeedbackType {

        EXECUTED, CHARGING_BEGIN, EXPIRED
    }
}