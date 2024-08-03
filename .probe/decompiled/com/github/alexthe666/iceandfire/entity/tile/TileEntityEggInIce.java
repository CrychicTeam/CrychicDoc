package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDragonEgg;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class TileEntityEggInIce extends BlockEntity {

    public EnumDragonEgg type;

    public int age;

    public int ticksExisted;

    @Nullable
    public UUID ownerUUID;

    private boolean spawned;

    public TileEntityEggInIce(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.EGG_IN_ICE.get(), pos, state);
    }

    public static void tickEgg(Level level, BlockPos pos, BlockState state, TileEntityEggInIce entityEggInIce) {
        entityEggInIce.age++;
        if (entityEggInIce.age >= IafConfig.dragonEggTime && entityEggInIce.type != null && !entityEggInIce.spawned && !level.isClientSide) {
            EntityIceDragon dragon = new EntityIceDragon(level);
            dragon.m_6034_((double) pos.m_123341_() + 0.5, (double) (pos.m_123342_() + 1), (double) pos.m_123343_() + 0.5);
            dragon.setVariant(entityEggInIce.type.ordinal() - 4);
            dragon.setGender(ThreadLocalRandom.current().nextBoolean());
            dragon.m_7105_(true);
            dragon.setHunger(50);
            dragon.m_21816_(entityEggInIce.ownerUUID);
            level.m_7967_(dragon);
            entityEggInIce.spawned = true;
            level.m_46961_(pos, false);
            level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        }
        entityEggInIce.ticksExisted++;
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag tag) {
        if (this.type != null) {
            tag.putByte("Color", (byte) this.type.ordinal());
        } else {
            tag.putByte("Color", (byte) 0);
        }
        tag.putInt("Age", this.age);
        if (this.ownerUUID == null) {
            tag.putString("OwnerUUID", "");
        } else {
            tag.putUUID("OwnerUUID", this.ownerUUID);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        this.type = EnumDragonEgg.values()[tag.getByte("Color")];
        this.age = tag.getInt("Age");
        UUID s = null;
        if (tag.hasUUID("OwnerUUID")) {
            s = tag.getUUID("OwnerUUID");
        } else {
            try {
                String s1 = tag.getString("OwnerUUID");
                s = OldUsersConverter.convertMobOwnerIfNecessary(this.f_58857_.getServer(), s1);
            } catch (Exception var4) {
            }
        }
        if (s != null) {
            this.ownerUUID = s;
        }
    }

    public void handleUpdateTag(CompoundTag parentNBTTagCompound) {
        this.load(parentNBTTagCompound);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag nbtTagCompound = new CompoundTag();
        this.saveAdditional(nbtTagCompound);
        return nbtTagCompound;
    }

    @Nullable
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbtTagCompound = new CompoundTag();
        this.saveAdditional(nbtTagCompound);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
    }

    public void spawnEgg() {
        if (this.type != null) {
            EntityDragonEgg egg = new EntityDragonEgg(IafEntityRegistry.DRAGON_EGG.get(), this.f_58857_);
            egg.setEggType(this.type);
            egg.m_6034_((double) this.f_58858_.m_123341_() + 0.5, (double) (this.f_58858_.m_123342_() + 1), (double) this.f_58858_.m_123343_() + 0.5);
            egg.setOwnerId(this.ownerUUID);
            if (!this.f_58857_.isClientSide) {
                this.f_58857_.m_7967_(egg);
            }
        }
    }
}