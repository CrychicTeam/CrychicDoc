package org.embeddedt.embeddium.client.gui.options;

import net.minecraft.resources.ResourceLocation;

public final class StandardOptions {

    public static class Group {

        public static final ResourceLocation RENDERING = new ResourceLocation("minecraft", "rendering");

        public static final ResourceLocation WINDOW = new ResourceLocation("minecraft", "window");

        public static final ResourceLocation INDICATORS = new ResourceLocation("minecraft", "indicators");

        public static final ResourceLocation GRAPHICS = new ResourceLocation("minecraft", "graphics");

        public static final ResourceLocation MIPMAPS = new ResourceLocation("minecraft", "mipmaps");

        public static final ResourceLocation DETAILS = new ResourceLocation("minecraft", "details");

        public static final ResourceLocation CHUNK_UPDATES = new ResourceLocation("embeddium", "chunk_updates");

        public static final ResourceLocation RENDERING_CULLING = new ResourceLocation("embeddium", "rendering_culling");

        public static final ResourceLocation CPU_SAVING = new ResourceLocation("embeddium", "cpu_saving");

        public static final ResourceLocation SORTING = new ResourceLocation("embeddium", "sorting");
    }

    public static class Option {

        public static final ResourceLocation RENDER_DISTANCE = new ResourceLocation("minecraft", "render_distance");

        public static final ResourceLocation SIMULATION_DISTANCE = new ResourceLocation("minecraft", "simulation_distance");

        public static final ResourceLocation BRIGHTNESS = new ResourceLocation("minecraft", "brightness");

        public static final ResourceLocation GUI_SCALE = new ResourceLocation("minecraft", "gui_scale");

        public static final ResourceLocation FULLSCREEN = new ResourceLocation("minecraft", "fullscreen");

        public static final ResourceLocation VSYNC = new ResourceLocation("minecraft", "vsync");

        public static final ResourceLocation MAX_FRAMERATE = new ResourceLocation("minecraft", "max_frame_rate");

        public static final ResourceLocation VIEW_BOBBING = new ResourceLocation("minecraft", "view_bobbing");

        public static final ResourceLocation ATTACK_INDICATOR = new ResourceLocation("minecraft", "attack_indicator");

        public static final ResourceLocation AUTOSAVE_INDICATOR = new ResourceLocation("minecraft", "autosave_indicator");

        public static final ResourceLocation GRAPHICS_MODE = new ResourceLocation("minecraft", "graphics_mode");

        public static final ResourceLocation CLOUDS = new ResourceLocation("minecraft", "clouds");

        public static final ResourceLocation WEATHER = new ResourceLocation("minecraft", "weather");

        public static final ResourceLocation LEAVES = new ResourceLocation("minecraft", "leaves");

        public static final ResourceLocation PARTICLES = new ResourceLocation("minecraft", "particles");

        public static final ResourceLocation SMOOTH_LIGHT = new ResourceLocation("minecraft", "smooth_lighting");

        public static final ResourceLocation BIOME_BLEND = new ResourceLocation("minecraft", "biome_blend");

        public static final ResourceLocation ENTITY_DISTANCE = new ResourceLocation("minecraft", "entity_distance");

        public static final ResourceLocation ENTITY_SHADOWS = new ResourceLocation("minecraft", "entity_shadows");

        public static final ResourceLocation VIGNETTE = new ResourceLocation("minecraft", "vignette");

        public static final ResourceLocation MIPMAP_LEVEL = new ResourceLocation("minecraft", "mipmap_levels");

        public static final ResourceLocation CHUNK_UPDATE_THREADS = new ResourceLocation("embeddium", "chunk_update_threads");

        public static final ResourceLocation DEFFER_CHUNK_UPDATES = new ResourceLocation("embeddium", "defer_chunk_updates");

        public static final ResourceLocation BLOCK_FACE_CULLING = new ResourceLocation("embeddium", "block_face_culling");

        public static final ResourceLocation COMPACT_VERTEX_FORMAT = new ResourceLocation("embeddium", "compact_vertex_format");

        public static final ResourceLocation FOG_OCCLUSION = new ResourceLocation("embeddium", "fog_occlusion");

        public static final ResourceLocation ENTITY_CULLING = new ResourceLocation("embeddium", "entity_culling");

        public static final ResourceLocation ANIMATE_VISIBLE_TEXTURES = new ResourceLocation("embeddium", "animate_only_visible_textures");

        public static final ResourceLocation NO_ERROR_CONTEXT = new ResourceLocation("embeddium", "no_error_context");

        public static final ResourceLocation PERSISTENT_MAPPING = new ResourceLocation("embeddium", "persistent_mapping");

        public static final ResourceLocation CPU_FRAMES_AHEAD = new ResourceLocation("embeddium", "cpu_render_ahead_limit");

        public static final ResourceLocation TRANSLUCENT_FACE_SORTING = new ResourceLocation("embeddium", "translucent_face_sorting");
    }

    public static class Pages {

        public static final OptionIdentifier<Void> GENERAL = OptionIdentifier.create("embeddium", "general");

        public static final OptionIdentifier<Void> QUALITY = OptionIdentifier.create("embeddium", "quality");

        public static final OptionIdentifier<Void> PERFORMANCE = OptionIdentifier.create("embeddium", "performance");

        public static final OptionIdentifier<Void> ADVANCED = OptionIdentifier.create("embeddium", "advanced");
    }
}