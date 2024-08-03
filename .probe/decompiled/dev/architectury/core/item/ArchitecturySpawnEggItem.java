package dev.architectury.core.item;

import dev.architectury.registry.registries.RegistrySupplier;
import java.util.Objects;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ArchitecturySpawnEggItem extends SpawnEggItem {

    private static final Logger LOGGER = LogManager.getLogger(ArchitecturySpawnEggItem.class);

    private final RegistrySupplier<? extends EntityType<? extends Mob>> entityType;

    protected static DispenseItemBehavior createDispenseItemBehavior() {
        return new DefaultDispenseItemBehavior() {

            @Override
            public ItemStack execute(BlockSource source, ItemStack stack) {
                Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                try {
                    entityType.spawn(source.getLevel(), stack, null, source.getPos().relative(direction), MobSpawnType.DISPENSER, direction != Direction.UP, false);
                } catch (Exception var6) {
                    f_181892_.error("Error while dispensing spawn egg from dispenser at {}", source.getPos(), var6);
                    return ItemStack.EMPTY;
                }
                stack.shrink(1);
                source.getLevel().m_142346_(null, GameEvent.ENTITY_PLACE, source.getPos());
                return stack;
            }
        };
    }

    public ArchitecturySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties) {
        this(entityType, backgroundColor, highlightColor, properties, createDispenseItemBehavior());
    }

    public ArchitecturySpawnEggItem(RegistrySupplier<? extends EntityType<? extends Mob>> entityType, int backgroundColor, int highlightColor, Item.Properties properties, @Nullable DispenseItemBehavior dispenseItemBehavior) {
        super(null, backgroundColor, highlightColor, properties);
        this.entityType = (RegistrySupplier<? extends EntityType<? extends Mob>>) Objects.requireNonNull(entityType, "entityType");
        SpawnEggItem.BY_ID.remove(null);
        entityType.listen(type -> {
            LOGGER.debug("Registering spawn egg {} for {}", this.toString(), Objects.toString(type.arch$registryName()));
            SpawnEggItem.BY_ID.put(type, this);
            this.f_43204_ = type;
            if (dispenseItemBehavior != null) {
                DispenserBlock.registerBehavior(this, dispenseItemBehavior);
            }
        });
    }

    @Override
    public EntityType<?> getType(@Nullable CompoundTag compoundTag) {
        EntityType<?> type = super.getType(compoundTag);
        return type == null ? (EntityType) this.entityType.get() : type;
    }
}