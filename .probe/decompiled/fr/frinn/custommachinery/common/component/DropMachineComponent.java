package fr.frinn.custommachinery.common.component;

import fr.frinn.custommachinery.api.component.ComponentIOMode;
import fr.frinn.custommachinery.api.component.IMachineComponentManager;
import fr.frinn.custommachinery.api.component.MachineComponentType;
import fr.frinn.custommachinery.common.init.Registration;
import fr.frinn.custommachinery.common.util.ingredient.IIngredient;
import fr.frinn.custommachinery.impl.component.AbstractMachineComponent;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class DropMachineComponent extends AbstractMachineComponent {

    public DropMachineComponent(IMachineComponentManager manager) {
        super(manager, ComponentIOMode.BOTH);
    }

    @Override
    public MachineComponentType<DropMachineComponent> getType() {
        return (MachineComponentType<DropMachineComponent>) Registration.DROP_MACHINE_COMPONENT.get();
    }

    public int getItemAmount(List<IIngredient<Item>> items, int radius, boolean whitelist) {
        List<Item> filter = items.stream().flatMap(ingredient -> ingredient.getAll().stream()).toList();
        AABB box = new AABB(this.getManager().getTile().m_58899_().offset(radius, radius, radius), this.getManager().getTile().m_58899_().offset(-radius, -radius, -radius));
        return this.getManager().getLevel().m_6443_(ItemEntity.class, box, entity -> filter.contains(entity.getItem().getItem()) == whitelist && entity.m_20183_().m_123314_(this.getManager().getTile().m_58899_(), (double) radius)).stream().mapToInt(entity -> entity.getItem().getCount()).sum();
    }

    public void consumeItem(List<IIngredient<Item>> items, int amount, int radius, boolean whitelist) {
        List<Item> filter = items.stream().flatMap(ingredient -> ingredient.getAll().stream()).toList();
        AtomicInteger toRemove = new AtomicInteger(amount);
        AABB box = new AABB(this.getManager().getTile().m_58899_().offset(radius, radius, radius), this.getManager().getTile().m_58899_().offset(-radius, -radius, -radius));
        this.getManager().getLevel().m_6443_(ItemEntity.class, box, entity -> filter.contains(entity.getItem().getItem()) == whitelist && entity.m_20183_().m_123314_(this.getManager().getTile().m_58899_(), (double) radius)).forEach(entity -> {
            int maxRemove = Math.min(toRemove.get(), entity.getItem().getCount());
            if (maxRemove == entity.getItem().getCount()) {
                entity.m_142687_(Entity.RemovalReason.DISCARDED);
            } else {
                entity.getItem().shrink(maxRemove);
            }
            toRemove.addAndGet(-maxRemove);
        });
    }

    public boolean produceItem(ItemStack stack) {
        Level world = this.getManager().getLevel();
        BlockPos pos = this.getManager().getTile().m_58899_().above();
        ItemEntity entity = new ItemEntity(world, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), stack);
        return world.m_7967_(entity);
    }
}