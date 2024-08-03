package net.minecraft.client.resources.model;

import com.google.common.annotations.VisibleForTesting;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

public class ModelResourceLocation extends ResourceLocation {

    @VisibleForTesting
    static final char VARIANT_SEPARATOR = '#';

    private final String variant;

    private ModelResourceLocation(String string0, String string1, String string2, @Nullable ResourceLocation.Dummy resourceLocationDummy3) {
        super(string0, string1, resourceLocationDummy3);
        this.variant = string2;
    }

    public ModelResourceLocation(String string0, String string1, String string2) {
        super(string0, string1);
        this.variant = lowercaseVariant(string2);
    }

    public ModelResourceLocation(ResourceLocation resourceLocation0, String string1) {
        this(resourceLocation0.getNamespace(), resourceLocation0.getPath(), lowercaseVariant(string1), null);
    }

    public static ModelResourceLocation vanilla(String string0, String string1) {
        return new ModelResourceLocation("minecraft", string0, string1);
    }

    private static String lowercaseVariant(String string0) {
        return string0.toLowerCase(Locale.ROOT);
    }

    public String getVariant() {
        return this.variant;
    }

    @Override
    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else if (object0 instanceof ModelResourceLocation && super.equals(object0)) {
            ModelResourceLocation $$1 = (ModelResourceLocation) object0;
            return this.variant.equals($$1.variant);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return 31 * super.hashCode() + this.variant.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + "#" + this.variant;
    }
}