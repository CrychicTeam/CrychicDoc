package dev.kosmx.playerAnim.api.layered.modifier;

import dev.kosmx.playerAnim.api.layered.AnimationContainer;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractModifier extends AnimationContainer<IAnimation> {

    @Nullable
    protected ModifierLayer host;

    public AbstractModifier() {
        super(null);
    }

    public boolean canRemove() {
        return false;
    }

    public void setHost(@Nullable ModifierLayer host) {
        this.host = host;
    }
}