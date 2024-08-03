package se.mickelus.tetra.items.modular.impl;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;

@ParametersAreNonnullByDefault
public class ModularSingleHeadedItem extends ItemModularHandheld {

    public static final String headKey = "single/head";

    public static final String handleKey = "single/handle";

    public static final String bindingKey = "single/binding";

    public static final String identifier = "modular_single";

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(1, -3, -11, 21);

    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-14, 0);

    @ObjectHolder(registryName = "item", value = "tetra:modular_single")
    public static ModularSingleHeadedItem instance;

    public ModularSingleHeadedItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.entityHitDamage = 1;
        this.majorModuleKeys = new String[] { "single/head", "single/handle" };
        this.minorModuleKeys = new String[] { "single/binding" };
        this.requiredModules = new String[] { "single/handle", "single/head" };
        this.updateConfig(ConfigHandler.honeSingleBase.get(), ConfigHandler.honeSingleIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_single"));
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("single/"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        return this.isThrowing(itemStack, entity) ? super.getModelCacheKey(itemStack, entity) + ":throwing" : super.getModelCacheKey(itemStack, entity);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        return this.isThrowing(itemStack, entity) ? "throwing" : null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }
}