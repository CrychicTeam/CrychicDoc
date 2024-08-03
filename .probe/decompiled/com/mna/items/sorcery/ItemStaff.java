package com.mna.items.sorcery;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.Registries;
import com.mna.api.items.ITieredItem;
import com.mna.api.items.ItemUtils;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.tools.CollectionUtils;
import com.mna.api.tools.MATags;
import com.mna.enchantments.framework.EnchantmentInit;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.math.MathUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;

public class ItemStaff extends ItemSpell implements ITieredItem<ItemStaff> {

    private static final String KEY_CHARGES = "charges";

    protected float attackDamage = 4.5F;

    private final int enchantability = 25;

    private static int charge_modifier = 40;

    private int tier = -1;

    private final ArrayList<Enchantment> allowedEnchantments = new ArrayList();

    private final Multimap<Attribute, AttributeModifier> attributeModifiers;

    public ItemStaff(float attackDamage) {
        this(attackDamage, -3.0F);
    }

    public ItemStaff(Item.Properties properties, float attackDamage, float attackSpeed) {
        super(properties);
        this.attackDamage = attackDamage;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", (double) attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    public ItemStaff(float attackDamage, float attackSpeed) {
        super(new Item.Properties().defaultDurability(100).durability(100).setNoRepair());
        this.attackDamage = attackDamage;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", (double) attackSpeed, AttributeModifier.Operation.ADDITION));
        this.attributeModifiers = builder.build();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean addItemWithGuiTooltip(ItemStack stack) {
        return this.shouldConsumeMana(stack);
    }

    @Override
    public boolean openGuiIfModifierPressed(ItemStack stack, Player player, Level world) {
        return !this.shouldConsumeMana(stack) ? false : super.openGuiIfModifierPressed(stack, player, world);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack activeStack = playerIn.m_21120_(handIn);
        if (!worldIn.isClientSide && !this.shouldConsumeMana(activeStack)) {
            this.consumeCharge(activeStack, playerIn, handIn);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    public int getMaxDamage(ItemStack stack) {
        if (!stack.hasTag()) {
            return super.getMaxDamage(stack);
        } else {
            CompoundTag tag = stack.getTag();
            return !tag.contains("charges") ? super.getMaxDamage(stack) : tag.getInt("charges");
        }
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return FastColor.ARGB32.color(255, 128, 64, 255);
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        SpellRecipe recipe = SpellRecipe.fromNBT(stack.getOrCreateTag());
        int chargeConsumed = (int) Math.ceil((double) recipe.getManaCost());
        if (!recipe.isChanneled()) {
            chargeConsumed *= charge_modifier;
        }
        int charges = ItemUtils.getCharges(stack) - chargeConsumed;
        int maxCharges = Math.max(ItemUtils.getMaxCharges(stack), 1);
        float pct = (float) charges / (float) maxCharges;
        return Math.round(13.0F * pct);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        if (!stack.hasTag()) {
            return false;
        } else {
            CompoundTag tag = stack.getTag();
            return tag.contains("charges");
        }
    }

    @Override
    protected boolean shouldConsumeMana(ItemStack stack) {
        return !stack.hasTag() ? true : !stack.getTag().contains("charges");
    }

    @Override
    protected boolean consumeChanneledMana(Player player, ItemStack stack) {
        if (this.shouldConsumeMana(stack)) {
            return super.consumeChanneledMana(player, stack);
        } else {
            this.consumeCharge(stack, player, player.m_7655_());
            return true;
        }
    }

    protected void consumeCharge(ItemStack spellStack, Player player, InteractionHand handIn) {
        if (!player.isCreative()) {
            SpellRecipe recipe = SpellRecipe.fromNBT(spellStack.getOrCreateTag());
            if (recipe.isValid()) {
                int chargeConsumed = (int) Math.ceil((double) recipe.getManaCost());
                if (!recipe.isChanneled()) {
                    chargeConsumed *= charge_modifier;
                }
                int charges = ItemUtils.getCharges(spellStack) - chargeConsumed;
                int maxCharges = Math.max(ItemUtils.getMaxCharges(spellStack), 1);
                ItemUtils.writeCharges(spellStack, charges);
                float damagePct = 1.0F - (float) charges / (float) maxCharges;
                int newDamage = (int) ((float) spellStack.getMaxDamage() * damagePct);
                if (charges <= 0) {
                    player.m_21190_(handIn);
                    Item item = spellStack.getItem();
                    spellStack.shrink(1);
                    player.awardStat(Stats.ITEM_BROKEN.get(item));
                    spellStack.setDamageValue(0);
                } else {
                    spellStack.setDamageValue(newDamage);
                }
            }
        }
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot, stack);
    }

    public int getEnchantmentValue(ItemStack stack) {
        return 25;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(book);
        for (Enchantment ench : enchantments.keySet()) {
            if (!this.getAllowedEnchantments().contains(ench)) {
                return false;
            }
        }
        return true;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.getAllowedEnchantments().contains(enchantment);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        SpellRecipe recipe = SpellRecipe.fromNBT(stack.getOrCreateTag());
        if (recipe != null && !recipe.isMysterious()) {
            int chargeConsumed = (int) Math.ceil((double) recipe.getManaCost());
            if (!recipe.isChanneled()) {
                chargeConsumed *= charge_modifier;
            }
            int charges = (int) Math.ceil((double) ((float) ItemUtils.getCharges(stack) / Math.max((float) chargeConsumed, 1.0F)));
            if (charges > 0) {
                MutableComponent comp = Component.translatable("item.mna.spell.charges_display", charges).withStyle(ChatFormatting.AQUA);
                tooltip.add(comp);
            }
        }
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    public static ItemStack buildRandomSpellStaff(float chanceForForceSelf, float chanceForNoHelpfulLogic, boolean allowMessWithMori, ServerLevel level) {
        Random random = new Random();
        ItemStack stack = MATags.getRandomItemFrom(MATags.Items.GENERATED_SPELL_ITEMS);
        if (stack.isEmpty()) {
            stack = new ItemStack(ItemInit.STAFF_AUM.get());
        }
        SpellRecipe randomRecipe = new SpellRecipe();
        int numCharges = 20 + (int) (Math.random() * 180.0);
        boolean forceSelf = Math.random() <= (double) chanceForForceSelf;
        boolean skipTags = forceSelf ? false : Math.random() <= (double) chanceForNoHelpfulLogic;
        if (!forceSelf) {
            List<Shape> shapeList = (List<Shape>) ((IForgeRegistry) Registries.Shape.get()).getValues().stream().filter(s -> s.canBeOnRandomStaff()).collect(Collectors.toList());
            MutableInt tierTotal = new MutableInt(0);
            shapeList.forEach(s -> tierTotal.add(s.getTier(level)));
            shapeList.sort((a, b) -> {
                int aT = a.getTier(level);
                int bT = b.getTier(level);
                if (aT < bT) {
                    return -1;
                } else {
                    return aT > bT ? 1 : 0;
                }
            });
            double r = Math.random() * (double) tierTotal.getValue().intValue();
            Shape selectedShape = null;
            for (Shape shape : shapeList) {
                r -= (double) (6 - shape.getTier(level));
                if (r <= 0.0) {
                    selectedShape = shape;
                    break;
                }
            }
            randomRecipe.setShape(selectedShape);
        } else {
            randomRecipe.setShape(Shapes.SELF);
        }
        MutableObject<SpellPartTags> targetTag = new MutableObject(randomRecipe.getShape().getPart().getUseTag());
        if (forceSelf) {
            double rnd = random.nextDouble();
            if (rnd < 0.05) {
                targetTag.setValue(SpellPartTags.DONOTUSE);
            } else if (rnd < 0.75) {
                targetTag.setValue(SpellPartTags.HARMFUL);
            } else {
                targetTag.setValue(SpellPartTags.SELF);
            }
        }
        int count = MathUtils.clamp(MathUtils.weightedRandomNumber(new int[] { 89, 5, 3, 2, 1 }) + 1, 1, 5);
        for (int i = 0; i < count; i++) {
            SpellEffect c = (SpellEffect) CollectionUtils.getRandom((Collection) ((IForgeRegistry) Registries.SpellEffect.get()).getValues().stream().filter(comp -> {
                if ((comp.getUseTag() != SpellPartTags.DONOTUSE || skipTags) && comp.canBeOnRandomStaff()) {
                    List<SpellReagent> reagents = comp.getRequiredReagents(null, null);
                    if (reagents != null && reagents.size() > 0) {
                        return false;
                    } else {
                        return skipTags ? true : CollectionUtils.componentMatchesShapeAndTag(randomRecipe.getShape().getPart(), comp, (SpellPartTags) targetTag.getValue());
                    }
                } else {
                    return false;
                }
            }).collect(Collectors.toList())).orElse(null);
            if (c != null) {
                if (targetTag.getValue() == SpellPartTags.NEUTRAL) {
                    targetTag.setValue(c.getUseTag());
                }
                randomRecipe.addComponent(c);
            }
        }
        if (randomRecipe.getComponents().size() == 0) {
            return stack;
        } else {
            UnmodifiableIterator var21 = randomRecipe.getShape().getContainedAttributes().iterator();
            while (var21.hasNext()) {
                com.mna.api.spells.attributes.Attribute attr = (com.mna.api.spells.attributes.Attribute) var21.next();
                if (attr != com.mna.api.spells.attributes.Attribute.DELAY) {
                    randomRecipe.getShape().setValue(attr, randomRecipe.getShape().getMinimumValue(attr) + (randomRecipe.getShape().getMaximumValue(attr) - randomRecipe.getShape().getMinimumValue(attr)) * (float) Math.random());
                }
            }
            randomRecipe.iterateComponents(c -> {
                UnmodifiableIterator var1x = c.getContainedAttributes().iterator();
                while (var1x.hasNext()) {
                    com.mna.api.spells.attributes.Attribute attrx = (com.mna.api.spells.attributes.Attribute) var1x.next();
                    if (attrx != com.mna.api.spells.attributes.Attribute.DELAY) {
                        c.setValue(attrx, c.getMinimumValue(attrx) + (c.getMaximumValue(attrx) - c.getMinimumValue(attrx)) * 0.5F + ((float) Math.random() - 0.5F));
                    }
                }
            });
            if (!randomRecipe.isValid()) {
                return ItemStack.EMPTY;
            } else {
                randomRecipe.setMysterious(true);
                randomRecipe.writeToNBT(stack.getOrCreateTag());
                int charges = (int) ((float) numCharges * randomRecipe.getManaCost());
                int var23 = charges * charge_modifier;
                ItemUtils.writeCharges(stack, var23);
                ItemUtils.writeMaxCharges(stack, var23);
                return stack;
            }
        }
    }

    protected ArrayList<Enchantment> getAllowedEnchantments() {
        this.allowedEnchantments.clear();
        if (this.allowedEnchantments.size() == 0) {
            this.allowedEnchantments.add(Enchantments.BANE_OF_ARTHROPODS);
            this.allowedEnchantments.add(Enchantments.FIRE_ASPECT);
            this.allowedEnchantments.add(Enchantments.KNOCKBACK);
            this.allowedEnchantments.add(Enchantments.MOB_LOOTING);
            this.allowedEnchantments.add(Enchantments.SMITE);
            this.allowedEnchantments.add(EnchantmentInit.BEHEADING.get());
            this.allowedEnchantments.add(EnchantmentInit.DURATION_MOD.get());
            this.allowedEnchantments.add(EnchantmentInit.RANGE_MOD.get());
            this.allowedEnchantments.add(EnchantmentInit.SPEED_MOD.get());
            this.allowedEnchantments.add(EnchantmentInit.BLUDGEONING.get());
            this.allowedEnchantments.add(Enchantments.AQUA_AFFINITY);
            this.allowedEnchantments.add(Enchantments.CHANNELING);
            this.allowedEnchantments.add(Enchantments.BLOCK_EFFICIENCY);
            this.allowedEnchantments.add(Enchantments.FLAMING_ARROWS);
            this.allowedEnchantments.add(Enchantments.LOYALTY);
            this.allowedEnchantments.add(Enchantments.FISHING_SPEED);
            this.allowedEnchantments.add(Enchantments.POWER_ARROWS);
            this.allowedEnchantments.add(Enchantments.QUICK_CHARGE);
            this.allowedEnchantments.add(Enchantments.THORNS);
            this.allowedEnchantments.add(Enchantments.FROST_WALKER);
        }
        return this.allowedEnchantments;
    }

    @Override
    public void setCachedTier(int tier) {
        this.tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this.tier;
    }

    public boolean isChargeSpell(ItemStack stack) {
        return !this.shouldConsumeMana(stack);
    }
}