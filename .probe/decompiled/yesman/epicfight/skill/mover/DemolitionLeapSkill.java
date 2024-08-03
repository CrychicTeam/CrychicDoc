package yesman.epicfight.skill.mover;

import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.phys.Vec3;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPPlayAnimation;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.skill.SkillSlots;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class DemolitionLeapSkill extends Skill implements ChargeableSkill {

    private static final UUID EVENT_UUID = UUID.fromString("3d142bf4-0dcd-11ee-be56-0242ac120002");

    private AnimationProvider chargingAnimation = () -> Animations.BIPED_DEMOLITION_LEAP_CHARGING;

    private AnimationProvider shootAnimation = () -> Animations.BIPED_DEMOLITION_LEAP;

    public DemolitionLeapSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (event.getPlayerPatch().isChargingSkill(this)) {
                event.getMovementInput().jumping = false;
            }
        });
        listener.addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, event -> {
            if (((DamageSource) event.getDamageSource()).is(DamageTypeTags.IS_FALL) && container.getDataManager().getDataValue(SkillDataKeys.PROTECT_NEXT_FALL.get())) {
                float damage = event.getAmount();
                event.setAmount(damage * 0.5F);
                event.setCanceled(true);
                container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), false);
            }
        }, 1);
        listener.addEventListener(PlayerEventListener.EventType.FALL_EVENT, EVENT_UUID, event -> {
            if (LevelUtil.calculateLivingEntityFallDamage(event.getForgeEvent().getEntity(), event.getForgeEvent().getDamageMultiplier(), event.getForgeEvent().getDistance()) == 0) {
                container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), false);
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, 1);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.FALL_EVENT, EVENT_UUID);
    }

    @Override
    public boolean isExecutableState(PlayerPatch<?> executer) {
        return super.isExecutableState(executer) && executer.getOriginal().m_20096_();
    }

    @Override
    public void cancelOnClient(LocalPlayerPatch executer, FriendlyByteBuf args) {
        super.cancelOnClient(executer, args);
        executer.resetSkillCharging();
        executer.playAnimationSynchronized(Animations.BIPED_IDLE, 0.0F);
    }

    @Override
    public void executeOnClient(LocalPlayerPatch executer, FriendlyByteBuf args) {
        args.readInt();
        int ticks = args.readInt();
        int modifiedTicks = (int) (7.4668F * Math.log10((double) ((float) ticks + 1.0F)) / Math.log10(2.0));
        Vec3f jumpDirection = new Vec3f(0.0F, (float) modifiedTicks * 0.05F, 0.0F);
        float xRot = Mth.clamp(70.0F + Mth.clamp(executer.getCameraXRot(), -90.0F, 0.0F), 0.0F, 70.0F);
        jumpDirection.add(0.0F, xRot / 70.0F * 0.05F, 0.0F);
        jumpDirection.rotate(xRot, Vec3f.X_AXIS);
        jumpDirection.rotate(-executer.getCameraYRot(), Vec3f.Y_AXIS);
        executer.getOriginal().m_20256_(jumpDirection.toDoubleVector());
        executer.resetSkillCharging();
    }

    @Override
    public void gatherChargingArguemtns(LocalPlayerPatch caster, ControllEngine controllEngine, FriendlyByteBuf buffer) {
        controllEngine.setChargingKey(SkillSlots.MOVER, this.getKeyMapping());
        caster.startSkillCharging(this);
    }

    @Override
    public void startCharging(PlayerPatch<?> caster) {
        caster.<Animator>getAnimator().playAnimation(this.chargingAnimation.get(), 0.0F);
        if (!caster.isLogicalClient()) {
            EpicFightNetworkManager.sendToAllPlayerTrackingThisEntity(new SPPlayAnimation(this.chargingAnimation.get(), 0.0F, caster), caster.getOriginal());
        }
    }

    @Override
    public void resetCharging(PlayerPatch<?> caster) {
    }

    @Override
    public void castSkill(ServerPlayerPatch caster, SkillContainer skillContainer, int chargingTicks, SPSkillExecutionFeedback feedbackPacket, boolean onMaxTick) {
        if (onMaxTick) {
            feedbackPacket.setFeedbackType(SPSkillExecutionFeedback.FeedbackType.EXPIRED);
        } else {
            caster.playSound(EpicFightSounds.ROCKET_JUMP.get(), 1.0F, 0.0F, 0.0F);
            caster.playSound(EpicFightSounds.ENTITY_MOVE.get(), 1.0F, 0.0F, 0.0F);
            int accumulatedTicks = caster.getChargingAmount();
            LevelUtil.circleSlamFracture(null, caster.getOriginal().m_9236_(), caster.getOriginal().m_20182_().subtract(0.0, 1.0, 0.0), (double) accumulatedTicks * 0.05, true, false, false);
            Vec3 entityEyepos = caster.getOriginal().m_146892_();
            EpicFightParticles.AIR_BURST.get().spawnParticleWithArgument(caster.getOriginal().serverLevel(), entityEyepos.x, entityEyepos.y, entityEyepos.z, 0.0, 0.0, 2.0 + 0.05 * (double) chargingTicks);
            caster.playAnimationSynchronized(this.shootAnimation.get(), 0.0F);
            feedbackPacket.getBuffer().writeInt(accumulatedTicks);
            skillContainer.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), true);
        }
    }

    @Override
    public int getAllowedMaxChargingTicks() {
        return 80;
    }

    @Override
    public int getMaxChargingTicks() {
        return 40;
    }

    @Override
    public int getMinChargingTicks() {
        return 12;
    }

    @Override
    public KeyMapping getKeyMapping() {
        return EpicFightKeyMappings.MOVER_SKILL;
    }

    @Override
    public void chargingTick(PlayerPatch<?> caster) {
        int chargingTicks = caster.getSkillChargingTicks();
        if (chargingTicks % 5 == 0 && caster.getAccumulatedChargeAmount() < this.getMaxChargingTicks() && caster.consumeStamina(this.consumption)) {
            caster.setChargingAmount(caster.getChargingAmount() + 5);
        }
    }
}