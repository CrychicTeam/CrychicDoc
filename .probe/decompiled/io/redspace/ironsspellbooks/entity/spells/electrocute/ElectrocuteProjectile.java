package io.redspace.ironsspellbooks.entity.spells.electrocute;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.AbstractConeProjectile;
import io.redspace.ironsspellbooks.registries.EntityRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ElectrocuteProjectile extends AbstractConeProjectile {

    private List<Vec3> beamVectors;

    public ElectrocuteProjectile(EntityType<? extends AbstractConeProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public ElectrocuteProjectile(Level level, LivingEntity entity) {
        super(EntityRegistry.ELECTROCUTE_PROJECTILE.get(), level, entity);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return super.m_6783_(pDistance);
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return super.m_6000_(pX, pY, pZ);
    }

    public void generateLightningBeams() {
        Random random = new Random();
        this.beamVectors = new ArrayList();
        Vec3 coreStart = new Vec3(0.0, 0.0, 0.0);
        int coreLength = random.nextInt(3) + 7;
        for (int core = 0; core < coreLength; core++) {
            Vec3 coreEnd = coreStart.add(0.0, 0.0, 1.0).add(randomVector(0.3F).multiply(2.5, 1.0, 2.5));
            this.beamVectors.add(coreStart);
            this.beamVectors.add(coreEnd);
            coreStart = coreEnd;
            int branchSegments = random.nextInt(3) + 1;
            this.beamVectors.addAll(generateBranch(coreEnd, branchSegments, 0.5F, 1));
        }
    }

    public static List<Vec3> generateBranch(Vec3 origin, int maxLength, float splitChance, int recursionCount) {
        List<Vec3> branchSegements = new ArrayList();
        Random random = new Random();
        int branches = random.nextInt(maxLength + 1);
        Vec3 branchStart = origin;
        int dir = random.nextBoolean() ? 1 : -1;
        float branchLength = 0.75F / (float) (recursionCount + 1);
        for (int i = 0; i < branches; i++) {
            Vec3 branchEnd = branchStart.add((double) ((float) dir * branchLength), 0.0, (double) branchLength).add(randomVector(0.3F));
            branchSegements.add(branchStart);
            branchSegements.add(branchEnd);
            if (random.nextFloat() <= splitChance) {
                branchSegements.addAll(generateBranch(branchEnd, maxLength - 1, splitChance * 1.2F, recursionCount + 1));
            }
            branchStart = branchEnd;
        }
        return branchSegements;
    }

    public int getAge() {
        return this.age;
    }

    public static Vec3 randomVector(float radius) {
        double x = Math.random() * 2.0 * (double) radius - (double) radius;
        double y = Math.random() * 2.0 * (double) radius - (double) radius;
        double z = Math.random() * 2.0 * (double) radius - (double) radius;
        return new Vec3(x, y, z);
    }

    public List<Vec3> getBeamCache() {
        if (this.beamVectors == null) {
            this.generateLightningBeams();
        }
        return this.beamVectors;
    }

    @Override
    public void spawnParticles() {
    }

    @Override
    protected void onHitEntity(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        DamageSources.applyDamage(entity, this.damage, SpellRegistry.ELECTROCUTE_SPELL.get().getDamageSource(this, this.m_19749_()));
        MagicManager.spawnParticles(this.m_9236_(), ParticleHelper.ELECTRICITY, entity.getX(), entity.getY() + (double) (entity.getBbHeight() / 2.0F), entity.getZ(), 10, (double) (entity.getBbWidth() / 3.0F), (double) (entity.getBbHeight() / 3.0F), (double) (entity.getBbWidth() / 3.0F), 0.1, false);
    }
}