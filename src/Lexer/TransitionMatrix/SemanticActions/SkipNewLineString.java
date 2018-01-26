package Lexer.TransitionMatrix.SemanticActions;


import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class SkipNewLineString implements SemanticAction {

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        code.addLine();
        code.setBuffer(lexeme.substring(0,lexeme.length()-4));
        return null;
    }
}
