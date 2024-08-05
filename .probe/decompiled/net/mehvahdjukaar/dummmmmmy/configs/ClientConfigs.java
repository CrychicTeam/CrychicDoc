package net.mehvahdjukaar.dummmmmmy.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.mehvahdjukaar.dummmmmmy.Dummmmmmy;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigBuilder;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigSpec;
import net.mehvahdjukaar.moonlight.api.platform.configs.ConfigType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.moonlight.api.util.math.ColorUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class ClientConfigs {

    public static final ConfigSpec SPEC;

    public static final Supplier<Double> ANIMATION_INTENSITY;

    public static final Supplier<Boolean> SHOW_HEARTHS;

    public static final Supplier<ClientConfigs.SkinType> SKIN;

    public static final Supplier<Boolean> DAMAGE_NUMBERS;

    public static final Supplier<Boolean> LIT_UP_PARTICLES;

    public static final Supplier<CritMode> CRIT_MODE;

    public static final Supplier<Map<ClientConfigs.IdOrTagPredicate, Integer>> DAMAGE_TO_COLORS;

    private static final int COLOR_GENERIC = 16777215;

    private static final int COLOR_CRIT = 16711680;

    private static final int COLOR_DRAGON = 15073535;

    private static final int COLOR_WITHER = 6710886;

    private static final int COLOR_EXPLOSION = 16759593;

    private static final int COLOR_IND_MAGIC = 8670439;

    private static final int COLOR_WATER = 1612003;

    private static final int COLOR_FREEZING = 643839;

    private static final int COLOR_TRIDENT = 65437;

    private static final int COLOR_FIRE = 16742144;

    private static final int COLOR_LIGHTNING = 16773632;

    private static final int COLOR_CACTUS = 1024521;

    private static final int COLOR_TRUE = 9502776;

    private static final int COLOR_WARDEN = 476496;

    private static final int COLOR_BLEED = 8456714;

    public static void init() {
    }

    public static int getDamageColor(ResourceLocation damageTypeId) {
        Map<ClientConfigs.IdOrTagPredicate, Integer> values = (Map<ClientConfigs.IdOrTagPredicate, Integer>) DAMAGE_TO_COLORS.get();
        Optional<Holder.Reference<DamageType>> opt = Utils.hackyGetRegistry(Registries.DAMAGE_TYPE).getHolder(ResourceKey.create(Registries.DAMAGE_TYPE, damageTypeId));
        if (opt.isEmpty()) {
            Dummmmmmy.LOGGER.error("Received invalid damage type: " + damageTypeId);
        } else {
            Holder.Reference<DamageType> holder = (Holder.Reference<DamageType>) opt.get();
            for (Entry<ClientConfigs.IdOrTagPredicate, Integer> e : values.entrySet()) {
                if (((ClientConfigs.IdOrTagPredicate) e.getKey()).test(holder)) {
                    return (Integer) e.getValue();
                }
            }
        }
        return -1;
    }

    static {
        ConfigBuilder builder = ConfigBuilder.create(Dummmmmmy.res("client"), ConfigType.CLIENT);
        builder.comment("lots of cosmetic stuff in here");
        builder.push("visuals").comment("To edit the damage numbers color you'll have to edit the config file manually");
        ANIMATION_INTENSITY = builder.comment("How much the dummy swings in degrees with respect to the damage dealt. default=0.75").define("animation_intensity", 0.75, 0.0, 2.0);
        SHOW_HEARTHS = builder.comment("Show hearths instead of damage dealt? (1 hearth = two damage)").define("show_hearths", false);
        DAMAGE_NUMBERS = builder.comment("Show damage numbers on entity").define("damage_numbers", true);
        LIT_UP_PARTICLES = builder.comment("Display particles fullbright").define("full_bright_damage_numbers", true);
        CRIT_MODE = PlatHelper.getPlatform().isForge() ? builder.comment("How crits should be shown").define("crit_mode", CritMode.COLOR_AND_MULTIPLIER) : () -> CritMode.OFF;
        SKIN = builder.comment("Skin used by the dummy").define("texture", ClientConfigs.SkinType.DEFAULT);
        Map<ClientConfigs.IdOrTagPredicate, Integer> map = new HashMap();
        map.put(new ClientConfigs.IdPredicate(Dummmmmmy.TRUE_DAMAGE), 9502776);
        map.put(new ClientConfigs.IdPredicate(Dummmmmmy.CRITICAL_DAMAGE), 16711680);
        map.put(new ClientConfigs.IdPredicate("generic"), 16777215);
        map.put(new ClientConfigs.IdPredicate("trident"), 65437);
        map.put(new ClientConfigs.IdPredicate("dragon_breath"), 15073535);
        map.put(new ClientConfigs.IdPredicate("sonic_boom"), 476496);
        map.put(new ClientConfigs.IdPredicate("attributeslib:bleeding"), 8456714);
        map.put(new ClientConfigs.TagPredicate(Dummmmmmy.IS_EXPLOSION), 16759593);
        map.put(new ClientConfigs.TagPredicate(Dummmmmmy.IS_COLD), 643839);
        map.put(new ClientConfigs.TagPredicate(Dummmmmmy.IS_THORN), 1024521);
        map.put(new ClientConfigs.TagPredicate(Dummmmmmy.IS_FIRE), 16742144);
        map.put(new ClientConfigs.TagPredicate(Dummmmmmy.IS_WITHER), 6710886);
        map.put(new ClientConfigs.TagPredicate(DamageTypeTags.IS_LIGHTNING), 16773632);
        map.put(new ClientConfigs.TagPredicate(DamageTypeTags.IS_DROWNING), 1612003);
        map.put(new ClientConfigs.TagPredicate(DamageTypeTags.WITCH_RESISTANT_TO), 8670439);
        DAMAGE_TO_COLORS = builder.comment("Add here custom colors (in hex format) to associate with your damage types. This is a map from damage source ID to a color where you can add new entries for each").defineObject("damage_type_colors", () -> map, Codec.unboundedMap(ClientConfigs.IdOrTagPredicate.CODEC, ColorUtils.CODEC));
        builder.pop();
        SPEC = builder.buildAndRegister();
    }

    public interface IdOrTagPredicate extends Predicate<Holder<DamageType>> {

        Codec<ClientConfigs.IdOrTagPredicate> CODEC = Codec.STRING.comapFlatMap(ClientConfigs.IdOrTagPredicate::read, ClientConfigs.IdOrTagPredicate::toString).stable();

        String toString();

        static DataResult<ClientConfigs.IdOrTagPredicate> read(String location) {
            return location.startsWith("#") ? ResourceLocation.read(location.substring(1)).map(ClientConfigs.TagPredicate::new) : ResourceLocation.read(location).map(ClientConfigs.IdPredicate::new);
        }
    }

    static record IdPredicate(ResourceLocation resourceLocation) implements ClientConfigs.IdOrTagPredicate {

        public IdPredicate(String name) {
            this(new ResourceLocation(name));
        }

        @Override
        public String toString() {
            return this.resourceLocation.toString();
        }

        public boolean test(Holder<DamageType> id) {
            return ((ResourceKey) id.unwrapKey().get()).location().equals(this.resourceLocation);
        }
    }

    public static enum SkinType {

        DEFAULT("dummy", "dummy_h"), ORIGINAL("dummy_1", "dummy_1"), DUNGEONS("dummy_3", "dummy_3_h"), ALTERNATIVE("dummy_2", "dummy_2_h");

        private final ResourceLocation texture;

        private final ResourceLocation shearedTexture;

        private SkinType(String name, String shearedName) {
            this.texture = new ResourceLocation("dummmmmmy:textures/entity/" + name + ".png");
            this.shearedTexture = new ResourceLocation("dummmmmmy:textures/entity/" + shearedName + ".png");
        }

        public ResourceLocation getSkin(Boolean sheared) {
            return sheared ? this.shearedTexture : this.texture;
        }
    }

    static record TagPredicate(TagKey<DamageType> tag) implements ClientConfigs.IdOrTagPredicate {

        public TagPredicate(ResourceLocation resourceLocation) {
            this(new TagKey<>(Registries.DAMAGE_TYPE, resourceLocation));
        }

        @Override
        public String toString() {
            return "#" + this.tag.location();
        }

        public boolean test(Holder<DamageType> holder) {
            return holder.is(this.tag);
        }
    }
}