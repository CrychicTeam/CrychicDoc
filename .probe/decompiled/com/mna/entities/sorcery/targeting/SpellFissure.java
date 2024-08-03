package com.mna.entities.sorcery.targeting;

import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mna.particles.types.movers.ParticleOrbitMover;
import com.mna.particles.types.movers.ParticleVelocityMover;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellFissure extends ChanneledSpellEntity {

    public SpellFissure(EntityType<? extends SpellFissure> type, Level world) {
        super(type, world);
    }

    public SpellFissure(LivingEntity caster, ISpellDefinition spell, Level world) {
        super(EntityInit.SPELL_FISSURE.get(), caster, spell, world);
        this.m_6034_(caster.m_20185_(), caster.m_20186_(), caster.m_20189_());
    }

    public float getStepHeight() {
        return 2.0F;
    }

    @Override
    public void tick() {
        SpellRecipe recipe = this.getSpell();
        if (!this.m_9236_().isClientSide() && !recipe.isValid()) {
            this.m_142687_(Entity.RemovalReason.DISCARDED);
        } else {
            LivingEntity caster = this.getCaster();
            if (caster != null && caster.isAlive() && caster.m_9236_().dimension().equals(this.m_9236_().dimension())) {
                if (!this.m_9236_().isClientSide() && this.f_19797_ >= this.getMaxAge()) {
                    if (!this.m_9236_().isClientSide()) {
                        this.applyEffect(caster.getUseItem(), recipe, caster, (ServerLevel) this.m_9236_());
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                } else if (caster.getUseItemRemainingTicks() > 0 && !caster.getUseItem().isEmpty()) {
                    if (this.snapToGround()) {
                        this.updateRotation(caster);
                        this.m_6853_(true);
                        this.m_6478_(MoverType.SELF, this.m_20184_());
                        if (this.m_9236_().isClientSide()) {
                            this.spawnParticles();
                            this.playSounds();
                        }
                    }
                } else {
                    if (!this.m_9236_().isClientSide()) {
                        this.applyEffect(caster.getUseItem(), recipe, caster, (ServerLevel) this.m_9236_());
                        this.m_142687_(Entity.RemovalReason.DISCARDED);
                    }
                }
            } else {
                if (!this.m_9236_().isClientSide()) {
                    this.m_142687_(Entity.RemovalReason.DISCARDED);
                }
            }
        }
    }

    private boolean snapToGround() {
        BlockPos curPos = this.m_20183_();
        int count = 0;
        if (!this.m_9236_().m_46859_(curPos)) {
            return true;
        } else {
            do {
                count++;
                curPos = curPos.below();
                if (!this.m_9236_().m_46859_(curPos)) {
                    this.m_6034_(this.m_20185_(), (double) (curPos.m_123342_() + 1), this.m_20189_());
                    return true;
                }
            } while (count < 3);
            this.m_142687_(Entity.RemovalReason.DISCARDED);
            return false;
        }
    }

    private void updateRotation(LivingEntity caster) {
        this.f_19854_ = this.m_20185_();
        this.f_19855_ = this.m_20186_();
        this.f_19856_ = this.m_20189_();
        Vec3 source = caster.m_146892_();
        Vec3 target = caster.m_146892_().add(caster.m_20156_().normalize().scale(64.0));
        ClipContext clipContext = new ClipContext(source, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, caster);
        BlockHitResult bhr = this.m_9236_().m_45547_(clipContext);
        if (bhr.getType() == HitResult.Type.BLOCK) {
            this.m_7618_(EntityAnchorArgument.Anchor.FEET, bhr.m_82450_());
        } else {
            this.m_19884_((double) caster.m_146909_(), (double) caster.m_146908_());
        }
        this.m_20256_(this.m_20156_().normalize().scale((double) this.getShapeAttribute(Attribute.SPEED)));
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        float spellRadius = recipe.getShape().getValue(Attribute.RADIUS);
        SpellContext context = new SpellContext(this.m_9236_(), recipe, this);
        SpellSource source = new SpellSource(caster, InteractionHand.MAIN_HAND);
        Direction face = Direction.UP;
        HashMap<SpellEffect, ComponentApplicationResult> results = new HashMap();
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
            int radius = (int) Math.floor((double) spellRadius);
            if (radius < 0) {
                radius = 0;
            }
            for (int i = -radius; i <= radius; i++) {
                for (int j = -radius; j <= radius; j++) {
                    for (int k = -radius; k <= radius; k++) {
                        BlockPos adjusted = this.m_20183_().below().offset(i, j, k);
                        HashMap<SpellEffect, ComponentApplicationResult> loopRes = SpellCaster.ApplyComponents(recipe, source, new SpellTarget(adjusted, face).doNotOffsetFace(), context);
                        SpellCaster.mergeComponentResults(results, loopRes);
                    }
                }
            }
        }
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
            float radius = recipe.getShape().getValue(Attribute.RADIUS);
            for (Entity target : this.m_9236_().getEntities(this, this.m_20191_().inflate((double) radius), e -> !e.isInvulnerable())) {
                if (target != null && target != caster) {
                    HashMap<SpellEffect, ComponentApplicationResult> loopRes = SpellCaster.ApplyComponents(recipe, source, new SpellTarget(target), context);
                    SpellCaster.mergeComponentResults(results, loopRes);
                }
            }
        }
        if (results.size() > 0) {
            List<SpellEffect> appliedEffects = (List<SpellEffect>) results.entrySet().stream().map(e -> ((ComponentApplicationResult) e.getValue()).is_success ? (SpellEffect) e.getKey() : null).filter(e -> e != null).collect(Collectors.toList());
            SpellCaster.spawnClientFX(this.m_9236_(), this.m_20182_(), new Vec3(0.0, 1.0, 0.0), source, appliedEffects);
        }
    }

    @Override
    protected void spawnParticles() {
        super.spawnParticles();
        BlockPos belowPos = this.m_20183_().below();
        if (!this.m_9236_().m_46859_(belowPos)) {
            Vec3 pos = this.m_20182_();
            FluidState flState = this.m_9236_().getFluidState(belowPos);
            if (!flState.isEmpty()) {
                ParticleOptions particle = flState.getDripParticle();
                if (particle != null) {
                    for (int i = 0; i < 2; i++) {
                        this.m_9236_().addParticle(particle, pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random(), 0.0, 0.0, 0.0);
                    }
                }
            } else {
                BlockState below = this.m_9236_().getBlockState(belowPos);
                for (int i = 0; i < 2; i++) {
                    this.m_9236_().addParticle(new MAParticleType(ParticleInit.ITEM.get()).setStack(new ItemStack(below.m_60734_())).setScale(0.15F).setMover(new ParticleVelocityMover(0.0, 0.2F, 0.0, true)).setGravity(0.05F), pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        Vec3 pos = this.m_20182_();
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setColor(10, 10, 10).setScale(0.1F), this.getCaster()), pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random(), 0.1F, 0.1F, 1.25);
        }
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
        Vec3 pos = this.m_20182_();
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), this.getCaster()).setScale(0.1F).setGravity(0.05F), pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random(), 0.0, 0.1F, 0.0);
        }
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        Vec3 pos = this.m_20182_();
        for (int i = 0; i < 5; i++) {
            if (hellfire) {
                Vec3 pStart = new Vec3(pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random());
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get().setScale(0.1F).setMover(new ParticleOrbitMover(pStart.x, pStart.y, pStart.z, 0.1F, 0.1F, 1.25))), this.getCaster()), pStart.x, pStart.y, pStart.z, 0.0, 0.0, 0.0);
            } else if (lightning) {
                Vec3 pStart = new Vec3(pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random());
                Vec3 pEnd = pStart.add(0.0, Math.random() + 0.5, 0.0);
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getCaster()), pStart.x, pStart.y, pStart.z, pEnd.x, pEnd.y, pEnd.z);
            } else {
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FLAME_ORBIT.get().setScale(0.1F)), this.getCaster()), pos.x - 1.0 + 2.0 * Math.random(), pos.y, pos.z - 1.0 + 2.0 * Math.random(), 0.1F, 0.1F, 1.25);
            }
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean ice) {
        Vec3 pos = this.m_20182_();
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.WATER.get()), this.getCaster()), pos.x - 0.5 + Math.random(), pos.y + Math.random() * 0.1, pos.z - 0.5 + Math.random(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        Vec3 pos = this.m_20182_();
        for (int i = 0; i < 5; i++) {
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER_VELOCITY.get()), this.getCaster()), pos.x - 0.5 + Math.random(), pos.y + Math.random() * 0.1, pos.z - 0.5 + Math.random(), 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        Vec3 pos = this.m_20182_();
        for (int i = 0; i < 2; i++) {
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE_MAGELIGHT.get()), this.getCaster()), pos.x - 0.5 + Math.random(), pos.y + Math.random() * 0.1, pos.z - 0.5 + Math.random(), 0.0, 0.0, 0.0);
        }
    }
}