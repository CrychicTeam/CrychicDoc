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
import com.mna.blocks.BlockInit;
import com.mna.config.GeneralConfig;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ComponentImpale extends SpellEffect implements IDamageComponent {

    private static final Predicate<? super Entity> TARGET_PREDICATE = e -> e instanceof LivingEntity && e.isAlive();

    public ComponentImpale(ResourceLocation icon) {
        super(icon, new AttributeValuePair(Attribute.DAMAGE, 5.0F, 1.0F, 15.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.SPEED, 1.0F, 1.0F, 3.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.DURATION, 3.0F, 1.0F, 10.0F, 1.0F, 1.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        BlockPos targetPos = target.getBlock();
        Direction targetFace = target.getBlockFace(this);
        if (target.isEntity()) {
            targetPos = target.getEntity().blockPosition().below();
            targetFace = Direction.UP;
        }
        SpellEffect.BlockPlaceResult bpr = this.tryPlaceBlock(source.getPlayer(), context.getServerLevel(), BlockInit.IMPALE_SPIKE.get(), targetPos, targetFace, true, null);
        if (bpr.success) {
            float strength = modificationData.getValue(Attribute.SPEED);
            Vec3 vel = targetFace.getAxis() == Direction.Axis.Y ? Vec3.atLowerCornerOf(targetFace.getOpposite().getNormal()).normalize().scale((double) strength).add(0.0, 1.0, 0.0) : Vec3.atLowerCornerOf(targetFace.getOpposite().getNormal()).normalize().scale((double) strength);
            context.getServerLevel().m_6249_(source.getCaster(), new AABB(bpr.position), TARGET_PREDICATE).stream().map(e -> (LivingEntity) e).forEach(e -> {
                e.hurt(DamageHelper.createSourcedType(DamageTypes.FALLING_STALACTITE, context.getLevel().registryAccess(), source.getCaster()), modificationData.getValue(Attribute.DAMAGE) * GeneralConfig.getDamageMultiplier());
                ComponentFling.flingTarget(e, vel, strength, 2.0F, 1.0F);
            });
            int duration = (int) modificationData.getValue(Attribute.DURATION);
            context.getServerLevel().m_186460_(bpr.position, BlockInit.IMPALE_SPIKE.get(), duration * 20 + (int) (Math.random() * 20.0));
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 1) {
            float particle_spread = 1.0F;
            float v = 0.1F;
            int particleCount = 10;
            for (int i = 0; i < particleCount; i++) {
                Vec3 velocity = new Vec3((double) (-v / 2.0F) + Math.random() * (double) v, Math.random() * (double) v, (double) (-v / 2.0F) + Math.random() * (double) v);
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.DUST.get()), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
            }
        }
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.AoE.EARTH;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.EARTH;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
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
        return Arrays.asList(Affinity.EARTH, Affinity.ICE);
    }
}