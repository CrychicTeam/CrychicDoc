package net.minecraft.world.level.block;

import com.mojang.authlib.GameProfile;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerHeadBlock extends SkullBlock {

    protected PlayerHeadBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(SkullBlock.Types.PLAYER, blockBehaviourProperties0);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        super.m_6402_(level0, blockPos1, blockState2, livingEntity3, itemStack4);
        if (level0.getBlockEntity(blockPos1) instanceof SkullBlockEntity $$6) {
            GameProfile $$7 = null;
            if (itemStack4.hasTag()) {
                CompoundTag $$8 = itemStack4.getTag();
                if ($$8.contains("SkullOwner", 10)) {
                    $$7 = NbtUtils.readGameProfile($$8.getCompound("SkullOwner"));
                } else if ($$8.contains("SkullOwner", 8) && !Util.isBlank($$8.getString("SkullOwner"))) {
                    $$7 = new GameProfile(null, $$8.getString("SkullOwner"));
                }
            }
            $$6.setOwner($$7);
        }
    }
}