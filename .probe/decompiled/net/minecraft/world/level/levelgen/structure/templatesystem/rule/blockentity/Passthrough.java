package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

public class Passthrough implements RuleBlockEntityModifier {

    public static final Passthrough INSTANCE = new Passthrough();

    public static final Codec<Passthrough> CODEC = Codec.unit(INSTANCE);

    @Nullable
    @Override
    public CompoundTag apply(RandomSource randomSource0, @Nullable CompoundTag compoundTag1) {
        return compoundTag1;
    }

    @Override
    public RuleBlockEntityModifierType<?> getType() {
        return RuleBlockEntityModifierType.PASSTHROUGH;
    }
}