package Parser.IntermediateCode;

import Lexer.SymbolTable.SymbolTable;

/**
 * Created by tomi on 28/10/17.
 */
public class ThirdConversion extends Third {

    private String UIConstPreamble = "_UI_";
    private String FPConstPreamble = "_FP_";
    private String operandType;
    private SymbolTable st;

    public ThirdConversion(Object v1, String type, SymbolTable st) {
        this.typeThird = "UL_F";
        this.operandType = type;
        value1 = v1;
        this.st = st;
    }

    private String getReg1() {
        if (this.getRef1() == -1)
            return this.UIConstPreamble + ((String)this.value1).replace(",","_").replace("+", "_").replace("-","_");
        else
            return ((ThirdArithmeticOperation)this.codeManager.getReferenceResult()).getResultName();
    }

    @Override
    public String generateAssembler() {
        return "\t\tFILD "+this.getReg1()+"\n";
    }
}
