package io.redspace.ironsspellbooks.registries;

import io.redspace.ironsspellbooks.gui.overlays.ActiveSpellOverlay;
import io.redspace.ironsspellbooks.gui.overlays.CastBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.ManaBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.RecastOverlay;
import io.redspace.ironsspellbooks.gui.overlays.ScreenEffectsOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellBarOverlay;
import io.redspace.ironsspellbooks.gui.overlays.SpellWheelOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber({ Dist.CLIENT })
public class OverlayRegistry {

    @SubscribeEvent
    public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
        event.registerBelow(VanillaGuiOverlay.HOTBAR.id(), "cast_bar", CastBarOverlay.instance);
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "mana_overlay", ManaBarOverlay.instance);
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "spell_bar", SpellBarOverlay.instance);
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "scroll_overlay", ActiveSpellOverlay.instance);
        event.registerAbove(VanillaGuiOverlay.EXPERIENCE_BAR.id(), "recast_bar", RecastOverlay.instance);
        event.registerAbove(VanillaGuiOverlay.PLAYER_LIST.id(), "spell_wheel", SpellWheelOverlay.instance);
        event.registerAbove(VanillaGuiOverlay.PLAYER_LIST.id(), "screen_effects", ScreenEffectsOverlay.instance);
    }
}