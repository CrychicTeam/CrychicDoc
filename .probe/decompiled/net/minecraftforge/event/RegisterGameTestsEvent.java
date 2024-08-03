package net.minecraftforge.event;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class RegisterGameTestsEvent extends Event implements IModBusEvent {

    private final Set<Method> gameTestMethods;

    public RegisterGameTestsEvent(Set<Method> gameTestMethods) {
        this.gameTestMethods = gameTestMethods;
    }

    public void register(Class<?> testClass) {
        Arrays.stream(testClass.getDeclaredMethods()).forEach(this::register);
    }

    public void register(Method testMethod) {
        this.gameTestMethods.add(testMethod);
    }
}