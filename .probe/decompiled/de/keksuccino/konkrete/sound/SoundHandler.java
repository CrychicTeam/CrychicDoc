package de.keksuccino.konkrete.sound;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.FloatControl.Type;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SoundHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static Map<String, Clip> sounds = new HashMap();

    private static boolean init = false;

    private static volatile boolean volumeHandling = true;

    private static List<String> unsupportedFormatAudios = new ArrayList();

    public static void init() {
        if (FMLEnvironment.dist != Dist.CLIENT) {
            LOGGER.error("[KONKRETE] Tried to initialize SoundHandler server-side!");
            new Throwable().printStackTrace();
        } else {
            if (!init) {
                new Thread(() -> {
                    float lastMaster = 0.0F;
                    while (volumeHandling) {
                        try {
                            float currentMaster = Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER);
                            if (lastMaster != currentMaster) {
                                updateVolume();
                            }
                            lastMaster = currentMaster;
                            Thread.sleep(100L);
                        } catch (Exception var2) {
                            var2.printStackTrace();
                        }
                    }
                }).start();
                init = true;
            }
        }
    }

    public static void registerSound(String key, String path) {
        if (!unsupportedFormatAudios.contains(key)) {
            if (FMLEnvironment.dist != Dist.CLIENT) {
                LOGGER.error("[KONKRETE] Tried to register sound server-side!");
                new Throwable().printStackTrace();
            } else {
                if (!sounds.containsKey(key)) {
                    AudioInputStream in = null;
                    try {
                        Clip c = AudioSystem.getClip();
                        in = AudioSystem.getAudioInputStream(new File(path));
                        c.open(in);
                        in.close();
                        sounds.put(key, c);
                        setVolume(key, getMinecraftMasterVolume());
                    } catch (IllegalArgumentException var7) {
                        LOGGER.error("[KONKRETE] Unable to register sound! Format not supported or invalid!");
                        printErrorLog(var7, key, 0, "registering audio");
                        unsupportedFormatAudios.add(key);
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception var6) {
                                var6.printStackTrace();
                            }
                        }
                        sounds.remove(key);
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        if (in != null) {
                            try {
                                in.close();
                            } catch (Exception var5) {
                                var5.printStackTrace();
                            }
                        }
                        sounds.remove(key);
                    }
                }
            }
        }
    }

    public static void unregisterSound(String key) {
        if (sounds.containsKey(key)) {
            ((Clip) sounds.get(key)).stop();
            ((Clip) sounds.get(key)).close();
            sounds.remove(key);
        }
    }

    public static void playSound(String key) {
        if (sounds.containsKey(key)) {
            ((Clip) sounds.get(key)).start();
        }
    }

    public static void stopSound(String key) {
        if (sounds.containsKey(key)) {
            ((Clip) sounds.get(key)).stop();
        }
    }

    public static void resetSound(String key) {
        if (sounds.containsKey(key)) {
            ((Clip) sounds.get(key)).setMicrosecondPosition(0L);
        }
    }

    public static boolean soundExists(String key) {
        return sounds.containsKey(key);
    }

    public static void setLooped(String key, boolean looped) {
        if (sounds.containsKey(key)) {
            Clip c = (Clip) sounds.get(key);
            if (looped) {
                c.setLoopPoints(0, -1);
                c.loop(-1);
            } else {
                c.loop(0);
            }
        }
    }

    public static boolean isPlaying(String key) {
        return sounds.containsKey(key) && ((Clip) sounds.get(key)).isRunning();
    }

    public static void updateVolume() {
        for (String s : sounds.keySet()) {
            setVolume(s, getMinecraftMasterVolume());
        }
    }

    private static void setVolume(String key, int percentage) {
        if (volumeHandling) {
            try {
                if (sounds.containsKey(key)) {
                    Clip clip = (Clip) sounds.get(key);
                    if (clip.isOpen()) {
                        if (clip.isControlSupported(Type.MASTER_GAIN)) {
                            FloatControl f = (FloatControl) ((Clip) sounds.get(key)).getControl(Type.MASTER_GAIN);
                            int gain = (int) ((float) ((int) f.getMinimum()) + (f.getMaximum() - f.getMinimum()) / 100.0F * (float) percentage);
                            f.setValue((float) gain);
                        } else {
                            volumeHandling = false;
                            LOGGER.error("[KONKRETE] Unable to set sound volume! MASTER_GAIN control not supported! Disabling volume handling!");
                            printErrorLog(new Throwable(), key, percentage, "setting volume");
                        }
                    } else {
                        LOGGER.error("[KONKRETE] Unable to set sound volume! Clip not open: " + key);
                    }
                }
            } catch (Exception var5) {
                volumeHandling = false;
                LOGGER.error("[KONKRETE] Unable to set sound volume! Disabling volume handling!");
                printErrorLog(var5, key, percentage, "setting volume");
            }
        }
    }

    private static void printErrorLog(Throwable throwable, String audioKey, int audioVolume, String action) {
        CrashReport c = CrashReport.forThrowable(throwable, "Exception in Konkrete while " + action);
        CrashReportCategory cat = c.addCategory("Audio Information");
        cat.setDetail("Key", audioKey);
        cat.setDetail("Volume", audioVolume);
        LOGGER.error(c.getFriendlyReport());
    }

    private static int getMinecraftMasterVolume() {
        return (int) (Minecraft.getInstance().options.getSoundSourceVolume(SoundSource.MASTER) * 100.0F);
    }
}