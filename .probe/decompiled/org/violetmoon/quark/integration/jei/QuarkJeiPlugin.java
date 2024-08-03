package org.violetmoon.quark.integration.jei;

import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.block.be.MatrixEnchantingTableBlockEntity;
import org.violetmoon.quark.addons.oddities.client.screen.BackpackInventoryScreen;
import org.violetmoon.quark.addons.oddities.client.screen.CrateScreen;
import org.violetmoon.quark.addons.oddities.module.MatrixEnchantingModule;
import org.violetmoon.quark.addons.oddities.util.CustomInfluence;
import org.violetmoon.quark.addons.oddities.util.Influence;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.content.building.module.VariantFurnacesModule;
import org.violetmoon.quark.content.tools.item.AncientTomeItem;
import org.violetmoon.quark.content.tools.module.AncientTomesModule;
import org.violetmoon.quark.content.tools.module.PickarangModule;
import org.violetmoon.quark.content.tweaks.module.DiamondRepairModule;
import org.violetmoon.quark.content.tweaks.recipe.ElytraDuplicationRecipe;
import org.violetmoon.quark.content.tweaks.recipe.SlabToBlockRecipe;
import org.violetmoon.zeta.config.ZetaGeneralConfig;
import org.violetmoon.zeta.event.play.loading.ZGatherHints;
import org.violetmoon.zeta.module.IDisableable;
import org.violetmoon.zeta.util.RegistryUtil;

@JeiPlugin
public class QuarkJeiPlugin implements IModPlugin {

    private static final ResourceLocation UID = new ResourceLocation("quark", "quark");

    public static final RecipeType<InfluenceEntry> INFLUENCING = RecipeType.create("quark", "influence", InfluenceEntry.class);

    @NotNull
    @Override
    public ResourceLocation getPluginUid() {
        return UID;
    }

    @Override
    public void registerItemSubtypes(@NotNull ISubtypeRegistration registration) {
        registration.useNbtForSubtypes(AncientTomesModule.ancient_tome);
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        List<ItemStack> disabledItems = Quark.ZETA.requiredModTooltipHandler.disabledItems();
        if (!disabledItems.isEmpty()) {
            jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, disabledItems);
        }
        Quark.ZETA.configManager.setJeiReloadListener(configInternals -> {
            if (Quark.ZETA.modules.isEnabled(DiamondRepairModule.class)) {
                Minecraft.getInstance().m_18689_(() -> this.hideAnvilRepairRecipes(jeiRuntime.getRecipeManager()));
            }
            if (ZetaGeneralConfig.hideDisabledContent) {
                Set<Potion> hidePotions = Sets.newHashSet();
                for (Potion potion : BuiltInRegistries.POTION) {
                    ResourceLocation loc = BuiltInRegistries.POTION.getKey(potion);
                    if (loc != null && loc.getNamespace().equals("quark") && !Quark.ZETA.brewingRegistry.isEnabled(potion)) {
                        hidePotions.add(potion);
                    }
                }
                NonNullList<ItemStack> stacksToHide = NonNullList.create();
                for (Item item : BuiltInRegistries.ITEM) {
                    ResourceLocation loc = BuiltInRegistries.ITEM.getKey(item);
                    if (loc.getNamespace().equals("quark") && !IDisableable.isEnabled(item)) {
                    }
                    if (item instanceof PotionItem || item instanceof TippedArrowItem) {
                        NonNullList<ItemStack> potionStacks = NonNullList.create();
                        potionStacks.stream().filter(it -> hidePotions.contains(PotionUtils.getPotion(it))).forEach(stacksToHide::add);
                    }
                }
                if (!stacksToHide.isEmpty()) {
                    Minecraft.getInstance().m_18689_(() -> jeiRuntime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, stacksToHide));
                }
            }
        });
    }

    @Override
    public void registerVanillaCategoryExtensions(@NotNull IVanillaCategoryExtensionRegistration registration) {
        registration.getCraftingCategory().addCategoryExtension(ElytraDuplicationRecipe.class, ElytraDuplicationExtension::new);
        registration.getCraftingCategory().addCategoryExtension(SlabToBlockRecipe.class, SlabToBlockExtension::new);
    }

    private boolean matrix() {
        return Quark.ZETA.modules.isEnabled(MatrixEnchantingModule.class) && MatrixEnchantingModule.allowInfluencing && !MatrixEnchantingModule.candleInfluencingFailed;
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        if (this.matrix()) {
            registration.addRecipeCategories(new InfluenceCategory(registration.getJeiHelpers().getGuiHelper()));
        }
    }

    @Override
    public void registerRecipes(@NotNull final IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getVanillaRecipeFactory();
        if (Quark.ZETA.modules.isEnabled(AncientTomesModule.class)) {
            this.registerAncientTomeAnvilRecipes(registration, factory);
        }
        if (Quark.ZETA.modules.isEnabled(PickarangModule.class)) {
            this.registerPickarangAnvilRepairs(PickarangModule.pickarang, Items.DIAMOND, registration, factory);
            this.registerPickarangAnvilRepairs(PickarangModule.flamerang, Items.NETHERITE_INGOT, registration, factory);
        }
        if (this.matrix()) {
            this.registerInfluenceRecipes(registration);
        }
        if (Quark.ZETA.modules.isEnabled(DiamondRepairModule.class)) {
            this.registerCustomAnvilRecipes(registration, factory);
        }
        if (ZetaGeneralConfig.enableJeiItemInfo) {
            final MutableComponent externalPreamble = Component.translatable("quark.jei.hint_preamble");
            externalPreamble.setStyle(externalPreamble.getStyle().withColor(744779));
            final List<Item> blacklist = RegistryUtil.massRegistryGet(ZetaGeneralConfig.suppressedInfo, BuiltInRegistries.ITEM);
            Quark.ZETA.playBus.fire(new ZGatherHints() {

                @Override
                public void accept(ItemLike itemLike, Component extra) {
                    Item item = itemLike.asItem();
                    if (!blacklist.contains(item)) {
                        MutableComponent compound = Component.literal("");
                        if (!BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("quark")) {
                            compound = compound.append(externalPreamble);
                        }
                        compound = compound.append(extra);
                        registration.addItemStackInfo(new ItemStack(item), compound);
                    }
                }

                @Override
                public RegistryAccess getRegistryAccess() {
                    return QuarkClient.ZETA_CLIENT.hackilyGetCurrentClientLevelRegistryAccess();
                }
            }, ZGatherHints.class);
        }
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        if (Quark.ZETA.modules.isEnabled(VariantFurnacesModule.class)) {
            registration.addRecipeCatalyst(new ItemStack(VariantFurnacesModule.deepslateFurnace), RecipeTypes.FUELING, RecipeTypes.SMELTING);
            registration.addRecipeCatalyst(new ItemStack(VariantFurnacesModule.blackstoneFurnace), RecipeTypes.FUELING, RecipeTypes.SMELTING);
        }
        if (this.matrix()) {
            if (MatrixEnchantingModule.automaticallyConvert) {
                registration.addRecipeCatalyst(new ItemStack(Blocks.ENCHANTING_TABLE), INFLUENCING);
            } else {
                registration.addRecipeCatalyst(new ItemStack(MatrixEnchantingModule.matrixEnchanter), INFLUENCING);
            }
        }
    }

    @Override
    public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
        registration.addGuiContainerHandler(CrateScreen.class, new QuarkJeiPlugin.CrateGuiHandler());
        registration.addRecipeClickArea(BackpackInventoryScreen.class, 137, 29, 10, 13, RecipeTypes.CRAFTING);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new BackpackRecipeTransferHandler(registration.getTransferHelper()), RecipeTypes.CRAFTING);
    }

    private void registerAncientTomeAnvilRecipes(@NotNull IRecipeRegistration registration, @NotNull IVanillaRecipeFactory factory) {
        List<IJeiAnvilRecipe> recipes = new ArrayList();
        for (Enchantment enchant : AncientTomesModule.validEnchants) {
            EnchantmentInstance data = new EnchantmentInstance(enchant, enchant.getMaxLevel());
            recipes.add(factory.createAnvilRecipe(EnchantedBookItem.createForEnchantment(data), Collections.singletonList(AncientTomeItem.getEnchantedItemStack(enchant)), Collections.singletonList(EnchantedBookItem.createForEnchantment(new EnchantmentInstance(data.enchantment, data.level + 1)))));
        }
        registration.addRecipes(RecipeTypes.ANVIL, recipes);
    }

    private void registerPickarangAnvilRepairs(Item pickarang, Item repairMaterial, @NotNull IRecipeRegistration registration, @NotNull IVanillaRecipeFactory factory) {
        ItemStack nearlyBroken = new ItemStack(pickarang);
        nearlyBroken.setDamageValue(nearlyBroken.getMaxDamage());
        ItemStack veryDamaged = nearlyBroken.copy();
        veryDamaged.setDamageValue(veryDamaged.getMaxDamage() * 3 / 4);
        ItemStack damaged = nearlyBroken.copy();
        damaged.setDamageValue(damaged.getMaxDamage() * 2 / 4);
        IJeiAnvilRecipe materialRepair = factory.createAnvilRecipe(nearlyBroken, Collections.singletonList(new ItemStack(repairMaterial)), Collections.singletonList(veryDamaged));
        IJeiAnvilRecipe toolRepair = factory.createAnvilRecipe(veryDamaged, Collections.singletonList(veryDamaged), Collections.singletonList(damaged));
        registration.addRecipes(RecipeTypes.ANVIL, Arrays.asList(materialRepair, toolRepair));
    }

    private void registerInfluenceRecipes(@NotNull IRecipeRegistration registration) {
        registration.addRecipes(INFLUENCING, (List<InfluenceEntry>) Arrays.stream(DyeColor.values()).map(color -> {
            Block candle = (Block) MatrixEnchantingTableBlockEntity.CANDLES.get(color.getId());
            Influence influence = (Influence) MatrixEnchantingModule.candleInfluences.get(color);
            return new InfluenceEntry(candle, influence);
        }).filter(InfluenceEntry::hasAny).collect(Collectors.toList()));
        registration.addRecipes(INFLUENCING, (List<InfluenceEntry>) MatrixEnchantingModule.customInfluences.entrySet().stream().map(entry -> {
            Block block = ((BlockState) entry.getKey()).m_60734_();
            Influence influence = ((CustomInfluence) entry.getValue()).influence();
            return new InfluenceEntry(block, influence);
        }).filter(InfluenceEntry::hasAny).collect(Collectors.toList()));
    }

    private void hideAnvilRepairRecipes(@NotNull IRecipeManager manager) {
        Stream<IJeiAnvilRecipe> anvilRecipe = manager.createRecipeLookup(RecipeTypes.ANVIL).get();
        List<IJeiAnvilRecipe> hidden = (List<IJeiAnvilRecipe>) anvilRecipe.filter(r -> {
            ItemStack left = (ItemStack) r.getLeftInputs().stream().filter(st -> {
                Item i = st.getItem();
                return DiamondRepairModule.repairChanges.containsKey(i) || DiamondRepairModule.unrepairableItems.contains(i);
            }).findFirst().orElse(null);
            if (left != null) {
                for (ItemStack right : r.getRightInputs()) {
                    Item item = left.getItem();
                    if (item.isValidRepairItem(left, right)) {
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        manager.hideRecipes(RecipeTypes.ANVIL, hidden);
    }

    private void registerCustomAnvilRecipes(@NotNull IRecipeRegistration registration, @NotNull IVanillaRecipeFactory factory) {
        for (Item item : DiamondRepairModule.repairChanges.keySet()) {
            ItemStack left = new ItemStack(item);
            ItemStack out = left.copy();
            int max = Quark.ZETA.itemExtensions.get(left).getMaxDamageZeta(left);
            left.setDamageValue(max - 1);
            out.setDamageValue(max - max / 4);
            for (Item repair : DiamondRepairModule.repairChanges.get(item)) {
                IJeiAnvilRecipe toolRepair = factory.createAnvilRecipe(left, Collections.singletonList(new ItemStack(repair)), Collections.singletonList(out));
                registration.addRecipes(RecipeTypes.ANVIL, List.of(toolRepair));
            }
        }
    }

    private static class CrateGuiHandler implements IGuiContainerHandler<CrateScreen> {

        @NotNull
        public List<Rect2i> getGuiExtraAreas(@NotNull CrateScreen containerScreen) {
            return containerScreen.getExtraAreas();
        }
    }
}