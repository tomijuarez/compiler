package Lexer.TransitionMatrix.SemanticActions;

import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

import java.util.List;

/**
 * Created by brian on 18/09/17.
 */
public class GenerateULONG implements SemanticAction {
    private int tokenId;
    private SymbolTable symbolTable;

    public GenerateULONG(int tokenId, SymbolTable symbolTable) {
        this.tokenId = tokenId;
        this.symbolTable = symbolTable;
    }

    @Override
    public Token execute(WrapperCode code, String buffer) {
        Token token = null;
        long num = Long.valueOf(buffer.trim());
        if(num>=0L && num <= (long)Math.pow(2,32)-1) {
            token = new Token(this.tokenId, buffer);
            Symbol symbol = new Symbol(token,false);
            symbol.setType("ulong");

            List<Symbol> symbols = this.symbolTable.getSymbols(buffer.toLowerCase());
            if (symbols != null && symbols.size() > 0)
                return token;

            this.symbolTable.addSymbol(buffer, symbol);
        }
        else {
            code.addError();
            System.out.println("Error léxico en la línea "+code.getLine()+": el número largo sin signo " + buffer + " se encuentra fuera del rango válido. Descartando buffer.");
        }

        return token;
    }
}