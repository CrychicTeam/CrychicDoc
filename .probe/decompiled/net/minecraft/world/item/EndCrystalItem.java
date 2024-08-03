package net.minecraft.world.item;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.end.EndDragonFight;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;

public class EndCrystalItem extends Item {

    public EndCrystalItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Level $$1 = useOnContext0.getLevel();
        BlockPos $$2 = useOnContext0.getClickedPos();
        BlockState $$3 = $$1.getBlockState($$2);
        if (!$$3.m_60713_(Blocks.OBSIDIAN) && !$$3.m_60713_(Blocks.BEDROCK)) {
            return InteractionResult.FAIL;
        } else {
            BlockPos $$4 = $$2.above();
            if (!$$1.m_46859_($$4)) {
                return InteractionResult.FAIL;
            } else {
                double $$5 = (double) $$4.m_123341_();
                double $$6 = (double) $$4.m_123342_();
                double $$7 = (double) $$4.m_123343_();
                List<Entity> $$8 = $$1.m_45933_(null, new AABB($$5, $$6, $$7, $$5 + 1.0, $$6 + 2.0, $$7 + 1.0));
                if (!$$8.isEmpty()) {
                    return InteractionResult.FAIL;
                } else {
                    if ($$1 instanceof ServerLevel) {
                        EndCrystal $$9 = new EndCrystal($$1, $$5 + 0.5, $$6, $$7 + 0.5);
                        $$9.setShowBottom(false);
                        $$1.m_7967_($$9);
                        $$1.m_142346_(useOnContext0.getPlayer(), GameEvent.ENTITY_PLACE, $$4);
                        EndDragonFight $$10 = ((ServerLevel) $$1).getDragonFight();
                        if ($$10 != null) {
                            $$10.tryRespawn();
                        }
                    }
                    useOnContext0.getItemInHand().shrink(1);
                    return InteractionResult.sidedSuccess($$1.isClientSide);
                }
            }
        }
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }
}