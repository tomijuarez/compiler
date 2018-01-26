package Lexer;

public class Token {
    private String lexeme;
    private int idToken;
    private int positionInSymbolTable = 0;

    public Token(int idToken, String lexeme) {
        this.lexeme = lexeme;
        this.idToken = idToken;
    }

    public void setPositionInSymbolTable(int pos) {
        this.positionInSymbolTable = pos;
    }

    public int getPositionInSymbolTable() {
        return this.positionInSymbolTable;
    }

    public String getLexeme() { return lexeme; }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public int getIdToken(){
        return idToken;
    }
}