package yesman.epicfight.network.server;

import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class SPPotion {

    private final MobEffectInstance effectInstance;

    private final SPPotion.Action action;

    private final int entityId;

    public SPPotion() {
        this.effectInstance = null;
        this.entityId = 0;
        this.action = SPPotion.Action.REMOVE;
    }

    public SPPotion(MobEffectInstance effect, SPPotion.Action action, int entityId) {
        this.effectInstance = effect;
        this.entityId = entityId;
        this.action = action;
    }

    public static SPPotion fromBytes(FriendlyByteBuf buf) {
        MobEffect effect = MobEffect.byId(buf.readInt());
        MobEffectInstance effectInstance = new MobEffectInstance(effect, 0, buf.readInt());
        int entityId = buf.readInt();
        SPPotion.Action action = SPPotion.Action.getAction(buf.readInt());
        return new SPPotion(effectInstance, action, entityId);
    }

    public static void toBytes(SPPotion msg, FriendlyByteBuf buf) {
        buf.writeInt(MobEffect.getId(msg.effectInstance.getEffect()));
        buf.writeInt(msg.effectInstance.getAmplifier());
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.action.getSymb());
    }

    public static void handle(SPPotion msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.level.getEntity(msg.entityId);
            if (entity != null && entity instanceof LivingEntity livEntity) {
                switch(msg.action) {
                    case ACTIVATE:
                        livEntity.addEffect(msg.effectInstance);
                        break;
                    case REMOVE:
                        livEntity.removeEffect(msg.effectInstance.getEffect());
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }

    public static enum Action {

        ACTIVATE(0), REMOVE(1);

        int action;

        private Action(int action) {
            this.action = action;
        }

        public int getSymb() {
            return this.action;
        }

        private static SPPotion.Action getAction(int symb) {
            if (symb == 0) {
                return ACTIVATE;
            } else {
                return symb == 1 ? REMOVE : null;
            }
        }
    }
}