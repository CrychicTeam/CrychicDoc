package info.journeymap.shaded.ar.com.hjg.pngj.chunks;

import info.journeymap.shaded.ar.com.hjg.pngj.ImageInfo;
import info.journeymap.shaded.ar.com.hjg.pngj.PngjException;

public class PngChunkICCP extends PngChunkSingle {

    public static final String ID = "iCCP";

    private String profileName;

    private byte[] compressedProfile;

    public PngChunkICCP(ImageInfo info) {
        super("iCCP", info);
    }

    @Override
    public PngChunk.ChunkOrderingConstraint getOrderingConstraint() {
        return PngChunk.ChunkOrderingConstraint.BEFORE_PLTE_AND_IDAT;
    }

    @Override
    public ChunkRaw createRawChunk() {
        ChunkRaw c = this.createEmptyChunk(this.profileName.length() + this.compressedProfile.length + 2, true);
        System.arraycopy(ChunkHelper.toBytes(this.profileName), 0, c.data, 0, this.profileName.length());
        c.data[this.profileName.length()] = 0;
        c.data[this.profileName.length() + 1] = 0;
        System.arraycopy(this.compressedProfile, 0, c.data, this.profileName.length() + 2, this.compressedProfile.length);
        return c;
    }

    @Override
    public void parseFromRaw(ChunkRaw chunk) {
        int pos0 = ChunkHelper.posNullByte(chunk.data);
        this.profileName = ChunkHelper.toString(chunk.data, 0, pos0);
        int comp = chunk.data[pos0 + 1] & 255;
        if (comp != 0) {
            throw new PngjException("bad compression for ChunkTypeICCP");
        } else {
            int compdatasize = chunk.data.length - (pos0 + 2);
            this.compressedProfile = new byte[compdatasize];
            System.arraycopy(chunk.data, pos0 + 2, this.compressedProfile, 0, compdatasize);
        }
    }

    public void setProfileNameAndContent(String name, byte[] profile) {
        this.profileName = name;
        this.compressedProfile = ChunkHelper.compressBytes(profile, true);
    }

    public void setProfileNameAndContent(String name, String profile) {
        this.setProfileNameAndContent(name, ChunkHelper.toBytes(profile));
    }

    public String getProfileName() {
        return this.profileName;
    }

    public byte[] getProfile() {
        return ChunkHelper.compressBytes(this.compressedProfile, false);
    }

    public String getProfileAsString() {
        return ChunkHelper.toString(this.getProfile());
    }
}