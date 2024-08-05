package se.mickelus.mutil.gui.animation;

import java.util.function.Consumer;

public class AnimationChain implements GuiAnimation {

    private final KeyframeAnimation[] animations;

    private KeyframeAnimation activeAnimation;

    private boolean looping = false;

    private Consumer<Boolean> stopHandler;

    private boolean isActive;

    public AnimationChain(KeyframeAnimation... animations) {
        this.animations = animations;
        for (int i = 0; i < animations.length; i++) {
            int index = i;
            animations[i].onStop(isActive -> {
                if (isActive) {
                    this.startNext(index);
                } else if (this.stopHandler != null) {
                    this.stopHandler.accept(false);
                }
            });
        }
    }

    public AnimationChain setLooping(boolean looping) {
        this.looping = looping;
        return this;
    }

    public AnimationChain onStop(Consumer<Boolean> handler) {
        this.stopHandler = handler;
        return this;
    }

    @Override
    public void stop() {
        this.isActive = false;
        if (this.activeAnimation != null) {
            this.activeAnimation.stop();
        }
    }

    @Override
    public void start() {
        this.isActive = true;
        this.activeAnimation = this.animations[0];
        this.activeAnimation.start();
    }

    private void startNext(int currentIndex) {
        if (this.isActive) {
            if (currentIndex + 1 >= this.animations.length) {
                if (this.looping) {
                    this.activeAnimation = this.animations[0];
                } else {
                    if (this.stopHandler != null) {
                        this.stopHandler.accept(true);
                    }
                    this.activeAnimation = null;
                    this.isActive = false;
                }
            } else {
                this.activeAnimation = this.animations[currentIndex + 1];
            }
            if (this.activeAnimation != null) {
                this.activeAnimation.start();
            }
        }
    }
}