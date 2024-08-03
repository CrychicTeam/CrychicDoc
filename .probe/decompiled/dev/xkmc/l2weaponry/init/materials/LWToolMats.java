package dev.xkmc.l2weaponry.init.materials;

import dev.xkmc.l2complements.init.materials.LCMats;
import dev.xkmc.l2damagetracker.contents.materials.api.IMatToolType;
import dev.xkmc.l2damagetracker.contents.materials.vanilla.VanillaMats;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import org.jetbrains.annotations.Nullable;

public enum LWToolMats implements ILWToolMats {

    IRON(new VanillaMats(Tiers.IRON), Items.IRON_NUGGET, Items.IRON_INGOT, Items.IRON_BLOCK, false),
    GOLD(new VanillaMats(Tiers.GOLD), Items.GOLD_NUGGET, Items.GOLD_INGOT, Items.GOLD_BLOCK, false),
    DIAMOND(new VanillaMats(Tiers.DIAMOND), Items.AIR, Items.DIAMOND, Items.DIAMOND_BLOCK, false),
    NETHERITE(new VanillaMats(Tiers.NETHERITE), Items.AIR, Items.NETHERITE_INGOT, Items.NETHERITE_BLOCK, true),
    TOTEMIC_GOLD(LCMats.TOTEMIC_GOLD, false),
    POSEIDITE(LCMats.POSEIDITE, false),
    SHULKERATE(LCMats.SHULKERATE, false),
    SCULKIUM(LCMats.SCULKIUM, true),
    ETERNIUM(LCMats.ETERNIUM, true);

    public final IMatToolType type;

    private final Supplier<Item> nugget;

    private final Supplier<Item> ingot;

    private final Supplier<Item> block;

    private final Supplier<Item> toolIngot;

    private final Supplier<Item> handle;

    final boolean fireRes;

    private LWToolMats(IMatToolType type, Item nugget, Item ingot, Item block, boolean fireRes) {
        this.type = type;
        this.nugget = () -> nugget;
        this.ingot = () -> ingot;
        this.block = () -> block;
        this.fireRes = fireRes;
        this.toolIngot = this.ingot;
        this.handle = LWItems.HANDLE;
    }

    private LWToolMats(LCMats type, boolean fireRes) {
        this.type = type;
        this.nugget = type::getNugget;
        this.ingot = type::getIngot;
        this.block = () -> type.getBlock().asItem();
        this.toolIngot = type::getToolIngot;
        this.handle = type::getToolStick;
        this.fireRes = fireRes;
    }

    @Override
    public Item getIngot() {
        return (Item) this.ingot.get();
    }

    public Item getToolIngot() {
        return (Item) this.toolIngot.get();
    }

    @Override
    public Item getStick() {
        return (Item) this.handle.get();
    }

    @Nullable
    @Override
    public ILWToolMats getBaseUpgrade() {
        return this == NETHERITE ? DIAMOND : null;
    }

    @Override
    public boolean is3D(LWToolTypes type) {
        return type == LWToolTypes.BATTLE_AXE || type == LWToolTypes.HAMMER || type == LWToolTypes.SPEAR;
    }

    @Override
    public Item getBlock() {
        return (Item) this.block.get();
    }

    @Override
    public Item getTool(LWToolTypes type) {
        return (Item) LWItems.GEN_ITEM[this.ordinal()][type.ordinal()].get();
    }

    public Item getNugget() {
        return (Item) this.nugget.get();
    }

    @Override
    public IMatToolType type() {
        return this.type;
    }

    @Override
    public boolean fireRes() {
        return this.fireRes;
    }
}