package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.DamageHelper;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IDamageComponent;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.config.GeneralConfig;
import com.mna.effects.EffectInit;
import com.mna.network.ServerMessageDispatcher;
import com.mna.tools.math.MathUtils;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ComponentLightningDamage extends SpellEffect implements IDamageComponent {

    public ComponentLightningDamage(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 20.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 3.0F, 0.0F, 10.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 10.0F, 1.0F, 3.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        float damage = modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier();
        float branchChance = MathUtils.clamp01(modificationData.getValue(Attribute.LESSER_MAGNITUDE) / 10.0F);
        float branchRadius = modificationData.getValue(Attribute.RADIUS);
        Vec3 lastTargetPosition = target.getPosition();
        int hits = 0;
        if (target.isEntity()) {
            if (!(target.getEntity() instanceof ItemEntity) && !(target.getEntity() instanceof ExperienceOrb)) {
                branchChance = this.updateArcChanceBasedOnEntityCondition(target.getEntity(), branchChance);
                this.affectSingleTarget(context.getServerLevel(), target.getEntity(), source.getCaster(), damage, context);
                hits++;
            }
            lastTargetPosition = target.getEntity().position().add(new Vec3(0.0, (double) target.getEntity().getEyeHeight(), 0.0));
        }
        while (Math.random() < (double) branchChance) {
            Vec3 checkTargetPosition = lastTargetPosition;
            List<LivingEntity> entities = context.getServerLevel().m_6443_(LivingEntity.class, new AABB(BlockPos.containing(lastTargetPosition)).inflate((double) branchRadius), e -> {
                if (source.hasCasterReference() && e == source.getCaster()) {
                    return false;
                } else {
                    Vec3 entitySightPos = new Vec3(e.m_20185_(), e.m_20188_(), e.m_20189_());
                    boolean visionCheck = context.getServerLevel().m_45547_(new ClipContext(checkTargetPosition, entitySightPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null)).getType() == HitResult.Type.MISS;
                    return e.isAlive() && !context.hasEntityBeenAffected(this, e) && visionCheck;
                }
            });
            if (entities.size() == 0) {
                break;
            }
            Vec3 sortLTP = lastTargetPosition;
            entities.sort((o1, o2) -> {
                Double o1Dist = o1.m_20238_(sortLTP);
                Double o2Dist = o2.m_20238_(sortLTP);
                return o1Dist.compareTo(o2Dist);
            });
            LivingEntity nextTarget = (LivingEntity) entities.get(0);
            branchChance = this.updateArcChanceBasedOnEntityCondition(target.getEntity(), branchChance);
            context.addAffectedEntity(this, nextTarget);
            this.affectSingleTarget(context.getServerLevel(), nextTarget, source.getCaster(), damage, context);
            ServerMessageDispatcher.sendParticleSpawn(lastTargetPosition.x, lastTargetPosition.y, lastTargetPosition.z, nextTarget.m_20185_(), nextTarget.m_20186_() + (double) nextTarget.m_20192_(), nextTarget.m_20189_(), context.getSpell().getParticleColorOverride(), 64.0F, context.getServerLevel().m_46472_(), ParticleInit.LIGHTNING_BOLT.get());
            lastTargetPosition = nextTarget.m_20182_().add(new Vec3(0.0, (double) nextTarget.m_20192_(), 0.0));
            branchChance *= 0.75F;
            hits++;
        }
        return hits > 0 ? ComponentApplicationResult.SUCCESS : ComponentApplicationResult.FAIL;
    }

    private void affectSingleTarget(ServerLevel level, Entity target, @Nullable LivingEntity source, float damage, SpellContext context) {
        target.hurt(DamageHelper.createSourcedType(DamageHelper.LIGHTNING, context.getLevel().registryAccess(), source), damage);
        if (level != null && target.isAlive() && target instanceof Creeper && damage >= 20.0F) {
            CompoundTag tag = new CompoundTag();
            ((Creeper) target).addAdditionalSaveData(tag);
            tag.putBoolean("powered", true);
            ((Creeper) target).readAdditionalSaveData(tag);
        }
        context.addAffectedEntity(this, target);
    }

    private float updateArcChanceBasedOnEntityCondition(Entity target, float magnitudeIn) {
        return target instanceof LivingEntity && ((LivingEntity) target).hasEffect(EffectInit.SOAKED.get()) ? 1.0F : magnitudeIn;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.LIGHTNING;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.LIGHTNING;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 1) {
            float particle_spread = 1.0F;
            float v = 0.4F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3((double) (-v / 2.0F) + Math.random() * (double) v, Math.random() * (double) v, (double) (-v / 2.0F) + Math.random() * (double) v);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.BLUE_SPARKLE_VELOCITY.get()).setScale(0.05F).setGravity(0.02F).setPhysics(true), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + Math.random() + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.HARMFUL;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.FIRE, Affinity.LIGHTNING);
    }
}