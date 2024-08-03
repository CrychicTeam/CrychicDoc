package yesman.epicfight.skill.guard;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.AttackResult;
import yesman.epicfight.api.utils.ExtendableEnum;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.client.gui.BattleModeGui;
import yesman.epicfight.client.world.capabilites.entitypatch.player.LocalPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.EpicFightSkills;
import yesman.epicfight.gameasset.EpicFightSounds;
import yesman.epicfight.particle.EpicFightParticles;
import yesman.epicfight.particle.HitParticleType;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.damagesource.EpicFightDamageSource;
import yesman.epicfight.world.damagesource.EpicFightDamageType;
import yesman.epicfight.world.entity.eventlistener.HurtEvent;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class GuardSkill extends Skill {

    protected static final UUID EVENT_UUID = UUID.fromString("b422f7a0-f378-11eb-9a03-0242ac130003");

    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions;

    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> advancedGuardMotions;

    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardBreakMotions;

    protected float penalizer;

    public static GuardSkill.Builder createGuardBuilder() {
        return new GuardSkill.Builder().setCategory(SkillCategories.GUARD).setActivateType(Skill.ActivateType.ONE_SHOT).setResource(Skill.Resource.STAMINA).addGuardMotion(CapabilityItem.WeaponCategories.AXE, (item, player) -> Animations.SWORD_GUARD_HIT).addGuardMotion(CapabilityItem.WeaponCategories.GREATSWORD, (item, player) -> Animations.GREATSWORD_GUARD_HIT).addGuardMotion(CapabilityItem.WeaponCategories.UCHIGATANA, (item, player) -> Animations.UCHIGATANA_GUARD_HIT).addGuardMotion(CapabilityItem.WeaponCategories.LONGSWORD, (item, player) -> Animations.LONGSWORD_GUARD_HIT).addGuardMotion(CapabilityItem.WeaponCategories.SPEAR, (item, player) -> item.getStyle(player) == CapabilityItem.Styles.TWO_HAND ? Animations.SPEAR_GUARD_HIT : null).addGuardMotion(CapabilityItem.WeaponCategories.SWORD, (item, player) -> item.getStyle(player) == CapabilityItem.Styles.ONE_HAND ? Animations.SWORD_GUARD_HIT : Animations.SWORD_DUAL_GUARD_HIT).addGuardMotion(CapabilityItem.WeaponCategories.TACHI, (item, player) -> Animations.LONGSWORD_GUARD_HIT).addGuardBreakMotion(CapabilityItem.WeaponCategories.AXE, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED).addGuardBreakMotion(CapabilityItem.WeaponCategories.GREATSWORD, (item, player) -> Animations.GREATSWORD_GUARD_BREAK).addGuardBreakMotion(CapabilityItem.WeaponCategories.UCHIGATANA, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED).addGuardBreakMotion(CapabilityItem.WeaponCategories.LONGSWORD, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED).addGuardBreakMotion(CapabilityItem.WeaponCategories.SPEAR, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED).addGuardBreakMotion(CapabilityItem.WeaponCategories.SWORD, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED).addGuardBreakMotion(CapabilityItem.WeaponCategories.TACHI, (item, player) -> Animations.BIPED_COMMON_NEUTRALIZED);
    }

    public GuardSkill(GuardSkill.Builder builder) {
        super(builder);
        this.guardMotions = builder.guardMotions;
        this.advancedGuardMotions = builder.advancedGuardMotions;
        this.guardBreakMotions = builder.guardBreakMotions;
    }

    @Override
    public void setParams(CompoundTag parameters) {
        super.setParams(parameters);
        this.penalizer = parameters.getFloat("penalizer");
    }

    @Override
    public void onInitiate(SkillContainer container) {
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID, event -> {
            CapabilityItem itemCapability = ((LocalPlayerPatch) event.getPlayerPatch()).getHoldingItemCapability(InteractionHand.MAIN_HAND);
            if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, GuardSkill.BlockType.GUARD) && this.isExecutableState(event.getPlayerPatch())) {
                ((LocalPlayerPatch) event.getPlayerPatch()).getOriginal().startUsingItem(InteractionHand.MAIN_HAND);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID, event -> {
            CapabilityItem itemCapability = ((ServerPlayerPatch) event.getPlayerPatch()).getHoldingItemCapability(InteractionHand.MAIN_HAND);
            if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, GuardSkill.BlockType.GUARD) && this.isExecutableState(event.getPlayerPatch())) {
                ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal().m_6672_(InteractionHand.MAIN_HAND);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID, event -> {
            ServerPlayer serverplayer = event.getPlayerPatch().getOriginal();
            container.getDataManager().setDataSync(SkillDataKeys.PENALTY_RESTORE_COUNTER.get(), serverplayer.f_19797_, serverplayer);
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID, event -> container.getDataManager().setDataSync(SkillDataKeys.PENALTY.get(), 0.0F, event.getPlayerPatch().getOriginal()));
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event -> {
            if (container.getExecuter().getOriginal().m_6117_() && this.guardMotions.containsKey(container.getExecuter().getHoldingItemCapability(InteractionHand.MAIN_HAND).getWeaponCategory())) {
                LocalPlayer clientPlayer = event.getPlayerPatch().getOriginal();
                clientPlayer.m_6858_(false);
                clientPlayer.sprintTriggerTime = -1;
                Minecraft mc = Minecraft.getInstance();
                ClientEngine.getInstance().controllEngine.setKeyBind(mc.options.keySprint, false);
            }
        });
        container.getExecuter().getEventListener().addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, event -> {
            CapabilityItem itemCapability = ((ServerPlayerPatch) event.getPlayerPatch()).getHoldingItemCapability(((ServerPlayerPatch) event.getPlayerPatch()).getOriginal().m_7655_());
            if (this.isHoldingWeaponAvailable(event.getPlayerPatch(), itemCapability, GuardSkill.BlockType.GUARD) && ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal().m_6117_() && this.isExecutableState(event.getPlayerPatch())) {
                DamageSource damageSource = (DamageSource) event.getDamageSource();
                boolean isFront = false;
                Vec3 sourceLocation = damageSource.getSourcePosition();
                if (sourceLocation != null) {
                    Vec3 viewVector = ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal().m_20252_(1.0F);
                    viewVector = viewVector.subtract(0.0, viewVector.y, 0.0).normalize();
                    Vec3 toSourceLocation = sourceLocation.subtract(((ServerPlayerPatch) event.getPlayerPatch()).getOriginal().m_20182_()).normalize();
                    if (toSourceLocation.dot(viewVector) > 0.0) {
                        isFront = true;
                    }
                }
                if (isFront) {
                    float impact = 0.5F;
                    float knockback = 0.25F;
                    if (event.getDamageSource() instanceof EpicFightDamageSource epicfightDamageSource) {
                        if (epicfightDamageSource.is(EpicFightDamageType.GUARD_PUNCTURE)) {
                            return;
                        }
                        impact = epicfightDamageSource.getImpact();
                        knockback += Math.min(impact * 0.1F, 1.0F);
                    }
                    this.guard(container, itemCapability, event, knockback, impact, false);
                }
            }
        }, 1);
    }

    public void guard(SkillContainer container, CapabilityItem itemCapability, HurtEvent.Pre event, float knockback, float impact, boolean advanced) {
        DamageSource damageSource = (DamageSource) event.getDamageSource();
        if (this.isBlockableSource(damageSource, advanced)) {
            ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.CLASH.get(), -0.05F, 0.1F);
            ServerPlayer serveerPlayer = ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal();
            EpicFightParticles.HIT_BLUNT.get().spawnParticleWithArgument(serveerPlayer.serverLevel(), HitParticleType.FRONT_OF_EYES, HitParticleType.ZERO, serveerPlayer, damageSource.getDirectEntity());
            if (damageSource.getDirectEntity() instanceof LivingEntity livingEntity) {
                knockback += (float) EnchantmentHelper.getKnockbackBonus(livingEntity) * 0.1F;
            }
            float penalty = container.getDataManager().getDataValue(SkillDataKeys.PENALTY.get()) + this.getPenalizer(itemCapability);
            float consumeAmount = penalty * impact;
            ((ServerPlayerPatch) event.getPlayerPatch()).knockBackEntity(damageSource.getDirectEntity().position(), knockback);
            ((ServerPlayerPatch) event.getPlayerPatch()).consumeStaminaAlways(consumeAmount);
            container.getDataManager().setDataSync(SkillDataKeys.PENALTY.get(), penalty, ((ServerPlayerPatch) event.getPlayerPatch()).getOriginal());
            GuardSkill.BlockType blockType = ((ServerPlayerPatch) event.getPlayerPatch()).hasStamina(0.0F) ? GuardSkill.BlockType.GUARD : GuardSkill.BlockType.GUARD_BREAK;
            StaticAnimation animation = this.getGuardMotion(event.getPlayerPatch(), itemCapability, blockType);
            if (animation != null) {
                ((ServerPlayerPatch) event.getPlayerPatch()).playAnimationSynchronized(animation, 0.0F);
            }
            if (blockType == GuardSkill.BlockType.GUARD_BREAK) {
                ((ServerPlayerPatch) event.getPlayerPatch()).playSound(EpicFightSounds.NEUTRALIZE_MOBS.get(), 3.0F, 0.0F, 0.1F);
            }
            this.dealEvent(event.getPlayerPatch(), event, advanced);
        }
    }

    public void dealEvent(PlayerPatch<?> playerpatch, HurtEvent.Pre event, boolean advanced) {
        event.setCanceled(true);
        event.setResult(AttackResult.ResultType.BLOCKED);
        LivingEntityPatch<?> attackerpatch = EpicFightCapabilities.getEntityPatch(((DamageSource) event.getDamageSource()).getEntity(), LivingEntityPatch.class);
        if (attackerpatch != null) {
            attackerpatch.setLastAttackEntity(playerpatch.getOriginal());
        }
        Entity directEntity = ((DamageSource) event.getDamageSource()).getDirectEntity();
        LivingEntityPatch<?> entitypatch = EpicFightCapabilities.getEntityPatch(directEntity, LivingEntityPatch.class);
        if (entitypatch != null) {
            entitypatch.onAttackBlocked((DamageSource) event.getDamageSource(), playerpatch);
        }
    }

    protected float getPenalizer(CapabilityItem itemCapability) {
        return this.penalizer;
    }

    protected Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> getGuradMotionMap(GuardSkill.BlockType blockType) {
        switch(blockType) {
            case GUARD_BREAK:
                return this.guardBreakMotions;
            case GUARD:
                return this.guardMotions;
            case ADVANCED_GUARD:
                return this.advancedGuardMotions;
            default:
                throw new IllegalArgumentException("unsupported block type " + blockType);
        }
    }

    protected boolean isHoldingWeaponAvailable(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, GuardSkill.BlockType blockType) {
        StaticAnimation anim = itemCapability.getGuardMotion(this, blockType, playerpatch);
        if (anim != null) {
            return true;
        } else {
            Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions = this.getGuradMotionMap(blockType);
            if (!guardMotions.containsKey(itemCapability.getWeaponCategory())) {
                return false;
            } else {
                Object motion = ((BiFunction) guardMotions.get(itemCapability.getWeaponCategory())).apply(itemCapability, playerpatch);
                return motion != null;
            }
        }
    }

    @Nullable
    protected StaticAnimation getGuardMotion(PlayerPatch<?> playerpatch, CapabilityItem itemCapability, GuardSkill.BlockType blockType) {
        StaticAnimation animation = itemCapability.getGuardMotion(this, blockType, playerpatch);
        return animation != null ? animation : (StaticAnimation) ((BiFunction) this.getGuradMotionMap(blockType).getOrDefault(itemCapability.getWeaponCategory(), (BiFunction) (a, b) -> null)).apply(itemCapability, playerpatch);
    }

    @Override
    public void updateContainer(SkillContainer container) {
        super.updateContainer(container);
        if (!container.getExecuter().isLogicalClient() && !container.getExecuter().getOriginal().m_6117_()) {
            float penalty = container.getDataManager().getDataValue(SkillDataKeys.PENALTY.get());
            if (penalty > 0.0F) {
                int hitTick = container.getDataManager().getDataValue(SkillDataKeys.PENALTY_RESTORE_COUNTER.get());
                if (container.getExecuter().getOriginal().f_19797_ - hitTick > 40) {
                    container.getDataManager().setDataSync(SkillDataKeys.PENALTY.get(), 0.0F, (ServerPlayer) container.getExecuter().getOriginal());
                }
            }
        } else {
            container.getExecuter().resetActionTick();
        }
    }

    @Override
    public void onRemoved(SkillContainer container) {
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, 1);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.CLIENT_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_USE_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SERVER_ITEM_STOP_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.DEALT_DAMAGE_EVENT_POST, EVENT_UUID);
    }

    @Override
    public boolean isExecutableState(PlayerPatch<?> executer) {
        return !executer.isUnstable() && !executer.getEntityState().hurt() && executer.getEntityState().canUseSkill() && executer.isBattleMode();
    }

    protected boolean isBlockableSource(DamageSource damageSource, boolean advanced) {
        return !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.is(EpicFightDamageType.PARTIAL_DAMAGE) && !damageSource.is(DamageTypeTags.BYPASSES_ARMOR) && !damageSource.is(DamageTypeTags.IS_PROJECTILE) && !damageSource.is(DamageTypeTags.IS_EXPLOSION) && !damageSource.is(DamageTypes.MAGIC) && !damageSource.is(DamageTypeTags.IS_FIRE);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Object> getTooltipArgsOfScreen(List<Object> list) {
        list.clear();
        StringBuilder sb = new StringBuilder();
        Iterator<WeaponCategory> iter = this.guardMotions.keySet().iterator();
        while (iter.hasNext()) {
            sb.append(WeaponCategory.ENUM_MANAGER.toTranslated((ExtendableEnum) iter.next()));
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        list.add(sb.toString());
        return list;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean shouldDraw(SkillContainer container) {
        return container.getDataManager().getDataValue(SkillDataKeys.PENALTY.get()) > 0.0F;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawOnGui(BattleModeGui gui, SkillContainer container, GuiGraphics guiGraphics, float x, float y) {
        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        poseStack.translate(0.0F, (float) gui.getSlidingProgression(), 0.0F);
        guiGraphics.blit(EpicFightSkills.GUARD.getSkillTexture(), (int) x, (int) y, 24, 24, 0.0F, 0.0F, 1, 1, 1, 1);
        guiGraphics.drawString(gui.font, String.format("x%.1f", container.getDataManager().getDataValue(SkillDataKeys.PENALTY.get())), x, y + 6.0F, 16777215, true);
        poseStack.popPose();
    }

    protected boolean isAdvancedGuard() {
        return false;
    }

    public static enum BlockType {

        GUARD_BREAK, GUARD, ADVANCED_GUARD
    }

    public static class Builder extends Skill.Builder<GuardSkill> {

        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardMotions = Maps.newHashMap();

        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> advancedGuardMotions = Maps.newHashMap();

        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?>> guardBreakMotions = Maps.newHashMap();

        public GuardSkill.Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public GuardSkill.Builder setActivateType(Skill.ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public GuardSkill.Builder setResource(Skill.Resource resource) {
            this.resource = resource;
            return this;
        }

        public GuardSkill.Builder setCreativeTab(CreativeModeTab tab) {
            this.tab = tab;
            return this;
        }

        public GuardSkill.Builder addGuardMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation> function) {
            this.guardMotions.put(weaponCategory, function);
            return this;
        }

        public GuardSkill.Builder addAdvancedGuardMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, ?> function) {
            this.advancedGuardMotions.put(weaponCategory, function);
            return this;
        }

        public GuardSkill.Builder addGuardBreakMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation> function) {
            this.guardBreakMotions.put(weaponCategory, function);
            return this;
        }
    }
}