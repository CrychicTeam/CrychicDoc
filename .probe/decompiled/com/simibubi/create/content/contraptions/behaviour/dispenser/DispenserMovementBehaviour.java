package com.simibubi.create.content.contraptions.behaviour.dispenser;

import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.mixin.accessor.DispenserBlockAccessor;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class DispenserMovementBehaviour extends DropperMovementBehaviour {

    private static final HashMap<Item, IMovedDispenseItemBehaviour> MOVED_DISPENSE_ITEM_BEHAVIOURS = new HashMap();

    private static final HashMap<Item, IMovedDispenseItemBehaviour> MOVED_PROJECTILE_DISPENSE_BEHAVIOURS = new HashMap();

    private static boolean spawnEggsRegistered = false;

    public static void gatherMovedDispenseItemBehaviours() {
        IMovedDispenseItemBehaviour.init();
    }

    public static void registerMovedDispenseItemBehaviour(Item item, IMovedDispenseItemBehaviour movedDispenseItemBehaviour) {
        MOVED_DISPENSE_ITEM_BEHAVIOURS.put(item, movedDispenseItemBehaviour);
    }

    public static DispenseItemBehavior getDispenseMethod(ItemStack itemstack) {
        return ((DispenserBlockAccessor) Blocks.DISPENSER).create$callGetDispenseMethod(itemstack);
    }

    @Override
    protected void activate(MovementContext context, BlockPos pos) {
        if (!spawnEggsRegistered) {
            spawnEggsRegistered = true;
            IMovedDispenseItemBehaviour.initSpawnEggs();
        }
        DispenseItemLocation location = this.getDispenseLocation(context);
        if (location.isEmpty()) {
            context.world.m_46796_(1001, pos, 0);
        } else {
            ItemStack itemStack = this.getItemStackAt(location, context);
            if (MOVED_DISPENSE_ITEM_BEHAVIOURS.containsKey(itemStack.getItem())) {
                this.setItemStackAt(location, ((IMovedDispenseItemBehaviour) MOVED_DISPENSE_ITEM_BEHAVIOURS.get(itemStack.getItem())).dispense(itemStack, context, pos), context);
                return;
            }
            ItemStack backup = itemStack.copy();
            try {
                if (MOVED_PROJECTILE_DISPENSE_BEHAVIOURS.containsKey(itemStack.getItem())) {
                    this.setItemStackAt(location, ((IMovedDispenseItemBehaviour) MOVED_PROJECTILE_DISPENSE_BEHAVIOURS.get(itemStack.getItem())).dispense(itemStack, context, pos), context);
                    return;
                }
                DispenseItemBehavior behavior = getDispenseMethod(itemStack);
                if (behavior instanceof AbstractProjectileDispenseBehavior) {
                    IMovedDispenseItemBehaviour movedBehaviour = MovedProjectileDispenserBehaviour.of((AbstractProjectileDispenseBehavior) behavior);
                    this.setItemStackAt(location, movedBehaviour.dispense(itemStack, context, pos), context);
                    MOVED_PROJECTILE_DISPENSE_BEHAVIOURS.put(itemStack.getItem(), movedBehaviour);
                    return;
                }
                Vec3 facingVec = Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(DispenserBlock.FACING)).getNormal());
                facingVec = (Vec3) context.rotation.apply(facingVec);
                facingVec.normalize();
                Direction clostestFacing = Direction.getNearest(facingVec.x, facingVec.y, facingVec.z);
                ContraptionBlockSource blockSource = new ContraptionBlockSource(context, pos, clostestFacing);
                if (behavior.getClass() != DefaultDispenseItemBehavior.class) {
                    this.setItemStackAt(location, behavior.dispense(blockSource, itemStack), context);
                    return;
                }
            } catch (NullPointerException var10) {
                itemStack = backup;
            }
            this.setItemStackAt(location, DEFAULT_BEHAVIOUR.dispense(itemStack, context, pos), context);
        }
    }
}