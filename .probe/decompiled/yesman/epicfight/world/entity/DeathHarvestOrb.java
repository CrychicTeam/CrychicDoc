package yesman.epicfight.world.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;

public class DeathHarvestOrb extends Entity {

    private Player dest;

    private Vec3 randVec;

    private float value;

    public DeathHarvestOrb(EntityType<?> type, Level level) {
        super(type, level);
    }

    public DeathHarvestOrb(Player dest, double x, double y, double z, int value) {
        this(EpicFightEntities.DEATH_HARVEST_ORB.get(), dest.m_9236_());
        this.m_6034_(x, y, z);
        this.dest = dest;
        this.value = (float) value;
        Vec3 toContrast = this.dest.m_20182_().add(0.0, (double) this.dest.m_20206_() * 0.5, 0.0).subtract(this.m_20182_()).scale(-1.0);
        double randX = this.f_19796_.nextDouble() * (toContrast.x > 0.0 ? 1.0 : -1.0);
        double randY = this.f_19796_.nextDouble() * (toContrast.y > 0.0 ? 0.75 : -0.75);
        double randZ = this.f_19796_.nextDouble() * (toContrast.z > 0.0 ? 1.0 : -1.0);
        this.randVec = new Vec3(randX, randY, randZ).normalize();
    }

    @Override
    public void tick() {
        super.baseTick();
        if (!this.m_9236_().isClientSide()) {
            double scaleFactor = Math.pow(Math.max(0.0, (double) (this.f_19797_ - 10) / 10.0), 2.0);
            Vec3 v1 = this.dest.m_20182_().add(0.0, (double) this.dest.m_20206_() * 0.5, 0.0).subtract(this.m_20182_()).scale(scaleFactor);
            Vec3 v2 = this.randVec.scale(1.0 - scaleFactor);
            this.m_6478_(MoverType.SELF, v1.add(v2).scale(0.23));
            for (Entity e : this.m_9236_().m_45933_(this, this.m_20191_())) {
                if (e.is(this.dest)) {
                    ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(this.dest, ServerPlayerPatch.class);
                    if (playerpatch != null) {
                        SkillContainer container = playerpatch.getSkill(SkillSlots.WEAPON_INNATE);
                        container.getSkill().setConsumptionSynchronize(playerpatch, container.getResource() + this.value);
                    }
                    this.m_146870_();
                }
            }
        } else {
            this.m_9236_().addParticle(ParticleTypes.SMOKE, this.m_20185_(), this.m_20186_(), this.m_20189_(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}