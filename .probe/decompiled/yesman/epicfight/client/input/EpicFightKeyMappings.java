package yesman.epicfight.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(value = { Dist.CLIENT }, bus = Bus.MOD)
public class EpicFightKeyMappings {

    public static final KeyMapping WEAPON_INNATE_SKILL_TOOLTIP = new KeyMapping("key.epicfight.show_tooltip", 340, "key.epicfight.gui");

    public static final KeyMapping SWITCH_MODE = new KeyMapping("key.epicfight.switch_mode", 82, "key.epicfight.combat");

    public static final KeyMapping DODGE = new KeyMapping("key.epicfight.dodge", 342, "key.epicfight.combat");

    public static final KeyMapping GUARD = new KeyMapping("key.epicfight.guard", 342, "key.epicfight.combat");

    public static final KeyMapping ATTACK = new CombatKeyMapping("key.epicfight.attack", InputConstants.Type.MOUSE, 0, "key.epicfight.combat");

    public static final KeyMapping WEAPON_INNATE_SKILL = new CombatKeyMapping("key.epicfight.weapon_innate_skill", InputConstants.Type.MOUSE, 0, "key.epicfight.combat");

    public static final KeyMapping MOVER_SKILL = new CombatKeyMapping("key.epicfight.mover_skill", InputConstants.Type.KEYSYM, 32, "key.epicfight.combat");

    public static final KeyMapping SKILL_EDIT = new KeyMapping("key.epicfight.skill_gui", 75, "key.epicfight.gui");

    public static final KeyMapping LOCK_ON = new KeyMapping("key.epicfight.lock_on", 71, "key.epicfight.combat");

    public static final KeyMapping CONFIG = new KeyMapping("key.epicfight.config", -1, "key.epicfight.gui");

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(WEAPON_INNATE_SKILL_TOOLTIP);
        event.register(SWITCH_MODE);
        event.register(DODGE);
        event.register(ATTACK);
        event.register(WEAPON_INNATE_SKILL);
        event.register(MOVER_SKILL);
        event.register(SKILL_EDIT);
        event.register(LOCK_ON);
        event.register(CONFIG);
    }
}