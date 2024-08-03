package snownee.jade.api.config;

import java.util.Collection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext;
import org.jetbrains.annotations.ApiStatus.NonExtendable;
import snownee.jade.Internals;
import snownee.jade.api.SimpleStringRepresentable;
import snownee.jade.api.theme.Theme;

@NonExtendable
public interface IWailaConfig {

    static IWailaConfig get() {
        return Internals.getWailaConfig();
    }

    IWailaConfig.IConfigGeneral getGeneral();

    IWailaConfig.IConfigOverlay getOverlay();

    IWailaConfig.IConfigFormatting getFormatting();

    IPluginConfig getPlugin();

    public static enum BossBarOverlapMode implements SimpleStringRepresentable {

        NO_OPERATION, HIDE_BOSS_BAR, HIDE_TOOLTIP, PUSH_DOWN
    }

    public static enum DisplayMode implements SimpleStringRepresentable {

        HOLD_KEY, TOGGLE, LITE
    }

    public static enum FluidMode implements SimpleStringRepresentable {

        NONE(ClipContext.Fluid.NONE), ANY(ClipContext.Fluid.ANY), SOURCE_ONLY(ClipContext.Fluid.SOURCE_ONLY);

        public final ClipContext.Fluid ctx;

        private FluidMode(ClipContext.Fluid ctx) {
            this.ctx = ctx;
        }
    }

    @NonExtendable
    public interface IConfigFormatting {

        void setModName(String var1);

        String getModName();

        Component registryName(String var1);

        @Deprecated
        Component title(Object var1);
    }

    @NonExtendable
    public interface IConfigGeneral {

        void setDisplayTooltip(boolean var1);

        boolean getDisplayEntities();

        boolean getDisplayBosses();

        boolean getDisplayBlocks();

        void setDisplayBlocks(boolean var1);

        void setDisplayEntities(boolean var1);

        void setDisplayBosses(boolean var1);

        void setDisplayMode(IWailaConfig.DisplayMode var1);

        void setHideFromDebug(boolean var1);

        void setHideFromTabList(boolean var1);

        void toggleTTS();

        void setTTSMode(IWailaConfig.TTSMode var1);

        void setDisplayFluids(boolean var1);

        void setDisplayFluids(IWailaConfig.FluidMode var1);

        void setItemModNameTooltip(boolean var1);

        boolean shouldDisplayTooltip();

        IWailaConfig.DisplayMode getDisplayMode();

        boolean shouldHideFromDebug();

        boolean shouldHideFromTabList();

        boolean shouldEnableTextToSpeech();

        IWailaConfig.TTSMode getTTSMode();

        boolean shouldDisplayFluids();

        IWailaConfig.FluidMode getDisplayFluids();

        boolean showItemModNameTooltip();

        float getReachDistance();

        void setReachDistance(float var1);

        IWailaConfig.BossBarOverlapMode getBossBarOverlapMode();

        void setBossBarOverlapMode(IWailaConfig.BossBarOverlapMode var1);

        void setDebug(boolean var1);

        boolean getBuiltinCamouflage();

        void setBuiltinCamouflage(boolean var1);

        boolean isDebug();
    }

    @NonExtendable
    public interface IConfigOverlay {

        void setOverlayPosX(float var1);

        void setOverlayPosY(float var1);

        void setOverlayScale(float var1);

        void setAnchorX(float var1);

        void setAnchorY(float var1);

        float getOverlayPosX();

        float getOverlayPosY();

        float getOverlayScale();

        float getAnchorX();

        float getAnchorY();

        void setFlipMainHand(boolean var1);

        boolean getFlipMainHand();

        float tryFlip(float var1);

        void setSquare(boolean var1);

        boolean getSquare();

        float getAutoScaleThreshold();

        float getAlpha();

        Theme getTheme();

        Collection<Theme> getThemes();

        void setAlpha(float var1);

        void applyTheme(ResourceLocation var1);

        static int applyAlpha(int color, float alpha) {
            int prevAlphaChannel = color >> 24 & 0xFF;
            if (prevAlphaChannel > 0) {
                alpha *= (float) prevAlphaChannel / 256.0F;
            }
            int alphaChannel = (int) (255.0F * Mth.clamp(alpha, 0.0F, 1.0F));
            return color & 16777215 | alphaChannel << 24;
        }

        boolean shouldShowIcon();

        void setIconMode(IWailaConfig.IconMode var1);

        IWailaConfig.IconMode getIconMode();

        void setAnimation(boolean var1);

        boolean getAnimation();

        void setDisappearingDelay(float var1);

        float getDisappearingDelay();
    }

    public static enum IconMode implements SimpleStringRepresentable {

        TOP, CENTERED, HIDE
    }

    public static enum TTSMode implements SimpleStringRepresentable {

        TOGGLE, PRESS
    }
}