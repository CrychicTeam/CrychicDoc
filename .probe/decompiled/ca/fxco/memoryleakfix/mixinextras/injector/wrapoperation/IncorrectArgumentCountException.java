package ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation;

class IncorrectArgumentCountException extends RuntimeException {

    IncorrectArgumentCountException(String message) {
        super(message);
    }
}