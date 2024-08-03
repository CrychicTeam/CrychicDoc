package com.github.einjerjar.mc.keymap.keys.sources.keymap;

import com.github.einjerjar.mc.keymap.keys.wrappers.keys.KeyHolder;
import com.github.einjerjar.mc.keymap.keys.wrappers.keys.VanillaKeymap;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public final class VanillaKeymapSource implements KeymapSource {

    @Override
    public List<KeyHolder> getKeyHolders() {
        List<KeyHolder> keymaps = new ArrayList();
        for (KeyMapping km : Minecraft.getInstance().options.keyMappings) {
            KeyHolder kh = new VanillaKeymap(km);
            keymaps.add(kh);
        }
        return keymaps;
    }

    @Override
    public boolean canUseSource() {
        return true;
    }
}