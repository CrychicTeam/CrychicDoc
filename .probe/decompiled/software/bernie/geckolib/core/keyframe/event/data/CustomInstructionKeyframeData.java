package software.bernie.geckolib.core.keyframe.event.data;

import java.util.Objects;

public class CustomInstructionKeyframeData extends KeyFrameData {

    private final String instructions;

    public CustomInstructionKeyframeData(double startTick, String instructions) {
        super(startTick);
        this.instructions = instructions;
    }

    public String getInstructions() {
        return this.instructions;
    }

    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.getStartTick(), this.instructions });
    }
}