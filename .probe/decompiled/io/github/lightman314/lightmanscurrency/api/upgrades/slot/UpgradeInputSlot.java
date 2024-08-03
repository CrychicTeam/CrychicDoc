package io.github.lightman314.lightmanscurrency.api.upgrades.slot;

import com.mojang.datafixers.util.Pair;
import io.github.lightman314.lightmanscurrency.api.upgrades.IUpgradeable;
import io.github.lightman314.lightmanscurrency.common.items.UpgradeItem;
import io.github.lightman314.lightmanscurrency.common.menus.slots.SimpleSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UpgradeInputSlot extends SimpleSlot {

    public static final ResourceLocation EMPTY_UPGRADE_SLOT = new ResourceLocation("lightmanscurrency", "item/empty_upgrade_slot");

    private final IUpgradeable machine;

    private final Runnable onModified;

    public UpgradeInputSlot(Container inventory, int index, int x, int y, IUpgradeable machine) {
        this(inventory, index, x, y, machine, () -> {
        });
    }

    public UpgradeInputSlot(Container inventory, int index, int x, int y, IUpgradeable machine, Runnable onModified) {
        super(inventory, index, x, y);
        this.machine = machine;
        this.onModified = onModified;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof UpgradeItem item ? this.machine.allowUpgrade(item) : false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.onModified != null) {
            this.onModified.run();
        }
    }

    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        return Pair.of(InventoryMenu.BLOCK_ATLAS, EMPTY_UPGRADE_SLOT);
    }
}