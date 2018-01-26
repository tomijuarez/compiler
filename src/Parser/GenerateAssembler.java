package Parser;

import Lexer.Lexer;
import Lexer.LexerConstants;
import Lexer.SymbolTable.Symbol;
import Lexer.SymbolTable.SymbolTable;
import Parser.IntermediateCode.AuxiliaryRegister;
import Parser.IntermediateCode.IntermediateCodeManager;
import Parser.IntermediateCode.Third;

import java.io.*;
import java.util.List;
import java.util.Map;

public class GenerateAssembler {

    private IntermediateCodeManager codeManager;
    private SymbolTable symbolTable;

    private String UIConstPreamble = "_UI_";
    private String FloatConstPreamble = "_FP_";

    public GenerateAssembler(IntermediateCodeManager codeManager, SymbolTable symbolTable) {
        this.codeManager = codeManager;
        this.symbolTable = symbolTable;
    }

    public void generateExecutableProgram() throws IOException {
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("salida.asm"), "utf-8"));
        writer.write(this.getAssembler());
        writer.close();

        String comc = "cmd /c .\\masm32\\bin\\ml /c /Zd /coff salida.asm ";
        Process ptasm32 = Runtime.getRuntime().exec(comc);
        InputStream is = ptasm32.getInputStream();
        String coml = "cmd /c \\masm32\\bin\\Link /SUBSYSTEM:CONSOLE salida.obj ";
        Process ptlink32 = Runtime.getRuntime().exec(coml);
        InputStream is2 = ptlink32.getInputStream();
    }

    private String getPreamble() {
        return ".386\n" +
                ".model flat, stdcall\n" +
                "option casemap :none\n" +
                "include \\masm32\\include\\windows.inc\n" +
                "include \\masm32\\include\\kernel32.inc\n" +
                "include \\masm32\\include\\user32.inc\n" +
                "includelib \\masm32\\lib\\kernel32.lib\n" +
                "includelib \\masm32\\lib\\user32.lib\n" +
                "include c:\\masm32\\include\\masm32rt.inc";
    }

    private String getDataSegment() {
        String data = "\n.data\n";
        int counter = 0;

        data += "\t;Límites superiores de los tipos FLOAT y ULONG\n";
        data += "\tFP_max DD 3.40282347E38\n" +
                "\tUI_max DD 4294967295\n";

        data += "\t;Constantes y variables registradas en la tabla de símbolos.";
        for (Map.Entry<String, List<Symbol>> symbol: this.symbolTable.getSymbols().entrySet()) {
            counter = 0;
            for (Symbol s : symbol.getValue()) {
                if (!this.symbolTable.isInDataSegment(s.getToken().getLexeme()) && !s.isAuxVar()) {
                    if (s.getToken().getIdToken() == LexerConstants.NUMERIC_CONST) {
                        data += "\n\t";
                        data += (s.getType().toLowerCase().equals("ulong")) ? this.UIConstPreamble : this.FloatConstPreamble;
                        data += s.getToken().getLexeme().replace(",","_").replace("+", "_").replace("-","_");
                        String val = ((s.getToken().getLexeme().charAt(0) == ',') ? s.getToken().getLexeme().replace(",","0.") : s.getToken().getLexeme().replace(",",","));
                        val = (val.charAt(val.length() - 1) == ',') ? val.replace(",", ".0") : val;
                        val = val.replace(',','.');
                        data += " DD " + val;
                    }
                    else {
                        if (s.getToken().getIdToken() == LexerConstants.ID) {
                            if (this.symbolTable.getSymbols(s.getToken().getLexeme()).size() > 1) {
                                data += "\n\t"+this.FloatConstPreamble+ s.getToken().getLexeme() + " DD ?"; //FP.
                                data += "\n\t"+this.UIConstPreamble+ s.getToken().getLexeme() + " DD ?"; //UL.
                            } else {
                                //No hay shadowing.
                                data += "\n\t";
                                data += (s.getType().toLowerCase().equals("ulong")) ? this.UIConstPreamble : this.FloatConstPreamble;
                                data += s.getToken().getLexeme();
                                data += " DD ?";
                            }
                        }
                    }
                    this.symbolTable.setIsInDataSegment(s.getToken().getLexeme());
                }
                counter++;
            }
        }

        data += "\n\t;Registros auxiliares.";

        for (Map.Entry<String, List<Symbol>> symbol: this.symbolTable.getSymbols().entrySet()) {
            counter = 0;
            for (Symbol s : symbol.getValue()) {
                if (s.isAuxVar()) {
                    data += "\n\t";
                    data += s.getToken().getLexeme();
                    data += " DD ?";
                }
            }
        }

        data += "\n\t;String en memoria para poder imprimirlos.";
        for (Map.Entry<String, List<Symbol>> symbol: this.symbolTable.getSymbols().entrySet()) {
            for (Symbol s : symbol.getValue()) {
                if (s.getToken().getIdToken() == LexerConstants.STRING_CONST) {
                    //String en memoria.
                    data += "\n\tST_STRING_ID_" + s.getPositionInSymbolTable() + " db \"" + s.getToken().getLexeme() + "\", 0";
                }
            }
        }

        data += "\n\t;Strings de alerta para verificaciones en tiempo de ejecución";
        data += "\n\tST_STRING_SUM_OVERFLOW db \"ERROR: overflow en suma.\", 0";
        data += "\n\tST_STRING_MULT_OVERFLOW db \"ERROR: overflow en multiplicación.\", 0";
        data += "\n\tST_STRING_DIV_BY_ZERO db \"ERROR: división por cero.\", 0";
        data += "\n\tST_STRING_NEG_ULONG db \"ERROR: resultado negativo en un registro entero largo sin signo.\", 0";

        return data;
    }

    private String getCodeSegment() {
        String code = "\n.code\n\tstart:\n";

        Third t;
        AuxiliaryRegister aux;
        while((t = this.codeManager.getNextThird()) != null) {
            code+= t.generateAssembler();
        }

        code += this.getUtilities();
        code += "\n\tend start\n";

        return code;
    }

    private String getUtilities() {
        String utilities = "\t\tjmp end_program\n" +
                "\t\tsum_overflow:\n" +
                "\t\t\tinvoke MessageBox, NULL, addr ST_STRING_SUM_OVERFLOW, addr ST_STRING_SUM_OVERFLOW, MB_OK\n"+
                "\t\t\tjmp end_program\n" +
                "\t\tmult_overflow:\n" +
                "\t\t\tinvoke MessageBox, NULL, addr ST_STRING_MULT_OVERFLOW, addr ST_STRING_MULT_OVERFLOW, MB_OK\n"+
                "\t\t\tjmp end_program \n" +
                "\t\tdivision_by_zero:\n" +
                "\t\t\tinvoke MessageBox, NULL, addr ST_STRING_DIV_BY_ZERO, addr ST_STRING_DIV_BY_ZERO, MB_OK\n"+
                "\t\t\tjmp end_program\n" +
                "\t\tui_negative_result:\n"+
                "\t\t\tinvoke MessageBox, NULL, addr ST_STRING_NEG_ULONG, addr ST_STRING_NEG_ULONG, MB_OK\n"+
                "\t\t\tjmp end_program\n" +
                "\t\tend_program:\n" +
                "\t\t\tinvoke ExitProcess, 0";

        return utilities;
    }

    public String getAssembler() {
        return this.getPreamble() + this.getDataSegment() + this.getCodeSegment();
    }
}
