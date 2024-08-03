package se.mickelus.tetra.items.modular.impl;

import com.google.common.collect.Lists;
import java.util.Collection;
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
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;

@ParametersAreNonnullByDefault
public class ModularBladedItem extends ItemModularHandheld {

    public static final String bladeKey = "sword/blade";

    public static final String hiltKey = "sword/hilt";

    public static final String guardKey = "sword/guard";

    public static final String pommelKey = "sword/pommel";

    public static final String fullerKey = "sword/fuller";

    public static final String identifier = "modular_sword";

    @ObjectHolder(registryName = "item", value = "tetra:modular_sword")
    public static ModularBladedItem instance;

    public ModularBladedItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.blockDestroyDamage = 2;
        this.majorModuleKeys = new String[] { "sword/blade", "sword/hilt" };
        this.minorModuleKeys = new String[] { "sword/fuller", "sword/guard", "sword/pommel" };
        this.requiredModules = new String[] { "sword/blade", "sword/hilt" };
        this.updateConfig(ConfigHandler.honeSwordBase.get(), ConfigHandler.honeSwordIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_sword"));
    }

    public static Collection<ItemStack> getCreativeTabItemStacks() {
        return Lists.newArrayList(new ItemStack[] { createItemStack("short_blade", "iron", "stick") });
    }

    public static ItemStack createItemStack(String blade, String bladeMaterial, String hiltMaterial) {
        ItemStack itemStack = new ItemStack(instance);
        IModularItem.putModuleInSlot(itemStack, "sword/blade", "sword/" + blade, "sword/" + blade + "_material", blade + "/" + bladeMaterial);
        IModularItem.putModuleInSlot(itemStack, "sword/hilt", "sword/basic_hilt", "sword/basic_hilt_material", "basic_hilt/" + hiltMaterial);
        IModularItem.updateIdentifier(itemStack);
        return itemStack;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("sword/"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        if (this.isThrowing(itemStack, entity)) {
            return super.getModelCacheKey(itemStack, entity) + ":throwing";
        } else {
            return this.isBlocking(itemStack, entity) ? super.getModelCacheKey(itemStack, entity) + ":blocking" : super.getModelCacheKey(itemStack, entity);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public String getTransformVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        if (this.isThrowing(itemStack, entity)) {
            return "throwing";
        } else {
            return this.isBlocking(itemStack, entity) ? "blocking" : null;
        }
    }
}