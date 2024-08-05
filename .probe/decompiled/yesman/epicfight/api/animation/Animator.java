package yesman.epicfight.api.animation;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import yesman.epicfight.api.animation.types.AttackAnimation;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.EntityState;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.utils.TypeFlexibleHashMap;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.main.EpicFightMod;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

public abstract class Animator {

    protected final Map<LivingMotion, StaticAnimation> livingAnimations = Maps.newHashMap();

    protected final TypeFlexibleHashMap<TypeFlexibleHashMap.TypeKey<?>> animationVariables = new TypeFlexibleHashMap<>(false);

    protected LivingEntityPatch<?> entitypatch;

    public Animator() {
        this.animationVariables.put(AttackAnimation.HIT_ENTITIES, Lists.newArrayList());
        this.animationVariables.put(AttackAnimation.HURT_ENTITIES, Lists.newArrayList());
    }

    public abstract void playAnimation(StaticAnimation var1, float var2);

    public abstract void playAnimationInstantly(StaticAnimation var1);

    public abstract void tick();

    public abstract void reserveAnimation(StaticAnimation var1);

    public abstract EntityState getEntityState();

    public abstract AnimationPlayer getPlayerFor(@Nullable DynamicAnimation var1);

    public abstract <T> Pair<AnimationPlayer, T> findFor(Class<T> var1);

    public abstract void init();

    public abstract void poseTick();

    public final void playAnimation(int namespaceId, int id, float convertTimeModifier) {
        this.playAnimation(EpicFightMod.getInstance().animationManager.findAnimationById(namespaceId, id), convertTimeModifier);
    }

    public final void playAnimationInstantly(int namespaceId, int id) {
        this.playAnimationInstantly(EpicFightMod.getInstance().animationManager.findAnimationById(namespaceId, id));
    }

    public boolean isReverse() {
        return false;
    }

    public void playDeathAnimation() {
        this.playAnimation(Animations.BIPED_DEATH, 0.0F);
    }

    public void addLivingAnimation(LivingMotion livingMotion, StaticAnimation animation) {
        this.livingAnimations.put(livingMotion, animation);
    }

    public StaticAnimation getLivingAnimation(LivingMotion livingMotion, StaticAnimation defaultGetter) {
        return (StaticAnimation) this.livingAnimations.getOrDefault(livingMotion, defaultGetter);
    }

    public Set<Entry<LivingMotion, StaticAnimation>> getLivingAnimationEntrySet() {
        return this.livingAnimations.entrySet();
    }

    public void removeAnimationVariables(TypeFlexibleHashMap.TypeKey<?> typeKey) {
        this.animationVariables.remove(typeKey);
    }

    public <T> void putAnimationVariables(TypeFlexibleHashMap.TypeKey<T> typeKey, T value) {
        if (this.animationVariables.containsKey(typeKey)) {
            this.animationVariables.replace(typeKey, value);
        } else {
            this.animationVariables.put(typeKey, value);
        }
    }

    public <T> T getAnimationVariables(TypeFlexibleHashMap.TypeKey<T> key) {
        return this.animationVariables.get(key);
    }

    public void resetLivingAnimations() {
        this.livingAnimations.clear();
    }
}