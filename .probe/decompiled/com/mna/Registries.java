package com.mna;

import com.mna.api.entities.construct.ai.ConstructTask;
import com.mna.api.faction.IFaction;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.tools.RLoc;
import com.mna.factions.NoneFaction;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

@EventBusSubscriber(modid = "mna", bus = Bus.MOD)
public class Registries {

    public static Supplier<IForgeRegistry<Shape>> Shape;

    public static Supplier<IForgeRegistry<SpellEffect>> SpellEffect;

    public static Supplier<IForgeRegistry<Modifier>> Modifier;

    public static Supplier<IForgeRegistry<RitualEffect>> RitualEffect;

    public static Supplier<IForgeRegistry<ConstructTask>> ConstructTasks;

    public static Supplier<IForgeRegistry<IFaction>> Factions;

    @SubscribeEvent
    public static void RegisterRegistries(NewRegistryEvent event) {
        RegistryBuilder<Shape> rbShapes = new RegistryBuilder<>();
        rbShapes.setName(RLoc.create("shapes")).set(new IForgeRegistry.MissingFactory<Shape>() {

            public Shape createMissing(ResourceLocation key, boolean isNetwork) {
                return new Shape.PhantomShape();
            }
        }).disableSaving().allowModification();
        Shape = event.create(rbShapes);
        RegistryBuilder<SpellEffect> rbComp = new RegistryBuilder<>();
        rbComp.setName(RLoc.create("components")).set(new IForgeRegistry.MissingFactory<SpellEffect>() {

            public SpellEffect createMissing(ResourceLocation key, boolean isNetwork) {
                return new SpellEffect.PhantomComponent();
            }
        }).disableSaving().allowModification();
        SpellEffect = event.create(rbComp);
        RegistryBuilder<Modifier> rbMod = new RegistryBuilder<>();
        rbMod.setName(RLoc.create("modifiers")).set(new IForgeRegistry.MissingFactory<Modifier>() {

            public Modifier createMissing(ResourceLocation key, boolean isNetwork) {
                return new Modifier.PhantomModifier();
            }
        }).disableSaving().allowModification();
        Modifier = event.create(rbMod);
        RegistryBuilder<RitualEffect> rbRitualEffects = new RegistryBuilder<>();
        rbRitualEffects.setName(RLoc.create("ritual-effects")).set(new IForgeRegistry.MissingFactory<RitualEffect>() {

            public RitualEffect createMissing(ResourceLocation key, boolean isNetwork) {
                return new RitualEffect.PhantomRitualEffect(key);
            }
        }).disableSaving().allowModification();
        RitualEffect = event.create(rbRitualEffects);
        RegistryBuilder rbConstructTasks = new RegistryBuilder();
        rbConstructTasks.setName(RLoc.create("construct_task")).set(new IForgeRegistry.MissingFactory<ConstructTask>() {

            public ConstructTask createMissing(ResourceLocation key, boolean isNetwork) {
                return new ConstructTask.PhantomTask();
            }
        }).disableSaving().allowModification();
        ConstructTasks = event.create(rbConstructTasks);
        RegistryBuilder rbFactions = new RegistryBuilder();
        rbFactions.setName(RLoc.create("factions")).set(new IForgeRegistry.MissingFactory<IFaction>() {

            public IFaction createMissing(ResourceLocation key, boolean isNetwork) {
                return new NoneFaction(key);
            }
        }).disableSaving().allowModification();
        Factions = event.create(rbFactions);
        ManaAndArtifice.LOGGER.info("Finished registering registries!");
    }
}