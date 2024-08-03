package yesman.epicfight.network.client;

import io.netty.buffer.Unpooled;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class CPExecuteSkill {

    private final int skillSlot;

    private final CPExecuteSkill.WorkType workType;

    private final FriendlyByteBuf buffer;

    public CPExecuteSkill() {
        this(0);
    }

    public CPExecuteSkill(int slotIndex) {
        this(slotIndex, CPExecuteSkill.WorkType.ACTIVATE);
    }

    public CPExecuteSkill(int slotIndex, CPExecuteSkill.WorkType active) {
        this.skillSlot = slotIndex;
        this.workType = active;
        this.buffer = new FriendlyByteBuf(Unpooled.buffer());
    }

    public CPExecuteSkill(int slotIndex, CPExecuteSkill.WorkType active, FriendlyByteBuf pb) {
        this.skillSlot = slotIndex;
        this.workType = active;
        this.buffer = new FriendlyByteBuf(Unpooled.buffer());
        if (pb != null) {
            this.buffer.writeBytes(pb);
        }
    }

    public FriendlyByteBuf getBuffer() {
        return this.buffer;
    }

    public static CPExecuteSkill fromBytes(FriendlyByteBuf buf) {
        CPExecuteSkill msg = new CPExecuteSkill(buf.readInt(), CPExecuteSkill.WorkType.values()[buf.readInt()]);
        while (buf.isReadable()) {
            msg.buffer.writeByte(buf.readByte());
        }
        return msg;
    }

    public static void toBytes(CPExecuteSkill msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.skillSlot);
        buf.writeInt(msg.workType.ordinal());
        while (msg.buffer.isReadable()) {
            buf.writeByte(msg.buffer.readByte());
        }
    }

    public static void handle(CPExecuteSkill msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer serverPlayer = ((NetworkEvent.Context) ctx.get()).getSender();
            ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
            SkillContainer skillContainer = playerpatch.getSkill(msg.skillSlot);
            switch(msg.workType) {
                case ACTIVATE:
                    skillContainer.requestExecute(playerpatch, msg.getBuffer());
                    break;
                case CANCEL:
                    skillContainer.requestCancel(playerpatch, msg.getBuffer());
                    break;
                case CHARGING_START:
                    skillContainer.requestCharging(playerpatch, msg.getBuffer());
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    public static enum WorkType {

        ACTIVATE, CANCEL, CHARGING_START
    }
}