package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.controller.ArrowFeatureController;
import dev.xkmc.l2archery.content.controller.BowFeatureController;
import dev.xkmc.l2archery.content.enchantment.IBowEnchantment;
import dev.xkmc.l2archery.content.energy.IFluxItem;
import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.BowArrowFeature;
import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.feature.bow.FluxFeature;
import dev.xkmc.l2archery.content.feature.bow.IGlowFeature;
import dev.xkmc.l2archery.content.feature.bow.InfinityFeature;
import dev.xkmc.l2archery.content.feature.bow.WindBowFeature;
import dev.xkmc.l2archery.content.feature.core.CompoundBowConfig;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.content.feature.core.StatFeature;
import dev.xkmc.l2archery.content.upgrade.Upgrade;
import dev.xkmc.l2archery.init.data.LangData;
import dev.xkmc.l2archery.init.registrate.ArcheryEffects;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import dev.xkmc.l2archery.init.registrate.ArcheryRegister;
import dev.xkmc.l2archery.mixin.AbstractArrowAccessor;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2library.util.code.GenericItemStack;
import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2library.util.raytrace.FastItem;
import dev.xkmc.l2library.util.raytrace.IGlowingTarget;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public class GenericBowItem extends BowItem implements FastItem, IGlowingTarget, IFluxItem {

    private static final HashSet<Class<?>> BLACKLIST = new HashSet();

    public static final String KEY = "upgrades";

    public final BowConfig config;

    private boolean arrowIsInfinite(ArrowItem item, ItemStack arrow, ItemStack bow) {
        if (BLACKLIST.contains(item.getClass())) {
            return false;
        } else {
            try {
                return item.isInfinite(arrow, bow, null);
            } catch (NullPointerException var5) {
                BLACKLIST.add(item.getClass());
                return false;
            }
        }
    }

    public static List<Upgrade> getUpgrades(ItemStack stack) {
        List<Upgrade> ans = new ArrayList();
        if (stack.getOrCreateTag().contains("upgrades")) {
            ListTag list = ItemCompoundTag.of(stack).getSubList("upgrades", 8).getOrCreate();
            for (int i = 0; i < list.size(); i++) {
                Upgrade up = ArcheryRegister.UPGRADE.get().getValue(new ResourceLocation(list.getString(i)));
                if (up != null) {
                    ans.add(up);
                }
            }
        }
        return ans;
    }

    public static void addUpgrade(ItemStack result, Upgrade upgrade) {
        List<Upgrade> list = getUpgrades(result);
        list.add(upgrade);
        ListTag tag = new ListTag();
        for (Upgrade up : list) {
            tag.add(StringTag.valueOf(up.getID()));
        }
        ItemCompoundTag.of(result).getSubList("upgrades", 8).setTag(tag);
    }

    public GenericBowItem(Item.Properties properties, Function<GenericBowItem, BowConfig> config) {
        super(properties);
        this.config = (BowConfig) config.apply(this);
        ArcheryItems.BOW_LIKE.add(this);
    }

    @Override
    public void releaseUsing(ItemStack bow, Level level, LivingEntity user, int remaining_pull_time) {
        if (user instanceof Player player) {
            Optional<AbstractArrow> arrow = this.releaseUsingAndShootArrow(bow, level, player, remaining_pull_time);
            arrow.ifPresent(level::m_7967_);
        }
    }

    public Optional<AbstractArrow> releaseUsingAndShootArrow(ItemStack bow, Level level, LivingEntity user, int remaining_pull_time) {
        boolean var10000;
        label92: {
            if (user instanceof Player pl && pl.getAbilities().instabuild) {
                var10000 = true;
                break label92;
            }
            var10000 = false;
        }
        boolean instabuild = var10000;
        BowFeatureController.stopUsing(user, new GenericItemStack<>(this, bow));
        boolean has_inf = instabuild || bow.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) > 0;
        ItemStack arrow = user.getProjectile(bow);
        int pull_time = this.getUseDuration(bow) - remaining_pull_time;
        if (user instanceof Player player) {
            pull_time = ForgeEventFactory.onArrowLoose(bow, level, player, pull_time, !arrow.isEmpty() || has_inf);
        }
        if (pull_time < 0) {
            return Optional.empty();
        } else if (arrow.isEmpty() && !has_inf) {
            return Optional.empty();
        } else {
            if (arrow.isEmpty()) {
                arrow = new ItemStack(Items.ARROW);
            }
            float power = this.getPowerForTime(user, (float) pull_time);
            if ((double) power < 0.1) {
                return Optional.empty();
            } else {
                boolean no_consume = instabuild;
                if (arrow.getItem() instanceof ArrowItem arrowItem) {
                    if (user instanceof Player player) {
                        no_consume = instabuild | arrowItem.isInfinite(arrow, bow, player);
                    } else {
                        no_consume = this.arrowIsInfinite(arrowItem, arrow, bow);
                    }
                    if (arrow.is(Items.TIPPED_ARROW) || arrow.is(Items.SPECTRAL_ARROW)) {
                        no_consume |= InfinityFeature.getLevel(this.getFeatures(bow)) >= 2;
                    }
                }
                Optional<AbstractArrow> arrowOpt = Optional.empty();
                if (!level.isClientSide) {
                    arrowOpt = this.shootArrowOnServer(user, level, bow, arrow, power, no_consume);
                    if (arrowOpt.isEmpty()) {
                        return Optional.empty();
                    }
                }
                float pitch = 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F;
                level.playSound(null, user.m_20185_(), user.m_20186_(), user.m_20189_(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, pitch);
                if (!no_consume && !level.isClientSide) {
                    arrow.shrink(1);
                    if (arrow.isEmpty() && user instanceof Player player) {
                        player.getInventory().removeItem(arrow);
                    }
                }
                if (user instanceof Player player) {
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
                return arrowOpt;
            }
        }
    }

    private Optional<AbstractArrow> shootArrowOnServer(LivingEntity player, Level level, ItemStack bow, ItemStack arrow, float power, boolean no_consume) {
        ArrowData data = this.parseArrow(arrow);
        AbstractArrow abstractarrow;
        if (data != null) {
            abstractarrow = ArrowFeatureController.createArrowEntity(new ArrowFeatureController.BowArrowUseContext(level, player, no_consume, power), BowData.of(this, bow), data);
        } else {
            ArrowItem arrowitem = (ArrowItem) (arrow.getItem() instanceof ArrowItem ? arrow.getItem() : Items.ARROW);
            AbstractArrow abstractarrowx = arrowitem.createArrow(level, arrow, player);
            abstractarrow = this.customArrow(abstractarrowx);
            abstractarrow.m_37251_(player, player.m_146909_(), player.m_146908_(), 0.0F, power * 3.0F, 1.0F);
            if (power == 1.0F) {
                abstractarrow.setCritArrow(true);
            }
            int j = bow.getEnchantmentLevel(Enchantments.POWER_ARROWS);
            if (j > 0) {
                abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() + (double) j * 0.5 + 0.5);
            }
            int k = bow.getEnchantmentLevel(Enchantments.PUNCH_ARROWS);
            if (k > 0) {
                abstractarrow.setKnockback(k);
            }
            if (bow.getEnchantmentLevel(Enchantments.FLAMING_ARROWS) > 0) {
                abstractarrow.m_20254_(100);
            }
            if (no_consume) {
                abstractarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
        }
        if (abstractarrow == null) {
            return Optional.empty();
        } else {
            bow.hurtAndBreak(1, player, pl -> pl.broadcastBreakEvent(player.getUsedItemHand()));
            return Optional.of(abstractarrow);
        }
    }

    public float getPullForTime(LivingEntity entity, float time) {
        float f = time / (float) this.config.pull_time();
        MobEffectInstance ins = entity.getEffect((MobEffect) ArcheryEffects.QUICK_PULL.get());
        if (ins != null) {
            f = (float) ((double) f * (1.5 + 0.5 * (double) ins.getAmplifier()));
        }
        return Math.min(1.0F, f);
    }

    public float getPowerForTime(LivingEntity entity, float time) {
        float f = this.getPullForTime(entity, time);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }
        return Math.min(1.0F, f);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public void onUseTick(Level level, LivingEntity user, ItemStack stack, int count) {
        if (user instanceof Player player) {
            BowFeatureController.usingTick(player, new GenericItemStack<>(this, stack));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        boolean flag = !player.getProjectile(itemstack).isEmpty();
        InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(itemstack, level, player, hand, flag);
        if (ret != null) {
            return ret;
        } else if (!player.getAbilities().instabuild && !flag) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.m_6672_(hand);
            BowFeatureController.startUsing(player, new GenericItemStack<>(this, itemstack));
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> {
            if (!stack.is(Items.ARROW) && !stack.is(Items.SPECTRAL_ARROW) && !stack.is(Items.TIPPED_ARROW)) {
                return stack.getItem() instanceof GenericArrowItem arrow ? ArrowFeatureController.canBowUseArrow(this, new GenericItemStack<>(arrow, stack)) : false;
            } else {
                return true;
            }
        };
    }

    @Nullable
    public ArrowData parseArrow(ItemStack arrow) {
        ArrowData data = null;
        if (arrow.getItem() instanceof GenericArrowItem gen) {
            data = ArrowData.of(gen);
        } else if (arrow.is(Items.ARROW)) {
            data = ArrowData.of(arrow.getItem());
        } else if (arrow.is(Items.SPECTRAL_ARROW)) {
            data = ArrowData.of(arrow.getItem());
        } else if (arrow.is(Items.TIPPED_ARROW)) {
            data = ArrowData.of(arrow.getItem(), arrow);
        }
        return data;
    }

    public AbstractArrow customArrow(AbstractArrow arrow) {
        if (arrow instanceof GenericArrowEntity) {
            return arrow;
        } else {
            ItemStack arrowStack = ((AbstractArrowAccessor) arrow).callGetPickupItem();
            if (arrowStack != null && this.getAllSupportedProjectiles().test(arrowStack)) {
                ArrowData data = this.parseArrow(arrowStack);
                if (data != null && arrow.m_19749_() instanceof LivingEntity user) {
                    Level level = arrow.m_9236_();
                    ItemStack bow = user.getItemInHand(user.getUsedItemHand());
                    if (bow.getItem() == this) {
                        AbstractArrow arrowEntity = ArrowFeatureController.createArrowEntity(new ArrowFeatureController.BowArrowUseContext(level, user, true, 1.0F), BowData.of(this, bow), data);
                        if (arrowEntity != null) {
                            arrowEntity.m_20049_("l2archery:rawShoot");
                            return arrowEntity;
                        }
                    }
                }
            }
            return arrow;
        }
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    public boolean isFast(ItemStack stack) {
        return Proxy.getPlayer().m_21023_((MobEffect) ArcheryEffects.RUN_BOW.get()) ? true : this.config.feature().stream().anyMatch(e -> e instanceof WindBowFeature);
    }

    @Override
    public int getDistance(ItemStack itemStack) {
        for (BowArrowFeature feature : this.getFeatures(itemStack).all()) {
            if (feature instanceof IGlowFeature glow) {
                return glow.range();
            }
        }
        return 0;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        List<Upgrade> ups = getUpgrades(stack);
        IBowConfig config = this.config;
        for (Upgrade up : ups) {
            if (up.getFeature() instanceof StatFeature f) {
                config = new CompoundBowConfig(config, f);
            }
        }
        config.addStatTooltip(list);
        for (Upgrade upx : ups) {
            list.add(Component.translatable(upx.getDescriptionId()).withStyle(ChatFormatting.GOLD));
        }
        FeatureList f = this.getFeatures(stack);
        f.addEffectsTooltip(list);
        f.addTooltip(list);
        this.tooltipDelegate(stack, list);
        list.add(LangData.REMAIN_UPGRADE.get(this.getUpgradeSlot(stack)));
    }

    public FeatureList getFeatures(@Nullable ItemStack stack) {
        FeatureList ans = new FeatureList();
        PotionArrowFeature bow_eff = this.config.getEffects();
        if (bow_eff.instances().size() > 0) {
            ans.add(bow_eff);
        }
        for (BowArrowFeature feature : this.config.feature()) {
            ans.add(feature);
        }
        if (stack != null) {
            ans.stage = FeatureList.Stage.UPGRADE;
            for (Upgrade up : getUpgrades(stack)) {
                BowArrowFeature f = up.getFeature();
                if (!(f instanceof StatFeature)) {
                    ans.add(f);
                }
            }
            ans.stage = FeatureList.Stage.ENCHANT;
            stack.getAllEnchantments().forEach((k, v) -> {
                if (k instanceof IBowEnchantment b) {
                    BowArrowFeature fx = b.getFeature(v);
                    if (!(fx instanceof StatFeature)) {
                        ans.add(fx);
                    }
                }
            });
        }
        return ans;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE ? true : super.canApplyAtEnchantingTable(stack, enchantment);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.values()[this.config.rank()];
    }

    public int getUpgradeSlot(ItemStack stack) {
        return this.config.rank() + this.getEnchantmentLevel(stack, Enchantments.BINDING_CURSE) - getUpgrades(stack).size();
    }

    public static void remakeEnergy(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Energy", 0);
    }

    @Nullable
    @Override
    public FluxFeature getFluxFeature(ItemStack stack) {
        for (Upgrade upgrade : getUpgrades(stack)) {
            BowArrowFeature var5 = upgrade.getFeature();
            if (var5 instanceof FluxFeature) {
                return (FluxFeature) var5;
            }
        }
        return null;
    }

    @Override
    public int getStorageRank(ItemStack stack) {
        return this.config.rank();
    }

    @Override
    public int getConsumptionRank(ItemStack stack) {
        return this.config.rank() + Math.min(4, getUpgrades(stack).size());
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.getFluxFeature(stack) != null || super.m_142522_(stack);
    }
}