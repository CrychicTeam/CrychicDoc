package se.mickelus.tetra.items.modular;

import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import se.mickelus.mutil.network.BlockPosPacket;

@ParametersAreNonnullByDefault
public class ChargedAbilityPacket extends BlockPosPacket {

    private int targetId = -1;

    private InteractionHand hand;

    private int ticksUsed;

    private Vec3 hitVec;

    public ChargedAbilityPacket() {
    }

    public ChargedAbilityPacket(LivingEntity target, BlockPos pos, Vec3 hitVec, InteractionHand hand, int ticksUsed) {
        super(pos == null ? BlockPos.ZERO : pos);
        this.targetId = (Integer) Optional.ofNullable(target).map(Entity::m_19879_).orElse(-1);
        this.hand = hand;
        this.ticksUsed = ticksUsed;
        this.hitVec = hitVec == null ? Vec3.ZERO : hitVec;
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        super.toBytes(buffer);
        buffer.writeInt(this.targetId);
        buffer.writeInt(this.hand.ordinal());
        buffer.writeInt(this.ticksUsed);
        buffer.writeDouble(this.hitVec.x);
        buffer.writeDouble(this.hitVec.y);
        buffer.writeDouble(this.hitVec.z);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        super.fromBytes(buffer);
        this.targetId = buffer.readInt();
        this.hand = InteractionHand.values()[buffer.readInt()];
        this.ticksUsed = buffer.readInt();
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        this.hitVec = new Vec3(x, y, z);
    }

    @Override
    public void handle(Player player) {
        LivingEntity target = (LivingEntity) Optional.of(this.targetId).filter(id -> id != -1).map(id -> player.m_9236_().getEntity(id)).filter(entity -> entity instanceof LivingEntity).map(entity -> (LivingEntity) entity).orElse(null);
        ItemModularHandheld.handleChargedAbility(player, this.hand, target, BlockPos.ZERO.equals(this.pos) ? null : this.pos, Vec3.ZERO.equals(this.hitVec) ? null : this.hitVec, this.ticksUsed);
    }
}