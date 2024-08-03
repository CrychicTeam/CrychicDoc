package me.lucko.spark.lib.bytesocks.ws.extensions;

import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidDataException;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidFrameException;
import me.lucko.spark.lib.bytesocks.ws.framing.ControlFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.DataFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.Framedata;

public abstract class CompressionExtension extends DefaultExtension {

    @Override
    public void isFrameValid(Framedata inputFrame) throws InvalidDataException {
        if (!(inputFrame instanceof DataFrame) || !inputFrame.isRSV2() && !inputFrame.isRSV3()) {
            if (inputFrame instanceof ControlFrame && (inputFrame.isRSV1() || inputFrame.isRSV2() || inputFrame.isRSV3())) {
                throw new InvalidFrameException("bad rsv RSV1: " + inputFrame.isRSV1() + " RSV2: " + inputFrame.isRSV2() + " RSV3: " + inputFrame.isRSV3());
            }
        } else {
            throw new InvalidFrameException("bad rsv RSV1: " + inputFrame.isRSV1() + " RSV2: " + inputFrame.isRSV2() + " RSV3: " + inputFrame.isRSV3());
        }
    }
}