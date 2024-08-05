package journeymap.client.event.handlers.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import journeymap.client.Constants;

public class KeyBindingAction {

    UpdateAwareKeyBinding keyBinding;

    Runnable action;

    public KeyBindingAction(UpdateAwareKeyBinding keyBinding, Runnable action) {
        this.keyBinding = keyBinding;
        this.action = action;
    }

    public boolean isActive(int key, boolean useContext, InputConstants.Type type) {
        return useContext ? this.keyBinding.isActiveAndMatches(type.getOrCreate(key)) : this.keyBinding.getKey().getValue() == key && this.keyBinding.getKeyModifier().isActive(null);
    }

    public Runnable getAction() {
        return this.action;
    }

    public UpdateAwareKeyBinding getKeyBinding() {
        return this.keyBinding;
    }

    public int order() {
        return this.keyBinding.getKeyModifier().ordinal();
    }

    public String toString() {
        return "KeyBindingAction{" + this.keyBinding.m_90863_().getString().toUpperCase() + " = " + Constants.getString(this.keyBinding.m_90860_()) + "}";
    }
}