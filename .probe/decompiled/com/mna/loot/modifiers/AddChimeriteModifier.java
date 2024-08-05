package com.mna.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.ItemInit;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.function.Supplier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class AddChimeriteModifier extends LootModifier {

    public static final Supplier<Codec<AddChimeriteModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(inst -> codecStart(inst).and(Codec.INT.fieldOf("numChimerite").forGetter(m -> m.numChimerite)).and(Codec.INT.fieldOf("magicLevel").forGetter(m -> m.magicLevel)).and(Codec.FLOAT.fieldOf("chance").forGetter(m -> m.chance)).and(Codec.FLOAT.fieldOf("fortuneBonusChance").forGetter(m -> m.fortuneBonusChance)).and(ForgeRegistries.ITEMS.getCodec().fieldOf("dropsWith").forGetter(m -> m.dropsWith)).apply(inst, AddChimeriteModifier::new)));

    private final Item dropsWith;

    private final int numChimerite;

    private final int magicLevel;

    private final float chance;

    private final float fortuneBonusChance;

    public AddChimeriteModifier(LootItemCondition[] conditions, int numChimerite, int magicLevel, float chance, float fortuneBonusChance, Item dropsWith) {
        super(conditions);
        this.numChimerite = numChimerite;
        this.magicLevel = magicLevel;
        this.chance = chance;
        this.fortuneBonusChance = fortuneBonusChance;
        this.dropsWith = dropsWith;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!generatedLoot.stream().filter(i -> i.getItem() == this.dropsWith).findFirst().isPresent()) {
            return generatedLoot;
        } else {
            Entity e = context.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (e instanceof Player) {
                ItemStack toolStack = context.getParamOrNull(LootContextParams.TOOL);
                int fortuneLevel = 0;
                if (toolStack != null && !toolStack.isEmpty()) {
                    fortuneLevel = toolStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                }
                float bonus = (float) fortuneLevel * this.fortuneBonusChance;
                ((Player) e).getCapability(PlayerMagicProvider.MAGIC).ifPresent(magic -> {
                    if (magic.getMagicLevel() >= this.magicLevel && Math.random() <= (double) (this.chance + bonus)) {
                        generatedLoot.add(new ItemStack(ItemInit.CHIMERITE_GEM.get(), (int) Math.ceil(Math.random() * (double) this.numChimerite * (double) (bonus + 1.0F))));
                    }
                });
            }
            return generatedLoot;
        }
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return (Codec<? extends IGlobalLootModifier>) CODEC.get();
    }
}