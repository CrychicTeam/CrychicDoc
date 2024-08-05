package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.IceAndFire;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.entity.Mob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AiDebug {

    private static final List<Mob> entities = new ArrayList();

    private static final Logger LOGGER = LogManager.getLogger();

    private AiDebug() {
    }

    public static boolean isEnabled() {
        return IceAndFire.VERSION.equals("0.0NONE");
    }

    public static void logData() {
        for (Mob entity : new ArrayList(entities)) {
            if (!entity.m_6084_()) {
                entities.remove(entity);
            } else {
                if (entity.goalSelector != null) {
                    List<String> goals = (List<String>) entity.goalSelector.getRunningGoals().map(goal -> goal.getGoal().toString()).collect(Collectors.toList());
                    if (!goals.isEmpty()) {
                        LOGGER.debug("{} - GOALS: {}", entity, goals);
                    }
                }
                if (entity.targetSelector != null) {
                    List<String> targets = (List<String>) entity.targetSelector.getRunningGoals().map(goal -> goal.getGoal().toString()).collect(Collectors.toList());
                    if (!targets.isEmpty()) {
                        LOGGER.debug("{} - TARGET: {}", entity, targets);
                    }
                }
            }
        }
    }

    public static boolean contains(Mob entity) {
        return entities.contains(entity);
    }

    public static void addEntity(Mob entity) {
        if (entities.contains(entity)) {
            entities.remove(entity);
        } else {
            entities.add(entity);
        }
    }
}