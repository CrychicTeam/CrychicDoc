package net.minecraft.gametest.framework;

import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;

public class MultipleTestTracker {

    private static final char NOT_STARTED_TEST_CHAR = ' ';

    private static final char ONGOING_TEST_CHAR = '_';

    private static final char SUCCESSFUL_TEST_CHAR = '+';

    private static final char FAILED_OPTIONAL_TEST_CHAR = 'x';

    private static final char FAILED_REQUIRED_TEST_CHAR = 'X';

    private final Collection<GameTestInfo> tests = Lists.newArrayList();

    @Nullable
    private final Collection<GameTestListener> listeners = Lists.newArrayList();

    public MultipleTestTracker() {
    }

    public MultipleTestTracker(Collection<GameTestInfo> collectionGameTestInfo0) {
        this.tests.addAll(collectionGameTestInfo0);
    }

    public void addTestToTrack(GameTestInfo gameTestInfo0) {
        this.tests.add(gameTestInfo0);
        this.listeners.forEach(gameTestInfo0::m_127624_);
    }

    public void addListener(GameTestListener gameTestListener0) {
        this.listeners.add(gameTestListener0);
        this.tests.forEach(p_127815_ -> p_127815_.addListener(gameTestListener0));
    }

    public void addFailureListener(final Consumer<GameTestInfo> consumerGameTestInfo0) {
        this.addListener(new GameTestListener() {

            @Override
            public void testStructureLoaded(GameTestInfo p_127830_) {
            }

            @Override
            public void testPassed(GameTestInfo p_177685_) {
            }

            @Override
            public void testFailed(GameTestInfo p_127832_) {
                consumerGameTestInfo0.accept(p_127832_);
            }
        });
    }

    public int getFailedRequiredCount() {
        return (int) this.tests.stream().filter(GameTestInfo::m_127639_).filter(GameTestInfo::m_127643_).count();
    }

    public int getFailedOptionalCount() {
        return (int) this.tests.stream().filter(GameTestInfo::m_127639_).filter(GameTestInfo::m_127644_).count();
    }

    public int getDoneCount() {
        return (int) this.tests.stream().filter(GameTestInfo::m_127641_).count();
    }

    public boolean hasFailedRequired() {
        return this.getFailedRequiredCount() > 0;
    }

    public boolean hasFailedOptional() {
        return this.getFailedOptionalCount() > 0;
    }

    public Collection<GameTestInfo> getFailedRequired() {
        return (Collection<GameTestInfo>) this.tests.stream().filter(GameTestInfo::m_127639_).filter(GameTestInfo::m_127643_).collect(Collectors.toList());
    }

    public Collection<GameTestInfo> getFailedOptional() {
        return (Collection<GameTestInfo>) this.tests.stream().filter(GameTestInfo::m_127639_).filter(GameTestInfo::m_127644_).collect(Collectors.toList());
    }

    public int getTotalCount() {
        return this.tests.size();
    }

    public boolean isDone() {
        return this.getDoneCount() == this.getTotalCount();
    }

    public String getProgressBar() {
        StringBuffer $$0 = new StringBuffer();
        $$0.append('[');
        this.tests.forEach(p_127806_ -> {
            if (!p_127806_.hasStarted()) {
                $$0.append(' ');
            } else if (p_127806_.hasSucceeded()) {
                $$0.append('+');
            } else if (p_127806_.hasFailed()) {
                $$0.append((char) (p_127806_.isRequired() ? 'X' : 'x'));
            } else {
                $$0.append('_');
            }
        });
        $$0.append(']');
        return $$0.toString();
    }

    public String toString() {
        return this.getProgressBar();
    }
}