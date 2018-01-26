package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class NoAction implements SemanticAction {
    @Override
    public Token execute(WrapperCode code, String buffer) {
        if (buffer.length() > 0)
            if (buffer.charAt(0) == '\n')
                code.addLine();
        return null;
    }
}