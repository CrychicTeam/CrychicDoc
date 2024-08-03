package net.minecraft.world;

public class InteractionResultHolder<T> {

    private final InteractionResult result;

    private final T object;

    public InteractionResultHolder(InteractionResult interactionResult0, T t1) {
        this.result = interactionResult0;
        this.object = t1;
    }

    public InteractionResult getResult() {
        return this.result;
    }

    public T getObject() {
        return this.object;
    }

    public static <T> InteractionResultHolder<T> success(T t0) {
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, t0);
    }

    public static <T> InteractionResultHolder<T> consume(T t0) {
        return new InteractionResultHolder<>(InteractionResult.CONSUME, t0);
    }

    public static <T> InteractionResultHolder<T> pass(T t0) {
        return new InteractionResultHolder<>(InteractionResult.PASS, t0);
    }

    public static <T> InteractionResultHolder<T> fail(T t0) {
        return new InteractionResultHolder<>(InteractionResult.FAIL, t0);
    }

    public static <T> InteractionResultHolder<T> sidedSuccess(T t0, boolean boolean1) {
        return boolean1 ? success(t0) : consume(t0);
    }
}