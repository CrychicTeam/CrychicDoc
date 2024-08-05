package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.ItemDialBlockEntity;
import com.rekindled.embers.util.DecimalFormats;
import java.text.DecimalFormat;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class ItemDialBlock extends DialBaseBlock {

    public static final String DIAL_TYPE = "item";

    public ItemDialBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos.relative((Direction) state.m_61143_(f_52588_), -1));
        if (blockEntity != null) {
            IItemHandler cap = (IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, ((Direction) state.m_61143_(f_52588_)).getOpposite()).orElse((IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null));
            if (cap != null) {
                double contents = 0.0;
                double capacity = 0.0;
                for (int i = 0; i < cap.getSlots(); i++) {
                    contents += (double) cap.getStackInSlot(i).getCount();
                    capacity += (double) cap.getSlotLimit(i);
                }
                if (contents >= capacity) {
                    return 15;
                }
                return (int) Math.ceil(14.0 * contents / capacity);
            }
        }
        return 0;
    }

    @Override
    protected void getBEData(Direction facing, ArrayList<Component> text, BlockEntity blockEntity, int maxLines) {
        if (blockEntity instanceof ItemDialBlockEntity dial && dial.display) {
            for (int i = 0; i < dial.itemStacks.length && i < maxLines; i++) {
                text.add(Component.translatable("embers.tooltip.itemdial.slot", i, formatItemStack(dial.itemStacks[i])));
            }
            if (dial.itemStacks.length + dial.extraLines > Math.min(maxLines, dial.itemStacks.length)) {
                text.add(Component.translatable("embers.tooltip.too_many", dial.itemStacks.length - Math.min(maxLines, dial.itemStacks.length) + dial.extraLines));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static MutableComponent formatItemStack(ItemStack stack) {
        DecimalFormat stackFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.item_amount");
        return !stack.isEmpty() ? Component.translatable("embers.tooltip.itemdial.item", stackFormat.format((long) stack.getCount()), stack.getHoverName().getString()) : Component.translatable("embers.tooltip.itemdial.noitem");
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.ITEM_DIAL_ENTITY.get().create(pPos, pState);
    }

    @Override
    public String getDialType() {
        return "item";
    }
}