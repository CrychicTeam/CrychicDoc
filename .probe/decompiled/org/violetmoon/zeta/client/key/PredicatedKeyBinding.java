package org.violetmoon.zeta.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.Predicate;
import net.minecraft.client.KeyMapping;

public class PredicatedKeyBinding extends KeyMapping {

    private final Predicate<InputConstants.Key> allowed;

    public PredicatedKeyBinding(String description, InputConstants.Type type, int keyCode, String category, Predicate<InputConstants.Key> allowed) {
        super(description, type, keyCode, category);
        this.allowed = allowed;
    }

    @Override
    public void setKey(InputConstants.Key key) {
        if (this.allowed.test(key)) {
            super.setKey(key);
        }
    }
}