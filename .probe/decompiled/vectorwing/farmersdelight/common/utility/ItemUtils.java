package vectorwing.farmersdelight.common.utility;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public class ItemUtils {

    public static void dropItems(Level level, BlockPos pos, IItemHandler inventory) {
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            Containers.dropItemStack(level, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), inventory.getStackInSlot(slot));
        }
    }

    public static boolean isInventoryEmpty(IItemHandler inventory) {
        for (int i = 0; i < inventory.getSlots(); i++) {
            if (!inventory.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void spawnItemEntity(Level level, ItemStack stack, double x, double y, double z, double xMotion, double yMotion, double zMotion) {
        ItemEntity entity = new ItemEntity(level, x, y, z, stack);
        entity.m_20334_(xMotion, yMotion, zMotion);
        level.m_7967_(entity);
    }
}