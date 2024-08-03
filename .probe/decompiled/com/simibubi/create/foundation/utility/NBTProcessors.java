package com.simibubi.create.foundation.utility;

import com.simibubi.create.AllBlockEntityTypes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.UnaryOperator;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;

public final class NBTProcessors {

    private static final Map<BlockEntityType<?>, UnaryOperator<CompoundTag>> processors = new HashMap();

    private static final Map<BlockEntityType<?>, UnaryOperator<CompoundTag>> survivalProcessors = new HashMap();

    public static synchronized void addProcessor(BlockEntityType<?> type, UnaryOperator<CompoundTag> processor) {
        processors.put(type, processor);
    }

    public static synchronized void addSurvivalProcessor(BlockEntityType<?> type, UnaryOperator<CompoundTag> processor) {
        survivalProcessors.put(type, processor);
    }

    public static UnaryOperator<CompoundTag> itemProcessor(String tagKey) {
        return data -> {
            CompoundTag compound = data.getCompound(tagKey);
            if (!compound.contains("tag", 10)) {
                return data;
            } else {
                CompoundTag itemTag = compound.getCompound("tag");
                if (itemTag == null) {
                    return data;
                } else {
                    for (String key : new HashSet(itemTag.getAllKeys())) {
                        if (isUnsafeItemNBTKey(key)) {
                            itemTag.remove(key);
                        }
                    }
                    if (itemTag.isEmpty()) {
                        compound.remove("tag");
                    }
                    return data;
                }
            }
        };
    }

    public static ItemStack withUnsafeNBTDiscarded(ItemStack stack) {
        if (stack.getTag() == null) {
            return stack;
        } else {
            ItemStack copy = stack.copy();
            stack.getTag().getAllKeys().stream().filter(NBTProcessors::isUnsafeItemNBTKey).forEach(copy::m_41749_);
            return copy;
        }
    }

    public static boolean isUnsafeItemNBTKey(String name) {
        if (name.equals("StoredEnchantments")) {
            return false;
        } else if (name.equals("Enchantments")) {
            return false;
        } else if (name.contains("Potion")) {
            return false;
        } else {
            return name.contains("Damage") ? false : !name.equals("display");
        }
    }

    public static boolean textComponentHasClickEvent(String json) {
        Component component = Component.Serializer.fromJson(json.isEmpty() ? "\"\"" : json);
        return component != null && component.getStyle() != null && component.getStyle().getClickEvent() != null;
    }

    private NBTProcessors() {
    }

    @Nullable
    public static CompoundTag process(BlockEntity blockEntity, CompoundTag compound, boolean survival) {
        if (compound == null) {
            return null;
        } else {
            BlockEntityType<?> type = blockEntity.getType();
            if (survival && survivalProcessors.containsKey(type)) {
                compound = (CompoundTag) ((UnaryOperator) survivalProcessors.get(type)).apply(compound);
            }
            if (compound != null && processors.containsKey(type)) {
                return (CompoundTag) ((UnaryOperator) processors.get(type)).apply(compound);
            } else if (blockEntity instanceof SpawnerBlockEntity) {
                return compound;
            } else {
                return blockEntity.onlyOpCanSetNbt() ? null : compound;
            }
        }
    }

    static {
        addProcessor(BlockEntityType.SIGN, data -> {
            for (int i = 0; i < 4; i++) {
                if (textComponentHasClickEvent(data.getString("Text" + (i + 1)))) {
                    return null;
                }
            }
            return data;
        });
        addProcessor(BlockEntityType.LECTERN, data -> {
            if (!data.contains("Book", 10)) {
                return data;
            } else {
                CompoundTag book = data.getCompound("Book");
                if (!book.contains("tag", 10)) {
                    return data;
                } else {
                    CompoundTag tag = book.getCompound("tag");
                    if (!tag.contains("pages", 9)) {
                        return data;
                    } else {
                        for (Tag inbt : tag.getList("pages", 8)) {
                            if (textComponentHasClickEvent(inbt.getAsString())) {
                                return null;
                            }
                        }
                        return data;
                    }
                }
            }
        });
        addProcessor((BlockEntityType<?>) AllBlockEntityTypes.CREATIVE_CRATE.get(), itemProcessor("Filter"));
        addProcessor((BlockEntityType<?>) AllBlockEntityTypes.PLACARD.get(), itemProcessor("Item"));
    }
}