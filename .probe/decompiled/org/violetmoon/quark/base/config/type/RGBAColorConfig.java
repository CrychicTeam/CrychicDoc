package org.violetmoon.quark.base.config.type;

import java.util.Objects;
import org.violetmoon.zeta.config.Config;

public class RGBAColorConfig extends RGBColorConfig {

    @Config
    public double a;

    protected double da;

    private RGBAColorConfig(double r, double g, double b, double a) {
        super(r, g, b, a);
        this.a = a;
    }

    public static RGBAColorConfig forColor(double r, double g, double b, double a) {
        RGBAColorConfig config = new RGBAColorConfig(r, g, b, a);
        config.color = config.calculateColor();
        config.dr = r;
        config.dg = g;
        config.db = b;
        config.da = a;
        return config;
    }

    @Override
    public double getAlphaComponent() {
        return this.a;
    }

    @Override
    void setAlphaComponent(double c) {
        this.a = c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RGBAColorConfig that = (RGBAColorConfig) o;
            return Double.compare(that.r, this.r) == 0 && Double.compare(that.g, this.g) == 0 && Double.compare(that.b, this.b) == 0 && Double.compare(that.a, this.a) == 0;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.r, this.g, this.b, this.a });
    }
}