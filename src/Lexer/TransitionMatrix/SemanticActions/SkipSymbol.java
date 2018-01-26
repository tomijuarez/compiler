package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.WrapperCode;

/**
 * Created by tomi on 27/09/17.
 */
public class SkipSymbol implements SemanticAction {

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        System.out.println("Error léxico en la línea "+code.getLine()+": símbolo no reconocido o no aceptado en este contexto. Descartando " + lexeme);
        code.addError();
        code.setBuffer("");
        if (lexeme.charAt(lexeme.length() - 1) == '\n')
            code.addLine();

        return null;
    }

}
