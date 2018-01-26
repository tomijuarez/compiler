package Lexer;

import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.TransitionMatrix.Rules.*;
import Lexer.TransitionMatrix.SemanticActions.*;
import Lexer.TransitionMatrix.Transition;
import Lexer.TransitionMatrix.TransitionMatrix;

import java.util.Objects;

public class Lexer {

    private WrapperCode code;
    private SymbolTable symbolTable = new SymbolTable();
    private TransitionMatrix transitions = new TransitionMatrix();

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    private void buildSymbolTable(){
        this.symbolTable.addSymbol("if", new Symbol(new Token(LexerConstants.IF, "if"), true));
        this.symbolTable.addSymbol("then", new Symbol(new Token(LexerConstants.THEN, "then"), true));
        this.symbolTable.addSymbol("else", new Symbol(new Token(LexerConstants.ELSE, "else"), true));
        this.symbolTable.addSymbol("begin", new Symbol(new Token(LexerConstants.BEGIN, "begin"), true));
        this.symbolTable.addSymbol("end", new Symbol(new Token(LexerConstants.END, "end"), true));
        this.symbolTable.addSymbol("end_if", new Symbol(new Token(LexerConstants.END_IF, "end_if"), true));
        this.symbolTable.addSymbol("out", new Symbol(new Token(LexerConstants.OUT, "out"), true));
        this.symbolTable.addSymbol("float", new Symbol(new Token(LexerConstants.FLOAT, "float"), true));
        this.symbolTable.addSymbol("ulong", new Symbol(new Token(LexerConstants.ULONG, "ulong"), true));
        this.symbolTable.addSymbol("switch", new Symbol(new Token(LexerConstants.SWITCH, "switch"), true));
        this.symbolTable.addSymbol("case", new Symbol(new Token(LexerConstants.CASE, "case"), true));
        this.symbolTable.addSymbol("let", new Symbol(new Token(LexerConstants.LET, "let"), true));
        this.symbolTable.addSymbol("ul_f", new Symbol(new Token(LexerConstants.UL_TO_F, "ul_f"), true));
    }

    public void buildTransitionMatrix() {
        /**
         * Creación de acciones semánticas.
         * En primer lugar se crean los generadores de los tokens. Estos ocurren dentro de los méotdos de las acciones semánticas.
         * Por otro lado, estas acciones semánticas se insertan en las acciones de eliminación del último caracter.
         */
        SemanticAction generateFloatSA = new GenerateFloat(LexerConstants.NUMERIC_CONST, this.symbolTable);
        SemanticAction generateUlongSA = new GenerateULONG(LexerConstants.NUMERIC_CONST, this.symbolTable);
        SemanticAction generateIdentifierSA = new GenerateID(LexerConstants.ID, this.symbolTable);
        SemanticAction nopSA = new NoAction();

        //Trunca si pasa el limite de 15 y avisa Warning.
        SemanticAction as1 = new DeleteCharacter(generateIdentifierSA);
        //No valida si pasa los limites ULONG, sino arma token CTE .
        SemanticAction as2 = new DeleteCharacter(generateUlongSA);
        //No valida si pasa los limites FLOAT, sino arma token CTE .
        SemanticAction as3 = new DeleteCharacter(generateFloatSA);
        //Devuelve a la entrada el ultimo caracter leido y genera un token simple según código ASCII.
        SemanticAction as4 = new GenerateOperator();
        //Genera un token de comparador <> (distinto)
        SemanticAction as5 = new GenerateCompoundSymbol(LexerConstants.NOT_EQUAL);
        //Genera un token de comparador <=(menor o igual que)
        SemanticAction as6 = new GenerateCompoundSymbol(LexerConstants.LESS_EQUAL);
        //Genera un token de comparador >=(mayor o igual que)
        SemanticAction as7 = new GenerateCompoundSymbol(LexerConstants.GREATER_EQUAL);
        //Genera un token == (igual que)
        SemanticAction as8 = new GenerateCompoundSymbol(LexerConstants.EQUALS);
        //Genera un token Asignación
        //SemanticAction as9 = new DeleteCharacter(as8);
        //Genera un token cadena de string vacía buffer
        SemanticAction as10 = new GenerateString(LexerConstants.STRING_CONST, this.symbolTable);
        //Genera token con símbolo simple según ASCII y elimina el último caracter del buffer.
        SemanticAction as11 = new DeleteCharacter(as4);
        //Acción semántica de símbolo inválido.
        SemanticAction as12 = new SkipSymbol();
        //Acción semántica para eliminar últimos 4 símbolos (...\n)
        SemanticAction as13 = new SkipNewLineString();
        SemanticAction as14 = new GenerateUnclosedToken(as10, '"', "se detectó un String sin cierre de comillas.");
        SemanticAction as15 = new GenerateUnclosedToken(null, ']', "se detectó un comentario sin cierre de corchetes.");
        SemanticAction as16 = new CountNewLine();
        SemanticAction as17 = new OnlyFirstChar(as4); //nuevo.


        /**
         * Creación de las reglas de transición.
         */

        /**
         * Regla de dígitos.
         */
        TransitionRule numericRule = new NumericRule();
        /**
         * Regla de letras excepto e y E.
         */
        TransitionRule charRule = new CharRule();
        /**
         * Regla de letras e y E.
         */
        TransitionRule eLetterRule = new SpecificCharacterTransition('e');
        /**
         * Regla de caracter _
         */
        TransitionRule underScoreRule = new SpecificCharacterTransition('_');
        /**
         * Regla de caracter [
         */
        TransitionRule openSquareBracketRule = new SpecificCharacterTransition('[');
        /**
         * Regla de caracter ]
         */
        TransitionRule closeSquareBracketRule = new SpecificCharacterTransition(']');
        /**
         * Regla de operadores + y -
         */
        TransitionRule plusRule = new SpecificCharacterTransition('+');
        TransitionRule minusRule = new SpecificCharacterTransition('-');
        TransitionRule arithOperatorsRule = new OrRule(plusRule, minusRule);
        /**
         * Regla de caracteres *, /, (, ) y :
         */
        TransitionRule multRule = new SpecificCharacterTransition('*');
        TransitionRule divRule = new SpecificCharacterTransition('/');
        TransitionRule openParenRule = new SpecificCharacterTransition('(');
        TransitionRule closeParenRule = new SpecificCharacterTransition(')');
        TransitionRule openBraces = new SpecificCharacterTransition('{');
        TransitionRule closeBraces = new SpecificCharacterTransition('}');
        TransitionRule semicolonRule = new SpecificCharacterTransition(':');

        TransitionRule orRule1 = new OrRule(multRule, divRule);
        TransitionRule orRule2 = new OrRule(openParenRule, closeParenRule);
        TransitionRule orRule3 = new OrRule(orRule1, orRule2);
        TransitionRule orRule4 = new OrRule(openBraces, closeBraces);
        TransitionRule orRule5 = new OrRule(orRule3, semicolonRule);
        TransitionRule caractersAgrupationRule = new OrRule(orRule4, orRule5);
        /**
         * Regla de caracter <
         */
        TransitionRule lessRule = new SpecificCharacterTransition('<');
        /**
         * Regla de caracter >
         */
        TransitionRule biggerRule = new SpecificCharacterTransition('>');
        /**
         * Regla de caracter =
         */
        TransitionRule equalRule = new SpecificCharacterTransition('=');
        /**
         * Regla de caracter ,
         */
        TransitionRule commaRule = new SpecificCharacterTransition(',');
        /**
         * Regla de caracter .
         */
        TransitionRule dotRule = new SpecificCharacterTransition('.');
        /**
         * Regla de caracter "
         */
        TransitionRule quoteRule = new SpecificCharacterTransition('"');
        /**
         * Regla de espacio en blanco (\b) o tabulación (\t)
         */
        TransitionRule spaceRule = new SpecificCharacterTransition(' ');
        TransitionRule tabRule = new SpecificCharacterTransition('\t');
        TransitionRule spacesAgrupationRule = new OrRule(spaceRule,tabRule);
        /**
         * Regla de salto de línea (\n)
         */
        TransitionRule newLineRule = new SpecificCharacterTransition('\n');
        /**
         * Regla de fin de archivo  EOF.
         */
        TransitionRule endOfFileRule = new EOFRule();
        /**
         * Regla incondicional (para los casos por default).
         */
        TransitionRule trueRule = new TrueRule();


        /**
         * Creación de las transiciones.
         */

        /**
         * Estado 0.
         */
        transitions.addTransition(0,new Transition(charRule, nopSA, 1));
        transitions.addTransition(0,new Transition(numericRule, nopSA, 2));
        transitions.addTransition(0,new Transition(underScoreRule, as12, 0));
        transitions.addTransition(0,new Transition(openSquareBracketRule, as11, 3));
        transitions.addTransition(0,new Transition(closeSquareBracketRule, as12, 0));
        transitions.addTransition(0,new Transition(arithOperatorsRule, as4, LexerConstants.FINAL_STATE));
        transitions.addTransition(0,new Transition(caractersAgrupationRule, as4, LexerConstants.FINAL_STATE));
        transitions.addTransition(0,new Transition(lessRule, nopSA, 4));
        transitions.addTransition(0,new Transition(biggerRule, nopSA, 5));
        transitions.addTransition(0,new Transition(equalRule, nopSA, 6));
        transitions.addTransition(0,new Transition(commaRule, nopSA, 10));
        transitions.addTransition(0,new Transition(dotRule, as4, LexerConstants.FINAL_STATE));
        transitions.addTransition(0,new Transition(quoteRule, nopSA, 7));
        transitions.addTransition(0,new Transition(spacesAgrupationRule, nopSA, 0));
        transitions.addTransition(0,new Transition(eLetterRule, nopSA, 1));
        transitions.addTransition(0,new Transition(newLineRule, nopSA, 0));
        transitions.addTransition(0,new Transition(endOfFileRule,nopSA,0));
        transitions.addTransition(0,new Transition(trueRule, as12, LexerConstants.FIRST_STATE));

        /**
         * Estado 1.
         */
        transitions.addTransition(1,new Transition(charRule, nopSA, 1));
        transitions.addTransition(1,new Transition(numericRule, nopSA, 1));
        transitions.addTransition(1,new Transition(underScoreRule, nopSA, 1));
        transitions.addTransition(1,new Transition(openSquareBracketRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(closeSquareBracketRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(arithOperatorsRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(caractersAgrupationRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(lessRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(biggerRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(equalRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(commaRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(dotRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(quoteRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(spacesAgrupationRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(eLetterRule, nopSA, 1));
        transitions.addTransition(1,new Transition(newLineRule, as1, LexerConstants.FINAL_STATE));
        transitions.addTransition(1,new Transition(trueRule, as1, LexerConstants.FINAL_STATE));

        /**
         * Estado 2.
         */
        transitions.addTransition(2,new Transition(charRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(numericRule, nopSA, 2));
        transitions.addTransition(2,new Transition(underScoreRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(openSquareBracketRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(closeSquareBracketRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(arithOperatorsRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(caractersAgrupationRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(lessRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(biggerRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(equalRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(commaRule, nopSA, 9));
        transitions.addTransition(2,new Transition(dotRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(quoteRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(spacesAgrupationRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(eLetterRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(newLineRule, as2, LexerConstants.FINAL_STATE));
        transitions.addTransition(2,new Transition(trueRule, as2, LexerConstants.FINAL_STATE));

        /**
         * Estado 3.
         */
        transitions.addTransition(3,new Transition(charRule, nopSA, 3));
        transitions.addTransition(3,new Transition(numericRule, nopSA, 3));
        transitions.addTransition(3,new Transition(underScoreRule, nopSA, 3));
        transitions.addTransition(3,new Transition(openSquareBracketRule, nopSA, 3));
        transitions.addTransition(3,new Transition(closeSquareBracketRule, nopSA, 0));
        transitions.addTransition(3,new Transition(arithOperatorsRule, nopSA, 3));
        transitions.addTransition(3,new Transition(caractersAgrupationRule, nopSA, 3));
        transitions.addTransition(3,new Transition(lessRule, nopSA, 3));
        transitions.addTransition(3,new Transition(biggerRule, nopSA, 3));
        transitions.addTransition(3,new Transition(equalRule, nopSA, 3));
        transitions.addTransition(3,new Transition(commaRule, nopSA, 3));
        transitions.addTransition(3,new Transition(dotRule, nopSA, 3));
        transitions.addTransition(3,new Transition(quoteRule, nopSA, 3));
        transitions.addTransition(3,new Transition(spacesAgrupationRule, nopSA, 3));
        transitions.addTransition(3,new Transition(eLetterRule, nopSA, 3));
        transitions.addTransition(3,new Transition(newLineRule, as16, 3));
        transitions.addTransition(3,new Transition(endOfFileRule, as15, LexerConstants.FIRST_STATE));
        transitions.addTransition(3,new Transition(trueRule, nopSA, 3));

        /**
         * Estado 4.
         */
        transitions.addTransition(4,new Transition(charRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(numericRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(underScoreRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(openSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(closeSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(arithOperatorsRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(caractersAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(lessRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(biggerRule, as5, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(equalRule, as6, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(commaRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(dotRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(quoteRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(spacesAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(eLetterRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(newLineRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(4,new Transition(trueRule, as11, LexerConstants.FINAL_STATE));

        /**
         * Estado 5.
         */
        transitions.addTransition(5,new Transition(charRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(numericRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(underScoreRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(openSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(closeSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(arithOperatorsRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(caractersAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(lessRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(biggerRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(equalRule, as7, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(commaRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(dotRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(quoteRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(spacesAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(eLetterRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(newLineRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(5,new Transition(trueRule, as11, LexerConstants.FINAL_STATE));

        /**
         * Estado 6.
         */
        transitions.addTransition(6,new Transition(charRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(numericRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(underScoreRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(openSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(closeSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(arithOperatorsRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(caractersAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(lessRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(biggerRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(equalRule, as8, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(commaRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(dotRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(quoteRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(spacesAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(eLetterRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(newLineRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(6,new Transition(trueRule, as11, LexerConstants.FINAL_STATE));

        /**
         * Estado 7.
         */
        transitions.addTransition(7,new Transition(charRule, nopSA, 7));
        transitions.addTransition(7,new Transition(numericRule, nopSA, 7));
        transitions.addTransition(7,new Transition(underScoreRule, nopSA, 7));
        transitions.addTransition(7,new Transition(openSquareBracketRule, nopSA, 7));
        transitions.addTransition(7,new Transition(closeSquareBracketRule, nopSA, 7));
        transitions.addTransition(7,new Transition(arithOperatorsRule, nopSA, 7));
        transitions.addTransition(7,new Transition(caractersAgrupationRule, nopSA, 7));
        transitions.addTransition(7,new Transition(lessRule, nopSA, 7));
        transitions.addTransition(7,new Transition(biggerRule, nopSA, 7));
        transitions.addTransition(7,new Transition(equalRule, nopSA, 7));
        transitions.addTransition(7,new Transition(commaRule, nopSA, 7));
        transitions.addTransition(7,new Transition(dotRule, nopSA, 8));
        transitions.addTransition(7,new Transition(quoteRule, as10, LexerConstants.FINAL_STATE));
        transitions.addTransition(7,new Transition(eLetterRule, nopSA, 7));
        transitions.addTransition(7,new Transition(spacesAgrupationRule, nopSA, 7));
        transitions.addTransition(7,new Transition(newLineRule, as12, LexerConstants.FIRST_STATE)); //OJO.
        transitions.addTransition(7,new Transition(endOfFileRule, as14, LexerConstants.FINAL_STATE));
        transitions.addTransition(7,new Transition(trueRule,nopSA,7));

        /**
         * Estado 8.
         */
        transitions.addTransition(8,new Transition(charRule, nopSA, 7));
        transitions.addTransition(8,new Transition(numericRule, nopSA, 7));
        transitions.addTransition(8,new Transition(underScoreRule, nopSA, 7));
        transitions.addTransition(8,new Transition(openSquareBracketRule, nopSA, 7));
        transitions.addTransition(8,new Transition(closeSquareBracketRule, nopSA, 7));
        transitions.addTransition(8,new Transition(arithOperatorsRule, nopSA, 7));
        transitions.addTransition(8,new Transition(caractersAgrupationRule, nopSA, 7));
        transitions.addTransition(8,new Transition(lessRule, nopSA, 7));
        transitions.addTransition(8,new Transition(biggerRule, nopSA, 7));
        transitions.addTransition(8,new Transition(equalRule, nopSA, 7));
        transitions.addTransition(8,new Transition(commaRule, nopSA, 7));
        transitions.addTransition(8,new Transition(dotRule, nopSA, 13));
        transitions.addTransition(8,new Transition(quoteRule, as10, LexerConstants.FINAL_STATE));
        transitions.addTransition(8,new Transition(spacesAgrupationRule, nopSA, 7));
        transitions.addTransition(8,new Transition(eLetterRule, nopSA, 7));
        transitions.addTransition(8,new Transition(newLineRule, as12, LexerConstants.FIRST_STATE));
        transitions.addTransition(8,new Transition(endOfFileRule, as14, LexerConstants.FINAL_STATE));
        transitions.addTransition(8,new Transition(trueRule, nopSA, 7));

        /**
         * Estado 9.
         */
        transitions.addTransition(9,new Transition(charRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(numericRule, nopSA, 9));
        transitions.addTransition(9,new Transition(underScoreRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(openSquareBracketRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(closeSquareBracketRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(arithOperatorsRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(caractersAgrupationRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(lessRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(biggerRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(equalRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(commaRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(dotRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(quoteRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(spacesAgrupationRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(eLetterRule, nopSA, 11));
        transitions.addTransition(9,new Transition(newLineRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(endOfFileRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(9,new Transition(trueRule, as3, LexerConstants.FINAL_STATE));

        /**
         * Estado 10.
         */
        transitions.addTransition(10,new Transition(charRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(numericRule, nopSA, 9)); //VER.
        transitions.addTransition(10,new Transition(underScoreRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(openSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(closeSquareBracketRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(arithOperatorsRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(caractersAgrupationRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(lessRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(biggerRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(equalRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(commaRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(dotRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(quoteRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(spaceRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(eLetterRule, nopSA, 11));
        transitions.addTransition(10,new Transition(newLineRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(newLineRule, as11, LexerConstants.FINAL_STATE));
        transitions.addTransition(10,new Transition(trueRule, as11, LexerConstants.FINAL_STATE));

        /**
         * Estado 11.
         */
        transitions.addTransition(11,new Transition(numericRule, nopSA, 12));
        transitions.addTransition(11,new Transition(arithOperatorsRule, nopSA, 12));
        transitions.addTransition(11,new Transition(trueRule, as17, LexerConstants.FINAL_STATE));

        /**
         * Estado 12.
         */
        transitions.addTransition(12,new Transition(charRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(numericRule, nopSA, 12));
        transitions.addTransition(12,new Transition(underScoreRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(openSquareBracketRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(closeSquareBracketRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(arithOperatorsRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(caractersAgrupationRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(lessRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(biggerRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(equalRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(commaRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(dotRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(quoteRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(spaceRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(eLetterRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(newLineRule, as3, LexerConstants.FINAL_STATE));
        transitions.addTransition(12,new Transition(trueRule, as12, LexerConstants.FIRST_STATE));
        /**
         * Estado 13.
         */
        transitions.addTransition(13,new Transition(charRule, nopSA, 7));
        transitions.addTransition(13,new Transition(numericRule, nopSA, 7));
        transitions.addTransition(13,new Transition(underScoreRule, nopSA, 7));
        transitions.addTransition(13,new Transition(openSquareBracketRule, nopSA, 7));
        transitions.addTransition(13,new Transition(closeSquareBracketRule, nopSA, 7));
        transitions.addTransition(13,new Transition(arithOperatorsRule, nopSA, 7));
        transitions.addTransition(13,new Transition(caractersAgrupationRule, nopSA, 7));
        transitions.addTransition(13,new Transition(lessRule, nopSA, 7));
        transitions.addTransition(13,new Transition(biggerRule, nopSA, 7));
        transitions.addTransition(13,new Transition(equalRule, nopSA, 7));
        transitions.addTransition(13,new Transition(commaRule, nopSA, 7));
        transitions.addTransition(13,new Transition(dotRule, nopSA, 14));
        transitions.addTransition(13,new Transition(quoteRule, as10, LexerConstants.FINAL_STATE));
        transitions.addTransition(13,new Transition(eLetterRule, nopSA, 7));
        transitions.addTransition(13,new Transition(newLineRule, as12, LexerConstants.FIRST_STATE));
        transitions.addTransition(13,new Transition(endOfFileRule, as14, LexerConstants.FINAL_STATE));
        transitions.addTransition(13,new Transition(trueRule, nopSA, 7));

        /**
         * Estado 14.
         */
        transitions.addTransition(14,new Transition(charRule, as1, 7));
        transitions.addTransition(14,new Transition(numericRule, as1, 7));
        transitions.addTransition(14,new Transition(underScoreRule, as1, 7));
        transitions.addTransition(14,new Transition(openSquareBracketRule, as1, 7));
        transitions.addTransition(14,new Transition(closeSquareBracketRule, as1, 7));
        transitions.addTransition(14,new Transition(arithOperatorsRule, as1, 7));
        transitions.addTransition(14,new Transition(caractersAgrupationRule, as1, 7));
        transitions.addTransition(14,new Transition(lessRule, as1, 7));
        transitions.addTransition(14,new Transition(biggerRule, as1, 7));
        transitions.addTransition(14,new Transition(equalRule, as1, 7));
        transitions.addTransition(14,new Transition(commaRule, as1, 7));
        transitions.addTransition(14,new Transition(dotRule, nopSA, 14));
        transitions.addTransition(14,new Transition(quoteRule, as10, LexerConstants.FINAL_STATE));
        transitions.addTransition(14,new Transition(eLetterRule, nopSA, 7));
        transitions.addTransition(12,new Transition(spaceRule, nopSA, 7));
        transitions.addTransition(14,new Transition(newLineRule, as13, 7));
        transitions.addTransition(14,new Transition(endOfFileRule, as14, LexerConstants.FINAL_STATE));
        transitions.addTransition(14,new Transition(trueRule, nopSA, 7));
    }

    public Lexer(WrapperCode code) {
        this.code = code;
        this.buildTransitionMatrix();
        this.buildSymbolTable();
    }

    public int getErrors() {
        return this.code.getErrors();
    }

    public Token getNextToken() {
        int currentState = LexerConstants.FIRST_STATE;
        char currentChar = ' ';
        code.setBuffer("");
        boolean firstChar = true;
        this.transitions.cleanToken();

        while (code.getCode().length() > 0 && currentState != LexerConstants.FINAL_STATE) {
            if (firstChar || (!firstChar && !Objects.equals(code.getBuffer(), ""))) {
                currentChar = code.getCode().charAt(0);
                code.setBuffer(code.getBuffer()+currentChar);
                code.setCode(code.getCode().substring(1, code.getCode().length()));
                currentState = this.transitions.performTransition(currentState, code.getBuffer(), currentChar, this.code);
                if(currentState == LexerConstants.FIRST_STATE || (currentState == LexerConstants.FINAL_STATE && this.transitions.getToken() == null)) { //Elimina /n y espacios en blanco.
                    code.setBuffer("");
                    currentState = LexerConstants.FIRST_STATE;
                    firstChar = true;
                }
                else
                    firstChar = false;
            }
            else {
                firstChar = true;
                currentState = LexerConstants.FIRST_STATE;
                code.setBuffer("");
            }
        }

        return this.transitions.getToken();
    }

    public int getLine() {
        return this.code.getLine();
    }

}