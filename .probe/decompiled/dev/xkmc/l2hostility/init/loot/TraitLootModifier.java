package dev.xkmc.l2hostility.init.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.l2hostility.compat.jei.ITraitLootRecipe;
import dev.xkmc.l2hostility.content.capability.mob.MobTraitCap;
import dev.xkmc.l2hostility.content.capability.player.PlayerDifficulty;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.util.code.GenericItemStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TraitLootModifier extends LootModifier implements ITraitLootRecipe {

    public static final Codec<TraitLootModifier> CODEC = RecordCodecBuilder.create(i -> codecStart(i).and(i.group(LHTraits.TRAITS.get().getCodec().optionalFieldOf("trait").forGetter(e -> Optional.ofNullable(e.trait)), Codec.DOUBLE.fieldOf("chance").forGetter(e -> e.chance), Codec.DOUBLE.fieldOf("rankBonus").forGetter(e -> e.rankBonus), ItemStack.CODEC.fieldOf("result").forGetter(e -> e.result))).apply(i, TraitLootModifier::new));

    @Nullable
    public final MobTrait trait;

    public final double chance;

    public final double rankBonus;

    public final ItemStack result;

    public TraitLootModifier(MobTrait trait, double chance, double rankBonus, ItemStack result, LootItemCondition... conditionsIn) {
        super(conditionsIn);
        this.trait = trait;
        this.chance = chance;
        this.rankBonus = rankBonus;
        this.result = result;
    }

    private TraitLootModifier(LootItemCondition[] conditionsIn, Optional<MobTrait> trait, double chance, double rankBonus, ItemStack result) {
        super(conditionsIn);
        this.trait = (MobTrait) trait.orElse(null);
        this.chance = chance;
        this.rankBonus = rankBonus;
        this.result = result;
    }

    @NotNull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> list, LootContext context) {
        if (context.getParam(LootContextParams.THIS_ENTITY) instanceof LivingEntity le && MobTraitCap.HOLDER.isProper(le)) {
            MobTraitCap cap = (MobTraitCap) MobTraitCap.HOLDER.get(le);
            if (this.trait == null || cap.hasTrait(this.trait)) {
                double factor = cap.dropRate;
                if (context.hasParam(LootContextParams.LAST_DAMAGE_PLAYER)) {
                    Player player = context.getParam(LootContextParams.LAST_DAMAGE_PLAYER);
                    PlayerDifficulty pl = PlayerDifficulty.HOLDER.get(player);
                    for (GenericItemStack<CurseCurioItem> stack : CurseCurioItem.getFromPlayer(player)) {
                        factor *= stack.item().getLootFactor(stack.stack(), pl, cap);
                    }
                }
                int lv = this.trait == null ? 0 : cap.getTraitLevel(this.trait);
                double rate = this.chance + (double) lv * this.rankBonus;
                int count = 0;
                for (int i = 0; (double) i < (double) this.result.getCount() * factor; i++) {
                    if (context.getRandom().nextDouble() < rate) {
                        count++;
                    }
                }
                if (count > 0) {
                    ItemStack ans = this.result.copy();
                    ans.setCount(count);
                    list.add(ans);
                }
            }
        }
        return list;
    }

    @Override
    public Codec<TraitLootModifier> codec() {
        return CODEC;
    }

    public LootItemCondition[] getConditions() {
        return this.conditions;
    }

    @Override
    public List<ItemStack> getResults() {
        return List.of(this.result);
    }

    @Override
    public List<ItemStack> getCurioRequired() {
        List<ItemStack> ans = new ArrayList();
        if (LHConfig.COMMON.disableHostilityLootCurioRequirement.get()) {
            return ans;
        } else {
            for (LootItemCondition c : this.getConditions()) {
                if (c instanceof PlayerHasItemCondition item) {
                    ans.add(item.item.getDefaultInstance());
                }
            }
            return ans;
        }
    }

    @Override
    public List<ItemStack> getInputs() {
        Set<MobTrait> set = new LinkedHashSet();
        List<ItemStack> ans = new ArrayList();
        if (this.trait != null) {
            set.add(this.trait);
        }
        for (LootItemCondition c : this.getConditions()) {
            if (c instanceof TraitLootCondition cl) {
                set.add(cl.trait);
            }
        }
        for (MobTrait e : set) {
            ans.add(e.asItem().getDefaultInstance());
        }
        return ans;
    }

    @Override
    public void addTooltip(List<Component> list) {
        int max = this.trait == null ? 0 : this.trait.getConfig().max_rank;
        int min = 1;
        int minLevel = 0;
        List<TraitLootCondition> other = new ArrayList();
        List<PlayerHasItemCondition> itemReq = new ArrayList();
        List<MobHealthCondition> health = new ArrayList();
        for (LootItemCondition c : this.getConditions()) {
            if (c instanceof TraitLootCondition) {
                TraitLootCondition cl = (TraitLootCondition) c;
                if (cl.trait == this.trait) {
                    max = Math.min(max, cl.maxLevel);
                    min = Math.max(min, cl.minLevel);
                } else {
                    other.add(cl);
                }
            } else if (c instanceof MobCapLootCondition cl) {
                minLevel = cl.minLevel;
            } else if (c instanceof PlayerHasItemCondition cl) {
                itemReq.add(cl);
            } else if (c instanceof MobHealthCondition cl) {
                health.add(cl);
            }
        }
        if (minLevel > 0) {
            list.add(LangData.LOOT_MIN_LEVEL.get(Component.literal(minLevel + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        for (MobHealthCondition e : health) {
            list.add(LangData.LOOT_MIN_HEALTH.get(Component.literal(e.minHealth + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.LIGHT_PURPLE));
        }
        if (this.trait != null) {
            for (int lv = min; lv <= max; lv++) {
                list.add(LangData.LOOT_CHANCE.get(Component.literal(Math.round((this.chance + this.rankBonus * (double) lv) * 100.0) + "%").withStyle(ChatFormatting.AQUA), this.trait.getDesc().withStyle(ChatFormatting.GOLD), Component.literal(lv + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
            }
        } else {
            list.add(LangData.LOOT_NO_TRAIT.get(Component.literal(Math.round(this.chance * 100.0) + "%").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        }
        for (TraitLootCondition cx : other) {
            int cmin = Math.max(cx.minLevel, 1);
            int cmax = Math.min(cx.maxLevel, cx.trait.getMaxLevel());
            String str = cmax == cmin ? cmin + "" : (cmax >= cx.trait.getMaxLevel() ? cmin + "+" : cmin + "-" + cmax);
            list.add(LangData.LOOT_OTHER_TRAIT.get(cx.trait.getDesc().withStyle(ChatFormatting.GOLD), Component.literal(str).withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.RED));
        }
        for (PlayerHasItemCondition e : itemReq) {
            MutableComponent name = e.item.getDescription().copy().withStyle(ChatFormatting.LIGHT_PURPLE);
            list.add(LangData.TOOLTIP_JEI_REQUIRED.get(name).withStyle(ChatFormatting.YELLOW));
        }
    }
}