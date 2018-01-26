package Lexer.TransitionMatrix.SemanticActions;

import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

import java.util.List;

public class GenerateFloat implements SemanticAction {
    private int tokenId;
    private SymbolTable symbolTable;

    public GenerateFloat(int tokenId, SymbolTable symbolTable) {
        this.tokenId = tokenId;
        this.symbolTable = symbolTable;
    }

    @Override
    public Token execute(WrapperCode code, String buffer) {
        Token token = null;
        float number = Float.valueOf(buffer.replace(",","."));
        if ((number > 1.17549435E-38f && number < 3.40282347E38f) || (number == 0.0f) ) {
            token = new Token(this.tokenId, buffer);
            Symbol symbol = new Symbol(token, false);
            symbol.setType("float");

            List<Symbol> symbols = this.symbolTable.getSymbols(buffer.toLowerCase());
            if (symbols != null && symbols.size() > 0) {
                //Ya está la constante. Actualizo las referencias.
                symbol.increaseRef();
                return token;
            }
            this.symbolTable.addSymbol(buffer, symbol);
        }
        else {
            code.addError();
            System.out.println("Error léxico en la línea "+code.getLine()+": el número flotante " + buffer + " se encuentra fuera del rango válido. Descartando buffer.");
        }

        return token;
    }
}
