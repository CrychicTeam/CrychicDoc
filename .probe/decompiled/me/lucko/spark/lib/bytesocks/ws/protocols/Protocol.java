package me.lucko.spark.lib.bytesocks.ws.protocols;

import java.util.regex.Pattern;

public class Protocol implements IProtocol {

    private static final Pattern patternSpace = Pattern.compile(" ");

    private static final Pattern patternComma = Pattern.compile(",");

    private final String providedProtocol;

    public Protocol(String providedProtocol) {
        if (providedProtocol == null) {
            throw new IllegalArgumentException();
        } else {
            this.providedProtocol = providedProtocol;
        }
    }

    @Override
    public boolean acceptProvidedProtocol(String inputProtocolHeader) {
        if ("".equals(this.providedProtocol)) {
            return true;
        } else {
            String protocolHeader = patternSpace.matcher(inputProtocolHeader).replaceAll("");
            String[] headers = patternComma.split(protocolHeader);
            for (String header : headers) {
                if (this.providedProtocol.equals(header)) {
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public String getProvidedProtocol() {
        return this.providedProtocol;
    }

    @Override
    public IProtocol copyInstance() {
        return new Protocol(this.getProvidedProtocol());
    }

    @Override
    public String toString() {
        return this.getProvidedProtocol();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Protocol protocol = (Protocol) o;
            return this.providedProtocol.equals(protocol.providedProtocol);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.providedProtocol.hashCode();
    }
}