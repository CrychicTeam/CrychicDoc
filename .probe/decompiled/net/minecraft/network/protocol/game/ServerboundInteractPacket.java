package net.minecraft.network.protocol.game;

import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ServerboundInteractPacket implements Packet<ServerGamePacketListener> {

    private final int entityId;

    private final ServerboundInteractPacket.Action action;

    private final boolean usingSecondaryAction;

    static final ServerboundInteractPacket.Action ATTACK_ACTION = new ServerboundInteractPacket.Action() {

        @Override
        public ServerboundInteractPacket.ActionType getType() {
            return ServerboundInteractPacket.ActionType.ATTACK;
        }

        @Override
        public void dispatch(ServerboundInteractPacket.Handler p_179624_) {
            p_179624_.onAttack();
        }

        @Override
        public void write(FriendlyByteBuf p_179622_) {
        }
    };

    private ServerboundInteractPacket(int int0, boolean boolean1, ServerboundInteractPacket.Action serverboundInteractPacketAction2) {
        this.entityId = int0;
        this.action = serverboundInteractPacketAction2;
        this.usingSecondaryAction = boolean1;
    }

    public static ServerboundInteractPacket createAttackPacket(Entity entity0, boolean boolean1) {
        return new ServerboundInteractPacket(entity0.getId(), boolean1, ATTACK_ACTION);
    }

    public static ServerboundInteractPacket createInteractionPacket(Entity entity0, boolean boolean1, InteractionHand interactionHand2) {
        return new ServerboundInteractPacket(entity0.getId(), boolean1, new ServerboundInteractPacket.InteractionAction(interactionHand2));
    }

    public static ServerboundInteractPacket createInteractionPacket(Entity entity0, boolean boolean1, InteractionHand interactionHand2, Vec3 vec3) {
        return new ServerboundInteractPacket(entity0.getId(), boolean1, new ServerboundInteractPacket.InteractionAtLocationAction(interactionHand2, vec3));
    }

    public ServerboundInteractPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.entityId = friendlyByteBuf0.readVarInt();
        ServerboundInteractPacket.ActionType $$1 = friendlyByteBuf0.readEnum(ServerboundInteractPacket.ActionType.class);
        this.action = (ServerboundInteractPacket.Action) $$1.reader.apply(friendlyByteBuf0);
        this.usingSecondaryAction = friendlyByteBuf0.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeVarInt(this.entityId);
        friendlyByteBuf0.writeEnum(this.action.getType());
        this.action.write(friendlyByteBuf0);
        friendlyByteBuf0.writeBoolean(this.usingSecondaryAction);
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleInteract(this);
    }

    @Nullable
    public Entity getTarget(ServerLevel serverLevel0) {
        return serverLevel0.getEntityOrPart(this.entityId);
    }

    public boolean isUsingSecondaryAction() {
        return this.usingSecondaryAction;
    }

    public void dispatch(ServerboundInteractPacket.Handler serverboundInteractPacketHandler0) {
        this.action.dispatch(serverboundInteractPacketHandler0);
    }

    interface Action {

        ServerboundInteractPacket.ActionType getType();

        void dispatch(ServerboundInteractPacket.Handler var1);

        void write(FriendlyByteBuf var1);
    }

    static enum ActionType {

        INTERACT(ServerboundInteractPacket.InteractionAction::new), ATTACK(p_179639_ -> ServerboundInteractPacket.ATTACK_ACTION), INTERACT_AT(ServerboundInteractPacket.InteractionAtLocationAction::new);

        final Function<FriendlyByteBuf, ServerboundInteractPacket.Action> reader;

        private ActionType(Function<FriendlyByteBuf, ServerboundInteractPacket.Action> p_179636_) {
            this.reader = p_179636_;
        }
    }

    public interface Handler {

        void onInteraction(InteractionHand var1);

        void onInteraction(InteractionHand var1, Vec3 var2);

        void onAttack();
    }

    static class InteractionAction implements ServerboundInteractPacket.Action {

        private final InteractionHand hand;

        InteractionAction(InteractionHand interactionHand0) {
            this.hand = interactionHand0;
        }

        private InteractionAction(FriendlyByteBuf friendlyByteBuf0) {
            this.hand = friendlyByteBuf0.readEnum(InteractionHand.class);
        }

        @Override
        public ServerboundInteractPacket.ActionType getType() {
            return ServerboundInteractPacket.ActionType.INTERACT;
        }

        @Override
        public void dispatch(ServerboundInteractPacket.Handler serverboundInteractPacketHandler0) {
            serverboundInteractPacketHandler0.onInteraction(this.hand);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeEnum(this.hand);
        }
    }

    static class InteractionAtLocationAction implements ServerboundInteractPacket.Action {

        private final InteractionHand hand;

        private final Vec3 location;

        InteractionAtLocationAction(InteractionHand interactionHand0, Vec3 vec1) {
            this.hand = interactionHand0;
            this.location = vec1;
        }

        private InteractionAtLocationAction(FriendlyByteBuf friendlyByteBuf0) {
            this.location = new Vec3((double) friendlyByteBuf0.readFloat(), (double) friendlyByteBuf0.readFloat(), (double) friendlyByteBuf0.readFloat());
            this.hand = friendlyByteBuf0.readEnum(InteractionHand.class);
        }

        @Override
        public ServerboundInteractPacket.ActionType getType() {
            return ServerboundInteractPacket.ActionType.INTERACT_AT;
        }

        @Override
        public void dispatch(ServerboundInteractPacket.Handler serverboundInteractPacketHandler0) {
            serverboundInteractPacketHandler0.onInteraction(this.hand, this.location);
        }

        @Override
        public void write(FriendlyByteBuf friendlyByteBuf0) {
            friendlyByteBuf0.writeFloat((float) this.location.x);
            friendlyByteBuf0.writeFloat((float) this.location.y);
            friendlyByteBuf0.writeFloat((float) this.location.z);
            friendlyByteBuf0.writeEnum(this.hand);
        }
    }
}