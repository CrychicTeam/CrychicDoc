package net.blay09.mods.balm.forge.client;

import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.client.BalmClientRuntime;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.blay09.mods.balm.api.client.rendering.BalmRenderers;
import net.blay09.mods.balm.api.client.rendering.BalmTextures;
import net.blay09.mods.balm.api.client.screen.BalmScreens;
import net.blay09.mods.balm.forge.client.keymappings.ForgeBalmKeyMappings;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmModels;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmRenderers;
import net.blay09.mods.balm.forge.client.rendering.ForgeBalmTextures;
import net.blay09.mods.balm.forge.client.screen.ForgeBalmScreens;
import net.blay09.mods.balm.forge.event.ForgeBalmClientEvents;
import net.blay09.mods.balm.forge.event.ForgeBalmEvents;

public class ForgeBalmClientRuntime implements BalmClientRuntime {

    private final BalmRenderers renderers = new ForgeBalmRenderers();

    private final BalmTextures textures = new ForgeBalmTextures();

    private final BalmScreens screens = new ForgeBalmScreens();

    private final BalmKeyMappings keyMappings = new ForgeBalmKeyMappings();

    private final BalmModels models = new ForgeBalmModels();

    public ForgeBalmClientRuntime() {
        ForgeBalmClientEvents.registerEvents((ForgeBalmEvents) Balm.getEvents());
    }

    @Override
    public BalmRenderers getRenderers() {
        return this.renderers;
    }

    @Override
    public BalmTextures getTextures() {
        return this.textures;
    }

    @Override
    public BalmScreens getScreens() {
        return this.screens;
    }

    @Override
    public BalmModels getModels() {
        return this.models;
    }

    @Override
    public BalmKeyMappings getKeyMappings() {
        return this.keyMappings;
    }

    @Override
    public void initialize(String modId, Runnable initializer) {
        ((ForgeBalmRenderers) this.renderers).register();
        ((ForgeBalmScreens) this.screens).register();
        ((ForgeBalmModels) this.models).register();
        ((ForgeBalmKeyMappings) this.keyMappings).register();
        initializer.run();
    }
}