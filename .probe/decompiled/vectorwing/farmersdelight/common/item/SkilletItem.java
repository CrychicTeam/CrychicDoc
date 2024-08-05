package vectorwing.farmersdelight.common.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import vectorwing.farmersdelight.common.block.SkilletBlock;
import vectorwing.farmersdelight.common.block.entity.SkilletBlockEntity;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class SkilletItem extends BlockItem {

    public static final Tiers SKILLET_TIER = Tiers.IRON;

    protected static final UUID FD_ATTACK_KNOCKBACK_UUID = UUID.fromString("e56350e0-8756-464d-92f9-54289ab41e0a");

    private final Multimap<Attribute, AttributeModifier> toolAttributes;

    public SkilletItem(Block block, Item.Properties properties) {
        super(block, properties.defaultDurability(SKILLET_TIER.getUses()));
        float attackDamage = 5.0F + SKILLET_TIER.getAttackDamageBonus();
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", (double) attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", -3.1F, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(FD_ATTACK_KNOCKBACK_UUID, "Tool modifier", 1.0, AttributeModifier.Operation.ADDITION));
        this.toolAttributes = builder.build();
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    private static boolean isPlayerNearHeatSource(Player player, LevelReader level) {
        if (player.m_6060_()) {
            return true;
        } else {
            BlockPos pos = player.m_20183_();
            for (BlockPos nearbyPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                if (level.m_8055_(nearbyPos).m_204336_(ModTags.HEAT_SOURCES)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        int fireAspectLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
        int cookingTime = stack.getOrCreateTag().getInt("CookTimeHandheld");
        return SkilletBlock.getSkilletCookingTime(cookingTime, fireAspectLevel);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack skilletStack = player.m_21120_(hand);
        if (isPlayerNearHeatSource(player, level)) {
            InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
            ItemStack cookingStack = player.m_21120_(otherHand);
            if (skilletStack.getOrCreateTag().contains("Cooking")) {
                player.m_6672_(hand);
                return InteractionResultHolder.pass(skilletStack);
            }
            Optional<CampfireCookingRecipe> recipe = getCookingRecipe(cookingStack, level);
            if (recipe.isPresent()) {
                ItemStack cookingStackCopy = cookingStack.copy();
                ItemStack cookingStackUnit = cookingStackCopy.split(1);
                skilletStack.getOrCreateTag().put("Cooking", cookingStackUnit.serializeNBT());
                skilletStack.getOrCreateTag().putInt("CookTimeHandheld", ((CampfireCookingRecipe) recipe.get()).m_43753_());
                player.m_6672_(hand);
                player.m_21008_(otherHand, cookingStackCopy);
                return InteractionResultHolder.consume(skilletStack);
            }
            player.displayClientMessage(TextUtils.getTranslation("item.skillet.how_to_cook"), true);
        }
        return InteractionResultHolder.pass(skilletStack);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int count) {
        if (entity instanceof Player player) {
            Vec3 pos = player.m_20182_();
            double x = pos.x() + 0.5;
            double y = pos.y();
            double z = pos.z() + 0.5;
            if (level.random.nextInt(50) == 0) {
                level.playLocalSound(x, y, z, ModSounds.BLOCK_SKILLET_SIZZLE.get(), SoundSource.BLOCKS, 0.4F, level.random.nextFloat() * 0.2F + 0.9F, false);
            }
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("Cooking")) {
                ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
                player.getInventory().placeItemBackInInventory(cookingStack);
                tag.remove("Cooking");
                tag.remove("CookTimeHandheld");
            }
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.contains("Cooking")) {
                ItemStack cookingStack = ItemStack.of(tag.getCompound("Cooking"));
                Optional<CampfireCookingRecipe> cookingRecipe = getCookingRecipe(cookingStack, level);
                cookingRecipe.ifPresent(recipe -> {
                    ItemStack resultStack = recipe.m_5874_(new SimpleContainer(), level.registryAccess());
                    if (!player.getInventory().add(resultStack)) {
                        player.drop(resultStack, false);
                    }
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, stack);
                    }
                });
                tag.remove("Cooking");
                tag.remove("CookTimeHandheld");
            }
        }
        return stack;
    }

    public static Optional<CampfireCookingRecipe> getCookingRecipe(ItemStack stack, Level level) {
        return stack.isEmpty() ? Optional.empty() : level.getRecipeManager().getRecipeFor(RecipeType.CAMPFIRE_COOKING, new SimpleContainer(stack), level);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, @Nullable Player player, ItemStack stack, BlockState state) {
        super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        if (level.getBlockEntity(pos) instanceof SkilletBlockEntity skillet) {
            skillet.setSkilletItem(stack);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return SKILLET_TIER.getRepairIngredient().test(repair) || super.m_6832_(toRepair, repair);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!level.isClientSide && state.m_60800_(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, entity, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        Player player = context.m_43723_();
        return player != null && player.m_6144_() ? super.place(context) : InteractionResult.PASS;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment.category.equals(EnchantmentCategory.WEAPON)) {
            Set<Enchantment> DENIED_ENCHANTMENTS = Sets.newHashSet(new Enchantment[] { Enchantments.SWEEPING_EDGE });
            return !DENIED_ENCHANTMENTS.contains(enchantment);
        } else {
            return enchantment.category.canEnchant(stack.getItem());
        }
    }

    @Override
    public int getEnchantmentValue() {
        return SKILLET_TIER.getEnchantmentValue();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.toolAttributes : super.m_7167_(equipmentSlot);
    }

    @EventBusSubscriber(modid = "farmersdelight", bus = Bus.FORGE)
    public static class SkilletEvents {

        @SubscribeEvent
        public static void playSkilletAttackSound(LivingDamageEvent event) {
            DamageSource damageSource = event.getSource();
            if (damageSource.getDirectEntity() instanceof LivingEntity livingEntity) {
                if (livingEntity.getItemInHand(InteractionHand.MAIN_HAND).is(ModItems.SKILLET.get())) {
                    float pitch = 0.9F + livingEntity.getRandom().nextFloat() * 0.2F;
                    if (livingEntity instanceof Player player) {
                        float attackPower = player.getAttackStrengthScale(0.0F);
                        if (attackPower > 0.8F) {
                            player.m_20193_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 1.0F, pitch);
                        } else {
                            player.m_20193_().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), ModSounds.ITEM_SKILLET_ATTACK_WEAK.get(), SoundSource.PLAYERS, 0.8F, 0.9F);
                        }
                    } else {
                        livingEntity.m_20193_().playSound(null, livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_(), ModSounds.ITEM_SKILLET_ATTACK_STRONG.get(), SoundSource.PLAYERS, 1.0F, pitch);
                    }
                }
            }
        }
    }
}