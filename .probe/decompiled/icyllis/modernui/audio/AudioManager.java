package icyllis.modernui.audio;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.MainThread;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;

public class AudioManager implements AutoCloseable {

    public static final Marker MARKER = MarkerManager.getMarker("Audio");

    public static final int TICK_PERIOD = 20;

    private static final AudioManager sInstance = new AudioManager();

    private final ScheduledExecutorService mExecutorService = Executors.newSingleThreadScheduledExecutor(this::createThread);

    private final List<String> mDeviceList = new ArrayList();

    private final CopyOnWriteArrayList<Track> mTracks = new CopyOnWriteArrayList();

    private boolean mInitialized;

    private boolean mIntegrated;

    private int mTimer;

    private AudioManager() {
    }

    @Nonnull
    public static AudioManager getInstance() {
        return sInstance;
    }

    @Nonnull
    private Thread createThread(Runnable target) {
        Thread t = new Thread(target, "Audio-Thread");
        t.setDaemon(true);
        return t;
    }

    @MainThread
    public void initialize() {
        this.initialize(false);
    }

    @MainThread
    public synchronized void initialize(boolean integrated) {
        if (!this.mInitialized) {
            this.mIntegrated = integrated;
            if (!integrated) {
                this.setDevice(null);
                List<String> devices = ALUtil.getStringList(0L, 4115);
                if (devices != null) {
                    this.mDeviceList.addAll(devices);
                }
            }
            this.mExecutorService.scheduleAtFixedRate(this::tick, 0L, 20L, TimeUnit.MILLISECONDS);
            this.mInitialized = true;
        }
    }

    public void setDevice(@Nullable String name) {
        long context = ALC11.alcGetCurrentContext();
        if (context == 0L) {
            long device = ALC11.alcOpenDevice(name);
            if (device == 0L && name != null) {
                device = ALC11.nalcOpenDevice(0L);
            }
            if (device != 0L) {
                context = ALC11.nalcCreateContext(device, 0L);
                ALC11.alcMakeContextCurrent(context);
                if (context != 0L) {
                    ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
                    ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
                    if (!alcCapabilities.OpenALC11 || !alCapabilities.OpenAL11) {
                        ModernUI.LOGGER.fatal(MARKER, "OpenAL 1.1 is not supported");
                    }
                    String devName = ALC11.alcGetString(device, 4101);
                    ModernUI.LOGGER.info(MARKER, "Open audio device {}", devName);
                } else {
                    ModernUI.LOGGER.error(MARKER, "Failed to create audio context");
                }
            } else {
                ModernUI.LOGGER.info(MARKER, "No suitable audio device was found");
            }
        }
    }

    private void tick() {
        int timer = this.mTimer + 1 & 127;
        try {
            if (timer == 0 && !this.mIntegrated) {
                List<String> devices = ALUtil.getStringList(0L, 4115);
                if (!this.mDeviceList.equals(devices)) {
                    this.mDeviceList.clear();
                    if (devices != null) {
                        this.mDeviceList.addAll(devices);
                    }
                    this.destroy();
                    this.setDevice(null);
                    ModernUI.LOGGER.info(MARKER, "Device list changed");
                }
            }
            for (Track track : this.mTracks) {
                track.tick();
            }
        } catch (Throwable var4) {
            ModernUI.LOGGER.error(MARKER, "Caught an exception on audio thread", var4);
        }
        this.mTimer = timer;
    }

    public void destroy() {
        long context = ALC11.alcGetCurrentContext();
        if (context != 0L) {
            long device = ALC11.alcGetContextsDevice(context);
            ALC11.alcMakeContextCurrent(0L);
            ALC11.alcDestroyContext(context);
            if (device != 0L) {
                ALC11.alcCloseDevice(device);
            }
        }
    }

    public void addTrack(@Nonnull Track track) {
        this.mTracks.add(track);
    }

    public void removeTrack(@Nonnull Track track) {
        this.mTracks.remove(track);
    }

    public void close() {
        this.mExecutorService.shutdown();
        for (Track track : new ArrayList(this.mTracks)) {
            try {
                track.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
        if (!this.mIntegrated) {
            this.destroy();
        }
    }
}