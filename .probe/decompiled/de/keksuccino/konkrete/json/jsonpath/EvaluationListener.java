package de.keksuccino.konkrete.json.jsonpath;

public interface EvaluationListener {

    EvaluationListener.EvaluationContinuation resultFound(EvaluationListener.FoundResult var1);

    public static enum EvaluationContinuation {

        CONTINUE, ABORT
    }

    public interface FoundResult {

        int index();

        String path();

        Object result();
    }
}