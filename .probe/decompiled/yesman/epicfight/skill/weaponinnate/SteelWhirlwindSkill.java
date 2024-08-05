package yesman.epicfight.skill.weaponinnate;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.events.engine.ControllEngine;
import yesman.epicfight.client.input.EpicFightKeyMappings;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.network.server.SPSkillExecutionFeedback;
import yesman.epicfight.skill.ChargeableSkill;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class SteelWhirlwindSkill extends WeaponInnateSkill implements ChargeableSkill {

    private static final UUID EVENT_UUID = UUID.fromString("d2d057cc-f30f-11ed-a05b-0242ac120003");

    private AnimationProvider chargingAnimation = () -> Animations.STEEL_WHIRLWIND_CHARGING;

    private AnimationProvider.AttackAnimationProvider attackAnimation = () -> (AttackAnimation) Animations.STEEL_WHIRLWIND;

    public static int getChargingPower(SkillContainer skillContainer) {
        return skillContainer.getDataManager().getDataValue(SkillDataKeys.CHARGING_POWER.get());
    }

    public SteelWhirlwindSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (event.getPlayerPatch().isChargingSkill(this)) {
                LocalPlayer clientPlayer = event.getPlayerPatch().getOriginal();
                clientPlayer.m_6858_(false);
                clientPlayer.sprintTriggerTime = -1;
                Minecraft mc = Minecraft.getInstance();
                ClientEngine.getInstance().controllEngine.setKeyBind(mc.options.keySprint, false);
                Input var10000 = event.getMovementInput();
                var10000.forwardImpulse = var10000.forwardImpulse * (1.0F - 0.8F * (float) event.getPlayerPatch().getSkillChargingTicks() / 30.0F);
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
    }

    public WeaponInnateSkill registerPropertiesToAnimation() {
        AttackAnimation anim = this.attackAnimation.get();
        for (AttackAnimation.Phase phase : anim.phases) {
            phase.addProperties(((Map) this.properties.get(0)).entrySet());
        }
        return this;
    }

    @Override
    public int getAllowedMaxChargingTicks() {
        return 60;
    }

    @Override
    public int getMaxChargingTicks() {
        return 30;
    }

    @Override
    public int getMinChargingTicks() {
        return 6;
    }

    @Override
    public void startCharging(PlayerPatch<?> caster) {
        caster.playAnimationSynchronized(this.chargingAnimation.get(), 0.0F);
    }

    @Override
    public void resetCharging(PlayerPatch<?> caster) {
    }

    @Override
    public void castSkill(ServerPlayerPatch caster, SkillContainer skillContainer, int chargingTicks, SPSkillExecutionFeedback feedbackPacket, boolean onMaxTick) {
        caster.getSkill(this).getDataManager().setDataSync(SkillDataKeys.CHARGING_POWER.get(), chargingTicks, caster.getOriginal());
        caster.playAnimationSynchronized(this.attackAnimation.get(), 0.0F);
        this.cancelOnServer(caster, null);
    }

    @Override
    public KeyMapping getKeyMapping() {
        return EpicFightKeyMappings.WEAPON_INNATE_SKILL;
    }

    @Override
    public void gatherChargingArguemtns(LocalPlayerPatch caster, ControllEngine controllEngine, FriendlyByteBuf buffer) {
    }

    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, (Map<AnimationProperty.AttackPhaseProperty<?>, Object>) this.properties.get(0), "Each Strike:");
        return list;
    }
}