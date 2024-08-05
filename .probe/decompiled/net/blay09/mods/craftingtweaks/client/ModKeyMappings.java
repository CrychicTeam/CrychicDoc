package net.blay09.mods.craftingtweaks.client;

import net.blay09.mods.craftingtweaks.CompressType;
import net.blay09.mods.kuma.api.InputBinding;
import net.blay09.mods.kuma.api.KeyConflictContext;
import net.blay09.mods.kuma.api.KeyModifier;
import net.blay09.mods.kuma.api.KeyModifiers;
import net.blay09.mods.kuma.api.Kuma;
import net.blay09.mods.kuma.api.ManagedKeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ModKeyMappings {

    public static ManagedKeyMapping keyRotate;

    public static ManagedKeyMapping keyRotateCounterClockwise;

    public static ManagedKeyMapping keyBalance;

    public static ManagedKeyMapping keySpread;

    public static ManagedKeyMapping keyClear;

    public static ManagedKeyMapping keyForceClear;

    public static ManagedKeyMapping keyCompressOne;

    public static ManagedKeyMapping keyCompressStack;

    public static ManagedKeyMapping keyCompressAll;

    public static ManagedKeyMapping keyDecompressOne;

    public static ManagedKeyMapping keyDecompressStack;

    public static ManagedKeyMapping keyDecompressAll;

    public static ManagedKeyMapping keyRefillLast;

    public static ManagedKeyMapping keyRefillLastStack;

    public static ManagedKeyMapping keyTransferStack;

    public static void initialize() {
        keyRotate = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "rotate")).withContext(KeyConflictContext.SCREEN).build();
        keyRotateCounterClockwise = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "rotate_counter_clockwise")).withContext(KeyConflictContext.SCREEN).build();
        keyBalance = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "balance")).withContext(KeyConflictContext.SCREEN).build();
        keySpread = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "spread")).withContext(KeyConflictContext.SCREEN).build();
        keyClear = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "clear")).withContext(KeyConflictContext.SCREEN).build();
        keyForceClear = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "force_clear")).withContext(KeyConflictContext.SCREEN).build();
        keyCompressOne = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "compress_one")).withDefault(InputBinding.key(75, KeyModifiers.of(new KeyModifier[] { KeyModifier.CONTROL }))).withFallbackDefault(InputBinding.none()).withContext(KeyConflictContext.SCREEN).build();
        keyCompressStack = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "compress_stack")).withDefault(InputBinding.key(75)).withContext(KeyConflictContext.SCREEN).build();
        keyCompressAll = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "compress_all")).withDefault(InputBinding.key(75, KeyModifiers.of(new KeyModifier[] { KeyModifier.SHIFT }))).withFallbackDefault(InputBinding.none()).withContext(KeyConflictContext.SCREEN).build();
        keyDecompressOne = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "decompress_one")).withContext(KeyConflictContext.SCREEN).build();
        keyDecompressStack = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "decompress_stack")).withContext(KeyConflictContext.SCREEN).build();
        keyDecompressAll = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "decompress_all")).withContext(KeyConflictContext.SCREEN).build();
        keyRefillLast = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "refill_last")).withDefault(InputBinding.key(258, KeyModifiers.of(new KeyModifier[] { KeyModifier.CONTROL }))).withContext(KeyConflictContext.SCREEN).build();
        keyRefillLastStack = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "refill_last_stack")).withDefault(InputBinding.key(258)).withContext(KeyConflictContext.SCREEN).build();
        keyTransferStack = Kuma.createKeyMapping(new ResourceLocation("craftingtweaks", "transfer_stack")).withContext(KeyConflictContext.SCREEN).build();
    }

    @Nullable
    public static CompressType getCompressTypeForKey(int keyCode, int scanCode, int modifiers) {
        if (keyCompressOne.isActiveAndMatchesKey(keyCode, scanCode, modifiers)) {
            return CompressType.COMPRESS_ONE;
        } else if (keyCompressStack.isActiveAndMatchesKey(keyCode, scanCode, modifiers)) {
            return CompressType.COMPRESS_STACK;
        } else if (keyCompressAll.isActiveAndMatchesKey(keyCode, scanCode, modifiers)) {
            return CompressType.COMPRESS_ALL;
        } else if (keyDecompressOne.isActiveAndMatchesKey(keyCode, scanCode, modifiers)) {
            return CompressType.DECOMPRESS_ONE;
        } else if (keyDecompressStack.isActiveAndMatchesKey(keyCode, scanCode, modifiers)) {
            return CompressType.DECOMPRESS_STACK;
        } else {
            return keyDecompressAll.isActiveAndMatchesKey(keyCode, scanCode, modifiers) ? CompressType.DECOMPRESS_ALL : null;
        }
    }
}