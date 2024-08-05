package dev.kosmx.playerAnim.core.data.quarktool;

public interface Playable {

    int playForward(int var1) throws QuarkParsingError;

    int playBackward(int var1) throws QuarkParsingError;
}