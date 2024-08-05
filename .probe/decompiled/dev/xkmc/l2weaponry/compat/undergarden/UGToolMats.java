package dev.xkmc.l2weaponry.compat.undergarden;

import com.google.common.collect.Lists;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2damagetracker.contents.materials.api.IMatToolType;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2weaponry.compat.ModMats;
import dev.xkmc.l2weaponry.init.materials.ILWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWExtraConfig;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import quek.undergarden.registry.UGBlocks;
import quek.undergarden.registry.UGItemTiers;
import quek.undergarden.registry.UGItems;

public enum UGToolMats implements ILWToolMats {

    CLOGGRUM(new ModMats(UGItemTiers.CLOGGRUM, new CloggrumTool()), false, UGItems.CLOGGRUM_INGOT, UGBlocks.CLOGGRUM_BLOCK), FROSTSTEEL(new ModMats(UGItemTiers.FROSTSTEEL, new FroststeelTool()), false, UGItems.FROSTSTEEL_INGOT, UGBlocks.FROSTSTEEL_BLOCK), UTHERIUM(new ModMats(UGItemTiers.UTHERIUM, new UteriumTool()), false, UGItems.UTHERIUM_CRYSTAL, UGBlocks.UTHERIUM_BLOCK), FORGOTTEN(new ModMats(UGItemTiers.FORGOTTEN, new ForgottenTool()), false, UGItems.FORGOTTEN_INGOT, UGBlocks.FORGOTTEN_BLOCK);

    private final IMatToolType type;

    private final boolean fireRes;

    private final Supplier<Item> ingot;

    private final Supplier<Block> block;

    private UGToolMats(IMatToolType type, boolean fireRes, Supplier<Item> ingot, Supplier<Block> block) {
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
        return (Item) UGCompat.ITEMS[this.ordinal()][type.ordinal()].get();
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
        return (Item) UGItems.UNDERBEAN_STICK.get();
    }

    @Override
    public boolean hasTool(LWToolTypes type) {
        return true;
    }

    @Override
    public void addEnchants(List<EnchantmentInstance> list, LWToolTypes type) {
        if (this.type.getExtraToolConfig() instanceof LWExtraConfig lw) {
            lw.addEnchants(list, type, this.getTool(type));
        }
    }

    @Override
    public String englishName() {
        return this.name();
    }

    @Override
    public Consumer<FinishedRecipe> getProvider(RegistrateRecipeProvider pvd, ICondition... cond) {
        List<ICondition> list = Lists.asList(new ModLoadedCondition("undergarden"), cond);
        return ConditionalRecipeWrapper.of(pvd, (ICondition[]) list.toArray(ICondition[]::new));
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}