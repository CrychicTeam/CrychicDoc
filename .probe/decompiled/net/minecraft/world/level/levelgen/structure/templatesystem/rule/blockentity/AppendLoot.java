package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;

public class AppendLoot implements RuleBlockEntityModifier {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<AppendLoot> CODEC = RecordCodecBuilder.create(p_277957_ -> p_277957_.group(ResourceLocation.CODEC.fieldOf("loot_table").forGetter(p_277581_ -> p_277581_.lootTable)).apply(p_277957_, AppendLoot::new));

    private final ResourceLocation lootTable;

    public AppendLoot(ResourceLocation resourceLocation0) {
        this.lootTable = resourceLocation0;
    }

    @Override
    public CompoundTag apply(RandomSource randomSource0, @Nullable CompoundTag compoundTag1) {
        CompoundTag $$2 = compoundTag1 == null ? new CompoundTag() : compoundTag1.copy();
        ResourceLocation.CODEC.encodeStart(NbtOps.INSTANCE, this.lootTable).resultOrPartial(LOGGER::error).ifPresent(p_277353_ -> $$2.put("LootTable", p_277353_));
        $$2.putLong("LootTableSeed", randomSource0.nextLong());
        return $$2;
    }

    @Override
    public RuleBlockEntityModifierType<?> getType() {
        return RuleBlockEntityModifierType.APPEND_LOOT;
    }
}