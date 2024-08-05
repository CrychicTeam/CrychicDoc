package net.blay09.mods.balm.api.proxy;

import java.lang.reflect.InvocationTargetException;

public class SidedProxy<T> {

    private final String commonName;

    private final String clientName;

    private T proxy;

    public SidedProxy(String commonName, String clientName) {
        this.commonName = commonName;
        this.clientName = clientName;
    }

    public void resolveCommon() throws ProxyResolutionException {
        try {
            this.proxy = (T) Class.forName(this.commonName).getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | InstantiationException var2) {
            throw new ProxyResolutionException(var2);
        }
    }

    public void resolveClient() throws ProxyResolutionException {
        try {
            this.proxy = (T) Class.forName(this.clientName).getConstructor().newInstance();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException | InstantiationException var2) {
            throw new ProxyResolutionException(var2);
        }
    }

    public T get() {
        if (this.proxy == null) {
            throw new IllegalStateException("Tried to access proxy before it was resolved");
        } else {
            return this.proxy;
        }
    }
}