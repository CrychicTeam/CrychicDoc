package net.raphimc.immediatelyfastapi;

public class ImmediatelyFastApi {

    private static ApiAccess API_IMPL = null;

    public static void setApiImpl(ApiAccess impl) {
        if (API_IMPL != null) {
            throw new IllegalStateException("API_IMPL has already been set");
        } else {
            API_IMPL = impl;
        }
    }

    public static ApiAccess getApiImpl() {
        if (API_IMPL == null) {
            throw new IllegalStateException("The API has not been initialized yet");
        } else {
            return API_IMPL;
        }
    }
}