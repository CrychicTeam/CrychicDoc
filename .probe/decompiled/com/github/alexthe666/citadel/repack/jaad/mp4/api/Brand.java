package com.github.alexthe666.citadel.repack.jaad.mp4.api;

public enum Brand {

    UNKNOWN_BRAND("\u0000\u0000\u0000\u0000", "unknown brand"),
    ISO_BASE_MEDIA("isom", "ISO base media file format v1"),
    ISO_BASE_MEDIA_2("iso2", "ISO base media file format v2"),
    ISO_BASE_MEDIA_3("iso3", "ISO base media file format v3"),
    ISO_BASE_MEDIA_4("iso4", "ISO base media file format v4"),
    MP4("mp41", "MP4 v1"),
    MP4_V2("mp42", "MP4 v2"),
    MP4_VIDEO("M4V ", "MPEG video"),
    MP4_AUDIO("M4A ", "MPEG audio"),
    MP4_PROTECTED_AUDIO("M4P ", "MPEG protected audio"),
    MP4_AUDIO_BOOK("M4B ", "MPEG audio book"),
    MP4_WITH_MPEG_7("mp71", "MPEG-7 Metadata"),
    AVC("avc1", "MPEG-4 AVC file format"),
    JPEG_2000("JP2 ", "JPEG 2000 Image"),
    JPEG_2000_COMPOUND_IMAGE("jpm ", "JPEG 2000 Compound Image"),
    JPEG_2000_EXTENDED("jpx ", "JPEG 2000 with extensions"),
    MOTION_JPEG_2000_SIMPLE_PROFILE("mj2s", "Motion JPEG 2000 Simple Profile"),
    MOTION_JPEG_2000_GENERAL_PROFILE("mjp2", "Motion JPEG 2000 General Profile"),
    DMB_FULL("dmb1", "DMB MAF supporting all the components defined in the specification"),
    DMB_MPEG_2("da0a", "DMB MAF with MPEG Layer II audio, MOT slides, DLS, JPG/PNG/MNG images"),
    DMB_MPEG_2_EXTENDED("da0b", "DMB MAF extending da0a with 3GPP timed text, DID, TVA, REL, IPMP"),
    DMB_BSAC("da1a", "DMB MAF with ER-BSAC audio, JPG/PNG/MNG images"),
    DMB_BSAC_EXTENDED("da1b", "DMB MAF extending da1a with 3GPP timed text, DID, TVA, REL, IPMP"),
    DMB_HE_AAC_V2("da2a", "DMB MAF with HE-AAC v2 audio, MOT slides, DLS, JPG/PNG/MNG images"),
    DMB_HE_AAC_V2_EXTENDED("da2b", "DMB MAF extending da2a with 3GPP timed text, DID, TVA, REL, IPMP"),
    DMB_HE_AAC("da3a", "DMB MAF with HE-AAC audio, JPG/PNG/MNG images"),
    DMB_HE_AAC_EXTENDED("da3b", "DMB MAF extending da3a with BIFS, 3GPP timed text, DID, TVA, REL, IPMP"),
    DMB_AVC_BSAC("dv1a", "DMB MAF with AVC video, ER-BSAC audio, BIFS, JPG/PNG/MNG images, TS"),
    DMB_MAF_AVC_BSAC_EXTENDED("dv1b", "DMB MAF extending dv1a with 3GPP timed text, DID, TVA, REL, IPMP"),
    DMB_MAF_AVC_HE_AAC_V2("dv2a", "DMB MAF with AVC video, HE-AAC v2 audio, BIFS, JPG/PNG/MNG images, TS"),
    DMB_MAF_AVC_HE_AAC_V2_EXTENDED("dv2b", "DMB MAF extending dv2a with 3GPP timed text, DID, TVA, REL, IPMP"),
    DMB_MAF_AVC_HE_AAC("dv3a", "DMB MAF with AVC video, HE-AAC audio, BIFS, JPG/PNG/MNG images, TS"),
    DMB_MAF_AVC_HE_AAC_EXTENDED("dv3b", "DMB MAF extending dv3a with 3GPP timed text, DID, TVA, REL, IPMP"),
    MPEG_21("mp21", "MPEG-21"),
    MPPI_PHOTO_PLAYER("MPPI", "MPPI Photo Player"),
    JPSEARCH("jpsi", "JPSearch data interchange format"),
    THREE_GPP_RELEASE_1("3gp1", "3GPP Release 1"),
    THREE_GPP_RELEASE_2("3gp2", "3GPP Release 2"),
    THREE_GPP_RELEASE_3("3gp3", "3GPP Release 3"),
    THREE_GPP_RELEASE_4("3gp4", "3GPP Release 4"),
    THREE_GPP_RELEASE_5("3gp5", "3GPP Release 5"),
    THREE_GPP_RELEASE_6("3gp6", "3GPP Release 6 Basic Profile"),
    THREE_GPP_RELEASE_6_GENERAL("3gg6", "3GPP Release 6 General Profile"),
    THREE_GPP_RELEASE_6_EXTENDED("3ge6", "3GPP Release 6 Extended Presentations Profile"),
    THREE_GPP_RELEASE_6_PROGRESSIVE_DOWNLOAD("3gr6", "3GPP Release 6 Progressive-Download Profile"),
    THREE_GPP_RELEASE_6_STREAMING("3gs6", "3GPP Release 6 Streaming Servers Profile"),
    THREE_GPP_RELEASE_7("3gp7", "3GPP Release 7"),
    THREE_GPP_RELEASE_7_EXTENDED("3ge7", "3GPP Release 7 Extended Presentations Profile"),
    THREE_GPP_RELEASE_7_STREAMING("3gs7", "3GPP Release 7 Streaming Servers Profile"),
    THREE_GPP_RELEASE_8("3gp7", "3GPP Release 8"),
    THREE_GPP_RELEASE_8_RECORDING("3gt8", "3GPP Release 8 Media Stream Recording Profile"),
    THREE_GPP_RELEASE_9("3gs9", "3GPP Release 9 Streaming Servers Profile"),
    THREE_GPP_RELEASE_9_PROGRESSIVE_DOWNLOAD("3gr9", "3GPP Release 9 Progressive-Download Profile"),
    THREE_GPP_RELEASE_9_EXTENDED("3ge9", "3GPP Release 9 Extended Presentations Profile"),
    THREE_GPP_RELEASE_9_RECORDING("3gt9", "3GPP Release 9 Media Stream Recording Profile"),
    THREE_GPP_RELEASE_9_FILE_DELIVERY("3gf9", "3GPP Release 9 File Delivery Server Profile"),
    THREE_GPP_RELEASE_9_ADAPTIVE_STREAMING("3gh9", "3GPP Release 9 Adaptive-Streaming Profile"),
    THREE_GPP_RELEASE_9_MEDIA_SEGMENT("3gm9", "3GPP Release 9 Media Segment Profile"),
    THREE_GPP2_A("3g2a", "3GPP2 compliant with 3GPP2 C.S0050-0 V1.0"),
    THREE_GPP2_B("3g2b", "3GPP2 compliant with 3GPP2 C.S0050-A V1.0.0"),
    THREE_GPP2_C("3g2c", "3GPP2 compliant with 3GPP2 C.S0050-B v1.0"),
    THREE_GPP2_KDDI_3G_EZMOVIE("KDDI", "3GPP2 EZmovie for KDDI 3G cellphones"),
    MPEG_4_MOBILE_PROFILE_("mmp4", "MPEG-4/3GPP Mobile Profile"),
    DIRAC("drc1", "Dirac wavelet compression encapsulated in ISO base media"),
    DIGITAL_MEDIA_PROJECT("dmpf", "Digital Media Project"),
    DVB_OVER_RTP("dvr1", "DVB over RTP"),
    DVB_OVER_MPEG_2_TRANSPORT_STREAM("dvt1", "DVB over MPEG-2 Transport Stream"),
    SD_MEMORY_CARD_VIDEO("sdv ", "SD Memory Card Video"),
    ADOBE_FLASH_PLAYER_VIDEO("F4V ", "Video for Adobe Flash Player 9+"),
    ADOBE_FLASH_PLAYER_PROTECTED_VIDEO("F4P ", "Protected Video for Adobe Flash Player 9+"),
    ADOBE_FLASH_PLAYER_AUDIO("F4A ", "Audio for Adobe Flash Player 9+"),
    ADOBE_FLASH_PLAYER_AUDIO_BOOK("F4B ", "Audio Book for Adobe Flash Player 9+"),
    APPLE_QUICKTIME("qt  ", "Apple Quicktime"),
    APPLE_TV("M4VH", "Apple TV"),
    APPLE_IPHONE_VIDEO("M4VP", "Apple iPhone Video"),
    ARRI_DIGITAL_CAMERA("ARRI", "ARRI Digital Camera"),
    CANON_DIGITAL_CAMERA("CAEP", "Canon Digital Camera"),
    CASIO_DIGITAL_CAMERA("caqv", "Casio Digital Camera"),
    CONVERGENT_DESIGN("CDes", "Convergent Design"),
    DECE_COMMON_FILE_FORMAT("ccff", "DECE common file format"),
    ISMACRYP_2_ENCRYPTED_FILE("isc2", "ISMACryp 2.0 Encrypted File"),
    NIKON_DIGITAL_CAMERA("niko", "Nikon Digital Camera"),
    LEICA_DIGITAL_CAMERA("LCAG", "Leica digital camera"),
    MICROSOFT_PIFF("piff", "Microsoft Protected Interoperable File Format"),
    NERO_DIGITAL_AAC_AUDIO("NDAS", "MP4 v2 with Nero Digital AAC Audio"),
    NERO_STANDARD_PROFILE("NDSS", "MPEG-4 Nero Standard Profile"),
    NERO_CINEMA_PROFILE("NDSC", "MPEG-4 Nero Cinema Profile"),
    NERO_HDTV_PROFILE("NDSH", "MPEG-4 Nero HDTV Profile"),
    NERO_MOBILE_PROFILE("NDSM", "MPEG-4 Nero Mobile Profile"),
    NERO_PORTABLE_PROFILE("NDSP", "MPEG-4 Nero Portable Profile"),
    NERO_AVC_STANDARD_PROFILE("NDXS", "H.264/MPEG-4 AVC Nero Standard Profile"),
    NERO_AVC_CINEMA_PROFILE("NDXC", "H.264/MPEG-4 AVC Nero Cinema Profile"),
    NERO_AVC_HDTV_PROFILE("NDXH", "H.264/MPEG-4 AVC Nero HDTV Profile"),
    NERO_AVC_MOBILE_PROFILE("NDXM", "H.264/MPEG-4 AVC Nero Mobile Profile"),
    NERO_AVC_PORTABLE_PROFILE("NDXP", "H.264/MPEG-4 AVC Portable Profile"),
    OMA_DCF_2("odcf", "Open Mobile Alliance DCF DRM Format 2.0"),
    OMA_PDCF_2_1("opf2", "Open Mobile Alliance PDCF DRM Format 2.1"),
    OMA_PDCF_XBS_EXTENSIONS("opx2", "Open Mobile Alliance PDCF DRM + XBS extensions"),
    PANASONIC_DIGITAL_CAMERA("pana", "Panasonic Digital Camera"),
    ROSS_VIDEO("ROSS", "Ross Video"),
    SAMSUNG_STEREOSCOPIC_SINGLE_STREAM("ssc1", "Samsung stereoscopic, single stream"),
    SAMSUNG_STEREOSCOPIC_DUAL_STREAM("ssc2", "Samsung stereoscopic, dual stream"),
    SONY_MOBILE("mqt ", "Sony Mobile"),
    SONY_PSP("MSNV", "MPEG-4 for SonyPSP");

    private String id;

    private String description;

    public static Brand forID(String id) {
        for (Brand b : values()) {
            if (b.id.equals(id)) {
                return b;
            }
        }
        Brand bx = UNKNOWN_BRAND;
        bx.id = id;
        return bx;
    }

    private Brand(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getID() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }
}