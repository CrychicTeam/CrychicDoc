package dev.xkmc.l2hostility.compat.data;

import com.github.L_Ender.cataclysm.init.ModEntities;
import com.github.L_Ender.cataclysm.init.ModItems;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import dev.xkmc.l2complements.init.registrate.LCEnchantments;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.L2Hostility;
import dev.xkmc.l2hostility.init.data.LHConfigGen;
import dev.xkmc.l2hostility.init.registrate.LHEnchantments;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2hostility.init.registrate.LHTraits;
import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.ConfigDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

public class CataclysmData {

    public static void genRecipe(RegistrateRecipeProvider pvd) {
    }

    public static void genConfig(ConfigDataProvider.Collector collector) {
        int equipLevel = 600;
        LHConfigGen.addEntity(collector, 200, 50, ModEntities.ENDER_GUARDIAN, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 2, 2), EntityConfig.trait((MobTrait) LHTraits.WEAKNESS.get(), 3, 5)), List.of(divinity(equipLevel), breaker(equipLevel)));
        LHConfigGen.addEntity(collector, 200, 50, ModEntities.NETHERITE_MONSTROSITY, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 2, 2), EntityConfig.trait((MobTrait) LHTraits.SLOWNESS.get(), 3, 5)), List.of(divinity(equipLevel), breaker(equipLevel)));
        collector.add(L2Hostility.ENTITY, ModEntities.IGNIS.getId(), new EntityConfig().put(EntityConfig.entity(200, 50, 0.0, 0.0, List.of((EntityType) ModEntities.IGNIS.get())).trait(List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.SOUL_BURNER.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.MASTER.get(), 1, 0))).item(List.of(EntityConfig.simplePool(equipLevel, "equipment/mainhand", getIgnisWeapon()), divinity(equipLevel), breaker(equipLevel))).master(5, 80, new EntityConfig.Minion(EntityType.BLAZE, 2, 0, 0.9, 16, 100, true, false, 32.0, false, true), new EntityConfig.Minion((EntityType<?>) ModEntities.IGNITED_REVENANT.get(), 2, 300, 0.67, 16, 400, true, false, 32.0, true, true), new EntityConfig.Minion((EntityType<?>) ModEntities.IGNITED_BERSERKER.get(), 1, 400, 0.33, 16, 400, true, false, 32.0, true, true))));
        LHConfigGen.addEntity(collector, 200, 50, ModEntities.THE_HARBINGER, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.CURSED.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.WITHER.get(), 2, 3)), List.of(divinity(equipLevel)));
        LHConfigGen.addEntity(collector, 200, 50, ModEntities.THE_LEVIATHAN, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REFLECT.get(), 2, 2), EntityConfig.trait((MobTrait) LHTraits.FREEZING.get(), 2, 3)), List.of(divinity(equipLevel)));
        LHConfigGen.addEntity(collector, 200, 50, ModEntities.ANCIENT_REMNANT, List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REPRINT.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 2, 3)), List.of(divinity(equipLevel)));
        collector.add(L2Hostility.ENTITY, ModEntities.ANCIENT_REMNANT.getId(), new EntityConfig().put(EntityConfig.entity(200, 50, 0.0, 0.0, List.of((EntityType) ModEntities.ANCIENT_REMNANT.get())).trait(List.of(EntityConfig.trait((MobTrait) LHTraits.TANK.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.REPRINT.get(), 1, 1), EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.MASTER.get(), 1, 0))).item(List.of(divinity(equipLevel))).master(4, 80, new EntityConfig.Minion((EntityType<?>) ModEntities.KOBOLETON.get(), 2, 0, 0.8, 16, 250, true, false, 32.0, false, true), new EntityConfig.Minion((EntityType<?>) ModEntities.KOBOLEDIATOR.get(), 1, 300, 0.6, 16, 400, true, false, 32.0, true, true), new EntityConfig.Minion((EntityType<?>) ModEntities.WADJET.get(), 1, 300, 0.6, 16, 400, true, false, 32.0, true, true))));
        LHConfigGen.addEntity(collector, 100, 30, ModEntities.ENDER_GOLEM, EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 0, 1), EntityConfig.trait((MobTrait) LHTraits.WEAKNESS.get(), 0, 1));
        LHConfigGen.addEntity(collector, 100, 30, ModEntities.AMETHYST_CRAB, EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 0, 1), EntityConfig.trait((MobTrait) LHTraits.POISON.get(), 0, 1));
        LHConfigGen.addEntity(collector, 100, 30, ModEntities.IGNITED_BERSERKER, EntityConfig.trait((MobTrait) LHTraits.REFLECT.get(), 0, 1), EntityConfig.trait((MobTrait) LHTraits.DRAIN.get(), 0, 1));
        LHConfigGen.addEntity(collector, 100, 30, ModEntities.KOBOLEDIATOR, EntityConfig.trait((MobTrait) LHTraits.ADAPTIVE.get(), 0, 1), EntityConfig.trait((MobTrait) LHTraits.DISPELL.get(), 0, 1, 250, 1.0F));
        LHConfigGen.addEntity(collector, 100, 30, ModEntities.WADJET, EntityConfig.trait((MobTrait) LHTraits.PROTECTION.get(), 2, 3), EntityConfig.trait((MobTrait) LHTraits.DEMENTOR.get(), 0, 1, 250, 1.0F));
    }

    private static ItemStack getIgnisWeapon() {
        ItemStack stack = ((Item) ModItems.THE_INCINERATOR.get()).getDefaultInstance();
        stack.enchant((Enchantment) LCEnchantments.VOID_TOUCH.get(), 10);
        stack.enchant(Enchantments.VANISHING_CURSE, 1);
        stack.enchant((Enchantment) LHEnchantments.VANISH.get(), 1);
        return stack;
    }

    private static EntityConfig.ItemPool divinity(int lv) {
        ItemStack stack = LHItems.RING_DIVINITY.asStack();
        stack.enchant(Enchantments.VANISHING_CURSE, 1);
        return EntityConfig.simplePool(lv, "curios/ring", stack);
    }

    private static EntityConfig.ItemPool breaker(int lv) {
        ItemStack stack = LHItems.IMAGINE_BREAKER.asStack();
        stack.enchant(Enchantments.VANISHING_CURSE, 1);
        return EntityConfig.simplePool(lv, "curios/hands", stack);
    }

    public static void genSlot(BiConsumer<String, Record> map) {
        map.accept("l2hostility/curios/entities/l2hostility_cataclysm", new CurioEntityBuilder(new ArrayList(List.of(ModEntities.ENDER_GUARDIAN.getId(), ModEntities.NETHERITE_MONSTROSITY.getId(), ModEntities.IGNIS.getId(), ModEntities.THE_HARBINGER.getId(), ModEntities.THE_LEVIATHAN.getId())), new ArrayList(List.of("ring", "hands")), SlotCondition.of()));
    }
}