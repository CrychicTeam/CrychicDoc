package net.minecraft.gametest.framework;

import com.google.common.collect.Lists;
import java.util.Collection;

public class GameTestTicker {

    public static final GameTestTicker SINGLETON = new GameTestTicker();

    private final Collection<GameTestInfo> testInfos = Lists.newCopyOnWriteArrayList();

    public void add(GameTestInfo gameTestInfo0) {
        this.testInfos.add(gameTestInfo0);
    }

    public void clear() {
        this.testInfos.clear();
    }

    public void tick() {
        this.testInfos.forEach(GameTestInfo::m_127628_);
        this.testInfos.removeIf(GameTestInfo::m_127641_);
    }
}