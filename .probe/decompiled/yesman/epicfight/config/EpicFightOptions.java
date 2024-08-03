package yesman.epicfight.config;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import yesman.epicfight.api.utils.math.Vec2i;
import yesman.epicfight.client.gui.component.ColorWidget;

public class EpicFightOptions {

    public static final float A_TICK = 0.05F;

    public static final float GENERAL_ANIMATION_CONVERT_TIME = 0.15F;

    public final OptionHandler.IntegerOptionHandler longPressCount;

    public final OptionHandler<Boolean> filterAnimation;

    public final OptionHandler<ClientConfig.HealthBarShowOptions> healthBarShowOption;

    public final OptionHandler<Boolean> showTargetIndicator;

    public final OptionHandler.DoubleOptionHandler aimHelperColor;

    public final OptionHandler<Boolean> enableAimHelperPointer;

    public final OptionHandler<Boolean> cameraAutoSwitch;

    public final OptionHandler<Boolean> autoPreparation;

    public final OptionHandler<Boolean> bloodEffects;

    public final OptionHandler<Boolean> noMiningInCombat;

    public final List<Item> battleAutoSwitchItems;

    public final List<Item> miningAutoSwitchItems;

    public int aimHelperRealColor;

    public final OptionHandler<Integer> staminaBarX;

    public final OptionHandler<Integer> staminaBarY;

    public final OptionHandler<ClientConfig.HorizontalBasis> staminaBarXBase;

    public final OptionHandler<ClientConfig.VerticalBasis> staminaBarYBase;

    public final OptionHandler<Integer> weaponInnateX;

    public final OptionHandler<Integer> weaponInnateY;

    public final OptionHandler<ClientConfig.HorizontalBasis> weaponInnateXBase;

    public final OptionHandler<ClientConfig.VerticalBasis> weaponInnateYBase;

    public final OptionHandler<Integer> passivesX;

    public final OptionHandler<Integer> passivesY;

    public final OptionHandler<ClientConfig.HorizontalBasis> passivesXBase;

    public final OptionHandler<ClientConfig.VerticalBasis> passivesYBase;

    public final OptionHandler<ClientConfig.AlignDirection> passivesAlignDirection;

    public final OptionHandler<Integer> chargingBarX;

    public final OptionHandler<Integer> chargingBarY;

    public final OptionHandler<ClientConfig.HorizontalBasis> chargingBarXBase;

    public final OptionHandler<ClientConfig.VerticalBasis> chargingBarYBase;

    public EpicFightOptions() {
        ClientConfig config = ConfigManager.INGAME_CONFIG;
        this.longPressCount = new OptionHandler.IntegerOptionHandler(config.longPressCountConfig.get(), 1, 10);
        this.filterAnimation = new OptionHandler<>(config.filterAnimation.get());
        this.healthBarShowOption = new OptionHandler<>((ClientConfig.HealthBarShowOptions) config.healthBarShowOption.get());
        this.showTargetIndicator = new OptionHandler<>(config.showTargetIndicator.get());
        this.aimHelperColor = new OptionHandler.DoubleOptionHandler(config.aimHelperColor.get(), 0.0, 1.0);
        this.enableAimHelperPointer = new OptionHandler<>(config.enableAimHelper.get());
        this.aimHelperRealColor = ColorWidget.toColorInteger(config.aimHelperColor.get());
        this.cameraAutoSwitch = new OptionHandler<>(config.cameraAutoSwitch.get());
        this.autoPreparation = new OptionHandler<>(config.autoPreparation.get());
        this.bloodEffects = new OptionHandler<>(config.bloodEffects.get());
        this.noMiningInCombat = new OptionHandler<>(config.noMiningInCombat.get());
        this.battleAutoSwitchItems = Lists.newArrayList(config.battleAutoSwitchItems.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).iterator());
        this.miningAutoSwitchItems = Lists.newArrayList(config.miningAutoSwitchItems.get().stream().map(itemName -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName))).iterator());
        this.staminaBarX = new OptionHandler<>(config.staminaBarX.get());
        this.staminaBarY = new OptionHandler<>(config.staminaBarY.get());
        this.staminaBarXBase = new OptionHandler<>((ClientConfig.HorizontalBasis) config.staminaBarXBase.get());
        this.staminaBarYBase = new OptionHandler<>((ClientConfig.VerticalBasis) config.staminaBarYBase.get());
        this.weaponInnateX = new OptionHandler<>(config.weaponInnateX.get());
        this.weaponInnateY = new OptionHandler<>(config.weaponInnateY.get());
        this.weaponInnateXBase = new OptionHandler<>((ClientConfig.HorizontalBasis) config.weaponInnateXBase.get());
        this.weaponInnateYBase = new OptionHandler<>((ClientConfig.VerticalBasis) config.weaponInnateYBase.get());
        this.passivesX = new OptionHandler<>(config.passivesX.get());
        this.passivesY = new OptionHandler<>(config.passivesY.get());
        this.passivesXBase = new OptionHandler<>((ClientConfig.HorizontalBasis) config.passivesXBase.get());
        this.passivesYBase = new OptionHandler<>((ClientConfig.VerticalBasis) config.passivesYBase.get());
        this.passivesAlignDirection = new OptionHandler<>((ClientConfig.AlignDirection) config.passivesAlignDirection.get());
        this.chargingBarX = new OptionHandler<>(config.chargingBarX.get());
        this.chargingBarY = new OptionHandler<>(config.chargingBarY.get());
        this.chargingBarXBase = new OptionHandler<>((ClientConfig.HorizontalBasis) config.chargingBarXBase.get());
        this.chargingBarYBase = new OptionHandler<>((ClientConfig.VerticalBasis) config.chargingBarYBase.get());
    }

    public void resetSettings() {
        this.longPressCount.setDefaultValue();
        this.filterAnimation.setDefaultValue();
        this.healthBarShowOption.setDefaultValue();
        this.showTargetIndicator.setDefaultValue();
        this.aimHelperColor.setDefaultValue();
        this.enableAimHelperPointer.setDefaultValue();
        this.cameraAutoSwitch.setDefaultValue();
        this.autoPreparation.setDefaultValue();
        this.bloodEffects.setDefaultValue();
        this.noMiningInCombat.setDefaultValue();
        this.aimHelperRealColor = ColorWidget.toColorInteger((Double) this.aimHelperColor.getValue());
        this.staminaBarX.setDefaultValue();
        this.staminaBarY.setDefaultValue();
        this.staminaBarXBase.setDefaultValue();
        this.staminaBarYBase.setDefaultValue();
        this.weaponInnateX.setDefaultValue();
        this.weaponInnateY.setDefaultValue();
        this.weaponInnateXBase.setDefaultValue();
        this.weaponInnateYBase.setDefaultValue();
        this.passivesX.setDefaultValue();
        this.passivesY.setDefaultValue();
        this.passivesXBase.setDefaultValue();
        this.passivesYBase.setDefaultValue();
        this.passivesAlignDirection.setDefaultValue();
        this.chargingBarX.setDefaultValue();
        this.chargingBarY.setDefaultValue();
        this.chargingBarXBase.setDefaultValue();
        this.chargingBarYBase.setDefaultValue();
    }

    public void save() {
        ClientConfig config = ConfigManager.INGAME_CONFIG;
        config.longPressCountConfig.set((Integer) this.longPressCount.getValue());
        config.filterAnimation.set(this.filterAnimation.getValue());
        config.healthBarShowOption.set(this.healthBarShowOption.getValue());
        config.showTargetIndicator.set(this.showTargetIndicator.getValue());
        config.aimHelperColor.set((Double) this.aimHelperColor.getValue());
        config.enableAimHelper.set(this.enableAimHelperPointer.getValue());
        config.cameraAutoSwitch.set(this.cameraAutoSwitch.getValue());
        config.autoPreparation.set(this.autoPreparation.getValue());
        config.bloodEffects.set(this.bloodEffects.getValue());
        config.noMiningInCombat.set(this.noMiningInCombat.getValue());
        this.aimHelperRealColor = ColorWidget.toColorInteger((Double) this.aimHelperColor.getValue());
        config.battleAutoSwitchItems.set(Lists.newArrayList(this.battleAutoSwitchItems.stream().map(item -> ForgeRegistries.ITEMS.getKey(item).toString()).iterator()));
        config.miningAutoSwitchItems.set(Lists.newArrayList(this.miningAutoSwitchItems.stream().map(item -> ForgeRegistries.ITEMS.getKey(item).toString()).iterator()));
        config.staminaBarX.set(this.staminaBarX.getValue());
        config.staminaBarY.set(this.staminaBarY.getValue());
        config.staminaBarXBase.set(this.staminaBarXBase.getValue());
        config.staminaBarYBase.set(this.staminaBarYBase.getValue());
        config.weaponInnateX.set(this.weaponInnateX.getValue());
        config.weaponInnateY.set(this.weaponInnateY.getValue());
        config.weaponInnateXBase.set(this.weaponInnateXBase.getValue());
        config.weaponInnateYBase.set(this.weaponInnateYBase.getValue());
        config.passivesX.set(this.passivesX.getValue());
        config.passivesY.set(this.passivesY.getValue());
        config.passivesXBase.set(this.passivesXBase.getValue());
        config.passivesYBase.set(this.passivesYBase.getValue());
        config.passivesAlignDirection.set(this.passivesAlignDirection.getValue());
    }

    public Vec2i getStaminaPosition(int width, int height) {
        int posX = (Integer) this.staminaBarXBase.getValue().positionGetter.apply(width, this.staminaBarX.getValue());
        int posY = (Integer) this.staminaBarYBase.getValue().positionGetter.apply(height, this.staminaBarY.getValue());
        return new Vec2i(posX, posY);
    }

    public Vec2i getWeaponInnatePosition(int width, int height) {
        int posX = (Integer) this.weaponInnateXBase.getValue().positionGetter.apply(width, this.weaponInnateX.getValue());
        int posY = (Integer) this.weaponInnateYBase.getValue().positionGetter.apply(height, this.weaponInnateY.getValue());
        return new Vec2i(posX, posY);
    }

    public Vec2i getChargingBarPosition(int width, int height) {
        int posX = (Integer) this.chargingBarXBase.getValue().positionGetter.apply(width, this.chargingBarX.getValue());
        int posY = (Integer) this.chargingBarYBase.getValue().positionGetter.apply(height, this.chargingBarY.getValue());
        return new Vec2i(posX, posY);
    }
}