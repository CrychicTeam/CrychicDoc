package vectorwing.farmersdelight.common.utility;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class MathUtils {

    public static final Random RAND = new Random();

    public static int calcRedstoneFromItemHandler(@Nullable IItemHandlerModifiable handler) {
        if (handler == null) {
            return 0;
        } else {
            int i = 0;
            float f = 0.0F;
            for (int j = 0; j < handler.getSlots(); j++) {
                ItemStack itemstack = handler.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    f += (float) itemstack.getCount() / (float) Math.min(handler.getSlotLimit(j), itemstack.getMaxStackSize());
                    i++;
                }
            }
            f /= (float) handler.getSlots();
            return Mth.floor(f * 14.0F) + (i > 0 ? 1 : 0);
        }
    }
}