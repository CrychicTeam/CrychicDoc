package com.mna.items.spawn_eggs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.common.util.NonNullSupplier;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModSpawnEggItem extends SpawnEggItem {

    protected static final List<ModSpawnEggItem> UNADDED_EGGS = new ArrayList();

    private final Lazy<? extends EntityType<?>> entityTypeSupplier;

    public ModSpawnEggItem(NonNullSupplier<? extends EntityType<?>> entityTypeSupplier, int color_a, int color_b, Item.Properties properties) {
        super(null, color_a, color_b, properties);
        this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
        UNADDED_EGGS.add(this);
    }

    public ModSpawnEggItem(RegistryObject<? extends EntityType<?>> entityTypeSupplier, int color_a, int color_b, Item.Properties properties) {
        super(null, color_a, color_b, properties);
        this.entityTypeSupplier = Lazy.of(entityTypeSupplier);
        UNADDED_EGGS.add(this);
    }

    public static void initUnaddedEggs() {
        Map<EntityType<?>, SpawnEggItem> EGGS = (Map<EntityType<?>, SpawnEggItem>) ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "f_43201_");
        DefaultDispenseItemBehavior defaultDispenseItemBehavior = new DefaultDispenseItemBehavior() {
        };
        for (SpawnEggItem egg : UNADDED_EGGS) {
            EGGS.put(egg.getType(null), egg);
            DispenserBlock.registerBehavior(egg, defaultDispenseItemBehavior);
        }
        UNADDED_EGGS.clear();
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundTag p_208076_1_) {
        return (EntityType<?>) this.entityTypeSupplier.get();
    }
}