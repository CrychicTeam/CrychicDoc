package snownee.kiwi.shadowed.org.yaml.snakeyaml.comments;

import snownee.kiwi.shadowed.org.yaml.snakeyaml.error.Mark;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.events.CommentEvent;

public class CommentLine {

    private final Mark startMark;

    private final Mark endMark;

    private final String value;

    private final CommentType commentType;

    public CommentLine(CommentEvent event) {
        this(event.getStartMark(), event.getEndMark(), event.getValue(), event.getCommentType());
    }

    public CommentLine(Mark startMark, Mark endMark, String value, CommentType commentType) {
        this.startMark = startMark;
        this.endMark = endMark;
        this.value = value;
        this.commentType = commentType;
    }

    public Mark getEndMark() {
        return this.endMark;
    }

    public Mark getStartMark() {
        return this.startMark;
    }

    public CommentType getCommentType() {
        return this.commentType;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        return "<" + this.getClass().getName() + " (type=" + this.getCommentType() + ", value=" + this.getValue() + ")>";
    }
}