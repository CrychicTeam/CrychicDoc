package snownee.jade.api;

public interface IWailaPlugin {

    default void register(IWailaCommonRegistration registration) {
    }

    default void registerClient(IWailaClientRegistration registration) {
    }
}