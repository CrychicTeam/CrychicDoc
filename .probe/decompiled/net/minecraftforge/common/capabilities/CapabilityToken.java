package net.minecraftforge.common.capabilities;

public abstract class CapabilityToken<T> {

    protected final String getType() {
        throw new RuntimeException("This will be implemented by a transformer");
    }

    public String toString() {
        return "CapabilityToken[" + this.getType() + "]";
    }
}