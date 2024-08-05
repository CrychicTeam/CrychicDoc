package com.github.alexthe666.citadel.repack.jcodec.containers.flv;

import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class FLVMetadata {

    private double duration;

    private double width;

    private double height;

    private double framerate;

    private String audiocodecid;

    private double videokeyframe_frequency;

    private String videodevice;

    private double avclevel;

    private double audiosamplerate;

    private double audiochannels;

    private String presetname;

    private double videodatarate;

    private double audioinputvolume;

    private Date creationdate;

    private String videocodecid;

    private double avcprofile;

    private String audiodevice;

    private double audiodatarate;

    public FLVMetadata(Map<String, Object> md) {
        Field[] declaredFields = Platform.getDeclaredFields(this.getClass());
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            Object object = md.get(field.getName());
            try {
                if (object instanceof Double) {
                    field.setDouble(this, (Double) object);
                } else if (object instanceof Boolean) {
                    field.setBoolean(this, (Boolean) object);
                } else {
                    field.set(this, object);
                }
            } catch (Exception var7) {
            }
        }
    }

    public double getDuration() {
        return this.duration;
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public double getFramerate() {
        return this.framerate;
    }

    public String getAudiocodecid() {
        return this.audiocodecid;
    }

    public double getVideokeyframe_frequency() {
        return this.videokeyframe_frequency;
    }

    public String getVideodevice() {
        return this.videodevice;
    }

    public double getAvclevel() {
        return this.avclevel;
    }

    public double getAudiosamplerate() {
        return this.audiosamplerate;
    }

    public double getAudiochannels() {
        return this.audiochannels;
    }

    public String getPresetname() {
        return this.presetname;
    }

    public double getVideodatarate() {
        return this.videodatarate;
    }

    public double getAudioinputvolume() {
        return this.audioinputvolume;
    }

    public Date getCreationdate() {
        return this.creationdate;
    }

    public String getVideocodecid() {
        return this.videocodecid;
    }

    public double getAvcprofile() {
        return this.avcprofile;
    }

    public String getAudiodevice() {
        return this.audiodevice;
    }

    public double getAudiodatarate() {
        return this.audiodatarate;
    }
}