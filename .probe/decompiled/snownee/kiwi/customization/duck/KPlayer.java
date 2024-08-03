package snownee.kiwi.customization.duck;

public interface KPlayer {

    void kiwi$setPlaceCount(int var1);

    int kiwi$getPlaceCount();

    default void kiwi$incrementPlaceCount() {
        this.kiwi$setPlaceCount(this.kiwi$getPlaceCount() + 1);
    }
}