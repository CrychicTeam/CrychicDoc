package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class EntityStymphalianArrow extends AbstractArrow {

    public EntityStymphalianArrow(EntityType<? extends AbstractArrow> t, Level worldIn) {
        super(t, worldIn);
        this.m_36781_(3.5);
    }

    public EntityStymphalianArrow(EntityType<? extends AbstractArrow> t, Level worldIn, double x, double y, double z) {
        this(t, worldIn);
        this.m_6034_(x, y, z);
        this.m_36781_(3.5);
    }

    public EntityStymphalianArrow(PlayMessages.SpawnEntity spawnEntity, Level world) {
        this(IafEntityRegistry.STYMPHALIAN_ARROW.get(), world);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public EntityStymphalianArrow(EntityType t, Level worldIn, LivingEntity shooter) {
        super(t, shooter, worldIn);
        this.m_36781_(3.5);
    }

    @Override
    public void tick() {
        super.tick();
        float sqrt = Mth.sqrt((float) (this.m_20184_().x * this.m_20184_().x + this.m_20184_().z * this.m_20184_().z));
        if (sqrt < 0.1F) {
            this.m_20256_(this.m_20184_().add(0.0, -0.01F, 0.0));
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(IafItemRegistry.STYMPHALIAN_ARROW.get());
    }
}