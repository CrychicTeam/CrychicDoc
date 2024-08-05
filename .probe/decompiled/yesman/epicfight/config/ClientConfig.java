package yesman.epicfight.config;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraftforge.common.ForgeConfigSpec;
import yesman.epicfight.api.utils.math.Vec2i;

public class ClientConfig {

    public final ForgeConfigSpec.IntValue longPressCountConfig;

    public final ForgeConfigSpec.BooleanValue filterAnimation;

    public final ForgeConfigSpec.DoubleValue aimHelperColor;

    public final ForgeConfigSpec.BooleanValue enableAimHelper;

    public final ForgeConfigSpec.BooleanValue cameraAutoSwitch;

    public final ForgeConfigSpec.BooleanValue autoPreparation;

    public final ForgeConfigSpec.BooleanValue bloodEffects;

    public final ForgeConfigSpec.BooleanValue noMiningInCombat;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> battleAutoSwitchItems;

    public final ForgeConfigSpec.ConfigValue<List<? extends String>> miningAutoSwitchItems;

    public final ForgeConfigSpec.BooleanValue showTargetIndicator;

    public final ForgeConfigSpec.EnumValue<ClientConfig.HealthBarShowOptions> healthBarShowOption;

    public final ForgeConfigSpec.ConfigValue<Integer> staminaBarX;

    public final ForgeConfigSpec.ConfigValue<Integer> staminaBarY;

    public final ForgeConfigSpec.EnumValue<ClientConfig.HorizontalBasis> staminaBarXBase;

    public final ForgeConfigSpec.EnumValue<ClientConfig.VerticalBasis> staminaBarYBase;

    public final ForgeConfigSpec.ConfigValue<Integer> weaponInnateX;

    public final ForgeConfigSpec.ConfigValue<Integer> weaponInnateY;

    public final ForgeConfigSpec.EnumValue<ClientConfig.HorizontalBasis> weaponInnateXBase;

    public final ForgeConfigSpec.EnumValue<ClientConfig.VerticalBasis> weaponInnateYBase;

    public final ForgeConfigSpec.ConfigValue<Integer> passivesX;

    public final ForgeConfigSpec.ConfigValue<Integer> passivesY;

    public final ForgeConfigSpec.EnumValue<ClientConfig.HorizontalBasis> passivesXBase;

    public final ForgeConfigSpec.EnumValue<ClientConfig.VerticalBasis> passivesYBase;

    public final ForgeConfigSpec.EnumValue<ClientConfig.AlignDirection> passivesAlignDirection;

    public final ForgeConfigSpec.ConfigValue<Integer> chargingBarX;

    public final ForgeConfigSpec.ConfigValue<Integer> chargingBarY;

    public final ForgeConfigSpec.EnumValue<ClientConfig.HorizontalBasis> chargingBarXBase;

    public final ForgeConfigSpec.EnumValue<ClientConfig.VerticalBasis> chargingBarYBase;

    private static final BiFunction<Integer, Integer, Integer> ORIGIN = (screenLength, value) -> value;

    private static final BiFunction<Integer, Integer, Integer> SCREEN_EDGE = (screenLength, value) -> screenLength - value;

    private static final BiFunction<Integer, Integer, Integer> CENTER = (screenLength, value) -> screenLength / 2 + value;

    private static final BiFunction<Integer, Integer, Integer> CENTER_SAVE = (screenLength, value) -> value - screenLength / 2;

    private static final ClientConfig.StartCoordGetter START_HORIZONTAL = (x, y, width, height, icons, horBasis, verBasis) -> horBasis == ClientConfig.HorizontalBasis.CENTER ? new Vec2i(x - width * (icons - 1) / 2, y) : new Vec2i(x, y);

    private static final ClientConfig.StartCoordGetter START_VERTICAL = (x, y, width, height, icons, horBasis, verBasis) -> verBasis == ClientConfig.VerticalBasis.CENTER ? new Vec2i(x, y - height * (icons - 1) / 2) : new Vec2i(x, y);

    private static final ClientConfig.NextCoordGetter NEXT_HORIZONTAL = (horBasis, verBasis, oldPos, width, height) -> horBasis != ClientConfig.HorizontalBasis.LEFT && horBasis != ClientConfig.HorizontalBasis.CENTER ? new Vec2i(oldPos.x - width, oldPos.y) : new Vec2i(oldPos.x + width, oldPos.y);

    private static final ClientConfig.NextCoordGetter NEXT_VERTICAL = (horBasis, verBasis, oldPos, width, height) -> verBasis != ClientConfig.VerticalBasis.TOP && verBasis != ClientConfig.VerticalBasis.CENTER ? new Vec2i(oldPos.x, oldPos.y - height) : new Vec2i(oldPos.x, oldPos.y + height);

    public ClientConfig(ForgeConfigSpec.Builder config) {
        this.longPressCountConfig = config.defineInRange("ingame.long_press_count", 2, 1, 10);
        this.healthBarShowOption = config.defineEnum("ingame.health_bar_show_option", ClientConfig.HealthBarShowOptions.HURT);
        this.showTargetIndicator = config.define("ingame.show_target_indicator", (Supplier<Boolean>) (() -> true));
        this.filterAnimation = config.define("ingame.filter_animation", (Supplier<Boolean>) (() -> false));
        this.aimHelperColor = config.defineInRange("ingame.laser_pointer_color", 0.328125, 0.0, 1.0);
        this.enableAimHelper = config.define("ingame.enable_laser_pointer", (Supplier<Boolean>) (() -> true));
        this.cameraAutoSwitch = config.define("ingame.camera_auto_switch", (Supplier<Boolean>) (() -> false));
        this.autoPreparation = config.define("ingame.auto_preparation", (Supplier<Boolean>) (() -> false));
        this.bloodEffects = config.define("ingame.blood_effects", (Supplier<Boolean>) (() -> true));
        this.noMiningInCombat = config.define("ingame.no_mining_in_combat", (Supplier<Boolean>) (() -> true));
        this.battleAutoSwitchItems = config.defineList("ingame.battle_autoswitch_items", Lists.newArrayList(), element -> element instanceof String str ? str.contains(":") : false);
        this.miningAutoSwitchItems = config.defineList("ingame.mining_autoswitch_items", Lists.newArrayList(), element -> element instanceof String str ? str.contains(":") : false);
        this.staminaBarX = config.define("ingame.ui.stamina_bar_x", 120);
        this.staminaBarY = config.define("ingame.ui.stamina_bar_y", 10);
        this.staminaBarXBase = config.defineEnum("ingame.ui.stamina_bar_x_base", ClientConfig.HorizontalBasis.RIGHT);
        this.staminaBarYBase = config.defineEnum("ingame.ui.stamina_bar_y_base", ClientConfig.VerticalBasis.BOTTOM);
        this.weaponInnateX = config.define("ingame.ui.weapon_innate_x", 42);
        this.weaponInnateY = config.define("ingame.ui.weapon_innate_y", 48);
        this.weaponInnateXBase = config.defineEnum("ingame.ui.weapon_innate_x_base", ClientConfig.HorizontalBasis.RIGHT);
        this.weaponInnateYBase = config.defineEnum("ingame.ui.weapon_innate_y_base", ClientConfig.VerticalBasis.BOTTOM);
        this.passivesX = config.define("ingame.ui.passives_x", 70);
        this.passivesY = config.define("ingame.ui.passives_y", 36);
        this.passivesXBase = config.defineEnum("ingame.ui.passives_x_base", ClientConfig.HorizontalBasis.RIGHT);
        this.passivesYBase = config.defineEnum("ingame.ui.passives_y_base", ClientConfig.VerticalBasis.BOTTOM);
        this.passivesAlignDirection = config.defineEnum("ingame.ui.passives_align_direction", ClientConfig.AlignDirection.HORIZONTAL);
        this.chargingBarX = config.define("ingame.ui.charging_bar_x", -119);
        this.chargingBarY = config.define("ingame.ui.charging_bar_y", 60);
        this.chargingBarXBase = config.defineEnum("ingame.ui.charging_bar_x_base", ClientConfig.HorizontalBasis.CENTER);
        this.chargingBarYBase = config.defineEnum("ingame.ui.charging_bar_y_base", ClientConfig.VerticalBasis.CENTER);
    }

    public static enum AlignDirection {

        HORIZONTAL(ClientConfig.START_HORIZONTAL, ClientConfig.NEXT_HORIZONTAL), VERTICAL(ClientConfig.START_VERTICAL, ClientConfig.NEXT_VERTICAL);

        public ClientConfig.StartCoordGetter startCoordGetter;

        public ClientConfig.NextCoordGetter nextPositionGetter;

        private AlignDirection(ClientConfig.StartCoordGetter startCoordGetter, ClientConfig.NextCoordGetter nextPositionGetter) {
            this.startCoordGetter = startCoordGetter;
            this.nextPositionGetter = nextPositionGetter;
        }
    }

    public static enum HealthBarShowOptions {

        NONE, HURT, TARGET;

        public String toString() {
            return this.name().toLowerCase();
        }

        public ClientConfig.HealthBarShowOptions nextOption() {
            return values()[(this.ordinal() + 1) % 3];
        }
    }

    public static enum HorizontalBasis {

        LEFT(ClientConfig.ORIGIN, ClientConfig.ORIGIN), RIGHT(ClientConfig.SCREEN_EDGE, ClientConfig.SCREEN_EDGE), CENTER(ClientConfig.CENTER, ClientConfig.CENTER_SAVE);

        public BiFunction<Integer, Integer, Integer> positionGetter;

        public BiFunction<Integer, Integer, Integer> saveCoordGetter;

        private HorizontalBasis(BiFunction<Integer, Integer, Integer> positionGetter, BiFunction<Integer, Integer, Integer> saveCoordGetter) {
            this.positionGetter = positionGetter;
            this.saveCoordGetter = saveCoordGetter;
        }
    }

    @FunctionalInterface
    public interface NextCoordGetter {

        Vec2i getNext(ClientConfig.HorizontalBasis var1, ClientConfig.VerticalBasis var2, Vec2i var3, int var4, int var5);
    }

    @FunctionalInterface
    public interface StartCoordGetter {

        Vec2i get(int var1, int var2, int var3, int var4, int var5, ClientConfig.HorizontalBasis var6, ClientConfig.VerticalBasis var7);
    }

    public static enum VerticalBasis {

        TOP(ClientConfig.ORIGIN, ClientConfig.ORIGIN), BOTTOM(ClientConfig.SCREEN_EDGE, ClientConfig.SCREEN_EDGE), CENTER(ClientConfig.CENTER, ClientConfig.CENTER_SAVE);

        public BiFunction<Integer, Integer, Integer> positionGetter;

        public BiFunction<Integer, Integer, Integer> saveCoordGetter;

        private VerticalBasis(BiFunction<Integer, Integer, Integer> positionGetter, BiFunction<Integer, Integer, Integer> saveCoordGetter) {
            this.positionGetter = positionGetter;
            this.saveCoordGetter = saveCoordGetter;
        }
    }
}