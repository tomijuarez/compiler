import Lexer.Lexer;
import Lexer.*;
import Lexer.WrapperCode;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import Parser.IntermediateCode.IntermediateCodeManager;
import Parser.GenerateAssembler;
import Parser.Parser;

public class Main {

    public static void main(String args[]) {
        String stringCode = "";
        Scanner scanner = new Scanner(System.in);

        System.out.println("Por favor, ingrese un archivo.");

        try {
            Scanner scan = new Scanner(new FileReader(scanner.nextLine()));
            while (scan.hasNextLine()) {
                stringCode += scan.nextLine() + "\n";
            }
            stringCode = stringCode.substring(0, stringCode.length() - 1); //Remove last \n
            stringCode += '\u001a';
            scan.close();
            try {
                displayMenu(stringCode);
            }
            catch (Throwable t) {
                System.out.println("Ocurrió un error inesperado: " +t);
                t.printStackTrace();
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("No es posible abrir el archivo. "+e.getMessage());
        }
    }

    private static void lex(String stringCode) {
        System.out.println("*******************************************************");
        System.out.println("* ETAPA DE ANÁLISIS LÉXICO. TOKENS RECONOCIDAS,       *");
        System.out.println("* ERRORES LÉXICOS Y TABLA DE SÍMBOLOS LUEGO DEL LEXER.*");
        System.out.println("*******************************************************");
        WrapperCode code = new WrapperCode(stringCode);
        Lexer lexer = new Lexer(code);
        Token token;
        System.out.println("______________________________________________________________");
        System.out.println("TOKENS RECONOCIDOS Y ANUNCIO DE ERRORES:");
        while ((token = lexer.getNextToken()) != null) {
            System.out.println("TOKEN ID: " + token.getIdToken() + " | LEXEMA: " + token.getLexeme() + " | LÍNEA: " + lexer.getLine());
        }
        System.out.println("______________________________________________________________");
        lexer.getSymbolTable().print();
        System.out.println("______________________________________________________________");
    }

    private static void parse(String stringCode) {
        System.out.println("*******************************************************");
        System.out.println("* ETAPA DE PARSING. REGLAS RECONOCIDAS, ERRORES       *");
        System.out.println("* SINTÁCTICOS Y TABLA DE SÍMBOLOS LUEGO DEL PARSING.  *");
        System.out.println("*******************************************************");
        WrapperCode code = new WrapperCode(stringCode);
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser();
        IntermediateCodeManager codeManager = new IntermediateCodeManager(lexer.getSymbolTable());
        parser.setArtifacts(lexer, lexer.getSymbolTable(), codeManager);
        parser.parse();
        System.out.println("______________________________________________________________");
        lexer.getSymbolTable().print();
        System.out.println("______________________________________________________________");
    }

    private static void generateIntermediateCode(String stringCode) {
        System.out.println("*******************************************************");
        System.out.println("* ETAPA DE GENERACIÓN DE CÓDIGO INTERMEDIO.           *");
        System.out.println("* IMPRESIÓN DE TERCETOS.                              *");
        System.out.println("*******************************************************");
        WrapperCode code = new WrapperCode(stringCode);
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser();
        IntermediateCodeManager codeManager = new IntermediateCodeManager(lexer.getSymbolTable());
        parser.setArtifacts(lexer, lexer.getSymbolTable(), codeManager);
        parser.parse();
        lexer.getSymbolTable().print();
        System.out.println("______________________________________________________________");
        if (!codeManager.hasParsingErrors()) {
            codeManager.print();
            GenerateAssembler assembler = new GenerateAssembler(codeManager, lexer.getSymbolTable());
            try {
                assembler.generateExecutableProgram();
                System.out.println("El programa escrito en lenguaje assembler ha sido volcado en el archivo llamado 'salida.txt'");
            }
            catch (IOException a) {
                a.printStackTrace();
            }
        }
        else
            System.out.println("No se puede generar el código intermedio debido a que hubo errores léxicos y/o sintácticos.");
        System.out.println("______________________________________________________________");
    }

    public static void displayMenu(String stringCode) {
        Scanner sn = new Scanner(System.in);
        boolean salir = false;
        int opcion; //Guardaremos la opcion del usuario

        while (!salir) {
            System.out.println("1. Analizador léxico, palabras reservadas y tabla de símbolos (salida del TP1)");
            System.out.println("2. Analizador sintáctico y tabla de símbolos post-parsing. (salida del TP2)");
            System.out.println("3. Código intermedio generado en tercetos y generación del código assembler (salida del TP3)");
            System.out.println("4. Salir");

            try {

                System.out.println("Elegí una de las opciones.");
                opcion = sn.nextInt();

                switch (opcion) {
                    case 1:
                        lex(stringCode);
                        break;
                    case 2:
                        parse(stringCode);
                        break;
                    case 3:
                        generateIntermediateCode(stringCode);
                        break;
                    case 4:
                        salir = true;
                        break;
                    default:
                        System.out.println("Sólo números entre 1 y 4");
                }
            } catch (InputMismatchException e) {
                System.out.println("Debes insertar un número.");
                sn.next();
            }
        }

    }
}
