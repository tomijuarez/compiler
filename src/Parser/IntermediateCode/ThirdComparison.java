package Parser.IntermediateCode;

import Lexer.SymbolTable.SymbolTable;

public class ThirdComparison extends Third {

    private String UIConstPreamble = "_UI_";
    private String FloatConstPreamble = "_FP_";

    private boolean op1IsConstant = false;
    private boolean op2IsConstant = false;

    private SymbolTable st;
    private String type;

    private boolean isSecondOperandConverted;
    private boolean isFirstOperandIsConverted;
    private boolean invertComparator = false;

    public ThirdComparison(String comp, Object v1, Object v2, SymbolTable st, String type, boolean isFirstOperandIsConverted, boolean isSecondOperandConverted){
        typeThird = comp;
        value1 = v1;
        value2 = v2;
        this.st = st;
        this.type = type;
        this.isFirstOperandIsConverted = isFirstOperandIsConverted;
        this.isSecondOperandConverted = isSecondOperandConverted;

        this.invertComparator = false;
    }

    private String getReg1() {
        if (this.getRef1() == -1)
            if (!this.value1.equals("eax") && !this.value1.equals("ebx") && !this.value1.equals("ecx")) {
                return (this.type.toLowerCase().equals("ulong"))
                        ? this.UIConstPreamble + ((String) this.value1).replace(",", "_").replace("+", "_").replace("-", "_")
                        : this.FloatConstPreamble + ((String) this.value1).replace(",", "_").replace("+", "_").replace("-", "_");
            }
            else {
                return (String) this.value1;
            }
        else {
            return (!this.isFirstOperandIsConverted) ? ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName() : null;
        }
    }

    private String getReg2() {
        if (this.getRef2() == -1)
            if (!this.value2.equals("eax") && !this.value2.equals("ebx") && !this.value2.equals("ecx")) {
                return (this.type.toLowerCase().equals("ulong"))
                    ? this.UIConstPreamble + ((String)this.value2).replace(",","_").replace("+", "_").replace("-","_")
                    : this.FloatConstPreamble + ((String)this.value2).replace(",","_").replace("+", "_").replace("-","_");
            }
            else {
                return (String) this.value2;
            }
        else {
            return (!this.isSecondOperandConverted) ? ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName() : null;
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

    public String equalComp() {
        return this.prepareConstants()+"\t\tCMP "+this.getReg1()+ ", "+this.getReg2()+ "\n"+
                "\t\tJNE";
    }

    public String notEqualComp() {
        return this.prepareConstants()+"\t\tCMP "+this.getReg1()+ ", "+this.getReg2()+ "\n"+
                "\t\tJE";
    }

    public String greaterComp() {
        return this.prepareConstants()+"\t\tCMP "+this.getReg1()+ ", "+this.getReg2()+ "\n"+
                "\t\tJLE";
    }

    public String lessComp() {
        return this.prepareConstants()+"\t\tCMP "+this.getReg1()+ ", "+this.getReg2()+ "\n"+
                "\t\tJGE";
    }

    public String lessEqualComp() {
        return this.prepareConstants()+"\t\tCMP "+this.getReg1()+ ", "+this.getReg2()+ "\n" +
                "\t\tJG";
    }

    public String greaterEqualComp() {
        return this.prepareConstants()+"\t\tCMP "+this.getReg1()+ ", "+this.getReg2()+ "\n" +
                "\t\tJL";
    }

    public String floatGreaterComp() {
        return this.buildComparison() + "\t\tJBE";
    }

    public String floatLessComp() {
        return this.buildComparison() + "\t\tJAE";
    }

    public String floatLessEqualComp() {
        return this.buildComparison() + "\t\tJA";
    }

    public String floatGreaterEqualComp() {
        return this.buildComparison() + "\t\tJB";
    }

    public String floatEqualComp() {
        return this.buildComparison() + "\t\tJNE";
    }

    public String floatNotEqualComp() {
        return this.buildComparison() + "\t\tJZ";
    }

    private String buildComparison() {
        String statement = "";

        if (this.isSecondOperandConverted && this.isFirstOperandIsConverted) {
            statement += "\t\tFXCH ST(1)\n" +
                    "\t\tFCOM";
        }
        else {
            if (this.isSecondOperandConverted) {
                statement += "\t\tFLD "+this.getReg1()+"\n" +
                        "\t\tFCOM";
            }
            else {
                if (this.isFirstOperandIsConverted) {
                    //Sólo el primer operando es de punto flotante.
                    statement += "\t\tFLD "+this.getReg2()+"\n" +
                            "\t\tFXCH ST(1)\n" +
                            "\t\tFCOM";
                }
                else {
                    //Ningún operando es de punto flotante.
                    //Ambos operandos son de punto flotante.
                    statement +=
                            "\t\tFLD "+this.getReg1()+"\n"+
                            "\t\tFCOM "+this.getReg2()+ "\n";
                }
            }
        }

        return statement + "\n\t\tfstsw ax\n\t\tsahf\n";
    }

    @Override
    public String generateAssembler() {
        String statement = "";
        switch (this.typeThird) {
            case "==":
                statement = (type.toLowerCase().equals("ulong")) ? this.equalComp() : this.floatEqualComp();
                break;
            case "<>":
                statement = (type.toLowerCase().equals("ulong")) ? this.notEqualComp() : this.floatNotEqualComp();
                break;
            case ">":
                statement = (type.toLowerCase().equals("ulong")) ? this.greaterComp() : this.floatGreaterComp();
                break;
            case "<":
                statement = (type.toLowerCase().equals("ulong")) ? this.lessComp() : this.floatLessComp();
                break;
            case ">=":
                statement = (type.toLowerCase().equals("ulong")) ? this.greaterEqualComp() : this.floatGreaterEqualComp();
                break;
            case "<=":
                statement = (type.toLowerCase().equals("ulong")) ? this.lessEqualComp() : this.floatLessEqualComp();
        }
        return statement;
    }
}