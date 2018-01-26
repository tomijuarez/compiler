package Parser.IntermediateCode;

import Lexer.SymbolTable.SymbolTable;

public class ThirdArithmeticOperation extends Third {

    private SymbolTable st;
    private String reultType;
    private String resultName;

    private String UIConstPreamble = "_UI_";
    private String FloatConstPreamble = "_FP_";

    private boolean op1IsConstant = false;
    private boolean op2IsConstant = false;

    private boolean isFirstOperatorConverted = false;
    private boolean isSecondOperatorConverted = false;

    public ThirdArithmeticOperation(String operand, Object v1, Object v2, SymbolTable st, String resultType, boolean isFirstOperatorConverted, boolean isSecondOperatorConverted){
        typeThird = operand;
        value1 = v1;
        value2 = v2;
        this.st = st;
        this.reultType = resultType;
        this.isFirstOperatorConverted = isFirstOperatorConverted;
        this.isSecondOperatorConverted = isSecondOperatorConverted;
    }

    public String getResultName() {
        return this.resultName;
    }

    public void setResultName(String resultName) {
        this.resultName = resultName;
    }

    private String getReg1() {
        if (this.getRef1() == -1)
            if (!this.value1.equals("eax") && !this.value1.equals("ebx") && !this.value1.equals("ecx")) {
                return (this.st.getType((String)this.value1).toLowerCase().equals("ulong"))
                        ? this.UIConstPreamble + ((String) this.value1).replace(",", "_").replace("+", "_").replace("-", "_")
                        : this.FloatConstPreamble + ((String) this.value1).replace(",", "_").replace("+", "_").replace("-", "_");
            }
            else {
                return (String) this.value1;
            }
        else {
            return (!this.isFirstOperatorConverted) ? ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName() : null;
        }
    }

    private String getReg2() {
        if (this.getRef2() == -1)
            if (!this.value2.equals("eax") && !this.value2.equals("ebx") && !this.value2.equals("ecx")) {
                return (this.st.getType((String)this.value1).toLowerCase().equals("ulong"))
                        ? this.UIConstPreamble + ((String)this.value2).replace(",","_").replace("+", "_").replace("-","_")
                        : this.FloatConstPreamble + ((String)this.value2).replace(",","_").replace("+", "_").replace("-","_");
            }
            else {
                return (String) this.value2;
            }
        else {
            return (!this.isSecondOperatorConverted) ? ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName() : null;
        }
    }

    private String prepareConstants() {
        String movements = "";
        if (this.getRef1() == -1) {
            if (this.st.isConstant((String)this.value1)) {
                movements += "\t\tMOV ebx, "+this.getReg1()+" \n";
                this.value1 = "ebx";
                this.op1IsConstant = true;
            }
        }

        if (this.getRef2() == -1) {
            if (this.st.isConstant((String)this.value2)) {
                movements += "\t\tMOV ecx, "+this.getReg2()+"\n";
                this.value2 = "ecx";
                this.op2IsConstant = true;

            }
        }
        return movements;
    }

    private String generateUlongAdd () {
        return this.prepareConstants() +
                "\t\tMOV eax, " + this.getReg1() + "\n" +
                "\t\tADD eax, " + this.getReg2() + "\n" +
                "\t\tMOV "+this.resultName+", eax\n" +
                "\t\tJC sum_overflow\n";
    }

    private String generateUlongMult () {
        return this.prepareConstants() +
                "\t\tMOV eax, " + this.getReg1() + "\n" +
                "\t\tMUL " + this.getReg2() + "\n" +
                "\t\tMOV "+this.resultName+", eax\n" +
                "\t\tJC mult_overflow\n";
    }

    private String generateUlongSub () {
        return this.prepareConstants() +
                "\t\tMOV eax, " + this.getReg1() + "\n" +
                "\t\tSUB eax, " + this.getReg2() + "\n" +
                "\t\tXOR edx, edx\n"+
                "\t\tCMP eax, edx\n"+
                "\t\tJL ui_negative_result\n"+
                "\t\tMOV "+this.resultName+", eax\n";
    }

    private String generateUlongDiv () {
        return this.prepareConstants() +
                "\t\tMOV eax, " + this.getReg1() + "\n" +
                "\t\tXOR edx, edx\n"+
                "\t\tCMP edx, "+this.getReg2()+"\n"+
                "\t\tJE division_by_zero\n"+
                "\t\tDIV " + this.getReg2() + "\n" +
                "\t\tMOV "+this.resultName+", eax\n";
    }

    private String generateFloatAdd () {
        return  this.prepareOperands(false)+
                "\t\tFADD\n" +
                "\t\tFCOM FP_max\n" +
                "\t\tFSTSW ax\n" +
                "\t\tSAHF\n" +
                "\t\tJAE sum_overflow\n" +
                "\t\tFST "+this.resultName+"\n";
    }

    private String generateFloatMult () {
        return  this.prepareOperands(false)+
                "\t\tFMUL\n" +
                "\t\tFCOMP FP_max\n" +
                "\t\tFSTSW ax\n" +
                "\t\tSAHF\n" +
                "\t\tJAE mult_overflow\n" +
                "\t\tFST "+this.resultName+"\n";
    }

    private String generateFloatSub () {
        return this.prepareOperands(true) +
                "\t\tfsub\n"+
                "\t\tFST "+this.resultName+"\n";
    }

    private String generateFloatDiv () {
        return this.makeZeroComp() +
                this.prepareOperands(true) +
                "\t\tfdiv\n"+
                "\t\tFST "+this.resultName+"\n";
    }

    private String makeZeroComp() {
        return ((this.isSecondOperatorConverted)
                ? "\t\tFLDZ\n" +
                "\t\tFCOMP\n"

                : "\t\tFLDZ\n" +
                "\t\tFCOMP "+this.getReg2()+"\n"
            )
                + "\t\tfstsw ax\n" +
                "\t\tsahf\n" +
                "\t\tjz division_by_zero\n";
    }

    private String prepareOperands(boolean keepOrder) {
        String statement = "";


            if (!this.isFirstOperatorConverted)
                statement += "\t\tFLD " + this.getReg1() + "\n";
            if (!this.isSecondOperatorConverted)
                statement += "\t\tFLD " + this.getReg2() + "\n";

        if (keepOrder && this.isSecondOperatorConverted && !this.isFirstOperatorConverted) {
            statement += "\t\tFXCH ST(1)\n";
        }

        return statement;
    }

    public String getResultType() {
        return this.reultType;
    }

    @Override
    public String generateAssembler() {
        String statement = "";
        if (this.reultType.equals("ulong")) {
            switch (this.typeThird) {
                case "+":
                    statement = this.generateUlongAdd();
                    break;
                case "-":
                    statement = this.generateUlongSub();
                    break;
                case "*":
                    statement = this.generateUlongMult();
                    break;
                case "/":
                    statement = this.generateUlongDiv();
                    break;
            }
        }
        else {
            switch (this.typeThird) {
                case "+":
                    statement = this.generateFloatAdd();
                    break;
                case "-":
                    statement = this.generateFloatSub();
                    break;
                case "*":
                    statement = this.generateFloatMult();
                    break;
                case "/":
                    statement = this.generateFloatDiv();
                    break;
            }
        }

        return (this.isLabeled()) ? statement+"\t\tlabel"+this.getLabel()+":\n" : statement;

    }
}