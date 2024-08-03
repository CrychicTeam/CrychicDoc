package icyllis.arc3d.compiler;

public enum SPIRVVersion {

    SPIRV_1_0(65536), SPIRV_1_3(66304), SPIRV_1_4(66560), SPIRV_1_5(66816), SPIRV_1_6(67072);

    public final int mVersionNumber;

    private SPIRVVersion(int versionNumber) {
        this.mVersionNumber = versionNumber;
    }

    public boolean isBefore(SPIRVVersion other) {
        return this.compareTo(other) < 0;
    }

    public boolean isAtLeast(SPIRVVersion other) {
        return this.compareTo(other) >= 0;
    }
}