package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import com.github.alexmodguy.alexscaves.server.entity.util.AlexsCavesBoat;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class AlexsCavesChestBoatEntity extends ChestBoat implements AlexsCavesBoat {

    public AlexsCavesChestBoatEntity(EntityType type, Level level) {
        super(type, level);
        this.f_19850_ = true;
    }

    public AlexsCavesChestBoatEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.CHEST_BOAT.get(), level);
        this.m_20011_(this.m_142242_());
    }

    public AlexsCavesChestBoatEntity(Level level, double x, double y, double z) {
        this(ACEntityRegistry.CHEST_BOAT.get(), level);
        this.m_6034_(x, y, z);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
    }

    public AlexsCavesChestBoatEntity(Level level, Vec3 location, AlexsCavesBoat.Type type) {
        this(level, location.x, location.y, location.z);
        this.setACBoatType(type);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.putString("ACBoatType", this.getACBoatType().getName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        if (nbt.contains("ACBoatType")) {
            this.f_19804_.set(f_38285_, AlexsCavesBoat.Type.byName(nbt.getString("ACBoatType")).ordinal());
        }
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
        this.f_38281_ = this.m_20184_().y;
        if (!this.m_20159_()) {
            if (onGround) {
                if (this.f_19789_ > 3.0F) {
                    if (this.f_38279_ != Boat.Status.ON_LAND) {
                        this.m_183634_();
                        return;
                    }
                    this.m_142535_(this.f_19789_, 1.0F, this.m_269291_().fall());
                    if (!this.m_9236_().isClientSide && !this.m_213877_()) {
                        this.m_6074_();
                        if (this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            for (int i = 0; i < 3; i++) {
                                this.m_19998_((ItemLike) this.getACBoatType().getPlankSupplier().get());
                            }
                            for (int j = 0; j < 2; j++) {
                                this.m_19998_(Items.STICK);
                            }
                        }
                    }
                }
                this.m_183634_();
            } else if (!this.m_9236_().getFluidState(this.m_20183_().below()).is(FluidTags.WATER) && y < 0.0) {
                this.f_19789_ -= (float) y;
            }
        }
    }

    public void setACBoatType(AlexsCavesBoat.Type type) {
        this.f_19804_.set(f_38285_, type.ordinal());
    }

    @Override
    public AlexsCavesBoat.Type getACBoatType() {
        return AlexsCavesBoat.Type.byId(this.f_19804_.<Integer>get(f_38285_));
    }

    @Override
    public void setVariant(Boat.Type vanillaType) {
    }

    @Override
    public Item getDropItem() {
        return (Item) this.getACBoatType().getChestDropSupplier().get();
    }

    @Override
    public Boat.Type getVariant() {
        return Boat.Type.OAK;
    }
}