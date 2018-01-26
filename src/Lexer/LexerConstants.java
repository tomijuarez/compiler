package Lexer;

public class LexerConstants {

    /**
     * Constantes de la matriz de transición de estados.
     */
    public static final int INVALID_TRANSITION = -1;
    public static final int FIRST_STATE        = 0;
    public static final int FINAL_STATE        = 1000;
    /**
     * Constantes de tokens generados explícitamente (no por ASCII asociado).
     */
    public final static int ID            = 501;
    public final static int STRING_CONST  = 502;
    public final static int GREATER_EQUAL = 503;
    public final static int LESS_EQUAL    = 504;
    public final static int EQUALS        = 505;
    public final static int NOT_EQUAL     = 506;
    public final static int NUMERIC_CONST = 507;

    /**
     * Palabras reservadas del lenguaje.
     */
    public final static int IF            = 1000;
    public final static int THEN          = 1001;
    public final static int ELSE          = 1002;
    public final static int BEGIN         = 1003;
    public final static int END           = 1004;
    public final static int END_IF        = 1005;
    public final static int OUT           = 1006;
    public final static int FLOAT         = 1007;
    public final static int ULONG         = 1008;
    public final static int SWITCH        = 1009;
    public final static int CASE          = 1010;
    public final static int LET           = 1011;
    public final static int STRING        = 1012;
    public final static int UL_TO_F       = 1013;
}
