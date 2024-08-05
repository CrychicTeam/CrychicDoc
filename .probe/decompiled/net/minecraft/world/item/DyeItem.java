package net.minecraft.world.item;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class DyeItem extends Item implements SignApplicator {

    private static final Map<DyeColor, DyeItem> ITEM_BY_COLOR = Maps.newEnumMap(DyeColor.class);

    private final DyeColor dyeColor;

    public DyeItem(DyeColor dyeColor0, Item.Properties itemProperties1) {
        super(itemProperties1);
        this.dyeColor = dyeColor0;
        ITEM_BY_COLOR.put(dyeColor0, this);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack0, Player player1, LivingEntity livingEntity2, InteractionHand interactionHand3) {
        if (livingEntity2 instanceof Sheep $$4 && $$4.m_6084_() && !$$4.isSheared() && $$4.getColor() != this.dyeColor) {
            $$4.m_9236_().playSound(player1, $$4, SoundEvents.DYE_USE, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!player1.m_9236_().isClientSide) {
                $$4.setColor(this.dyeColor);
                itemStack0.shrink(1);
            }
            return InteractionResult.sidedSuccess(player1.m_9236_().isClientSide);
        }
        return InteractionResult.PASS;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public static DyeItem byColor(DyeColor dyeColor0) {
        return (DyeItem) ITEM_BY_COLOR.get(dyeColor0);
    }

    @Override
    public boolean tryApplyToSign(Level level0, SignBlockEntity signBlockEntity1, boolean boolean2, Player player3) {
        if (signBlockEntity1.updateText(p_277649_ -> p_277649_.setColor(this.getDyeColor()), boolean2)) {
            level0.playSound(null, signBlockEntity1.m_58899_(), SoundEvents.DYE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }
}