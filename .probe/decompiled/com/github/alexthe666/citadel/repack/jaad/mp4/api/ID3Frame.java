package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class ID3Frame {

    static final int ALBUM_TITLE = 1413565506;

    static final int ALBUM_SORT_ORDER = 1414745921;

    static final int ARTIST = 1414546737;

    static final int ATTACHED_PICTURE = 1095780675;

    static final int AUDIO_ENCRYPTION = 1095061059;

    static final int AUDIO_SEEK_POINT_INDEX = 1095979081;

    static final int BAND = 1414546738;

    static final int BEATS_PER_MINUTE = 1413632077;

    static final int COMMENTS = 1129270605;

    static final int COMMERCIAL_FRAME = 1129270610;

    static final int COMMERCIAL_INFORMATION = 1464029005;

    static final int COMPOSER = 1413697357;

    static final int CONDUCTOR = 1414546739;

    static final int CONTENT_GROUP_DESCRIPTION = 1414091825;

    static final int CONTENT_TYPE = 1413697358;

    static final int COPYRIGHT = 1464029008;

    static final int COPYRIGHT_MESSAGE = 1413697360;

    static final int ENCODED_BY = 1413828163;

    static final int ENCODING_TIME = 1413760334;

    static final int ENCRYPTION_METHOD_REGISTRATION = 1162756946;

    static final int EQUALISATION = 1162958130;

    static final int EVENT_TIMING_CODES = 1163150159;

    static final int FILE_OWNER = 1414485838;

    static final int FILE_TYPE = 1413893204;

    static final int GENERAL_ENCAPSULATED_OBJECT = 1195724610;

    static final int GROUP_IDENTIFICATION_REGISTRATION = 1196575044;

    static final int INITIAL_KEY = 1414219097;

    static final int INTERNET_RADIO_STATION_NAME = 1414681422;

    static final int INTERNET_RADIO_STATION_OWNER = 1414681423;

    static final int MODIFIED_BY = 1414546740;

    static final int INVOLVED_PEOPLE_LIST = 1414090828;

    static final int INTERNATIONAL_STANDARD_RECORDING_CODE = 1414746691;

    static final int LANGUAGES = 1414283598;

    static final int LENGTH = 1414284622;

    static final int LINKED_INFORMATION = 1279872587;

    static final int LYRICIST = 1413830740;

    static final int MEDIA_TYPE = 1414350148;

    static final int MOOD = 1414352719;

    static final int MPEG_LOCATION_LOOKUP_TABLE = 1296845908;

    static final int MUSICIAN_CREDITS_LIST = 1414349644;

    static final int MUSIC_CD_IDENTIFIER = 1296254025;

    static final int OFFICIAL_ARTIST_WEBPAGE = 1464811858;

    static final int OFFICIAL_AUDIO_FILE_WEBPAGE = 1464811846;

    static final int OFFICIAL_AUDIO_SOURCE_WEBPAGE = 1464811859;

    static final int OFFICIAL_INTERNET_RADIO_STATION_HOMEPAGE = 1464816211;

    static final int ORIGINAL_ALBUM_TITLE = 1414480204;

    static final int ORIGINAL_ARTIST = 1414484037;

    static final int ORIGINAL_FILENAME = 1414481486;

    static final int ORIGINAL_LYRICIST = 1414483033;

    static final int ORIGINAL_RELEASE_TIME = 1413762898;

    static final int OWNERSHIP_FRAME = 1331121733;

    static final int PART_OF_A_SET = 1414549331;

    static final int PAYMENT = 1464877401;

    static final int PERFORMER_SORT_ORDER = 1414745936;

    static final int PLAYLIST_DELAY = 1413762137;

    static final int PLAY_COUNTER = 1346588244;

    static final int POPULARIMETER = 1347375181;

    static final int POSITION_SYNCHRONISATION_FRAME = 1347375955;

    static final int PRIVATE_FRAME = 1347570006;

    static final int PRODUCED_NOTICE = 1414550095;

    static final int PUBLISHER = 1414550850;

    static final int PUBLISHERS_OFFICIAL_WEBPAGE = 1464882498;

    static final int RECOMMENDED_BUFFER_SIZE = 1380078918;

    static final int RECORDING_TIME = 1413763651;

    static final int RELATIVE_VOLUME_ADJUSTMENT = 1381384498;

    static final int RELEASE_TIME = 1413763660;

    static final int REVERB = 1381388866;

    static final int SEEK_FRAME = 1397048651;

    static final int SET_SUBTITLE = 1414746964;

    static final int SIGNATURE_FRAME = 1397311310;

    static final int ENCODING_TOOLS_AND_SETTINGS = 1414746949;

    static final int SUBTITLE = 1414091827;

    static final int SYNCHRONISED_LYRIC = 1398361172;

    static final int SYNCHRONISED_TEMPO_CODES = 1398363203;

    static final int TAGGING_TIME = 1413764167;

    static final int TERMS_OF_USE = 1431520594;

    static final int TITLE = 1414091826;

    static final int TITLE_SORT_ORDER = 1414745940;

    static final int TRACK_NUMBER = 1414677323;

    static final int UNIQUE_FILE_IDENTIFIER = 1430669636;

    static final int UNSYNCHRONISED_LYRIC = 1431522388;

    static final int USER_DEFINED_TEXT_INFORMATION_FRAME = 1415075928;

    static final int USER_DEFINED_URL_LINK_FRAME = 1465407576;

    private static final String[] TEXT_ENCODINGS = new String[] { "ISO-8859-1", "UTF-16", "UTF-16", "UTF-8" };

    private static final String[] VALID_TIMESTAMPS = new String[] { "yyyy, yyyy-MM", "yyyy-MM-dd", "yyyy-MM-ddTHH", "yyyy-MM-ddTHH:mm", "yyyy-MM-ddTHH:mm:ss" };

    private static final String UNKNOWN_LANGUAGE = "xxx";

    private long size;

    private int id;

    private int flags;

    private int groupID;

    private int encryptionMethod;

    private byte[] data;

    ID3Frame(DataInputStream in) throws IOException {
        this.id = in.readInt();
        this.size = (long) ID3Tag.readSynch(in);
        this.flags = in.readShort();
        if (this.isInGroup()) {
            this.groupID = in.read();
        }
        if (this.isEncrypted()) {
            this.encryptionMethod = in.read();
        }
        this.data = new byte[(int) this.size];
        in.readFully(this.data);
    }

    public int getID() {
        return this.id;
    }

    public long getSize() {
        return this.size;
    }

    public final boolean isInGroup() {
        return (this.flags & 64) == 64;
    }

    public int getGroupID() {
        return this.groupID;
    }

    public final boolean isCompressed() {
        return (this.flags & 8) == 8;
    }

    public final boolean isEncrypted() {
        return (this.flags & 4) == 4;
    }

    public int getEncryptionMethod() {
        return this.encryptionMethod;
    }

    public byte[] getData() {
        return this.data;
    }

    public String getText() {
        return new String(this.data, Charset.forName(TEXT_ENCODINGS[0]));
    }

    public String getEncodedText() {
        int enc = this.data[0];
        int t = -1;
        for (int i = 1; i < this.data.length && t < 0; i++) {
            if (this.data[i] == 0 && (enc == 0 || enc == 3 || this.data[i + 1] == 0)) {
                t = i;
            }
        }
        return new String(this.data, 1, t - 1, Charset.forName(TEXT_ENCODINGS[enc]));
    }

    public int getNumber() {
        return Integer.parseInt(new String(this.data));
    }

    public int[] getNumbers() {
        String x = new String(this.data, Charset.forName(TEXT_ENCODINGS[0]));
        int i = x.indexOf(47);
        int[] y;
        if (i > 0) {
            y = new int[] { Integer.parseInt(x.substring(0, i)), Integer.parseInt(x.substring(i + 1)) };
        } else {
            y = new int[] { Integer.parseInt(x) };
        }
        return y;
    }

    public Date getDate() {
        int i = (int) Math.floor((double) (this.data.length / 3)) - 1;
        Date date;
        if (i >= 0 && i < VALID_TIMESTAMPS.length) {
            SimpleDateFormat sdf = new SimpleDateFormat(VALID_TIMESTAMPS[i]);
            date = sdf.parse(new String(this.data), new ParsePosition(0));
        } else {
            date = null;
        }
        return date;
    }

    public Locale getLocale() {
        String s = new String(this.data).toLowerCase();
        Locale l;
        if (s.equals("xxx")) {
            l = null;
        } else {
            l = new Locale(s);
        }
        return l;
    }
}