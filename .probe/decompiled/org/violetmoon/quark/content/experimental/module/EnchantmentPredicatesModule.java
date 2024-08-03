package org.violetmoon.quark.content.experimental.module;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.RegistryUtil;

@ZetaLoadModule(category = "experimental", enabledByDefault = false, description = "This feature generates Resource Pack Item Model predicates on the items defined in 'Items to Change'\nfor the Enchantments defined in 'Enchantments to Register'.\n\nExample: if 'minecraft:silk_touch' is added to 'Enchantments to Register', and 'minecraft:netherite_pickaxe'\nis added to 'Items to Change', then a predicate named 'quark_has_enchant_minecraft_silk_touch' will be available\nto the netherite_pickaxe.json item model, whose value will be the enchantment level.")
public class EnchantmentPredicatesModule extends ZetaModule {

    @Config
    public static List<String> itemsToChange = Lists.newArrayList();

    @Config
    public static List<String> enchantmentsToRegister = Lists.newArrayList();

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends EnchantmentPredicatesModule {

        @LoadEvent
        public void clientSetup(ZClientSetup e) {
            if (this.enabled) {
                e.enqueueWork(() -> {
                    List<Item> items = RegistryUtil.massRegistryGet(itemsToChange, BuiltInRegistries.ITEM);
                    for (Enchantment enchant : RegistryUtil.massRegistryGet(enchantmentsToRegister, BuiltInRegistries.ENCHANTMENT)) {
                        ResourceLocation enchantRes = BuiltInRegistries.ENCHANTMENT.getKey(enchant);
                        ResourceLocation name = new ResourceLocation("quark_has_enchant_" + enchantRes.getNamespace() + "_" + enchantRes.getPath());
                        ItemPropertyFunction fun = (stack, level, entity, i) -> (float) EnchantmentHelper.getTagEnchantmentLevel(enchant, stack);
                        for (Item item : items) {
                            ItemProperties.register(item, name, fun);
                        }
                    }
                });
            }
        }
    }
}