package me.lucko.spark.lib.asm.signature;

public class SignatureReader {

    private final String signatureValue;

    public SignatureReader(String signature) {
        this.signatureValue = signature;
    }

    public void accept(SignatureVisitor signatureVistor) {
        String signature = this.signatureValue;
        int length = signature.length();
        int offset;
        if (signature.charAt(0) == '<') {
            offset = 2;
            char currentChar;
            do {
                int classBoundStartOffset = signature.indexOf(58, offset);
                signatureVistor.visitFormalTypeParameter(signature.substring(offset - 1, classBoundStartOffset));
                offset = classBoundStartOffset + 1;
                currentChar = signature.charAt(offset);
                if (currentChar == 'L' || currentChar == '[' || currentChar == 'T') {
                    offset = parseType(signature, offset, signatureVistor.visitClassBound());
                }
                while ((currentChar = signature.charAt(offset++)) == ':') {
                    offset = parseType(signature, offset, signatureVistor.visitInterfaceBound());
                }
            } while (currentChar != '>');
        } else {
            offset = 0;
        }
        if (signature.charAt(offset) == '(') {
            offset++;
            while (signature.charAt(offset) != ')') {
                offset = parseType(signature, offset, signatureVistor.visitParameterType());
            }
            offset = parseType(signature, offset + 1, signatureVistor.visitReturnType());
            while (offset < length) {
                offset = parseType(signature, offset + 1, signatureVistor.visitExceptionType());
            }
        } else {
            offset = parseType(signature, offset, signatureVistor.visitSuperclass());
            while (offset < length) {
                offset = parseType(signature, offset, signatureVistor.visitInterface());
            }
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        parseType(this.signatureValue, 0, signatureVisitor);
    }

    private static int parseType(String signature, int startOffset, SignatureVisitor signatureVisitor) {
        int offset = startOffset + 1;
        char currentChar = signature.charAt(startOffset);
        switch(currentChar) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'V':
            case 'Z':
                signatureVisitor.visitBaseType(currentChar);
                return offset;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new IllegalArgumentException();
            case 'L':
                int start = offset;
                boolean visited = false;
                boolean inner = false;
                while (true) {
                    currentChar = signature.charAt(offset++);
                    if (currentChar != '.' && currentChar != ';') {
                        if (currentChar == '<') {
                            String name = signature.substring(start, offset - 1);
                            if (inner) {
                                signatureVisitor.visitInnerClassType(name);
                            } else {
                                signatureVisitor.visitClassType(name);
                            }
                            visited = true;
                            while ((currentChar = signature.charAt(offset)) != '>') {
                                switch(currentChar) {
                                    case '*':
                                        offset++;
                                        signatureVisitor.visitTypeArgument();
                                        break;
                                    case '+':
                                    case '-':
                                        offset = parseType(signature, offset + 1, signatureVisitor.visitTypeArgument(currentChar));
                                        break;
                                    case ',':
                                    default:
                                        offset = parseType(signature, offset, signatureVisitor.visitTypeArgument('='));
                                }
                            }
                        }
                    } else {
                        if (!visited) {
                            String name = signature.substring(start, offset - 1);
                            if (inner) {
                                signatureVisitor.visitInnerClassType(name);
                            } else {
                                signatureVisitor.visitClassType(name);
                            }
                        }
                        if (currentChar == ';') {
                            signatureVisitor.visitEnd();
                            return offset;
                        }
                        start = offset;
                        visited = false;
                        inner = true;
                    }
                }
            case 'T':
                int endOffset = signature.indexOf(59, offset);
                signatureVisitor.visitTypeVariable(signature.substring(offset, endOffset));
                return endOffset + 1;
            case '[':
                return parseType(signature, offset, signatureVisitor.visitArrayType());
        }
    }
}