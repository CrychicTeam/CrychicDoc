package yesman.epicfight.world.capabilities.entitypatch;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.data.reloader.MobPatchReloadListener;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.world.damagesource.StunType;
import yesman.epicfight.world.entity.ai.attribute.EpicFightAttributes;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;

public class CustomMobPatch<T extends PathfinderMob> extends MobPatch<T> {

    private final MobPatchReloadListener.CustomMobPatchProvider provider;

    public CustomMobPatch(Faction faction, MobPatchReloadListener.CustomMobPatchProvider provider) {
        super(faction);
        this.provider = provider;
    }

    @Override
    protected void initAI() {
        super.initAI();
        this.original.f_21345_.addGoal(0, new AnimatedAttackGoal<>(this, (CombatBehaviors<CustomMobPatch<T>>) ((CombatBehaviors.Builder<CustomMobPatch<T>>) this.provider.getCombatBehaviorsBuilder()).build(this)));
        this.original.f_21345_.addGoal(1, new TargetChasingGoal(this, this.getOriginal(), this.provider.getChasingSpeed(), true));
    }

    @Override
    protected void initAttributes() {
        this.original.m_21051_(EpicFightAttributes.WEIGHT.get()).setBaseValue(this.original.m_21051_(Attributes.MAX_HEALTH).getBaseValue() * 2.0);
        this.original.m_21051_(EpicFightAttributes.MAX_STRIKES.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.MAX_STRIKES.get()));
        this.original.m_21051_(EpicFightAttributes.ARMOR_NEGATION.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.ARMOR_NEGATION.get()));
        this.original.m_21051_(EpicFightAttributes.IMPACT.get()).setBaseValue((Double) this.provider.getAttributeValues().get(EpicFightAttributes.IMPACT.get()));
        if (this.provider.getAttributeValues().containsKey(Attributes.ATTACK_DAMAGE)) {
            this.original.m_21051_(Attributes.ATTACK_DAMAGE).setBaseValue((Double) this.provider.getAttributeValues().get(Attributes.ATTACK_DAMAGE));
        }
    }

    @Override
    public void initAnimator(ClientAnimator clientAnimator) {
        for (Pair<LivingMotion, StaticAnimation> pair : this.provider.getDefaultAnimations()) {
            clientAnimator.addLivingAnimation((LivingMotion) pair.getFirst(), (StaticAnimation) pair.getSecond());
        }
        clientAnimator.setCurrentMotionsAsDefault();
    }

    @Override
    public void updateMotion(boolean considerInaction) {
        super.commonAggressiveMobUpdateMotion(considerInaction);
    }

    @Override
    public StaticAnimation getHitAnimation(StunType stunType) {
        return (StaticAnimation) this.provider.getStunAnimations().get(stunType);
    }

    @Override
    public OpenMatrix4f getModelMatrix(float partialTicks) {
        float scale = this.provider.getScale();
        return super.getModelMatrix(partialTicks).scale(scale, scale, scale);
    }
}