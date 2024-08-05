package net.minecraftforge.event;

import com.google.common.base.Preconditions;
import java.util.Locale;
import java.util.Map;
import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
import net.minecraftforge.eventbus.api.Event;

public class RegisterStructureConversionsEvent extends Event {

    private final Map<String, StructuresBecomeConfiguredFix.Conversion> map;

    public RegisterStructureConversionsEvent(Map<String, StructuresBecomeConfiguredFix.Conversion> map) {
        this.map = map;
    }

    public void register(String oldStructureID, StructuresBecomeConfiguredFix.Conversion conversion) {
        Preconditions.checkNotNull(oldStructureID, "Original structure ID must not be null");
        Preconditions.checkArgument(oldStructureID.toLowerCase(Locale.ROOT).equals(oldStructureID), "Original structure ID should be in all lowercase");
        Preconditions.checkNotNull(conversion, "Structure conversion must not be null");
        Preconditions.checkNotNull(conversion.fallback(), "Fallback structure ID in structure conversion must not be null");
        if (this.map.putIfAbsent(oldStructureID.toLowerCase(Locale.ROOT), conversion) != null) {
            throw new IllegalArgumentException("Conversion has already been registered for structure " + oldStructureID);
        }
    }
}