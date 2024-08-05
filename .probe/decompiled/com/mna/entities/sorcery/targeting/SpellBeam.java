package com.mna.entities.sorcery.targeting;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.spells.targeting.SpellTargetHelper;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.ModifiedSpellPart;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.BlockUtils;
import com.mna.tools.render.LineSegment;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellBeam extends ChanneledSpellEntity {

    private static final Vec3 UP = new Vec3(0.0, 1.0, 0.0);

    private boolean lastTickMissed = true;

    private Vec3 lastTickImpact = null;

    private Vec3 lastTickTarget = null;

    private BlockPos lastTickBlockPos = null;

    private Direction lastTickBlockFace = Direction.UP;

    public SpellBeam(EntityType<? extends SpellBeam> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SpellBeam(LivingEntity caster, ISpellDefinition spell, Level world) {
        super(EntityInit.SPELL_BEAM.get(), caster, spell, world);
        this.m_6034_(caster.m_20185_(), caster.m_20186_(), caster.m_20189_());
        this.m_19915_(this.getCaster().m_146908_(), this.getCaster().m_146909_());
    }

    @Override
    public void tick() {
        LivingEntity caster = this.getCaster();
        SpellRecipe recipe = this.getSpell();
        if (caster != null && recipe != null && recipe.isValid()) {
            float range = this.getShapeAttributeByAge(Attribute.RANGE);
            this.f_19854_ = this.m_20185_();
            this.f_19855_ = this.m_20186_();
            this.f_19856_ = this.m_20189_();
            Vec3 newPos = caster.m_20182_().add(0.0, (double) (caster.m_20192_() - 0.1F), 0.0);
            HumanoidArm casterHandedness = caster.getMainArm();
            if ((casterHandedness != HumanoidArm.RIGHT || caster.getUsedItemHand() != InteractionHand.MAIN_HAND) && (casterHandedness != HumanoidArm.LEFT || caster.getUsedItemHand() != InteractionHand.OFF_HAND)) {
                newPos = newPos.add(caster.m_20154_().cross(UP).normalize().scale(-0.1F));
            } else {
                newPos = newPos.add(caster.m_20154_().cross(UP).normalize().scale(0.1F));
            }
            this.m_6034_(newPos.x, newPos.y, newPos.z);
            this.m_146926_(caster.m_146909_());
            this.f_19860_ = caster.f_19860_;
            this.m_146922_(caster.yHeadRot);
            this.f_19859_ = caster.yHeadRotO;
            HitResult result = SpellTargetHelper.rayTrace(this, this.m_9236_(), this.m_20182_(), caster.m_20154_(), true, false, ClipContext.Block.COLLIDER, entity -> entity.isPickable() && entity.isAlive() && entity != caster, caster.m_20191_().inflate((double) range, (double) range, (double) range), (double) range);
            this.lastTickImpact = result.getLocation();
            if (result.getType() == HitResult.Type.BLOCK) {
                this.lastTickBlockPos = ((BlockHitResult) result).getBlockPos();
                this.lastTickBlockFace = ((BlockHitResult) result).getDirection();
                this.lastTickTarget = this.lastTickImpact;
            } else if (result.getType() == HitResult.Type.ENTITY) {
                this.lastTickTarget = ((EntityHitResult) result).getEntity().position();
                this.lastTickBlockPos = BlockPos.containing(this.lastTickTarget);
            }
            if (result.getType() == HitResult.Type.MISS) {
                this.lastTickMissed = true;
            } else {
                this.lastTickMissed = false;
            }
        } else {
            this.lastTickMissed = true;
        }
        super.tick();
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        if (!this.lastTickMissed && caster != null) {
            ModifiedSpellPart<Shape> s = recipe.getShape();
            float radius_h = s.getValue(Attribute.WIDTH) / 2.0F;
            float radius_v = s.getValue(Attribute.HEIGHT) / 2.0F;
            float radius_d = s.getValue(Attribute.DEPTH) * Math.max((float) this.f_19797_ / 20.0F, 1.0F) / 2.0F;
            SpellSource source = new SpellSource(caster, caster.getUsedItemHand());
            List<Entity> entities = this.targetEntities(this.lastTickTarget, radius_h * 2.0F, radius_v * 2.0F, radius_d);
            BlockPos[] blocks = this.targetBlocks(this.lastTickBlockPos, radius_h, radius_v, radius_d);
            SpellContext context = new SpellContext(world, recipe, this);
            for (Entity e : entities) {
                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(e), context);
            }
            for (BlockPos b : blocks) {
                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(b, this.lastTickBlockFace), context);
            }
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        this.spawnParticles(recipe, ParticleInit.AIR_VELOCITY.get(), 10, 0.1F);
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
        this.spawnParticles(recipe, ParticleInit.DUST.get(), 5, 0.01F);
        float particle_spread = 0.2F;
        this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.EARTH.get()), this.getCaster()), this.lastTickImpact.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.lastTickImpact.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.lastTickImpact.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, 0.05, 0.31, 0.12);
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        if (!lightning) {
            this.spawnParticles(recipe, hellfire ? ParticleInit.HELLFIRE.get() : ParticleInit.FLAME.get(), 20, 0.01F);
        } else {
            this.spawnLightningParticles(recipe, ParticleInit.LIGHTNING_BOLT.get(), 1);
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean frost) {
        this.spawnParticles(recipe, frost ? ParticleInit.FROST.get() : ParticleInit.WATER.get(), 10, 0.01F);
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        this.spawnParticles(recipe, ParticleInit.ENDER_VELOCITY.get(), 10, 0.01F);
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        this.spawnParticles(recipe, ParticleInit.ARCANE.get(), 10, 0.01F);
    }

    private void spawnParticles(SpellRecipe recipe, MAParticleType type, int amount, float velocity) {
        if (!this.lastTickMissed) {
            float particle_spread = 0.2F;
            for (int i = 0; i < amount; i++) {
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(type), this.getCaster()), this.lastTickImpact.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.lastTickImpact.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.lastTickImpact.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, (double) (-velocity) + Math.random() * (double) velocity * 2.0, (double) (-velocity) + Math.random() * (double) velocity * 2.0, (double) (-velocity) + Math.random() * (double) velocity * 2.0);
            }
        }
    }

    private void spawnLightningParticles(SpellRecipe recipe, MAParticleType type, int amount) {
        if (!this.lastTickMissed) {
            float particle_spread = 0.5F;
            for (int i = 0; i < amount; i++) {
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(type), this.getCaster()), this.lastTickImpact.x, this.lastTickImpact.y, this.lastTickImpact.z, this.lastTickImpact.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.lastTickImpact.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, this.lastTickImpact.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0);
            }
        }
    }

    protected BlockPos[] targetBlocks(BlockPos impactPoint, float radius_h, float radius_v, float radius_d) {
        BlockPos projectedDepthPoint = BlockUtils.Vector3dToBlockPosRound(this.m_20154_().normalize().scale((double) radius_d).add((double) impactPoint.m_123341_(), (double) impactPoint.m_123342_(), (double) impactPoint.m_123343_()));
        return this.getAllBlockLocationsDepth(impactPoint, projectedDepthPoint, radius_h, radius_v);
    }

    private BlockPos[] getAllBlockLocationsDepth(BlockPos startPoint, BlockPos endPoint, float radius_h, float radius_v) {
        double radiusOffsetX = Math.cos(Math.toRadians((double) this.m_146908_())) * (double) radius_h;
        double radiusOffsetZ = Math.sin(Math.toRadians((double) this.m_146908_())) * (double) radius_h;
        ArrayList<BlockPos> vecList = new ArrayList();
        BlockUtils.stepThroughBlocksLinear(startPoint, endPoint, pos -> {
            Vec3 a = new Vec3((double) pos.m_123341_() + radiusOffsetX, (double) pos.m_123342_(), (double) pos.m_123343_() + radiusOffsetZ);
            Vec3 b = new Vec3((double) pos.m_123341_() - radiusOffsetX, (double) pos.m_123342_(), (double) pos.m_123343_() - radiusOffsetZ);
            BlockPos[] positions = this.getAllBlockLocationsHorizontal(BlockUtils.Vector3dToBlockPosRound(a), BlockUtils.Vector3dToBlockPosRound(b));
            for (BlockPos position : positions) {
                for (int y = -((int) Math.floor((double) radius_v)); (double) y <= Math.floor((double) radius_v); y++) {
                    BlockPos vOffset = position.offset(0, y, 0);
                    if (!vecList.contains(vOffset)) {
                        vecList.add(vOffset);
                    }
                }
            }
        });
        return (BlockPos[]) vecList.toArray(new BlockPos[0]);
    }

    private BlockPos[] getAllBlockLocationsHorizontal(BlockPos p1, BlockPos p2) {
        List<BlockPos> allPoints = new ArrayList();
        BlockUtils.stepThroughBlocksLinear(p1, p2, pos -> {
            if (!allPoints.contains(pos)) {
                allPoints.add(pos);
            }
        });
        return (BlockPos[]) allPoints.toArray(new BlockPos[0]);
    }

    public int[] getBeamColor() {
        SpellRecipe recipe = this.getSpell();
        if (recipe == null) {
            return new int[] { 255, 255, 255 };
        } else {
            int override = this.getOverrideColor();
            return override == -1 ? recipe.getHighestAffinity().getColor() : new int[] { FastColor.ARGB32.red(override), FastColor.ARGB32.green(override), FastColor.ARGB32.blue(override) };
        }
    }

    protected List<Entity> targetEntities(Vec3 impactPoint, float width, float height, float depth) {
        Vec3 forward = this.m_20154_().normalize();
        Vec3 center = impactPoint.add(forward.scale((double) (depth / 2.0F)));
        AABB bb = new AABB(BlockPos.containing(center)).inflate((double) Math.max(width, Math.max(height, depth)));
        List<Entity> possibleTargets = this.m_9236_().m_45933_(this.getCaster(), bb);
        List<Entity> targets = new ArrayList();
        float r_width = width / 2.0F;
        float r_height = height / 2.0F;
        float r_depth = depth / 2.0F;
        Vec3 depth_b = impactPoint.add(forward.scale((double) depth));
        Vec3 horizontal_a = center.add(forward.cross(UP).scale((double) r_width));
        Vec3 horizontal_b = center.add(forward.cross(UP).scale((double) (-r_width)));
        LineSegment line_horizontal = new LineSegment(horizontal_a, horizontal_b);
        LineSegment line_depth = new LineSegment(impactPoint, depth_b);
        for (Entity e : possibleTargets) {
            if (e != this && e != this.getCaster()) {
                Vec3 target = e.position();
                Vec3 closest_horizontal = line_horizontal.closestPointOnLine(target);
                Vec3 closest_depth = line_depth.closestPointOnLine(target);
                target = target.subtract(0.0, target.y, 0.0);
                closest_horizontal = closest_horizontal.subtract(0.0, closest_horizontal.y, 0.0);
                closest_depth = closest_depth.subtract(0.0, closest_depth.y, 0.0);
                double depthDist = Math.abs(closest_horizontal.distanceTo(target));
                double heightDist = Math.abs(center.y - e.getY());
                double widthDist = closest_depth.distanceTo(target);
                if (widthDist >= (double) (-r_width) && widthDist <= (double) r_width && heightDist >= (double) (-r_height) && heightDist <= (double) r_height && depthDist >= (double) (-r_depth) && depthDist <= (double) r_depth) {
                    targets.add(e);
                }
            }
        }
        return targets;
    }

    public Vec3 getLastTickImpact() {
        return this.lastTickImpact;
    }
}