package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.WrapperCode;

public class OnlyFirstChar implements SemanticAction {

    private SemanticAction a;

    public OnlyFirstChar(SemanticAction a) {
        this.a = a;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        code.setCode(lexeme.substring(1, lexeme.length()) + code.getCode());
        code.setBuffer(String.valueOf(lexeme.charAt(0)));
        return this.a.execute(code, code.getBuffer());
    }
}
