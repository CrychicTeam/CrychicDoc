package fr.frinn.custommachinery.api.crafting;

import net.minecraft.network.chat.Component;

public final class CraftingResult {

    private static final CraftingResult SUCCESS = new CraftingResult(CraftingResult.RESULT.SUCCESS, Component.literal("success"));

    private static final CraftingResult PASS = new CraftingResult(CraftingResult.RESULT.PASS, Component.literal("pass"));

    private final CraftingResult.RESULT result;

    private final Component message;

    private CraftingResult(CraftingResult.RESULT result, Component message) {
        this.result = result;
        this.message = message;
    }

    public static CraftingResult success() {
        return SUCCESS;
    }

    public static CraftingResult pass() {
        return PASS;
    }

    public static CraftingResult error(Component message) {
        return new CraftingResult(CraftingResult.RESULT.ERROR, message);
    }

    public boolean isSuccess() {
        return this.result != CraftingResult.RESULT.ERROR;
    }

    public Component getMessage() {
        return this.message;
    }

    public static enum RESULT {

        SUCCESS, PASS, ERROR
    }
}