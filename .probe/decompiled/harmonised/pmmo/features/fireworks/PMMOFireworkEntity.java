package harmonised.pmmo.features.fireworks;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PMMOFireworkEntity extends FireworkRocketEntity {

    public PMMOFireworkEntity(Level worldIn, double x, double y, double z, ItemStack givenItem) {
        super(worldIn, x, y, z, givenItem);
    }

    @Override
    public void explode() {
        this.m_9236_().broadcastEntityEvent(this, (byte) 17);
        this.m_142687_(Entity.RemovalReason.DISCARDED);
    }
}