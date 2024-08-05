package icyllis.modernui.graphics.drawable;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jetbrains.annotations.ApiStatus.Internal;

public class AnimatedImageDrawable extends Drawable implements Animatable2 {

    private static final ExecutorService ANIMATED_IMAGE_EXECUTOR = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "Animated-Image-Thread");
        t.setDaemon(true);
        t.setPriority(6);
        return t;
    });

    @Internal
    public static Executor getAnimatedImageExecutor() {
        return ANIMATED_IMAGE_EXECUTOR;
    }

    @Override
    public void start() {
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void registerAnimationCallback(@NonNull Animatable2.AnimationCallback callback) {
    }

    @Override
    public boolean unregisterAnimationCallback(@NonNull Animatable2.AnimationCallback callback) {
        return false;
    }

    @Override
    public void clearAnimationCallbacks() {
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
    }
}