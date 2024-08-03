package com.craisinlord.integrated_api.world.placements;

import com.craisinlord.integrated_api.modinit.IAPlacements;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

public class UnlimitedCountPlacement extends RepeatingPlacement {

    public static final Codec<UnlimitedCountPlacement> CODEC = IntProvider.NON_NEGATIVE_CODEC.fieldOf("count").xmap(UnlimitedCountPlacement::new, countPlacement -> countPlacement.count).codec();

    private final IntProvider count;

    private UnlimitedCountPlacement(IntProvider intProvider) {
        this.count = intProvider;
    }

    public static UnlimitedCountPlacement of(IntProvider intProvider) {
        return new UnlimitedCountPlacement(intProvider);
    }

    public static UnlimitedCountPlacement of(int i) {
        return of(ConstantInt.of(i));
    }

    @Override
    protected int count(RandomSource random, BlockPos blockPos) {
        return this.count.sample(random);
    }

    @Override
    public PlacementModifierType<?> type() {
        return IAPlacements.UNLIMITED_COUNT.get();
    }
}