package Lexer.SymbolTable;

import Lexer.Token;

public class Symbol {
    /**
     * Esta información se conoce en tiempo de compilación.
     * El lexer reconoce tokens (lexema e id de token) y los asigna en la tabla de símbolos.
     */
    private Token token;
    /**
     * Esta información se conoce en tiempo de compilación.
     * En la etapa de análisis léxico se conoce si es una palabra reservada del lenguaje o no.
     */
    private boolean isKeyword;
    /**
     * Esta información se conoce en tiempo de compilación.
     * En la etapa de análisis léxico, en el bloque de declaraciones se conoce el tipo de cada variable
     *  y este no varía a lo largo del programa.
     */
    private String type;
    /**
     * Esta información se conoce en runtime.
     * Los valores asociados e un identificador puede no estar asignado a la hora de la compilación
     *  o bien el valor asignado no es una constante, sino que depende de otras variables.
     */
    private Object value;

    private int positionInSymbolTable = 0;

    private int references = 1;

    private boolean declared = false;

    private boolean isInDataSegment = false;

    private boolean isAuxVar = false;

    public void setIsAuxVar() {
        this.isAuxVar = true;
    }

    public boolean isAuxVar() {
        return this.isAuxVar;
    }

    public void setPositionInSymbolTable(int pos) {
        this.positionInSymbolTable = pos;
        this.token.setPositionInSymbolTable(pos);
    }

    public int getPositionInSymbolTable() {
        return this.positionInSymbolTable;
    }

    public Symbol(Token token, boolean isKeyword) {
        this.token = token;
        this.isKeyword = isKeyword;
        this.type = "";
    }

    public boolean isInDataSegment() {
        return this.isInDataSegment;
    }

    public void setIsInDataSegment() {
        this.isInDataSegment = true;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isKeyword() {
        return isKeyword;
    }

    public void resetReferences() {
        this.references = 1;
    }

    public void decreaseRef() {
        this.references--;
    }

    public void increaseRef() {
        this.references++;
    }

    public int getReferences() {
        return this.references;
    }

    public void setDeclared() {
        this.declared = true;
    }

    public boolean isDeclared() {
        return this.declared;
    }
}