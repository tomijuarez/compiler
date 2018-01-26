package Lexer.SymbolTable;

import Lexer.LexerConstants;
import Lexer.Token;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    /**
     * Tabla de símbolos con clave de búsqueda por identificador.
     * Esta estructura posee una lista de símbolos ya que se asume que cada identificador puede tener
     *  asociado diferentes símbolos. Por ejemplo: una función y una variable con el mismo nombre.
     */
    HashMap<String, List<Symbol>> symbolsByIdentifier = new HashMap<>();

    public SymbolTable() {
    }

    public boolean isInDataSegment(String identifier) {
        if (!this.symbolsByIdentifier.containsKey(identifier.toLowerCase()))
            return true; //Para no agregarlo.

        for(Symbol symbol: this.symbolsByIdentifier.get(identifier.toLowerCase())) {
            if (symbol.isInDataSegment())
                return true;
        }
        return false;
    }

    public void setIsInDataSegment(String identifier) {
        if (!this.symbolsByIdentifier.containsKey(identifier.toLowerCase()))
            return;

        this.symbolsByIdentifier.get(identifier.toLowerCase()).forEach(Symbol::setIsInDataSegment);
    }

    public List<Symbol> getAttributes(String identifier) {
        return symbolsByIdentifier.get(identifier);
    }

    public void addSymbol(String identifier, Symbol symbol) {
        List<Symbol> symbols;

        symbols = (this.symbolsByIdentifier.get(identifier.toLowerCase()) == null)
                ? new ArrayList<>()
                : this.symbolsByIdentifier.get(identifier.toLowerCase());

        symbol.setPositionInSymbolTable(this.getSymbols().size());
        symbols.add(symbol);
        this.symbolsByIdentifier.put(identifier.toLowerCase(), symbols);
    }

    public List<Symbol> getSymbols(String identifier) {
        return this.symbolsByIdentifier.get(identifier);
    }

    /**
     * Esta función retorna el símbolo único identificado según el nombre del identificador
     *  y el identificar del token correspondiente. No pueden existir dos símbolos en el mismo programa
     *  con el mismo identificador de token y el mismo identificador.
     *
     * @param identifier
     * @param tokenId
     * @return Symbol
     */

    public Symbol getSymbol(String identifier, int tokenId) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier.toLowerCase());

        if (symbols == null)
            return null;

        for (Symbol symbol : symbols) {
            if (symbol.getToken().getIdToken() == tokenId)
                return symbol;
        }

        return null;
    }

    public Symbol getSymbol(String identifier) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier.toLowerCase());

        return (symbols == null)
                ? null
                : symbols.get(symbols.size() - 1); //El último token registrado con este lexema.
    }

    public void replaceEntry(String oldLexeme, String newLexeme) {
        List<Symbol> symbols = this.getSymbols(oldLexeme);

        for (Symbol symbol: symbols) {
            symbol.getToken().setLexeme(newLexeme);
        }

        this.symbolsByIdentifier.remove(oldLexeme);
        this.symbolsByIdentifier.put(newLexeme, symbols);
    }

    public void copyEntry(String oldLexeme, String newLexeme) {
        List<Symbol> symbols = new ArrayList<>();
        Token aux;
        Symbol symb;
        for (Symbol symbol: this.getSymbols(oldLexeme)) {
            aux = symbol.getToken();
            symb = new Symbol(new Token(aux.getIdToken(),newLexeme),false);
            symb.setType(symbol.getType());
            symbols.add(symb);
        }

        this.symbolsByIdentifier.put(newLexeme, symbols);
    }

    public void print() {
        System.out.println("PALABRAS RESERVADAS DEL LENGUAJE: ");
        for (String identifier : this.symbolsByIdentifier.keySet())
            this.printKeywords(identifier);

        System.out.println("______________________________________________________________");

        System.out.println("TABLA DE SÍMBOLOS: ");
        for(String identifier : this.symbolsByIdentifier.keySet())
            this.printSymbols(identifier);
    }

    private void printKeywords(String identifier) {
        for (Symbol symbol : this.symbolsByIdentifier.get(identifier)) {
            if(symbol.isKeyword())
                System.out.println("TOKEN ID: "+symbol.getToken().getIdToken() + " | LEXEMA: " +symbol.getToken().getLexeme());
        }
    }

    private void printSymbols(String identifier) {
        for (Symbol symbol : this.symbolsByIdentifier.get(identifier)) {
            if (!symbol.isKeyword())
                System.out.println("TOKEN ID: "+symbol.getToken().getIdToken() + " | LEXEMA: " +symbol.getToken().getLexeme() + " | TIPO: " + symbol.getType());
        }
    }

    public void setDeclared(String identifier, String type) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier);

        if (symbols.size() > 0) {
            Symbol symbol = symbols.get(symbols.size() - 1); //El último
            symbol.setDeclared();
            symbol.setType(type);
        }
    }

    public boolean isDeclared(String identifier) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier);
        //Basta con saber si el primero fue declarado.
        return symbols.get(0).isDeclared();
    }

    public String getType(String identifier) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier.toLowerCase());
        return symbols.get(symbols.size() - 1).getType(); //El último.
    }

    public void appendId(String identifier, String type) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier);
        Symbol s;

        if (symbols.size() > 0) {
            s = symbols.get(0);
            Token t = s.getToken();
            Token newToken = new Token(t.getIdToken(), t.getLexeme());
            Symbol newSymbol = new Symbol(newToken, false); //REFERENCIA
            newSymbol.setType(type);
            newSymbol.setDeclared();
            symbols.add(newSymbol);
        }
    }

    public String getLastDeclaredType(String identifier) {
        List<Symbol> symbols = this.symbolsByIdentifier.get(identifier);
        return (symbols.size() > 0)
                ? this.symbolsByIdentifier.get(identifier).get(symbols.size() - 1).getType()
                : ""
                ;
    }

    public void clean() {
        List<String> identifiersToDelete = new ArrayList<>();

        for (String identifier : this.symbolsByIdentifier.keySet()) {
            for (Symbol symbol : this.symbolsByIdentifier.get(identifier)) {
                if (symbol.getReferences() < 1) {
                    //Si no tiene referencias, lo elimino.
                    identifiersToDelete.add(identifier);
                }
            }
        }

        for (String id: identifiersToDelete) {
            this.symbolsByIdentifier.remove(id);
        }

    }

    public Map<String, List<Symbol>> getSymbols() {
        return this.symbolsByIdentifier;
    }

    public boolean isConstant(String constant){
        return (this.getSymbol(constant, LexerConstants.NUMERIC_CONST) != null);
    }
}