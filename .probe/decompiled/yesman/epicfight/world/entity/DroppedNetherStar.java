package yesman.epicfight.world.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;

public class DroppedNetherStar extends ItemEntity {

    public DroppedNetherStar(EntityType<? extends DroppedNetherStar> entityType, Level level) {
        super(entityType, level);
    }

    public DroppedNetherStar(Level level, double x, double y, double z, ItemStack itemstack, double dx, double dy, double dz) {
        this(EpicFightEntities.DROPPED_NETHER_STAR.get(), level);
        this.m_6034_(x, y, z);
        this.m_20334_(dx, dy, dz);
        this.m_32045_(itemstack);
        this.lifespan = itemstack.getItem() == null ? 6000 : itemstack.getEntityLifespan(level);
        this.f_19794_ = true;
        this.m_32010_(30);
        this.m_20242_(true);
    }

    public DroppedNetherStar(Level level, Vec3 position, Vec3 deltaMovement) {
        this(level, position.x, position.y, position.z, new ItemStack(Items.NETHER_STAR), deltaMovement.x, deltaMovement.y, deltaMovement.z);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.f_19797_ % 70 == 0) {
            this.m_9236_().playLocalSound(this.m_20185_(), this.m_20186_(), this.m_20189_(), EpicFightSounds.NETHER_STAR_GLITTER.get(), this.m_5720_(), 1.0F, 1.0F, false);
        }
        Vec3 deltaMove = this.m_20184_();
        if (this.m_9236_().isClientSide()) {
            Vec3 particleDeltaMove = new Vec3(-deltaMove.x, -1.0, -deltaMove.z).normalize().add((double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F), 0.0, (double) ((this.f_19796_.nextFloat() - 0.5F) * 0.1F));
            this.m_9236_().addParticle(EpicFightParticles.NORMAL_DUST.get(), this.m_20185_() + (double) ((this.f_19796_.nextFloat() - 0.5F) * this.m_20205_()), this.m_20186_() + (double) this.m_20206_() * 2.5, this.m_20189_() + (double) ((this.f_19796_.nextFloat() - 0.5F) * this.m_20205_()), particleDeltaMove.x, 0.0, particleDeltaMove.z);
        }
        this.m_20256_(deltaMove.multiply(0.68, 0.68, 0.68));
    }

    @Override
    public boolean isOnFire() {
        return true;
    }

    @Override
    public boolean displayFireAnimation() {
        return false;
    }
}