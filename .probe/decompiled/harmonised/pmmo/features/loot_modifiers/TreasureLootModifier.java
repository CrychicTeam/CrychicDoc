package harmonised.pmmo.features.loot_modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.RegistryUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class TreasureLootModifier extends LootModifier {

    public static final Codec<TreasureLootModifier> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance).and(instance.group(ResourceLocation.CODEC.fieldOf("item").forGetter(tlm -> RegistryUtil.getId(tlm.drop)), Codec.INT.fieldOf("count").forGetter(tlm -> tlm.count), Codec.DOUBLE.fieldOf("chance").forGetter(tlm -> tlm.chance), Codec.BOOL.optionalFieldOf("per_level").forGetter(tlm -> Optional.of(tlm.perLevel)), Codec.STRING.optionalFieldOf("skill").forGetter(tlm -> Optional.of(tlm.skill)))).apply(instance, TreasureLootModifier::new));

    public ItemStack drop;

    private final int count;

    public double chance;

    public boolean perLevel;

    public String skill;

    public TreasureLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootItemID, int count, double chance) {
        this(conditionsIn, lootItemID, count, chance, Optional.of(false), Optional.empty());
    }

    public TreasureLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootItemID, int count, double chance, Optional<Boolean> perLevel, Optional<String> skill) {
        super(conditionsIn);
        this.chance = chance;
        this.drop = lootItemID.equals(new ResourceLocation("air")) ? Items.AIR.getDefaultInstance() : new ItemStack(ForgeRegistries.ITEMS.getValue(lootItemID));
        this.drop.setCount(count);
        this.count = count;
        this.perLevel = (Boolean) perLevel.orElse(false);
        this.skill = (String) skill.orElse("");
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (!Config.TREASURE_ENABLED.get()) {
            return generatedLoot;
        } else {
            if (this.perLevel && context.getParam(LootContextParams.THIS_ENTITY) instanceof Player player) {
                this.chance = this.chance * (double) Core.get(player.m_9236_()).getData().getPlayerSkillLevel(this.skill, player.m_20148_());
            }
            if (context.getRandom().nextDouble() <= this.chance) {
                BlockState state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
                if (state != null && this.drop.getItem() == Items.AIR) {
                    this.drop = (ItemStack) state.m_287290_(this.builderFromContext(context)).get(0);
                    this.drop.setCount(this.count);
                }
                Entity breaker = context.getParamOrNull(LootContextParams.THIS_ENTITY);
                if (breaker instanceof Player player) {
                    player.m_213846_(LangProvider.FOUND_TREASURE.asComponent());
                }
                generatedLoot.add(this.drop.copy());
            }
            return generatedLoot;
        }
    }

    private LootParams.Builder builderFromContext(LootContext context) {
        return new LootParams.Builder(context.getLevel()).withParameter(LootContextParams.ORIGIN, context.getParam(LootContextParams.ORIGIN)).withParameter(LootContextParams.TOOL, context.getParam(LootContextParams.TOOL));
    }
}