package snownee.kiwi.schedule;

import javax.annotation.Nullable;

public abstract class Task<T extends ITicker> {

    public abstract boolean tick(T var1);

    @Nullable
    public abstract T ticker();

    public boolean shouldSave() {
        return true;
    }
}