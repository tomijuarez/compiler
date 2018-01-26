package Parser.IntermediateCode;

import Lexer.LexerConstants;
import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Lexer.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IntermediateCodeManager {

    private SymbolTable symbolTable;
    private Queue thirds = new Queue();
    private List<Third> references = new ArrayList<>();
    private boolean parsingErrors = false;
    private List<Third> biThirds = new ArrayList<>();
    private List<Third> bfThirds = new ArrayList<>();
    private List<String> switchConditions = new ArrayList<>();
    private List<AuxiliaryRegister> auxiliaryRegisters = new ArrayList<>();

    public IntermediateCodeManager(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public String currentThird() {
        return String.valueOf(this.thirds.size() - 1);
    }

    public String previousThird() {
        return String.valueOf(this.thirds.size() - 2);
    }

    private boolean hasIncompleteThirds() {
        return (this.biThirds.size() == 0 || this.bfThirds.size() == 0);
    }

    public void addThird(Third third) {
        this.thirds.push(third);
        third.setCodeManager(this);
    }

    public void markLast() {
        if (this.thirds.getThirds().size() > 0)
            this.thirds.getThirds().get(this.thirds.getThirds().size() - 1).setIsLabeled(this.thirds.getThirds().size());
    }

    public void markAsLabeled(int stackReference, int labelInt) {
        this.thirds.getThirds().get(stackReference).setIsLabeled(labelInt);
    }

    public void addArithThird(ThirdArithmeticOperation third) {
        this.thirds.push(third);
        String resultName = "aux" + (this.auxiliaryRegisters.size() + 1);

        Symbol s =  new Symbol(new Token(LexerConstants.ID, resultName),false);
        s.setType(third.getResultType());
        s.setIsAuxVar();
        this.symbolTable.addSymbol(resultName,s);

        this.auxiliaryRegisters.add(new AuxiliaryRegister(resultName, third.getResultType().toLowerCase()));
        third.setResultName(resultName);
        third.setCodeManager(this);
    }

    public AuxiliaryRegister getAux() {
        return (this.auxiliaryRegisters.size() > 0)
                ? this.auxiliaryRegisters.get(this.auxiliaryRegisters.size() - 1)
                : null;
    }

    public AuxiliaryRegister popAux() {
        if (this.auxiliaryRegisters.size() == 0)
            return null;

        AuxiliaryRegister aux = this.auxiliaryRegisters.get(this.auxiliaryRegisters.size() - 1);
        this.auxiliaryRegisters.remove(this.auxiliaryRegisters.size() - 1);
        return aux;
    }

/*    public void completeThird(int nextThird) {
        Third third = (this.bfThird != null) ? this.bfThird : this.biThird;

        if (third != null) {
            third.setValue2(nextThird);
        }
    }*/

    public void print() {
        Queue auxThirds = new Queue(this.thirds);
        if (auxThirds.size() > 0) {
            System.out.println("TERCETOS:");
            Third it;
            int counter = 0;
            while ((it = auxThirds.pop()) != null) {
                System.out.println(counter + ". " + it.toString());
                counter++;
            }
        }
        else
            System.out.println("No hay tercetos para mostrar.");
    }

    public void addBFThird(Third third) {
        this.thirds.push(third);
        this.bfThirds.add(third);
        third.setCodeManager(this);
    }

    public void addBIThird(Third third) {
        this.thirds.push(third);
        this.biThirds.add(third);
        third.setCodeManager(this);
    }

    public void completeBI(int jump) {
        if (this.biThirds.size() == 0)
            return;
        Third third = this.biThirds.get(this.biThirds.size() - 1);
        third.setValue1(jump);
        this.biThirds.remove(third);
    }

    public void completeBF(int jump) {
        if (this.bfThirds.size() == 0)
            return;
        Third third = this.bfThirds.get(this.bfThirds.size() - 1);
        third.setValue2(jump);
        this.bfThirds.remove(third);
        this.markAsLabeled((jump - 1)/*BI*/,jump);
    }

    public void completeAllBI(int jump) {
        if (this.biThirds.size() == 0)
            return;

        for (Third bi : this.biThirds) {
            bi.setValue1(jump);
        }

        this.biThirds = new ArrayList<>();
    }

    public void removeLastBI() {
        if (this.biThirds.size() == 0 || this.bfThirds.size() == 0)
            return;

        this.biThirds.remove(this.biThirds.size() - 1);
        ThirdBF third = (ThirdBF) this.bfThirds.get(this.bfThirds.size() - 1);
        third.setValue1(((int)third.getValue1()) - 1);
    }

    public String popSwitchDisc() {
        String disc = (this.switchConditions.size() > 0)
                ? this.switchConditions.get(this.switchConditions.size() - 1)
                : "";
        this.switchConditions.remove(this.switchConditions.size() - 1);

        return disc;
    }

    public String getSwitchDisc() {
        return (this.switchConditions.size() > 0)
                ? this.switchConditions.get(this.switchConditions.size() - 1)
                : "";
    }

    public void addSwitchDisc(String condition) {
        this.switchConditions.add(condition);
    }

    public void setParsingErrors() {
        this.parsingErrors = true;
    }

    public boolean hasParsingErrors() {
        return this.parsingErrors;
    }

    public Queue getThirds() {
        return thirds;
    }

    public Third getReferenceResult() {
        return (this.references.size() > 0)
                ? this.references.get(this.references.size() - 1)
                : null;
    }

    public Third getNextThird() {
        Third ref = this.thirds.pop();
        if (ref instanceof ThirdArithmeticOperation)
            this.references.add(ref);
        return ref;
    }
}
