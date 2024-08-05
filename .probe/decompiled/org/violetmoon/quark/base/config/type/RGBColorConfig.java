package org.violetmoon.quark.base.config.type;

import java.util.Objects;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.config.definition.RGBClientDefinition;
import org.violetmoon.zeta.client.config.definition.ClientDefinitionExt;
import org.violetmoon.zeta.client.config.definition.IConfigDefinitionProvider;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.ConfigFlagManager;
import org.violetmoon.zeta.config.SectionDefinition;
import org.violetmoon.zeta.config.type.IConfigType;
import org.violetmoon.zeta.module.ZetaModule;

public class RGBColorConfig implements IConfigType, IConfigDefinitionProvider {

    @Config
    public double r;

    @Config
    public double g;

    @Config
    public double b;

    protected double dr;

    protected double dg;

    protected double db;

    protected int color;

    private RGBColorConfig(double r, double g, double b) {
        this(r, g, b, 1.0);
    }

    RGBColorConfig(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static RGBColorConfig forColor(double r, double g, double b) {
        RGBColorConfig config = new RGBColorConfig(r, g, b);
        config.color = config.calculateColor();
        config.dr = r;
        config.dg = g;
        config.db = b;
        return config;
    }

    public int getColor() {
        return this.color;
    }

    public double getElement(int idx) {
        return switch(idx) {
            case 0 ->
                this.r;
            case 1 ->
                this.g;
            case 2 ->
                this.b;
            case 3 ->
                this.getAlphaComponent();
            default ->
                0.0;
        };
    }

    public void setElement(int idx, double c) {
        switch(idx) {
            case 0:
                this.r = c;
                break;
            case 1:
                this.g = c;
                break;
            case 2:
                this.b = c;
                break;
            case 3:
                this.setAlphaComponent(c);
        }
        this.color = this.calculateColor();
    }

    @Override
    public void onReload(ZetaModule module, ConfigFlagManager flagManager) {
        this.color = this.calculateColor();
    }

    int calculateColor() {
        int rComponent = clamp(this.r * 255.0) << 16;
        int gComponent = clamp(this.g * 255.0) << 8;
        int bComponent = clamp(this.b * 255.0);
        int aComponent = clamp(this.getAlphaComponent() * 255.0) << 24;
        return aComponent | bComponent | gComponent | rComponent;
    }

    double getAlphaComponent() {
        return 1.0;
    }

    void setAlphaComponent(double c) {
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RGBColorConfig that = (RGBColorConfig) o;
            return Double.compare(that.r, this.r) == 0 && Double.compare(that.g, this.g) == 0 && Double.compare(that.b, this.b) == 0;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.r, this.g, this.b });
    }

    private static int clamp(double val) {
        return clamp((int) val);
    }

    private static int clamp(int val) {
        return Mth.clamp(val, 0, 255);
    }

    @NotNull
    @Override
    public ClientDefinitionExt<SectionDefinition> getClientConfigDefinition(SectionDefinition parent) {
        return new RGBClientDefinition(parent);
    }
}