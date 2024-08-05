package icyllis.modernui.mc;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.audio.FFT;
import icyllis.modernui.audio.Track;
import icyllis.modernui.audio.VorbisPullDecoder;
import icyllis.modernui.core.Core;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class MusicPlayer {

    private static volatile MusicPlayer sInstance;

    private Track mCurrentTrack;

    private FFT mFFT;

    private float mGain = 1.0F;

    private String mName;

    private Consumer<Track> mOnTrackLoadCallback;

    public static MusicPlayer getInstance() {
        if (sInstance != null) {
            return sInstance;
        } else {
            synchronized (MusicPlayer.class) {
                if (sInstance == null) {
                    sInstance = new MusicPlayer();
                }
            }
            return sInstance;
        }
    }

    private MusicPlayer() {
    }

    public static String openDialogGet() {
        MemoryStack stack = MemoryStack.stackPush();
        String var2;
        try {
            PointerBuffer filters = stack.mallocPointer(1);
            stack.nUTF8("*.ogg", true);
            filters.put(stack.getPointerAddress());
            filters.rewind();
            var2 = TinyFileDialogs.tinyfd_openFileDialog(null, null, filters, "Ogg Vorbis (*.ogg)", false);
        } catch (Throwable var4) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }
            }
            throw var4;
        }
        if (stack != null) {
            stack.close();
        }
        return var2;
    }

    public void clearTrack() {
        if (this.mCurrentTrack != null) {
            this.mCurrentTrack.close();
            this.mCurrentTrack = null;
        }
        this.mName = null;
    }

    public void replaceTrack(Path path) {
        this.clearTrack();
        CompletableFuture.supplyAsync(() -> {
            try {
                FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
                Track var4;
                try {
                    ByteBuffer nativeEncodedData = Core.readIntoNativeBuffer(channel).flip();
                    VorbisPullDecoder decoder = new VorbisPullDecoder(nativeEncodedData);
                    var4 = new Track(decoder);
                } catch (Throwable var6) {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }
                    throw var6;
                }
                if (channel != null) {
                    channel.close();
                }
                return var4;
            } catch (IOException var7) {
                ModernUI.LOGGER.error("Failed to open Ogg Vorbis, {}", path, var7);
                return null;
            }
        }).whenCompleteAsync((track, ex) -> {
            this.mCurrentTrack = track;
            if (track != null) {
                track.setGain(this.mGain);
                this.mName = path.getFileName().toString();
            }
            if (this.mOnTrackLoadCallback != null) {
                this.mOnTrackLoadCallback.accept(track);
            }
        }, Core.getUiThreadExecutor());
    }

    public void setOnTrackLoadCallback(Consumer<Track> onTrackLoadCallback) {
        this.mOnTrackLoadCallback = onTrackLoadCallback;
    }

    @Nullable
    public String getTrackName() {
        return this.mName;
    }

    public boolean hasTrack() {
        return this.mCurrentTrack != null;
    }

    public float getTrackTime() {
        return this.mCurrentTrack != null ? this.mCurrentTrack.getTime() : 0.0F;
    }

    public float getTrackLength() {
        return this.mCurrentTrack != null ? this.mCurrentTrack.getLength() : 0.0F;
    }

    public void play() {
        if (this.mCurrentTrack != null) {
            this.mCurrentTrack.play();
        }
    }

    public void pause() {
        if (this.mCurrentTrack != null) {
            this.mCurrentTrack.pause();
        }
    }

    public boolean isPlaying() {
        return this.mCurrentTrack != null ? this.mCurrentTrack.isPlaying() : false;
    }

    public boolean seek(float fraction) {
        return this.mCurrentTrack != null ? this.mCurrentTrack.seekToSeconds(fraction * this.mCurrentTrack.getLength()) : true;
    }

    public void setGain(float gain) {
        if (this.mGain != gain) {
            this.mGain = gain;
            if (this.mCurrentTrack != null) {
                this.mCurrentTrack.setGain(gain);
            }
        }
    }

    public float getGain() {
        return this.mGain;
    }

    public void setAnalyzerCallback(Consumer<FFT> setup, Consumer<FFT> callback) {
        if (this.mCurrentTrack != null) {
            if (setup == null && callback == null) {
                this.mCurrentTrack.setAnalyzer(null, null);
            } else {
                if (this.mFFT == null || this.mFFT.getSampleRate() != this.mCurrentTrack.getSampleRate()) {
                    this.mFFT = FFT.create(1024, this.mCurrentTrack.getSampleRate());
                }
                if (setup != null) {
                    setup.accept(this.mFFT);
                }
                this.mCurrentTrack.setAnalyzer(this.mFFT, callback);
            }
        }
    }
}