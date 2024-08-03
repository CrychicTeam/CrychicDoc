package net.liopyu.entityjs.util;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.core.animation.Animation;

public class EntityJSHelperClass {

    public static final Set<String> errorMessagesLogged = new HashSet();

    public static final Set<String> warningMessagesLogged = new HashSet();

    public static void logErrorMessageOnce(String errorMessage) {
        if (!errorMessagesLogged.contains(errorMessage)) {
            ConsoleJS.STARTUP.error(errorMessage);
            errorMessagesLogged.add(errorMessage);
        }
    }

    public static void logWarningMessageOnce(String errorMessage) {
        if (!warningMessagesLogged.contains(errorMessage)) {
            ConsoleJS.STARTUP.warn(errorMessage);
            warningMessagesLogged.add(errorMessage);
        }
    }

    public static void logErrorMessageOnceCatchable(String errorMessage, Throwable e) {
        if (!errorMessagesLogged.contains(errorMessage)) {
            ConsoleJS.STARTUP.error(errorMessage, e);
            errorMessagesLogged.add(errorMessage);
        }
    }

    public static <T> boolean consumerCallback(Consumer<T> consumer, T value, String errorMessage) {
        try {
            consumer.accept(value);
            return true;
        } catch (Throwable var4) {
            logErrorMessageOnceCatchable(errorMessage, var4);
            return false;
        }
    }

    public static Object convertObjectToDesired(Object input, String outputType) {
        String var2 = outputType.toLowerCase();
        return switch(var2) {
            case "integer" ->
                convertToInteger(input);
            case "double" ->
                convertToDouble(input);
            case "float" ->
                convertToFloat(input);
            case "boolean" ->
                convertToBoolean(input);
            case "interactionresult" ->
                convertToInteractionResult(input);
            case "resourcelocation" ->
                convertToResourceLocation(input);
            case "looptype" ->
                convertToLoopType(input);
            default ->
                input;
        };
    }

    private static Animation.LoopType convertToLoopType(Object input) {
        if (input instanceof Animation.LoopType) {
            return (Animation.LoopType) input;
        } else if (input instanceof String) {
            String stringValue = ((String) input).toUpperCase();
            return switch(stringValue) {
                case "LOOP" ->
                    Animation.LoopType.LOOP;
                case "PLAY_ONCE" ->
                    Animation.LoopType.PLAY_ONCE;
                case "HOLD_ON_LAST_FRAME" ->
                    Animation.LoopType.HOLD_ON_LAST_FRAME;
                default ->
                    Animation.LoopType.DEFAULT;
            };
        } else {
            return Animation.LoopType.DEFAULT;
        }
    }

    private static ResourceLocation convertToResourceLocation(Object input) {
        if (input instanceof ResourceLocation) {
            return (ResourceLocation) input;
        } else {
            return input instanceof String ? new ResourceLocation((String) input) : null;
        }
    }

    private static InteractionResult convertToInteractionResult(Object input) {
        if (input instanceof InteractionResult) {
            return (InteractionResult) input;
        } else {
            if (input instanceof String) {
                String stringValue = ((String) input).toLowerCase();
                switch(stringValue) {
                    case "success":
                        return InteractionResult.SUCCESS;
                    case "consume":
                        return InteractionResult.CONSUME;
                    case "pass":
                        return InteractionResult.PASS;
                    case "fail":
                        return InteractionResult.FAIL;
                    case "consume_partial":
                        return InteractionResult.CONSUME_PARTIAL;
                }
            }
            return null;
        }
    }

    private static Boolean convertToBoolean(Object input) {
        if (input instanceof Boolean) {
            return (Boolean) input;
        } else {
            if (input instanceof String) {
                String stringValue = ((String) input).toLowerCase();
                if ("true".equals(stringValue)) {
                    return true;
                }
                if ("false".equals(stringValue)) {
                    return false;
                }
            }
            return null;
        }
    }

    private static Integer convertToInteger(Object input) {
        if (input instanceof Integer) {
            return (Integer) input;
        } else {
            return !(input instanceof Double) && !(input instanceof Float) ? null : ((Number) input).intValue();
        }
    }

    private static Double convertToDouble(Object input) {
        if (input instanceof Double) {
            return (Double) input;
        } else {
            return !(input instanceof Integer) && !(input instanceof Float) ? null : ((Number) input).doubleValue();
        }
    }

    private static Float convertToFloat(Object input) {
        if (input instanceof Float) {
            return (Float) input;
        } else {
            return !(input instanceof Integer) && !(input instanceof Double) ? null : ((Number) input).floatValue();
        }
    }

    public static class EntityMovementTracker {

        private double prevX = 0.0;

        private double prevY = 0.0;

        private double prevZ = 0.0;

        public boolean isMoving(Entity entity) {
            double currentX = entity.getX();
            double currentY = entity.getY();
            double currentZ = entity.getZ();
            boolean moving = currentX != this.prevX || currentY != this.prevY || currentZ != this.prevZ;
            this.prevX = currentX;
            this.prevY = currentY;
            this.prevZ = currentZ;
            return moving;
        }
    }
}