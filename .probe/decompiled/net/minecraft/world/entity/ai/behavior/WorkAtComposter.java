package net.minecraft.world.entity.ai.behavior;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;

public class WorkAtComposter extends WorkAtPoi {

    private static final List<Item> COMPOSTABLE_ITEMS = ImmutableList.of(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);

    @Override
    protected void useWorkstation(ServerLevel serverLevel0, Villager villager1) {
        Optional<GlobalPos> $$2 = villager1.getBrain().getMemory(MemoryModuleType.JOB_SITE);
        if ($$2.isPresent()) {
            GlobalPos $$3 = (GlobalPos) $$2.get();
            BlockState $$4 = serverLevel0.m_8055_($$3.pos());
            if ($$4.m_60713_(Blocks.COMPOSTER)) {
                this.makeBread(villager1);
                this.compostItems(serverLevel0, villager1, $$3, $$4);
            }
        }
    }

    private void compostItems(ServerLevel serverLevel0, Villager villager1, GlobalPos globalPos2, BlockState blockState3) {
        BlockPos $$4 = globalPos2.pos();
        if ((Integer) blockState3.m_61143_(ComposterBlock.LEVEL) == 8) {
            blockState3 = ComposterBlock.extractProduce(villager1, blockState3, serverLevel0, $$4);
        }
        int $$5 = 20;
        int $$6 = 10;
        int[] $$7 = new int[COMPOSTABLE_ITEMS.size()];
        SimpleContainer $$8 = villager1.m_35311_();
        int $$9 = $$8.getContainerSize();
        BlockState $$10 = blockState3;
        for (int $$11 = $$9 - 1; $$11 >= 0 && $$5 > 0; $$11--) {
            ItemStack $$12 = $$8.getItem($$11);
            int $$13 = COMPOSTABLE_ITEMS.indexOf($$12.getItem());
            if ($$13 != -1) {
                int $$14 = $$12.getCount();
                int $$15 = $$7[$$13] + $$14;
                $$7[$$13] = $$15;
                int $$16 = Math.min(Math.min($$15 - 10, $$5), $$14);
                if ($$16 > 0) {
                    $$5 -= $$16;
                    for (int $$17 = 0; $$17 < $$16; $$17++) {
                        $$10 = ComposterBlock.insertItem(villager1, $$10, serverLevel0, $$12, $$4);
                        if ((Integer) $$10.m_61143_(ComposterBlock.LEVEL) == 7) {
                            this.spawnComposterFillEffects(serverLevel0, blockState3, $$4, $$10);
                            return;
                        }
                    }
                }
            }
        }
        this.spawnComposterFillEffects(serverLevel0, blockState3, $$4, $$10);
    }

    private void spawnComposterFillEffects(ServerLevel serverLevel0, BlockState blockState1, BlockPos blockPos2, BlockState blockState3) {
        serverLevel0.m_46796_(1500, blockPos2, blockState3 != blockState1 ? 1 : 0);
    }

    private void makeBread(Villager villager0) {
        SimpleContainer $$1 = villager0.m_35311_();
        if ($$1.m_18947_(Items.BREAD) <= 36) {
            int $$2 = $$1.m_18947_(Items.WHEAT);
            int $$3 = 3;
            int $$4 = 3;
            int $$5 = Math.min(3, $$2 / 3);
            if ($$5 != 0) {
                int $$6 = $$5 * 3;
                $$1.removeItemType(Items.WHEAT, $$6);
                ItemStack $$7 = $$1.addItem(new ItemStack(Items.BREAD, $$5));
                if (!$$7.isEmpty()) {
                    villager0.m_5552_($$7, 0.5F);
                }
            }
        }
    }
}