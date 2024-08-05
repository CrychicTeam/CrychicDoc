package dev.xkmc.l2weaponry.compat.aerial;

import com.google.common.collect.Lists;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2damagetracker.contents.materials.api.IMatToolType;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2weaponry.compat.ModMats;
import dev.xkmc.l2weaponry.init.materials.ILWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import fr.factionbedrock.aerialhell.Item.Material.ToolMaterials;
import fr.factionbedrock.aerialhell.Registry.AerialHellBlocksAndItems;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

public enum AHToolMats implements ILWToolMats {

    ARSONIST(new ModMats(ToolMaterials.arsonist, new ArsonistTool()), true, AerialHellBlocksAndItems.ARSONIST_INGOT, AerialHellBlocksAndItems.ARSONIST_BLOCK), LUNAR(new ModMats(ToolMaterials.lunatic, new ExtraToolConfig()), false, AerialHellBlocksAndItems.LUNATIC_CRYSTAL, AerialHellBlocksAndItems.LUNATIC_CRYSTAL_BLOCK), RUBY(new ModMats(ToolMaterials.ruby, new ExtraToolConfig()), false, AerialHellBlocksAndItems.RUBY, AerialHellBlocksAndItems.RUBY_BLOCK), VOLUCITE(new ModMats(ToolMaterials.volucite, new VoluciteTool()), false, AerialHellBlocksAndItems.VOLUCITE_VIBRANT, AerialHellBlocksAndItems.VOLUCITE_BLOCK);

    private final IMatToolType type;

    private final boolean fireRes;

    private final Supplier<Item> ingot;

    private final Supplier<Block> block;

    private AHToolMats(IMatToolType type, boolean fireRes, Supplier<Item> ingot, Supplier<Block> block) {
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
        return (Item) AHCompat.ITEMS[this.ordinal()][type.ordinal()].get();
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
        return (Item) LWItems.HANDLE.get();
    }

    @Override
    public Consumer<FinishedRecipe> getProvider(RegistrateRecipeProvider pvd, ICondition... cond) {
        List<ICondition> list = Lists.asList(new ModLoadedCondition("aerialhell"), cond);
        return ConditionalRecipeWrapper.of(pvd, (ICondition[]) list.toArray(ICondition[]::new));
    }

    @Override
    public boolean hasTool(LWToolTypes type) {
        if (type == LWToolTypes.NUNCHAKU) {
            return false;
        } else {
            return type != LWToolTypes.PLATE_SHIELD && type != LWToolTypes.ROUND_SHIELD ? ILWToolMats.super.hasTool(type) : this != RUBY && this != LUNAR;
        }
    }

    @Override
    public String prefix() {
        if (this == ARSONIST) {
            return "§c";
        } else if (this == LUNAR) {
            return "§6";
        } else {
            return this == VOLUCITE ? "§2" : "";
        }
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}