package com.mna.api.entities.construct;

import java.util.function.Predicate;

public interface IConstructModelRegistrationHelper {

    void register(ConstructMaterial var1, String var2, Predicate<String> var3, Predicate<String> var4, Predicate<String> var5, Predicate<String> var6);
}