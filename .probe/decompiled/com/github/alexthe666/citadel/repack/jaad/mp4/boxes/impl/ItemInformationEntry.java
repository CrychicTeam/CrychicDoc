package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class ItemInformationEntry extends FullBox {

    private int itemID;

    private int itemProtectionIndex;

    private String itemName;

    private String contentType;

    private String contentEncoding;

    private long extensionType;

    private ItemInformationEntry.Extension extension;

    public ItemInformationEntry() {
        super("Item Information Entry");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        if (this.version == 0 || this.version == 1) {
            this.itemID = (int) in.readBytes(2);
            this.itemProtectionIndex = (int) in.readBytes(2);
            this.itemName = in.readUTFString((int) this.getLeft(in), "UTF-8");
            this.contentType = in.readUTFString((int) this.getLeft(in), "UTF-8");
            this.contentEncoding = in.readUTFString((int) this.getLeft(in), "UTF-8");
        }
        if (this.version == 1 && this.getLeft(in) > 0L) {
            this.extensionType = in.readBytes(4);
            if (this.getLeft(in) > 0L) {
                this.extension = ItemInformationEntry.Extension.forType((int) this.extensionType);
                if (this.extension != null) {
                    this.extension.decode(in);
                }
            }
        }
    }

    public int getItemID() {
        return this.itemID;
    }

    public int getItemProtectionIndex() {
        return this.itemProtectionIndex;
    }

    public String getItemName() {
        return this.itemName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public long getExtensionType() {
        return this.extensionType;
    }

    public ItemInformationEntry.Extension getExtension() {
        return this.extension;
    }

    public abstract static class Extension {

        private static final int TYPE_FDEL = 1717855596;

        static ItemInformationEntry.Extension forType(int type) {
            return switch(type) {
                case 1717855596 ->
                    new ItemInformationEntry.FDExtension();
                default ->
                    null;
            };
        }

        abstract void decode(MP4InputStream var1) throws IOException;
    }

    public static class FDExtension extends ItemInformationEntry.Extension {

        private String contentLocation;

        private String contentMD5;

        private long contentLength;

        private long transferLength;

        private long[] groupID;

        @Override
        void decode(MP4InputStream in) throws IOException {
            this.contentLocation = in.readUTFString(100, "UTF-8");
            this.contentMD5 = in.readUTFString(100, "UTF-8");
            this.contentLength = in.readBytes(8);
            this.transferLength = in.readBytes(8);
            int entryCount = in.read();
            this.groupID = new long[entryCount];
            for (int i = 0; i < entryCount; i++) {
                this.groupID[i] = in.readBytes(4);
            }
        }

        public String getContentLocation() {
            return this.contentLocation;
        }

        public String getContentMD5() {
            return this.contentMD5;
        }

        public long getContentLength() {
            return this.contentLength;
        }

        public long getTransferLength() {
            return this.transferLength;
        }

        public long[] getGroupID() {
            return this.groupID;
        }
    }
}