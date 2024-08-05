package com.mna.entities.utility;

import com.mna.api.config.GeneralConfigValues;
import com.mna.entities.EntityInit;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

public class DisplayDamage extends Entity implements IEntityAdditionalSpawnData {

    public static final int AGE_TIME = 30;

    public static final int FADE_OUT_START = 10;

    public static final int FADE_OUT_DURATION = 20;

    public static final int DEFAULT_COLOR = 16777215;

    float amount = 0.0F;

    int textColor = 16777215;

    public DisplayDamage(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public DisplayDamage(Level pLevel, DamageSource source, float amount) {
        this(EntityInit.DAMAGE_NUMBERS.get(), pLevel);
        this.amount = amount;
        GeneralConfigValues.InsightDamageColors.stream().filter(s -> s.contains(source.getMsgId() + ":")).findFirst().ifPresent(s -> {
            String color = s.split(":")[1];
            try {
                this.textColor = Integer.decode(color.replace("0x", "#"));
            } catch (Throwable var4) {
            }
        });
    }

    @Override
    public void tick() {
        if (++this.f_19797_ >= 30) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        }
        this.m_6478_(MoverType.SELF, new Vec3(0.0, 0.01F, 0.0));
    }

    public float getAmount() {
        return this.amount;
    }

    public int getColor() {
        return this.textColor | this.getTextAlpha() << 24;
    }

    public int getTextAlpha() {
        return this.f_19797_ < 10 ? 255 : 255 - 255 * (this.f_19797_ - 10) / 20;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeFloat(this.amount);
        buffer.writeInt(this.textColor);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.amount = additionalData.readFloat();
        this.textColor = additionalData.readInt();
    }
}