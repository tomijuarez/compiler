package Parser.IntermediateCode;

import Lexer.SymbolTable.SymbolTable;

public class ThirdAssignation extends Third {

    private SymbolTable st;

    private String UIConstPreamble = "_UI_";
    private String FloatConstPreamble = "_FP_";

    private boolean op1IsConstant = false;
    private boolean op2IsConstant = false;
    private boolean op1IsRegister = false;
    private boolean op2IsRegister = false;
    private String type;
    private boolean rightIsConverted = false;

    public ThirdAssignation(Object op1, Object op2, SymbolTable st, String type, boolean rightConverted){
        this.typeThird = "=";
        this.value1 = op1;
        this.value2 = op2;
        this.st = st;
        this.type = type;
        this.rightIsConverted = rightConverted;
    }

    private String getReg1() {
        if (this.getRef1() == -1)
            if (this.st.isConstant((String)this.value1)) {
                return (this.st.getType((String)this.value1).toLowerCase().equals("ulong"))
                        ? this.UIConstPreamble + ((String)this.value1).replace(",","_").replace("+", "_").replace("-","_")
                        : this.FloatConstPreamble + ((String)this.value1).replace(",","_").replace("+", "_").replace("-","_");
            }
            else {
                String preamble = (this.type.toLowerCase().equals("ulong")) ? this.UIConstPreamble : this.FloatConstPreamble;
                return (this.op1IsConstant || this.op1IsRegister)
                        ? preamble +((String) this.value1).replace(",", "_").replace("+", "_").replace("-", "_")
                        : preamble +((String) this.value1);
            }
        else {
            return ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName();
        }
    }

    private String getReg2() {
        if (this.getRef2() == -1)
            if (this.st.isConstant((String)this.value2)) {
                return (this.st.getType((String)this.value2).toLowerCase().equals("ulong"))
                        ? this.UIConstPreamble + ((String)this.value2).replace(",","_").replace("+", "_").replace("-","_")
                        : this.FloatConstPreamble + ((String)this.value2).replace(",","_").replace("+", "_").replace("-","_");
            }
            else{
                String preamble = (this.type.toLowerCase().equals("ulong")) ? this.UIConstPreamble : this.FloatConstPreamble;
                return (this.op2IsConstant || this.op2IsRegister)
                        ? preamble +((String) this.value2).replace(",", "_").replace("+", "_").replace("-", "_")
                        : preamble +((String) this.value2);
            }
        else {
            return (this.codeManager.getReferenceResult() != null)? ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName() : null;
        }
    }

    private String prepareConstants() {
        String movements = "";
        if (this.getRef1() == -1) {
            if (this.st.isConstant((String)this.value1)) {
                movements = "\t\tMOV ebx, "+this.getReg1()+" \n";
                this.value1 = "ebx";
                this.op1IsConstant = true;
            }
        }

        if (this.getRef2() == -1) {
            if (this.st.isConstant((String)this.value2)) {
                movements = "\t\tMOV ecx, "+this.getReg2()+"\n";
                this.value2 = "ecx";
                this.op2IsConstant = true;
            }
        }
        return movements;
    }

    @Override
    public String generateAssembler() {
        String assembly = "";
        if (this.getReg2() != null) {
            if (this.type.toLowerCase().equals("ulong")) {
                String reg = "ebx";
                if (!this.getReg2().equals("eax") && !this.getReg2().equals("ebx") && !this.getReg2().equals("ecx") && !this.getReg2().equals("edx")) {
                    assembly += "\t\tMOV ebx, " + this.getReg2() + "\n";
                    this.value2 = "ebx";
                } else
                    reg = this.getReg2();

                this.op2IsRegister = true;
                assembly += this.prepareConstants() + "\t\tMOV " + this.getReg1() + ", " + reg + "\n";
            }
            else {
                //Float.
                assembly += (!this.rightIsConverted) ? "\t\tFLD "+this.getReg2()+"\n" : "";
                assembly += "\t\tFST "+this.getReg1()+"\n";

            }
        }
        else
            assembly = "\t\tFST "+this.getReg1()+"\n";

        if (this.isLabeled())
            assembly += "\t\tlabel" + this.getLabel() + ":\n";

        return assembly;
    }
}