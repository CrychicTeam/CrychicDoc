package net.blay09.mods.craftingtweaks.registry;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;
import java.util.function.Predicate;
import net.blay09.mods.craftingtweaks.api.ButtonAlignment;
import net.blay09.mods.craftingtweaks.api.ButtonStyle;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.CraftingGridBuilder;
import net.blay09.mods.craftingtweaks.api.CraftingGridDecorator;
import net.blay09.mods.craftingtweaks.api.CraftingGridProvider;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksButtonStyles;
import net.blay09.mods.craftingtweaks.api.TweakType;
import net.blay09.mods.craftingtweaks.api.impl.DefaultCraftingGrid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDrivenGridFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataDrivenGridFactory.class);

    public static CraftingGridProvider createGridProvider(final CraftingTweaksRegistrationData data) {
        final String senderModId = data.getModId();
        String containerClassName = data.getContainerClass();
        Predicate<AbstractContainerMenu> matchesContainerClass = it -> it.getClass().getName().equals(containerClassName);
        Predicate<AbstractContainerMenu> containerPredicate = matchesContainerClass;
        String validContainerPredicateLegacy = data.getContainerCallbackClass();
        if (!validContainerPredicateLegacy.isEmpty()) {
            try {
                Class<?> functionClass = Class.forName(validContainerPredicateLegacy);
                if (!Function.class.isAssignableFrom(functionClass)) {
                    logger.error("{} sent a container callback that's not even a function", senderModId);
                    return null;
                }
                Function<AbstractContainerMenu, Boolean> function = (Function<AbstractContainerMenu, Boolean>) functionClass.getDeclaredConstructor().newInstance();
                containerPredicate = t -> matchesContainerClass.test(t) && (Boolean) function.apply(t);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException var13) {
                logger.error("{} sent an invalid container callback.", senderModId);
            }
        }
        String validContainerPredicate = data.getValidContainerPredicateClass();
        if (!validContainerPredicate.isEmpty()) {
            try {
                Class<?> predicateClass = Class.forName(validContainerPredicate);
                if (!Predicate.class.isAssignableFrom(predicateClass)) {
                    logger.error("{} sent an invalid ValidContainerPredicate - it must implement Predicate<Container>", senderModId);
                    return null;
                }
                Predicate<AbstractContainerMenu> providedPredicate = (Predicate<AbstractContainerMenu>) predicateClass.getDeclaredConstructor().newInstance();
                containerPredicate = it -> matchesContainerClass.test(it) && providedPredicate.test(it);
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException var12) {
                logger.error("{} sent an invalid ValidContainerPredicate: {}", senderModId, var12.getMessage());
            }
        }
        String getGridStartFunction = data.getGetGridStartFunctionClass();
        Function<AbstractContainerMenu, Integer> gridStartFunction = null;
        if (!getGridStartFunction.isEmpty()) {
            try {
                Class<?> functionClass = Class.forName(getGridStartFunction);
                if (!Function.class.isAssignableFrom(functionClass)) {
                    logger.error("{} sent an invalid GetGridStartFunction - it must implement Function<Container, Integer>", senderModId);
                    return null;
                }
                gridStartFunction = (Function<AbstractContainerMenu, Integer>) functionClass.getDeclaredConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException var11) {
                logger.error("{} sent an invalid GetGridStartFunction: {}", senderModId, var11.getMessage());
            }
        }
        final Predicate<AbstractContainerMenu> effectiveContainerPredicate = containerPredicate;
        final Function<AbstractContainerMenu, Integer> effectiveGridStartFunction = gridStartFunction;
        return new CraftingGridProvider() {

            @Override
            public String getModId() {
                return senderModId;
            }

            @Override
            public boolean handles(AbstractContainerMenu menu) {
                return effectiveContainerPredicate.test(menu);
            }

            @Override
            public void buildCraftingGrids(CraftingGridBuilder builder, AbstractContainerMenu menu) {
                int gridSlotNumber = data.getGridSlotNumber();
                int gridSize = data.getGridSize();
                CraftingGridDecorator grid;
                if (effectiveGridStartFunction != null) {
                    grid = new DefaultCraftingGrid(new ResourceLocation(senderModId, "default"), gridSlotNumber, gridSize) {

                        @Override
                        public int getGridStartSlot(Player player, AbstractContainerMenu menu) {
                            return (Integer) effectiveGridStartFunction.apply(menu);
                        }
                    };
                    builder.addCustomGrid((CraftingGrid) grid);
                } else {
                    grid = builder.addGrid(gridSlotNumber, gridSize);
                }
                int buttonOffsetX = DataDrivenGridFactory.unwrapOr(data.getButtonOffsetX(), 0);
                int buttonOffsetY = DataDrivenGridFactory.unwrapOr(data.getButtonOffsetY(), 0);
                grid.setButtonAlignmentOffset(buttonOffsetX, buttonOffsetY);
                ButtonAlignment alignToGrid = ButtonAlignment.LEFT;
                String alignToGridName = data.getAlignToGrid();
                String buttonStyle = alignToGridName.toLowerCase();
                switch(buttonStyle) {
                    case "north":
                    case "up":
                    case "top":
                        alignToGrid = ButtonAlignment.TOP;
                        break;
                    case "south":
                    case "down":
                    case "bottom":
                        alignToGrid = ButtonAlignment.BOTTOM;
                        break;
                    case "east":
                    case "right":
                        alignToGrid = ButtonAlignment.RIGHT;
                        break;
                    case "west":
                    case "left":
                        alignToGrid = ButtonAlignment.LEFT;
                }
                grid.setButtonAlignment(alignToGrid);
                ButtonStyle buttonStyle = CraftingTweaksButtonStyles.DEFAULT;
                String buttonStyleName = data.getButtonStyle();
                String rotateTweak = buttonStyleName.toLowerCase();
                switch(rotateTweak) {
                    case "default":
                        buttonStyle = CraftingTweaksButtonStyles.DEFAULT;
                        break;
                    case "small_width":
                        buttonStyle = CraftingTweaksButtonStyles.SMALL_WIDTH;
                        break;
                    case "small_height":
                        buttonStyle = CraftingTweaksButtonStyles.SMALL_HEIGHT;
                        break;
                    case "small":
                        buttonStyle = CraftingTweaksButtonStyles.SMALL;
                }
                grid.setButtonStyle(buttonStyle);
                if (data.isHideButtons()) {
                    grid.hideAllTweakButtons();
                }
                if (data.isPhantomItems()) {
                    grid.usePhantomItems();
                }
                CraftingTweaksRegistrationData.TweakData rotateTweak = data.getTweakRotate();
                if (!rotateTweak.isEnabled()) {
                    grid.disableTweak(TweakType.Rotate);
                }
                if (!rotateTweak.isShowButton()) {
                    grid.hideTweakButton(TweakType.Rotate);
                }
                if (rotateTweak.getButtonX() != null || rotateTweak.getButtonY() != null) {
                    grid.setButtonPosition(TweakType.Rotate, DataDrivenGridFactory.unwrapOr(data.getButtonOffsetX(), -16) + DataDrivenGridFactory.unwrapOr(rotateTweak.getButtonX(), 0), DataDrivenGridFactory.unwrapOr(data.getButtonOffsetY(), 16) + DataDrivenGridFactory.unwrapOr(rotateTweak.getButtonY(), 0));
                }
                CraftingTweaksRegistrationData.TweakData balanceTweak = data.getTweakBalance();
                if (!balanceTweak.isEnabled()) {
                    grid.disableTweak(TweakType.Balance);
                }
                if (!balanceTweak.isShowButton()) {
                    grid.hideTweakButton(TweakType.Balance);
                }
                if (balanceTweak.getButtonX() != null || balanceTweak.getButtonY() != null) {
                    grid.setButtonPosition(TweakType.Balance, DataDrivenGridFactory.unwrapOr(data.getButtonOffsetX(), -16) + DataDrivenGridFactory.unwrapOr(balanceTweak.getButtonX(), 0), DataDrivenGridFactory.unwrapOr(data.getButtonOffsetY(), 16) + DataDrivenGridFactory.unwrapOr(balanceTweak.getButtonY(), 0));
                }
                CraftingTweaksRegistrationData.TweakData clearTweak = data.getTweakClear();
                if (!clearTweak.isEnabled()) {
                    grid.disableTweak(TweakType.Clear);
                }
                if (!clearTweak.isShowButton()) {
                    grid.hideTweakButton(TweakType.Clear);
                }
                if (clearTweak.getButtonX() != null || clearTweak.getButtonY() != null) {
                    grid.setButtonPosition(TweakType.Clear, DataDrivenGridFactory.unwrapOr(data.getButtonOffsetX(), -16) + DataDrivenGridFactory.unwrapOr(clearTweak.getButtonX(), 0), DataDrivenGridFactory.unwrapOr(data.getButtonOffsetY(), 16) + DataDrivenGridFactory.unwrapOr(clearTweak.getButtonY(), 0));
                }
            }
        };
    }

    private static int unwrapOr(Integer integer, int defaultValue) {
        return integer != null ? integer : defaultValue;
    }
}