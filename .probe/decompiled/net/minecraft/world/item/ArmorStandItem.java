package net.minecraft.world.item;

import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class ArmorStandItem extends Item {

    public ArmorStandItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext0) {
        Direction $$1 = useOnContext0.getClickedFace();
        if ($$1 == Direction.DOWN) {
            return InteractionResult.FAIL;
        } else {
            Level $$2 = useOnContext0.getLevel();
            BlockPlaceContext $$3 = new BlockPlaceContext(useOnContext0);
            BlockPos $$4 = $$3.getClickedPos();
            ItemStack $$5 = useOnContext0.getItemInHand();
            Vec3 $$6 = Vec3.atBottomCenterOf($$4);
            AABB $$7 = EntityType.ARMOR_STAND.getDimensions().makeBoundingBox($$6.x(), $$6.y(), $$6.z());
            if ($$2.m_45756_(null, $$7) && $$2.m_45933_(null, $$7).isEmpty()) {
                if ($$2 instanceof ServerLevel $$8) {
                    Consumer<ArmorStand> $$9 = EntityType.createDefaultStackConfig($$8, $$5, useOnContext0.getPlayer());
                    ArmorStand $$10 = EntityType.ARMOR_STAND.create($$8, $$5.getTag(), $$9, $$4, MobSpawnType.SPAWN_EGG, true, true);
                    if ($$10 == null) {
                        return InteractionResult.FAIL;
                    }
                    float $$11 = (float) Mth.floor((Mth.wrapDegrees(useOnContext0.getRotation() - 180.0F) + 22.5F) / 45.0F) * 45.0F;
                    $$10.m_7678_($$10.m_20185_(), $$10.m_20186_(), $$10.m_20189_(), $$11, 0.0F);
                    $$8.m_47205_($$10);
                    $$2.playSound(null, $$10.m_20185_(), $$10.m_20186_(), $$10.m_20189_(), SoundEvents.ARMOR_STAND_PLACE, SoundSource.BLOCKS, 0.75F, 0.8F);
                    $$10.m_146852_(GameEvent.ENTITY_PLACE, useOnContext0.getPlayer());
                }
                $$5.shrink(1);
                return InteractionResult.sidedSuccess($$2.isClientSide);
            } else {
                return InteractionResult.FAIL;
            }
        }
    }
}