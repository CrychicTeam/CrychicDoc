package yesman.epicfight.network.server;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public class SPPlayAnimationInstant extends SPPlayAnimation {

    public SPPlayAnimationInstant(int namespaceId, int animation, int entityId, float convertTimeModifier) {
        super(namespaceId, animation, entityId, convertTimeModifier);
    }

    public SPPlayAnimationInstant(StaticAnimation animation, float convertTimeModifier, LivingEntityPatch<?> entitypatch) {
        this(animation.getNamespaceId(), animation.getId(), entitypatch.getOriginal().m_19879_(), convertTimeModifier);
    }

    public static SPPlayAnimationInstant fromBytes(FriendlyByteBuf buf) {
        return new SPPlayAnimationInstant(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat());
    }

    @Override
    public void onArrive() {
        Minecraft mc = Minecraft.getInstance();
        Entity entity = mc.player.m_9236_().getEntity(this.entityId);
        if (entity != null) {
            LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(entity, LivingEntityPatch.class);
            entitypatch.<Animator>getAnimator().playAnimationInstantly(this.namespaceId, this.animationId);
            entitypatch.<Animator>getAnimator().poseTick();
            entitypatch.<Animator>getAnimator().poseTick();
        }
    }
}