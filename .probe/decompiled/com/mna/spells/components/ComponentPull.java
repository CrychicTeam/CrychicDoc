package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.particles.MAParticleType;
import com.mna.api.particles.ParticleInit;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ComponentPull extends SpellEffect {

    public static final String FLING_STRENGTH = "mna:flung";

    public static final String FLING_TIME = "mna:fling_time";

    public ComponentPull(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.SPEED, 1.0F, 1.0F, 3.0F, 1.0F, 5.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        float speed = modificationData.getValue(Attribute.SPEED);
        if (target.isLivingEntity()) {
            LivingEntity le = target.getLivingEntity();
            float max_velocity = 2.0F;
            if (CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), le, SlotTypePreset.RING)) {
                max_velocity = 3.0F;
            }
            if (source.hasCasterReference() && le == source.getCaster()) {
                if (source.isPlayerCaster()) {
                    return ComponentApplicationResult.FAIL;
                } else {
                    Vec3 motion = source.getCaster().m_20154_().normalize().scale(-0.5);
                    motion = motion.scale((double) (speed * 2.0F));
                    source.getCaster().m_5997_(motion.x, motion.y, motion.z);
                    motion = source.getCaster().m_20184_();
                    source.getCaster().m_20334_(Math.min((double) max_velocity, motion.x), Math.min((double) max_velocity, motion.y), Math.min((double) max_velocity, motion.z));
                    source.getCaster().f_19864_ = true;
                    if (source.isPlayerCaster()) {
                        ((ServerPlayer) source.getPlayer()).connection.send(new ClientboundSetEntityMotionPacket(le));
                    }
                    this.setFlags(le, speed);
                    return ComponentApplicationResult.SUCCESS;
                }
            } else if (le instanceof Player targetPlayer) {
                Vec3 motion = source.hasCasterReference() ? source.getCaster().m_20154_().normalize().scale(-0.5) : targetPlayer.m_20154_().normalize().scale(0.5);
                motion = motion.scale((double) (speed * 2.0F));
                targetPlayer.m_5997_(motion.x, motion.y, motion.z);
                motion = targetPlayer.m_20184_();
                targetPlayer.m_20334_(Math.min((double) max_velocity, motion.x), Math.min((double) max_velocity, motion.y), Math.min((double) max_velocity, motion.z));
                targetPlayer.f_19864_ = true;
                ((ServerPlayer) targetPlayer).connection.send(new ClientboundSetEntityMotionPacket(targetPlayer));
                if (!CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), targetPlayer, SlotTypePreset.RING)) {
                    this.setFlags(targetPlayer, speed);
                }
                return ComponentApplicationResult.SUCCESS;
            } else {
                float mX = (float) (target.getLivingEntity().m_20185_() - source.getOrigin().x());
                float mZ = (float) (target.getLivingEntity().m_20189_() - source.getOrigin().z());
                le.knockback((double) speed, (double) mX, (double) mZ);
                le.m_5997_(0.0, (double) (0.2F * speed), 0.0);
                if (le instanceof PathfinderMob) {
                    ((PathfinderMob) le).m_21573_().stop();
                }
                this.setFlags(le, speed);
                return ComponentApplicationResult.SUCCESS;
            }
        } else if (target.isEntity()) {
            float mX = (float) (source.getOrigin().x() - target.getEntity().getX());
            float mZ = (float) (source.getOrigin().z() - target.getEntity().getZ());
            target.getEntity().hasImpulse = true;
            Vec3 vector3d = target.getEntity().getDeltaMovement();
            Vec3 vector3d1 = new Vec3((double) mX, 0.0, (double) mZ).normalize().scale((double) (-speed));
            target.getEntity().setDeltaMovement(vector3d.x / 2.0 - vector3d1.x, target.getEntity().onGround() ? Math.min(0.4, vector3d.y / 2.0 + (double) speed) : vector3d.y, vector3d.z / 2.0 - vector3d1.z);
            return ComponentApplicationResult.SUCCESS;
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    private void setFlags(LivingEntity le, float strength) {
        le.getPersistentData().putFloat("mna:flung", strength);
        le.getPersistentData().putLong("mna:fling_time", le.m_9236_().getGameTime());
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.Single.WIND;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age <= 10 && caster != null) {
            float particle_spread = 1.0F;
            int particleCount = 10;
            Vec3 velocity = caster.m_20154_();
            for (int i = 0; i < particleCount; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_VELOCITY.get()).setScale(0.2F).setColor(10, 10, 10), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, velocity.x, velocity.y, velocity.z);
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
        return 200;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.NEUTRAL;
    }
}