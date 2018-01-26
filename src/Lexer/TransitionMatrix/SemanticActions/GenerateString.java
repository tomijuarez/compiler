package Lexer.TransitionMatrix.SemanticActions;

import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.WrapperCode;

import java.util.List;

public class GenerateString implements SemanticAction {

    private int tokenId;
    private SymbolTable symbolTable;

    public GenerateString(int tokenId, SymbolTable symbolTable) {
        this.tokenId = tokenId;
        this.symbolTable = symbolTable;
    }

    @Override
    public Token execute(WrapperCode code, String lexeme){
        Token token = new Token(this.tokenId, lexeme.replace("\"",""));
        Symbol symbol = new Symbol(token, false);
        symbol.setType("string");

        List<Symbol> symbols = this.symbolTable.getSymbols(lexeme.toLowerCase());
        if (symbols != null && symbols.size() > 0) {
            token.setPositionInSymbolTable(symbols.get(0).getPositionInSymbolTable());
            return token;
        }

        this.symbolTable.addSymbol(lexeme, symbol);
        return token;
    }
}