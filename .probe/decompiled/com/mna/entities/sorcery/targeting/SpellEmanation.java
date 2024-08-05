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
import com.mna.particles.types.movers.ParticleLerpMover;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpellEmanation extends ChanneledSpellEntity {

    public SpellEmanation(EntityType<? extends SpellEmanation> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SpellEmanation(LivingEntity caster, ISpellDefinition spell, Level world) {
        super(EntityInit.SPELL_EMANATION.get(), caster, spell, world);
        this.m_6034_(caster.m_20185_(), caster.m_20186_(), caster.m_20189_());
    }

    @Override
    public void tick() {
        if (this.getCaster() != null) {
            this.m_6034_(this.getCaster().m_20185_(), this.getCaster().m_20186_() + 0.1F, this.getCaster().m_20189_());
        }
        super.tick();
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        if (caster != null) {
            float radius_h = this.getShapeAttributeByAge(Attribute.WIDTH);
            float radius_v = this.getShapeAttributeByAge(Attribute.HEIGHT);
            SpellSource source = new SpellSource(caster, caster.getUsedItemHand());
            SpellContext context = new SpellContext(world, recipe, this);
            if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
                AABB bb = new AABB(this.m_20185_() - (double) radius_h, this.m_20186_(), this.m_20189_() - (double) radius_h, this.m_20185_() + (double) radius_h, this.m_20186_() + (double) radius_v, this.m_20189_() + (double) radius_h);
                for (Entity e : world.m_45933_(caster, bb)) {
                    if (this.losCheck(e)) {
                        SpellCaster.ApplyComponents(recipe, source, new SpellTarget(e), context);
                    }
                }
            }
            boolean below = this.getCaster() != null && this.getCaster().m_6047_();
            if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
                BlockPos origin = this.m_20183_();
                for (int i = -((int) radius_h); (float) i <= radius_h; i++) {
                    for (int j = 0; (float) j < radius_v; j++) {
                        for (int k = -((int) radius_h); (float) k <= radius_h; k++) {
                            BlockPos adjusted = origin.offset(i, j - (below ? 1 : 0), k);
                            if (below && (double) adjusted.m_123342_() < this.m_20186_() || this.losCheck(adjusted)) {
                                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(adjusted, Direction.UP).doNotOffsetFace(), context);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        float particle_spread = 0.0F;
        for (int i = 0; i < 20; i++) {
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), this.getCaster()), me.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, me.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, me.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, 0.2F, 0.15F, (double) radius);
        }
    }

    @Override
    protected void spawnEarthParticles(SpellRecipe recipe) {
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        for (int i = 0; i < 50; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            Vec3 offset = new Vec3(Math.cos(angle) * (double) radius, 0.0, Math.sin(angle) * (double) radius);
            Vec3 pos = me.add(offset);
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), this.getCaster()), pos.x, pos.y, pos.z, 0.0, 0.1, 0.0);
        }
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        if (!lightning) {
            for (int i = 0; i < 50; i++) {
                double angle = Math.random() * 2.0 * Math.PI;
                Vec3 offset = new Vec3(Math.cos(angle) * (double) radius, 0.0, Math.sin(angle) * (double) radius);
                Vec3 pos = me.add(offset);
                this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(hellfire ? ParticleInit.HELLFIRE.get() : ParticleInit.FLAME.get()), this.getCaster()), pos.x, pos.y, pos.z, 0.0, 0.1, 0.0);
            }
        } else {
            Vec3 offset = new Vec3((double) (-radius) + Math.random() * (double) radius * 2.0, (double) (-radius) + Math.random() * (double) radius * 2.0, (double) (-radius) + Math.random() * (double) radius * 2.0);
            Vec3 pos = me.add(offset);
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.LIGHTNING_BOLT.get()), this.getCaster()), me.x, me.y, me.z, pos.x, pos.y, pos.z);
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean frost) {
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        for (int i = 0; i < 50; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            Vec3 offset = new Vec3(Math.cos(angle) * (double) radius, 0.0, Math.sin(angle) * (double) radius);
            Vec3 pos = me.add(offset);
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(frost ? ParticleInit.FROST.get() : ParticleInit.WATER.get()), this.getCaster()), pos.x, pos.y, pos.z, 0.0, 0.1, 0.0);
        }
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        for (int i = 0; i < 50; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            Vec3 offset = new Vec3(Math.cos(angle) * (double) radius, 0.1F, Math.sin(angle) * (double) radius);
            Vec3 pos = me.add(offset);
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ENDER.get()), this.getCaster()), me.x, pos.y, me.z, pos.x, pos.y, pos.z);
        }
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        Vec3 me = this.m_20182_();
        float radius = this.getShapeAttributeByAge(Attribute.WIDTH);
        for (int i = 0; i < 50; i++) {
            double angle = Math.random() * 2.0 * Math.PI;
            Vec3 offset = new Vec3(Math.cos(angle) * (double) radius, 0.1F + Math.abs(Math.sin((double) ((float) this.f_19797_ / 0.02F))) * 0.75, -Math.sin(angle) * (double) radius);
            Vec3 pos = me.add(offset);
            this.m_9236_().addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.ARCANE.get()), this.getCaster()).setMaxAge(10 + (int) radius * 2).setMover(new ParticleLerpMover(me.x, pos.y, me.z, pos.x, pos.y, pos.z)), me.x, pos.y, me.z, 0.0, 0.0, 0.0);
        }
    }
}