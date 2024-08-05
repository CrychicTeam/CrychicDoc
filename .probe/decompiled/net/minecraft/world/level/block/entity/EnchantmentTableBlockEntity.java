package net.minecraft.world.level.block.entity;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class EnchantmentTableBlockEntity extends BlockEntity implements Nameable {

    public int time;

    public float flip;

    public float oFlip;

    public float flipT;

    public float flipA;

    public float open;

    public float oOpen;

    public float rot;

    public float oRot;

    public float tRot;

    private static final RandomSource RANDOM = RandomSource.create();

    private Component name;

    public EnchantmentTableBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.ENCHANTING_TABLE, blockPos0, blockState1);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        if (this.m_8077_()) {
            compoundTag0.putString("CustomName", Component.Serializer.toJson(this.name));
        }
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        if (compoundTag0.contains("CustomName", 8)) {
            this.name = Component.Serializer.fromJson(compoundTag0.getString("CustomName"));
        }
    }

    public static void bookAnimationTick(Level level0, BlockPos blockPos1, BlockState blockState2, EnchantmentTableBlockEntity enchantmentTableBlockEntity3) {
        enchantmentTableBlockEntity3.oOpen = enchantmentTableBlockEntity3.open;
        enchantmentTableBlockEntity3.oRot = enchantmentTableBlockEntity3.rot;
        Player $$4 = level0.m_45924_((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, 3.0, false);
        if ($$4 != null) {
            double $$5 = $$4.m_20185_() - ((double) blockPos1.m_123341_() + 0.5);
            double $$6 = $$4.m_20189_() - ((double) blockPos1.m_123343_() + 0.5);
            enchantmentTableBlockEntity3.tRot = (float) Mth.atan2($$6, $$5);
            enchantmentTableBlockEntity3.open += 0.1F;
            if (enchantmentTableBlockEntity3.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float $$7 = enchantmentTableBlockEntity3.flipT;
                do {
                    enchantmentTableBlockEntity3.flipT = enchantmentTableBlockEntity3.flipT + (float) (RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while ($$7 == enchantmentTableBlockEntity3.flipT);
            }
        } else {
            enchantmentTableBlockEntity3.tRot += 0.02F;
            enchantmentTableBlockEntity3.open -= 0.1F;
        }
        while (enchantmentTableBlockEntity3.rot >= (float) Math.PI) {
            enchantmentTableBlockEntity3.rot -= (float) (Math.PI * 2);
        }
        while (enchantmentTableBlockEntity3.rot < (float) -Math.PI) {
            enchantmentTableBlockEntity3.rot += (float) (Math.PI * 2);
        }
        while (enchantmentTableBlockEntity3.tRot >= (float) Math.PI) {
            enchantmentTableBlockEntity3.tRot -= (float) (Math.PI * 2);
        }
        while (enchantmentTableBlockEntity3.tRot < (float) -Math.PI) {
            enchantmentTableBlockEntity3.tRot += (float) (Math.PI * 2);
        }
        float $$8 = enchantmentTableBlockEntity3.tRot - enchantmentTableBlockEntity3.rot;
        while ($$8 >= (float) Math.PI) {
            $$8 -= (float) (Math.PI * 2);
        }
        while ($$8 < (float) -Math.PI) {
            $$8 += (float) (Math.PI * 2);
        }
        enchantmentTableBlockEntity3.rot += $$8 * 0.4F;
        enchantmentTableBlockEntity3.open = Mth.clamp(enchantmentTableBlockEntity3.open, 0.0F, 1.0F);
        enchantmentTableBlockEntity3.time++;
        enchantmentTableBlockEntity3.oFlip = enchantmentTableBlockEntity3.flip;
        float $$9 = (enchantmentTableBlockEntity3.flipT - enchantmentTableBlockEntity3.flip) * 0.4F;
        float $$10 = 0.2F;
        $$9 = Mth.clamp($$9, -0.2F, 0.2F);
        enchantmentTableBlockEntity3.flipA = enchantmentTableBlockEntity3.flipA + ($$9 - enchantmentTableBlockEntity3.flipA) * 0.9F;
        enchantmentTableBlockEntity3.flip = enchantmentTableBlockEntity3.flip + enchantmentTableBlockEntity3.flipA;
    }

    @Override
    public Component getName() {
        return (Component) (this.name != null ? this.name : Component.translatable("container.enchant"));
    }

    public void setCustomName(@Nullable Component component0) {
        this.name = component0;
    }

    @Nullable
    @Override
    public Component getCustomName() {
        return this.name;
    }
}