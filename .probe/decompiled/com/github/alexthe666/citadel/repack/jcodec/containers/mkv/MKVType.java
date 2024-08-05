package com.github.alexthe666.citadel.repack.jcodec.containers.mkv;

import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBase;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlBin;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlDate;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlFloat;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlMaster;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlSint;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlString;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlUint;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.EbmlVoid;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.MkvBlock;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.boxes.MkvSegment;
import com.github.alexthe666.citadel.repack.jcodec.containers.mkv.util.EbmlUtil;
import com.github.alexthe666.citadel.repack.jcodec.platform.Platform;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

public final class MKVType {

    private static final List<MKVType> _values = new ArrayList();

    public static final MKVType Void = new MKVType("Void", new byte[] { -20 }, EbmlVoid.class);

    public static final MKVType CRC32 = new MKVType("CRC32", new byte[] { -65 }, EbmlBin.class);

    public static final MKVType EBML = new MKVType("EBML", new byte[] { 26, 69, -33, -93 }, EbmlMaster.class);

    public static final MKVType EBMLVersion = new MKVType("EBMLVersion", new byte[] { 66, -122 }, EbmlUint.class);

    public static final MKVType EBMLReadVersion = new MKVType("EBMLReadVersion", new byte[] { 66, -9 }, EbmlUint.class);

    public static final MKVType EBMLMaxIDLength = new MKVType("EBMLMaxIDLength", new byte[] { 66, -14 }, EbmlUint.class);

    public static final MKVType EBMLMaxSizeLength = new MKVType("EBMLMaxSizeLength", new byte[] { 66, -13 }, EbmlUint.class);

    public static final MKVType DocType = new MKVType("DocType", new byte[] { 66, -126 }, EbmlString.class);

    public static final MKVType DocTypeVersion = new MKVType("DocTypeVersion", new byte[] { 66, -121 }, EbmlUint.class);

    public static final MKVType DocTypeReadVersion = new MKVType("DocTypeReadVersion", new byte[] { 66, -123 }, EbmlUint.class);

    public static final MKVType Segment = new MKVType("Segment", MkvSegment.SEGMENT_ID, MkvSegment.class);

    public static final MKVType SeekHead = new MKVType("SeekHead", new byte[] { 17, 77, -101, 116 }, EbmlMaster.class);

    public static final MKVType Seek = new MKVType("Seek", new byte[] { 77, -69 }, EbmlMaster.class);

    public static final MKVType SeekID = new MKVType("SeekID", new byte[] { 83, -85 }, EbmlBin.class);

    public static final MKVType SeekPosition = new MKVType("SeekPosition", new byte[] { 83, -84 }, EbmlUint.class);

    public static final MKVType Info = new MKVType("Info", new byte[] { 21, 73, -87, 102 }, EbmlMaster.class);

    public static final MKVType SegmentUID = new MKVType("SegmentUID", new byte[] { 115, -92 }, EbmlBin.class);

    public static final MKVType SegmentFilename = new MKVType("SegmentFilename", new byte[] { 115, -124 }, EbmlString.class);

    public static final MKVType PrevUID = new MKVType("PrevUID", new byte[] { 60, -71, 35 }, EbmlBin.class);

    public static final MKVType PrevFilename = new MKVType("PrevFilename", new byte[] { 60, -125, -85 }, EbmlString.class);

    public static final MKVType NextUID = new MKVType("NextUID", new byte[] { 62, -71, 35 }, EbmlBin.class);

    public static final MKVType NextFilenam = new MKVType("NextFilenam", new byte[] { 62, -125, -69 }, EbmlString.class);

    public static final MKVType SegmentFamily = new MKVType("SegmentFamily", new byte[] { 68, 68 }, EbmlBin.class);

    public static final MKVType ChapterTranslate = new MKVType("ChapterTranslate", new byte[] { 105, 36 }, EbmlMaster.class);

    public static final MKVType ChapterTranslateEditionUID = new MKVType("ChapterTranslateEditionUID", new byte[] { 105, -4 }, EbmlUint.class);

    public static final MKVType ChapterTranslateCodec = new MKVType("ChapterTranslateCodec", new byte[] { 105, -65 }, EbmlUint.class);

    public static final MKVType ChapterTranslateID = new MKVType("ChapterTranslateID", new byte[] { 105, -91 }, EbmlBin.class);

    public static final MKVType TimecodeScale = new MKVType("TimecodeScale", new byte[] { 42, -41, -79 }, EbmlUint.class);

    public static final MKVType Duration = new MKVType("Duration", new byte[] { 68, -119 }, EbmlFloat.class);

    public static final MKVType DateUTC = new MKVType("DateUTC", new byte[] { 68, 97 }, EbmlDate.class);

    public static final MKVType Title = new MKVType("Title", new byte[] { 123, -87 }, EbmlString.class);

    public static final MKVType MuxingApp = new MKVType("MuxingApp", new byte[] { 77, -128 }, EbmlString.class);

    public static final MKVType WritingApp = new MKVType("WritingApp", new byte[] { 87, 65 }, EbmlString.class);

    public static final MKVType Cluster = new MKVType("Cluster", EbmlMaster.CLUSTER_ID, EbmlMaster.class);

    public static final MKVType Timecode = new MKVType("Timecode", new byte[] { -25 }, EbmlUint.class);

    public static final MKVType SilentTracks = new MKVType("SilentTracks", new byte[] { 88, 84 }, EbmlMaster.class);

    public static final MKVType SilentTrackNumber = new MKVType("SilentTrackNumber", new byte[] { 88, -41 }, EbmlUint.class);

    public static final MKVType Position = new MKVType("Position", new byte[] { -89 }, EbmlUint.class);

    public static final MKVType PrevSize = new MKVType("PrevSize", new byte[] { -85 }, EbmlUint.class);

    public static final MKVType SimpleBlock = new MKVType("SimpleBlock", MkvBlock.SIMPLEBLOCK_ID, MkvBlock.class);

    public static final MKVType BlockGroup = new MKVType("BlockGroup", new byte[] { -96 }, EbmlMaster.class);

    public static final MKVType Block = new MKVType("Block", MkvBlock.BLOCK_ID, MkvBlock.class);

    public static final MKVType BlockAdditions = new MKVType("BlockAdditions", new byte[] { 117, -95 }, EbmlMaster.class);

    public static final MKVType BlockMore = new MKVType("BlockMore", new byte[] { -90 }, EbmlMaster.class);

    public static final MKVType BlockAddID = new MKVType("BlockAddID", new byte[] { -18 }, EbmlUint.class);

    public static final MKVType BlockAdditional = new MKVType("BlockAdditional", new byte[] { -91 }, EbmlBin.class);

    public static final MKVType BlockDuration = new MKVType("BlockDuration", new byte[] { -101 }, EbmlUint.class);

    public static final MKVType ReferencePriority = new MKVType("ReferencePriority", new byte[] { -6 }, EbmlUint.class);

    public static final MKVType ReferenceBlock = new MKVType("ReferenceBlock", new byte[] { -5 }, EbmlSint.class);

    public static final MKVType CodecState = new MKVType("CodecState", new byte[] { -92 }, EbmlBin.class);

    public static final MKVType Slices = new MKVType("Slices", new byte[] { -114 }, EbmlMaster.class);

    public static final MKVType TimeSlice = new MKVType("TimeSlice", new byte[] { -24 }, EbmlMaster.class);

    public static final MKVType LaceNumber = new MKVType("LaceNumber", new byte[] { -52 }, EbmlUint.class);

    public static final MKVType Tracks = new MKVType("Tracks", new byte[] { 22, 84, -82, 107 }, EbmlMaster.class);

    public static final MKVType TrackEntry = new MKVType("TrackEntry", new byte[] { -82 }, EbmlMaster.class);

    public static final MKVType TrackNumber = new MKVType("TrackNumber", new byte[] { -41 }, EbmlUint.class);

    public static final MKVType TrackUID = new MKVType("TrackUID", new byte[] { 115, -59 }, EbmlUint.class);

    public static final MKVType TrackType = new MKVType("TrackType", new byte[] { -125 }, EbmlUint.class);

    public static final MKVType FlagEnabled = new MKVType("FlagEnabled", new byte[] { -71 }, EbmlUint.class);

    public static final MKVType FlagDefault = new MKVType("FlagDefault", new byte[] { -120 }, EbmlUint.class);

    public static final MKVType FlagForced = new MKVType("FlagForced", new byte[] { 85, -86 }, EbmlUint.class);

    public static final MKVType FlagLacing = new MKVType("FlagLacing", new byte[] { -100 }, EbmlUint.class);

    public static final MKVType MinCache = new MKVType("MinCache", new byte[] { 109, -25 }, EbmlUint.class);

    public static final MKVType MaxCache = new MKVType("MaxCache", new byte[] { 109, -8 }, EbmlUint.class);

    public static final MKVType DefaultDuration = new MKVType("DefaultDuration", new byte[] { 35, -29, -125 }, EbmlUint.class);

    public static final MKVType MaxBlockAdditionID = new MKVType("MaxBlockAdditionID", new byte[] { 85, -18 }, EbmlUint.class);

    public static final MKVType Name = new MKVType("Name", new byte[] { 83, 110 }, EbmlString.class);

    public static final MKVType Language = new MKVType("Language", new byte[] { 34, -75, -100 }, EbmlString.class);

    public static final MKVType CodecID = new MKVType("CodecID", new byte[] { -122 }, EbmlString.class);

    public static final MKVType CodecPrivate = new MKVType("CodecPrivate", new byte[] { 99, -94 }, EbmlBin.class);

    public static final MKVType CodecName = new MKVType("CodecName", new byte[] { 37, -122, -120 }, EbmlString.class);

    public static final MKVType AttachmentLink = new MKVType("AttachmentLink", new byte[] { 116, 70 }, EbmlUint.class);

    public static final MKVType CodecDecodeAll = new MKVType("CodecDecodeAll", new byte[] { -86 }, EbmlUint.class);

    public static final MKVType TrackOverlay = new MKVType("TrackOverlay", new byte[] { 111, -85 }, EbmlUint.class);

    public static final MKVType TrackTranslate = new MKVType("TrackTranslate", new byte[] { 102, 36 }, EbmlMaster.class);

    public static final MKVType TrackTranslateEditionUID = new MKVType("TrackTranslateEditionUID", new byte[] { 102, -4 }, EbmlUint.class);

    public static final MKVType TrackTranslateCodec = new MKVType("TrackTranslateCodec", new byte[] { 102, -65 }, EbmlUint.class);

    public static final MKVType TrackTranslateTrackID = new MKVType("TrackTranslateTrackID", new byte[] { 102, -91 }, EbmlBin.class);

    public static final MKVType Video = new MKVType("Video", new byte[] { -32 }, EbmlMaster.class);

    public static final MKVType FlagInterlaced = new MKVType("FlagInterlaced", new byte[] { -102 }, EbmlUint.class);

    public static final MKVType StereoMode = new MKVType("StereoMode", new byte[] { 83, -72 }, EbmlUint.class);

    public static final MKVType AlphaMode = new MKVType("AlphaMode", new byte[] { 83, -64 }, EbmlUint.class);

    public static final MKVType PixelWidth = new MKVType("PixelWidth", new byte[] { -80 }, EbmlUint.class);

    public static final MKVType PixelHeight = new MKVType("PixelHeight", new byte[] { -70 }, EbmlUint.class);

    public static final MKVType PixelCropBottom = new MKVType("PixelCropBottom", new byte[] { 84, -86 }, EbmlUint.class);

    public static final MKVType PixelCropTop = new MKVType("PixelCropTop", new byte[] { 84, -69 }, EbmlUint.class);

    public static final MKVType PixelCropLeft = new MKVType("PixelCropLeft", new byte[] { 84, -52 }, EbmlUint.class);

    public static final MKVType PixelCropRight = new MKVType("PixelCropRight", new byte[] { 84, -35 }, EbmlUint.class);

    public static final MKVType DisplayWidth = new MKVType("DisplayWidth", new byte[] { 84, -80 }, EbmlUint.class);

    public static final MKVType DisplayHeight = new MKVType("DisplayHeight", new byte[] { 84, -70 }, EbmlUint.class);

    public static final MKVType DisplayUnit = new MKVType("DisplayUnit", new byte[] { 84, -78 }, EbmlUint.class);

    public static final MKVType AspectRatioType = new MKVType("AspectRatioType", new byte[] { 84, -77 }, EbmlUint.class);

    public static final MKVType ColourSpace = new MKVType("ColourSpace", new byte[] { 46, -75, 36 }, EbmlBin.class);

    public static final MKVType Audio = new MKVType("Audio", new byte[] { -31 }, EbmlMaster.class);

    public static final MKVType SamplingFrequency = new MKVType("SamplingFrequency", new byte[] { -75 }, EbmlFloat.class);

    public static final MKVType OutputSamplingFrequency = new MKVType("OutputSamplingFrequency", new byte[] { 120, -75 }, EbmlFloat.class);

    public static final MKVType Channels = new MKVType("Channels", new byte[] { -97 }, EbmlUint.class);

    public static final MKVType BitDepth = new MKVType("BitDepth", new byte[] { 98, 100 }, EbmlUint.class);

    public static final MKVType TrackOperation = new MKVType("TrackOperation", new byte[] { -30 }, EbmlMaster.class);

    public static final MKVType TrackCombinePlanes = new MKVType("TrackCombinePlanes", new byte[] { -29 }, EbmlMaster.class);

    public static final MKVType TrackPlane = new MKVType("TrackPlane", new byte[] { -28 }, EbmlMaster.class);

    public static final MKVType TrackPlaneUID = new MKVType("TrackPlaneUID", new byte[] { -27 }, EbmlUint.class);

    public static final MKVType TrackPlaneType = new MKVType("TrackPlaneType", new byte[] { -26 }, EbmlUint.class);

    public static final MKVType TrackJoinBlocks = new MKVType("TrackJoinBlocks", new byte[] { -23 }, EbmlMaster.class);

    public static final MKVType TrackJoinUID = new MKVType("TrackJoinUID", new byte[] { -19 }, EbmlUint.class);

    public static final MKVType ContentEncodings = new MKVType("ContentEncodings", new byte[] { 109, -128 }, EbmlMaster.class);

    public static final MKVType ContentEncoding = new MKVType("ContentEncoding", new byte[] { 98, 64 }, EbmlMaster.class);

    public static final MKVType ContentEncodingOrder = new MKVType("ContentEncodingOrder", new byte[] { 80, 49 }, EbmlUint.class);

    public static final MKVType ContentEncodingScope = new MKVType("ContentEncodingScope", new byte[] { 80, 50 }, EbmlUint.class);

    public static final MKVType ContentEncodingType = new MKVType("ContentEncodingType", new byte[] { 80, 51 }, EbmlUint.class);

    public static final MKVType ContentCompression = new MKVType("ContentCompression", new byte[] { 80, 52 }, EbmlMaster.class);

    public static final MKVType ContentCompAlgo = new MKVType("ContentCompAlgo", new byte[] { 66, 84 }, EbmlUint.class);

    public static final MKVType ContentCompSettings = new MKVType("ContentCompSettings", new byte[] { 66, 85 }, EbmlBin.class);

    public static final MKVType ContentEncryption = new MKVType("ContentEncryption", new byte[] { 80, 53 }, EbmlMaster.class);

    public static final MKVType ContentEncAlgo = new MKVType("ContentEncAlgo", new byte[] { 71, -31 }, EbmlUint.class);

    public static final MKVType ContentEncKeyID = new MKVType("ContentEncKeyID", new byte[] { 71, -30 }, EbmlBin.class);

    public static final MKVType ContentSignature = new MKVType("ContentSignature", new byte[] { 71, -29 }, EbmlBin.class);

    public static final MKVType ContentSigKeyID = new MKVType("ContentSigKeyID", new byte[] { 71, -28 }, EbmlBin.class);

    public static final MKVType ContentSigAlgo = new MKVType("ContentSigAlgo", new byte[] { 71, -27 }, EbmlUint.class);

    public static final MKVType ContentSigHashAlgo = new MKVType("ContentSigHashAlgo", new byte[] { 71, -26 }, EbmlUint.class);

    public static final MKVType Cues = new MKVType("Cues", new byte[] { 28, 83, -69, 107 }, EbmlMaster.class);

    public static final MKVType CuePoint = new MKVType("CuePoint", new byte[] { -69 }, EbmlMaster.class);

    public static final MKVType CueTime = new MKVType("CueTime", new byte[] { -77 }, EbmlUint.class);

    public static final MKVType CueTrackPositions = new MKVType("CueTrackPositions", new byte[] { -73 }, EbmlMaster.class);

    public static final MKVType CueTrack = new MKVType("CueTrack", new byte[] { -9 }, EbmlUint.class);

    public static final MKVType CueClusterPosition = new MKVType("CueClusterPosition", new byte[] { -15 }, EbmlUint.class);

    public static final MKVType CueRelativePosition = new MKVType("CueRelativePosition", new byte[] { -16 }, EbmlUint.class);

    public static final MKVType CueDuration = new MKVType("CueDuration", new byte[] { -78 }, EbmlUint.class);

    public static final MKVType CueBlockNumber = new MKVType("CueBlockNumber", new byte[] { 83, 120 }, EbmlUint.class);

    public static final MKVType CueCodecState = new MKVType("CueCodecState", new byte[] { -22 }, EbmlUint.class);

    public static final MKVType CueReference = new MKVType("CueReference", new byte[] { -37 }, EbmlMaster.class);

    public static final MKVType CueRefTime = new MKVType("CueRefTime", new byte[] { -106 }, EbmlUint.class);

    public static final MKVType Attachments = new MKVType("Attachments", new byte[] { 25, 65, -92, 105 }, EbmlMaster.class);

    public static final MKVType AttachedFile = new MKVType("AttachedFile", new byte[] { 97, -89 }, EbmlMaster.class);

    public static final MKVType FileDescription = new MKVType("FileDescription", new byte[] { 70, 126 }, EbmlString.class);

    public static final MKVType FileName = new MKVType("FileName", new byte[] { 70, 110 }, EbmlString.class);

    public static final MKVType FileMimeType = new MKVType("FileMimeType", new byte[] { 70, 96 }, EbmlString.class);

    public static final MKVType FileData = new MKVType("FileData", new byte[] { 70, 92 }, EbmlBin.class);

    public static final MKVType FileUID = new MKVType("FileUID", new byte[] { 70, -82 }, EbmlUint.class);

    public static final MKVType Chapters = new MKVType("Chapters", new byte[] { 16, 67, -89, 112 }, EbmlMaster.class);

    public static final MKVType EditionEntry = new MKVType("EditionEntry", new byte[] { 69, -71 }, EbmlMaster.class);

    public static final MKVType EditionUID = new MKVType("EditionUID", new byte[] { 69, -68 }, EbmlUint.class);

    public static final MKVType EditionFlagHidden = new MKVType("EditionFlagHidden", new byte[] { 69, -67 }, EbmlUint.class);

    public static final MKVType EditionFlagDefault = new MKVType("EditionFlagDefault", new byte[] { 69, -37 }, EbmlUint.class);

    public static final MKVType EditionFlagOrdered = new MKVType("EditionFlagOrdered", new byte[] { 69, -35 }, EbmlUint.class);

    public static final MKVType ChapterAtom = new MKVType("ChapterAtom", new byte[] { -74 }, EbmlMaster.class);

    public static final MKVType ChapterUID = new MKVType("ChapterUID", new byte[] { 115, -60 }, EbmlUint.class);

    public static final MKVType ChapterStringUID = new MKVType("ChapterStringUID", new byte[] { 86, 84 }, EbmlString.class);

    public static final MKVType ChapterTimeStart = new MKVType("ChapterTimeStart", new byte[] { -111 }, EbmlUint.class);

    public static final MKVType ChapterTimeEnd = new MKVType("ChapterTimeEnd", new byte[] { -110 }, EbmlUint.class);

    public static final MKVType ChapterFlagHidden = new MKVType("ChapterFlagHidden", new byte[] { -104 }, EbmlUint.class);

    public static final MKVType ChapterFlagEnabled = new MKVType("ChapterFlagEnabled", new byte[] { 69, -104 }, EbmlUint.class);

    public static final MKVType ChapterSegmentUID = new MKVType("ChapterSegmentUID", new byte[] { 110, 103 }, EbmlBin.class);

    public static final MKVType ChapterSegmentEditionUID = new MKVType("ChapterSegmentEditionUID", new byte[] { 110, -68 }, EbmlUint.class);

    public static final MKVType ChapterPhysicalEquiv = new MKVType("ChapterPhysicalEquiv", new byte[] { 99, -61 }, EbmlUint.class);

    public static final MKVType ChapterTrack = new MKVType("ChapterTrack", new byte[] { -113 }, EbmlMaster.class);

    public static final MKVType ChapterTrackNumber = new MKVType("ChapterTrackNumber", new byte[] { -119 }, EbmlUint.class);

    public static final MKVType ChapterDisplay = new MKVType("ChapterDisplay", new byte[] { -128 }, EbmlMaster.class);

    public static final MKVType ChapString = new MKVType("ChapString", new byte[] { -123 }, EbmlString.class);

    public static final MKVType ChapLanguage = new MKVType("ChapLanguage", new byte[] { 67, 124 }, EbmlString.class);

    public static final MKVType ChapCountry = new MKVType("ChapCountry", new byte[] { 67, 126 }, EbmlString.class);

    public static final MKVType ChapProcess = new MKVType("ChapProcess", new byte[] { 105, 68 }, EbmlMaster.class);

    public static final MKVType ChapProcessCodecID = new MKVType("ChapProcessCodecID", new byte[] { 105, 85 }, EbmlUint.class);

    public static final MKVType ChapProcessPrivate = new MKVType("ChapProcessPrivate", new byte[] { 69, 13 }, EbmlBin.class);

    public static final MKVType ChapProcessCommand = new MKVType("ChapProcessCommand", new byte[] { 105, 17 }, EbmlMaster.class);

    public static final MKVType ChapProcessTime = new MKVType("ChapProcessTime", new byte[] { 105, 34 }, EbmlUint.class);

    public static final MKVType ChapProcessData = new MKVType("ChapProcessData", new byte[] { 105, 51 }, EbmlBin.class);

    public static final MKVType Tags = new MKVType("Tags", new byte[] { 18, 84, -61, 103 }, EbmlMaster.class);

    public static final MKVType Tag = new MKVType("Tag", new byte[] { 115, 115 }, EbmlMaster.class);

    public static final MKVType Targets = new MKVType("Targets", new byte[] { 99, -64 }, EbmlMaster.class);

    public static final MKVType TargetTypeValue = new MKVType("TargetTypeValue", new byte[] { 104, -54 }, EbmlUint.class);

    public static final MKVType TargetType = new MKVType("TargetType", new byte[] { 99, -54 }, EbmlString.class);

    public static final MKVType TagTrackUID = new MKVType("TagTrackUID", new byte[] { 99, -59 }, EbmlUint.class);

    public static final MKVType TagEditionUID = new MKVType("TagEditionUID", new byte[] { 99, -55 }, EbmlUint.class);

    public static final MKVType TagChapterUID = new MKVType("TagChapterUID", new byte[] { 99, -60 }, EbmlUint.class);

    public static final MKVType TagAttachmentUID = new MKVType("TagAttachmentUID", new byte[] { 99, -58 }, EbmlUint.class);

    public static final MKVType SimpleTag = new MKVType("SimpleTag", new byte[] { 103, -56 }, EbmlMaster.class);

    public static final MKVType TagName = new MKVType("TagName", new byte[] { 69, -93 }, EbmlString.class);

    public static final MKVType TagLanguage = new MKVType("TagLanguage", new byte[] { 68, 122 }, EbmlString.class);

    public static final MKVType TagDefault = new MKVType("TagDefault", new byte[] { 68, -124 }, EbmlUint.class);

    public static final MKVType TagString = new MKVType("TagString", new byte[] { 68, -121 }, EbmlString.class);

    public static final MKVType TagBinary = new MKVType("TagBinary", new byte[] { 68, -123 }, EbmlBin.class);

    public static MKVType[] firstLevelHeaders = new MKVType[] { SeekHead, Info, Cluster, Tracks, Cues, Attachments, Chapters, Tags, EBMLVersion, EBMLReadVersion, EBMLMaxIDLength, EBMLMaxSizeLength, DocType, DocTypeVersion, DocTypeReadVersion };

    public final byte[] id;

    public final Class<? extends EbmlBase> clazz;

    private String _name;

    public static final Map<MKVType, Set<MKVType>> children = new HashMap();

    private MKVType(String name, byte[] id, Class<? extends EbmlBase> clazz) {
        this._name = name;
        this.id = id;
        this.clazz = clazz;
        _values.add(this);
    }

    public String name() {
        return this._name;
    }

    public String toString() {
        return this._name;
    }

    public static MKVType[] values() {
        return (MKVType[]) _values.toArray(new MKVType[0]);
    }

    public static <T extends EbmlBase> T createByType(MKVType g) {
        try {
            T elem = (T) Platform.newInstance((Class<T>) g.clazz, new Object[] { g.id });
            elem.type = g;
            return elem;
        } catch (Exception var2) {
            var2.printStackTrace();
            return (T) (new EbmlBin(g.id));
        }
    }

    public static <T extends EbmlBase> T createById(byte[] id, long offset) {
        MKVType[] values = values();
        for (int i = 0; i < values.length; i++) {
            MKVType t = values[i];
            if (Platform.arrayEqualsByte(t.id, id)) {
                return createByType(t);
            }
        }
        System.err.println("WARNING: unspecified ebml ID (" + EbmlUtil.toHexString(id) + ") encountered at position 0x" + Long.toHexString(offset).toUpperCase());
        T t = (T) (new EbmlVoid(id));
        t.type = Void;
        return t;
    }

    public static boolean isHeaderFirstByte(byte b) {
        MKVType[] values = values();
        for (int i = 0; i < values.length; i++) {
            MKVType t = values[i];
            if (t.id[0] == b) {
                return true;
            }
        }
        return false;
    }

    public static boolean isSpecifiedHeader(byte[] b) {
        MKVType[] values = values();
        for (int i = 0; i < values.length; i++) {
            MKVType firstLevelHeader = values[i];
            if (Platform.arrayEqualsByte(firstLevelHeader.id, b)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isFirstLevelHeader(byte[] b) {
        for (MKVType firstLevelHeader : firstLevelHeaders) {
            if (Platform.arrayEqualsByte(firstLevelHeader.id, b)) {
                return true;
            }
        }
        return false;
    }

    public static MKVType getParent(MKVType t) {
        for (Entry<MKVType, Set<MKVType>> ent : children.entrySet()) {
            if (((Set) ent.getValue()).contains(t)) {
                return (MKVType) ent.getKey();
            }
        }
        return null;
    }

    public static boolean possibleChild(EbmlMaster parent, EbmlBase child) {
        if (parent == null) {
            return child.type == EBML || child.type == Segment;
        } else if (Platform.arrayEqualsByte(child.id, Void.id) || Platform.arrayEqualsByte(child.id, CRC32.id)) {
            return child.offset != parent.dataOffset + (long) parent.dataLen;
        } else if (child.type != Void && child.type != CRC32) {
            Set<MKVType> candidates = (Set<MKVType>) children.get(parent.type);
            return candidates != null && candidates.contains(child.type);
        } else {
            return true;
        }
    }

    public static boolean possibleChildById(EbmlMaster parent, byte[] typeId) {
        if (parent != null || !Platform.arrayEqualsByte(EBML.id, typeId) && !Platform.arrayEqualsByte(Segment.id, typeId)) {
            if (parent == null) {
                return false;
            } else if (!Platform.arrayEqualsByte(Void.id, typeId) && !Platform.arrayEqualsByte(CRC32.id, typeId)) {
                for (MKVType aCandidate : (Set) children.get(parent.type)) {
                    if (Platform.arrayEqualsByte(aCandidate.id, typeId)) {
                        return true;
                    }
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static EbmlBase findFirst(EbmlBase master, MKVType[] path) {
        List<MKVType> tlist = new LinkedList(Arrays.asList(path));
        return findFirstSub(master, tlist);
    }

    public static <T> T findFirstTree(List<? extends EbmlBase> tree, MKVType[] path) {
        List<MKVType> tlist = new LinkedList(Arrays.asList(path));
        for (EbmlBase e : tree) {
            EbmlBase z = findFirstSub(e, tlist);
            if (z != null) {
                return (T) z;
            }
        }
        return null;
    }

    private static EbmlBase findFirstSub(EbmlBase elem, List<MKVType> path) {
        if (path.size() == 0) {
            return null;
        } else if (!elem.type.equals(path.get(0))) {
            return null;
        } else if (path.size() == 1) {
            return elem;
        } else {
            MKVType head = (MKVType) path.remove(0);
            EbmlBase result = null;
            if (elem instanceof EbmlMaster) {
                Iterator<EbmlBase> iter = ((EbmlMaster) elem).children.iterator();
                while (iter.hasNext() && result == null) {
                    result = findFirstSub((EbmlBase) iter.next(), path);
                }
            }
            path.add(0, head);
            return result;
        }
    }

    public static <T> List<T> findList(List<? extends EbmlBase> tree, Class<T> class1, MKVType[] path) {
        List<T> result = new LinkedList();
        List<MKVType> tlist = new LinkedList(Arrays.asList(path));
        if (tlist.size() > 0) {
            for (EbmlBase node : tree) {
                MKVType head = (MKVType) tlist.remove(0);
                if (head == null || head.equals(node.type)) {
                    findSubList(node, tlist, result);
                }
                tlist.add(0, head);
            }
        }
        return result;
    }

    private static <T> void findSubList(EbmlBase element, List<MKVType> path, Collection<T> result) {
        if (path.size() > 0) {
            MKVType head = (MKVType) path.remove(0);
            if (element instanceof EbmlMaster) {
                EbmlMaster nb = (EbmlMaster) element;
                for (EbmlBase candidate : nb.children) {
                    if (head == null || head.equals(candidate.type)) {
                        findSubList(candidate, path, result);
                    }
                }
            }
            path.add(0, head);
        } else {
            result.add(element);
        }
    }

    public static <T> T[] findAllTree(List<? extends EbmlBase> tree, Class<T> class1, MKVType[] path) {
        List<EbmlBase> result = new LinkedList();
        List<MKVType> tlist = new LinkedList(Arrays.asList(path));
        if (tlist.size() > 0) {
            for (EbmlBase node : tree) {
                MKVType head = (MKVType) tlist.remove(0);
                if (head == null || head.equals(node.type)) {
                    findSub(node, tlist, result);
                }
                tlist.add(0, head);
            }
        }
        return (T[]) result.toArray((Object[]) Array.newInstance(class1, 0));
    }

    public static <T> T[] findAll(EbmlBase master, Class<T> class1, boolean ga, MKVType[] path) {
        List<EbmlBase> result = new LinkedList();
        List<MKVType> tlist = new LinkedList(Arrays.asList(path));
        if (!master.type.equals(tlist.get(0))) {
            return (T[]) result.toArray((Object[]) Array.newInstance(class1, 0));
        } else {
            tlist.remove(0);
            findSub(master, tlist, result);
            return (T[]) result.toArray((Object[]) Array.newInstance(class1, 0));
        }
    }

    private static void findSub(EbmlBase master, List<MKVType> path, Collection<EbmlBase> result) {
        if (path.size() > 0) {
            MKVType head = (MKVType) path.remove(0);
            if (master instanceof EbmlMaster) {
                EbmlMaster nb = (EbmlMaster) master;
                for (EbmlBase candidate : nb.children) {
                    if (head == null || head.equals(candidate.type)) {
                        findSub(candidate, path, result);
                    }
                }
            }
            path.add(0, head);
        } else {
            result.add(master);
        }
    }

    static {
        children.put(EBML, new HashSet(Arrays.asList(EBMLVersion, EBMLReadVersion, EBMLMaxIDLength, EBMLMaxSizeLength, DocType, DocTypeVersion, DocTypeReadVersion)));
        children.put(Segment, new HashSet(Arrays.asList(SeekHead, Info, Cluster, Tracks, Cues, Attachments, Chapters, Tags)));
        children.put(SeekHead, new HashSet(Arrays.asList(Seek)));
        children.put(Seek, new HashSet(Arrays.asList(SeekID, SeekPosition)));
        children.put(Info, new HashSet(Arrays.asList(SegmentUID, SegmentFilename, PrevUID, PrevFilename, NextUID, NextFilenam, SegmentFamily, ChapterTranslate, TimecodeScale, Duration, DateUTC, Title, MuxingApp, WritingApp)));
        children.put(ChapterTranslate, new HashSet(Arrays.asList(ChapterTranslateEditionUID, ChapterTranslateCodec, ChapterTranslateID)));
        children.put(Cluster, new HashSet(Arrays.asList(Timecode, SilentTracks, Position, PrevSize, SimpleBlock, BlockGroup)));
        children.put(SilentTracks, new HashSet(Arrays.asList(SilentTrackNumber)));
        children.put(BlockGroup, new HashSet(Arrays.asList(Block, BlockAdditions, BlockDuration, ReferencePriority, ReferenceBlock, CodecState, Slices)));
        children.put(BlockAdditions, new HashSet(Arrays.asList(BlockMore)));
        children.put(BlockMore, new HashSet(Arrays.asList(BlockAddID, BlockAdditional)));
        children.put(Slices, new HashSet(Arrays.asList(TimeSlice)));
        children.put(TimeSlice, new HashSet(Arrays.asList(LaceNumber)));
        children.put(Tracks, new HashSet(Arrays.asList(TrackEntry)));
        children.put(TrackEntry, new HashSet(Arrays.asList(TrackNumber, TrackUID, TrackType, TrackType, FlagDefault, FlagForced, FlagLacing, MinCache, MaxCache, DefaultDuration, MaxBlockAdditionID, Name, Language, CodecID, CodecPrivate, CodecName, AttachmentLink, CodecDecodeAll, TrackOverlay, TrackTranslate, Video, Audio, TrackOperation, ContentEncodings)));
        children.put(TrackTranslate, new HashSet(Arrays.asList(TrackTranslateEditionUID, TrackTranslateCodec, TrackTranslateTrackID)));
        children.put(Video, new HashSet(Arrays.asList(FlagInterlaced, StereoMode, AlphaMode, PixelWidth, PixelHeight, PixelCropBottom, PixelCropTop, PixelCropLeft, PixelCropRight, DisplayWidth, DisplayHeight, DisplayUnit, AspectRatioType, ColourSpace)));
        children.put(Audio, new HashSet(Arrays.asList(SamplingFrequency, OutputSamplingFrequency, Channels, BitDepth)));
        children.put(TrackOperation, new HashSet(Arrays.asList(TrackCombinePlanes, TrackJoinBlocks)));
        children.put(TrackCombinePlanes, new HashSet(Arrays.asList(TrackPlane)));
        children.put(TrackPlane, new HashSet(Arrays.asList(TrackPlaneUID, TrackPlaneType)));
        children.put(TrackJoinBlocks, new HashSet(Arrays.asList(TrackJoinUID)));
        children.put(ContentEncodings, new HashSet(Arrays.asList(ContentEncoding)));
        children.put(ContentEncoding, new HashSet(Arrays.asList(ContentEncodingOrder, ContentEncodingScope, ContentEncodingType, ContentCompression, ContentEncryption)));
        children.put(ContentCompression, new HashSet(Arrays.asList(ContentCompAlgo, ContentCompSettings)));
        children.put(ContentEncryption, new HashSet(Arrays.asList(ContentEncAlgo, ContentEncKeyID, ContentSignature, ContentSigKeyID, ContentSigAlgo, ContentSigHashAlgo)));
        children.put(Cues, new HashSet(Arrays.asList(CuePoint)));
        children.put(CuePoint, new HashSet(Arrays.asList(CueTime, CueTrackPositions)));
        children.put(CueTrackPositions, new HashSet(Arrays.asList(CueTrack, CueClusterPosition, CueRelativePosition, CueDuration, CueBlockNumber, CueCodecState, CueReference)));
        children.put(CueReference, new HashSet(Arrays.asList(CueRefTime)));
        children.put(Attachments, new HashSet(Arrays.asList(AttachedFile)));
        children.put(AttachedFile, new HashSet(Arrays.asList(FileDescription, FileName, FileMimeType, FileData, FileUID)));
        children.put(Chapters, new HashSet(Arrays.asList(EditionEntry)));
        children.put(EditionEntry, new HashSet(Arrays.asList(EditionUID, EditionFlagHidden, EditionFlagDefault, EditionFlagOrdered, ChapterAtom)));
        children.put(ChapterAtom, new HashSet(Arrays.asList(ChapterUID, ChapterStringUID, ChapterTimeStart, ChapterTimeEnd, ChapterFlagHidden, ChapterFlagEnabled, ChapterSegmentUID, ChapterSegmentEditionUID, ChapterPhysicalEquiv, ChapterTrack, ChapterDisplay, ChapProcess)));
        children.put(ChapterTrack, new HashSet(Arrays.asList(ChapterTrackNumber)));
        children.put(ChapterDisplay, new HashSet(Arrays.asList(ChapString, ChapLanguage, ChapCountry)));
        children.put(ChapProcess, new HashSet(Arrays.asList(ChapProcessCodecID, ChapProcessPrivate, ChapProcessCommand)));
        children.put(ChapProcessCommand, new HashSet(Arrays.asList(ChapProcessTime, ChapProcessData)));
        children.put(Tags, new HashSet(Arrays.asList(Tag)));
        children.put(Tag, new HashSet(Arrays.asList(Targets, SimpleTag)));
        children.put(Targets, new HashSet(Arrays.asList(TargetTypeValue, TargetType, TagTrackUID, TagEditionUID, TagChapterUID, TagAttachmentUID)));
        children.put(SimpleTag, new HashSet(Arrays.asList(TagName, TagLanguage, TagDefault, TagString, TagBinary)));
    }
}