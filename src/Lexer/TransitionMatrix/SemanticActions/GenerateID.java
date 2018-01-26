package Lexer.TransitionMatrix.SemanticActions;

import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.Token;
import Lexer.WrapperCode;

import java.util.List;

public class GenerateID implements SemanticAction {
    private SymbolTable symbolTable;
    private int tokenId;
    private static final int MAX_ID = 15;

    public GenerateID(int tokenId, SymbolTable symbolTable) {
        this.tokenId = tokenId;
        this.symbolTable = symbolTable;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme) {
        Token token;
        String newLexeme = lexeme;

        if (newLexeme.length() > MAX_ID) {
            System.out.println("Warning léxico en la línea "+code.getLine()+": el identificador " + newLexeme + " supera el máximo de caracteres permitidos con lo cual se truncará a "+MAX_ID +" caracteres.");
            newLexeme = newLexeme.substring(0, MAX_ID);
        }

        List<Symbol> symbols = this.symbolTable.getSymbols(newLexeme.toLowerCase());
        if (symbols != null && symbols.size() > 0) {
            if (symbols.get(0).isKeyword()) {
                return new Token(symbols.get(0).getToken().getIdToken(), newLexeme);
            }
            return new Token(this.tokenId, newLexeme);
        }

        //No está en la tabla de símbolos.
        token = new Token(this.tokenId, newLexeme);
        this.symbolTable.addSymbol(newLexeme, new Symbol(token,false));
        return token;
    }
}
