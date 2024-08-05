package dev.latvian.mods.kubejs.block.entity;

import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.typings.desc.PrimitiveDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class InventoryAttachment extends SimpleContainer implements BlockEntityAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType("inventory", TypeDescJS.object().add("xsize", TypeDescJS.NUMBER, false).add("ysize", TypeDescJS.NUMBER, false).add("inputFilter", new PrimitiveDescJS("Ingredient"), true), map -> {
        int width = ((Number) map.get("width")).intValue();
        int height = ((Number) map.get("height")).intValue();
        Ingredient inputFilter = map.containsKey("inputFilter") ? IngredientJS.of(map.get("inputFilter")) : null;
        return entity -> new InventoryAttachment(entity, width, height, inputFilter);
    });

    public final int width;

    public final int height;

    public final BlockEntityJS blockEntity;

    public final Ingredient inputFilter;

    public InventoryAttachment(BlockEntityJS blockEntity, int width, int height, @Nullable Ingredient inputFilter) {
        super(width * height);
        this.width = width;
        this.height = height;
        this.blockEntity = blockEntity;
        this.inputFilter = inputFilter;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.blockEntity.save();
    }

    @Override
    public CompoundTag writeAttachment() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (int i = 0; i < this.m_6643_(); i++) {
            ItemStack stack = this.m_8020_(i);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                itemTag.putByte("Slot", (byte) i);
                stack.save(itemTag);
                list.add(itemTag);
            }
        }
        tag.put("items", list);
        return tag;
    }

    @Override
    public void readAttachment(CompoundTag tag) {
        for (int i = 0; i < this.m_6643_(); i++) {
            this.m_8016_(i);
        }
        ListTag list = tag.getList("items", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag itemTag = list.getCompound(i);
            byte slot = itemTag.getByte("Slot");
            if (slot >= 0 && slot < this.m_6643_()) {
                this.m_6836_(slot, ItemStack.of(itemTag));
            }
        }
    }

    @Override
    public void onRemove(BlockState newState) {
        Containers.dropContents(this.blockEntity.m_58904_(), this.blockEntity.m_58899_(), this);
    }

    @Override
    public boolean canAddItem(ItemStack itemStack) {
        return (this.inputFilter == null || this.inputFilter.test(itemStack)) && super.canAddItem(itemStack);
    }

    @Override
    public boolean canPlaceItem(int i, ItemStack itemStack) {
        return (this.inputFilter == null || this.inputFilter.test(itemStack)) && super.m_7013_(i, itemStack);
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.blockEntity.m_58901_();
    }

    public int kjs$getWidth() {
        return this.width;
    }

    public int kjs$getHeight() {
        return this.height;
    }
}