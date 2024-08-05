package noppes.npcs.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import noppes.npcs.CustomEntities;
import noppes.npcs.ModelData;

public class EntityNpcDragon extends EntityNPCInterface {

    private EntityDimensions size = new EntityDimensions(1.8F, 1.4F, false);

    public double[][] field_40162_d;

    public int field_40164_e;

    public float prevAnimTime;

    public float animTime;

    public int field_40178_aA;

    public boolean isFlying = false;

    private boolean exploded = false;

    public EntityNpcDragon(EntityType<? extends EntityNPCInterface> type, Level world) {
        super(type, world);
        this.field_40162_d = new double[64][3];
        this.field_40164_e = -1;
        this.prevAnimTime = 0.0F;
        this.animTime = 0.0F;
        this.field_40178_aA = 0;
        this.scaleX = 0.4F;
        this.scaleY = 0.4F;
        this.scaleZ = 0.4F;
        this.display.setSkinTexture("customnpcs:textures/entity/dragon/blackdragon.png");
    }

    @Override
    public double getPassengersRidingOffset() {
        return 1.1;
    }

    public double[] getMovementOffsets(int i, float f) {
        f = 1.0F - f;
        int j = this.field_40164_e - i * 1 & 63;
        int k = this.field_40164_e - i * 1 - 1 & 63;
        double[] ad = new double[3];
        double d = this.field_40162_d[j][0];
        double d1 = this.field_40162_d[k][0] - d;
        while (d1 < -180.0) {
            d1 += 360.0;
        }
        while (d1 >= 180.0) {
            d1 -= 360.0;
        }
        ad[0] = d + d1 * (double) f;
        d = this.field_40162_d[j][1];
        d1 = this.field_40162_d[k][1] - d;
        ad[1] = d + d1 * (double) f;
        ad[2] = this.field_40162_d[j][2] + (this.field_40162_d[k][2] - this.field_40162_d[j][2]) * (double) f;
        return ad;
    }

    @Override
    public void tick() {
        this.m_146870_();
        this.m_21557_(true);
        if (!this.m_9236_().isClientSide) {
            CompoundTag compound = new CompoundTag();
            this.m_7380_(compound);
            EntityCustomNpc npc = new EntityCustomNpc(CustomEntities.entityCustomNpc, this.m_9236_());
            npc.readAdditionalSaveData(compound);
            ModelData data = npc.modelData;
            data.setEntity(ForgeRegistries.ENTITY_TYPES.getKey(CustomEntities.entityNpcDragon));
            this.m_9236_().m_7967_(npc);
        }
        super.tick();
    }

    @Override
    public void aiStep() {
        this.prevAnimTime = this.animTime;
        if (!this.m_9236_().isClientSide || !(this.m_21223_() <= 0.0F)) {
            this.exploded = false;
            float f1 = 0.045F;
            f1 *= (float) Math.pow(2.0, this.m_20184_().y);
            this.animTime += f1 * 0.5F;
        } else if (!this.exploded) {
            this.exploded = true;
            float f = (this.f_19796_.nextFloat() - 0.5F) * 8.0F;
            float f2 = (this.f_19796_.nextFloat() - 0.5F) * 4.0F;
            float f4 = (this.f_19796_.nextFloat() - 0.5F) * 8.0F;
            this.m_9236_().addParticle(ParticleTypes.EXPLOSION, this.m_20185_() + (double) f, this.m_20186_() + 2.0 + (double) f2, this.m_20189_() + (double) f4, 0.0, 0.0, 0.0);
        }
        super.aiStep();
    }

    @Override
    public EntityDimensions getDimensions(Pose pos) {
        return this.size;
    }
}