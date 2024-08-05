package com.github.alexthe666.citadel.repack.jaad;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4Container;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.MetaData;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Movie;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Protection;
import com.github.alexthe666.citadel.repack.jaad.mp4.api.Track;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

public class MP4Info {

    private static final String USAGE = "usage:\nnet.sourceforge.jaad.MP4Info [options] <infile>\n\n\t-b\talso print all boxes";

    public static void main(String[] args) {
        try {
            if (args.length < 1) {
                printUsage();
            } else {
                boolean boxes = false;
                String file;
                if (args.length > 1) {
                    if (args[0].equals("-b")) {
                        boxes = true;
                    } else {
                        printUsage();
                    }
                    file = args[1];
                } else {
                    file = args[0];
                }
                MP4Container cont = new MP4Container(new RandomAccessFile(file, "r"));
                Movie movie = cont.getMovie();
                System.out.println("Movie:");
                List<Track> tracks = movie.getTracks();
                for (int i = 0; i < tracks.size(); i++) {
                    Track t = (Track) tracks.get(i);
                    System.out.println("\tTrack " + i + ": " + t.getCodec() + " (language: " + t.getLanguage() + ", created: " + t.getCreationTime() + ")");
                    Protection p = t.getProtection();
                    if (p != null) {
                        System.out.println("\t\tprotection: " + p.getScheme());
                    }
                }
                if (movie.containsMetaData()) {
                    System.out.println("\tMetadata:");
                    Map<MetaData.Field<?>, Object> data = movie.getMetaData().getAll();
                    for (MetaData.Field<?> key : data.keySet()) {
                        if (key.equals(MetaData.Field.COVER_ARTWORKS)) {
                            List<?> l = (List<?>) data.get(MetaData.Field.COVER_ARTWORKS);
                            System.out.println("\t\t" + l.size() + " Cover Artworks present");
                        } else {
                            System.out.println("\t\t" + key.getName() + " = " + data.get(key));
                        }
                    }
                }
                List<Protection> protections = movie.getProtections();
                if (protections.size() > 0) {
                    System.out.println("\tprotections:");
                    for (Protection p : protections) {
                        System.out.println("\t\t" + p.getScheme());
                    }
                }
                if (boxes) {
                    System.out.println("================================");
                    for (Box box : cont.getBoxes()) {
                        printBox(box, 0);
                    }
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
            System.err.println("error while reading file: " + var11.toString());
        }
    }

    private static void printUsage() {
        System.out.println("usage:\nnet.sourceforge.jaad.MP4Info [options] <infile>\n\n\t-b\talso print all boxes");
        System.exit(1);
    }

    private static void printBox(Box box, int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("  ");
        }
        sb.append(box.toString());
        System.out.println(sb.toString());
        for (Box child : box.getChildren()) {
            printBox(child, level + 1);
        }
    }
}