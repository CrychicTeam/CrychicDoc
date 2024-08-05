package net.blay09.mods.defaultoptions.difficulty;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.event.client.screen.ScreenInitEvent;
import net.blay09.mods.defaultoptions.config.DefaultOptionsConfig;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;

public class DefaultDifficultyHandler {

    public static void initialize() {
        Balm.getEvents().onEvent(ScreenInitEvent.Post.class, DefaultDifficultyHandler::onInitGui);
    }

    public static void onInitGui(ScreenInitEvent.Post event) {
        if (event.getScreen() instanceof CreateWorldScreen screen) {
            WorldCreationUiState uiState = screen.getUiState();
            Difficulty difficulty = DefaultOptionsConfig.getActive().defaultDifficulty.toDifficulty();
            uiState.setDifficulty(difficulty);
            if (DefaultOptionsConfig.getActive().lockDifficulty) {
                lockDifficultyButton(screen);
            }
            uiState.addListener(state -> {
                if (DefaultOptionsConfig.getActive().lockDifficulty) {
                    lockDifficultyButton(screen);
                }
            });
        }
    }

    private static void lockDifficultyButton(CreateWorldScreen screen) {
        AbstractWidget difficultyButton = findDifficultyButton(screen);
        if (difficultyButton != null) {
            difficultyButton.active = false;
        }
    }

    private static AbstractWidget findDifficultyButton(CreateWorldScreen screen) {
        return (AbstractWidget) screen.m_6702_().stream().filter(it -> {
            if (it instanceof CycleButton<?> button && button.getValue() instanceof Difficulty) {
                return true;
            }
            return false;
        }).findAny().orElse(null);
    }
}