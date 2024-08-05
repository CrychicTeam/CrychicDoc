package harmonised.pmmo.features.loot_modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import harmonised.pmmo.config.Config;
import harmonised.pmmo.core.Core;
import harmonised.pmmo.util.MsLoggy;
import harmonised.pmmo.util.RegistryUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class RareDropModifier extends LootModifier {

    public static final Codec<RareDropModifier> CODEC = RecordCodecBuilder.create(instance -> codecStart(instance).and(instance.group(ResourceLocation.CODEC.fieldOf("item").forGetter(tlm -> RegistryUtil.getId(tlm.drop)), Codec.INT.fieldOf("count").forGetter(tlm -> tlm.drop.getCount()), Codec.DOUBLE.fieldOf("chance").forGetter(tlm -> tlm.chance), Codec.BOOL.optionalFieldOf("per_level").forGetter(tlm -> Optional.of(tlm.perLevel)), Codec.STRING.optionalFieldOf("skill").forGetter(tlm -> Optional.of(tlm.skill)))).apply(instance, RareDropModifier::new));

    public ItemStack drop;

    public double chance;

    public boolean perLevel;

    public String skill;

    public RareDropModifier(LootItemCondition[] conditionsIn, ResourceLocation lootItemID, int count, double chance) {
        this(conditionsIn, lootItemID, count, chance, Optional.of(false), Optional.empty());
    }

    public RareDropModifier(LootItemCondition[] conditionsIn, ResourceLocation lootItemID, int count, double chance, Optional<Boolean> perLevel, Optional<String> skill) {
        super(conditionsIn);
        this.chance = chance;
        this.drop = new ItemStack(ForgeRegistries.ITEMS.getValue(lootItemID));
        this.drop.setCount(count);
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
            double rand = MsLoggy.DEBUG.logAndReturn(context.getRandom().nextDouble(), MsLoggy.LOG_CODE.FEATURE, "Rand: {} as test for " + this.drop.serializeNBT().toString());
            if (rand <= this.chance) {
                generatedLoot.add(this.drop.copy());
            }
            return generatedLoot;
        }
    }
}