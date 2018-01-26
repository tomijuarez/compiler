package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class DeleteCharacter implements SemanticAction {

    private SemanticAction generator;

    public DeleteCharacter(SemanticAction generator) {
        this.generator = generator;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        char currentChar = lexeme.charAt(lexeme.length() - 1);

        code.setCode(currentChar + code.getCode());

        return this.generator.execute(
                code,
                lexeme.substring(0, lexeme.length()-1)
        );
    }
}
