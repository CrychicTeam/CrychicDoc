package com.mna.blocks.tileentities;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.items.ItemInit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;

public class BookshelfTile extends TileEntityWithInventory {

    private static List<Item> directDisplayItems = Arrays.asList(ItemInit.GUIDE_BOOK.get(), ItemInit.SPELL_BOOK.get(), ItemInit.GRIMOIRE.get(), ItemInit.GRIMOIRE_COUNCIL.get(), ItemInit.GRIMOIRE_DEMON.get(), ItemInit.GRIMOIRE_UNDEAD.get(), ItemInit.GRIMOIRE_FEY.get(), ItemInit.BOOK_MARKS.get(), ItemInit.FLAT_LANDS_BOOK.get(), ItemInit.MODIFIER_BOOK.get(), ItemInit.ROTE_BOOK.get(), ItemInit.RECIPE_COPY_BOOK.get());

    private ItemStack[] displayItems = new ItemStack[15];

    public BookshelfTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.BOOKSHELF.get(), pos, state, 15);
        for (int i = 0; i < 15; i++) {
            this.displayItems[i] = ItemStack.EMPTY;
        }
    }

    private void refreshDisplayItems(ArrayList<ResourceLocation> items, ArrayList<Integer> colors) {
        for (int i = 0; i < 15; i++) {
            Item item = ForgeRegistries.ITEMS.getValue((ResourceLocation) items.get(i));
            int color = (Integer) colors.get(i);
            if (item == null || item == Items.AIR) {
                this.displayItems[i] = ItemStack.EMPTY;
            } else if (directDisplayItems.contains(item)) {
                this.displayItems[i] = new ItemStack(item);
                if (color > -1 && item instanceof DyeableLeatherItem) {
                    ((DyeableLeatherItem) item).setColor(this.displayItems[i], color);
                }
            } else {
                this.displayItems[i] = new ItemStack(ItemInit.SPELL_BOOK.get());
                ItemInit.SPELL_BOOK.get().m_41115_(this.displayItems[i], color > -1 ? color : this.resourceLocationToColor((ResourceLocation) items.get(i)));
            }
        }
    }

    private int resourceLocationToColor(ResourceLocation rLoc) {
        return rLoc.toString().hashCode();
    }

    public ItemStack getDisplayItem(int index) {
        return this.displayItems[index % this.displayItems.length];
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        ListTag items = new ListTag();
        for (int i = 0; i < 15; i++) {
            CompoundTag item = new CompoundTag();
            int color = -1;
            if (this.m_8020_(i).getItem() instanceof DyeableLeatherItem) {
                color = ((DyeableLeatherItem) this.m_8020_(i).getItem()).getColor(this.m_8020_(i));
            }
            item.putString("rLoc", ForgeRegistries.ITEMS.getKey(this.m_8020_(i).getItem()).toString());
            if (color > -1) {
                item.putInt("color", color);
            }
            items.add(item);
        }
        tag.put("displayItems", items);
        return tag;
    }

    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        this.parseItemsList(tag);
    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag compoundtag = pkt.getTag();
        if (compoundtag != null) {
            this.parseItemsList(compoundtag);
        }
    }

    private void parseItemsList(CompoundTag tag) {
        if (tag.contains("displayItems")) {
            ListTag items = tag.getList("displayItems", 10);
            ArrayList<ResourceLocation> rLocs = new ArrayList();
            ArrayList<Integer> colors = new ArrayList();
            items.forEach(i -> {
                CompoundTag item = (CompoundTag) i;
                rLocs.add(new ResourceLocation(item.getString("rLoc")));
                if (item.contains("color")) {
                    colors.add(item.getInt("color"));
                } else {
                    colors.add(-1);
                }
            });
            this.refreshDisplayItems(rLocs, colors);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] items = new int[this.m_6643_()];
        int i = 0;
        while (i < this.m_6643_()) {
            items[i] = i++;
        }
        return items;
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
        return true;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

    public AABB getRenderBoundingBox() {
        return new AABB(this.m_58899_()).expandTowards(0.0, 1.0, 0.0);
    }
}