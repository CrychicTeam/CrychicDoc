package com.github.alexthe666.alexsmobs.item;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityVoidWorm;
import com.github.alexthe666.alexsmobs.misc.AMAdvancementTriggerRegistry;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ItemMysteriousWorm extends Item {

    public ItemMysteriousWorm(Item.Properties props) {
        super(props);
    }

    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) AlexsMobs.PROXY.getISTERProperties());
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        if (AMConfig.voidWormSummonable) {
            String dim = entity.m_9236_().dimension().location().toString();
            if (AMConfig.voidWormSpawnDimensions.contains(dim) && entity.m_20186_() < -60.0 && !entity.m_213877_()) {
                entity.m_6074_();
                EntityVoidWorm worm = AMEntityRegistry.VOID_WORM.get().create(entity.m_9236_());
                worm.m_6034_(entity.m_20185_(), 0.0, entity.m_20189_());
                worm.setSegmentCount(25 + new Random().nextInt(15));
                worm.m_146926_(-90.0F);
                worm.updatePostSummon = true;
                worm.setMaxHealth(AMConfig.voidWormMaxHealth, true);
                if (!entity.m_9236_().isClientSide) {
                    Entity thrower = entity.getOwner();
                    if (thrower != null) {
                        UUID uuid = thrower.getUUID();
                        if (entity.m_9236_().m_46003_(uuid) instanceof ServerPlayer) {
                            AMAdvancementTriggerRegistry.VOID_WORM_SUMMON.trigger((ServerPlayer) entity.m_9236_().m_46003_(uuid));
                        }
                    }
                    entity.m_9236_().m_7967_(worm);
                }
            }
        }
        return false;
    }
}