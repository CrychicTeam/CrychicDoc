package yesman.epicfight.skill.identity;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.ExtendableEnum;
import yesman.epicfight.api.utils.LevelUtil;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillCategories;
import yesman.epicfight.skill.SkillCategory;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataKeys;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.entitypatch.player.ServerPlayerPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

public class MeteorSlamSkill extends Skill {

    private static final UUID EVENT_UUID = UUID.fromString("03181ad0-e750-11ed-a05b-0242ac120003");

    protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> slamMotions;

    private final double minDistance = 6.0;

    public static float getFallDistance(SkillContainer skillContainer) {
        return skillContainer.getDataManager().getDataValue(SkillDataKeys.FALL_DISTANCE.get());
    }

    public static MeteorSlamSkill.Builder createMeteorSlamBuilder() {
        return new MeteorSlamSkill.Builder().setCategory(SkillCategories.IDENTITY).setResource(Skill.Resource.NONE).addSlamMotion(CapabilityItem.WeaponCategories.SPEAR, (item, player) -> Animations.METEOR_SLAM).addSlamMotion(CapabilityItem.WeaponCategories.GREATSWORD, (item, player) -> Animations.METEOR_SLAM).addSlamMotion(CapabilityItem.WeaponCategories.TACHI, (item, player) -> Animations.METEOR_SLAM).addSlamMotion(CapabilityItem.WeaponCategories.LONGSWORD, (item, player) -> Animations.METEOR_SLAM);
    }

    public MeteorSlamSkill(MeteorSlamSkill.Builder builder) {
        super(builder);
        this.slamMotions = builder.slamMotions;
    }

    @Override
    public void onInitiate(SkillContainer container) {
        PlayerEventListener listener = container.getExecuter().getEventListener();
        listener.addEventListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID, event -> {
            if (container.getExecuter() instanceof ServerPlayerPatch serverPlayerPatch) {
                Skill skill = event.getSkillContainer().getSkill();
                if (skill.getCategory() != SkillCategories.BASIC_ATTACK && skill.getCategory() != SkillCategories.AIR_ATTACK) {
                    return;
                }
                if (container.getExecuter().getOriginal().m_20096_() || container.getExecuter().getOriginal().m_146909_() < 40.0F) {
                    return;
                }
                CapabilityItem holdingItem = container.getExecuter().getHoldingItemCapability(InteractionHand.MAIN_HAND);
                if (!this.slamMotions.containsKey(holdingItem.getWeaponCategory())) {
                    return;
                }
                StaticAnimation slamAnimation = (StaticAnimation) ((BiFunction) this.slamMotions.get(holdingItem.getWeaponCategory())).apply(holdingItem, container.getExecuter());
                if (slamAnimation == null) {
                    return;
                }
                Vec3 vec3 = container.getExecuter().getOriginal().m_20299_(1.0F);
                Vec3 vec31 = container.getExecuter().getOriginal().m_20252_(1.0F);
                Vec3 vec32 = vec3.add(vec31.x * 50.0, vec31.y * 50.0, vec31.z * 50.0);
                HitResult hitResult = container.getExecuter().getOriginal().m_9236_().m_45547_(new ClipContext(vec3, vec32, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, container.getExecuter().getOriginal()));
                if (hitResult.getType() != HitResult.Type.MISS) {
                    Vec3 to = hitResult.getLocation();
                    Vec3 from = container.getExecuter().getOriginal().m_20182_();
                    double distance = to.distanceTo(from);
                    if (distance > 6.0) {
                        container.getExecuter().playAnimationSynchronized(slamAnimation, 0.0F);
                        container.getDataManager().setDataSync(SkillDataKeys.FALL_DISTANCE.get(), (float) distance, serverPlayerPatch.getOriginal());
                        container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), true);
                        event.setCanceled(true);
                    }
                }
            }
        });
        listener.addEventListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID, event -> {
            if (((DamageSource) event.getDamageSource()).is(DamageTypeTags.IS_FALL) && container.getDataManager().getDataValue(SkillDataKeys.PROTECT_NEXT_FALL.get())) {
                float stamina = container.getExecuter().getStamina();
                float damage = event.getAmount();
                event.setAmount(damage - stamina);
                event.setCanceled(true);
                container.getExecuter().setStamina(stamina - damage);
                container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), false);
            }
        });
        listener.addEventListener(PlayerEventListener.EventType.FALL_EVENT, EVENT_UUID, event -> {
            if (LevelUtil.calculateLivingEntityFallDamage(event.getForgeEvent().getEntity(), event.getForgeEvent().getDamageMultiplier(), event.getForgeEvent().getDistance()) == 0) {
                container.getDataManager().setData(SkillDataKeys.PROTECT_NEXT_FALL.get(), false);
            }
        });
    }

    @Override
    public void onRemoved(SkillContainer container) {
        super.onRemoved(container);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.FALL_EVENT, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.HURT_EVENT_PRE, EVENT_UUID);
        container.getExecuter().getEventListener().removeListener(PlayerEventListener.EventType.SKILL_EXECUTE_EVENT, EVENT_UUID);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Object> getTooltipArgsOfScreen(List<Object> list) {
        StringBuilder sb = new StringBuilder();
        Iterator<WeaponCategory> iter = this.slamMotions.keySet().iterator();
        while (iter.hasNext()) {
            sb.append(WeaponCategory.ENUM_MANAGER.toTranslated((ExtendableEnum) iter.next()));
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        list.add(sb.toString());
        return list;
    }

    public static class Builder extends Skill.Builder<MeteorSlamSkill> {

        protected final Map<WeaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation>> slamMotions = Maps.newHashMap();

        public MeteorSlamSkill.Builder addSlamMotion(WeaponCategory weaponCategory, BiFunction<CapabilityItem, PlayerPatch<?>, StaticAnimation> function) {
            this.slamMotions.put(weaponCategory, function);
            return this;
        }

        public MeteorSlamSkill.Builder setCategory(SkillCategory category) {
            this.category = category;
            return this;
        }

        public MeteorSlamSkill.Builder setActivateType(Skill.ActivateType activateType) {
            this.activateType = activateType;
            return this;
        }

        public MeteorSlamSkill.Builder setResource(Skill.Resource resource) {
            this.resource = resource;
            return this;
        }

        public MeteorSlamSkill.Builder setCreativeTab(CreativeModeTab tab) {
            this.tab = tab;
            return this;
        }
    }
}