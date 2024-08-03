package com.mna;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD, value = { Dist.CLIENT })
public class KeybindInit {

    public static final Lazy<KeyMapping> RadialMenuOpen = Lazy.of(() -> new KeyMapping("key.spellbookopen", InputConstants.Type.KEYSYM, 90, "key.categories.mna"));

    public static final Lazy<KeyMapping> SpellBookChord = Lazy.of(() -> new KeyMapping("key.spellbookchord", InputConstants.Type.KEYSYM, 86, "key.categories.mna"));

    public static final Lazy<KeyMapping> InventoryItemOpen = Lazy.of(() -> new KeyMapping("key.gui-key-modifier", InputConstants.Type.KEYSYM, 341, "key.categories.mna"));

    @SubscribeEvent
    public static void initKeybinds(RegisterKeyMappingsEvent event) {
        event.register((KeyMapping) RadialMenuOpen.get());
        event.register((KeyMapping) InventoryItemOpen.get());
        event.register((KeyMapping) SpellBookChord.get());
        ManaAndArtifice.LOGGER.info("M&A -> Keybindings Registered");
    }
}