package software.bernie.geckolib.network.packet;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.GeoReplacedEntity;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.RenderUtils;

public class EntityAnimTriggerPacket<D> {

    private final int entityId;

    private final boolean isReplacedEntity;

    private final String controllerName;

    private final String animName;

    public EntityAnimTriggerPacket(int entityId, @Nullable String controllerName, String animName) {
        this(entityId, false, controllerName, animName);
    }

    public EntityAnimTriggerPacket(int entityId, boolean isReplacedEntity, @Nullable String controllerName, String animName) {
        this.entityId = entityId;
        this.isReplacedEntity = isReplacedEntity;
        this.controllerName = controllerName == null ? "" : controllerName;
        this.animName = animName;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityId);
        buffer.writeBoolean(this.isReplacedEntity);
        buffer.writeUtf(this.controllerName);
        buffer.writeUtf(this.animName);
    }

    public static <D> EntityAnimTriggerPacket<D> decode(FriendlyByteBuf buffer) {
        return new EntityAnimTriggerPacket<>(buffer.readVarInt(), buffer.readBoolean(), buffer.readUtf(), buffer.readUtf());
    }

    public void receivePacket(Supplier<NetworkEvent.Context> context) {
        NetworkEvent.Context handler = (NetworkEvent.Context) context.get();
        handler.enqueueWork(() -> {
            Entity entity = ClientUtils.getLevel().getEntity(this.entityId);
            if (entity != null) {
                if (this.isReplacedEntity) {
                    if (RenderUtils.getReplacedAnimatable(entity.getType()) instanceof GeoReplacedEntity replacedEntity) {
                        replacedEntity.triggerAnim(entity, this.controllerName.isEmpty() ? null : this.controllerName, this.animName);
                    }
                } else if (entity instanceof GeoEntity geoEntity) {
                    geoEntity.triggerAnim(this.controllerName.isEmpty() ? null : this.controllerName, this.animName);
                }
            }
        });
        handler.setPacketHandled(true);
    }
}