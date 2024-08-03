package net.minecraft.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class Item implements FeatureElement, ItemLike {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Map<Block, Item> BY_BLOCK = Maps.newHashMap();

    protected static final UUID BASE_ATTACK_DAMAGE_UUID = UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF");

    protected static final UUID BASE_ATTACK_SPEED_UUID = UUID.fromString("FA233E1C-4180-4865-B01B-BCCE9785ACA3");

    public static final int MAX_STACK_SIZE = 64;

    public static final int EAT_DURATION = 32;

    public static final int MAX_BAR_WIDTH = 13;

    private final Holder.Reference<Item> builtInRegistryHolder = BuiltInRegistries.ITEM.m_203693_(this);

    private final Rarity rarity;

    private final int maxStackSize;

    private final int maxDamage;

    private final boolean isFireResistant;

    @Nullable
    private final Item craftingRemainingItem;

    @Nullable
    private String descriptionId;

    @Nullable
    private final FoodProperties foodProperties;

    private final FeatureFlagSet requiredFeatures;

    public static int getId(Item item0) {
        return item0 == null ? 0 : BuiltInRegistries.ITEM.m_7447_(item0);
    }

    public static Item byId(int int0) {
        return BuiltInRegistries.ITEM.byId(int0);
    }

    @Deprecated
    public static Item byBlock(Block block0) {
        return (Item) BY_BLOCK.getOrDefault(block0, Items.AIR);
    }

    public Item(Item.Properties itemProperties0) {
        this.rarity = itemProperties0.rarity;
        this.craftingRemainingItem = itemProperties0.craftingRemainingItem;
        this.maxDamage = itemProperties0.maxDamage;
        this.maxStackSize = itemProperties0.maxStackSize;
        this.foodProperties = itemProperties0.foodProperties;
        this.isFireResistant = itemProperties0.isFireResistant;
        this.requiredFeatures = itemProperties0.requiredFeatures;
        if (SharedConstants.IS_RUNNING_IN_IDE) {
            String $$1 = this.getClass().getSimpleName();
            if (!$$1.endsWith("Item")) {
                LOGGER.error("Item classes should end with Item and {} doesn't.", $$1);
            }
        }
    }

    @Deprecated
    public Holder.Reference<Item> builtInRegistryHolder() {
        return this.builtInRegistryHolder;
    }

    public void onUseTick(Level level0, LivingEntity livingEntity1, ItemStack itemStack2, int int3) {
    }

    public void onDestroyed(ItemEntity itemEntity0) {
    }

    public void verifyTagAfterLoad(CompoundTag compoundTag0) {
    }

    public boolean canAttackBlock(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        return true;
    }

    @Override
    public Item asItem() {
        return this;
    }

    public InteractionResult useOn(UseOnContext useOnContext0) {
        return InteractionResult.PASS;
    }

    public float getDestroySpeed(ItemStack itemStack0, BlockState blockState1) {
        return 1.0F;
    }

    public InteractionResultHolder<ItemStack> use(Level level0, Player player1, InteractionHand interactionHand2) {
        if (this.isEdible()) {
            ItemStack $$3 = player1.m_21120_(interactionHand2);
            if (player1.canEat(this.getFoodProperties().canAlwaysEat())) {
                player1.m_6672_(interactionHand2);
                return InteractionResultHolder.consume($$3);
            } else {
                return InteractionResultHolder.fail($$3);
            }
        } else {
            return InteractionResultHolder.pass(player1.m_21120_(interactionHand2));
        }
    }

    public ItemStack finishUsingItem(ItemStack itemStack0, Level level1, LivingEntity livingEntity2) {
        return this.isEdible() ? livingEntity2.eat(level1, itemStack0) : itemStack0;
    }

    public final int getMaxStackSize() {
        return this.maxStackSize;
    }

    public final int getMaxDamage() {
        return this.maxDamage;
    }

    public boolean canBeDepleted() {
        return this.maxDamage > 0;
    }

    public boolean isBarVisible(ItemStack itemStack0) {
        return itemStack0.isDamaged();
    }

    public int getBarWidth(ItemStack itemStack0) {
        return Math.round(13.0F - (float) itemStack0.getDamageValue() * 13.0F / (float) this.maxDamage);
    }

    public int getBarColor(ItemStack itemStack0) {
        float $$1 = Math.max(0.0F, ((float) this.maxDamage - (float) itemStack0.getDamageValue()) / (float) this.maxDamage);
        return Mth.hsvToRgb($$1 / 3.0F, 1.0F, 1.0F);
    }

    public boolean overrideStackedOnOther(ItemStack itemStack0, Slot slot1, ClickAction clickAction2, Player player3) {
        return false;
    }

    public boolean overrideOtherStackedOnMe(ItemStack itemStack0, ItemStack itemStack1, Slot slot2, ClickAction clickAction3, Player player4, SlotAccess slotAccess5) {
        return false;
    }

    public boolean hurtEnemy(ItemStack itemStack0, LivingEntity livingEntity1, LivingEntity livingEntity2) {
        return false;
    }

    public boolean mineBlock(ItemStack itemStack0, Level level1, BlockState blockState2, BlockPos blockPos3, LivingEntity livingEntity4) {
        return false;
    }

    public boolean isCorrectToolForDrops(BlockState blockState0) {
        return false;
    }

    public InteractionResult interactLivingEntity(ItemStack itemStack0, Player player1, LivingEntity livingEntity2, InteractionHand interactionHand3) {
        return InteractionResult.PASS;
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    public String toString() {
        return BuiltInRegistries.ITEM.getKey(this).getPath();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this));
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public String getDescriptionId(ItemStack itemStack0) {
        return this.getDescriptionId();
    }

    public boolean shouldOverrideMultiplayerNbt() {
        return true;
    }

    @Nullable
    public final Item getCraftingRemainingItem() {
        return this.craftingRemainingItem;
    }

    public boolean hasCraftingRemainingItem() {
        return this.craftingRemainingItem != null;
    }

    public void inventoryTick(ItemStack itemStack0, Level level1, Entity entity2, int int3, boolean boolean4) {
    }

    public void onCraftedBy(ItemStack itemStack0, Level level1, Player player2) {
    }

    public boolean isComplex() {
        return false;
    }

    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return itemStack0.getItem().isEdible() ? UseAnim.EAT : UseAnim.NONE;
    }

    public int getUseDuration(ItemStack itemStack0) {
        if (itemStack0.getItem().isEdible()) {
            return this.getFoodProperties().isFastFood() ? 16 : 32;
        } else {
            return 0;
        }
    }

    public void releaseUsing(ItemStack itemStack0, Level level1, LivingEntity livingEntity2, int int3) {
    }

    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
    }

    public Optional<TooltipComponent> getTooltipImage(ItemStack itemStack0) {
        return Optional.empty();
    }

    public Component getName(ItemStack itemStack0) {
        return Component.translatable(this.getDescriptionId(itemStack0));
    }

    public boolean isFoil(ItemStack itemStack0) {
        return itemStack0.isEnchanted();
    }

    public Rarity getRarity(ItemStack itemStack0) {
        if (!itemStack0.isEnchanted()) {
            return this.rarity;
        } else {
            switch(this.rarity) {
                case COMMON:
                case UNCOMMON:
                    return Rarity.RARE;
                case RARE:
                    return Rarity.EPIC;
                case EPIC:
                default:
                    return this.rarity;
            }
        }
    }

    public boolean isEnchantable(ItemStack itemStack0) {
        return this.getMaxStackSize() == 1 && this.canBeDepleted();
    }

    protected static BlockHitResult getPlayerPOVHitResult(Level level0, Player player1, ClipContext.Fluid clipContextFluid2) {
        float $$3 = player1.m_146909_();
        float $$4 = player1.m_146908_();
        Vec3 $$5 = player1.m_146892_();
        float $$6 = Mth.cos(-$$4 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float $$7 = Mth.sin(-$$4 * (float) (Math.PI / 180.0) - (float) Math.PI);
        float $$8 = -Mth.cos(-$$3 * (float) (Math.PI / 180.0));
        float $$9 = Mth.sin(-$$3 * (float) (Math.PI / 180.0));
        float $$10 = $$7 * $$8;
        float $$12 = $$6 * $$8;
        double $$13 = 5.0;
        Vec3 $$14 = $$5.add((double) $$10 * 5.0, (double) $$9 * 5.0, (double) $$12 * 5.0);
        return level0.m_45547_(new ClipContext($$5, $$14, ClipContext.Block.OUTLINE, clipContextFluid2, player1));
    }

    public int getEnchantmentValue() {
        return 0;
    }

    public boolean isValidRepairItem(ItemStack itemStack0, ItemStack itemStack1) {
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return ImmutableMultimap.of();
    }

    public boolean useOnRelease(ItemStack itemStack0) {
        return false;
    }

    public ItemStack getDefaultInstance() {
        return new ItemStack(this);
    }

    public boolean isEdible() {
        return this.foodProperties != null;
    }

    @Nullable
    public FoodProperties getFoodProperties() {
        return this.foodProperties;
    }

    public SoundEvent getDrinkingSound() {
        return SoundEvents.GENERIC_DRINK;
    }

    public SoundEvent getEatingSound() {
        return SoundEvents.GENERIC_EAT;
    }

    public boolean isFireResistant() {
        return this.isFireResistant;
    }

    public boolean canBeHurtBy(DamageSource damageSource0) {
        return !this.isFireResistant || !damageSource0.is(DamageTypeTags.IS_FIRE);
    }

    public boolean canFitInsideContainerItems() {
        return true;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    public static class Properties {

        int maxStackSize = 64;

        int maxDamage;

        @Nullable
        Item craftingRemainingItem;

        Rarity rarity = Rarity.COMMON;

        @Nullable
        FoodProperties foodProperties;

        boolean isFireResistant;

        FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

        public Item.Properties food(FoodProperties foodProperties0) {
            this.foodProperties = foodProperties0;
            return this;
        }

        public Item.Properties stacksTo(int int0) {
            if (this.maxDamage > 0) {
                throw new RuntimeException("Unable to have damage AND stack.");
            } else {
                this.maxStackSize = int0;
                return this;
            }
        }

        public Item.Properties defaultDurability(int int0) {
            return this.maxDamage == 0 ? this.durability(int0) : this;
        }

        public Item.Properties durability(int int0) {
            this.maxDamage = int0;
            this.maxStackSize = 1;
            return this;
        }

        public Item.Properties craftRemainder(Item item0) {
            this.craftingRemainingItem = item0;
            return this;
        }

        public Item.Properties rarity(Rarity rarity0) {
            this.rarity = rarity0;
            return this;
        }

        public Item.Properties fireResistant() {
            this.isFireResistant = true;
            return this;
        }

        public Item.Properties requiredFeatures(FeatureFlag... featureFlag0) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset(featureFlag0);
            return this;
        }
    }
}