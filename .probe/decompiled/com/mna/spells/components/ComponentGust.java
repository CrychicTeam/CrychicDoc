package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerMagic;
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
import com.mna.api.tools.MATags;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.interop.CuriosInterop;
import com.mna.items.ItemInit;
import com.mna.tools.BlockUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import top.theillusivec4.curios.api.SlotTypePreset;

public class ComponentGust extends SpellEffect {

    public ComponentGust(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.PRECISION, 0.0F, 0.0F, 1.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.SPEED, 0.0F, 0.0F, 3.0F, 1.0F, 2.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        float speed = modificationData.getValue(Attribute.SPEED);
        float precision = modificationData.getValue(Attribute.PRECISION);
        if (target.isBlock() && precision == 0.0F) {
            if (!context.getServerLevel().m_46749_(target.getBlock())) {
                return ComponentApplicationResult.FAIL;
            }
            BlockState state = context.getServerLevel().m_8055_(target.getBlock());
            if (BlockStateIsGustable(state) && BlockUtils.destroyBlock(source.getCaster(), context.getServerLevel(), target.getBlock(), true, Tiers.WOOD)) {
                BlockUtils.updateBlockState(context.getServerLevel(), target.getBlock());
                return ComponentApplicationResult.SUCCESS;
            }
        } else if (target.isEntity() && speed > 0.0F) {
            if (target.getEntity() instanceof ItemEntity) {
                Vec3 tPos = target.getEntity().position();
                Vec3 cPos = source.getOrigin();
                Vec3 delta = cPos.subtract(tPos).normalize();
                target.getEntity().setDeltaMovement(delta);
                return ComponentApplicationResult.SUCCESS;
            }
            if (target.getEntity() instanceof LivingEntity) {
                if (context.hasEntityBeenAffected(this, target.getEntity())) {
                    return ComponentApplicationResult.FAIL;
                }
                context.addAffectedEntity(this, target.getEntity());
                LivingEntity le = target.getLivingEntity();
                float max_velocity = 1.5F;
                if (CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), source.getCaster(), SlotTypePreset.RING)) {
                    max_velocity += 0.5F;
                }
                Vec3 motion = new Vec3(0.0, 0.25, 0.0);
                motion = motion.scale((double) (speed * 2.0F));
                if (!(le instanceof Player targetPlayer)) {
                    le.m_5997_(0.0, motion.y, 0.0);
                    motion = le.m_20184_();
                    if (motion.y() > (double) max_velocity) {
                        motion = new Vec3(motion.x, (double) max_velocity, motion.z);
                        le.m_20256_(motion);
                    }
                    if (le instanceof PathfinderMob) {
                        ((PathfinderMob) le).m_21573_().stop();
                    }
                    this.setFlags(le, speed);
                    return ComponentApplicationResult.SUCCESS;
                }
                if (targetPlayer == source.getCaster()) {
                    IPlayerMagic magic = (IPlayerMagic) source.getPlayer().getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                    if (magic == null || magic.getAirCasts() >= magic.getAirCastLimit(source.getPlayer(), context.getSpell())) {
                        return ComponentApplicationResult.FAIL;
                    }
                    if (!source.getPlayer().m_20096_()) {
                        magic.incrementAirCasts(source.getPlayer(), context.getSpell());
                    }
                }
                targetPlayer.m_5997_(0.0, motion.y, 0.0);
                motion = targetPlayer.m_20184_();
                if (motion.y() > (double) max_velocity) {
                    motion = new Vec3(motion.x, (double) max_velocity, motion.z);
                }
                targetPlayer.m_20334_(motion.x, motion.y, motion.z);
                targetPlayer.f_19864_ = true;
                ((ServerPlayer) targetPlayer).connection.send(new ClientboundSetEntityMotionPacket(targetPlayer));
                if (!CuriosInterop.IsItemInCurioSlot(ItemInit.AIR_CAST_RING.get(), targetPlayer, SlotTypePreset.RING)) {
                    this.setFlags(targetPlayer, speed);
                }
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    private void setFlags(LivingEntity le, float strength) {
        le.getPersistentData().putFloat("mna:flung", strength);
        le.getPersistentData().putLong("mna:fling_time", le.m_9236_().getGameTime());
    }

    public static boolean BlockStateIsGustable(BlockState state) {
        return MATags.isBlockIn(state.m_60734_(), MATags.Blocks.GUST_DESTRUCTIBLE_BLOCKS) || state.m_60734_() instanceof IPlantable;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.WIND;
    }

    @Override
    public SoundEvent SoundEffect() {
        return SFX.Spell.Impact.AoE.WIND;
    }

    @Override
    public void SpawnParticles(Level world, Vec3 impact_position, Vec3 normal, int age, LivingEntity caster, ISpellDefinition recipe) {
        if (age < 10) {
            float particle_spread = 1.0F;
            int particleCount = 5;
            for (int i = 0; i < particleCount; i++) {
                world.addParticle(recipe.colorParticle(new MAParticleType(ParticleInit.AIR_ORBIT.get()).setScale(0.2F).setColor(10, 10, 10), caster), impact_position.x + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.y + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, impact_position.z + (double) (-particle_spread) + Math.random() * (double) particle_spread * 2.0, Math.random() * 0.2F, 0.3F, 2.5);
            }
        }
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 50;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.WIND, Affinity.LIGHTNING, Affinity.WATER);
    }
}