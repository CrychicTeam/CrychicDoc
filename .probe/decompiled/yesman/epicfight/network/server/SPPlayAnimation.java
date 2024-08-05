package yesman.epicfight.network.server;

import io.netty.buffer.ByteBuf;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class SPPlayAnimation {

    protected int namespaceId;

    protected int animationId;

    protected int entityId;

    protected float convertTimeModifier;

    public SPPlayAnimation() {
        this.animationId = 0;
        this.entityId = 0;
        this.convertTimeModifier = 0.0F;
    }

    public SPPlayAnimation(StaticAnimation animation, float convertTimeModifier, LivingEntityPatch<?> entitypatch) {
        this(animation.getNamespaceId(), animation.getId(), entitypatch.getOriginal().m_19879_(), convertTimeModifier);
    }

    public SPPlayAnimation(StaticAnimation animation, int entityId, float convertTimeModifier) {
        this(animation.getNamespaceId(), animation.getId(), entityId, convertTimeModifier);
    }

    public SPPlayAnimation(int namespaceId, int animationId, int entityId, float convertTimeModifier) {
        this.namespaceId = namespaceId;
        this.animationId = animationId;
        this.entityId = entityId;
        this.convertTimeModifier = convertTimeModifier;
    }

    public <T extends SPPlayAnimation> void onArrive() {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.player.m_9236_().getEntity(this.entityId);
        if (entity != null) {
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            if (entitypatch != null) {
                entitypatch.<Animator>getAnimator().playAnimation(this.namespaceId, this.animationId, this.convertTimeModifier);
            }
        }
    }

    public static SPPlayAnimation fromBytes(FriendlyByteBuf buf) {
        return new SPPlayAnimation(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat());
    }

    public static void toBytes(SPPlayAnimation msg, ByteBuf buf) {
        buf.writeInt(msg.namespaceId);
        buf.writeInt(msg.animationId);
        buf.writeInt(msg.entityId);
        buf.writeFloat(msg.convertTimeModifier);
    }

    public static void handle(SPPlayAnimation msg, Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> msg.onArrive());
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}