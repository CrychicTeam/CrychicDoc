package com.mna.entities.sorcery;

import com.mna.gui.containers.providers.NamedRift;
import com.mna.tools.GuiTools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class Rift extends Entity implements IEntityAdditionalSpawnData {

    private static final String KEY_AGE = "age";

    private static final String KEY_IS_ENDERCHEST = "isEnderchest";

    private int age = 0;

    private boolean isEnderChest = false;

    public Rift(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    @Override
    public void tick() {
        this.setAge(this.getAge() + 1);
        if (this.getAge() >= 200 && !this.m_9236_().isClientSide()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        if (!player.m_9236_().isClientSide()) {
            if (this.isEnderChest) {
                GuiTools.openEnderChest(player);
            } else {
                NetworkHooks.openScreen((ServerPlayer) player, new NamedRift());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void baseTick() {
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("age")) {
            this.setAge(compound.getInt("age"));
        }
        if (compound.contains("isEnderchest")) {
            this.setEnderChest(compound.getBoolean("isEnderchest"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("age", this.getAge());
        compound.putBoolean("isEnderchest", this.isEnderChest);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public int getAge() {
        return this.age;
    }

    public boolean getIsEnderChest() {
        return this.isEnderChest;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEnderChest(boolean enderChest) {
        this.isEnderChest = enderChest;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.age);
        buffer.writeBoolean(this.isEnderChest);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.age = additionalData.readInt();
        this.isEnderChest = additionalData.readBoolean();
    }
}