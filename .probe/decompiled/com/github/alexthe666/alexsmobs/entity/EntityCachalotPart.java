package com.github.alexthe666.alexsmobs.entity;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.message.MessageHurtMultipart;
import com.github.alexthe666.alexsmobs.message.MessageInteractMultipart;
import java.util.List;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.entity.PartEntity;

public class EntityCachalotPart extends PartEntity<EntityCachalotWhale> {

    private final EntityDimensions size;

    public float scale = 1.0F;

    public EntityCachalotPart(EntityCachalotWhale parent, float sizeX, float sizeY) {
        super(parent);
        this.size = EntityDimensions.scalable(sizeX, sizeY);
        this.m_6210_();
    }

    public EntityCachalotPart(EntityCachalotWhale entityCachalotWhale, float sizeX, float sizeY, EntityDimensions size) {
        super(entityCachalotWhale);
        this.size = size;
    }

    protected void collideWithNearbyEntities() {
        List<Entity> entities = this.m_9236_().m_45933_(this, this.m_20191_().expandTowards(0.2, 0.0, 0.2));
        Entity parent = this.getParent();
        if (parent != null) {
            entities.stream().filter(entity -> entity != parent && (!(entity instanceof EntityCachalotPart) || ((EntityCachalotPart) entity).getParent() != parent) && entity.isPushable()).forEach(entity -> entity.push(parent));
        }
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (this.m_9236_().isClientSide && this.getParent() != null) {
            AlexsMobs.sendMSGToServer(new MessageInteractMultipart(this.getParent().m_19879_(), hand == InteractionHand.OFF_HAND));
        }
        return this.getParent() == null ? InteractionResult.PASS : this.getParent().mobInteract(player, hand);
    }

    protected void collideWithEntity(Entity entityIn) {
        entityIn.push(this);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.m_9236_().isClientSide && this.getParent() != null && !this.getParent().m_6673_(source)) {
            ResourceLocation key = ((Registry) this.m_9236_().registryAccess().registry(Registries.DAMAGE_TYPE).get()).getKey(source.type());
            if (key != null) {
                AlexsMobs.sendMSGToServer(new MessageHurtMultipart(this.m_19879_(), this.getParent().m_19879_(), amount, key.toString()));
            }
        }
        return !this.m_6673_(source) && this.getParent().attackEntityPartFrom(this, source, amount);
    }

    @Override
    public boolean is(Entity entityIn) {
        return this == entityIn || this.getParent() == entityIn;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EntityDimensions getDimensions(Pose poseIn) {
        return this.size == null ? EntityDimensions.scalable(0.0F, 0.0F) : this.size.scale(this.scale);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.m_8119_();
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }
}