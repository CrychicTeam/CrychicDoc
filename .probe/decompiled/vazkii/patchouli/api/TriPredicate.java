package vazkii.patchouli.api;

@FunctionalInterface
public interface TriPredicate<A, B, C> {

    boolean test(A var1, B var2, C var3);
}