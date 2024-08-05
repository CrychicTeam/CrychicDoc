package vectorwing.farmersdelight.common.block.entity.container;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

@ParametersAreNonnullByDefault
public class CookingPotResultSlot extends SlotItemHandler {

    public final CookingPotBlockEntity tileEntity;

    private final Player player;

    private int removeCount;

    public CookingPotResultSlot(Player player, CookingPotBlockEntity tile, IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
        this.tileEntity = tile;
        this.player = player;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    @Nonnull
    @Override
    public ItemStack remove(int amount) {
        if (this.m_6657_()) {
            this.removeCount = this.removeCount + Math.min(amount, this.m_7993_().getCount());
        }
        return super.remove(amount);
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.m_142406_(thePlayer, stack);
    }

    @Override
    protected void onQuickCraft(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack stack) {
        stack.onCraftedBy(this.player.m_9236_(), this.player, this.removeCount);
        if (!this.player.m_9236_().isClientSide) {
            this.tileEntity.awardUsedRecipes(this.player, this.tileEntity.getDroppableInventory());
        }
        this.removeCount = 0;
    }
}