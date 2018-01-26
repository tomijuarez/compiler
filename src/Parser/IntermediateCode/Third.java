package Parser.IntermediateCode;

import Lexer.SymbolTable.SymbolTable;

import java.util.Stack;

public abstract class Third {

    protected String typeThird; // tipo de terceto, ejemplo : DIV, MUL, etc
    protected Object value1;
    protected Object value2;
    private String name;
    protected IntermediateCodeManager codeManager;
    private boolean isLabeled = false;
    private int label;

    public void setCodeManager(IntermediateCodeManager codeManager) {
        this.codeManager = codeManager;
    }

    public void setIsLabeled(int label) {
        this.isLabeled = true;
        this.label = label;
    }

    public boolean isLabeled() {
        return this.isLabeled;
    }

    public int getLabel() {
        return this.label;
    }

    public String getTypeThird(){
        return typeThird;
    }

    public Object getValue1(){
        return value1;
    }

    public Object getValue2(){
        return value2;
    }

    protected int getRef1() {
        if (this.value1 instanceof StackReference) {
            StackReference ref = (StackReference) this.value1;
            return ref.getRef();
        }
        return -1;
    }

    protected int getRef2() {
        if (this.value2 instanceof StackReference) {
            StackReference ref = (StackReference) this.value2;
            return ref.getRef();
        }
        return -1;
    }

    public void setValue1(Object newValue){ value1 = newValue; }

    public void setValue2(Object newValue){ value2 = newValue; }

    @Override
    public String toString() {
        String op1 = (this.value1 != null) ? this.value1.toString() : "-";
        String op2 = (this.value2 != null) ? this.value2.toString() : "-";
        return '(' + this.typeThird + ", " + op1 + ", " + op2 + ')';
    }

    public abstract String generateAssembler();


}