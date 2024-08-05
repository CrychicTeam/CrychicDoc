package dev.xkmc.l2backpack.content.remote.common;

import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class StorageContainer implements ContainerListener {

    private final CompoundTag tag;

    final long password;

    public final UUID id;

    public final SimpleContainer container;

    public final int color;

    StorageContainer(UUID id, int color, CompoundTag tag) {
        this.tag = tag;
        this.id = id;
        this.color = color;
        this.password = tag.getLong("password");
        this.container = new SimpleContainer(27);
        if (tag.contains("container")) {
            ListTag list = tag.getList("container", 10);
            for (int i = 0; i < list.size(); i++) {
                this.container.setItem(i, ItemStack.of((CompoundTag) list.get(i)));
            }
        }
        this.container.addListener(this);
    }

    @Override
    public void containerChanged(Container cont) {
        ListTag list = new ListTag();
        for (int i = 0; i < this.container.getContainerSize(); i++) {
            list.add(i, (Tag) this.container.getItem(i).save(new CompoundTag()));
        }
        this.tag.put("container", list);
    }

    public boolean isValid() {
        return this.password == this.tag.getLong("password");
    }
}