package net.minecraft.server;

import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class ChainedJsonException extends IOException {

    private final List<ChainedJsonException.Entry> entries = Lists.newArrayList();

    private final String message;

    public ChainedJsonException(String string0) {
        this.entries.add(new ChainedJsonException.Entry());
        this.message = string0;
    }

    public ChainedJsonException(String string0, Throwable throwable1) {
        super(throwable1);
        this.entries.add(new ChainedJsonException.Entry());
        this.message = string0;
    }

    public void prependJsonKey(String string0) {
        ((ChainedJsonException.Entry) this.entries.get(0)).addJsonKey(string0);
    }

    public void setFilenameAndFlush(String string0) {
        ((ChainedJsonException.Entry) this.entries.get(0)).filename = string0;
        this.entries.add(0, new ChainedJsonException.Entry());
    }

    public String getMessage() {
        return "Invalid " + this.entries.get(this.entries.size() - 1) + ": " + this.message;
    }

    public static ChainedJsonException forException(Exception exception0) {
        if (exception0 instanceof ChainedJsonException) {
            return (ChainedJsonException) exception0;
        } else {
            String $$1 = exception0.getMessage();
            if (exception0 instanceof FileNotFoundException) {
                $$1 = "File not found";
            }
            return new ChainedJsonException($$1, exception0);
        }
    }

    public static class Entry {

        @Nullable
        String filename;

        private final List<String> jsonKeys = Lists.newArrayList();

        Entry() {
        }

        void addJsonKey(String string0) {
            this.jsonKeys.add(0, string0);
        }

        @Nullable
        public String getFilename() {
            return this.filename;
        }

        public String getJsonKeys() {
            return StringUtils.join(this.jsonKeys, "->");
        }

        public String toString() {
            if (this.filename != null) {
                return this.jsonKeys.isEmpty() ? this.filename : this.filename + " " + this.getJsonKeys();
            } else {
                return this.jsonKeys.isEmpty() ? "(Unknown file)" : "(Unknown file) " + this.getJsonKeys();
            }
        }
    }
}