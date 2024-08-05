package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.CopyrightBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.ID3TagBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.ITunesMetadataBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.NeroMetadataTagsBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.ThreeGPPAlbumBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.ThreeGPPLocationBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.ThreeGPPMetadataBox;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MetaData {

    private static final String[] STANDARD_GENRES = new String[] { "undefined", "blues", "classic rock", "country", "dance", "disco", "funk", "grunge", "hip hop", "jazz", "metal", "new age", "oldies", "other", "pop", "r and b", "rap", "reggae", "rock", "techno", "industrial", "alternative", "ska", "death metal", "pranks", "soundtrack", "euro techno", "ambient", "trip hop", "vocal", "jazz funk", "fusion", "trance", "classical", "instrumental", "acid", "house", "game", "sound clip", "gospel", "noise", "alternrock", "bass", "soul", "punk", "space", "meditative", "instrumental pop", "instrumental rock", "ethnic", "gothic", "darkwave", "techno industrial", "electronic", "pop folk", "eurodance", "dream", "southern rock", "comedy", "cult", "gangsta", "top ", "christian rap", "pop funk", "jungle", "native american", "cabaret", "new wave", "psychedelic", "rave", "showtunes", "trailer", "lo fi", "tribal", "acid punk", "acid jazz", "polka", "retro", "musical", "rock and roll", "hard rock", "folk", "folk rock", "national folk", "swing", "fast fusion", "bebob", "latin", "revival", "celtic", "bluegrass", "avantgarde", "gothic rock", "progressive rock", "psychedelic rock", "symphonic rock", "slow rock", "big band", "chorus", "easy listening", "acoustic", "humour", "speech", "chanson", "opera", "chamber music", "sonata", "symphony", "booty bass", "primus", "porn groove", "satire", "slow jam", "club", "tango", "samba", "folklore", "ballad", "power ballad", "rhythmic soul", "freestyle", "duet", "punk rock", "drum solo", "a capella", "euro house", "dance hall" };

    private static final String[] NERO_TAGS = new String[] { "artist", "title", "album", "track", "totaltracks", "year", "genre", "disc", "totaldiscs", "url", "copyright", "comment", "lyrics", "credits", "rating", "label", "composer", "isrc", "mood", "tempo" };

    private Map<MetaData.Field<?>, Object> contents = new HashMap();

    MetaData() {
    }

    void parse(Box udta, Box meta) {
        if (meta.hasChild(1668313716L)) {
            CopyrightBox cprt = (CopyrightBox) meta.getChild(1668313716L);
            this.put(MetaData.Field.LANGUAGE, new Locale(cprt.getLanguageCode()));
            this.put(MetaData.Field.COPYRIGHT, cprt.getNotice());
        }
        if (udta != null) {
            this.parse3GPPData(udta);
        }
        if (meta.hasChild(1768174386L)) {
            this.parseID3((ID3TagBox) meta.getChild(1768174386L));
        }
        if (meta.hasChild(1768715124L)) {
            this.parseITunesMetaData(meta.getChild(1768715124L));
        }
        if (meta.hasChild(1952540531L)) {
            this.parseNeroTags((NeroMetadataTagsBox) meta.getChild(1952540531L));
        }
    }

    private void parse3GPPData(Box udta) {
        if (udta.hasChild(1634493037L)) {
            ThreeGPPAlbumBox albm = (ThreeGPPAlbumBox) udta.getChild(1634493037L);
            this.put(MetaData.Field.ALBUM, albm.getData());
            this.put(MetaData.Field.TRACK_NUMBER, albm.getTrackNumber());
        }
        if (udta.hasChild(1685283696L)) {
            this.put(MetaData.Field.DESCRIPTION, ((ThreeGPPMetadataBox) udta.getChild(1685283696L)).getData());
        }
        if (udta.hasChild(1803122532L)) {
            this.put(MetaData.Field.KEYWORDS, ((ThreeGPPMetadataBox) udta.getChild(1803122532L)).getData());
        }
        if (udta.hasChild(1819239273L)) {
            this.put(MetaData.Field.LOCATION, ((ThreeGPPLocationBox) udta.getChild(1819239273L)).getPlaceName());
        }
        if (udta.hasChild(1885696614L)) {
            this.put(MetaData.Field.ARTIST, ((ThreeGPPMetadataBox) udta.getChild(1885696614L)).getData());
        }
        if (udta.hasChild(2037543523L)) {
            String value = ((ThreeGPPMetadataBox) udta.getChild(2037543523L)).getData();
            try {
                this.put(MetaData.Field.RELEASE_DATE, new Date((long) Integer.parseInt(value)));
            } catch (NumberFormatException var4) {
                Logger.getLogger("MP4 API").log(Level.INFO, "unable to parse 3GPP metadata: recording year value: {0}", value);
            }
        }
        if (udta.hasChild(1953068140L)) {
            this.put(MetaData.Field.TITLE, ((ThreeGPPMetadataBox) udta.getChild(1953068140L)).getData());
        }
    }

    private void parseITunesMetaData(Box ilst) {
        for (Box box : ilst.getChildren()) {
            long l = box.getType();
            ITunesMetadataBox data = (ITunesMetadataBox) box.getChild(1684108385L);
            if (l == 2839630420L) {
                this.put(MetaData.Field.ARTIST, data.getText());
            } else if (l == 2842583405L) {
                this.put(MetaData.Field.TITLE, data.getText());
            } else if (l == 1631670868L) {
                this.put(MetaData.Field.ALBUM_ARTIST, data.getText());
            } else if (l == 2841734242L) {
                this.put(MetaData.Field.ALBUM, data.getText());
            } else if (l == 1953655662L) {
                byte[] b = data.getData();
                int b3 = b[3];
                int b5 = b[5];
                this.put(MetaData.Field.TRACK_NUMBER, b3);
                this.put(MetaData.Field.TOTAL_TRACKS, b5);
            } else if (l == 1684632427L) {
                this.put(MetaData.Field.DISK_NUMBER, data.getInteger());
            } else if (l == 2843177588L) {
                this.put(MetaData.Field.COMPOSER, data.getText());
            } else if (l == 2841865588L) {
                this.put(MetaData.Field.COMMENTS, data.getText());
            } else if (l == 1953329263L) {
                this.put(MetaData.Field.TEMPO, data.getInteger());
            } else if (l == 2841928057L) {
                this.put(MetaData.Field.RELEASE_DATE, data.getDate());
            } else if (l == 1735291493L || l == 2842125678L) {
                String s = null;
                if (data.getDataType() == ITunesMetadataBox.DataType.UTF8) {
                    s = data.getText();
                } else {
                    int i = data.getInteger();
                    if (i > 0 && i < STANDARD_GENRES.length) {
                        s = STANDARD_GENRES[data.getInteger()];
                    }
                }
                if (s != null) {
                    this.put(MetaData.Field.GENRE, s);
                }
            } else if (l == 2841996899L) {
                this.put(MetaData.Field.ENCODER_NAME, data.getText());
            } else if (l == 2842980207L) {
                this.put(MetaData.Field.ENCODER_TOOL, data.getText());
            } else if (l == 1668313716L) {
                this.put(MetaData.Field.COPYRIGHT, data.getText());
            } else if (l == 1668311404L) {
                this.put(MetaData.Field.COMPILATION, data.getBoolean());
            } else if (l == 1668249202L) {
                Artwork aw = new Artwork(Artwork.Type.forDataType(data.getDataType()), data.getData());
                if (this.contents.containsKey(MetaData.Field.COVER_ARTWORKS)) {
                    this.get(MetaData.Field.COVER_ARTWORKS).add(aw);
                } else {
                    List<Artwork> list = new ArrayList();
                    list.add(aw);
                    this.put(MetaData.Field.COVER_ARTWORKS, list);
                }
            } else if (l == 2842129008L) {
                this.put(MetaData.Field.GROUPING, data.getText());
            } else if (l == 2842458482L) {
                this.put(MetaData.Field.LYRICS, data.getText());
            } else if (l == 1920233063L) {
                this.put(MetaData.Field.RATING, data.getInteger());
            } else if (l == 1885565812L) {
                this.put(MetaData.Field.PODCAST, data.getInteger());
            } else if (l == 1886745196L) {
                this.put(MetaData.Field.PODCAST_URL, data.getText());
            } else if (l == 1667331175L) {
                this.put(MetaData.Field.CATEGORY, data.getText());
            } else if (l == 1801812343L) {
                this.put(MetaData.Field.KEYWORDS, data.getText());
            } else if (l == 1684370275L) {
                this.put(MetaData.Field.DESCRIPTION, data.getText());
            } else if (l == 1818518899L) {
                this.put(MetaData.Field.DESCRIPTION, data.getText());
            } else if (l == 1953919848L) {
                this.put(MetaData.Field.TV_SHOW, data.getText());
            } else if (l == 1953918574L) {
                this.put(MetaData.Field.TV_NETWORK, data.getText());
            } else if (l == 1953916275L) {
                this.put(MetaData.Field.TV_EPISODE, data.getText());
            } else if (l == 1953916270L) {
                this.put(MetaData.Field.TV_EPISODE_NUMBER, data.getInteger());
            } else if (l == 1953919854L) {
                this.put(MetaData.Field.TV_SEASON, data.getInteger());
            } else if (l == 1886745188L) {
                this.put(MetaData.Field.PURCHASE_DATE, data.getText());
            } else if (l == 1885823344L) {
                this.put(MetaData.Field.GAPLESS_PLAYBACK, data.getText());
            } else if (l == 1751414372L) {
                this.put(MetaData.Field.HD_VIDEO, data.getBoolean());
            } else if (l == 1936679282L) {
                this.put(MetaData.Field.ARTIST_SORT_TEXT, data.getText());
            } else if (l == 1936682605L) {
                this.put(MetaData.Field.TITLE_SORT_TEXT, data.getText());
            } else if (l == 1936679276L) {
                this.put(MetaData.Field.ALBUM_SORT_TEXT, data.getText());
            }
        }
    }

    private void parseID3(ID3TagBox box) {
        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(box.getID3Data()));
            ID3Tag tag = new ID3Tag(in);
            for (ID3Frame frame : tag.getFrames()) {
                switch(frame.getID()) {
                    case 1413565506:
                        this.put(MetaData.Field.ALBUM, frame.getEncodedText());
                        break;
                    case 1413632077:
                        this.put(MetaData.Field.TEMPO, frame.getNumber());
                        break;
                    case 1413697357:
                        this.put(MetaData.Field.COMPOSER, frame.getEncodedText());
                        break;
                    case 1413697360:
                        this.put(MetaData.Field.COPYRIGHT, frame.getEncodedText());
                        break;
                    case 1413760334:
                        this.put(MetaData.Field.ENCODING_DATE, frame.getDate());
                        break;
                    case 1413763660:
                        this.put(MetaData.Field.RELEASE_DATE, frame.getDate());
                        break;
                    case 1414091826:
                        this.put(MetaData.Field.TITLE, frame.getEncodedText());
                        break;
                    case 1414283598:
                        this.put(MetaData.Field.LANGUAGE, frame.getLocale());
                        break;
                    case 1414284622:
                        this.put(MetaData.Field.LENGTH_IN_MILLISECONDS, frame.getNumber());
                        break;
                    case 1414546737:
                        this.put(MetaData.Field.ARTIST, frame.getEncodedText());
                        break;
                    case 1414550850:
                        this.put(MetaData.Field.PUBLISHER, frame.getEncodedText());
                        break;
                    case 1414677323:
                        int[] num = frame.getNumbers();
                        this.put(MetaData.Field.TRACK_NUMBER, num[0]);
                        if (num.length > 1) {
                            this.put(MetaData.Field.TOTAL_TRACKS, num[1]);
                        }
                        break;
                    case 1414681422:
                        this.put(MetaData.Field.INTERNET_RADIO_STATION, frame.getEncodedText());
                        break;
                    case 1414745921:
                        this.put(MetaData.Field.ALBUM_SORT_TEXT, frame.getEncodedText());
                        break;
                    case 1414745936:
                        this.put(MetaData.Field.ARTIST_SORT_TEXT, frame.getEncodedText());
                        break;
                    case 1414745940:
                        this.put(MetaData.Field.TITLE_SORT_TEXT, frame.getEncodedText());
                        break;
                    case 1414746949:
                        this.put(MetaData.Field.ENCODER_TOOL, frame.getEncodedText());
                }
            }
        } catch (IOException var7) {
            Logger.getLogger("MP4 API").log(Level.SEVERE, "Exception in MetaData.parseID3: {0}", var7.toString());
        }
    }

    private void parseNeroTags(NeroMetadataTagsBox tags) {
        Map<String, String> pairs = tags.getPairs();
        for (String key : pairs.keySet()) {
            String val = (String) pairs.get(key);
            try {
                if (key.equals(NERO_TAGS[0])) {
                    this.put(MetaData.Field.ARTIST, val);
                }
                if (key.equals(NERO_TAGS[1])) {
                    this.put(MetaData.Field.TITLE, val);
                }
                if (key.equals(NERO_TAGS[2])) {
                    this.put(MetaData.Field.ALBUM, val);
                }
                if (key.equals(NERO_TAGS[3])) {
                    this.put(MetaData.Field.TRACK_NUMBER, Integer.parseInt(val));
                }
                if (key.equals(NERO_TAGS[4])) {
                    this.put(MetaData.Field.TOTAL_TRACKS, Integer.parseInt(val));
                }
                if (key.equals(NERO_TAGS[5])) {
                    Calendar c = Calendar.getInstance();
                    c.set(1, Integer.parseInt(val));
                    this.put(MetaData.Field.RELEASE_DATE, c.getTime());
                }
                if (key.equals(NERO_TAGS[6])) {
                    this.put(MetaData.Field.GENRE, val);
                }
                if (key.equals(NERO_TAGS[7])) {
                    this.put(MetaData.Field.DISK_NUMBER, Integer.parseInt(val));
                }
                if (key.equals(NERO_TAGS[8])) {
                    this.put(MetaData.Field.TOTAL_DISKS, Integer.parseInt(val));
                }
                if (key.equals(NERO_TAGS[9])) {
                }
                if (key.equals(NERO_TAGS[10])) {
                    this.put(MetaData.Field.COPYRIGHT, val);
                }
                if (key.equals(NERO_TAGS[11])) {
                    this.put(MetaData.Field.COMMENTS, val);
                }
                if (key.equals(NERO_TAGS[12])) {
                    this.put(MetaData.Field.LYRICS, val);
                }
                if (key.equals(NERO_TAGS[13])) {
                }
                if (key.equals(NERO_TAGS[14])) {
                    this.put(MetaData.Field.RATING, Integer.parseInt(val));
                }
                if (key.equals(NERO_TAGS[15])) {
                    this.put(MetaData.Field.PUBLISHER, val);
                }
                if (key.equals(NERO_TAGS[16])) {
                    this.put(MetaData.Field.COMPOSER, val);
                }
                if (key.equals(NERO_TAGS[17])) {
                }
                if (key.equals(NERO_TAGS[18])) {
                }
                if (key.equals(NERO_TAGS[19])) {
                    this.put(MetaData.Field.TEMPO, Integer.parseInt(val));
                }
            } catch (NumberFormatException var7) {
                Logger.getLogger("MP4 API").log(Level.SEVERE, "Exception in MetaData.parseNeroTags: {0}", var7.toString());
            }
        }
    }

    private <T> void put(MetaData.Field<T> field, T value) {
        this.contents.put(field, value);
    }

    boolean containsMetaData() {
        return !this.contents.isEmpty();
    }

    public <T> T get(MetaData.Field<T> field) {
        return (T) this.contents.get(field);
    }

    public Map<MetaData.Field<?>, Object> getAll() {
        return Collections.unmodifiableMap(this.contents);
    }

    public static class Field<T> {

        public static final MetaData.Field<String> ARTIST = new MetaData.Field<>("Artist");

        public static final MetaData.Field<String> TITLE = new MetaData.Field<>("Title");

        public static final MetaData.Field<String> ALBUM_ARTIST = new MetaData.Field<>("Album Artist");

        public static final MetaData.Field<String> ALBUM = new MetaData.Field<>("Album");

        public static final MetaData.Field<Integer> TRACK_NUMBER = new MetaData.Field<>("Track Number");

        public static final MetaData.Field<Integer> TOTAL_TRACKS = new MetaData.Field<>("Total Tracks");

        public static final MetaData.Field<Integer> DISK_NUMBER = new MetaData.Field<>("Disk Number");

        public static final MetaData.Field<Integer> TOTAL_DISKS = new MetaData.Field<>("Total disks");

        public static final MetaData.Field<String> COMPOSER = new MetaData.Field<>("Composer");

        public static final MetaData.Field<String> COMMENTS = new MetaData.Field<>("Comments");

        public static final MetaData.Field<Integer> TEMPO = new MetaData.Field<>("Tempo");

        public static final MetaData.Field<Integer> LENGTH_IN_MILLISECONDS = new MetaData.Field<>("Length in milliseconds");

        public static final MetaData.Field<Date> RELEASE_DATE = new MetaData.Field<>("Release Date");

        public static final MetaData.Field<String> GENRE = new MetaData.Field<>("Genre");

        public static final MetaData.Field<String> ENCODER_NAME = new MetaData.Field<>("Encoder Name");

        public static final MetaData.Field<String> ENCODER_TOOL = new MetaData.Field<>("Encoder Tool");

        public static final MetaData.Field<Date> ENCODING_DATE = new MetaData.Field<>("Encoding Date");

        public static final MetaData.Field<String> COPYRIGHT = new MetaData.Field<>("Copyright");

        public static final MetaData.Field<String> PUBLISHER = new MetaData.Field<>("Publisher");

        public static final MetaData.Field<Boolean> COMPILATION = new MetaData.Field<>("Part of compilation");

        public static final MetaData.Field<List<Artwork>> COVER_ARTWORKS = new MetaData.Field<>("Cover Artworks");

        public static final MetaData.Field<String> GROUPING = new MetaData.Field<>("Grouping");

        public static final MetaData.Field<String> LOCATION = new MetaData.Field<>("Location");

        public static final MetaData.Field<String> LYRICS = new MetaData.Field<>("Lyrics");

        public static final MetaData.Field<Integer> RATING = new MetaData.Field<>("Rating");

        public static final MetaData.Field<Integer> PODCAST = new MetaData.Field<>("Podcast");

        public static final MetaData.Field<String> PODCAST_URL = new MetaData.Field<>("Podcast URL");

        public static final MetaData.Field<String> CATEGORY = new MetaData.Field<>("Category");

        public static final MetaData.Field<String> KEYWORDS = new MetaData.Field<>("Keywords");

        public static final MetaData.Field<Integer> EPISODE_GLOBAL_UNIQUE_ID = new MetaData.Field<>("Episode Global Unique ID");

        public static final MetaData.Field<String> DESCRIPTION = new MetaData.Field<>("Description");

        public static final MetaData.Field<String> TV_SHOW = new MetaData.Field<>("TV Show");

        public static final MetaData.Field<String> TV_NETWORK = new MetaData.Field<>("TV Network");

        public static final MetaData.Field<String> TV_EPISODE = new MetaData.Field<>("TV Episode");

        public static final MetaData.Field<Integer> TV_EPISODE_NUMBER = new MetaData.Field<>("TV Episode Number");

        public static final MetaData.Field<Integer> TV_SEASON = new MetaData.Field<>("TV Season");

        public static final MetaData.Field<String> INTERNET_RADIO_STATION = new MetaData.Field<>("Internet Radio Station");

        public static final MetaData.Field<String> PURCHASE_DATE = new MetaData.Field<>("Purchase Date");

        public static final MetaData.Field<String> GAPLESS_PLAYBACK = new MetaData.Field<>("Gapless Playback");

        public static final MetaData.Field<Boolean> HD_VIDEO = new MetaData.Field<>("HD Video");

        public static final MetaData.Field<Locale> LANGUAGE = new MetaData.Field<>("Language");

        public static final MetaData.Field<String> ARTIST_SORT_TEXT = new MetaData.Field<>("Artist Sort Text");

        public static final MetaData.Field<String> TITLE_SORT_TEXT = new MetaData.Field<>("Title Sort Text");

        public static final MetaData.Field<String> ALBUM_SORT_TEXT = new MetaData.Field<>("Album Sort Text");

        private String name;

        private Field(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}