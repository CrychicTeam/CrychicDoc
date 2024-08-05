package net.mehvahdjukaar.moonlight.api.util;

import net.mehvahdjukaar.moonlight.core.mixins.accessor.DispenserBlockAccessor;
import net.mehvahdjukaar.moonlight.core.mixins.accessor.DispenserBlockEntityAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;

public class DispenserHelper {

    public static final DefaultDispenseItemBehavior PLACE_BLOCK_BEHAVIOR = new DispenserHelper.PlaceBlockDispenseBehavior();

    private static final DefaultDispenseItemBehavior SHOOT_BEHAVIOR = new DefaultDispenseItemBehavior();

    public static void registerCustomBehavior(DispenserHelper.AdditionalDispenserBehavior behavior) {
        DispenserBlock.registerBehavior(behavior.item, behavior);
    }

    public static void registerPlaceBlockBehavior(ItemLike block) {
        DispenserBlock.registerBehavior(block, PLACE_BLOCK_BEHAVIOR);
    }

    public static class AddItemToInventoryBehavior extends DispenserHelper.AdditionalDispenserBehavior {

        public AddItemToInventoryBehavior(Item item) {
            super(item);
        }

        @Override
        protected InteractionResultHolder<ItemStack> customBehavior(BlockSource source, ItemStack stack) {
            ServerLevel world = source.getLevel();
            BlockPos blockpos = source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
            if (world.m_7702_(blockpos) instanceof WorldlyContainer tile) {
                if (tile.m_7013_(0, stack)) {
                    if (tile.m_7983_()) {
                        tile.m_6836_(0, stack.split(1));
                    } else {
                        tile.m_8020_(0).grow(1);
                        stack.shrink(1);
                    }
                    return InteractionResultHolder.success(stack);
                } else {
                    return InteractionResultHolder.fail(stack);
                }
            } else {
                return InteractionResultHolder.pass(stack);
            }
        }
    }

    public abstract static class AdditionalDispenserBehavior implements DispenseItemBehavior {

        private final DispenseItemBehavior fallback;

        private final Item item;

        protected AdditionalDispenserBehavior(Item item) {
            this.item = item;
            this.fallback = (DispenseItemBehavior) DispenserBlockAccessor.getDispenserRegistry().get(item);
        }

        @Override
        public final ItemStack dispense(BlockSource source, ItemStack stack) {
            try {
                InteractionResultHolder<ItemStack> result = this.customBehavior(source, stack);
                InteractionResult type = result.getResult();
                if (type != InteractionResult.PASS) {
                    boolean success = type.consumesAction();
                    this.playSound(source, success);
                    this.playAnimation(source, (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING));
                    if (success) {
                        ItemStack resultStack = result.getObject();
                        if (resultStack.getItem() == stack.getItem()) {
                            return resultStack;
                        }
                        return this.fillItemInDispenser(source, stack, result.getObject());
                    }
                }
            } catch (Exception var7) {
            }
            return this.fallback.dispense(source, stack);
        }

        protected abstract InteractionResultHolder<ItemStack> customBehavior(BlockSource var1, ItemStack var2);

        protected void playSound(BlockSource source, boolean success) {
            source.getLevel().m_46796_(success ? 1000 : 1001, source.getPos(), 0);
        }

        protected void playAnimation(BlockSource source, Direction direction) {
            source.getLevel().m_46796_(2000, source.getPos(), direction.get3DDataValue());
        }

        private ItemStack fillItemInDispenser(BlockSource source, ItemStack empty, ItemStack filled) {
            empty.shrink(1);
            if (empty.isEmpty()) {
                return filled.copy();
            } else {
                if (!this.mergeDispenserItem(source.getEntity(), filled)) {
                    DispenserHelper.SHOOT_BEHAVIOR.dispense(source, filled.copy());
                }
                return empty;
            }
        }

        private boolean mergeDispenserItem(DispenserBlockEntity te, ItemStack filled) {
            NonNullList<ItemStack> stacks = ((DispenserBlockEntityAccessor) te).getItems();
            for (int i = 0; i < te.getContainerSize(); i++) {
                ItemStack s = stacks.get(i);
                if (s.isEmpty() || s.getItem() == filled.getItem() && s.getMaxStackSize() > s.getCount()) {
                    filled.grow(s.getCount());
                    te.m_6836_(i, filled);
                    return true;
                }
            }
            return false;
        }
    }

    public static class PlaceBlockDispenseBehavior extends OptionalDispenseItemBehavior {

        @Override
        public ItemStack execute(BlockSource source, ItemStack stack) {
            this.m_123573_(false);
            if (stack.getItem() instanceof BlockItem bi) {
                Direction direction = (Direction) source.getBlockState().m_61143_(DispenserBlock.FACING);
                BlockPos blockpos = source.getPos().relative(direction);
                InteractionResult result = bi.place(new DirectionalPlaceContext(source.getLevel(), blockpos, direction, stack, direction));
                this.m_123573_(result.consumesAction());
            }
            return stack;
        }
    }
}