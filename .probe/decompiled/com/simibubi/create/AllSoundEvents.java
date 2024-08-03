package com.simibubi.create;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

public class AllSoundEvents {

    public static final Map<ResourceLocation, AllSoundEvents.SoundEntry> ALL = new HashMap();

    public static final AllSoundEvents.SoundEntry SCHEMATICANNON_LAUNCH_BLOCK = create("schematicannon_launch_block").subtitle("Schematicannon fires").playExisting(SoundEvents.GENERIC_EXPLODE, 0.1F, 1.1F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry SCHEMATICANNON_FINISH = create("schematicannon_finish").subtitle("Schematicannon dings").playExisting(SoundEvents.NOTE_BLOCK_BELL, 1.0F, 0.7F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry DEPOT_SLIDE = create("depot_slide").subtitle("Item slides").playExisting(SoundEvents.SAND_BREAK, 0.125F, 1.5F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry DEPOT_PLOP = create("depot_plop").subtitle("Item lands").playExisting(SoundEvents.ITEM_FRAME_ADD_ITEM, 0.25F, 1.25F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry FUNNEL_FLAP = create("funnel_flap").subtitle("Funnel flaps").playExisting(SoundEvents.ITEM_FRAME_ROTATE_ITEM, 0.125F, 1.5F).playExisting(SoundEvents.WOOL_BREAK, 0.0425F, 0.75F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry SLIME_ADDED = create("slime_added").subtitle("Slime squishes").playExisting(SoundEvents.SLIME_BLOCK_PLACE).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry MECHANICAL_PRESS_ACTIVATION = create("mechanical_press_activation").subtitle("Mechanical Press clangs").playExisting(SoundEvents.ANVIL_LAND, 0.125F, 1.0F).playExisting(SoundEvents.ITEM_BREAK, 0.5F, 1.0F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry MECHANICAL_PRESS_ACTIVATION_ON_BELT = create("mechanical_press_activation_belt").subtitle("Mechanical Press bonks").playExisting(SoundEvents.WOOL_HIT, 0.75F, 1.0F).playExisting(SoundEvents.ITEM_BREAK, 0.15F, 0.75F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry MIXING = create("mixing").subtitle("Mixing noises").playExisting(SoundEvents.GILDED_BLACKSTONE_BREAK, 0.125F, 0.5F).playExisting(SoundEvents.NETHERRACK_BREAK, 0.125F, 0.5F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CRANKING = create("cranking").subtitle("Hand Crank turns").playExisting(SoundEvents.WOOD_PLACE, 0.075F, 0.5F).playExisting(SoundEvents.WOODEN_BUTTON_CLICK_OFF, 0.025F, 0.5F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry WORLDSHAPER_PLACE = create("worldshaper_place").subtitle("Worldshaper zaps").playExisting(SoundEvents.NOTE_BLOCK_BASEDRUM).category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry SCROLL_VALUE = create("scroll_value").subtitle("Scroll-input clicks").playExisting(SoundEvents.NOTE_BLOCK_HAT, 0.124F, 1.0F).category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry CONFIRM = create("confirm").subtitle("Affirmative ding").playExisting(SoundEvents.NOTE_BLOCK_BELL, 0.5F, 0.8F).category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry DENY = create("deny").subtitle("Declining boop").playExisting(SoundEvents.NOTE_BLOCK_BASS, 1.0F, 0.5F).category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry COGS = create("cogs").subtitle("Cogwheels rumble").category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry FWOOMP = create("fwoomp").subtitle("Potato Launcher fwoomps").category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry POTATO_HIT = create("potato_hit").subtitle("Vegetable impacts").playExisting(SoundEvents.ITEM_FRAME_BREAK, 0.75F, 0.75F).playExisting(SoundEvents.WEEPING_VINES_BREAK, 0.75F, 1.25F).category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry CONTRAPTION_ASSEMBLE = create("contraption_assemble").subtitle("Contraption moves").playExisting(SoundEvents.WOODEN_TRAPDOOR_OPEN, 0.5F, 0.5F).playExisting(SoundEvents.CHEST_OPEN, 0.045F, 0.74F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CONTRAPTION_DISASSEMBLE = create("contraption_disassemble").subtitle("Contraption stops").playExisting(SoundEvents.IRON_TRAPDOOR_CLOSE, 0.35F, 0.75F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry WRENCH_ROTATE = create("wrench_rotate").subtitle("Wrench used").playExisting(SoundEvents.WOODEN_TRAPDOOR_CLOSE, 0.25F, 1.25F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry WRENCH_REMOVE = create("wrench_remove").subtitle("Component breaks").playExisting(SoundEvents.ITEM_PICKUP, 0.25F, 0.75F).playExisting(SoundEvents.NETHERITE_BLOCK_HIT, 0.25F, 0.75F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CRAFTER_CLICK = create("crafter_click").subtitle("Crafter clicks").playExisting(SoundEvents.NETHERITE_BLOCK_HIT, 0.25F, 1.0F).playExisting(SoundEvents.WOODEN_TRAPDOOR_OPEN, 0.125F, 1.0F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CRAFTER_CRAFT = create("crafter_craft").subtitle("Crafter crafts").playExisting(SoundEvents.ITEM_BREAK, 0.125F, 0.75F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry COPPER_ARMOR_EQUIP = create("copper_armor_equip").subtitle("Diving equipment clinks").playExisting(SoundEvents.ARMOR_EQUIP_GOLD, 1.0F, 1.0F).category(SoundSource.PLAYERS).build();

    public static final AllSoundEvents.SoundEntry SANDING_SHORT = create("sanding_short").subtitle("Sanding noises").addVariant("sanding_short_1").category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry SANDING_LONG = create("sanding_long").subtitle("Sanding noises").category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CONTROLLER_CLICK = create("controller_click").subtitle("Controller clicks").playExisting(SoundEvents.ITEM_FRAME_ADD_ITEM, 0.35F, 1.0F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CONTROLLER_PUT = create("controller_put").subtitle("Controller thumps").playExisting(SoundEvents.BOOK_PUT, 1.0F, 1.0F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CONTROLLER_TAKE = create("controller_take").subtitle("Lectern empties").playExisting(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry SAW_ACTIVATE_WOOD = create("saw_activate_wood").subtitle("Mechanical Saw activates").playExisting(SoundEvents.BOAT_PADDLE_LAND, 0.75F, 1.5F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry SAW_ACTIVATE_STONE = create("saw_activate_stone").subtitle("Mechanical Saw activates").playExisting(SoundEvents.UI_STONECUTTER_TAKE_RESULT, 0.125F, 1.25F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry BLAZE_MUNCH = create("blaze_munch").subtitle("Blaze Burner munches").playExisting(SoundEvents.GENERIC_EAT, 0.5F, 1.0F).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CRUSHING_1 = create("crushing_1").subtitle("Crushing noises").playExisting(SoundEvents.NETHERRACK_HIT).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CRUSHING_2 = create("crushing_2").noSubtitle().playExisting(SoundEvents.GRAVEL_PLACE).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry CRUSHING_3 = create("crushing_3").noSubtitle().playExisting(SoundEvents.NETHERITE_BLOCK_BREAK).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry PECULIAR_BELL_USE = create("peculiar_bell_use").subtitle("Peculiar Bell tolls").playExisting(SoundEvents.BELL_BLOCK).category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_HIGH = create("whistle_high").subtitle("High whistling").category(SoundSource.RECORDS).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_MEDIUM = create("whistle").subtitle("Whistling").category(SoundSource.RECORDS).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_LOW = create("whistle_low").subtitle("Low whistling").category(SoundSource.RECORDS).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry STEAM = create("steam").subtitle("Steam noises").category(SoundSource.NEUTRAL).attenuationDistance(32).build();

    public static final AllSoundEvents.SoundEntry TRAIN = create("train").subtitle("Bogey wheels rumble").category(SoundSource.NEUTRAL).attenuationDistance(128).build();

    public static final AllSoundEvents.SoundEntry TRAIN2 = create("train2").noSubtitle().category(SoundSource.NEUTRAL).attenuationDistance(128).build();

    public static final AllSoundEvents.SoundEntry TRAIN3 = create("train3").subtitle("Bogey wheels rumble muffled").category(SoundSource.NEUTRAL).attenuationDistance(16).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_TRAIN = create("whistle_train").subtitle("Whistling").category(SoundSource.RECORDS).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_TRAIN_LOW = create("whistle_train_low").subtitle("Low whistling").category(SoundSource.RECORDS).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_TRAIN_MANUAL = create("whistle_train_manual").subtitle("Train honks").category(SoundSource.NEUTRAL).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_TRAIN_MANUAL_LOW = create("whistle_train_manual_low").subtitle("Train honks").category(SoundSource.NEUTRAL).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_TRAIN_MANUAL_END = create("whistle_train_manual_end").noSubtitle().category(SoundSource.NEUTRAL).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_TRAIN_MANUAL_LOW_END = create("whistle_train_manual_low_end").noSubtitle().category(SoundSource.NEUTRAL).attenuationDistance(64).build();

    public static final AllSoundEvents.SoundEntry WHISTLE_CHIFF = create("chiff").noSubtitle().category(SoundSource.RECORDS).build();

    public static final AllSoundEvents.SoundEntry HAUNTED_BELL_CONVERT = create("haunted_bell_convert").subtitle("Haunted Bell awakens").category(SoundSource.BLOCKS).build();

    public static final AllSoundEvents.SoundEntry HAUNTED_BELL_USE = create("haunted_bell_use").subtitle("Haunted Bell tolls").category(SoundSource.BLOCKS).build();

    private static AllSoundEvents.SoundEntryBuilder create(String name) {
        return create(Create.asResource(name));
    }

    public static AllSoundEvents.SoundEntryBuilder create(ResourceLocation id) {
        return new AllSoundEvents.SoundEntryBuilder(id);
    }

    public static void prepare() {
        for (AllSoundEvents.SoundEntry entry : ALL.values()) {
            entry.prepare();
        }
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> {
            for (AllSoundEvents.SoundEntry entry : ALL.values()) {
                entry.register(helper);
            }
        });
    }

    public static void provideLang(BiConsumer<String, String> consumer) {
        for (AllSoundEvents.SoundEntry entry : ALL.values()) {
            if (entry.hasSubtitle()) {
                consumer.accept(entry.getSubtitleKey(), entry.getSubtitle());
            }
        }
    }

    public static AllSoundEvents.SoundEntryProvider provider(DataGenerator generator) {
        return new AllSoundEvents.SoundEntryProvider(generator);
    }

    public static void playItemPickup(Player player) {
        player.m_9236_().playSound(null, player.m_20183_(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, 1.0F + Create.RANDOM.nextFloat());
    }

    public static record ConfiguredSoundEvent(Supplier<SoundEvent> event, float volume, float pitch) {
    }

    private static class CustomSoundEntry extends AllSoundEvents.SoundEntry {

        protected List<ResourceLocation> variants;

        protected RegistryObject<SoundEvent> event;

        public CustomSoundEntry(ResourceLocation id, List<ResourceLocation> variants, String subtitle, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.variants = variants;
        }

        @Override
        public void prepare() {
            this.event = RegistryObject.create(this.id, ForgeRegistries.SOUND_EVENTS);
        }

        @Override
        public void register(RegisterEvent.RegisterHelper<SoundEvent> helper) {
            ResourceLocation location = this.event.getId();
            helper.register(location, SoundEvent.createVariableRangeEvent(location));
        }

        @Override
        public SoundEvent getMainEvent() {
            return this.event.get();
        }

        @Override
        public void write(JsonObject json) {
            JsonObject entry = new JsonObject();
            JsonArray list = new JsonArray();
            JsonObject s = new JsonObject();
            s.addProperty("name", this.id.toString());
            s.addProperty("type", "file");
            if (this.attenuationDistance != 0) {
                s.addProperty("attenuation_distance", this.attenuationDistance);
            }
            list.add(s);
            for (ResourceLocation variant : this.variants) {
                s = new JsonObject();
                s.addProperty("name", variant.toString());
                s.addProperty("type", "file");
                if (this.attenuationDistance != 0) {
                    s.addProperty("attenuation_distance", this.attenuationDistance);
                }
                list.add(s);
            }
            entry.add("sounds", list);
            if (this.hasSubtitle()) {
                entry.addProperty("subtitle", this.getSubtitleKey());
            }
            json.add(this.id.getPath(), entry);
        }

        @Override
        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            world.playSound(entity, x, y, z, this.event.get(), this.category, volume, pitch);
        }

        @Override
        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            world.playLocalSound(x, y, z, this.event.get(), this.category, volume, pitch, fade);
        }
    }

    public abstract static class SoundEntry {

        protected ResourceLocation id;

        protected String subtitle;

        protected SoundSource category;

        protected int attenuationDistance;

        public SoundEntry(ResourceLocation id, String subtitle, SoundSource category, int attenuationDistance) {
            this.id = id;
            this.subtitle = subtitle;
            this.category = category;
            this.attenuationDistance = attenuationDistance;
        }

        public abstract void prepare();

        public abstract void register(RegisterEvent.RegisterHelper<SoundEvent> var1);

        public abstract void write(JsonObject var1);

        public abstract SoundEvent getMainEvent();

        public String getSubtitleKey() {
            return this.id.getNamespace() + ".subtitle." + this.id.getPath();
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public boolean hasSubtitle() {
            return this.subtitle != null;
        }

        public String getSubtitle() {
            return this.subtitle;
        }

        public void playOnServer(Level world, Vec3i pos) {
            this.playOnServer(world, pos, 1.0F, 1.0F);
        }

        public void playOnServer(Level world, Vec3i pos, float volume, float pitch) {
            this.play(world, null, pos, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3i pos) {
            this.play(world, entity, pos, 1.0F, 1.0F);
        }

        public void playFrom(Entity entity) {
            this.playFrom(entity, 1.0F, 1.0F);
        }

        public void playFrom(Entity entity, float volume, float pitch) {
            if (!entity.isSilent()) {
                this.play(entity.level(), null, entity.blockPosition(), volume, pitch);
            }
        }

        public void play(Level world, Player entity, Vec3i pos, float volume, float pitch) {
            this.play(world, entity, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, volume, pitch);
        }

        public void play(Level world, Player entity, Vec3 pos, float volume, float pitch) {
            this.play(world, entity, pos.x(), pos.y(), pos.z(), volume, pitch);
        }

        public abstract void play(Level var1, Player var2, double var3, double var5, double var7, float var9, float var10);

        public void playAt(Level world, Vec3i pos, float volume, float pitch, boolean fade) {
            this.playAt(world, (double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, volume, pitch, fade);
        }

        public void playAt(Level world, Vec3 pos, float volume, float pitch, boolean fade) {
            this.playAt(world, pos.x(), pos.y(), pos.z(), volume, pitch, fade);
        }

        public abstract void playAt(Level var1, double var2, double var4, double var6, float var8, float var9, boolean var10);
    }

    public static class SoundEntryBuilder {

        protected ResourceLocation id;

        protected String subtitle = "unregistered";

        protected SoundSource category = SoundSource.BLOCKS;

        protected List<AllSoundEvents.ConfiguredSoundEvent> wrappedEvents = new ArrayList();

        protected List<ResourceLocation> variants = new ArrayList();

        protected int attenuationDistance;

        public SoundEntryBuilder(ResourceLocation id) {
            this.id = id;
        }

        public AllSoundEvents.SoundEntryBuilder subtitle(String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public AllSoundEvents.SoundEntryBuilder attenuationDistance(int distance) {
            this.attenuationDistance = distance;
            return this;
        }

        public AllSoundEvents.SoundEntryBuilder noSubtitle() {
            this.subtitle = null;
            return this;
        }

        public AllSoundEvents.SoundEntryBuilder category(SoundSource category) {
            this.category = category;
            return this;
        }

        public AllSoundEvents.SoundEntryBuilder addVariant(String name) {
            return this.addVariant(Create.asResource(name));
        }

        public AllSoundEvents.SoundEntryBuilder addVariant(ResourceLocation id) {
            this.variants.add(id);
            return this;
        }

        public AllSoundEvents.SoundEntryBuilder playExisting(Supplier<SoundEvent> event, float volume, float pitch) {
            this.wrappedEvents.add(new AllSoundEvents.ConfiguredSoundEvent(event, volume, pitch));
            return this;
        }

        public AllSoundEvents.SoundEntryBuilder playExisting(SoundEvent event, float volume, float pitch) {
            return this.playExisting(() -> event, volume, pitch);
        }

        public AllSoundEvents.SoundEntryBuilder playExisting(SoundEvent event) {
            return this.playExisting(event, 1.0F, 1.0F);
        }

        public AllSoundEvents.SoundEntryBuilder playExisting(Holder<SoundEvent> event) {
            return this.playExisting(event::get, 1.0F, 1.0F);
        }

        public AllSoundEvents.SoundEntry build() {
            AllSoundEvents.SoundEntry entry = (AllSoundEvents.SoundEntry) (this.wrappedEvents.isEmpty() ? new AllSoundEvents.CustomSoundEntry(this.id, this.variants, this.subtitle, this.category, this.attenuationDistance) : new AllSoundEvents.WrappedSoundEntry(this.id, this.subtitle, this.wrappedEvents, this.category, this.attenuationDistance));
            AllSoundEvents.ALL.put(entry.getId(), entry);
            return entry;
        }
    }

    private static class SoundEntryProvider implements DataProvider {

        private PackOutput output;

        public SoundEntryProvider(DataGenerator generator) {
            this.output = generator.getPackOutput();
        }

        @Override
        public CompletableFuture<?> run(CachedOutput cache) {
            return this.generate(this.output.getOutputFolder(), cache);
        }

        @Override
        public String getName() {
            return "Create's Custom Sounds";
        }

        public CompletableFuture<?> generate(Path path, CachedOutput cache) {
            path = path.resolve("assets/create");
            JsonObject json = new JsonObject();
            AllSoundEvents.ALL.entrySet().stream().sorted(Entry.comparingByKey()).forEach(entry -> ((AllSoundEvents.SoundEntry) entry.getValue()).write(json));
            return DataProvider.saveStable(cache, json, path.resolve("sounds.json"));
        }
    }

    private static class WrappedSoundEntry extends AllSoundEvents.SoundEntry {

        private List<AllSoundEvents.ConfiguredSoundEvent> wrappedEvents;

        private List<AllSoundEvents.WrappedSoundEntry.CompiledSoundEvent> compiledEvents;

        public WrappedSoundEntry(ResourceLocation id, String subtitle, List<AllSoundEvents.ConfiguredSoundEvent> wrappedEvents, SoundSource category, int attenuationDistance) {
            super(id, subtitle, category, attenuationDistance);
            this.wrappedEvents = wrappedEvents;
            this.compiledEvents = new ArrayList();
        }

        @Override
        public void prepare() {
            for (int i = 0; i < this.wrappedEvents.size(); i++) {
                AllSoundEvents.ConfiguredSoundEvent wrapped = (AllSoundEvents.ConfiguredSoundEvent) this.wrappedEvents.get(i);
                ResourceLocation location = this.getIdOf(i);
                RegistryObject<SoundEvent> event = RegistryObject.create(location, ForgeRegistries.SOUND_EVENTS);
                this.compiledEvents.add(new AllSoundEvents.WrappedSoundEntry.CompiledSoundEvent(event, wrapped.volume(), wrapped.pitch()));
            }
        }

        @Override
        public void register(RegisterEvent.RegisterHelper<SoundEvent> helper) {
            for (AllSoundEvents.WrappedSoundEntry.CompiledSoundEvent compiledEvent : this.compiledEvents) {
                ResourceLocation location = compiledEvent.event().getId();
                helper.register(location, SoundEvent.createVariableRangeEvent(location));
            }
        }

        @Override
        public SoundEvent getMainEvent() {
            return ((AllSoundEvents.WrappedSoundEntry.CompiledSoundEvent) this.compiledEvents.get(0)).event().get();
        }

        protected ResourceLocation getIdOf(int i) {
            return new ResourceLocation(this.id.getNamespace(), i == 0 ? this.id.getPath() : this.id.getPath() + "_compounded_" + i);
        }

        @Override
        public void write(JsonObject json) {
            for (int i = 0; i < this.wrappedEvents.size(); i++) {
                AllSoundEvents.ConfiguredSoundEvent event = (AllSoundEvents.ConfiguredSoundEvent) this.wrappedEvents.get(i);
                JsonObject entry = new JsonObject();
                JsonArray list = new JsonArray();
                JsonObject s = new JsonObject();
                s.addProperty("name", ((SoundEvent) event.event().get()).getLocation().toString());
                s.addProperty("type", "event");
                if (this.attenuationDistance != 0) {
                    s.addProperty("attenuation_distance", this.attenuationDistance);
                }
                list.add(s);
                entry.add("sounds", list);
                if (i == 0 && this.hasSubtitle()) {
                    entry.addProperty("subtitle", this.getSubtitleKey());
                }
                json.add(this.getIdOf(i).getPath(), entry);
            }
        }

        @Override
        public void play(Level world, Player entity, double x, double y, double z, float volume, float pitch) {
            for (AllSoundEvents.WrappedSoundEntry.CompiledSoundEvent event : this.compiledEvents) {
                world.playSound(entity, x, y, z, event.event().get(), this.category, event.volume() * volume, event.pitch() * pitch);
            }
        }

        @Override
        public void playAt(Level world, double x, double y, double z, float volume, float pitch, boolean fade) {
            for (AllSoundEvents.WrappedSoundEntry.CompiledSoundEvent event : this.compiledEvents) {
                world.playLocalSound(x, y, z, event.event().get(), this.category, event.volume() * volume, event.pitch() * pitch, fade);
            }
        }

        private static record CompiledSoundEvent(RegistryObject<SoundEvent> event, float volume, float pitch) {
        }
    }
}