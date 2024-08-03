package org.violetmoon.quark.content.tools.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.base.item.QuarkMusicDiscItem;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.bus.ZPhase;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.loading.ZLootTableLoad;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tools")
public class EndermoshMusicDiscModule extends ZetaModule {

    @Config
    protected boolean playEndermoshDuringEnderdragonFight = false;

    @Config
    protected boolean addToEndCityLoot = true;

    @Config
    protected int lootWeight = 5;

    @Config
    protected int lootQuality = 1;

    @Hint
    public QuarkMusicDiscItem endermosh;

    @LoadEvent
    public final void register(ZRegister event) {
        this.endermosh = new QuarkMusicDiscItem(14, () -> QuarkSounds.MUSIC_ENDERMOSH, "endermosh", this, 3783);
    }

    @PlayEvent
    public void onLootTableLoad(ZLootTableLoad event) {
        if (this.addToEndCityLoot) {
            ResourceLocation res = event.getName();
            if (res.equals(BuiltInLootTables.END_CITY_TREASURE)) {
                LootPoolEntryContainer entry = LootItem.lootTableItem(this.endermosh).setWeight(this.lootWeight).setQuality(this.lootQuality).m_7512_();
                event.add(entry);
            }
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends EndermoshMusicDiscModule {

        private boolean isFightingDragon;

        private int delay;

        private SimpleSoundInstance sound;

        @PlayEvent
        public void clientTick(ZClientTick event) {
            if (event.getPhase() == ZPhase.END) {
                if (this.playEndermoshDuringEnderdragonFight) {
                    boolean wasFightingDragon = this.isFightingDragon;
                    Minecraft mc = Minecraft.getInstance();
                    this.isFightingDragon = mc.level != null && mc.level.m_46472_().location().equals(LevelStem.END.location()) && mc.gui.getBossOverlay().shouldPlayMusic();
                    int targetDelay = 50;
                    if (this.isFightingDragon) {
                        if (this.delay == 50) {
                            this.sound = SimpleSoundInstance.forMusic(QuarkSounds.MUSIC_ENDERMOSH);
                            mc.getSoundManager().playDelayed(this.sound, 0);
                            mc.gui.setNowPlaying(this.endermosh.m_43050_());
                        }
                        double x = mc.player.m_20185_();
                        double z = mc.player.m_20189_();
                        if (mc.screen == null && x * x + z * z < 3000.0) {
                            this.delay++;
                        }
                    } else if (wasFightingDragon && this.sound != null) {
                        mc.getSoundManager().stop(this.sound);
                        this.delay = 0;
                        this.sound = null;
                    }
                }
            }
        }
    }
}