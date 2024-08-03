package net.raphimc.immediatelyfast.feature.core;

public class ImmediatelyFastConfig {

    private String REGULAR_INFO = "----- Regular config values below -----";

    public boolean font_atlas_resizing = true;

    public boolean map_atlas_generation = true;

    public boolean hud_batching = true;

    public boolean fast_text_lookup = true;

    public boolean fast_buffer_upload = true;

    public long fast_buffer_upload_size_mb = 256L;

    public boolean fast_buffer_upload_explicit_flush = true;

    private String COSMETIC_INFO = "----- Cosmetic only config values below (Does not optimize anything) -----";

    public boolean dont_add_info_into_debug_hud = false;

    private String EXPERIMENTAL_INFO = "----- Experimental config values below (Rendering glitches may occur) -----";

    public boolean experimental_disable_error_checking = false;

    public boolean experimental_disable_resource_pack_conflict_handling = false;

    public boolean experimental_sign_text_buffering = false;

    public boolean experimental_screen_batching = false;

    private String DEBUG_INFO = "----- Debug only config values below (Do not touch) -----";

    public boolean debug_only_and_not_recommended_disable_universal_batching = false;

    public boolean debug_only_and_not_recommended_disable_mod_conflict_handling = false;

    public boolean debug_only_and_not_recommended_disable_hardware_conflict_handling = false;

    public boolean debug_only_print_additional_error_information = false;

    public boolean debug_only_use_last_usage_for_batch_ordering = false;
}