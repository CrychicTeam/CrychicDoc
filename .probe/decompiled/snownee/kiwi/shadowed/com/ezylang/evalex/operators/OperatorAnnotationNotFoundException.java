package snownee.kiwi.shadowed.com.ezylang.evalex.operators;

public class OperatorAnnotationNotFoundException extends RuntimeException {

    public OperatorAnnotationNotFoundException(String className) {
        super("Operator annotation for '" + className + "' not found");
    }
}