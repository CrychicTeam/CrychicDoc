package se.mickelus.tetra.items.modular.impl.shield;

import com.google.common.collect.Multimap;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.NonNullLazy;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.module.ItemModuleMajor;
import se.mickelus.tetra.module.ItemUpgradeRegistry;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.properties.TetraAttributes;

@ParametersAreNonnullByDefault
public class ModularShieldItem extends ItemModularHandheld {

    public static final String plateKey = "shield/plate";

    public static final String gripKey = "shield/grip";

    public static final String bossKey = "shield/boss";

    public static final String identifier = "modular_shield";

    public static final String bannerImprovementKey = "shield/banner";

    @ObjectHolder(registryName = "item", value = "tetra:modular_shield")
    public static ModularShieldItem instance;

    public ModularShieldItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.majorModuleKeys = new String[] { "shield/plate", "shield/grip" };
        this.minorModuleKeys = new String[] { "shield/boss" };
        this.requiredModules = new String[] { "shield/plate", "shield/grip" };
        this.updateConfig(ConfigHandler.honeShieldBase.get(), ConfigHandler.honeShieldIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_shield"));
        SchematicRegistry.instance.registerSchematic(new ApplyBannerSchematic());
        ItemUpgradeRegistry.instance.registerReplacementHook(this::copyBanner);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        final NonNullLazy<BlockEntityWithoutLevelRenderer> renderer = NonNullLazy.of(() -> new ModularShieldRenderer(Minecraft.getInstance()));
        consumer.accept(new IClientItemExtensions() {

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer.get();
            }
        });
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("shield/"));
    }

    @Override
    public void clientInit() {
        super.clientInit();
        ItemProperties.register(this, new ResourceLocation("blocking"), (itemStack, world, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == itemStack ? 1.0F : 0.0F);
    }

    private ItemStack copyBanner(ItemStack original, ItemStack replacement) {
        if (this.equals(replacement.getItem())) {
            Optional.ofNullable(original.getTagElement("BlockEntityTag")).ifPresent(tag -> {
                replacement.getOrCreateTag().put("BlockEntityTag", tag);
                CastOptional.cast(this.getModuleFromSlot(replacement, "shield/plate"), ItemModuleMajor.class).filter(module -> module.acceptsImprovement("shield/banner")).ifPresent(module -> module.addImprovement(replacement, "shield/banner", 0));
            });
        }
        return replacement;
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        return slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND ? AttributeHelper.emptyMap : this.getAttributeModifiersCached(itemStack);
    }

    @Override
    public double getAbilityBaseDamage(ItemStack itemStack) {
        return this.getAttributeValue(itemStack, TetraAttributes.abilityDamage.get());
    }

    @Override
    public double getCooldownBase(ItemStack itemStack) {
        return this.getAttributeValue(itemStack, TetraAttributes.abilityCooldown.get());
    }
}