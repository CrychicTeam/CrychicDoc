package se.mickelus.tetra.module.data;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SynergyData extends VariantData {

    public String[] improvements = new String[0];

    public String[] moduleVariants = new String[0];

    public String[] modules = new String[0];

    public boolean sameVariant = false;

    public boolean matchSuffixed = false;

    public String name;

    public String visibilityKey;

    public boolean obscured = false;

    public boolean hidden = false;
}