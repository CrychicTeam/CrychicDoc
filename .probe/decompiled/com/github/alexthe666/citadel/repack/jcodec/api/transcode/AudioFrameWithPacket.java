package com.github.alexthe666.citadel.repack.jcodec.api.transcode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.AudioBuffer;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Packet;

public class AudioFrameWithPacket {

    private AudioBuffer audio;

    private Packet packet;

    public AudioFrameWithPacket(AudioBuffer audio, Packet packet) {
        this.audio = audio;
        this.packet = packet;
    }

    public AudioBuffer getAudio() {
        return this.audio;
    }

    public Packet getPacket() {
        return this.packet;
    }
}