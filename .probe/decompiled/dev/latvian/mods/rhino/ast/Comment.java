package dev.latvian.mods.rhino.ast;

import dev.latvian.mods.rhino.Token;

public class Comment extends AstNode {

    private String value;

    private Token.CommentType commentType;

    public Comment(int pos, int len, Token.CommentType type, String value) {
        super(pos, len);
        this.type = 162;
        this.commentType = type;
        this.value = value;
    }

    public Token.CommentType getCommentType() {
        return this.commentType;
    }

    public void setCommentType(Token.CommentType type) {
        this.commentType = type;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String commentString) {
        this.value = commentString;
        this.setLength(this.value.length());
    }
}