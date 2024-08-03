package net.raphimc.immediatelyfast.compat;

@FunctionalInterface
public interface TriConsumer<A, B, C> {

    void accept(A var1, B var2, C var3);
}