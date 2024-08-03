package yesman.epicfight.skill.weaponinnate;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.property.AnimationProperty;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class RushingTempoSkill extends WeaponInnateSkill {

    private final Map<ResourceLocation, Supplier<AttackAnimation>> comboAnimation = Maps.newHashMap();

    public RushingTempoSkill(Skill.Builder<? extends Skill> builder) {
        super(builder);
        this.comboAnimation.put(Animations.TACHI_AUTO1.getRegistryName(), (Supplier) () -> (AttackAnimation) Animations.RUSHING_TEMPO1);
        this.comboAnimation.put(Animations.TACHI_AUTO2.getRegistryName(), (Supplier) () -> (AttackAnimation) Animations.RUSHING_TEMPO2);
        this.comboAnimation.put(Animations.TACHI_AUTO3.getRegistryName(), (Supplier) () -> (AttackAnimation) Animations.RUSHING_TEMPO3);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
    }

    @Override
    public void onRemoved(SkillContainer container) {
    }

    @Override
    public void executeOnServer(ServerPlayerPatch executer, FriendlyByteBuf args) {
        ResourceLocation rl = executer.<Animator>getAnimator().getPlayerFor(null).getAnimation().getRegistryName();
        if (this.comboAnimation.containsKey(rl)) {
            executer.playAnimationSynchronized((StaticAnimation) ((Supplier) this.comboAnimation.get(executer.<Animator>getAnimator().getPlayerFor(null).getAnimation().getRegistryName())).get(), 0.0F);
            super.executeOnServer(executer, args);
        }
    }

    @Override
    public boolean checkExecuteCondition(PlayerPatch<?> executer) {
        EntityState playerState = executer.getEntityState();
        return this.comboAnimation.containsKey(executer.<Animator>getAnimator().getPlayerFor(null).getAnimation().getRegistryName()) && playerState.canUseSkill() && playerState.inaction();
    }

    @Override
    public List<Component> getTooltipOnItem(ItemStack itemStack, CapabilityItem cap, PlayerPatch<?> playerCap) {
        List<Component> list = Lists.newArrayList();
        String traslatableText = this.getTranslationKey();
        list.add(Component.translatable(traslatableText).withStyle(ChatFormatting.WHITE).append(Component.literal(String.format("[%.0f]", this.consumption)).withStyle(ChatFormatting.AQUA)));
        list.add(Component.translatable(traslatableText + ".tooltip", this.maxStackSize).withStyle(ChatFormatting.DARK_GRAY));
        this.generateTooltipforPhase(list, itemStack, cap, playerCap, (Map<AnimationProperty.AttackPhaseProperty<?>, Object>) this.properties.get(0), "Each Strike:");
        return list;
    }

    public WeaponInnateSkill registerPropertiesToAnimation() {
        this.comboAnimation.values().forEach(animation -> ((AttackAnimation) animation.get()).phases[0].addProperties(((Map) this.properties.get(0)).entrySet()));
        return this;
    }
}