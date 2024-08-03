package com.simibubi.create.content.logistics.filter;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.fan.processing.AllFanProcessingTypes;
import com.simibubi.create.content.logistics.filter.attribute.BookAuthorAttribute;
import com.simibubi.create.content.logistics.filter.attribute.BookCopyAttribute;
import com.simibubi.create.content.logistics.filter.attribute.ColorAttribute;
import com.simibubi.create.content.logistics.filter.attribute.EnchantAttribute;
import com.simibubi.create.content.logistics.filter.attribute.FluidContentsAttribute;
import com.simibubi.create.content.logistics.filter.attribute.ItemNameAttribute;
import com.simibubi.create.content.logistics.filter.attribute.ShulkerFillLevelAttribute;
import com.simibubi.create.content.logistics.filter.attribute.astralsorcery.AstralSorceryAmuletAttribute;
import com.simibubi.create.content.logistics.filter.attribute.astralsorcery.AstralSorceryAttunementAttribute;
import com.simibubi.create.content.logistics.filter.attribute.astralsorcery.AstralSorceryCrystalAttribute;
import com.simibubi.create.content.logistics.filter.attribute.astralsorcery.AstralSorceryPerkGemAttribute;
import com.simibubi.create.foundation.utility.Lang;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

public interface ItemAttribute {

    List<ItemAttribute> types = new ArrayList();

    ItemAttribute standard = register(ItemAttribute.StandardTraits.DUMMY);

    ItemAttribute inTag = register(new ItemAttribute.InTag(ItemTags.LOGS));

    ItemAttribute addedBy = register(new ItemAttribute.AddedBy("dummy"));

    ItemAttribute hasEnchant = register(EnchantAttribute.EMPTY);

    ItemAttribute shulkerFillLevel = register(ShulkerFillLevelAttribute.EMPTY);

    ItemAttribute hasColor = register(ColorAttribute.EMPTY);

    ItemAttribute hasFluid = register(FluidContentsAttribute.EMPTY);

    ItemAttribute hasName = register(new ItemNameAttribute("dummy"));

    ItemAttribute bookAuthor = register(new BookAuthorAttribute("dummy"));

    ItemAttribute bookCopy = register(new BookCopyAttribute(-1));

    ItemAttribute astralAmulet = register(new AstralSorceryAmuletAttribute("dummy", -1));

    ItemAttribute astralAttunement = register(new AstralSorceryAttunementAttribute("dummy"));

    ItemAttribute astralCrystal = register(new AstralSorceryCrystalAttribute("dummy"));

    ItemAttribute astralPerkGem = register(new AstralSorceryPerkGemAttribute("dummy"));

    static ItemAttribute register(ItemAttribute attributeType) {
        types.add(attributeType);
        return attributeType;
    }

    @Nullable
    static ItemAttribute fromNBT(CompoundTag nbt) {
        for (ItemAttribute itemAttribute : types) {
            if (itemAttribute.canRead(nbt)) {
                return itemAttribute.readNBT(nbt.getCompound(itemAttribute.getNBTKey()));
            }
        }
        return null;
    }

    default boolean appliesTo(ItemStack stack, Level world) {
        return this.appliesTo(stack);
    }

    boolean appliesTo(ItemStack var1);

    default List<ItemAttribute> listAttributesOf(ItemStack stack, Level world) {
        return this.listAttributesOf(stack);
    }

    List<ItemAttribute> listAttributesOf(ItemStack var1);

    String getTranslationKey();

    void writeNBT(CompoundTag var1);

    ItemAttribute readNBT(CompoundTag var1);

    default void serializeNBT(CompoundTag nbt) {
        CompoundTag compound = new CompoundTag();
        this.writeNBT(compound);
        nbt.put(this.getNBTKey(), compound);
    }

    default Object[] getTranslationParameters() {
        return new String[0];
    }

    default boolean canRead(CompoundTag nbt) {
        return nbt.contains(this.getNBTKey());
    }

    default String getNBTKey() {
        return this.getTranslationKey();
    }

    @OnlyIn(Dist.CLIENT)
    default MutableComponent format(boolean inverted) {
        return Lang.translateDirect("item_attributes." + this.getTranslationKey() + (inverted ? ".inverted" : ""), this.getTranslationParameters());
    }

    public static class AddedBy implements ItemAttribute {

        private String modId;

        public AddedBy(String modId) {
            this.modId = modId;
        }

        @Override
        public boolean appliesTo(ItemStack stack) {
            return this.modId.equals(stack.getItem().getCreatorModId(stack));
        }

        @Override
        public List<ItemAttribute> listAttributesOf(ItemStack stack) {
            String id = stack.getItem().getCreatorModId(stack);
            return id == null ? Collections.emptyList() : Arrays.asList(new ItemAttribute.AddedBy(id));
        }

        @Override
        public String getTranslationKey() {
            return "added_by";
        }

        @Override
        public Object[] getTranslationParameters() {
            Optional<? extends ModContainer> modContainerById = ModList.get().getModContainerById(this.modId);
            String name = (String) modContainerById.map(ModContainer::getModInfo).map(IModInfo::getDisplayName).orElse(StringUtils.capitalize(this.modId));
            return new Object[] { name };
        }

        @Override
        public void writeNBT(CompoundTag nbt) {
            nbt.putString("id", this.modId);
        }

        @Override
        public ItemAttribute readNBT(CompoundTag nbt) {
            return new ItemAttribute.AddedBy(nbt.getString("id"));
        }
    }

    public static class InTag implements ItemAttribute {

        public TagKey<Item> tag;

        public InTag(TagKey<Item> tag) {
            this.tag = tag;
        }

        @Override
        public boolean appliesTo(ItemStack stack) {
            return stack.is(this.tag);
        }

        @Override
        public List<ItemAttribute> listAttributesOf(ItemStack stack) {
            return (List<ItemAttribute>) stack.getTags().map(ItemAttribute.InTag::new).collect(Collectors.toList());
        }

        @Override
        public String getTranslationKey() {
            return "in_tag";
        }

        @Override
        public Object[] getTranslationParameters() {
            return new Object[] { "#" + this.tag.location() };
        }

        @Override
        public void writeNBT(CompoundTag nbt) {
            nbt.putString("space", this.tag.location().getNamespace());
            nbt.putString("path", this.tag.location().getPath());
        }

        @Override
        public ItemAttribute readNBT(CompoundTag nbt) {
            return new ItemAttribute.InTag(ItemTags.create(new ResourceLocation(nbt.getString("space"), nbt.getString("path"))));
        }
    }

    public static enum StandardTraits implements ItemAttribute {

        DUMMY(s -> false),
        PLACEABLE(s -> s.getItem() instanceof BlockItem),
        CONSUMABLE(ItemStack::m_41614_),
        FLUID_CONTAINER(s -> s.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).isPresent()),
        ENCHANTED(ItemStack::m_41793_),
        MAX_ENCHANTED(ItemAttribute.StandardTraits::maxEnchanted),
        RENAMED(ItemStack::m_41788_),
        DAMAGED(ItemStack::m_41768_),
        BADLY_DAMAGED(s -> s.isDamaged() && (float) s.getDamageValue() / (float) s.getMaxDamage() > 0.75F),
        NOT_STACKABLE((ItemStack::m_41753_).negate()),
        EQUIPABLE(s -> LivingEntity.getEquipmentSlotForItem(s).getType() != EquipmentSlot.Type.HAND),
        FURNACE_FUEL(AbstractFurnaceBlockEntity::m_58399_),
        WASHABLE(AllFanProcessingTypes.SPLASHING::canProcess),
        HAUNTABLE(AllFanProcessingTypes.HAUNTING::canProcess),
        CRUSHABLE((s, w) -> testRecipe(s, w, AllRecipeTypes.CRUSHING.getType()) || testRecipe(s, w, AllRecipeTypes.MILLING.getType())),
        SMELTABLE((s, w) -> testRecipe(s, w, RecipeType.SMELTING)),
        SMOKABLE((s, w) -> testRecipe(s, w, RecipeType.SMOKING)),
        BLASTABLE((s, w) -> testRecipe(s, w, RecipeType.BLASTING)),
        COMPOSTABLE(s -> ComposterBlock.COMPOSTABLES.containsKey(s.getItem()));

        private static final RecipeWrapper RECIPE_WRAPPER = new RecipeWrapper(new ItemStackHandler(1));

        private Predicate<ItemStack> test;

        private BiPredicate<ItemStack, Level> testWithWorld;

        private StandardTraits(Predicate<ItemStack> test) {
            this.test = test;
        }

        private static boolean testRecipe(ItemStack s, Level w, RecipeType<? extends Recipe<Container>> type) {
            RECIPE_WRAPPER.setItem(0, s.copy());
            return w.getRecipeManager().getRecipeFor(type, RECIPE_WRAPPER, w).isPresent();
        }

        private static boolean maxEnchanted(ItemStack s) {
            return EnchantmentHelper.getEnchantments(s).entrySet().stream().anyMatch(e -> ((Enchantment) e.getKey()).getMaxLevel() <= (Integer) e.getValue());
        }

        private StandardTraits(BiPredicate<ItemStack, Level> test) {
            this.testWithWorld = test;
        }

        @Override
        public boolean appliesTo(ItemStack stack, Level world) {
            return this.testWithWorld != null ? this.testWithWorld.test(stack, world) : this.appliesTo(stack);
        }

        @Override
        public boolean appliesTo(ItemStack stack) {
            return this.test.test(stack);
        }

        @Override
        public List<ItemAttribute> listAttributesOf(ItemStack stack, Level world) {
            List<ItemAttribute> attributes = new ArrayList();
            for (ItemAttribute.StandardTraits trait : values()) {
                if (trait.appliesTo(stack, world)) {
                    attributes.add(trait);
                }
            }
            return attributes;
        }

        @Override
        public List<ItemAttribute> listAttributesOf(ItemStack stack) {
            return null;
        }

        @Override
        public String getTranslationKey() {
            return Lang.asId(this.name());
        }

        @Override
        public String getNBTKey() {
            return "standard_trait";
        }

        @Override
        public void writeNBT(CompoundTag nbt) {
            nbt.putBoolean(this.name(), true);
        }

        @Override
        public ItemAttribute readNBT(CompoundTag nbt) {
            for (ItemAttribute.StandardTraits trait : values()) {
                if (nbt.contains(trait.name())) {
                    return trait;
                }
            }
            return null;
        }
    }
}