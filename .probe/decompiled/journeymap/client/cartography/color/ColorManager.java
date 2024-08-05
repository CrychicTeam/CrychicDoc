package journeymap.client.cartography.color;

import com.google.common.base.Joiner;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.model.BlockMD;
import journeymap.client.task.multi.MapPlayerTask;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@ParametersAreNonnullByDefault
public enum ColorManager {

    INSTANCE;

    private Logger logger = Journeymap.getLogger();

    private volatile ColorPalette currentPalette;

    private String lastResourcePackNames;

    private String lastModNames;

    private double lastPaletteVersion;

    private HashMap<String, float[]> iconColorCache = new HashMap();

    public void reset() {
        this.lastResourcePackNames = null;
        this.lastModNames = null;
        this.lastPaletteVersion = 0.0;
        this.currentPalette = null;
        this.iconColorCache.clear();
    }

    public static String getResourcePackNames() {
        Collection<Pack> entries = Constants.getResourcePacks().getAvailablePacks();
        String packs;
        if (entries.isEmpty()) {
            packs = Constants.RESOURCE_PACKS_DEFAULT;
        } else {
            ArrayList<String> entryStrings = new ArrayList(entries.size());
            for (Pack entry : entries) {
                entryStrings.add(entry.getTitle().getString());
            }
            Collections.sort(entryStrings);
            packs = Joiner.on(", ").join(entryStrings);
        }
        return packs;
    }

    public void ensureCurrent(boolean forceReset) {
        if (GLFW.glfwGetCurrentContext() != Minecraft.getInstance().getWindow().getWindow()) {
            this.logger.error("ColorManager.ensureCurrent() must be called on main thread!");
        } else {
            String currentResourcePackNames = getResourcePackNames();
            String currentModNames = LoaderHooks.getModNames();
            double currentPaletteVersion = this.currentPalette == null ? 0.0 : this.currentPalette.getVersion();
            if (this.currentPalette != null && !forceReset) {
                if (!currentResourcePackNames.equals(this.lastResourcePackNames) && !this.iconColorCache.isEmpty()) {
                    this.logger.debug("Resource Pack(s) changed: " + currentResourcePackNames);
                    forceReset = true;
                }
                if (!currentModNames.equals(this.lastModNames)) {
                    this.logger.debug("Mod Pack(s) changed: " + currentModNames);
                    forceReset = true;
                }
                if (currentPaletteVersion != this.lastPaletteVersion) {
                    this.logger.debug("Color Palette version changed: " + currentPaletteVersion);
                    forceReset = true;
                }
            }
            if (forceReset || this.iconColorCache.isEmpty()) {
                this.logger.debug("Building color palette...");
                this.initBlockColors(forceReset);
            }
            this.lastModNames = currentModNames;
            this.lastResourcePackNames = currentResourcePackNames;
            this.lastPaletteVersion = this.currentPalette == null ? 0.0 : this.currentPalette.getVersion();
        }
    }

    public ColorPalette getCurrentPalette() {
        return this.currentPalette;
    }

    private void initBlockColors(boolean forceReset) {
        try {
            long start = System.currentTimeMillis();
            ColorPalette palette = ColorPalette.getActiveColorPalette();
            Collection<BlockMD> blockMDs;
            if (JourneymapClient.getInstance().isMapping()) {
                blockMDs = BlockMD.getAllValid();
            } else {
                blockMDs = BlockMD.getAllMinecraft();
            }
            if (forceReset || palette == null) {
                this.logger.debug("Color palette update required.");
                this.iconColorCache.clear();
                blockMDs.forEach(BlockMD::clearColor);
            }
            boolean standard = true;
            boolean permanent = false;
            if (palette != null) {
                standard = palette.isStandard();
                permanent = palette.isPermanent();
                if (permanent && forceReset) {
                    this.logger.debug("Applying permanent palette colors before updating");
                }
                if (permanent || !forceReset) {
                    try {
                        int count = palette.applyColors(blockMDs, true);
                        this.logger.debug(String.format("Loaded %d block colors from %s", count, palette.getOrigin()));
                    } catch (Exception var10) {
                        this.logger.warn(String.format("Could not load block colors from %s: %s", palette.getOrigin(), var10));
                    }
                }
            }
            if (forceReset || palette == null) {
                palette = ColorPalette.create(standard, permanent);
            }
            this.currentPalette = palette;
            for (BlockMD blockMD : blockMDs) {
                if (!blockMD.hasColor()) {
                    blockMD.getTextureColor();
                    this.currentPalette.applyColor(blockMD, true);
                }
                if (!blockMD.hasColor()) {
                    this.logger.warn("Could not derive color for " + blockMD.getBlockState());
                }
            }
            if (this.currentPalette.isDirty()) {
                long elapsed = System.currentTimeMillis() - start;
                this.currentPalette.writeToFile();
                this.logger.info(String.format("Updated color palette for %s blockstates in %sms: %s", this.currentPalette.size(), elapsed, this.currentPalette.getOrigin()));
            } else {
                long elapsed = System.currentTimeMillis() - start;
                this.logger.info(String.format("Loaded color palette for %s blockstates in %sms", this.currentPalette.size(), elapsed));
            }
            MapPlayerTask.forceNearbyRemap();
        } catch (Throwable var11) {
            this.logger.error("ColorManager.initBlockColors() encountered an unexpected error: " + LogFormatter.toPartialString(var11));
        }
    }

    @Nullable
    public float[] getAverageColor(Collection<ColoredSprite> sprites) {
        if (sprites != null && !sprites.isEmpty()) {
            List<String> names = (List<String>) sprites.stream().map(ColoredSprite::getIconName).collect(Collectors.toList());
            Collections.sort(names);
            String name = Joiner.on(",").join(names);
            float[] rgba;
            if (this.iconColorCache.containsKey(name)) {
                rgba = (float[]) this.iconColorCache.get(name);
            } else {
                rgba = this.calculateAverageColor(sprites);
                if (rgba != null) {
                    this.iconColorCache.put(name, rgba);
                    if (this.logger.isDebugEnabled()) {
                        this.logger.debug(String.format("Cached color %s for %s", RGB.toHexString(RGB.toInteger(rgba)), name));
                    }
                }
            }
            return rgba;
        } else {
            return null;
        }
    }

    private float[] calculateAverageColor(Collection<ColoredSprite> sprites) {
        List<NativeImage> images = new ArrayList(sprites.size());
        for (ColoredSprite coloredSprite : sprites) {
            NativeImage img = coloredSprite.getColoredImage();
            if (img != null) {
                images.add(img);
            }
        }
        if (images.isEmpty()) {
            return null;
        } else {
            int count = 0;
            int b = 0;
            int g = 0;
            int r = 0;
            int a = 0;
            for (NativeImage image : images) {
                try {
                    int[] argbInts = image.makePixelArray();
                    for (int argb : argbInts) {
                        int alpha = argb >> 24 & 0xFF;
                        if (alpha > 0) {
                            count++;
                            a += alpha;
                            r += argb >> 16 & 0xFF;
                            g += argb >> 8 & 0xFF;
                            b += argb & 0xFF;
                        }
                    }
                } catch (Exception var16) {
                }
            }
            if (count > 0) {
                int rgb = RGB.toInteger(r / count, g / count, b / count);
                return RGB.floats(rgb, (float) (a / count));
            } else {
                return null;
            }
        }
    }
}