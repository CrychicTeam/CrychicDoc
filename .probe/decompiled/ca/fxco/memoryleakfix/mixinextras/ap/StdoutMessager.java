package ca.fxco.memoryleakfix.mixinextras.ap;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class StdoutMessager implements Messager {

    public void printMessage(Kind kind, CharSequence msg) {
        System.out.printf("[%s] %s%n", kind.name(), msg);
    }

    public void printMessage(Kind kind, CharSequence msg, Element e) {
        this.printMessage(kind, msg);
    }

    public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
        this.printMessage(kind, msg);
    }

    public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
        this.printMessage(kind, msg);
    }
}