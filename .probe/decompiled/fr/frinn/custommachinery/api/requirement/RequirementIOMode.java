package fr.frinn.custommachinery.api.requirement;

import fr.frinn.custommachinery.api.ICustomMachineryAPI;
import fr.frinn.custommachinery.api.codec.NamedCodec;
import java.util.Locale;

public enum RequirementIOMode {

    INPUT, OUTPUT;

    public static final NamedCodec<RequirementIOMode> CODEC = NamedCodec.enumCodec(RequirementIOMode.class);

    public static RequirementIOMode value(String mode) {
        return valueOf(mode.toUpperCase(Locale.ENGLISH));
    }

    public String toString() {
        return super.toString().toLowerCase(Locale.ENGLISH);
    }

    public String getTranslationKey() {
        return ICustomMachineryAPI.INSTANCE.modid() + ".requirement.mode." + this;
    }
}