package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class GenerateOperator implements SemanticAction {
    public GenerateOperator() {
        //Blank.
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        return new Token((int) (code.getBuffer().charAt(0)), String.valueOf(code.getBuffer().charAt(0)));
    }
}