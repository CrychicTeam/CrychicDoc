package dev.xkmc.l2weaponry.compat.dragons;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.DragonSteelTier;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.google.common.collect.Lists;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2damagetracker.contents.materials.api.IMatToolType;
import dev.xkmc.l2library.serial.recipe.ConditionalRecipeWrapper;
import dev.xkmc.l2weaponry.compat.ModMats;
import dev.xkmc.l2weaponry.init.materials.ILWToolMats;
import dev.xkmc.l2weaponry.init.materials.LWToolTypes;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

public enum DragonToolMats implements ILWToolMats {

    ICE_DRAGONSTEEL(new ModMats(DragonSteelTier.DRAGONSTEEL_TIER_ICE, new IceDragonBoneTool()), true, IafItemRegistry.DRAGONSTEEL_ICE_INGOT, IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK), FIRE_DRAGONSTEEL(new ModMats(DragonSteelTier.DRAGONSTEEL_TIER_FIRE, new FireDragonBoneTool()), true, IafItemRegistry.DRAGONSTEEL_FIRE_INGOT, IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK), LIGHTNING_DRAGONSTEEL(new ModMats(DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING, new LightningDragonBoneTool()), true, IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT, IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK);

    private final IMatToolType type;

    private final boolean fireRes;

    private final Supplier<Item> ingot;

    private final Supplier<Block> block;

    private DragonToolMats(IMatToolType type, boolean fireRes, Supplier<Item> ingot, Supplier<Block> block) {
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
        return (Item) DragonCompat.ITEMS[this.ordinal()][type.ordinal()].get();
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
        return IafItemRegistry.WITHERBONE.get();
    }

    @Override
    public Consumer<FinishedRecipe> getProvider(RegistrateRecipeProvider pvd, ICondition... cond) {
        List<ICondition> list = Lists.asList(new ModLoadedCondition("iceandfire"), cond);
        return ConditionalRecipeWrapper.of(pvd, (ICondition[]) list.toArray(ICondition[]::new));
    }

    @Override
    public boolean isOptional() {
        return true;
    }
}