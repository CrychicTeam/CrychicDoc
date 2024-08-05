package io.redspace.ironsspellbooks.player;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "irons_spellbooks", bus = Bus.MOD)
public final class KeyMappings {

    public static final String KEY_BIND_GENERAL_CATEGORY = "key.irons_spellbooks.group_1";

    public static final String KEY_BIND_QUICK_CAST_CATEGORY = "key.irons_spellbooks.group_2";

    public static final KeyMapping SPELL_WHEEL_KEYMAP = new KeyMapping(getResourceName("spell_wheel"), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 82, "key.irons_spellbooks.group_1");

    public static final KeyMapping SPELLBOOK_CAST_ACTIVE_KEYMAP = new KeyMapping(getResourceName("spellbook_cast"), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 86, "key.irons_spellbooks.group_1");

    public static final KeyMapping SPELLBAR_SCROLL_MODIFIER_KEYMAP = new KeyMapping(getResourceName("spell_bar_modifier"), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 342, "key.irons_spellbooks.group_1");

    public static final List<KeyMapping> QUICK_CAST_MAPPINGS = createQuickCastKeybinds();

    private static String getResourceName(String name) {
        return String.format("key.irons_spellbooks.%s", name);
    }

    @SubscribeEvent
    public static void onRegisterKeybinds(RegisterKeyMappingsEvent event) {
        event.register(SPELL_WHEEL_KEYMAP);
        event.register(SPELLBOOK_CAST_ACTIVE_KEYMAP);
        event.register(SPELLBAR_SCROLL_MODIFIER_KEYMAP);
        QUICK_CAST_MAPPINGS.forEach(event::register);
    }

    private static List<KeyMapping> createQuickCastKeybinds() {
        ArrayList<KeyMapping> qcm = new ArrayList();
        for (int i = 1; i <= 15; i++) {
            qcm.add(new KeyMapping(getResourceName(String.format("spell_quick_cast_%d", i)), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, InputConstants.UNKNOWN.getValue(), "key.irons_spellbooks.group_2"));
        }
        return qcm;
    }
}