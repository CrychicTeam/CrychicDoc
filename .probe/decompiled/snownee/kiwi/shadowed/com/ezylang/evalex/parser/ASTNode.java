package snownee.kiwi.shadowed.com.ezylang.evalex.parser;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Generated;

public final class ASTNode {

    private final List<ASTNode> parameters;

    private final Token token;

    public ASTNode(Token token, ASTNode... parameters) {
        this.token = token;
        this.parameters = Arrays.asList(parameters);
    }

    public String toJSON() {
        if (this.parameters.isEmpty()) {
            return String.format("{\"type\":\"%s\",\"value\":\"%s\"}", this.token.getType(), this.token.getValue());
        } else {
            String childrenJson = (String) this.parameters.stream().map(ASTNode::toJSON).collect(Collectors.joining(","));
            return String.format("{\"type\":\"%s\",\"value\":\"%s\",\"children\":[%s]}", this.token.getType(), this.token.getValue(), childrenJson);
        }
    }

    @Generated
    public List<ASTNode> getParameters() {
        return this.parameters;
    }

    @Generated
    public Token getToken() {
        return this.token;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ASTNode)) {
            return false;
        } else {
            ASTNode other = (ASTNode) o;
            Object this$parameters = this.getParameters();
            Object other$parameters = other.getParameters();
            if (this$parameters == null ? other$parameters == null : this$parameters.equals(other$parameters)) {
                Object this$token = this.getToken();
                Object other$token = other.getToken();
                return this$token == null ? other$token == null : this$token.equals(other$token);
            } else {
                return false;
            }
        }
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $parameters = this.getParameters();
        result = result * 59 + ($parameters == null ? 43 : $parameters.hashCode());
        Object $token = this.getToken();
        return result * 59 + ($token == null ? 43 : $token.hashCode());
    }

    @Generated
    public String toString() {
        return "ASTNode(parameters=" + this.getParameters() + ", token=" + this.getToken() + ")";
    }
}