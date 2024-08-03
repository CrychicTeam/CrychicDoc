package dev.xkmc.l2weaponry.compat.twilightforest;

import com.google.common.collect.Lists;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2damagetracker.contents.materials.api.IMatToolType;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2weaponry.compat.ModMats;
import dev.xkmc.l2weaponry.init.materials.ILWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.util.TwilightItemTier;

public enum TFToolMats implements ILWToolMats {

    IRONWOOD(new ModMats(TwilightItemTier.IRONWOOD, new IronwoodTool()), false, TFItems.IRONWOOD_INGOT, TFBlocks.IRONWOOD_BLOCK), STEELEAF(new ModMats(TwilightItemTier.STEELEAF, new SteeleafTool()), false, TFItems.STEELEAF_INGOT, TFBlocks.STEELEAF_BLOCK), KNIGHTMETAL(new ModMats(TwilightItemTier.KNIGHTMETAL, new KnightmetalTool()), false, TFItems.KNIGHTMETAL_INGOT, TFBlocks.KNIGHTMETAL_BLOCK), FIERY(new ModMats(TwilightItemTier.FIERY, new FieryTool()), true, TFItems.FIERY_INGOT, TFBlocks.FIERY_BLOCK);

    private final IMatToolType type;

    private final boolean fireRes;

    private final Supplier<Item> ingot;

    private final Supplier<Block> block;

    private TFToolMats(IMatToolType type, boolean fireRes, Supplier<Item> ingot, Supplier<Block> block) {
        this.type = type;
        this.fireRes = fireRes;
        this.ingot = ingot;
        this.block = block;
    }

    @Override
    public IMatToolType type() {
        return this.type;
    }

    @Override
    public boolean fireRes() {
        return this.fireRes;
    }

    @Override
    public Item getTool(LWToolTypes type) {
        return (Item) TFCompat.ITEMS[this.ordinal()][type.ordinal()].get();
    }

    @Override
    public Item getIngot() {
        return (Item) this.ingot.get();
    }

    @Override
    public Item getBlock() {
        return ((Block) this.block.get()).asItem();
    }

    @Override
    public Item getStick() {
        return this == FIERY ? Items.BLAZE_ROD : (Item) LWItems.HANDLE.get();
    }

    @Override
    public void addEnchants(List<EnchantmentInstance> list, LWToolTypes type) {
        if (this.type.getExtraToolConfig() instanceof LWExtraConfig lw) {
            lw.addEnchants(list, type, this.getTool(type));
        }
    }

    @Override
    public String englishName() {
        return this == KNIGHTMETAL ? "knightly" : this.name();
    }

    @Override
    public boolean emissive() {
        return this == FIERY;
    }

    @Override
    public Consumer<FinishedRecipe> getProvider(RegistrateRecipeProvider pvd, ICondition... cond) {
        List<ICondition> list = Lists.asList(new ModLoadedCondition("twilightforest"), cond);
        return ConditionalRecipeWrapper.of(pvd, (ICondition[]) list.toArray(ICondition[]::new));
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}