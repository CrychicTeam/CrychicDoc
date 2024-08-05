package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.modulargolems.content.menu.ghost.ReadOnlyContainer;
import net.minecraft.world.item.ItemStack;

public record TargetFilterEditor(GolemConfigEditor editor) implements ReadOnlyContainer {

    public TargetFilterLine targetHostile() {
        return new TargetFilterLine(this.editor, this.editor.entry().targetFilter.hostileTo, 0);
    }

    public TargetFilterLine targetFriendly() {
        return new TargetFilterLine(this.editor, this.editor.entry().targetFilter.friendlyTo, 18);
    }

    private TargetFilterConfig getConfig() {
        return this.editor.entry().targetFilter;
    }

    @Override
    public int getContainerSize() {
        return 36;
    }

    @Override
    public boolean isEmpty() {
        return this.getConfig().hostileTo.size() == 0 && this.getConfig().friendlyTo.size() == 0;
    }

    @Override
    public ItemStack getItem(int slot) {
        return slot < 18 ? this.targetHostile().getItem(slot) : this.targetFriendly().getItem(slot);
    }

    public void resetHostile() {
        this.getConfig().resetHostile();
        this.editor().sync();
    }

    public void resetFriendly() {
        this.getConfig().resetFriendly();
        this.editor().sync();
    }
}