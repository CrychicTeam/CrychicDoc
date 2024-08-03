package club.iananderson.seasonhud.platform;

import club.iananderson.seasonhud.client.SeasonHUDClient;
import club.iananderson.seasonhud.client.minimaps.MapAtlases;
import club.iananderson.seasonhud.impl.minimaps.CurrentMinimap;
import club.iananderson.seasonhud.platform.services.IMinimapHelper;
import dev.ftb.mods.ftbchunks.client.FTBChunksClientConfig;
import java.util.Objects;
import journeymap.client.ui.UIManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import sereneseasons.config.ServerConfig;
import xaero.common.HudMod;

public class ForgeMinimapHelper implements IMinimapHelper {

    @Override
    public boolean hideHudInCurrentDimension() {
        ResourceKey<Level> currentDim = ((ClientLevel) Objects.requireNonNull(SeasonHUDClient.mc.level)).m_46472_();
        return !ServerConfig.isDimensionWhitelisted(currentDim);
    }

    @Override
    public boolean currentMinimapHidden() {
        if (CurrentMinimap.minimapLoaded("journeymap")) {
            return !UIManager.INSTANCE.getMiniMap().getCurrentMinimapProperties().enabled.get();
        } else if (CurrentMinimap.minimapLoaded("ftbchunks") && !CurrentMinimap.minimapLoaded("journeymap") && !CurrentMinimap.minimapLoaded("xaerominimap") && !CurrentMinimap.minimapLoaded("xaerominimapfair") && !CurrentMinimap.minimapLoaded("map_atlases")) {
            return !(Boolean) FTBChunksClientConfig.MINIMAP_ENABLED.get();
        } else if (CurrentMinimap.minimapLoaded("xaerominimap") || CurrentMinimap.minimapLoaded("xaerominimapfair")) {
            return !HudMod.INSTANCE.getSettings().getMinimap();
        } else {
            return CurrentMinimap.minimapLoaded("map_atlases") ? !MapAtlases.shouldDraw(Minecraft.getInstance()) : false;
        }
    }
}