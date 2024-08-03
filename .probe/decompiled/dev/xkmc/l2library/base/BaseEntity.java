package dev.xkmc.l2library.base;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.codec.PacketCodec;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.l2serial.util.Wrappers;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

@SerialClass
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class BaseEntity extends Entity implements IEntityAdditionalSpawnData {

    public BaseEntity(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.put("auto-serial", TagCodec.toTag(new CompoundTag(), this));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("auto-serial")) {
            Wrappers.run(() -> TagCodec.fromTag(tag.getCompound("auto-serial"), this.getClass(), this, f -> true));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        PacketCodec.to(buffer, this);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf data) {
        PacketCodec.from(data, this.getClass(), this);
    }
}