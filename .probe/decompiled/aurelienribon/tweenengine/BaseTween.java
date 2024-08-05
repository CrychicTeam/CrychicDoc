package aurelienribon.tweenengine;

import java.util.function.Consumer;

public abstract class BaseTween<T> {

    private int step;

    private int repeatCnt;

    private boolean isIterationStep;

    private boolean isYoyo;

    protected float delay;

    protected float duration;

    private float repeatDelay;

    private float currentTime;

    private float deltaTime;

    private boolean isStarted;

    private boolean isInitialized;

    private boolean isFinished;

    private boolean isKilled;

    private boolean isPaused;

    private TweenCallback callback;

    private int callbackTriggers;

    private Object userData;

    boolean isAutoRemoveEnabled;

    boolean isAutoStartEnabled;

    protected void reset() {
        this.step = -2;
        this.repeatCnt = 0;
        this.isIterationStep = this.isYoyo = false;
        this.delay = this.duration = this.repeatDelay = this.currentTime = this.deltaTime = 0.0F;
        this.isStarted = this.isInitialized = this.isFinished = this.isKilled = this.isPaused = false;
        this.callback = null;
        this.callbackTriggers = 8;
        this.userData = null;
        this.isAutoRemoveEnabled = this.isAutoStartEnabled = true;
    }

    public T build() {
        return (T) this;
    }

    public T start() {
        this.build();
        this.currentTime = 0.0F;
        this.isStarted = true;
        return (T) this;
    }

    public T start(TweenManager manager) {
        manager.add(this);
        return (T) this;
    }

    public T delay(float delay) {
        this.delay += delay;
        return (T) this;
    }

    public void kill() {
        this.isKilled = true;
    }

    public void free() {
    }

    public void pause() {
        this.isPaused = true;
    }

    public void resume() {
        this.isPaused = false;
    }

    public T repeat(int count, float delay) {
        if (this.isStarted) {
            throw new RuntimeException("You can't change the repetitions of a tween or timeline once it is started");
        } else {
            this.repeatCnt = count;
            this.repeatDelay = delay >= 0.0F ? delay : 0.0F;
            this.isYoyo = false;
            return (T) this;
        }
    }

    public T repeatYoyo(int count, float delay) {
        if (this.isStarted) {
            throw new RuntimeException("You can't change the repetitions of a tween or timeline once it is started");
        } else {
            this.repeatCnt = count;
            this.repeatDelay = delay >= 0.0F ? delay : 0.0F;
            this.isYoyo = true;
            return (T) this;
        }
    }

    public T setCallback(TweenCallback callback) {
        this.callback = callback;
        return (T) this;
    }

    public T addCallback(TweenCallback callback) {
        if (this.callback == null) {
            this.callback = callback;
        } else {
            this.callback = this.callback.andThen(callback);
        }
        return (T) this;
    }

    public T addCallback(int flags, Consumer<BaseTween<?>> callback) {
        this.callbackTriggers |= flags;
        return this.addCallback((type, source) -> {
            if ((flags & type) != 0) {
                callback.accept(source);
            }
        });
    }

    public T setCallbackTriggers(int flags) {
        this.callbackTriggers = flags;
        return (T) this;
    }

    public T setUserData(Object data) {
        this.userData = data;
        return (T) this;
    }

    public float getDelay() {
        return this.delay;
    }

    public float getDuration() {
        return this.duration;
    }

    public int getRepeatCount() {
        return this.repeatCnt;
    }

    public float getRepeatDelay() {
        return this.repeatDelay;
    }

    public float getFullDuration() {
        return this.repeatCnt < 0 ? -1.0F : this.delay + this.duration + (this.repeatDelay + this.duration) * (float) this.repeatCnt;
    }

    public Object getUserData() {
        return this.userData;
    }

    public int getStep() {
        return this.step;
    }

    public float getCurrentTime() {
        return this.currentTime;
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public boolean isInitialized() {
        return this.isInitialized;
    }

    public boolean isFinished() {
        return this.isFinished || this.isKilled;
    }

    public boolean isYoyo() {
        return this.isYoyo;
    }

    public boolean isPaused() {
        return this.isPaused;
    }

    protected abstract void forceStartValues();

    protected abstract void forceEndValues();

    protected abstract boolean containsTarget(Object var1);

    protected abstract boolean containsTarget(Object var1, int var2);

    protected void initializeOverride() {
    }

    protected void updateOverride(int step, int lastStep, boolean isIterationStep, float delta) {
    }

    protected void forceToStart() {
        this.currentTime = -this.delay;
        this.step = -1;
        this.isIterationStep = false;
        if (this.isReverse(0)) {
            this.forceEndValues();
        } else {
            this.forceStartValues();
        }
    }

    protected void forceToEnd(float time) {
        this.currentTime = time - this.getFullDuration();
        this.step = this.repeatCnt * 2 + 1;
        this.isIterationStep = false;
        if (this.isReverse(this.repeatCnt * 2)) {
            this.forceStartValues();
        } else {
            this.forceEndValues();
        }
    }

    protected void callCallback(int type) {
        if (this.callback != null && (this.callbackTriggers & type) > 0) {
            this.callback.onEvent(type, this);
        }
    }

    protected boolean isReverse(int step) {
        return this.isYoyo && Math.abs(step % 4) == 2;
    }

    protected boolean isValid(int step) {
        return step >= 0 && step <= this.repeatCnt * 2 || this.repeatCnt < 0;
    }

    protected void killTarget(Object target) {
        if (this.containsTarget(target)) {
            this.kill();
        }
    }

    protected void killTarget(Object target, int tweenType) {
        if (this.containsTarget(target, tweenType)) {
            this.kill();
        }
    }

    public void update(float delta) {
        if (this.isStarted && !this.isPaused && !this.isKilled) {
            this.deltaTime = delta;
            if (!this.isInitialized) {
                this.initialize();
            }
            if (this.isInitialized) {
                this.testRelaunch();
                this.updateStep();
                this.testCompletion();
            }
            this.currentTime = this.currentTime + this.deltaTime;
            this.deltaTime = 0.0F;
        }
    }

    private void initialize() {
        if (this.currentTime + this.deltaTime >= this.delay) {
            this.initializeOverride();
            this.isInitialized = true;
            this.isIterationStep = true;
            this.step = 0;
            this.deltaTime = this.deltaTime - (this.delay - this.currentTime);
            this.currentTime = 0.0F;
            this.callCallback(1);
            this.callCallback(2);
        }
    }

    private void testRelaunch() {
        if (!this.isIterationStep && this.repeatCnt >= 0 && this.step < 0 && this.currentTime + this.deltaTime >= 0.0F) {
            assert this.step == -1;
            this.isIterationStep = true;
            this.step = 0;
            float delta = 0.0F - this.currentTime;
            this.deltaTime -= delta;
            this.currentTime = 0.0F;
            this.callCallback(1);
            this.callCallback(2);
            this.updateOverride(this.step, this.step - 1, this.isIterationStep, delta);
        } else if (!this.isIterationStep && this.repeatCnt >= 0 && this.step > this.repeatCnt * 2 && this.currentTime + this.deltaTime < 0.0F) {
            assert this.step == this.repeatCnt * 2 + 1;
            this.isIterationStep = true;
            this.step = this.repeatCnt * 2;
            float delta = 0.0F - this.currentTime;
            this.deltaTime -= delta;
            this.currentTime = this.duration;
            this.callCallback(16);
            this.callCallback(32);
            this.updateOverride(this.step, this.step + 1, this.isIterationStep, delta);
        }
    }

    private void updateStep() {
        while (this.isValid(this.step)) {
            if (!this.isIterationStep && this.currentTime + this.deltaTime <= 0.0F) {
                this.isIterationStep = true;
                this.step--;
                float delta = 0.0F - this.currentTime;
                this.deltaTime -= delta;
                this.currentTime = this.duration;
                if (this.isReverse(this.step)) {
                    this.forceStartValues();
                } else {
                    this.forceEndValues();
                }
                this.callCallback(32);
                this.updateOverride(this.step, this.step + 1, this.isIterationStep, delta);
            } else if (!this.isIterationStep && this.currentTime + this.deltaTime >= this.repeatDelay) {
                this.isIterationStep = true;
                this.step++;
                float delta = this.repeatDelay - this.currentTime;
                this.deltaTime -= delta;
                this.currentTime = 0.0F;
                if (this.isReverse(this.step)) {
                    this.forceEndValues();
                } else {
                    this.forceStartValues();
                }
                this.callCallback(2);
                this.updateOverride(this.step, this.step - 1, this.isIterationStep, delta);
            } else if (this.isIterationStep && this.currentTime + this.deltaTime < 0.0F) {
                this.isIterationStep = false;
                this.step--;
                float delta = 0.0F - this.currentTime;
                this.deltaTime -= delta;
                this.currentTime = 0.0F;
                this.updateOverride(this.step, this.step + 1, false, delta);
                this.callCallback(64);
                if (this.step < 0 && this.repeatCnt >= 0) {
                    this.callCallback(128);
                } else {
                    this.currentTime = this.repeatDelay;
                }
            } else {
                if (!this.isIterationStep || !(this.currentTime + this.deltaTime > this.duration)) {
                    if (this.isIterationStep) {
                        float delta = this.deltaTime;
                        this.deltaTime -= delta;
                        this.currentTime += delta;
                        this.updateOverride(this.step, this.step, true, delta);
                    } else {
                        float delta = this.deltaTime;
                        this.deltaTime -= delta;
                        this.currentTime += delta;
                    }
                    break;
                }
                this.isIterationStep = false;
                this.step++;
                float delta = this.duration - this.currentTime;
                this.deltaTime -= delta;
                this.currentTime = this.duration;
                this.updateOverride(this.step, this.step - 1, false, delta);
                this.callCallback(4);
                if (this.step > this.repeatCnt * 2 && this.repeatCnt >= 0) {
                    this.callCallback(8);
                }
                this.currentTime = 0.0F;
            }
        }
    }

    private void testCompletion() {
        this.isFinished = this.repeatCnt >= 0 && (this.step > this.repeatCnt * 2 || this.step < 0);
    }
}