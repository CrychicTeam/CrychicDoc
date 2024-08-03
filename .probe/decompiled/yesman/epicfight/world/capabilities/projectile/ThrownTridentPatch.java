package yesman.epicfight.world.capabilities.projectile;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import yesman.epicfight.api.utils.math.ValueModifier;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPSpawnData;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.skill.weaponinnate.EverlastingAllegiance;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageSources;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.damagesource.ExtraDamageInstance;
import yesman.epicfight.world.damagesource.StunType;

public class ThrownTridentPatch extends ProjectilePatch<ThrownTrident> {

    private boolean innateActivated;

    private int returnTick;

    private float independentXRotO;

    private float independentXRot;

    @Override
    public void onStartTracking(ServerPlayer trackingPlayer) {
        if (this.innateActivated) {
            SPSpawnData packet = new SPSpawnData(this.original.m_19879_());
            packet.getBuffer().writeInt(this.returnTick);
            packet.getBuffer().writeInt(this.original.f_19797_);
            EpicFightNetworkManager.sendToPlayer(packet, trackingPlayer);
        }
    }

    @Override
    public void processSpawnData(ByteBuf buf) {
        this.innateActivated = true;
        this.returnTick = buf.readInt();
        this.original.f_19797_ = buf.readInt();
    }

    protected void setMaxStrikes(ThrownTrident projectileEntity, int maxStrikes) {
        projectileEntity.m_36767_((byte) (maxStrikes - 1));
    }

    public void onJoinWorld(ThrownTrident projectileEntity, EntityJoinLevelEvent event) {
        super.onJoinWorld(projectileEntity, event);
        if (!this.isLogicalClient()) {
            ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(projectileEntity.m_19749_(), ServerPlayerPatch.class);
            if (playerpatch != null) {
                SkillContainer container = playerpatch.getSkill(SkillSlots.WEAPON_INNATE);
                if (container.getSkill() instanceof EverlastingAllegiance) {
                    EverlastingAllegiance.setThrownTridentEntityId(playerpatch.getOriginal(), container, projectileEntity.m_19879_());
                }
            }
            this.armorNegation = 20.0F;
        }
    }

    public void tickEnd() {
        if (!this.isLogicalClient()) {
            if (this.original.dealtDamage) {
                ServerPlayerPatch playerpatch = EpicFightCapabilities.getEntityPatch(this.original.m_19749_(), ServerPlayerPatch.class);
                if (playerpatch != null) {
                    SkillContainer container = playerpatch.getSkill(SkillSlots.WEAPON_INNATE);
                    if (container.getSkill() instanceof EverlastingAllegiance && EverlastingAllegiance.getThrownTridentEntityId(container) > -1) {
                        EverlastingAllegiance.setThrownTridentEntityId(playerpatch.getOriginal(), container, -1);
                    }
                }
            }
            if (this.innateActivated) {
                List<Entity> entities = this.original.m_9236_().m_45933_(this.original, this.original.m_20191_().inflate(1.0, 1.0, 1.0));
                EpicFightDamageSources damageSources = EpicFightDamageSources.of(this.original.m_9236_());
                EpicFightDamageSource source = damageSources.trident(this.original.m_19749_(), this.original).setStunType(StunType.HOLD).addRuntimeTag(EpicFightDamageType.WEAPON_INNATE).addExtraDamage(ExtraDamageInstance.SWEEPING_EDGE_ENCHANTMENT.create()).setDamageModifier(ValueModifier.multiplier(1.4F)).setArmorNegation(30.0F);
                for (Entity entity : entities) {
                    if (!entity.is(this.original.m_19749_())) {
                        float f = 8.0F;
                        if (entity instanceof LivingEntity) {
                            LivingEntity livingentity = (LivingEntity) entity;
                            f += EnchantmentHelper.getDamageBonus(this.original.tridentItem, livingentity.getMobType());
                            if (entity.hurt(source, f)) {
                                entity.playSound(EpicFightSounds.BLADE_HIT.get(), 1.0F, 1.0F);
                                ((ServerLevel) entity.level()).sendParticles(EpicFightParticles.HIT_BLADE.get(), entity.position().x, entity.position().y + (double) entity.getBbHeight() * 0.5, entity.position().z, 0, 0.0, 0.0, 0.0, 1.0);
                            }
                        }
                    }
                }
            }
        }
        if (this.innateActivated) {
            int elapsedTicks = Math.max(this.original.f_19797_ - this.returnTick - 10, 0);
            Vec3 toOwner = this.original.m_19749_().getEyePosition().subtract(this.original.m_20182_());
            double length = toOwner.length();
            double speed = Math.min(Math.pow((double) elapsedTicks, 2.0) * 5.0E-4 + Math.abs((double) elapsedTicks * 0.05), Math.min(10.0, length));
            Vec3 toMaster = toOwner.normalize().scale(speed);
            this.original.m_20256_(new Vec3(0.0, 0.0, 0.0));
            Vec3 pos = this.original.m_20182_();
            this.original.m_6034_(pos.x + toMaster.x, pos.y + toMaster.y, pos.z + toMaster.z);
            this.original.m_146926_(0.0F);
            this.original.f_19860_ = 0.0F;
            this.original.m_146922_(0.0F);
            this.original.f_19859_ = 0.0F;
            this.independentXRotO = this.independentXRot;
            this.independentXRot += 60.0F;
            this.original.f_19860_ = this.independentXRotO;
            this.original.m_146926_(this.independentXRot);
            if (this.original.f_19797_ % 3 == 0) {
                this.original.m_5496_(EpicFightSounds.WHOOSH_ROD.get(), 3.0F, 1.0F);
            }
        }
    }

    public boolean isInnateActivated() {
        return this.innateActivated;
    }

    public void catchByPlayer(PlayerPatch<?> playerpatch) {
        playerpatch.playAnimationSynchronized(Animations.EVERLASTING_ALLEGIANCE_CATCH, 0.0F);
    }

    public void recalledBySkill() {
        this.original.m_5496_(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
        this.original.dealtDamage = true;
        this.innateActivated = true;
        this.independentXRot = this.original.m_146909_();
        this.returnTick = this.original.f_19797_;
        this.initialFirePosition = this.original.m_20182_();
    }
}