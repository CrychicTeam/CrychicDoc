package journeymap.client.event.handlers.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import journeymap.client.event.handlers.KeyEventHandler;
import net.minecraft.client.KeyMapping;

public class UpdateAwareKeyBinding extends KeyMapping {

    KeyModifier keyModifier;

    KeyConflictContext conflictContext;

    InputConstants.Key key;

    final KeyEventHandler handler;

    public UpdateAwareKeyBinding(String description, KeyConflictContext keyConflictContext, KeyModifier keyModifier, InputConstants.Type inputType, int keyCode, String category, KeyEventHandler handler) {
        super(description, keyConflictContext.getForge(), keyModifier.getForge(), inputType, keyCode, category);
        this.keyModifier = keyModifier;
        this.conflictContext = keyConflictContext;
        this.handler = handler;
        this.key = InputConstants.Type.KEYSYM.getOrCreate(keyCode);
    }

    @Override
    public void setKey(InputConstants.Key key) {
        super.setKey(key);
        if (this.handler != null) {
            this.handler.sortActionsNeeded = true;
        }
    }

    public KeyModifier getModifier() {
        return KeyModifier.fromForge(super.getKeyModifier());
    }

    public KeyConflictContext getConflictContext() {
        return this.conflictContext;
    }

    public InputConstants.Key getKey() {
        return super.getKey();
    }

    public boolean isActiveAndMatches(InputConstants.Key keyCode) {
        return keyCode != InputConstants.UNKNOWN && keyCode.equals(this.getKey()) && this.getConflictContext().isActive() && this.getModifier().isActive(this.getConflictContext());
    }

    public void setKeyModifierAndCode(net.minecraftforge.client.settings.KeyModifier keyModifier, InputConstants.Key keyCode) {
        super.setKeyModifierAndCode(keyModifier, keyCode);
        this.handler.sortActionsNeeded = true;
    }
}