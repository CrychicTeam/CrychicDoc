package com.github.alexthe666.citadel.repack.jcodec.movtool;

import com.github.alexthe666.citadel.repack.jcodec.containers.mp4.boxes.MovieBox;
import java.io.File;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QTEdit {

    protected final QTEdit.EditFactory[] factories;

    private final List<Flatten.ProgressListener> listeners = new ArrayList();

    public QTEdit(QTEdit.EditFactory[] editFactories) {
        this.factories = editFactories;
    }

    public void addProgressListener(Flatten.ProgressListener listener) {
        this.listeners.add(listener);
    }

    public void execute(String[] args) throws Exception {
        LinkedList<String> aa = new LinkedList(Arrays.asList(args));
        List<MP4Edit> commands = new LinkedList();
        label45: while (aa.size() > 0) {
            int i = 0;
            while (true) {
                if (i < this.factories.length) {
                    if (!((String) aa.get(0)).equals(this.factories[i].getName())) {
                        i++;
                        continue;
                    }
                    aa.remove(0);
                    try {
                        commands.add(this.factories[i].parseArgs(aa));
                    } catch (Exception var6) {
                        System.err.println("ERROR: " + var6.getMessage());
                        return;
                    }
                }
                if (i == this.factories.length) {
                    break label45;
                }
                break;
            }
        }
        if (aa.size() == 0) {
            System.err.println("ERROR: A movie file should be specified");
            this.help();
        }
        if (commands.size() == 0) {
            System.err.println("ERROR: At least one command should be specified");
            this.help();
        }
        File input = new File((String) aa.remove(0));
        if (!input.exists()) {
            System.err.println("ERROR: Input file '" + input.getAbsolutePath() + "' doesn't exist");
            this.help();
        }
        new ReplaceMP4Editor().replace(input, new CompoundMP4Edit(commands));
    }

    protected void help() {
        System.out.println("Quicktime movie editor");
        System.out.println("Syntax: qtedit <command1> <options> ... <commandN> <options> <movie>");
        System.out.println("Where options:");
        for (QTEdit.EditFactory commandFactory : this.factories) {
            System.out.println("\t" + commandFactory.getHelp());
        }
        System.exit(-1);
    }

    public abstract static class BaseCommand implements MP4Edit {

        public void applyRefs(MovieBox movie, FileChannel[][] refs) {
            this.apply(movie);
        }

        @Override
        public abstract void apply(MovieBox var1);
    }

    public interface EditFactory {

        String getName();

        MP4Edit parseArgs(List<String> var1);

        String getHelp();
    }
}