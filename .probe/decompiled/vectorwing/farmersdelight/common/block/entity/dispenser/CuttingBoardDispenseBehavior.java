package vectorwing.farmersdelight.common.block.entity.dispenser;

import java.util.HashMap;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class CuttingBoardDispenseBehavior extends OptionalDispenseItemBehavior {

    private static final HashMap<Item, DispenseItemBehavior> DISPENSE_ITEM_BEHAVIOR_HASH_MAP = new HashMap();

    public static final CuttingBoardDispenseBehavior INSTANCE = new CuttingBoardDispenseBehavior();

    public static void registerBehaviour(Item item, CuttingBoardDispenseBehavior behavior) {
        DISPENSE_ITEM_BEHAVIOR_HASH_MAP.put(item, (DispenseItemBehavior) DispenserBlock.DISPENSER_REGISTRY.get(item));
        DispenserBlock.registerBehavior(item, behavior);
    }

    @Override
    public final ItemStack dispense(BlockSource source, ItemStack stack) {
        if (this.tryDispenseStackOnCuttingBoard(source, stack)) {
            this.m_6823_(source);
            this.m_123387_(source, (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
            return stack;
        } else {
            return ((DispenseItemBehavior) DISPENSE_ITEM_BEHAVIOR_HASH_MAP.get(stack.getItem())).dispense(source, stack);
        }
    }

    public boolean tryDispenseStackOnCuttingBoard(BlockSource source, ItemStack stack) {
        this.m_123573_(false);
        Level level = source.getLevel();
        BlockPos pos = source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
        BlockState state = level.getBlockState(pos);
        Block block = state.m_60734_();
        if (block instanceof CuttingBoardBlock && level.getBlockEntity(pos) instanceof CuttingBoardBlockEntity cuttingBoard) {
            ItemStack boardItem = cuttingBoard.getStoredItem().copy();
            if (!boardItem.isEmpty() && cuttingBoard.processStoredItemUsingTool(stack, null)) {
                CuttingBoardBlock.spawnCuttingParticles(level, pos, boardItem, 5);
                this.m_123573_(true);
            }
            return true;
        } else {
            return false;
        }
    }
}