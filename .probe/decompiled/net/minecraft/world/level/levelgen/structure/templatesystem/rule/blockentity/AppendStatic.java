package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;

public class AppendStatic implements RuleBlockEntityModifier {

    public static final Codec<AppendStatic> CODEC = RecordCodecBuilder.create(p_277505_ -> p_277505_.group(CompoundTag.CODEC.fieldOf("data").forGetter(p_278105_ -> p_278105_.tag)).apply(p_277505_, AppendStatic::new));

    private final CompoundTag tag;

    public AppendStatic(CompoundTag compoundTag0) {
        this.tag = compoundTag0;
    }

    @Override
    public CompoundTag apply(RandomSource randomSource0, @Nullable CompoundTag compoundTag1) {
        return compoundTag1 == null ? this.tag.copy() : compoundTag1.merge(this.tag);
    }

    @Override
    public RuleBlockEntityModifierType<?> getType() {
        return RuleBlockEntityModifierType.APPEND_STATIC;
    }
}