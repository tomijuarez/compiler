package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class GenerateEqual implements SemanticAction {

    private int tokenId;

    public GenerateEqual(int tokenId) {
        this.tokenId = tokenId;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        return new Token(this.tokenId, lexeme);
    }
}