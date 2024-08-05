package yesman.epicfight.skill.weaponinnate;

import java.util.List;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.AnimationProvider;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class SimpleWeaponInnateSkill extends WeaponInnateSkill {

    protected AnimationProvider.AttackAnimationProvider attackAnimation;

    public static SimpleWeaponInnateSkill.Builder createSimpleWeaponInnateBuilder() {
        return new SimpleWeaponInnateSkill.Builder().setCategory(SkillCategories.WEAPON_INNATE).setResource(Skill.Resource.WEAPON_INNATE_ENERGY);
    }

    public SimpleWeaponInnateSkill(SimpleWeaponInnateSkill.Builder builder) {
        super(builder);
        this.attackAnimation = () -> (AttackAnimation) EpicFightMod.getInstance().animationManager.findAnimationByPath(builder.attackAnimation.toString());
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        executer.playAnimationSynchronized(this.attackAnimation.get(), 0.0F);
        super.executeOnServer(executer, args);
    }

    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, (Map<AnimationProperty.AttackPhaseProperty<?>, Object>) this.properties.get(0), "Each Strike:");
        return list;
    }

    public WeaponInnateSkill registerPropertiesToAnimation() {
        AttackAnimation anim = this.attackAnimation.get();
        for (AttackAnimation.Phase phase : anim.phases) {
            phase.addProperties(((Map) this.properties.get(0)).entrySet());
        }
        return this;
    }

    public static class Builder extends Skill.Builder<SimpleWeaponInnateSkill> {

        protected ResourceLocation attackAnimation;

        public SimpleWeaponInnateSkill.Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public SimpleWeaponInnateSkill.Builder setActivateType(Skill.ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public SimpleWeaponInnateSkill.Builder setResource(Skill.Resource resource) {
            this.resource = resource;
            return this;
        }

        public SimpleWeaponInnateSkill.Builder setAnimations(ResourceLocation attackAnimation) {
            this.attackAnimation = attackAnimation;
            return this;
        }
    }
}