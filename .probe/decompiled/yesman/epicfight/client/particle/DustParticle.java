package yesman.epicfight.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.particle.EpicFightParticles;

@OnlyIn(Dist.CLIENT)
public class DustParticle extends TextureSheetParticle {

    private final DustParticle.PhysicsType physicsType;

    public DustParticle(ClientLevel level, double x, double y, double z, double xd, double yd, double zd, DustParticle.PhysicsType physicsType) {
        super(level, x, y, z);
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
        this.f_107663_ = physicsType == DustParticle.PhysicsType.NORMAL ? this.f_107223_.nextFloat() * 0.01F + 0.01F : this.f_107223_.nextFloat() * 0.02F + 0.02F;
        this.f_107225_ = (physicsType == DustParticle.PhysicsType.NORMAL ? 12 : 2) + this.f_107223_.nextInt(6);
        this.f_107219_ = physicsType == DustParticle.PhysicsType.NORMAL;
        this.f_107226_ = physicsType == DustParticle.PhysicsType.NORMAL ? 0.68F : 0.0F;
        float angle = this.f_107223_.nextFloat() * 360.0F;
        this.f_107231_ = angle;
        this.f_107204_ = angle;
        Vec3 deltaMovement = physicsType.function.getDeltaMovement(xd, yd, zd);
        this.f_107215_ = deltaMovement.x;
        this.f_107216_ = deltaMovement.y;
        this.f_107217_ = deltaMovement.z;
        this.physicsType = physicsType;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return EpicFightParticleRenderTypes.BLEND_LIGHTMAP_PARTICLE;
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (this.physicsType == DustParticle.PhysicsType.EXPANSIVE) {
            this.f_107215_ *= 0.48;
            this.f_107216_ *= 0.48;
            this.f_107217_ *= 0.48;
        } else if (this.physicsType == DustParticle.PhysicsType.CONTRACTIVE) {
            this.f_107215_ *= 1.35;
            this.f_107216_ *= 1.35;
            this.f_107217_ *= 1.35;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ContractiveDustProvider implements ParticleProvider<SimpleParticleType> {

        protected SpriteSet sprite;

        public ContractiveDustProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DustParticle dustParticle = new DustParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, DustParticle.PhysicsType.CONTRACTIVE);
            dustParticle.m_108335_(this.sprite);
            return dustParticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ContractiveMetaParticle extends NoRenderParticle {

        private final double radius;

        private final int density;

        public ContractiveMetaParticle(ClientLevel level, double x, double y, double z, double radius, int lifetime, int density) {
            super(level, x, y, z);
            this.radius = radius;
            this.f_107225_ = lifetime;
            this.density = density;
        }

        @Override
        public void tick() {
            super.m_5989_();
            for (int x = -1; x <= 1; x += 2) {
                for (int y = -1; y <= 1; y += 2) {
                    for (int z = -1; z <= 1; z += 2) {
                        for (int i = 0; i < this.density; i++) {
                            Vec3 rand = new Vec3(Math.random() * (double) x, Math.random() * (double) y, Math.random() * (double) z).normalize().scale(this.radius);
                            this.f_107208_.addParticle(EpicFightParticles.DUST_CONTRACTIVE.get(), this.f_107212_ + rand.x, this.f_107213_ + rand.y, this.f_107214_ + rand.z, -rand.x, -rand.y, -rand.z);
                        }
                    }
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        public static class Provider implements ParticleProvider<SimpleParticleType> {

            public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                return new DustParticle.ContractiveMetaParticle(worldIn, x, y, z, xSpeed, (int) Double.doubleToLongBits(ySpeed), (int) Double.doubleToLongBits(zSpeed));
            }
        }
    }

    @FunctionalInterface
    interface DeltaMovementFunction {

        Vec3 getDeltaMovement(double var1, double var3, double var5);
    }

    @OnlyIn(Dist.CLIENT)
    public static class ExpansiveDustProvider implements ParticleProvider<SimpleParticleType> {

        protected SpriteSet sprite;

        public ExpansiveDustProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DustParticle dustParticle = new DustParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, DustParticle.PhysicsType.EXPANSIVE);
            dustParticle.m_108335_(this.sprite);
            return dustParticle;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ExpansiveMetaParticle extends NoRenderParticle {

        public ExpansiveMetaParticle(ClientLevel level, double x, double y, double z, double radius, int density) {
            super(level, x, y, z);
            for (int vx = -1; vx <= 1; vx += 2) {
                for (int vz = -1; vz <= 1; vz += 2) {
                    for (int i = 0; i < density; i++) {
                        Vec3 rand = new Vec3(Math.random() * (double) vx, Math.random(), Math.random() * (double) vz).normalize().scale(radius);
                        level.addParticle(EpicFightParticles.DUST_EXPANSIVE.get(), x, y, z, rand.x, rand.y, rand.z);
                    }
                }
            }
        }

        @OnlyIn(Dist.CLIENT)
        public static class Provider implements ParticleProvider<SimpleParticleType> {

            public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
                return new DustParticle.ExpansiveMetaParticle(worldIn, x, y, z, xSpeed, (int) Double.doubleToLongBits(ySpeed));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class NormalDustProvider implements ParticleProvider<SimpleParticleType> {

        protected SpriteSet sprite;

        public NormalDustProvider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            DustParticle dustParticle = new DustParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, DustParticle.PhysicsType.NORMAL);
            dustParticle.m_108335_(this.sprite);
            return dustParticle;
        }
    }

    private static enum PhysicsType {

        EXPANSIVE((dx, dy, dz) -> new Vec3(dx, dy, dz)), CONTRACTIVE((dx, dy, dz) -> new Vec3(dx * 0.02, dy * 0.02, dz * 0.02)), NORMAL((dx, dy, dz) -> new Vec3(dx, dy, dz));

        DustParticle.DeltaMovementFunction function;

        private PhysicsType(DustParticle.DeltaMovementFunction function) {
            this.function = function;
        }
    }
}