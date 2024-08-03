package com.mojang.blaze3d.audio;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.OptionalLong;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.util.Mth;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALC11;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALUtil;
import org.lwjgl.openal.SOFTHRTF;
import org.lwjgl.system.MemoryStack;
import org.slf4j.Logger;

public class Library {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final int NO_DEVICE = 0;

    private static final int DEFAULT_CHANNEL_COUNT = 30;

    private long currentDevice;

    private long context;

    private boolean supportsDisconnections;

    @Nullable
    private String defaultDeviceName;

    private static final Library.ChannelPool EMPTY = new Library.ChannelPool() {

        @Nullable
        @Override
        public Channel acquire() {
            return null;
        }

        @Override
        public boolean release(Channel p_83708_) {
            return false;
        }

        @Override
        public void cleanup() {
        }

        @Override
        public int getMaxCount() {
            return 0;
        }

        @Override
        public int getUsedCount() {
            return 0;
        }
    };

    private Library.ChannelPool staticChannels = EMPTY;

    private Library.ChannelPool streamingChannels = EMPTY;

    private final Listener listener = new Listener();

    public Library() {
        this.defaultDeviceName = getDefaultDeviceName();
    }

    public void init(@Nullable String string0, boolean boolean1) {
        this.currentDevice = openDeviceOrFallback(string0);
        this.supportsDisconnections = ALC10.alcIsExtensionPresent(this.currentDevice, "ALC_EXT_disconnect");
        ALCCapabilities $$2 = ALC.createCapabilities(this.currentDevice);
        if (OpenAlUtil.checkALCError(this.currentDevice, "Get capabilities")) {
            throw new IllegalStateException("Failed to get OpenAL capabilities");
        } else if (!$$2.OpenALC11) {
            throw new IllegalStateException("OpenAL 1.1 not supported");
        } else {
            this.setHrtf($$2.ALC_SOFT_HRTF && boolean1);
            this.context = ALC10.alcCreateContext(this.currentDevice, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(this.context);
            int $$3 = this.getChannelCount();
            int $$4 = Mth.clamp((int) Mth.sqrt((float) $$3), 2, 8);
            int $$5 = Mth.clamp($$3 - $$4, 8, 255);
            this.staticChannels = new Library.CountingChannelPool($$5);
            this.streamingChannels = new Library.CountingChannelPool($$4);
            ALCapabilities $$6 = AL.createCapabilities($$2);
            OpenAlUtil.checkALError("Initialization");
            if (!$$6.AL_EXT_source_distance_model) {
                throw new IllegalStateException("AL_EXT_source_distance_model is not supported");
            } else {
                AL10.alEnable(512);
                if (!$$6.AL_EXT_LINEAR_DISTANCE) {
                    throw new IllegalStateException("AL_EXT_LINEAR_DISTANCE is not supported");
                } else {
                    OpenAlUtil.checkALError("Enable per-source distance models");
                    LOGGER.info("OpenAL initialized on device {}", this.getCurrentDeviceName());
                }
            }
        }
    }

    private void setHrtf(boolean boolean0) {
        int $$1 = ALC10.alcGetInteger(this.currentDevice, 6548);
        if ($$1 > 0) {
            MemoryStack $$2 = MemoryStack.stackPush();
            try {
                IntBuffer $$3 = $$2.callocInt(10).put(6546).put(boolean0 ? 1 : 0).put(6550).put(0).put(0).flip();
                if (!SOFTHRTF.alcResetDeviceSOFT(this.currentDevice, $$3)) {
                    LOGGER.warn("Failed to reset device: {}", ALC10.alcGetString(this.currentDevice, ALC10.alcGetError(this.currentDevice)));
                }
            } catch (Throwable var7) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }
                throw var7;
            }
            if ($$2 != null) {
                $$2.close();
            }
        }
    }

    private int getChannelCount() {
        MemoryStack $$0 = MemoryStack.stackPush();
        int var7;
        label58: {
            try {
                int $$1 = ALC10.alcGetInteger(this.currentDevice, 4098);
                if (OpenAlUtil.checkALCError(this.currentDevice, "Get attributes size")) {
                    throw new IllegalStateException("Failed to get OpenAL attributes");
                }
                IntBuffer $$2 = $$0.mallocInt($$1);
                ALC10.alcGetIntegerv(this.currentDevice, 4099, $$2);
                if (OpenAlUtil.checkALCError(this.currentDevice, "Get attributes")) {
                    throw new IllegalStateException("Failed to get OpenAL attributes");
                }
                int $$3 = 0;
                while ($$3 < $$1) {
                    int $$4 = $$2.get($$3++);
                    if ($$4 == 0) {
                        break;
                    }
                    int $$5 = $$2.get($$3++);
                    if ($$4 == 4112) {
                        var7 = $$5;
                        break label58;
                    }
                }
            } catch (Throwable var9) {
                if ($$0 != null) {
                    try {
                        $$0.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if ($$0 != null) {
                $$0.close();
            }
            return 30;
        }
        if ($$0 != null) {
            $$0.close();
        }
        return var7;
    }

    @Nullable
    public static String getDefaultDeviceName() {
        if (!ALC10.alcIsExtensionPresent(0L, "ALC_ENUMERATE_ALL_EXT")) {
            return null;
        } else {
            ALUtil.getStringList(0L, 4115);
            return ALC10.alcGetString(0L, 4114);
        }
    }

    public String getCurrentDeviceName() {
        String $$0 = ALC10.alcGetString(this.currentDevice, 4115);
        if ($$0 == null) {
            $$0 = ALC10.alcGetString(this.currentDevice, 4101);
        }
        if ($$0 == null) {
            $$0 = "Unknown";
        }
        return $$0;
    }

    public synchronized boolean hasDefaultDeviceChanged() {
        String $$0 = getDefaultDeviceName();
        if (Objects.equals(this.defaultDeviceName, $$0)) {
            return false;
        } else {
            this.defaultDeviceName = $$0;
            return true;
        }
    }

    private static long openDeviceOrFallback(@Nullable String string0) {
        OptionalLong $$1 = OptionalLong.empty();
        if (string0 != null) {
            $$1 = tryOpenDevice(string0);
        }
        if ($$1.isEmpty()) {
            $$1 = tryOpenDevice(getDefaultDeviceName());
        }
        if ($$1.isEmpty()) {
            $$1 = tryOpenDevice(null);
        }
        if ($$1.isEmpty()) {
            throw new IllegalStateException("Failed to open OpenAL device");
        } else {
            return $$1.getAsLong();
        }
    }

    private static OptionalLong tryOpenDevice(@Nullable String string0) {
        long $$1 = ALC10.alcOpenDevice(string0);
        return $$1 != 0L && !OpenAlUtil.checkALCError($$1, "Open device") ? OptionalLong.of($$1) : OptionalLong.empty();
    }

    public void cleanup() {
        this.staticChannels.cleanup();
        this.streamingChannels.cleanup();
        ALC10.alcDestroyContext(this.context);
        if (this.currentDevice != 0L) {
            ALC10.alcCloseDevice(this.currentDevice);
        }
    }

    public Listener getListener() {
        return this.listener;
    }

    @Nullable
    public Channel acquireChannel(Library.Pool libraryPool0) {
        return (libraryPool0 == Library.Pool.STREAMING ? this.streamingChannels : this.staticChannels).acquire();
    }

    public void releaseChannel(Channel channel0) {
        if (!this.staticChannels.release(channel0) && !this.streamingChannels.release(channel0)) {
            throw new IllegalStateException("Tried to release unknown channel");
        }
    }

    public String getDebugString() {
        return String.format(Locale.ROOT, "Sounds: %d/%d + %d/%d", this.staticChannels.getUsedCount(), this.staticChannels.getMaxCount(), this.streamingChannels.getUsedCount(), this.streamingChannels.getMaxCount());
    }

    public List<String> getAvailableSoundDevices() {
        List<String> $$0 = ALUtil.getStringList(0L, 4115);
        return $$0 == null ? Collections.emptyList() : $$0;
    }

    public boolean isCurrentDeviceDisconnected() {
        return this.supportsDisconnections && ALC11.alcGetInteger(this.currentDevice, 787) == 0;
    }

    interface ChannelPool {

        @Nullable
        Channel acquire();

        boolean release(Channel var1);

        void cleanup();

        int getMaxCount();

        int getUsedCount();
    }

    static class CountingChannelPool implements Library.ChannelPool {

        private final int limit;

        private final Set<Channel> activeChannels = Sets.newIdentityHashSet();

        public CountingChannelPool(int int0) {
            this.limit = int0;
        }

        @Nullable
        @Override
        public Channel acquire() {
            if (this.activeChannels.size() >= this.limit) {
                if (SharedConstants.IS_RUNNING_IN_IDE) {
                    Library.LOGGER.warn("Maximum sound pool size {} reached", this.limit);
                }
                return null;
            } else {
                Channel $$0 = Channel.create();
                if ($$0 != null) {
                    this.activeChannels.add($$0);
                }
                return $$0;
            }
        }

        @Override
        public boolean release(Channel channel0) {
            if (!this.activeChannels.remove(channel0)) {
                return false;
            } else {
                channel0.destroy();
                return true;
            }
        }

        @Override
        public void cleanup() {
            this.activeChannels.forEach(Channel::m_83665_);
            this.activeChannels.clear();
        }

        @Override
        public int getMaxCount() {
            return this.limit;
        }

        @Override
        public int getUsedCount() {
            return this.activeChannels.size();
        }
    }

    public static enum Pool {

        STATIC, STREAMING
    }
}