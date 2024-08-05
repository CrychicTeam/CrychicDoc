package dev.latvian.mods.rhino.ast;

import java.util.SortedSet;
import java.util.TreeSet;

public class AstRoot extends ScriptNode {

    private SortedSet<Comment> comments;

    public AstRoot() {
        this.type = 137;
    }

    public AstRoot(int pos) {
        super(pos);
        this.type = 137;
    }

    public SortedSet<Comment> getComments() {
        return this.comments;
    }

    public void setComments(SortedSet<Comment> comments) {
        if (comments == null) {
            this.comments = null;
        } else {
            if (this.comments != null) {
                this.comments.clear();
            }
            for (Comment c : comments) {
                this.addComment(c);
            }
        }
    }

    public void addComment(Comment comment) {
        this.assertNotNull(comment);
        if (this.comments == null) {
            this.comments = new TreeSet(new AstNode.PositionComparator());
        }
        this.comments.add(comment);
        comment.setParent(this);
    }
}