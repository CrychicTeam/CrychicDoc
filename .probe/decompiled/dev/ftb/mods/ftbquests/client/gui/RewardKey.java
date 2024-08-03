package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public class RewardKey {

    private final String title;

    private final Icon icon;

    private final ItemStack stack;

    public RewardKey(String title, Icon icon) {
        this(title, icon, ItemStack.EMPTY);
    }

    public RewardKey(String title, Icon icon, ItemStack stack) {
        this.title = title;
        this.icon = icon;
        this.stack = stack;
    }

    public String getTitle() {
        return this.title;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public int hashCode() {
        return this.stack.isEmpty() ? Objects.hash(new Object[] { this.title, this.icon }) : Objects.hash(new Object[] { this.stack.getItem(), this.stack.getTag() });
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RewardKey key)) {
            return false;
        } else {
            return !this.stack.isEmpty() ? this.stack.getItem() == key.stack.getItem() && Objects.equals(this.stack.getTag(), key.stack.getTag()) : this.title.equals(key.title) && this.icon.equals(key.icon);
        }
    }
}