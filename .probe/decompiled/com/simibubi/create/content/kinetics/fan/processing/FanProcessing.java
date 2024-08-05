package com.simibubi.create.content.kinetics.fan.processing;

import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FanProcessing {

    public static boolean canProcess(ItemEntity entity, FanProcessingType type) {
        if (entity.getPersistentData().contains("CreateData")) {
            CompoundTag compound = entity.getPersistentData().getCompound("CreateData");
            if (compound.contains("Processing")) {
                CompoundTag processing = compound.getCompound("Processing");
                if (AllFanProcessingTypes.parseLegacy(processing.getString("Type")) != type) {
                    return type.canProcess(entity.getItem(), entity.m_9236_());
                }
                if (processing.getInt("Time") >= 0) {
                    return true;
                }
                if (processing.getInt("Time") == -1) {
                    return false;
                }
            }
        }
        return type.canProcess(entity.getItem(), entity.m_9236_());
    }

    public static boolean applyProcessing(ItemEntity entity, FanProcessingType type) {
        if (decrementProcessingTime(entity, type) != 0) {
            return false;
        } else {
            List<ItemStack> stacks = type.process(entity.getItem(), entity.m_9236_());
            if (stacks == null) {
                return false;
            } else if (stacks.isEmpty()) {
                entity.m_146870_();
                return false;
            } else {
                entity.setItem((ItemStack) stacks.remove(0));
                for (ItemStack additional : stacks) {
                    ItemEntity entityIn = new ItemEntity(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), additional);
                    entityIn.m_20256_(entity.m_20184_());
                    entity.m_9236_().m_7967_(entityIn);
                }
                return true;
            }
        }
    }

    public static TransportedItemStackHandlerBehaviour.TransportedResult applyProcessing(TransportedItemStack transported, Level world, FanProcessingType type) {
        TransportedItemStackHandlerBehaviour.TransportedResult ignore = TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
        if (transported.processedBy != type) {
            transported.processedBy = type;
            int timeModifierForStackSize = (transported.stack.getCount() - 1) / 16 + 1;
            int processingTime = AllConfigs.server().kinetics.fanProcessingTime.get() * timeModifierForStackSize + 1;
            transported.processingTime = processingTime;
            if (!type.canProcess(transported.stack, world)) {
                transported.processingTime = -1;
            }
            return ignore;
        } else if (transported.processingTime == -1) {
            return ignore;
        } else if (transported.processingTime-- > 0) {
            return ignore;
        } else {
            List<ItemStack> stacks = type.process(transported.stack, world);
            if (stacks == null) {
                return ignore;
            } else {
                List<TransportedItemStack> transportedStacks = new ArrayList();
                for (ItemStack additional : stacks) {
                    TransportedItemStack newTransported = transported.getSimilar();
                    newTransported.stack = additional.copy();
                    transportedStacks.add(newTransported);
                }
                return TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(transportedStacks);
            }
        }
    }

    private static int decrementProcessingTime(ItemEntity entity, FanProcessingType type) {
        CompoundTag nbt = entity.getPersistentData();
        if (!nbt.contains("CreateData")) {
            nbt.put("CreateData", new CompoundTag());
        }
        CompoundTag createData = nbt.getCompound("CreateData");
        if (!createData.contains("Processing")) {
            createData.put("Processing", new CompoundTag());
        }
        CompoundTag processing = createData.getCompound("Processing");
        if (!processing.contains("Type") || AllFanProcessingTypes.parseLegacy(processing.getString("Type")) != type) {
            processing.putString("Type", FanProcessingTypeRegistry.getIdOrThrow(type).toString());
            int timeModifierForStackSize = (entity.getItem().getCount() - 1) / 16 + 1;
            int processingTime = AllConfigs.server().kinetics.fanProcessingTime.get() * timeModifierForStackSize + 1;
            processing.putInt("Time", processingTime);
        }
        int value = processing.getInt("Time") - 1;
        processing.putInt("Time", value);
        return value;
    }
}