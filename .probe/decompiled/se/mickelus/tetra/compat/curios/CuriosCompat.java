package se.mickelus.tetra.compat.curios;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;

@ParametersAreNonnullByDefault
public class CuriosCompat {

    public static final String modId = "curios";

    public static final Boolean isLoaded = ModList.get().isLoaded("curios");

    public static void enqueueIMC(InterModEnqueueEvent event) {
        if (isLoaded) {
            InterModComms.sendTo("curios", "register_type", () -> new SlotTypeMessage.Builder("belt").size(1).build());
        }
    }
}