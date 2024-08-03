package ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation;

@FunctionalInterface
public interface Operation<R> {

    R call(Object... var1);
}