package org.violetmoon.zeta.mixin.plugin;

public @interface DelegateReturnValueModifier {

    String[] target();

    String delegate();

    String desc();
}