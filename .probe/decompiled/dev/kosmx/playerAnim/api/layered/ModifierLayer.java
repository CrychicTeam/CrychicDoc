package dev.kosmx.playerAnim.api.layered;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModifierLayer<T extends IAnimation> implements IAnimation {

    private final List<AbstractModifier> modifiers = new ArrayList();

    @Nullable
    T animation;

    public ModifierLayer(@Nullable T animation, AbstractModifier... modifiers) {
        this.animation = animation;
        Collections.addAll(this.modifiers, modifiers);
    }

    public ModifierLayer() {
        this(null);
    }

    @Override
    public void tick() {
        for (int i = 0; i < this.modifiers.size(); i++) {
            if (((AbstractModifier) this.modifiers.get(i)).canRemove()) {
                this.removeModifier(i--);
            }
        }
        if (this.modifiers.size() > 0) {
            ((AbstractModifier) this.modifiers.get(0)).tick();
        } else if (this.animation != null) {
            this.animation.tick();
        }
    }

    public void addModifier(@NotNull AbstractModifier modifier, int idx) {
        modifier.setHost(this);
        this.modifiers.add(idx, modifier);
        this.linkModifiers();
    }

    public void addModifierBefore(@NotNull AbstractModifier modifier) {
        this.addModifier(modifier, 0);
    }

    public void addModifierLast(@NotNull AbstractModifier modifier) {
        this.addModifier(modifier, this.modifiers.size());
    }

    public void removeModifier(int idx) {
        this.modifiers.remove(idx);
        this.linkModifiers();
    }

    public void setAnimation(@Nullable T animation) {
        this.animation = animation;
        this.linkModifiers();
    }

    public void replaceAnimationWithFade(@NotNull AbstractFadeModifier fadeModifier, @Nullable T newAnimation) {
        this.replaceAnimationWithFade(fadeModifier, newAnimation, false);
    }

    public void replaceAnimationWithFade(@NotNull AbstractFadeModifier fadeModifier, @Nullable T newAnimation, boolean fadeFromNothing) {
        if (fadeFromNothing || this.getAnimation() != null && this.getAnimation().isActive()) {
            fadeModifier.setBeginAnimation(this.getAnimation());
            this.addModifierLast(fadeModifier);
        }
        this.setAnimation(newAnimation);
    }

    public int size() {
        return this.modifiers.size();
    }

    protected void linkModifiers() {
        Iterator<AbstractModifier> modifierIterator = this.modifiers.iterator();
        if (modifierIterator.hasNext()) {
            AbstractModifier tmp = (AbstractModifier) modifierIterator.next();
            while (modifierIterator.hasNext()) {
                AbstractModifier tmp2 = (AbstractModifier) modifierIterator.next();
                tmp.setAnim(tmp2);
                tmp = tmp2;
            }
            tmp.setAnim(this.animation);
        }
    }

    @Override
    public boolean isActive() {
        if (this.modifiers.size() > 0) {
            return ((AbstractModifier) this.modifiers.get(0)).isActive();
        } else {
            return this.animation != null ? this.animation.isActive() : false;
        }
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        if (this.modifiers.size() > 0) {
            return ((AbstractModifier) this.modifiers.get(0)).get3DTransform(modelName, type, tickDelta, value0);
        } else {
            return this.animation != null ? this.animation.get3DTransform(modelName, type, tickDelta, value0) : value0;
        }
    }

    @Override
    public void setupAnim(float tickDelta) {
        if (this.modifiers.size() > 0) {
            ((AbstractModifier) this.modifiers.get(0)).setupAnim(tickDelta);
        } else if (this.animation != null) {
            this.animation.setupAnim(tickDelta);
        }
    }

    @NotNull
    @Override
    public FirstPersonMode getFirstPersonMode(float tickDelta) {
        if (this.modifiers.size() > 0) {
            return ((AbstractModifier) this.modifiers.get(0)).getFirstPersonMode(tickDelta);
        } else {
            return this.animation != null ? this.animation.getFirstPersonMode(tickDelta) : IAnimation.super.getFirstPersonMode(tickDelta);
        }
    }

    @NotNull
    @Override
    public FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        if (this.modifiers.size() > 0) {
            return ((AbstractModifier) this.modifiers.get(0)).getFirstPersonConfiguration(tickDelta);
        } else {
            return this.animation != null ? this.animation.getFirstPersonConfiguration(tickDelta) : IAnimation.super.getFirstPersonConfiguration(tickDelta);
        }
    }

    @Nullable
    public T getAnimation() {
        return this.animation;
    }
}