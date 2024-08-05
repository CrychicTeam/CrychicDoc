package dev.xkmc.modulargolems.content.entity.mode;

import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;

public class GolemModes {

    public static final List<GolemMode> LIST = new ArrayList();

    public static final GolemMode FOLLOW = new GolemMode(false, true, false, MGLangData.MODE_FOLLOWING, MGLangData.MODE_FOLLOW);

    public static final GolemMode GUARD = new GolemMode(true, true, true, MGLangData.MODE_GUARDING, MGLangData.MODE_GUARD);

    public static final GolemMode STAND = new GolemMode(true, false, false, MGLangData.MODE_STANDING, MGLangData.MODE_STAND);

    public static final GolemMode FREE_WANDER = new GolemMode(false, true, true, MGLangData.MODE_WANDERING, MGLangData.MODE_WANDER);

    public static final GolemMode SQUAD = new GolemMode(false, true, false, MGLangData.MODE_TEAMING, MGLangData.MODE_SQUAD);

    public static final GolemMode ROUTE = new PatrolMode(false, true, false, MGLangData.MODE_PATROLLING, MGLangData.MODE_PATROL);

    public static GolemMode get(int i) {
        return (GolemMode) LIST.get(i);
    }

    public static GolemMode nextMode(GolemMode mode) {
        return get((mode.getID() + 1) % LIST.size());
    }

    public static void register() {
    }
}