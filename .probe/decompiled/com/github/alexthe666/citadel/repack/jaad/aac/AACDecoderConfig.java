package com.github.alexthe666.citadel.repack.jaad.aac;

import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.IBitStream;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.PCE;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.SyntaxConstants;

public class AACDecoderConfig implements SyntaxConstants {

    private Profile profile = Profile.AAC_MAIN;

    private Profile extProfile = Profile.UNKNOWN;

    private SampleFrequency sampleFrequency = SampleFrequency.SAMPLE_FREQUENCY_NONE;

    private ChannelConfiguration channelConfiguration = ChannelConfiguration.CHANNEL_CONFIG_UNSUPPORTED;

    private boolean frameLengthFlag = false;

    private boolean dependsOnCoreCoder;

    private int coreCoderDelay;

    private boolean extensionFlag;

    private boolean sbrPresent = false;

    private boolean downSampledSBR = false;

    private boolean sbrEnabled = true;

    private boolean sectionDataResilience = false;

    private boolean scalefactorResilience = false;

    private boolean spectralDataResilience = false;

    private AACDecoderConfig() {
    }

    public ChannelConfiguration getChannelConfiguration() {
        return this.channelConfiguration;
    }

    public void setChannelConfiguration(ChannelConfiguration channelConfiguration) {
        this.channelConfiguration = channelConfiguration;
    }

    public int getCoreCoderDelay() {
        return this.coreCoderDelay;
    }

    public void setCoreCoderDelay(int coreCoderDelay) {
        this.coreCoderDelay = coreCoderDelay;
    }

    public boolean isDependsOnCoreCoder() {
        return this.dependsOnCoreCoder;
    }

    public void setDependsOnCoreCoder(boolean dependsOnCoreCoder) {
        this.dependsOnCoreCoder = dependsOnCoreCoder;
    }

    public Profile getExtObjectType() {
        return this.extProfile;
    }

    public void setExtObjectType(Profile extObjectType) {
        this.extProfile = extObjectType;
    }

    public int getFrameLength() {
        return this.frameLengthFlag ? 960 : 1024;
    }

    public boolean isSmallFrameUsed() {
        return this.frameLengthFlag;
    }

    public void setSmallFrameUsed(boolean shortFrame) {
        this.frameLengthFlag = shortFrame;
    }

    public Profile getProfile() {
        return this.profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public SampleFrequency getSampleFrequency() {
        return this.sampleFrequency;
    }

    public void setSampleFrequency(SampleFrequency sampleFrequency) {
        this.sampleFrequency = sampleFrequency;
    }

    public boolean isSBRPresent() {
        return this.sbrPresent;
    }

    public boolean isSBRDownSampled() {
        return this.downSampledSBR;
    }

    public boolean isSBREnabled() {
        return this.sbrEnabled;
    }

    public void setSBREnabled(boolean enabled) {
        this.sbrEnabled = enabled;
    }

    public boolean isScalefactorResilienceUsed() {
        return this.scalefactorResilience;
    }

    public boolean isSectionDataResilienceUsed() {
        return this.sectionDataResilience;
    }

    public boolean isSpectralDataResilienceUsed() {
        return this.spectralDataResilience;
    }

    public static AACDecoderConfig parseMP4DecoderSpecificInfo(byte[] data) throws AACException {
        IBitStream _in = BitStream.createBitStream(data);
        AACDecoderConfig config = new AACDecoderConfig();
        AACDecoderConfig var10;
        try {
            config.profile = readProfile(_in);
            int sf = _in.readBits(4);
            if (sf == 15) {
                config.sampleFrequency = SampleFrequency.forFrequency(_in.readBits(24));
            } else {
                config.sampleFrequency = SampleFrequency.forInt(sf);
            }
            config.channelConfiguration = ChannelConfiguration.forInt(_in.readBits(4));
            Profile cp = config.profile;
            if (Profile.AAC_SBR == cp) {
                config.extProfile = cp;
                config.sbrPresent = true;
                sf = _in.readBits(4);
                config.downSampledSBR = config.sampleFrequency.getIndex() == sf;
                config.sampleFrequency = SampleFrequency.forInt(sf);
                config.profile = readProfile(_in);
            } else {
                if (Profile.AAC_MAIN != cp && Profile.AAC_LC != cp && Profile.AAC_SSR != cp && Profile.AAC_LTP != cp && Profile.ER_AAC_LC != cp && Profile.ER_AAC_LTP != cp && Profile.ER_AAC_LD != cp) {
                    throw new AACException("profile not supported: " + cp.getIndex());
                }
                config.frameLengthFlag = _in.readBool();
                if (config.frameLengthFlag) {
                    throw new AACException("config uses 960-sample frames, not yet supported");
                }
                config.dependsOnCoreCoder = _in.readBool();
                if (config.dependsOnCoreCoder) {
                    config.coreCoderDelay = _in.readBits(14);
                } else {
                    config.coreCoderDelay = 0;
                }
                config.extensionFlag = _in.readBool();
                if (config.extensionFlag) {
                    if (cp.isErrorResilientProfile()) {
                        config.sectionDataResilience = _in.readBool();
                        config.scalefactorResilience = _in.readBool();
                        config.spectralDataResilience = _in.readBool();
                    }
                    _in.skipBit();
                }
                if (config.channelConfiguration == ChannelConfiguration.CHANNEL_CONFIG_NONE) {
                    _in.skipBits(3);
                    PCE pce = new PCE();
                    pce.decode(_in);
                    config.profile = pce.getProfile();
                    config.sampleFrequency = pce.getSampleFrequency();
                    config.channelConfiguration = ChannelConfiguration.forInt(pce.getChannelCount());
                }
                if (_in.getBitsLeft() > 10) {
                    readSyncExtension(_in, config);
                }
            }
            var10 = config;
        } finally {
            _in.destroy();
        }
        return var10;
    }

    private static Profile readProfile(IBitStream _in) throws AACException {
        int i = _in.readBits(5);
        if (i == 31) {
            i = 32 + _in.readBits(6);
        }
        return Profile.forInt(i);
    }

    private static void readSyncExtension(IBitStream _in, AACDecoderConfig config) throws AACException {
        int type = _in.readBits(11);
        switch(type) {
            case 695:
                Profile profile = Profile.forInt(_in.readBits(5));
                if (profile.equals(Profile.AAC_SBR)) {
                    config.sbrPresent = _in.readBool();
                    if (config.sbrPresent) {
                        config.profile = profile;
                        int tmp = _in.readBits(4);
                        if (tmp == config.sampleFrequency.getIndex()) {
                            config.downSampledSBR = true;
                        }
                        if (tmp == 15) {
                            throw new AACException("sample rate specified explicitly, not supported yet!");
                        }
                    }
                }
        }
    }
}