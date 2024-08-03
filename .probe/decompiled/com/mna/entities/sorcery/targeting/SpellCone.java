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
import com.mna.tools.EntityUtil;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class SpellCone extends ChanneledSpellEntity {

    public SpellCone(EntityType<? extends SpellCone> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SpellCone(LivingEntity caster, ISpellDefinition spell, Level world) {
        super(EntityInit.SPELL_CONE.get(), caster, spell, world);
        this.m_6034_(caster.m_20185_(), caster.m_20186_(), caster.m_20189_());
    }

    @Override
    public void tick() {
        if (this.getCaster() != null) {
            this.m_6034_(this.getCaster().m_20185_(), this.getCaster().m_146892_().y, this.getCaster().m_20189_());
        }
        super.tick();
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        if (caster != null) {
            float depth = this.getShapeAttributeByAge(Attribute.DEPTH);
            float width = this.getShapeAttributeByAge(Attribute.WIDTH);
            float r = width;
            float angle = (float) Math.abs(Math.sin((double) (depth / width)) * 180.0 / Math.PI);
            SpellSource source = new SpellSource(caster, caster.getUsedItemHand());
            SpellContext context = new SpellContext(world, recipe, this);
            if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
                EntityUtil.getEntitiesWithinCone(this.m_9236_(), this.m_20182_(), this.getCaster().m_20156_(), depth, -angle, angle, e -> e.isAlive() && e != this.getCaster()).forEach(e -> {
                    if (this.losCheck(e)) {
                        SpellCaster.ApplyComponents(recipe, source, new SpellTarget(e), context);
                    }
                });
            }
            if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
                int min = 0;
                int max = (int) depth;
                Vec3 pos = this.m_20182_().subtract(0.0, 1.0, 0.0);
                Vec3 step = this.getCaster().m_20156_().normalize();
                HashSet<BlockPos> allBlocks = new HashSet();
                for (int i = min; i < max; i++) {
                    pos = pos.add(step);
                    float radiusAtStep = r * ((float) i / (float) max);
                    BlockPos[] positions = this.targetBlocks(pos, radiusAtStep, radiusAtStep);
                    allBlocks.addAll(Arrays.asList(positions));
                }
                for (BlockPos bp : allBlocks) {
                    SpellCaster.ApplyComponents(recipe, source, new SpellTarget(bp, Direction.UP).doNotOffsetFace(), context);
                }
            }
        }
    }

    protected BlockPos[] targetBlocks(Vec3 pos, float radius_h, float radius_v) {
        double radiusOffsetX = Math.cos(Math.toRadians((double) this.m_146908_())) * (double) radius_h;
        double radiusOffsetZ = Math.sin(Math.toRadians((double) this.m_146908_())) * (double) radius_h;
        Vec3 a = new Vec3(pos.x() + radiusOffsetX, pos.y(), pos.z() + radiusOffsetZ);
        Vec3 b = new Vec3(pos.x() - radiusOffsetX, pos.y(), pos.z() - radiusOffsetZ);
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

    private void spawnConeParticles(int count, MAParticleType particle) {
        Vec3 forward = Vec3.directionFromRotation(this.getCaster().m_146909_(), this.getCaster().getYHeadRot()).scale(0.25);
        Vec3 origin = this.m_20182_().add(forward.x, forward.y, forward.z);
        float depth = this.getShapeAttributeByAge(Attribute.DEPTH);
        for (int i = 0; i < count; i++) {
            Vector3f direction = new Vector3f((float) forward.x, (float) forward.y, (float) forward.z);
            direction.rotate(Axis.YP.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
            direction.rotate(Axis.XN.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
            direction.rotate(Axis.ZN.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
            direction.normalize();
            float speed = (float) (0.5 + Math.random());
            direction.mul(speed);
            this.m_9236_().addParticle(particle.setMaxAge((int) Math.ceil((double) (depth / speed))), origin.x + (double) direction.x(), origin.y + (double) direction.y(), origin.z + (double) direction.z(), (double) direction.x(), (double) direction.y(), (double) direction.z());
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        int count = 25;
        this.spawnConeParticles(count, recipe.colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), this.getCaster()));
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
        int count = 25;
        this.spawnConeParticles(count, recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), this.getCaster()));
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        int count = 25;
        if (!lightning) {
            this.spawnConeParticles(count, recipe.colorParticle(new MAParticleType(hellfire ? ParticleInit.HELLFIRE.get() : ParticleInit.FLAME.get()), this.getCaster()));
        } else {
            Vec3 forward = Vec3.directionFromRotation(this.getCaster().m_146909_(), this.getCaster().getYHeadRot()).scale(0.25);
            Vec3 origin = this.m_20182_().add(forward.x, forward.y, forward.z);
            float depth = this.getShapeAttributeByAge(Attribute.DEPTH);
            for (int i = 0; i < 5; i++) {
                Vector3f direction = new Vector3f((float) forward.x, (float) forward.y, (float) forward.z);
                direction.rotate(Axis.YP.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
                direction.rotate(Axis.XN.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
                direction.rotate(Axis.ZN.rotationDegrees((float) (-15 + (int) (Math.random() * 30.0))));
                direction.normalize();
                direction.mul(depth);
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getCaster()).setMaxAge(1 + (int) depth * 2), origin.x, origin.y, origin.z, origin.x + (double) direction.x(), origin.y + (double) direction.y(), origin.z + (double) direction.z());
            }
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean frost) {
        int count = 25;
        this.spawnConeParticles(count, recipe.colorParticle(new MAParticleType(frost ? ParticleInit.FROST.get() : ParticleInit.WATER.get()), this.getCaster()));
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        int count = 25;
        this.spawnConeParticles(count, recipe.colorParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()), this.getCaster()));
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        int count = 25;
        this.spawnConeParticles(count, recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE.get()), this.getCaster()));
    }
}