package vectorwing.farmersdelight.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PopsicleItem extends ConsumableItem {

    public PopsicleItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        consumer.m_20095_();
    }
}