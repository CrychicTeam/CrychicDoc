package yesman.epicfight.skill.weaponinnate;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class ConditionalWeaponInnateSkill extends WeaponInnateSkill {

    protected final StaticAnimation[] attackAnimations;

    protected final Function<ServerPlayerPatch, Integer> selector;

    public static ConditionalWeaponInnateSkill.Builder createConditionalWeaponInnateBuilder() {
        return new ConditionalWeaponInnateSkill.Builder().setCategory(SkillCategories.WEAPON_INNATE).setResource(Skill.Resource.WEAPON_INNATE_ENERGY);
    }

    public ConditionalWeaponInnateSkill(ConditionalWeaponInnateSkill.Builder builder) {
        super(builder);
        this.properties = Lists.newArrayList();
        this.attackAnimations = new StaticAnimation[builder.animationLocations.length];
        this.selector = builder.selector;
        for (int i = 0; i < builder.animationLocations.length; i++) {
            this.attackAnimations[i] = EpicFightMod.getInstance().animationManager.findAnimationByPath(builder.animationLocations[i].toString());
        }
    }

    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        List<Component> list = super.getTooltipOnItem(itemStack, cap, playerCap);
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, (Map<AnimationProperty.AttackPhaseProperty<?>, Object>) this.properties.get(0), "Each Strikes:");
        return list;
    }

    public WeaponInnateSkill registerPropertiesToAnimation() {
        for (int i = 0; i < this.attackAnimations.length; i++) {
            this.attackAnimations[i] = EpicFightMod.getInstance().animationManager.refreshAnimation(this.attackAnimations[i]);
        }
        for (StaticAnimation animation : this.attackAnimations) {
            AttackAnimation anim = (AttackAnimation) animation;
            for (AttackAnimation.Phase phase : anim.phases) {
                phase.addProperties(((Map) this.properties.get(0)).entrySet());
            }
        }
        return this;
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        executer.playAnimationSynchronized(this.attackAnimations[this.getAnimationInCondition(executer)], 0.0F);
        super.executeOnServer(executer, args);
    }

    public int getAnimationInCondition(ServerPlayerPatch executer) {
        return (Integer) this.selector.apply(executer);
    }

    public static class Builder extends Skill.Builder<ConditionalWeaponInnateSkill> {

        protected Function<ServerPlayerPatch, Integer> selector;

        protected ResourceLocation[] animationLocations;

        public ConditionalWeaponInnateSkill.Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public ConditionalWeaponInnateSkill.Builder setActivateType(Skill.ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public ConditionalWeaponInnateSkill.Builder setResource(Skill.Resource resource) {
            this.resource = resource;
            return this;
        }

        public ConditionalWeaponInnateSkill.Builder setSelector(Function<ServerPlayerPatch, Integer> selector) {
            this.selector = selector;
            return this;
        }

        public ConditionalWeaponInnateSkill.Builder setAnimations(ResourceLocation... animationLocations) {
            this.animationLocations = animationLocations;
            return this;
        }
    }
}