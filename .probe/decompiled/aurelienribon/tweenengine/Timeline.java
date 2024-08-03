package aurelienribon.tweenengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Timeline extends BaseTween<Timeline> {

    private static final Pool.Callback<Timeline> poolCallback = new Pool.Callback<Timeline>() {

        public void onPool(Timeline obj) {
            obj.reset();
        }

        public void onUnPool(Timeline obj) {
            obj.reset();
        }
    };

    static final Pool<Timeline> pool = new Pool<Timeline>(10, poolCallback) {

        protected Timeline create() {
            return new Timeline();
        }
    };

    private final List<BaseTween<?>> children = new ArrayList(10);

    private Timeline current;

    private Timeline parent;

    private Timeline.Modes mode;

    private boolean isBuilt;

    public static int getPoolSize() {
        return pool.size();
    }

    public static void ensurePoolCapacity(int minCapacity) {
        pool.ensureCapacity(minCapacity);
    }

    public static Timeline createSequence() {
        Timeline tl = pool.get();
        tl.setup(Timeline.Modes.SEQUENCE);
        return tl;
    }

    public static Timeline createParallel() {
        Timeline tl = pool.get();
        tl.setup(Timeline.Modes.PARALLEL);
        return tl;
    }

    private Timeline() {
        this.reset();
    }

    @Override
    protected void reset() {
        super.reset();
        this.children.clear();
        this.current = this.parent = null;
        this.isBuilt = false;
    }

    private void setup(Timeline.Modes mode) {
        this.mode = mode;
        this.current = this;
    }

    public Timeline push(Tween tween) {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else {
            this.current.children.add(tween);
            return this;
        }
    }

    public Timeline push(Timeline timeline) {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else if (timeline.current != timeline) {
            throw new RuntimeException("You forgot to call a few 'end()' statements in your pushed timeline");
        } else {
            timeline.parent = this.current;
            this.current.children.add(timeline);
            return this;
        }
    }

    public Timeline pushPause(float time) {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else {
            this.current.children.add(Tween.mark().delay(time));
            return this;
        }
    }

    public Timeline beginSequence() {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else {
            Timeline tl = pool.get();
            tl.parent = this.current;
            tl.mode = Timeline.Modes.SEQUENCE;
            this.current.children.add(tl);
            this.current = tl;
            return this;
        }
    }

    public Timeline beginParallel() {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else {
            Timeline tl = pool.get();
            tl.parent = this.current;
            tl.mode = Timeline.Modes.PARALLEL;
            this.current.children.add(tl);
            this.current = tl;
            return this;
        }
    }

    public Timeline end() {
        if (this.isBuilt) {
            throw new RuntimeException("You can't push anything to a timeline once it is started");
        } else if (this.current == this) {
            throw new RuntimeException("Nothing to end...");
        } else {
            this.current = this.current.parent;
            return this;
        }
    }

    public List<BaseTween<?>> getChildren() {
        return this.isBuilt ? Collections.unmodifiableList(this.current.children) : this.current.children;
    }

    public Timeline build() {
        if (this.isBuilt) {
            return this;
        } else {
            this.duration = 0.0F;
            for (BaseTween<?> obj : this.children) {
                if (obj.getRepeatCount() < 0) {
                    throw new RuntimeException("You can't push an object with infinite repetitions in a timeline");
                }
                obj.build();
                switch(this.mode) {
                    case SEQUENCE:
                        float tDelay = this.duration;
                        this.duration = this.duration + obj.getFullDuration();
                        obj.delay += tDelay;
                        break;
                    case PARALLEL:
                        this.duration = Math.max(this.duration, obj.getFullDuration());
                }
            }
            this.isBuilt = true;
            return this;
        }
    }

    public Timeline start() {
        super.start();
        for (BaseTween<?> obj : this.children) {
            obj.start();
        }
        return this;
    }

    @Override
    public void free() {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            BaseTween<?> obj = (BaseTween<?>) this.children.remove(i);
            obj.free();
        }
        pool.free(this);
    }

    @Override
    protected void updateOverride(int step, int lastStep, boolean isIterationStep, float delta) {
        if (!isIterationStep && step > lastStep) {
            assert delta >= 0.0F;
            float dt = this.isReverse(lastStep) ? -delta - 1.0F : delta + 1.0F;
            for (BaseTween<?> aChildren : this.children) {
                aChildren.update(dt);
            }
        } else if (!isIterationStep && step < lastStep) {
            assert delta <= 0.0F;
            float dt = this.isReverse(lastStep) ? -delta - 1.0F : delta + 1.0F;
            for (int i = this.children.size() - 1; i >= 0; i--) {
                ((BaseTween) this.children.get(i)).update(dt);
            }
        } else {
            assert isIterationStep;
            if (step > lastStep) {
                if (this.isReverse(step)) {
                    this.forceEndValues();
                    for (BaseTween<?> aChildren : this.children) {
                        aChildren.update(delta);
                    }
                } else {
                    this.forceStartValues();
                    for (BaseTween<?> aChildren : this.children) {
                        aChildren.update(delta);
                    }
                }
            } else if (step < lastStep) {
                if (this.isReverse(step)) {
                    this.forceStartValues();
                    for (int i = this.children.size() - 1; i >= 0; i--) {
                        ((BaseTween) this.children.get(i)).update(delta);
                    }
                } else {
                    this.forceEndValues();
                    for (int i = this.children.size() - 1; i >= 0; i--) {
                        ((BaseTween) this.children.get(i)).update(delta);
                    }
                }
            } else {
                float dt = this.isReverse(step) ? -delta : delta;
                if (delta >= 0.0F) {
                    for (BaseTween<?> aChildren : this.children) {
                        aChildren.update(dt);
                    }
                } else {
                    for (int i = this.children.size() - 1; i >= 0; i--) {
                        ((BaseTween) this.children.get(i)).update(dt);
                    }
                }
            }
        }
    }

    @Override
    protected void forceStartValues() {
        for (int i = this.children.size() - 1; i >= 0; i--) {
            BaseTween<?> obj = (BaseTween<?>) this.children.get(i);
            obj.forceToStart();
        }
    }

    @Override
    protected void forceEndValues() {
        for (BaseTween<?> obj : this.children) {
            obj.forceToEnd(this.duration);
        }
    }

    @Override
    protected boolean containsTarget(Object target) {
        for (BaseTween<?> obj : this.children) {
            if (obj.containsTarget(target)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected boolean containsTarget(Object target, int tweenType) {
        for (BaseTween<?> obj : this.children) {
            if (obj.containsTarget(target, tweenType)) {
                return true;
            }
        }
        return false;
    }

    private static enum Modes {

        SEQUENCE, PARALLEL
    }
}