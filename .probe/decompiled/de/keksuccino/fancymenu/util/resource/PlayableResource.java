package de.keksuccino.fancymenu.util.resource;

public interface PlayableResource extends Resource {

    void play();

    boolean isPlaying();

    void pause();

    boolean isPaused();

    void stop();
}