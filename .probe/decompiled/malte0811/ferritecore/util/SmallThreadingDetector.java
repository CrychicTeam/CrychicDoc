package malte0811.ferritecore.util;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import malte0811.ferritecore.ducks.SmallThreadDetectable;
import net.minecraft.util.ThreadingDetector;

public class SmallThreadingDetector {

    public static void acquire(SmallThreadDetectable obj, String name) {
        byte oldState;
        synchronized (obj) {
            oldState = obj.ferritecore$getState();
            if (oldState == 0) {
                obj.ferritecore$setState((byte) 1);
                return;
            }
            if (oldState == 1) {
                SmallThreadingDetector.GlobalCrashHandler.startCrash(obj, name);
                obj.ferritecore$setState((byte) 2);
            }
        }
        if (oldState == 1) {
            SmallThreadingDetector.GlobalCrashHandler.crashAcquire(obj);
        } else {
            SmallThreadingDetector.GlobalCrashHandler.crashBystander(obj);
        }
    }

    public static void release(SmallThreadDetectable obj) {
        byte oldState;
        synchronized (obj) {
            oldState = obj.ferritecore$getState();
            if (oldState == 1) {
                obj.ferritecore$setState((byte) 0);
                return;
            }
        }
        if (oldState == 2) {
            SmallThreadingDetector.GlobalCrashHandler.crashRelease(obj);
        }
    }

    private static class CrashingState {

        final String name;

        final SmallThreadDetectable owner;

        Thread acquireThread;

        Thread releaseThread;

        RuntimeException mainException;

        private CrashingState(String name, SmallThreadDetectable owner) {
            this.name = name;
            this.owner = owner;
        }

        public synchronized void waitUntilReady(SmallThreadingDetector.ThreadRole role) {
            if (role == SmallThreadingDetector.ThreadRole.ACQUIRE) {
                this.acquireThread = Thread.currentThread();
            } else if (role == SmallThreadingDetector.ThreadRole.RELEASE) {
                this.releaseThread = Thread.currentThread();
            }
            this.notifyAll();
            try {
                this.waitUntilOrCrash(() -> this.acquireThread != null && this.releaseThread != null);
                if (role == SmallThreadingDetector.ThreadRole.ACQUIRE) {
                    this.mainException = ThreadingDetector.makeThreadingException(this.name, this.releaseThread);
                    this.notifyAll();
                } else {
                    this.waitUntilOrCrash(() -> this.mainException != null);
                }
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }
        }

        private synchronized void waitUntilOrCrash(BooleanSupplier isReady) throws InterruptedException {
            long maxTotalTime = 10000L;
            long start = System.currentTimeMillis();
            while (!isReady.getAsBoolean()) {
                if (System.currentTimeMillis() - start > 60000L) {
                    throw new RuntimeException("Threading detector crash did not find other thread, missing release call? Owner: " + this.owner + " (ID hash: " + System.identityHashCode(this.owner) + "), time: " + System.currentTimeMillis());
                }
                this.wait(10000L);
            }
        }
    }

    private static class GlobalCrashHandler {

        private static final Object MONITOR = new Object();

        private static final Map<SmallThreadDetectable, SmallThreadingDetector.CrashingState> ACTIVE_CRASHES = new IdentityHashMap();

        private static void startCrash(SmallThreadDetectable owner, String name) {
            synchronized (MONITOR) {
                ACTIVE_CRASHES.put(owner, new SmallThreadingDetector.CrashingState(name, owner));
            }
        }

        private static void crashAcquire(SmallThreadDetectable owner) {
            SmallThreadingDetector.CrashingState state = getAndWait(owner, SmallThreadingDetector.ThreadRole.ACQUIRE);
            throw state.mainException;
        }

        private static void crashRelease(SmallThreadDetectable owner) {
            SmallThreadingDetector.CrashingState state = getAndWait(owner, SmallThreadingDetector.ThreadRole.RELEASE);
            throw state.mainException;
        }

        private static void crashBystander(SmallThreadDetectable owner) {
            SmallThreadingDetector.CrashingState state = getAndWait(owner, SmallThreadingDetector.ThreadRole.BYSTANDER);
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }
            throw new RuntimeException("Bystander to crash of type" + state.name + "on threads " + state.releaseThread + ", " + state.acquireThread);
        }

        private static SmallThreadingDetector.CrashingState getAndWait(SmallThreadDetectable owner, SmallThreadingDetector.ThreadRole role) {
            SmallThreadingDetector.CrashingState result;
            synchronized (MONITOR) {
                result = (SmallThreadingDetector.CrashingState) Objects.requireNonNull((SmallThreadingDetector.CrashingState) ACTIVE_CRASHES.get(owner));
            }
            result.waitUntilReady(role);
            return result;
        }
    }

    private static enum ThreadRole {

        ACQUIRE, RELEASE, BYSTANDER
    }
}