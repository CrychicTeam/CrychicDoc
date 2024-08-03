package org.violetmoon.quark.base.config.type;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.config.definition.ConvulsionMatrixClientDefinition;
import org.violetmoon.quark.content.client.module.GreenerGrassModule;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.definition.IConfigDefinitionProvider;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.ConfigFlagManager;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.type.IConfigType;
import org.violetmoon.zeta.module.ZetaModule;

public class ConvulsionMatrixConfig implements IConfigType, IConfigDefinitionProvider {

    @Config
    public List<Double> r;

    @Config
    public List<Double> g;

    @Config
    public List<Double> b;

    public final ConvulsionMatrixConfig.Params params;

    public double[] colorMatrix;

    public ConvulsionMatrixConfig(ConvulsionMatrixConfig.Params params) {
        this.params = params;
        double[] defaultMatrix = params.defaultMatrix;
        this.colorMatrix = Arrays.copyOf(defaultMatrix, defaultMatrix.length);
        this.updateRGB();
    }

    @Override
    public void onReload(ZetaModule module, ConfigFlagManager flagManager) {
        try {
            this.colorMatrix = new double[] { (Double) this.r.get(0), (Double) this.r.get(1), (Double) this.r.get(2), (Double) this.g.get(0), (Double) this.g.get(1), (Double) this.g.get(2), (Double) this.b.get(0), (Double) this.b.get(1), (Double) this.b.get(2) };
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException var4) {
            var4.printStackTrace();
            this.colorMatrix = Arrays.copyOf(this.params.defaultMatrix, this.params.defaultMatrix.length);
        }
    }

    private void updateRGB() {
        this.r = Arrays.asList(this.colorMatrix[0], this.colorMatrix[1], this.colorMatrix[2]);
        this.g = Arrays.asList(this.colorMatrix[3], this.colorMatrix[4], this.colorMatrix[5]);
        this.b = Arrays.asList(this.colorMatrix[6], this.colorMatrix[7], this.colorMatrix[8]);
    }

    public static int convolve(double[] colorMatrix, int color) {
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int outR = clamp((int) ((double) r * colorMatrix[0] + (double) g * colorMatrix[1] + (double) b * colorMatrix[2]));
        int outG = clamp((int) ((double) r * colorMatrix[3] + (double) g * colorMatrix[4] + (double) b * colorMatrix[5]));
        int outB = clamp((int) ((double) r * colorMatrix[6] + (double) g * colorMatrix[7] + (double) b * colorMatrix[8]));
        return 0xFF000000 | ((outR & 0xFF) << 16) + ((outG & 0xFF) << 8) + (outB & 0xFF);
    }

    private static int clamp(int val) {
        return Math.min(255, Math.max(0, val));
    }

    public int convolve(int color) {
        return convolve(this.colorMatrix, color);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            if (obj instanceof ConvulsionMatrixConfig other && Arrays.equals(other.colorMatrix, this.colorMatrix)) {
                return true;
            }
            return false;
        }
    }

    public int hashCode() {
        return Arrays.hashCode(this.colorMatrix);
    }

    @NotNull
    @Override
    public ClientDefinitionExt<SectionDefinition> getClientConfigDefinition(SectionDefinition def) {
        return new ConvulsionMatrixClientDefinition(this, def);
    }

    public static class Params {

        private static final String IDENTITY_NAME = "Vanilla";

        public static final double[] IDENTITY = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0 };

        public final String name;

        public final String[] biomeNames;

        public final double[] defaultMatrix;

        public final int[] testColors;

        @Nullable
        public final int[] folliageTestColors;

        private final String[] presetNames;

        private final double[][] presets;

        public final Map<String, double[]> presetMap;

        public Params(String name, double[] defaultMatrix, String[] biomeNames, int[] testColors, @Nullable int[] folliageTestColors, String[] presetNames, double[][] presets) {
            Preconditions.checkArgument(defaultMatrix.length == 9);
            Preconditions.checkArgument(biomeNames.length == 6);
            Preconditions.checkArgument(testColors.length == 6);
            Preconditions.checkArgument(folliageTestColors == null || folliageTestColors.length == 6);
            Preconditions.checkArgument(presetNames.length == presets.length);
            this.name = name;
            this.defaultMatrix = defaultMatrix;
            this.biomeNames = biomeNames;
            this.testColors = testColors;
            this.folliageTestColors = folliageTestColors;
            this.presetNames = presetNames;
            this.presets = presets;
            this.presetMap = new LinkedHashMap();
            this.presetMap.put("Vanilla", IDENTITY);
            for (int i = 0; i < presetNames.length; i++) {
                this.presetMap.put(presetNames[i], presets[i]);
            }
        }

        public ConvulsionMatrixConfig.Params cloneWithNewDefault(double[] newDefault) {
            return new ConvulsionMatrixConfig.Params(this.name, newDefault, this.biomeNames, this.testColors, this.folliageTestColors, this.presetNames, this.presets);
        }

        public boolean shouldDisplayFolliage() {
            return this.folliageTestColors != null && GreenerGrassModule.affectLeaves;
        }
    }
}