package org.violetmoon.zeta.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.function.Predicate;

public class SortedPredicatedKeyBinding extends SortedKeyBinding {

    private final Predicate<InputConstants.Key> allowed;

    public SortedPredicatedKeyBinding(String description, InputConstants.Type type, int keyCode, String category, int priority, Predicate<InputConstants.Key> allowed) {
        super(description, type, keyCode, category, priority);
        this.allowed = allowed;
    }

    @Override
    public void setKey(InputConstants.Key key) {
        if (this.allowed.test(key)) {
            super.m_90848_(key);
        }
    }
}