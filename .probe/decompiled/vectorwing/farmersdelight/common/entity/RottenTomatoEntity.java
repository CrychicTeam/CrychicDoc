package vectorwing.farmersdelight.common.entity;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import vectorwing.farmersdelight.common.registry.ModEntityTypes;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RottenTomatoEntity extends ThrowableItemProjectile {

    public RottenTomatoEntity(EntityType<? extends RottenTomatoEntity> entityType, Level level) {
        super(entityType, level);
    }

    public RottenTomatoEntity(Level level, LivingEntity entity) {
        super(ModEntityTypes.ROTTEN_TOMATO.get(), entity, level);
    }

    public RottenTomatoEntity(Level level, double x, double y, double z) {
        super(ModEntityTypes.ROTTEN_TOMATO.get(), x, y, z, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.ROTTEN_TOMATO.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        ItemStack entityStack = new ItemStack(this.getDefaultItem());
        if (id == 3) {
            ParticleOptions iparticledata = new ItemParticleOption(ParticleTypes.ITEM, entityStack);
            for (int i = 0; i < 12; i++) {
                this.m_9236_().addParticle(iparticledata, this.m_20185_(), this.m_20186_(), this.m_20189_(), ((double) this.f_19796_.nextFloat() * 2.0 - 1.0) * 0.1F, ((double) this.f_19796_.nextFloat() * 2.0 - 1.0) * 0.1F + 0.1F, ((double) this.f_19796_.nextFloat() * 2.0 - 1.0) * 0.1F);
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.m_5790_(result);
        Entity entity = result.getEntity();
        entity.hurt(this.m_269291_().thrown(this, this.m_19749_()), 0.0F);
        this.m_5496_(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
    }

    @Override
    protected void onHit(HitResult result) {
        super.m_6532_(result);
        if (!this.m_9236_().isClientSide) {
            this.m_9236_().broadcastEntityEvent(this, (byte) 3);
            this.m_5496_(ModSounds.ENTITY_ROTTEN_TOMATO_HIT.get(), 1.0F, (this.f_19796_.nextFloat() - this.f_19796_.nextFloat()) * 0.2F + 1.0F);
            this.m_146870_();
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}