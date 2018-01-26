package Lexer.TransitionMatrix.SemanticActions;

import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class SkipLastSymbol implements SemanticAction {

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        System.out.println("Error léxico en la línea "+code.getLine()+": símbolo no reconocido. Descartando " + lexeme.charAt(lexeme.length() - 1));
        code.addError();
        //Agrego espacio para forzar la generación del token.
        code.setCode(""+ lexeme.substring(0, lexeme.length()-2) +code.getCode());
        return null;
    }

}
