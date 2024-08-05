package icyllis.modernui.mc.testforge;

import icyllis.modernui.mc.forge.MuiRegistries;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class TestContainerMenu extends AbstractContainerMenu {

    private boolean mDiamond;

    public TestContainerMenu() {
        super(null, 0);
        assert FMLEnvironment.dist.isClient();
    }

    public TestContainerMenu(int containerId, @Nonnull Inventory inventory, @Nonnull FriendlyByteBuf data) {
        super(MuiRegistries.TEST_MENU.get(), containerId);
        this.mDiamond = data.readBoolean();
    }

    public TestContainerMenu(int containerId, @Nonnull Inventory inventory, @Nonnull Player player) {
        super(MuiRegistries.TEST_MENU.get(), containerId);
    }

    @Override
    public void removed(@Nonnull Player player) {
        super.removed(player);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
    }

    public boolean isDiamond() {
        return this.mDiamond;
    }

    @Deprecated
    @Nonnull
    @Override
    protected final DataSlot addDataSlot(@Nonnull DataSlot intValue) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    @Override
    protected final void addDataSlots(@Nonnull ContainerData array) {
        throw new UnsupportedOperationException();
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    public final void setData(int id, int data) {
        throw new UnsupportedOperationException();
    }
}