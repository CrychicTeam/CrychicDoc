package com.mna.entities.sorcery.targeting;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.BlockUtils;
import com.mna.tools.render.LineSegment;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpellWall extends ChanneledSpellEntity {

    protected Vec3 forward;

    protected Vec3 perpendicular;

    public SpellWall(EntityType<? extends SpellWall> type, Level world) {
        super(type, world);
    }

    public SpellWall(LivingEntity caster, ISpellDefinition spell, Level world, boolean axisLock) {
        this(EntityInit.SPELL_WALL.get(), caster, spell, world, axisLock);
    }

    protected SpellWall(EntityType<? extends SpellWall> entityType, LivingEntity caster, ISpellDefinition spell, Level world, boolean axisLock) {
        super(entityType, caster, spell, world);
        if (!axisLock) {
            this.m_146922_(caster.m_146908_());
            this.m_146926_(caster.m_146909_());
        } else {
            this.m_146922_(caster.m_6350_().toYRot());
        }
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        float radius_h = this.getShapeAttributeByAge(Attribute.WIDTH);
        float radius_v = this.getShapeAttributeByAge(Attribute.HEIGHT);
        SpellSource source = new SpellSource(caster, caster.getUsedItemHand());
        SpellContext context = new SpellContext(world, recipe, this);
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities() && ((SpellEffect) c.getPart()).canBeChanneled())) {
            for (Entity e : this.targetEntities(radius_h, radius_v)) {
                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(e), context);
            }
        }
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks() && ((SpellEffect) c.getPart()).canBeChanneled())) {
            for (BlockPos p : this.targetBlocks(radius_h, radius_v)) {
                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(p, null).doNotOffsetFace(), context);
            }
        }
    }

    protected BlockPos[] targetBlocks(float radius_h, float radius_v) {
        double radiusOffsetX = Math.cos(Math.toRadians((double) this.m_146908_())) * (double) radius_h;
        double radiusOffsetZ = Math.sin(Math.toRadians((double) this.m_146908_())) * (double) radius_h;
        Vec3 a = new Vec3(this.m_20185_() + radiusOffsetX, this.m_20186_(), this.m_20189_() + radiusOffsetZ);
        Vec3 b = new Vec3(this.m_20185_() - radiusOffsetX, this.m_20186_(), this.m_20189_() - radiusOffsetZ);
        return this.getAllBlockLocationsBetween(a, b, radius_v);
    }

    protected BlockPos[] getAllBlockLocationsBetween(Vec3 a, Vec3 b, float radius_v) {
        List<BlockPos> allPoints = new ArrayList();
        BlockUtils.stepThroughBlocksLinear(BlockUtils.Vector3dToBlockPosRound(a), BlockUtils.Vector3dToBlockPosRound(b), pos -> {
            for (int i = 0; (float) i < radius_v; i++) {
                if (!allPoints.contains(pos.offset(0, i, 0))) {
                    allPoints.add(pos.offset(0, i, 0));
                }
            }
        });
        return (BlockPos[]) allPoints.toArray(new BlockPos[0]);
    }

    protected List<Entity> targetEntities(float radius_h, float radius_v) {
        List<Entity> possibleTargets = this.m_9236_().m_45976_(Entity.class, new AABB(this.m_20185_() - (double) radius_h, this.m_20186_(), this.m_20189_() - (double) radius_h, this.m_20185_() + (double) radius_h, this.m_20186_() + (double) radius_v, this.m_20189_() + (double) radius_h));
        List<Entity> targets = new ArrayList();
        double dirX = Math.cos((Math.PI / 180.0) * (double) this.m_146909_());
        double dirZ = Math.sin((Math.PI / 180.0) * (double) this.m_146908_());
        Vec3 a = new Vec3(this.m_20185_() - dirX * (double) radius_h, this.m_20186_(), this.m_20189_() - dirZ * (double) radius_h);
        Vec3 b = new Vec3(this.m_20185_() - dirX * (double) (-radius_h), this.m_20186_(), this.m_20189_() - dirZ * (double) (-radius_h));
        LineSegment wallLine = new LineSegment(a, b);
        for (Entity e : possibleTargets) {
            if (e != this && e != this.getCaster() && e instanceof LivingEntity && e.isAlive()) {
                Vec3 target = e.position();
                Vec3 closest = wallLine.closestPointOnLine(target);
                target = target.subtract(0.0, target.y, 0.0);
                closest = closest.subtract(0.0, closest.y, 0.0);
                double hDist = Math.abs(closest.distanceTo(target));
                double vDist = Math.abs(this.m_20186_() - e.getY());
                if (hDist < 0.75 && vDist < (double) radius_v) {
                    targets.add(e);
                }
            }
        }
        return targets;
    }

    @Override
    public void tick() {
        if (this.actAsChanneled()) {
            super.tick();
        } else if (!this.m_9236_().isClientSide() && this.f_19797_ >= this.getMaxAge()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            SpellRecipe recipe = this.getSpell();
            if (!this.m_9236_().isClientSide() && !recipe.isValid()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                LivingEntity caster = this.getCaster();
                if (caster == null) {
                    if (!this.m_9236_().isClientSide()) {
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                } else {
                    if (!this.m_9236_().isClientSide()) {
                        if (this.f_19797_ > 0 && (this.getApplicationRate() == 1 || this.f_19797_ % this.getApplicationRate() == 0)) {
                            this.applyEffect(ItemStack.EMPTY, recipe, caster, (ServerLevel) this.m_9236_());
                        }
                    } else {
                        this.spawnParticles();
                        this.playSounds();
                    }
                }
            }
        }
    }

    protected boolean actAsChanneled() {
        return false;
    }

    @Override
    public void setCaster(LivingEntity player) {
        this.m_146926_(player.m_146909_());
        this.m_146922_(player.m_146908_());
        super.setCaster(player);
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        this.spawnParticlesAlongRadius(recipe, ParticleInit.AIR_ORBIT.get(), new Vec3(0.3F, 0.02F, 0.3F), p -> p.setScale(0.2F).setColor(10, 10, 10));
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
        this.spawnParticlesAlongRadius(recipe, ParticleInit.DUST.get(), new Vec3(0.0, 0.05F, 0.0), null);
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        if (!lightning) {
            this.spawnParticlesAlongRadius(recipe, hellfire ? ParticleInit.HELLFIRE.get() : ParticleInit.FLAME.get(), new Vec3(0.0, (double) (this.getShapeAttributeByAge(Attribute.HEIGHT) / 30.0F), 0.0), null);
        } else {
            this.spawnLightningParticlesAlongRadius(recipe, new Vec3(-0.5 + Math.random(), Math.random(), -0.5 + Math.random()));
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean frost) {
        this.spawnParticlesAlongRadius(recipe, frost ? ParticleInit.FROST.get() : ParticleInit.WATER.get(), new Vec3(0.0, 0.025F, 0.0), null);
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        this.spawnParticlesAlongRadius(recipe, ParticleInit.ENDER_VELOCITY.get(), new Vec3(0.0, 0.025F, 0.0), null);
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        this.spawnParticlesAlongRadius(recipe, ParticleInit.ARCANE.get(), new Vec3(0.0, 0.025F, 0.0), null);
    }

    protected void spawnParticlesAlongRadius(SpellRecipe recipe, MAParticleType particleData, Vec3 motionData, Consumer<MAParticleType> particleAdjuster) {
        this.spawnParticlesAlongRadius(recipe, particleData, motionData, true, false, particleAdjuster);
    }

    protected void spawnParticlesAlongRadius(SpellRecipe recipe, MAParticleType particleData, Vec3 motionData, boolean randomY, boolean addPositionToMotionData, Consumer<MAParticleType> particleAdjuster) {
        if (this.forward == null) {
            float x = -Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
            float y = -Mth.sin(this.m_146909_() * (float) (Math.PI / 180.0));
            float z = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
            this.forward = new Vec3((double) x, (double) y, (double) z);
        }
        if (this.perpendicular == null) {
            this.perpendicular = this.forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
        }
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        float height = this.getShapeAttributeByAge(Attribute.HEIGHT);
        for (int i = 0; (float) i < 20.0F * radius * (height / 2.0F); i++) {
            double radiusMid = Math.random() * (double) radius * 2.0 - (double) radius;
            Vec3 particleOrigin = new Vec3(me.x - this.perpendicular.x * radiusMid, randomY ? me.y + Math.random() * (double) height : me.y, me.z - this.perpendicular.z * radiusMid);
            Vec3 particleMotion = addPositionToMotionData ? motionData.add(particleOrigin) : motionData;
            MAParticleType pfxAdj = new MAParticleType(particleData);
            if (particleAdjuster != null) {
                particleAdjuster.accept(pfxAdj);
            }
            recipe.colorParticle(pfxAdj, this.getCaster());
            this.m_9236_().addParticle(pfxAdj, particleOrigin.x, particleOrigin.y, particleOrigin.z, particleMotion.x, particleMotion.y, particleMotion.z);
        }
    }

    protected void spawnLightningParticlesAlongRadius(SpellRecipe recipe, Vec3 motionData) {
        if (this.forward == null) {
            float x = -Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
            float y = -Mth.sin(this.m_146909_() * (float) (Math.PI / 180.0));
            float z = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
            this.forward = new Vec3((double) x, (double) y, (double) z);
        }
        if (this.perpendicular == null) {
            this.perpendicular = this.forward.cross(new Vec3(0.0, 1.0, 0.0)).normalize();
        }
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        float height = this.getShapeAttributeByAge(Attribute.HEIGHT);
        for (int i = 0; (float) i < 2.0F * radius; i++) {
            double radiusMid = Math.random() * (double) radius * 2.0 - (double) radius;
            Vec3 particleOrigin = new Vec3(me.x - this.perpendicular.x * radiusMid, me.y, me.z - this.perpendicular.z * radiusMid);
            Vec3 particleMotion = motionData.add(particleOrigin);
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getCaster()), particleOrigin.x, particleOrigin.y, particleOrigin.z, particleMotion.x, particleMotion.y + (double) height, particleMotion.z);
        }
    }
}