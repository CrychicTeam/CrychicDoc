package se.mickelus.tetra.items.modular.impl.bow;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public class ProjectileMotionPacket extends AbstractPacket {

    private int entityId = -1;

    private float motionX;

    private float motionY;

    private float motionZ;

    public ProjectileMotionPacket() {
    }

    public ProjectileMotionPacket(Projectile target) {
        this.entityId = target.m_19879_();
        Vec3 motion = target.m_20184_();
        this.motionX = (float) motion.x;
        this.motionY = (float) motion.y;
        this.motionZ = (float) motion.z;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.entityId);
        buffer.writeFloat(this.motionX);
        buffer.writeFloat(this.motionY);
        buffer.writeFloat(this.motionZ);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.entityId = buffer.readVarInt();
        this.motionX = buffer.readFloat();
        this.motionY = buffer.readFloat();
        this.motionZ = buffer.readFloat();
    }

    @Override
    public void handle(Player player) {
        Optional.of(this.entityId).filter(id -> id != -1).map(id -> player.m_9236_().getEntity(id)).ifPresent(entity -> entity.setDeltaMovement((double) this.motionX, (double) this.motionY, (double) this.motionZ));
    }
}