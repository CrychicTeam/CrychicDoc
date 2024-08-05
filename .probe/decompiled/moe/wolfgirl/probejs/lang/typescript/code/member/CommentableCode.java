package moe.wolfgirl.probejs.lang.typescript.code.member;

import java.util.ArrayList;
import java.util.List;
import moe.wolfgirl.probejs.lang.typescript.Declaration;
import moe.wolfgirl.probejs.lang.typescript.code.Code;

public abstract class CommentableCode extends Code {

    public final List<String> comments = new ArrayList();

    public List<String> formatComments() {
        List<String> formatted = new ArrayList();
        formatted.add("/**");
        for (String comment : this.comments) {
            formatted.add(" * %s".formatted(comment));
        }
        formatted.add(" */");
        return formatted;
    }

    public abstract List<String> formatRaw(Declaration declaration);

    @Override
    public final List<String> format(Declaration declaration) {
        if (this.comments.size() == 0) {
            return this.formatRaw(declaration);
        } else {
            List<String> result = new ArrayList(this.formatComments());
            result.addAll(this.formatRaw(declaration));
            return result;
        }
    }

    public void addComment(String... comments) {
        for (String comment : comments) {
            this.comments.addAll(List.of(comment.split("\\n")));
        }
    }

    public void addCommentAtStart(String... comments) {
        List<String> lines = new ArrayList();
        for (String comment : comments) {
            lines.addAll(List.of(comment.split("\\n")));
        }
        this.comments.addAll(0, lines);
    }

    public void linebreak() {
        this.comments.add("");
    }

    public void newline(String... comments) {
        this.comments.add("");
        this.addComment(comments);
    }
}