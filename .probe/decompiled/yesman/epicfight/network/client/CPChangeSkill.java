package yesman.epicfight.network.client;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.api.data.reloader.SkillManager;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class CPChangeSkill {

    private final int skillSlotIndex;

    private final int itemSlotIndex;

    private final String skillName;

    private final boolean consumeXp;

    public CPChangeSkill() {
        this(0, -1, "", false);
    }

    public CPChangeSkill(int skillSlotIndex, int itemSlotIndex, String name, boolean consumeXp) {
        this.skillSlotIndex = skillSlotIndex;
        this.itemSlotIndex = itemSlotIndex;
        this.skillName = name;
        this.consumeXp = consumeXp;
    }

    public static CPChangeSkill fromBytes(FriendlyByteBuf buf) {
        return new CPChangeSkill(buf.readInt(), buf.readInt(), buf.readUtf(), buf.readBoolean());
    }

    public static void toBytes(CPChangeSkill msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.skillSlotIndex);
        buf.writeInt(msg.itemSlotIndex);
        buf.writeUtf(msg.skillName);
        buf.writeBoolean(msg.consumeXp);
    }

    public static void handle(CPChangeSkill msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            ServerPlayer serverPlayer = ((NetworkEvent.Context) ctx.get()).getSender();
            ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(serverPlayer, ServerPlayerPatch.class);
            if (playerpatch != null) {
                Skill skill = SkillManager.getSkill(msg.skillName);
                playerpatch.getSkill(msg.skillSlotIndex).setSkill(skill);
                if (skill.getCategory().learnable()) {
                    playerpatch.getSkillCapability().addLearnedSkill(skill);
                }
                if (msg.consumeXp) {
                    serverPlayer.giveExperienceLevels(-skill.getRequiredXp());
                } else if (!serverPlayer.isCreative()) {
                    serverPlayer.m_150109_().removeItem(serverPlayer.m_150109_().getItem(msg.itemSlotIndex));
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}