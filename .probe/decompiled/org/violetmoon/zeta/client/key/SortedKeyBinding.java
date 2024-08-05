package org.violetmoon.zeta.client.key;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class SortedKeyBinding extends KeyMapping {

    private final int priority;

    public SortedKeyBinding(String description, InputConstants.Type type, int keyCode, String category, int priority) {
        super(description, type, keyCode, category);
        this.priority = priority;
    }

    @Override
    public int compareTo(KeyMapping keyBinding) {
        return this.m_90858_().equals(keyBinding.getCategory()) && keyBinding instanceof SortedKeyBinding sorted ? Integer.compare(this.priority, sorted.priority) : super.compareTo(keyBinding);
    }
}