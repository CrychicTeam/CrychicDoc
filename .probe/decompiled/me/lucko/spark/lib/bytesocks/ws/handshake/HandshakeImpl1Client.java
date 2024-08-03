package me.lucko.spark.lib.bytesocks.ws.handshake;

public class HandshakeImpl1Client extends HandshakedataImpl1 implements ClientHandshakeBuilder {

    private String resourceDescriptor = "*";

    @Override
    public void setResourceDescriptor(String resourceDescriptor) {
        if (resourceDescriptor == null) {
            throw new IllegalArgumentException("http resource descriptor must not be null");
        } else {
            this.resourceDescriptor = resourceDescriptor;
        }
    }

    @Override
    public String getResourceDescriptor() {
        return this.resourceDescriptor;
    }
}