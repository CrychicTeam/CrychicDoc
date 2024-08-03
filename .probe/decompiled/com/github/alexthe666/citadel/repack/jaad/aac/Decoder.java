package com.github.alexthe666.citadel.repack.jaad.aac;

import com.github.alexthe666.citadel.repack.jaad.aac.filterbank.FilterBank;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.Constants;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.PCE;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.SyntacticElements;
import com.github.alexthe666.citadel.repack.jaad.aac.transport.ADIFHeader;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

public class Decoder implements Constants {

    private final DecoderConfig config;

    private final SyntacticElements syntacticElements;

    private final FilterBank filterBank;

    private BitStream in;

    private ADIFHeader adifHeader;

    public static boolean canDecode(Profile profile) {
        return profile.isDecodingSupported();
    }

    public Decoder(byte[] decoderSpecificInfo) throws AACException {
        this.config = DecoderConfig.parseMP4DecoderSpecificInfo(decoderSpecificInfo);
        if (this.config == null) {
            throw new IllegalArgumentException("illegal MP4 decoder specific info");
        } else if (!canDecode(this.config.getProfile())) {
            throw new AACException("unsupported profile: " + this.config.getProfile().getDescription());
        } else {
            this.syntacticElements = new SyntacticElements(this.config);
            this.filterBank = new FilterBank(this.config.isSmallFrameUsed(), this.config.getChannelConfiguration().getChannelCount());
            this.in = new BitStream();
            LOGGER.log(Level.FINE, "profile: {0}", this.config.getProfile());
            LOGGER.log(Level.FINE, "sf: {0}", this.config.getSampleFrequency().getFrequency());
            LOGGER.log(Level.FINE, "channels: {0}", this.config.getChannelConfiguration().getDescription());
        }
    }

    public DecoderConfig getConfig() {
        return this.config;
    }

    public void decodeFrame(byte[] frame, SampleBuffer buffer) throws AACException {
        if (frame != null) {
            this.in.setData(frame);
        }
        try {
            this.decode(buffer);
        } catch (AACException var4) {
            if (!var4.isEndOfStream()) {
                throw var4;
            }
            LOGGER.log(Level.WARNING, "unexpected end of frame", var4);
        }
    }

    private void decode(SampleBuffer buffer) throws AACException {
        if (ADIFHeader.isPresent(this.in)) {
            this.adifHeader = ADIFHeader.readHeader(this.in);
            PCE pce = this.adifHeader.getFirstPCE();
            this.config.setProfile(pce.getProfile());
            this.config.setSampleFrequency(pce.getSampleFrequency());
            this.config.setChannelConfiguration(ChannelConfiguration.forInt(pce.getChannelCount()));
        }
        if (!canDecode(this.config.getProfile())) {
            throw new AACException("unsupported profile: " + this.config.getProfile().getDescription());
        } else {
            this.syntacticElements.startNewFrame();
            try {
                this.syntacticElements.decode(this.in);
                this.syntacticElements.process(this.filterBank);
                this.syntacticElements.sendToOutput(buffer);
            } catch (AACException var3) {
                buffer.setData(new byte[0], 0, 0, 0, 0);
                throw var3;
            } catch (Exception var4) {
                buffer.setData(new byte[0], 0, 0, 0, 0);
                throw new AACException(var4);
            }
        }
    }

    static {
        for (Handler h : LOGGER.getHandlers()) {
            LOGGER.removeHandler(h);
        }
        LOGGER.setLevel(Level.WARNING);
        ConsoleHandler h = new ConsoleHandler();
        h.setLevel(Level.ALL);
        LOGGER.addHandler(h);
    }
}