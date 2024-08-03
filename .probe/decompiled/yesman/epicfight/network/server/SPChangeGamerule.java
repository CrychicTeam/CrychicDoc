package yesman.epicfight.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.world.gamerule.EpicFightGamerules;

public class SPChangeGamerule {

    private final SPChangeGamerule.SynchronizedGameRules gamerule;

    private final int gameruleId;

    private final Object object;

    public SPChangeGamerule() {
        this.gamerule = null;
        this.gameruleId = -1;
        this.object = 0;
    }

    public SPChangeGamerule(SPChangeGamerule.SynchronizedGameRules gamerule, Object object) {
        this.gamerule = gamerule;
        this.gameruleId = gamerule.ordinal();
        this.object = object;
    }

    public static SPChangeGamerule fromBytes(FriendlyByteBuf buf) {
        int id = buf.readInt();
        SPChangeGamerule.SynchronizedGameRules gamerule = SPChangeGamerule.SynchronizedGameRules.values()[id];
        Object obj = null;
        switch(gamerule.valueType) {
            case INTEGER:
                obj = buf.readInt();
                break;
            case BOOLEAN:
                obj = buf.readBoolean();
        }
        return new SPChangeGamerule(gamerule, obj);
    }

    public static void toBytes(SPChangeGamerule msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.gameruleId);
        switch(msg.gamerule.valueType) {
            case INTEGER:
                buf.writeInt((Integer) msg.object);
                break;
            case BOOLEAN:
                buf.writeBoolean((Boolean) msg.object);
        }
    }

    public static void handle(SPChangeGamerule msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            switch(msg.gamerule.valueType) {
                case INTEGER:
                    ((GameRules.IntegerValue) Minecraft.getInstance().level.m_46469_().getRule(msg.gamerule.key)).tryDeserialize(msg.object.toString());
                    break;
                case BOOLEAN:
                    ((GameRules.BooleanValue) Minecraft.getInstance().level.m_46469_().getRule(msg.gamerule.key)).set((Boolean) msg.object, null);
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    public static enum SynchronizedGameRules {

        HAS_FALL_ANIMATION(SPChangeGamerule.SynchronizedGameRules.ValueType.BOOLEAN, EpicFightGamerules.HAS_FALL_ANIMATION), WEIGHT_PENALTY(SPChangeGamerule.SynchronizedGameRules.ValueType.INTEGER, EpicFightGamerules.WEIGHT_PENALTY), DIABLE_ENTITY_UI(SPChangeGamerule.SynchronizedGameRules.ValueType.BOOLEAN, EpicFightGamerules.DISABLE_ENTITY_UI), CAN_SWITCH_COMBAT(SPChangeGamerule.SynchronizedGameRules.ValueType.BOOLEAN, EpicFightGamerules.CAN_SWITCH_COMBAT), STIFF_COMBO_ATTACKS(SPChangeGamerule.SynchronizedGameRules.ValueType.BOOLEAN, EpicFightGamerules.STIFF_COMBO_ATTACKS);

        SPChangeGamerule.SynchronizedGameRules.ValueType valueType;

        GameRules.Key<?> key;

        private SynchronizedGameRules(SPChangeGamerule.SynchronizedGameRules.ValueType valueType, GameRules.Key<?> key) {
            this.valueType = valueType;
            this.key = key;
        }

        static enum ValueType {

            INTEGER, BOOLEAN
        }
    }
}