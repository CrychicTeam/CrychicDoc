package dev.xkmc.modulargolems.events;

import dev.xkmc.modulargolems.content.item.golem.GolemHolder;
import dev.xkmc.modulargolems.init.registrate.GolemItems;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.phys.Vec3;

public class GolemDispenserBehaviors {

    public static void registerDispenseBehaviors() {
        DispenseItemBehavior behavior = new DefaultDispenseItemBehavior() {

            @Override
            protected ItemStack execute(BlockSource source, ItemStack stack) {
                if (((GolemHolder) stack.getItem()).summon(stack, source.getLevel(), Vec3.atBottomCenterOf(source.getPos().relative((Direction) source.getBlockState().m_61143_(DispenserBlock.FACING))), null, null)) {
                    stack.shrink(1);
                }
                return stack;
            }
        };
        DispenserBlock.registerBehavior((ItemLike) GolemItems.HOLDER_GOLEM.get(), behavior);
        DispenserBlock.registerBehavior((ItemLike) GolemItems.HOLDER_HUMANOID.get(), behavior);
        DispenserBlock.registerBehavior((ItemLike) GolemItems.HOLDER_DOG.get(), behavior);
    }
}