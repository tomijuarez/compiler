package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class GenerateGreaterEqual implements SemanticAction {
    private int tokenId;

    public GenerateGreaterEqual(int tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        return new Token(this.tokenId, lexeme);
    }
}