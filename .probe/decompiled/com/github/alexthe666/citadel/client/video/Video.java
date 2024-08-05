package com.github.alexthe666.citadel.client.video;

import com.github.alexthe666.citadel.client.texture.VideoFrameTexture;
import com.github.alexthe666.citadel.repack.jaad.spi.javasound.AACAudioFileReader;
import com.github.alexthe666.citadel.repack.jcodec.api.FrameGrab;
import com.github.alexthe666.citadel.repack.jcodec.common.io.IOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.io.NIOUtils;
import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.scale.ColorUtil;
import com.github.alexthe666.citadel.repack.jcodec.scale.RgbToBgr;
import com.github.alexthe666.citadel.repack.jcodec.scale.Transform;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Video {

    public static final Logger LOGGER = LogManager.getLogger("citadel-video");

    private boolean paused;

    private boolean hasAudioLoaded;

    private boolean repeat;

    private boolean muted;

    private String url;

    private ResourceLocation resourceLocation;

    private VideoFrameTexture texture;

    private FrameGrab frameGrabber = null;

    private FrameGrab prevFrameGrabber = null;

    private File mp4FileOnDisk = null;

    private double framesPerSecond;

    private long startTime = -1L;

    private int lastFrame = -1;

    private long pausedAudioTime = 0L;

    private Clip audioClip;

    public Video(String url, ResourceLocation resourceLocation, VideoFrameTexture texture, double framesPerSecond, boolean muted) {
        this.url = url;
        this.resourceLocation = resourceLocation;
        this.texture = texture;
        this.framesPerSecond = framesPerSecond;
        this.muted = muted;
        this.setupFrameGrabber();
    }

    public void update() {
        if (this.frameGrabber != null) {
            if (this.prevFrameGrabber == null) {
                this.onStart();
            }
            long milliseconds = System.currentTimeMillis() - this.startTime;
            int frame = (int) ((double) milliseconds / 1000.0 * this.framesPerSecond);
            this.pausedAudioTime = milliseconds * 1000L;
            if (this.lastFrame == frame || this.paused) {
                return;
            }
            this.lastFrame = frame;
            try {
                Picture picture = this.frameGrabber.getNativeFrame();
                if (picture != null) {
                    BufferedImage bufferedImage = toBufferedImage(picture);
                    this.texture.setPixelsFromBufferedImage(bufferedImage);
                } else if (this.repeat) {
                    this.frameGrabber.seekToFramePrecise(0);
                    if (this.audioClip != null && !this.muted) {
                        this.audioClip.loop(-1);
                        this.audioClip.setFramePosition(0);
                    }
                    this.startTime = System.currentTimeMillis();
                }
            } catch (Exception var6) {
                var6.printStackTrace();
            }
        }
        this.prevFrameGrabber = this.frameGrabber;
    }

    public void onStart() {
        this.startTime = System.currentTimeMillis();
    }

    private void setupFrameGrabber() {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        executorService.submit(() -> {
            try {
                InputStream in = new URL(this.url).openStream();
                Path path = Paths.get(getVideoCacheFolder().toString(), this.resourceLocation.getPath());
                Files.copy(in, path, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
                in.close();
                this.mp4FileOnDisk = path.toFile();
                this.frameGrabber = FrameGrab.createFrameGrab(NIOUtils.readableChannel(this.mp4FileOnDisk));
                LOGGER.info("loaded mp4 video from " + this.url);
                if (!this.muted) {
                    this.setupAudio(this.mp4FileOnDisk, 0L);
                }
            } catch (Exception var3) {
                var3.printStackTrace();
            }
        });
    }

    private void setupAudio(File mp4File, long time) {
        AACAudioFileReader aacAudioFileReader = new AACAudioFileReader();
        try {
            AudioInputStream audioInputStream = aacAudioFileReader.getAudioInputStream(mp4File);
            this.audioClip = AudioSystem.getClip();
            this.audioClip.open(audioInputStream);
            this.audioClip.setMicrosecondPosition(time);
            this.audioClip.start();
            if (!this.hasAudioLoaded) {
                LOGGER.info("loaded mp4 audio from " + this.url);
            }
            this.hasAudioLoaded = true;
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if (this.audioClip != null && this.hasAudioLoaded) {
            if (!paused && !this.muted) {
                if (!this.audioClip.isOpen()) {
                    this.setupAudio(this.mp4FileOnDisk, this.pausedAudioTime);
                }
            } else if (this.audioClip.isOpen()) {
                this.audioClip.close();
            }
        }
    }

    public boolean isRepeat() {
        return this.repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public boolean isMuted() {
        return this.muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public double getFramesPerSecond() {
        return this.framesPerSecond;
    }

    public void setFramesPerSecond(double framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
    }

    public ResourceLocation getResourceLocation() {
        return this.resourceLocation;
    }

    public File getMp4FileOnDisk() {
        return this.mp4FileOnDisk;
    }

    public int getLastFrame() {
        return this.lastFrame;
    }

    private static Path getVideoCacheFolder() {
        Path configPath = FMLPaths.GAMEDIR.get();
        Path jsonPath = Paths.get(configPath.toAbsolutePath().toString(), "citadel/video_cache");
        if (!Files.exists(jsonPath, new LinkOption[0])) {
            try {
                IOUtils.forceMkdir(jsonPath.toFile());
            } catch (Exception var3) {
            }
        }
        return jsonPath;
    }

    private static BufferedImage toBufferedImage(Picture src) {
        if (src.getColor() != ColorSpace.BGR) {
            Picture bgr = Picture.createCropped(src.getWidth(), src.getHeight(), ColorSpace.BGR, src.getCrop());
            if (src.getColor() == ColorSpace.RGB) {
                new RgbToBgr().transform(src, bgr);
            } else {
                Transform transform = ColorUtil.getTransform(src.getColor(), ColorSpace.RGB);
                transform.transform(src, bgr);
                new RgbToBgr().transform(bgr, bgr);
            }
            src = bgr;
        }
        BufferedImage dst = new BufferedImage(src.getCroppedWidth(), src.getCroppedHeight(), 5);
        if (src.getCrop() == null) {
            toBufferedImage2(src, dst);
        } else {
            toBufferedImageCropped(src, dst);
        }
        return dst;
    }

    private static void toBufferedImageCropped(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        int dstStride = dst.getWidth() * 3;
        int srcStride = src.getWidth() * 3;
        int line = 0;
        int srcOff = 0;
        for (int dstOff = 0; line < dst.getHeight(); line++) {
            int id = dstOff;
            for (int is = srcOff; id < dstOff + dstStride; is += 3) {
                data[id] = (byte) (srcData[is] + 128);
                data[id + 1] = (byte) (srcData[is + 1] + 128);
                data[id + 2] = (byte) (srcData[is + 2] + 128);
                id += 3;
            }
            srcOff += srcStride;
            dstOff += dstStride;
        }
    }

    private static void toBufferedImage2(Picture src, BufferedImage dst) {
        byte[] data = ((DataBufferByte) dst.getRaster().getDataBuffer()).getData();
        byte[] srcData = src.getPlaneData(0);
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) (srcData[i] + 128);
        }
    }
}