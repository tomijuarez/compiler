package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.WrapperCode;

/**
 * Created by tomi on 16/10/17.
 */
public class CountNewLine implements SemanticAction {

    public CountNewLine() {
        //BLANK.
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        code.addLine();

        return null;
    }
}
