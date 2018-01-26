package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.WrapperCode;

public interface SemanticAction {
    Token execute(WrapperCode code, String lexeme);
}