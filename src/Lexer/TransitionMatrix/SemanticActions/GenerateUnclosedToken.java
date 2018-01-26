package Lexer.TransitionMatrix.SemanticActions;

import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

public class GenerateUnclosedToken implements SemanticAction {

    private SemanticAction sa;
    private char expectedSymbol;
    private String warning;

    public GenerateUnclosedToken(SemanticAction sa, char expectedSymbol, String warning) {
        this.sa = sa;
        this.expectedSymbol = expectedSymbol;
        this.warning = warning;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        System.out.println("Warning léxico en la línea "+code.getLine()+": " + this.warning);
        lexeme = (lexeme.length() > 0)
            ? lexeme.substring(0, lexeme.length() - 1) //Para quitar el EOF o '\u001A'
            : lexeme
            ;
        return (this.sa != null) ? this.sa.execute(code, lexeme + this.expectedSymbol) : null;
    }
}