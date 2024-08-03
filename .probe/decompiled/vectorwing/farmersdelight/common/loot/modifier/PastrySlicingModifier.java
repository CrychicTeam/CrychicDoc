package vectorwing.farmersdelight.common.loot.modifier;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.PieBlock;

public class PastrySlicingModifier extends LootModifier {

    public static final Supplier<Codec<PastrySlicingModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().fieldOf("slice").forGetter(m -> m.pastrySlice)).apply(inst, PastrySlicingModifier::new)));

    public static final int MAX_CAKE_BITES = 7;

    public static final int MAX_PIE_BITES = 4;

    private final Item pastrySlice;

    protected PastrySlicingModifier(LootItemCondition[] conditionsIn, Item pastrySliceIn) {
        super(conditionsIn);
        this.pastrySlice = pastrySliceIn;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (state != null) {
            Block targetBlock = state.m_60734_();
            if (targetBlock instanceof CakeBlock) {
                int bites = (Integer) state.m_61143_(CakeBlock.BITES);
                generatedLoot.add(new ItemStack(this.pastrySlice, 7 - bites));
            } else if (targetBlock instanceof PieBlock) {
                int bites = (Integer) state.m_61143_(PieBlock.BITES);
                generatedLoot.add(new ItemStack(this.pastrySlice, 4 - bites));
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return (Codec<? extends IGlobalLootModifier>) CODEC.get();
    }
}