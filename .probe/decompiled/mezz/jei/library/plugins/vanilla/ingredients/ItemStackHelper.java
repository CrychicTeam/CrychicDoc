package mezz.jei.library.plugins.vanilla.ingredients;

import com.google.common.collect.Streams;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.common.Internal;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IJeiClientConfigs;
import mezz.jei.common.platform.IPlatformItemStackHelper;
import mezz.jei.common.platform.IPlatformRegistry;
import mezz.jei.common.platform.Services;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.common.util.StackHelper;
import mezz.jei.common.util.TagUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemStackHelper implements IIngredientHelper<ItemStack> {

    private final StackHelper stackHelper;

    private final IColorHelper colorHelper;

    public ItemStackHelper(StackHelper stackHelper, IColorHelper colorHelper) {
        this.stackHelper = stackHelper;
        this.colorHelper = colorHelper;
    }

    @Override
    public IIngredientType<ItemStack> getIngredientType() {
        return VanillaTypes.ITEM_STACK;
    }

    public String getDisplayName(ItemStack ingredient) {
        Component displayNameTextComponent = ingredient.getHoverName();
        String displayName = displayNameTextComponent.getString();
        ErrorUtil.checkNotNull(displayName, "itemStack.getDisplayName()");
        return displayName;
    }

    public String getUniqueId(ItemStack ingredient, UidContext context) {
        ErrorUtil.checkNotEmpty(ingredient);
        return this.stackHelper.getUniqueIdentifierForStack(ingredient, context);
    }

    public String getWildcardId(ItemStack ingredient) {
        ErrorUtil.checkNotEmpty(ingredient);
        return StackHelper.getRegistryNameForStack(ingredient);
    }

    public String getDisplayModId(ItemStack ingredient) {
        ErrorUtil.checkNotEmpty(ingredient);
        IPlatformItemStackHelper itemStackHelper = Services.PLATFORM.getItemStackHelper();
        return (String) itemStackHelper.getCreatorModId(ingredient).or(() -> Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(ingredient.getItem()).map(ResourceLocation::m_135827_)).orElseThrow(() -> {
            String stackInfo = this.getErrorInfo(ingredient);
            return new IllegalStateException("null registryName for: " + stackInfo);
        });
    }

    public Iterable<Integer> getColors(ItemStack ingredient) {
        return this.colorHelper.getColors(ingredient, 2);
    }

    public ResourceLocation getResourceLocation(ItemStack ingredient) {
        ErrorUtil.checkNotEmpty(ingredient);
        Item item = ingredient.getItem();
        return (ResourceLocation) Services.PLATFORM.getRegistry(Registries.ITEM).getRegistryName(item).orElseThrow(() -> {
            String stackInfo = this.getErrorInfo(ingredient);
            return new IllegalStateException("item.getRegistryName() returned null for: " + stackInfo);
        });
    }

    public ItemStack getCheatItemStack(ItemStack ingredient) {
        return ingredient;
    }

    public ItemStack copyIngredient(ItemStack ingredient) {
        return ingredient.copy();
    }

    public ItemStack normalizeIngredient(ItemStack ingredient) {
        ItemStack copy = ingredient.copy();
        copy.setCount(1);
        return copy;
    }

    public boolean isValidIngredient(ItemStack ingredient) {
        return !ingredient.isEmpty();
    }

    public boolean isIngredientOnServer(ItemStack ingredient) {
        Item item = ingredient.getItem();
        IPlatformRegistry<Item> registry = Services.PLATFORM.getRegistry(Registries.ITEM);
        return registry.contains(item);
    }

    public Stream<ResourceLocation> getTagStream(ItemStack ingredient) {
        Stream<ResourceLocation> itemTagStream = ingredient.getTags().map(TagKey::f_203868_);
        if (ingredient.getItem() instanceof BlockItem blockItem) {
            IJeiClientConfigs jeiClientConfigs = Internal.getJeiClientConfigs();
            IClientConfig clientConfig = jeiClientConfigs.getClientConfig();
            if (clientConfig.isLookupBlockTagsEnabled()) {
                Stream<ResourceLocation> blockTagStream = blockItem.getBlock().defaultBlockState().m_204343_().map(TagKey::f_203868_);
                return Streams.concat(new Stream[] { itemTagStream, blockTagStream });
            }
        }
        return itemTagStream;
    }

    public String getErrorInfo(@Nullable ItemStack ingredient) {
        return ErrorUtil.getItemStackInfo(ingredient);
    }

    @Override
    public Optional<ResourceLocation> getTagEquivalent(Collection<ItemStack> ingredients) {
        return TagUtil.getTagEquivalent(ingredients, ItemStack::m_41720_, BuiltInRegistries.ITEM::m_203612_);
    }
}