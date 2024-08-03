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
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.EntityInit;
import com.mna.entities.sorcery.base.ChanneledSpellEntity;
import com.mna.particles.types.movers.ParticleVelocityMover;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SpellFocus extends ChanneledSpellEntity {

    public SpellFocus(EntityType<? extends SpellFocus> entityType, Level world) {
        super(entityType, world);
    }

    public SpellFocus(LivingEntity caster, ISpellDefinition spell, Level world, Vec3 position) {
        super(EntityInit.SPELL_FOCUS.get(), caster, spell, world);
        this.m_146884_(position);
    }

    @Override
    public void tick() {
        if (this.getCaster() != null) {
            this.getCaster().getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                Vec3 pos = this.getCaster().m_146892_().add(this.getCaster().m_20156_().normalize().scale((double) m.getFocusDistance()));
                ClipContext ctx = new ClipContext(this.getCaster().m_146892_(), pos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this);
                BlockHitResult bhr = this.m_9236_().m_45547_(ctx);
                if (bhr.getType() == HitResult.Type.BLOCK) {
                    pos = bhr.m_82450_();
                }
                this.m_6034_(pos.x, pos.y, pos.z);
            });
            super.tick();
        } else {
            if (!this.m_9236_().isClientSide()) {
                this.m_142687_(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected int getApplicationRate() {
        return 1;
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
            if (radius > 0) {
                for (int i = -radius; i <= radius; i++) {
                    for (int j = -radius; j <= radius; j++) {
                        for (int k = -radius; k <= radius; k++) {
                            BlockPos adjusted = this.m_20183_().offset(i, j, k);
                            results.putAll(SpellCaster.ApplyComponents(recipe, source, new SpellTarget(adjusted, face).doNotOffsetFace(), context));
                        }
                    }
                }
            }
        }
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
            float radiusx = recipe.getShape().getValue(Attribute.RADIUS);
            for (Entity target : this.m_9236_().getEntities(this, this.m_20191_().inflate((double) radiusx), e -> !e.isInvulnerable() && e.isAlive() && e instanceof LivingEntity)) {
                if (target != null && target != caster) {
                    results.putAll(SpellCaster.ApplyComponents(recipe, source, new SpellTarget(target), context));
                }
            }
        }
        if (results.size() > 0) {
            List<SpellEffect> appliedEffects = (List<SpellEffect>) results.entrySet().stream().map(e -> e.getValue() == ComponentApplicationResult.SUCCESS ? (SpellEffect) e.getKey() : null).filter(e -> e != null).collect(Collectors.toList());
            SpellCaster.spawnClientFX(this.m_9236_(), this.m_20182_(), Vec3.ZERO, source, appliedEffects);
        }
    }

    private void spawnParticle(MAParticleType particle) {
        float radius = Math.max(1.0F, this.getSpell().getShape().getValue(Attribute.RADIUS));
        Vec3 end = this.m_20182_();
        for (int i = 0; (float) i < 3.0F * radius + 1.0F; i++) {
            Vec3 start = this.m_20182_().add((double) (-radius) + (double) (2.0F * radius) * Math.random(), (double) (-radius) + (double) (2.0F * radius) * Math.random(), (double) (-radius) + (double) (2.0F * radius) * Math.random());
            this.m_9236_().addParticle(particle, start.x, start.y, start.z, end.x, end.y, end.z);
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_LERP.get()).setScale(0.05F).setColor(10, 10, 10), this.getCaster()));
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
        this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.DUST_LERP.get()), this.getCaster()));
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        if (lightning) {
            this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getCaster()));
        } else if (hellfire) {
            this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.HELLFIRE.get()).setMover(new ParticleVelocityMover(0.0, 0.1F, 0.0, true)), this.getCaster()));
        } else {
            this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.FLAME_LERP.get()), this.getCaster()));
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean ice) {
        this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.WATER_LERP.get()), this.getCaster()));
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER.get()), this.getCaster()));
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        this.spawnParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE_LERP.get()), this.getCaster()));
    }
}