package yesman.epicfight.network.server;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class SPChangeLivingMotion {

    private final int entityId;

    private int count;

    private final boolean setChangesAsDefault;

    private List<LivingMotion> motionList = Lists.newArrayList();

    private List<StaticAnimation> animationList = Lists.newArrayList();

    public SPChangeLivingMotion() {
        this(-1);
    }

    public SPChangeLivingMotion(int entityId) {
        this(entityId, 0, false);
    }

    public SPChangeLivingMotion(int entityId, boolean setChangesAsDefault) {
        this(entityId, 0, setChangesAsDefault);
    }

    private SPChangeLivingMotion(int entityId, int count, boolean setChangesAsDefault) {
        this.entityId = entityId;
        this.count = count;
        this.setChangesAsDefault = setChangesAsDefault;
    }

    public SPChangeLivingMotion putPair(LivingMotion motion, StaticAnimation animation) {
        if (animation != null) {
            this.motionList.add(motion);
            this.animationList.add(animation);
            this.count++;
        }
        return this;
    }

    public void putEntries(Set<Entry<LivingMotion, StaticAnimation>> motionSet) {
        motionSet.forEach(entry -> {
            if (entry.getValue() != null) {
                this.motionList.add((LivingMotion) entry.getKey());
                this.animationList.add((StaticAnimation) entry.getValue());
                this.count++;
            }
        });
    }

    public static SPChangeLivingMotion fromBytes(FriendlyByteBuf buf) {
        SPChangeLivingMotion msg = new SPChangeLivingMotion(buf.readInt(), buf.readInt(), buf.readBoolean());
        List<LivingMotion> motionList = Lists.newArrayList();
        List<StaticAnimation> animationList = Lists.newArrayList();
        for (int i = 0; i < msg.count; i++) {
            motionList.add(LivingMotion.ENUM_MANAGER.get(buf.readInt()));
        }
        for (int i = 0; i < msg.count; i++) {
            animationList.add(EpicFightMod.getInstance().animationManager.findAnimationById(buf.readInt(), buf.readInt()));
        }
        msg.motionList = motionList;
        msg.animationList = animationList;
        return msg;
    }

    public static void toBytes(SPChangeLivingMotion msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.count);
        buf.writeBoolean(msg.setChangesAsDefault);
        for (LivingMotion motion : msg.motionList) {
            buf.writeInt(motion.universalOrdinal());
        }
        for (StaticAnimation anim : msg.animationList) {
            buf.writeInt(anim.getNamespaceId());
            buf.writeInt(anim.getId());
        }
    }

    public static void handle(SPChangeLivingMotion msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Entity entity = mc.player.m_9236_().getEntity(msg.entityId);
            if (entity != null && entity.getCapability(EpicFightCapabilities.CAPABILITY_ENTITY).orElse(null) instanceof LivingEntityPatch<?> entitypatch) {
                ClientAnimator animator = entitypatch.getClientAnimator();
                animator.resetLivingAnimations();
                animator.offAllLayers();
                animator.resetMotion();
                animator.resetCompositeMotion();
                for (int i = 0; i < msg.count; i++) {
                    entitypatch.getClientAnimator().addLivingAnimation((LivingMotion) msg.motionList.get(i), (StaticAnimation) msg.animationList.get(i));
                }
                if (msg.setChangesAsDefault) {
                    animator.setCurrentMotionsAsDefault();
                }
            }
        });
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}