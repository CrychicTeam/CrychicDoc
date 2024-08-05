package net.raphimc.immediatelyfast.apiimpl;

import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfastapi.ConfigAccess;

public class RuntimeConfigAccessImpl implements ConfigAccess {

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return switch(key) {
            case "font_atlas_resizing" ->
                ImmediatelyFast.runtimeConfig.font_atlas_resizing;
            case "hud_batching" ->
                ImmediatelyFast.runtimeConfig.hud_batching;
            case "fast_buffer_upload" ->
                ImmediatelyFast.runtimeConfig.fast_buffer_upload;
            case "legacy_fast_buffer_upload" ->
                ImmediatelyFast.runtimeConfig.legacy_fast_buffer_upload;
            default ->
                defaultValue;
        };
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return defaultValue;
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return defaultValue;
    }

    @Override
    public String getString(String key, String defaultValue) {
        return defaultValue;
    }
}