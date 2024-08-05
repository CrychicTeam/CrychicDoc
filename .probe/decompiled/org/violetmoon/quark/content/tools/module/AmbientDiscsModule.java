package org.violetmoon.quark.content.tools.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.base.item.QuarkMusicDiscItem;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.living.ZLivingDeath;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "tools")
public class AmbientDiscsModule extends ZetaModule {

    @Config
    public static boolean dropOnSpiderKill = true;

    @Config
    public static double volume = 3.0;

    @Hint(key = "ambience_discs")
    private final List<Item> discs = new ArrayList();

    @LoadEvent
    public void register(ZRegister event) {
        this.disc(QuarkSounds.AMBIENT_DRIPS);
        this.disc(QuarkSounds.AMBIENT_OCEAN);
        this.disc(QuarkSounds.AMBIENT_RAIN);
        this.disc(QuarkSounds.AMBIENT_WIND);
        this.disc(QuarkSounds.AMBIENT_FIRE);
        this.disc(QuarkSounds.AMBIENT_CLOCK);
        this.disc(QuarkSounds.AMBIENT_CRICKETS);
        this.disc(QuarkSounds.AMBIENT_CHATTER);
    }

    private void disc(SoundEvent sound) {
        String name = sound.getLocation().getPath().replaceAll(".+\\.", "");
        this.discs.add(new QuarkMusicDiscItem(15, () -> sound, name, this, Integer.MAX_VALUE));
    }

    @PlayEvent
    public void onMobDeath(ZLivingDeath event) {
        if (dropOnSpiderKill && event.getEntity() instanceof Spider && event.getSource().getEntity() instanceof Skeleton) {
            Item item = (Item) this.discs.get(event.getEntity().level().random.nextInt(this.discs.size()));
            event.getEntity().spawnAtLocation(item, 0);
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends AmbientDiscsModule {

        public static void onJukeboxLoad(JukeboxBlockEntity tile) {
            Minecraft mc = Minecraft.getInstance();
            LevelRenderer render = mc.levelRenderer;
            BlockPos pos = tile.m_58899_();
            SoundInstance sound = (SoundInstance) render.playingRecords.get(pos);
            SoundManager soundEngine = mc.getSoundManager();
            if (sound == null || !soundEngine.isActive(sound)) {
                if (sound != null) {
                    soundEngine.play(sound);
                } else {
                    ItemStack stack = tile.m_272036_();
                    if (stack.getItem() instanceof QuarkMusicDiscItem disc) {
                        playAmbientSound(disc, pos);
                    }
                }
            }
        }

        public static boolean playAmbientSound(QuarkMusicDiscItem disc, BlockPos pos) {
            if (disc.isAmbient) {
                Minecraft mc = Minecraft.getInstance();
                SoundManager soundEngine = mc.getSoundManager();
                LevelRenderer render = mc.levelRenderer;
                SimpleSoundInstance simplesound = new SimpleSoundInstance(((SoundEvent) disc.soundSupplier.get()).getLocation(), SoundSource.RECORDS, (float) AmbientDiscsModule.volume, 1.0F, SoundInstance.createUnseededRandom(), true, 0, SoundInstance.Attenuation.LINEAR, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), false);
                render.playingRecords.put(pos, simplesound);
                soundEngine.play(simplesound);
                if (mc.level != null) {
                    mc.level.addParticle(ParticleTypes.NOTE, (double) pos.m_123341_() + Math.random(), (double) pos.m_123342_() + 1.1, (double) pos.m_123343_() + Math.random(), Math.random(), 0.0, 0.0);
                }
                return true;
            } else {
                return false;
            }
        }
    }
}