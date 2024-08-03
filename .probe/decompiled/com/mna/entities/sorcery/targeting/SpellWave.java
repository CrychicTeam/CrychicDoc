package com.mna.entities.sorcery.targeting;

import com.mna.api.particles.ParticleInit;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.EntityInit;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpellWave extends SpellWall {

    public SpellWave(EntityType<? extends SpellWave> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public SpellWave(LivingEntity caster, ISpellDefinition spell, Level world, boolean axisLock) {
        super(EntityInit.SPELL_WAVE.get(), caster, spell, world, axisLock);
        this.m_6034_(caster.m_20185_(), caster.m_20186_(), caster.m_20189_());
        if (axisLock) {
            Vec3 motion = Vec3.directionFromRotation(0.0F, caster.m_6350_().toYRot()).normalize().scale((double) this.getShapeAttribute(Attribute.SPEED));
            this.m_20256_(motion);
            if (caster.m_6047_()) {
                this.m_146884_(this.m_20182_().add(motion.normalize()).add(0.0, -0.5, 0.0));
            } else {
                this.m_146884_(this.m_20182_().add(motion.normalize()));
            }
        } else {
            float x = -Mth.sin(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
            float y = -Mth.sin(this.m_146909_() * (float) (Math.PI / 180.0));
            float z = Mth.cos(this.m_146908_() * (float) (Math.PI / 180.0)) * Mth.cos(this.m_146909_() * (float) (Math.PI / 180.0));
            Vec3 motion = new Vec3((double) x, (double) y, (double) z).normalize().scale((double) this.getShapeAttribute(Attribute.SPEED));
            this.m_20256_(motion);
        }
    }

    @Override
    public void tick() {
        this.m_6034_(this.m_20185_() + this.m_20184_().x, this.m_20186_() + this.m_20184_().y, this.m_20189_() + this.m_20184_().z);
        super.tick();
    }

    @Override
    protected boolean actAsChanneled() {
        return true;
    }

    @Override
    protected void applyEffect(ItemStack stack, SpellRecipe recipe, LivingEntity caster, ServerLevel world) {
        float radius_h = this.getShapeAttributeByAge(Attribute.WIDTH);
        float radius_v = this.getShapeAttributeByAge(Attribute.HEIGHT);
        SpellSource source = new SpellSource(caster, caster.getUsedItemHand());
        SpellContext context = new SpellContext(world, recipe, this);
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsEntities())) {
            for (Entity e : this.targetEntities(radius_h, radius_v)) {
                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(e), context);
            }
        }
        if (recipe.getComponents().stream().anyMatch(c -> ((SpellEffect) c.getPart()).targetsBlocks())) {
            for (BlockPos p : this.targetBlocks(radius_h, radius_v)) {
                SpellCaster.ApplyComponents(recipe, source, new SpellTarget(p, null), context);
            }
        }
    }

    @Override
    protected void spawnAirParticles(SpellRecipe recipe) {
        Vec3 jitter = new Vec3(-0.1F + Math.random() * 0.2F, 0.0, -0.1F + Math.random() * 0.2F);
        this.spawnParticlesAlongRadius(recipe, ParticleInit.AIR_VELOCITY.get(), this.m_20184_().scale(0.75).add(jitter), p -> p.setScale(0.2F).setColor(10, 10, 10));
    }

    @Override
    protected void spawnArcaneParticles(SpellRecipe recipe) {
        this.spawnParticlesAlongRadius(recipe, ParticleInit.ARCANE.get(), this.m_20184_().scale(0.75), null);
    }

    @Override
    protected void spawnEnderParticles(SpellRecipe recipe) {
        this.spawnParticlesAlongRadius(recipe, ParticleInit.ENDER_VELOCITY.get(), this.m_20184_().scale(0.75), null);
    }

    @Override
    protected void spawnFireParticles(SpellRecipe recipe, boolean hellfire, boolean lightning) {
        if (!lightning) {
            this.spawnParticlesAlongRadius(recipe, hellfire ? ParticleInit.HELLFIRE.get() : ParticleInit.FLAME.get(), this.m_20184_(), false, false, null);
        } else {
            this.spawnLightningParticlesAlongRadius(recipe, this.m_20184_().add(new Vec3(-0.5 + Math.random(), Math.random(), -0.5 + Math.random())));
        }
    }

    @Override
    protected void spawnWaterParticles(SpellRecipe recipe, boolean frost) {
        this.spawnParticlesAlongRadius(recipe, frost ? ParticleInit.FROST.get() : ParticleInit.WATER.get(), this.m_20184_().scale(0.75), null);
    }

    @Override
    protected int getApplicationRate() {
        return 1;
    }
}