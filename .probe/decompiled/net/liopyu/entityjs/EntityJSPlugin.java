package net.liopyu.entityjs;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.liopyu.entityjs.builders.living.entityjs.AnimalEntityJSBuilder;
import net.liopyu.entityjs.builders.living.entityjs.BaseLivingEntityJSBuilder;
import net.liopyu.entityjs.builders.living.entityjs.MobEntityJSBuilder;
import net.liopyu.entityjs.builders.living.entityjs.TameableMobJSBuilder;
import net.liopyu.entityjs.builders.living.entityjs.WaterEntityJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.AllayJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.AxolotlJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.BatJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.BeeJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.BlazeJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.CamelJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.CatJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.ChickenJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.CowJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.CreeperJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.DolphinJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.DonkeyJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.EnderManJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.EvokerJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.GhastJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.GoatJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.GuardianJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.HorseJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.IllusionerJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.IronGolemJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.PandaJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.ParrotJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.PiglinJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.WitherJSBuilder;
import net.liopyu.entityjs.builders.living.vanilla.ZombieJSBuilder;
import net.liopyu.entityjs.builders.nonliving.entityjs.ArrowEntityJSBuilder;
import net.liopyu.entityjs.builders.nonliving.entityjs.BaseEntityJSBuilder;
import net.liopyu.entityjs.builders.nonliving.entityjs.ProjectileEntityJSBuilder;
import net.liopyu.entityjs.builders.nonliving.modded.CGMProjectileEntityJSBuilder;
import net.liopyu.entityjs.builders.nonliving.vanilla.BoatJSBuilder;
import net.liopyu.entityjs.builders.nonliving.vanilla.EyeOfEnderJSBuilder;
import net.liopyu.entityjs.util.EntityJSUtils;
import net.liopyu.entityjs.util.EventHandlers;
import org.slf4j.Logger;

public class EntityJSPlugin extends KubeJSPlugin {

    public static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public void init() {
        RegistryInfo.ENTITY_TYPE.addType("entityjs:nonliving", BaseEntityJSBuilder.class, BaseEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:living", BaseLivingEntityJSBuilder.class, BaseLivingEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:mob", MobEntityJSBuilder.class, MobEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:animal", AnimalEntityJSBuilder.class, AnimalEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:watercreature", WaterEntityJSBuilder.class, WaterEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:tamable", TameableMobJSBuilder.class, TameableMobJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:arrow", ArrowEntityJSBuilder.class, ArrowEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("entityjs:projectile", ProjectileEntityJSBuilder.class, ProjectileEntityJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:zombie", ZombieJSBuilder.class, ZombieJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:allay", AllayJSBuilder.class, AllayJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:axolotl", AxolotlJSBuilder.class, AxolotlJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:bat", BatJSBuilder.class, BatJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:bee", BeeJSBuilder.class, BeeJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:blaze", BlazeJSBuilder.class, BlazeJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:boat", BoatJSBuilder.class, BoatJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:camel", CamelJSBuilder.class, CamelJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:cat", CatJSBuilder.class, CatJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:chicken", ChickenJSBuilder.class, ChickenJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:cow", CowJSBuilder.class, CowJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:creeper", CreeperJSBuilder.class, CreeperJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:dolphin", DolphinJSBuilder.class, DolphinJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:donkey", DonkeyJSBuilder.class, DonkeyJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:enderman", EnderManJSBuilder.class, EnderManJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:evoker", EvokerJSBuilder.class, EvokerJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:ghast", GhastJSBuilder.class, GhastJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:goat", GoatJSBuilder.class, GoatJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:guardian", GuardianJSBuilder.class, GuardianJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:horse", HorseJSBuilder.class, HorseJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:illusioner", IllusionerJSBuilder.class, IllusionerJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:iron_golem", IronGolemJSBuilder.class, IronGolemJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:panda", PandaJSBuilder.class, PandaJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:parrot", ParrotJSBuilder.class, ParrotJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:eye_of_ender", EyeOfEnderJSBuilder.class, EyeOfEnderJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:piglin", PiglinJSBuilder.class, PiglinJSBuilder::new);
        RegistryInfo.ENTITY_TYPE.addType("minecraft:wither", WitherJSBuilder.class, WitherJSBuilder::new);
        if (Platform.isModLoaded("cgm") && Platform.isModLoaded("framework")) {
            LOGGER.info("Loading CGM-EntityJS compatibility");
            RegistryInfo.ENTITY_TYPE.addType("cgm:ammo", CGMProjectileEntityJSBuilder.class, CGMProjectileEntityJSBuilder::new);
        }
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("EntityJSUtils", EntityJSUtils.class);
    }

    @Override
    public void registerEvents() {
        EventHandlers.EntityJSEvents.register();
    }
}