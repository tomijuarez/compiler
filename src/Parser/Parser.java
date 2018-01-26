//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
	package Parser;

  	import Lexer.*;
    import Lexer.SymbolTable.SymbolTable;
    import Lexer.SymbolTable.Symbol;
    import Parser.IntermediateCode.*;
    import java.util.ArrayList;
    import java.util.List;

    class ParserConstants {
    	/*Parser errors.*/
    	public static final String ERR_DOT_EXPECTED 		= "falta el caracter '.' al finalizar la sentencia.";
    	public static final String ERR_VAR_TYPE_EXPECTED    = "falta el tipo de las variables en al sentencia declarativa.";
    	public static final String ERR_VAR_LIST_EXPECTED	= "falta una lista de variables en la sentencia declarativa.";
    	public static final String ERR_SEMICOLON_EXPECTED 	= "falta el caracter ':'.";
    	public static final String ERR_COMMA_EXPECTED		= "falta el caracter ',' entre cada identificador.";
    	public static final String ERR_IDENTIFIER_EXPECTED  = "falta un identificador de variable antes de la ','.";
    	public static final String ERR_END_IF_EXPECTED		= "falta la palabra reservada END_IF.";
    	public static final String ERR_IF_EXPECTED 			= "falta la palabra reservada IF.";
    	public static final String ERR_LPAREN_EXPECTED		= "falta el caracter '('.";
    	public static final String ERR_RPAREN_EXPECTED      = "falta el caracter ').";
    	public static final String ERR_THEN_EXPECTED 		= "falta la palabra reservada THEN.";
    	public static final String ERR_STATEMENT_EXPECTED   = "falta una sentencia de ejecución.";
    	public static final String ERR_STATEMENT_BLOCK_EXPECTED = "falta un bloque de sentencias de ejecución.";
    	public static final String ERR_END_EXPECTED 		= "falta la palabra reservada END.";
    	public static final String ERR_CONDITION_EXPECTED   = "falta una condición.";
    	public static final String ERR_BEGIN_EXPECTED		= "falta la palabra reservada BEGIN.";
    	public static final String ERR_OUT_EXPECTED			= "falta la palabra reservada OUT.";
    	public static final String ERR_CONSTANT_EXPECTED  	= "falta una constante numérica.";
    	public static final String ERR_CASE_EXPECTED 		= "falta la palabra reservada CASE";
    	public static final String ERR_SWITCH_EXPECTED		= "falta la palabra reservada SWITCH.";
    	public static final String ERR_CASES_EXPECTED  		= "falta al menos una sentenia CASE.";
    	public static final String ERR_LBRACE_EXPECTED 		= "falta el caracter '{'.";
    	public static final String ERR_RBRACE_EXPECTED 		= "falta el caracter'}'.";
    	public static final String ERR_ASSIGN_EXPECTED  	= "falta el caracter '='.";
    	public static final String ERR_LET_EXPECTED 		= "falta la palabra reservada LET.";
    	public static final String ERR_EXPRESSION_EXPECTED 	= "falta una expresión."; 	
    	public static final String ERR_UL2F_EXPECTED 		= "falta la palabra reservada UL_F.";
    	public static final String ERR_ID_SWITCH           	= "falta una variable de control en la sentencia switch.";
    	public static final String ERR_OUT_OF_BOUND_ULONG   = "la constante de tipo ULONG se encuentra fuera de rango."; 	
    	public static final String ERR_LEFT_ID_EXPECTED		= "falta un identificador en el lado izquierdo de la asignación.";
    	public static final String ERR_RIGHT_EXPR_EXPECTED	= "falta una expresión en el lado derecho de la asignación.";
    
    	/*Errores semánticos.*/
    	public static final String ERR_NOT_DECLARED_VAR = "no se ha declarado la variable ";
    	public static final String ERR_ASSIGN_TYPES 	= "el identificador del lado izquierdo debe ser del mismo tipo que la expresión del lado derecho en una asignación.";
    	public static final String ERR_REDECLARED_VAR   = "ya se ha declarado la variable ";
    	public static final String ERR_NOT_EQUAL_TYPES  = "se debe operar únicamente con operadores del mismo tipo ";
    	public static final String ERR_CONVERSION_FLOAT = "no es posible convertir una expresión de tipo float a float.";

    }
//#line 69 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=501;
public final static short STRING_CONST=502;
public final static short GREATER_EQUAL=503;
public final static short LESS_EQUAL=504;
public final static short EQUAL=505;
public final static short NOT_EQUAL=506;
public final static short NUMERIC_CONST=507;
public final static short IF=1000;
public final static short THEN=1001;
public final static short ELSE=1002;
public final static short BEGIN=1003;
public final static short END=1004;
public final static short END_IF=1005;
public final static short OUT=1006;
public final static short FLOAT=1007;
public final static short ULONG=1008;
public final static short SWITCH=1009;
public final static short CASE=1010;
public final static short LET=1011;
public final static short STRING=1012;
public final static short UL_F=1013;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    1,    1,    1,    1,    2,    4,    4,
    4,    4,    4,    5,    5,    5,    5,    6,    6,    3,
    3,    3,    3,    3,   12,   14,   15,   17,   18,   19,
   20,    7,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,   16,   16,   16,   16,   16,   21,
   21,    9,    9,    9,    9,    9,    9,    9,    9,    9,
    9,    9,   10,   10,   10,   10,   10,   10,   23,   11,
   11,   11,   11,   11,   11,   11,   11,   11,   24,   24,
   26,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   13,   13,   13,   13,   13,   13,   22,   27,   27,   27,
   28,   28,   28,   29,   29,   29,   29,   29,   29,
};
final static short yylen[] = {                            2,
    0,    1,    1,    1,    2,    2,    2,    1,    4,    3,
    3,    3,    3,    1,    3,    2,    2,    1,    1,    1,
    1,    1,    1,    1,    1,    1,    1,    1,    1,    1,
    1,    8,    8,    7,    8,    7,    7,    7,    7,    7,
    7,    7,    8,    7,    7,    7,    7,    7,   10,   10,
   10,   10,    9,   10,    9,    9,    9,    9,    9,    9,
    9,    9,   10,    9,    9,    9,    9,    9,    9,    9,
   10,    9,    9,    9,    9,    9,    9,    9,   10,    9,
    9,    9,    9,    9,    4,    3,    4,    3,    3,    2,
    1,    5,    4,    4,    4,    4,    4,    3,    3,    3,
    6,    7,    5,    4,    4,    4,    4,    4,    1,    8,
    7,    8,    7,    7,    7,    7,    7,    7,    2,    1,
    1,    4,    4,    4,    3,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    1,    3,    3,    1,
    3,    3,    1,    1,    1,    2,    4,    3,    3,
};
final static short yydefred[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    3,    4,    8,    0,   20,   21,   22,   23,
   24,    7,    0,  144,  145,    0,    0,    0,    0,   25,
    0,  137,    0,  143,    0,    0,  109,    0,    0,    0,
    0,   19,   18,    0,   17,    0,    0,    0,    0,    0,
    5,    6,   16,    0,    0,    0,   99,    0,    0,    0,
    0,    0,    0,    0,  146,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   13,    0,    0,    0,  100,
   11,    0,   15,   12,    0,   93,  148,    0,  149,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  141,  142,  107,  105,    0,  106,    0,    0,    0,
    0,   95,    0,    0,   96,   97,  108,    0,    0,    9,
    0,  147,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  103,  121,    0,    0,  120,    0,
    0,    0,    0,    0,    0,   92,    0,    0,    0,    0,
    0,  101,    0,   91,    0,    0,    0,    0,    0,    0,
   90,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  119,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   88,    0,    0,   39,   46,
    0,   30,    0,    0,    0,    0,   89,   38,   45,    0,
    0,    0,    0,    0,    0,   36,    0,    0,    0,    0,
    0,    0,   37,   44,    0,    0,    0,    0,   40,   47,
    0,    0,    0,    0,    0,  126,    0,    0,  125,  116,
    0,  127,  113,    0,    0,  114,  115,  117,  102,   41,
   48,    0,    0,    0,    0,  118,   87,   85,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   35,   32,
   43,   33,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  124,    0,  122,  112,
  110,    0,    0,    0,    0,   67,   59,   82,   75,   66,
   58,   81,   74,   64,   56,   55,    0,    0,    0,    0,
   72,    0,    0,    0,    0,   65,   57,   80,   73,   68,
   60,   83,   76,   69,   61,   84,   77,   63,   50,   54,
   49,   79,   52,   71,   51,
};
final static short yydgoto[] = {                         11,
   12,   13,  221,   15,   16,   44,   17,   18,   19,   20,
   21,   29,   30,  136,  137,  222,  139,  140,  223,  224,
  141,   31,   39,  158,  159,  160,   32,   33,   34,
};
final static short yysindex[] = {                       225,
   29,   41,  -36,   11,   17,   16, -879, -396,  -19,    8,
    0,  253,    0,    0,    0,  -21,    0,    0,    0,    0,
    0,    0,  -40,    0,    0,  -29,  -31, -390,   78,    0,
  228,    0,   73,    0,  103,   30,    0,   25,  113,   -9,
    8,    0,    0,  106,    0,    0,  117,  124,  127,   79,
    0,    0,    0,  -43, -331,  132,    0,    8,  131,  -28,
   38,  139, -811,  -23,    0, -799,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,  158,   49,  160,   84,
   33,   90,  -38,  143,  149,    0,  168, -785,   95,    0,
    0,  173,    0,    0,   91,    0,    0,  180,    0,  309,
  309,  260,  309,   38,   38,   38,   38,   38,   38,   73,
   73,    0,    0,    0,    0,  176,    0, -451, -111, -451,
 -451,    0,    8,  155,    0,    0,    0,  309, -451,    0,
  179,    0,   41,  317,    0, -779, -777,    0, -772, -771,
  325, -770, -768, -760, -759,  237, -753, -752, -747, -746,
 -744, -739, -743, -734,    0,    0,   15, -105,    0,  212,
 -100,  -98,  -95,  -93,  121,    0, -731, -730, -724, -721,
  -91,    0,  241,    0,  268,  249,  255,  309,  309,  256,
    0,  257,  259,  309,  309,  309,  263, -221, -218, -711,
 -703,  264,  266,  309,  309,  267,  271,  309,  309,  309,
  275,  273,    0,  309,  274, -103,  276,  277,  278,  281,
  284,  288,  309,  309,  289,    0,  291,  294,    0,    0,
    0,    0, -679, -667, -664, -663,    0,    0,    0, -661,
 -658, -657, -654, -653, -651,    0,  310,  312,  313,  315,
  285,  292,    0,    0, -643, -642, -639, -637,    0,    0,
 -636, -631, -629, -625,    0,    0,  299,    0,    0,    0,
    0,    0,    0,  339,  341,    0,    0,    0,    0,    0,
    0, -621, -617, -616, -612,    0,    0,    0,  348,  349,
  355,  359,  360,  364,  365,  367,  371,  372,    0,    0,
    0,    0,  373, -217, -216,  374, -213, -207,  375,  376,
  377,  378,  382,  383,  384,  385,    0,    0,    0,    0,
    0,  386,  387,  388,  389,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  390,  396,  398,  399,
    0,  400,  401,  402,  404,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yyrindex[] = {                       437,
    0,  306,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  451,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  330,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   60,    0,
    0,   58,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  183,    0,    0,    0,    0,    0,   58,    0,    0,
    0,    0,    0,  -22,  -20,  -13,  -10,   13,   14,   24,
   48,    0,    0,    0,    0,   72,    0,    0,    0,    0,
    0,    0,    0,   87,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  218,    0,    0, -223,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -110,    0,    0,
  245,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   99,    0,  111,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  -11,    0,    0,   36,    0,    0,
  193,    0,    0,    0,  123,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  206,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  135,    0,  147,
    0,    0,  159,    0,  171,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,  440,  562,    0,    0,   42,    0,    0,    0,    0,
    0,   71,    0,  148,  161,  338,  280,  297,  391,  503,
  319,  771,   69,   21,   76,  298,   53,   64,  134,
};
final static int YYTABLESIZE=1336;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         58,
  140,  123,   91,   27,   28,   57,   28,  122,   28,   63,
   60,  161,   97,   28,   86,   28,   28,  102,  134,  202,
  135,  265,   55,  138,  205,   28,  207,  131,   91,  208,
  136,  209,   27,  215,  237,   28,   54,  239,  327,  329,
  140,  140,  332,  140,  140,  140,  140,  139,  334,   91,
   36,   83,   28,  132,  133,  156,   38,   56,  140,   98,
  140,  140,  140,  138,  138,   80,  138,  138,  138,  138,
   79,  104,  200,  119,   22,   91,   41,   49,   62,   48,
   73,  138,   74,  138,  138,  138,   94,  139,  139,  116,
  139,  139,  139,  139,  115,   92,   91,   64,   34,   98,
  137,   23,  137,   98,   45,  139,   81,  139,  139,  139,
   42,  104,   98,  129,   75,  104,   65,   98,   66,   76,
   98,   73,  111,   74,   90,  140,   94,   42,   43,  104,
   94,  131,  104,   73,   62,   74,  110,  111,   34,  162,
  163,  164,   34,   77,   94,   86,   53,   94,  138,  171,
   42,   86,  264,   82,   42,  120,   34,   87,   78,   34,
  128,  210,  111,   73,   88,   74,  111,   89,   42,   93,
   70,   42,  139,   73,   62,   74,   96,   94,   62,   99,
  111,  206,   10,  111,   98,   73,   53,   74,  125,  100,
   53,   73,   62,   74,  126,   62,  104,   73,   78,   74,
  166,  103,   78,  114,   53,  117,  118,   53,  112,  113,
   70,   94,  121,  127,   70,  128,   78,  129,  130,   78,
  132,  155,   10,   34,  172,  176,   10,  177,   70,  178,
  179,   70,   91,  203,  182,   42,  183,  203,  203,  203,
   10,  184,  185,   10,  129,   91,  203,  111,  142,  147,
  151,  192,  193,   91,  194,  195,  140,   91,  198,   62,
  196,  143,  148,  152,    9,  197,   91,  199,    8,  204,
   73,   53,   74,  211,  212,  167,    9,  213,   91,  138,
  214,  203,    7,   78,   91,   10,  216,   72,  168,   71,
  241,  128,    9,  188,  219,   70,    8,   10,  242,    9,
  220,  227,  228,  139,  229,   91,  189,    9,  236,  243,
    7,  244,  249,   10,    9,   98,  250,  130,  260,  263,
   10,  266,  267,  268,    9,  279,  269,  104,   10,  270,
  123,    9,  257,  271,  276,   10,  277,  280,    9,  278,
  281,  282,   94,  283,  307,   10,  284,  285,    9,   14,
  286,  287,   10,  288,   34,  289,    9,  290,  291,   10,
  292,  299,  300,   14,    9,  301,   42,  302,  303,   10,
  109,  144,  144,  304,  144,  305,  144,   10,  111,  306,
  144,  149,  153,  312,  310,   10,  311,  313,  314,  144,
   62,  144,  315,  316,  317,  156,   86,  145,  150,  154,
  318,  156,   53,  156,  319,  320,  156,  169,  156,  321,
  322,  156,  323,  156,   78,  156,  324,  325,  326,  331,
  336,  337,  338,  339,  170,  190,   70,  340,  341,  342,
  343,  344,  345,  346,  347,  348,    1,  138,  138,  138,
  138,  349,  191,  350,  351,  352,  353,  354,  130,  355,
    2,   51,  175,    0,  201,    0,    0,    0,    0,    0,
   24,  123,   24,    0,   24,  138,   25,    0,   25,   24,
   25,   24,   24,   26,    0,   25,    0,   25,   25,   53,
    1,   46,   47,  138,    0,    0,    0,   25,    0,   91,
    0,   24,    0,    0,    0,  129,    0,   25,    0,    0,
   31,  140,    0,  140,  140,  140,  140,  140,   24,    0,
    0,    0,   35,    0,   25,    0,   40,   37,    0,    0,
    0,  156,    0,  217,  138,   37,  138,  138,  138,  138,
  138,   78,    0,    0,    0,    0,   91,  256,  259,    0,
    0,  262,  128,    0,    0,    0,    0,    0,  139,    0,
  139,  139,  139,  139,  139,    0,    0,    0,  157,    0,
   98,   14,    0,    0,    0,    0,   98,    0,    0,  225,
    0,    0,  104,   52,  230,  232,  234,    0,  104,    0,
    0,    0,    0,    0,  245,  247,    0,   94,  251,  253,
    0,    0,    0,   94,  309,    0,    0,    0,    0,   34,
    0,    0,    0,  272,  274,   34,    0,    0,    0,    0,
    0,   42,    0,    0,    0,    0,    0,   42,    0,    0,
    0,    0,    0,  111,    0,    0,    0,    0,    0,  111,
    0,  294,  297,    0,    0,   62,    0,    0,    0,    0,
    0,   62,    0,    0,    0,    0,    0,   53,    0,    0,
    0,    0,    0,   53,    0,    0,    0,    0,    0,   78,
    0,  135,  135,  135,  135,   78,    0,    0,    0,    0,
    0,   70,    0,    0,    0,    0,    0,   70,    0,    0,
    0,  226,    0,   10,    0,    0,  231,  233,  235,  135,
    0,    0,    0,   91,    0,  174,  246,  248,    0,  130,
  252,  254,  181,    0,    0,    0,   91,  135,    0,    0,
    0,    0,  123,    0,    0,  273,  275,    0,   91,    0,
    0,    0,    0,    0,    0,    2,    0,    0,    0,    0,
   67,   68,   69,   70,    0,    0,  181,  133,    0,    0,
    0,    0,    0,  295,  298,   91,    0,    0,    0,    0,
    0,    0,    0,    2,    0,    0,    0,    0,    0,    0,
  133,  255,  258,    0,    0,  261,    0,    0,  133,    0,
    0,    0,    0,    0,    0,  133,    0,    0,   29,    0,
   50,   27,    0,  238,    0,  133,  240,  328,  330,    0,
    0,  333,  133,   59,    0,    0,   61,  335,    0,  133,
    0,    0,    0,    0,    0,    0,   14,    0,    0,  133,
   84,   85,    0,    0,    0,    0,    0,  133,  308,    0,
    0,    0,    0,    0,    0,  133,    0,    0,   95,    0,
   61,    0,  144,  144,  144,  144,    0,  104,  105,  106,
  107,  108,  109,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  124,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   86,    0,  165,   86,    0,    0,    0,  157,   86,
    0,    0,    0,    0,  157,    0,  157,    0,    0,  157,
    0,  157,    0,    0,  157,    0,  157,    0,  157,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   42,   43,    0,    0,    0,    0,    0,
    0,    0,   26,    0,   26,    0,   26,  101,  134,    0,
  135,   26,    0,   26,   26,   42,   43,  131,   91,    0,
  136,    0,   91,   26,   91,    0,    0,   91,  129,   91,
  140,  140,  140,   26,  140,  140,  140,    0,    0,  140,
  140,  140,    0,  132,  133,    0,    0,    0,    0,    0,
   26,    0,    0,  138,  138,  138,    0,  138,  138,  138,
    0,    0,  138,  138,  138,   91,    0,    0,    0,   91,
    0,   91,    0,    0,   91,  128,   91,  139,  139,  139,
    0,  139,  139,  139,    0,    0,  139,  139,  139,   98,
    0,   98,    0,   98,   98,   98,    0,    0,   98,   98,
   98,  104,    0,  104,    0,  104,  104,  104,    0,    0,
  104,  104,  104,    0,    0,    0,   94,    0,   94,    0,
   94,   94,   94,    0,    0,   94,   94,   94,   34,    0,
   34,    0,   34,   34,   34,    0,    0,   34,   34,   34,
   42,    0,   42,    0,   42,   42,   42,    0,    0,   42,
   42,   42,  111,    0,  111,    0,  111,  111,  111,    0,
    0,  111,  111,  111,   62,    0,   62,    0,   62,   62,
   62,    0,    0,   62,   62,   62,   53,    0,   53,    0,
   53,   53,   53,    0,    0,   53,   53,   53,   78,    0,
   78,    0,   78,   78,   78,    0,    0,   78,   78,   78,
   70,    0,   70,    0,   70,   70,   70,    0,    0,   70,
   70,   70,   10,    0,    0,    0,    0,    0,   10,    0,
    0,   10,   91,   10,    0,    0,   91,    0,   91,    0,
    0,   91,  130,   91,    0,   91,    0,    0,    0,   91,
    0,   91,    0,    0,   91,  123,   91,   91,    0,   28,
    0,   91,   26,   91,    3,    0,   91,    0,   91,    0,
    4,    0,    0,    5,    0,    6,    3,    0,  186,  134,
    0,  187,    4,    0,   91,    5,    0,    6,   91,   31,
   91,    0,    3,   91,    0,   91,    0,    0,    4,    3,
  146,    5,  134,    6,    0,    4,    0,    3,    5,    0,
    6,  218,    0,    4,    3,    0,    5,  134,    6,    0,
    4,    0,    0,    5,    3,    6,    0,  134,    0,  293,
    4,    3,    0,    5,  134,    6,  296,    4,    3,    0,
    5,  134,    6,    0,    4,    0,    0,    5,    3,    6,
    0,  134,   14,   14,    4,    0,    3,    5,    0,    6,
  173,    0,    4,    0,    3,    5,    0,    6,  180,    0,
    4,    0,    0,    5,    0,    6,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
    0,   40,   46,   40,   45,   46,   45,   46,   45,   41,
   40,  123,   41,   45,  125,   45,   45,   41,   41,  125,
   41,  125,   44,    0,  125,   45,  125,   41,   40,  125,
   41,  125,  256,  125,  256,   45,   58,  256,  256,  256,
   40,   41,  256,   43,   44,   45,   46,    0,  256,   61,
   40,   61,   45,   41,   41,  507,   40,   16,   58,    0,
   60,   61,   62,   40,   41,   41,   43,   44,   45,   46,
   41,    0,   58,   41,   46,   40,   61,    9,   26,    9,
   43,   58,   45,   60,   61,   62,    0,   40,   41,   41,
   43,   44,   45,   46,   46,   54,   61,   27,    0,   40,
   43,   61,   45,   44,  501,   58,   38,   60,   61,   62,
    0,   40,   60,  125,   42,   44,  507,   58,   41,   47,
   61,   43,    0,   45,   46,  125,   40, 1007, 1008,   58,
   44,   41,   61,   43,    0,   45,   73,   74,   40,  119,
  120,  121,   44,   41,   58,  256,    0,   61,  125,  129,
   40,   46,  256,   41,   44,  123,   58,   41,    0,   61,
  125,   41,   40,   43,   41,   45,   44,   41,   58,  501,
    0,   61,  125,   43,   40,   45,   46,   46,   44,   41,
   58,  161,    0,   61,  125,   43,   40,   45,   46, 1001,
   44,   43,   58,   45,   46,   61,  125,   43,   40,   45,
   46, 1001,   44,   46,   58,   46,  123,   61,   75,   76,
   40,  125,  123,   46,   44, 1001,   58,  123,   46,   61,
   41,   46,   40,  125,   46, 1005,   44, 1005,   58, 1002,
 1002,   61,   40,  158, 1005,  125, 1005,  162,  163,  164,
   58, 1002, 1002,   61,  256,   40,  171,  125,  101,  102,
  103, 1005, 1005,   61, 1002, 1002,  256,   40, 1002,  125,
 1005,  101,  102,  103,   40, 1005,   61, 1002,   44,   58,
   43,  125,   45, 1005, 1005,  128,   40, 1002,   61,  256,
 1002,  206,   58,  125,   40,   61,   46,   60,  128,   62,
 1002,  256,   40,  146,   46,  125,   44,   61, 1002,   40,
   46,   46,   46,  256,   46,   61,  146,   40,   46,   46,
   58,   46,   46,   61,   40,  256,   46,  125,   46,   46,
   61,   46,   46,   46,   40, 1005,   46,  256,   61,   46,
  125,   40,   58,   46,   46,   61,   46, 1005,   40,   46,
 1005, 1005,  256, 1005,   46,   61, 1005, 1005,   40,   44,
 1005, 1005,   61, 1005,  256,   46,   40,   46,   46,   61,
   46, 1005, 1005,   58,   40, 1005,  256, 1005, 1005,   61,
   41,   42,   43, 1005,   45, 1005,   47,   61,  256, 1005,
  101,  102,  103, 1005,   46,   61,   46, 1005, 1005,   60,
  256,   62, 1005,   46,   46,  507,  507,  101,  102,  103,
   46,  507,  256,  507,   46,   46,  507,  128,  507,   46,
   46,  507,   46,  507,  256,  507,   46,   46,   46,   46,
   46,   46,   46,   46,  128,  146,  256,   46,   46,   46,
   46,   46,   46,   46,   46,   46,    0,  100,  101,  102,
  103,   46,  146,   46,   46,   46,   46,   46,  256,   46,
    0,   12,  134,   -1,  157,   -1,   -1,   -1,   -1,   -1,
  501,  256,  501,   -1,  501,  128,  507,   -1,  507,  501,
  507,  501,  501,  256,   -1,  507,   -1,  507,  507,  501,
  256,  501,  502,  146,   -1,   -1,   -1,  507,   -1,  501,
   -1,  501,   -1,   -1,   -1,  507,   -1,  507,   -1,   -1,
  256,  501,   -1,  503,  504,  505,  506,  507,  501,   -1,
   -1,   -1,  502,   -1,  507,   -1,  501,  501,   -1,   -1,
   -1,  507,   -1,  256,  501,  501,  503,  504,  505,  506,
  507,  502,   -1,   -1,   -1,   -1,  501,  200,  201,   -1,
   -1,  204,  507,   -1,   -1,   -1,   -1,   -1,  501,   -1,
  503,  504,  505,  506,  507,   -1,   -1,   -1, 1010,   -1,
  501,    0,   -1,   -1,   -1,   -1,  507,   -1,   -1,  179,
   -1,   -1,  501,   12,  184,  185,  186,   -1,  507,   -1,
   -1,   -1,   -1,   -1,  194,  195,   -1,  501,  198,  199,
   -1,   -1,   -1,  507,  257,   -1,   -1,   -1,   -1,  501,
   -1,   -1,   -1,  213,  214,  507,   -1,   -1,   -1,   -1,
   -1,  501,   -1,   -1,   -1,   -1,   -1,  507,   -1,   -1,
   -1,   -1,   -1,  501,   -1,   -1,   -1,   -1,   -1,  507,
   -1,  241,  242,   -1,   -1,  501,   -1,   -1,   -1,   -1,
   -1,  507,   -1,   -1,   -1,   -1,   -1,  501,   -1,   -1,
   -1,   -1,   -1,  507,   -1,   -1,   -1,   -1,   -1,  501,
   -1,  100,  101,  102,  103,  507,   -1,   -1,   -1,   -1,
   -1,  501,   -1,   -1,   -1,   -1,   -1,  507,   -1,   -1,
   -1,  179,   -1,  501,   -1,   -1,  184,  185,  186,  128,
   -1,   -1,   -1,  501,   -1,  134,  194,  195,   -1,  507,
  198,  199,  141,   -1,   -1,   -1,  501,  146,   -1,   -1,
   -1,   -1,  507,   -1,   -1,  213,  214,   -1,  501,   -1,
   -1,   -1,   -1,   -1,   -1,  501,   -1,   -1,   -1,   -1,
  503,  504,  505,  506,   -1,   -1,  175,  501,   -1,   -1,
   -1,   -1,   -1,  241,  242,  501,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  501,   -1,   -1,   -1,   -1,   -1,   -1,
  501,  200,  201,   -1,   -1,  204,   -1,   -1,  501,   -1,
   -1,   -1,   -1,   -1,   -1,  501,   -1,   -1, 1002,   -1,
   10, 1005,   -1, 1005,   -1,  501, 1005, 1005, 1005,   -1,
   -1, 1005,  501,   23,   -1,   -1,   26, 1005,   -1,  501,
   -1,   -1,   -1,   -1,   -1,   -1,  501,   -1,   -1,  501,
   40,   41,   -1,   -1,   -1,   -1,   -1,  501,  257,   -1,
   -1,   -1,   -1,   -1,   -1,  501,   -1,   -1,   58,   -1,
   60,   -1,  503,  504,  505,  506,   -1,   67,   68,   69,
   70,   71,   72,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   83,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1, 1002,   -1,  123, 1005,   -1,   -1,   -1, 1010, 1010,
   -1,   -1,   -1,   -1, 1010,   -1, 1010,   -1,   -1, 1010,
   -1, 1010,   -1,   -1, 1010,   -1, 1010,   -1, 1010,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1, 1007, 1008,   -1,   -1,   -1,   -1,   -1,
   -1,   -1, 1013,   -1, 1013,   -1, 1013, 1001, 1001,   -1,
 1001, 1013,   -1, 1013, 1013, 1007, 1008, 1001, 1000,   -1,
 1001,   -1, 1004, 1013, 1006,   -1,   -1, 1009, 1010, 1011,
 1000, 1001, 1002, 1013, 1004, 1005, 1006,   -1,   -1, 1009,
 1010, 1011,   -1, 1001, 1001,   -1,   -1,   -1,   -1,   -1,
 1013,   -1,   -1, 1000, 1001, 1002,   -1, 1004, 1005, 1006,
   -1,   -1, 1009, 1010, 1011, 1000,   -1,   -1,   -1, 1004,
   -1, 1006,   -1,   -1, 1009, 1010, 1011, 1000, 1001, 1002,
   -1, 1004, 1005, 1006,   -1,   -1, 1009, 1010, 1011, 1000,
   -1, 1002,   -1, 1004, 1005, 1006,   -1,   -1, 1009, 1010,
 1011, 1000,   -1, 1002,   -1, 1004, 1005, 1006,   -1,   -1,
 1009, 1010, 1011,   -1,   -1,   -1, 1000,   -1, 1002,   -1,
 1004, 1005, 1006,   -1,   -1, 1009, 1010, 1011, 1000,   -1,
 1002,   -1, 1004, 1005, 1006,   -1,   -1, 1009, 1010, 1011,
 1000,   -1, 1002,   -1, 1004, 1005, 1006,   -1,   -1, 1009,
 1010, 1011, 1000,   -1, 1002,   -1, 1004, 1005, 1006,   -1,
   -1, 1009, 1010, 1011, 1000,   -1, 1002,   -1, 1004, 1005,
 1006,   -1,   -1, 1009, 1010, 1011, 1000,   -1, 1002,   -1,
 1004, 1005, 1006,   -1,   -1, 1009, 1010, 1011, 1000,   -1,
 1002,   -1, 1004, 1005, 1006,   -1,   -1, 1009, 1010, 1011,
 1000,   -1, 1002,   -1, 1004, 1005, 1006,   -1,   -1, 1009,
 1010, 1011, 1000,   -1,   -1,   -1,   -1,   -1, 1006,   -1,
   -1, 1009, 1000, 1011,   -1,   -1, 1004,   -1, 1006,   -1,
   -1, 1009, 1010, 1011,   -1, 1000,   -1,   -1,   -1, 1004,
   -1, 1006,   -1,   -1, 1009, 1010, 1011, 1000,   -1, 1002,
   -1, 1004, 1005, 1006, 1000,   -1, 1009,   -1, 1011,   -1,
 1006,   -1,   -1, 1009,   -1, 1011, 1000,   -1, 1002, 1003,
   -1, 1005, 1006,   -1, 1000, 1009,   -1, 1011, 1004, 1005,
 1006,   -1, 1000, 1009,   -1, 1011,   -1,   -1, 1006, 1000,
 1001, 1009, 1003, 1011,   -1, 1006,   -1, 1000, 1009,   -1,
 1011, 1004,   -1, 1006, 1000,   -1, 1009, 1003, 1011,   -1,
 1006,   -1,   -1, 1009, 1000, 1011,   -1, 1003,   -1, 1005,
 1006, 1000,   -1, 1009, 1003, 1011, 1005, 1006, 1000,   -1,
 1009, 1003, 1011,   -1, 1006,   -1,   -1, 1009, 1000, 1011,
   -1, 1003, 1007, 1008, 1006,   -1, 1000, 1009,   -1, 1011,
 1004,   -1, 1006,   -1, 1000, 1009,   -1, 1011, 1004,   -1,
 1006,   -1,   -1, 1009,   -1, 1011,
};
}
final static short YYFINAL=11;
final static short YYMAXTOKEN=1013;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'",null,
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,"ID","STRING_CONST",
"GREATER_EQUAL","LESS_EQUAL","EQUAL","NOT_EQUAL","NUMERIC_CONST",null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,"IF","THEN","ELSE","BEGIN","END",
"END_IF","OUT","FLOAT","ULONG","SWITCH","CASE","LET","STRING","UL_F",
};
final static String yyrule[] = {
"$accept : program",
"program :",
"program : statements",
"statements : declarative_statements",
"statements : executional_statements",
"statements : statements declarative_statements",
"statements : statements executional_statements",
"statements : error '.'",
"declarative_statements : variables_declaration_statement",
"variables_declaration_statement : var_list ':' type '.'",
"variables_declaration_statement : var_list ':' type",
"variables_declaration_statement : var_list ':' '.'",
"variables_declaration_statement : var_list type '.'",
"variables_declaration_statement : ':' type '.'",
"var_list : ID",
"var_list : var_list ',' ID",
"var_list : var_list ID",
"var_list : ',' ID",
"type : ULONG",
"type : FLOAT",
"executional_statements : if_statement",
"executional_statements : if_else_statement",
"executional_statements : assignation_statement",
"executional_statements : out_statement",
"executional_statements : switch_statement",
"if_cond : condition",
"then_if_execution_statement : executional_statements",
"then_if_execution_block : explicit_delimited_execution_block",
"then_ifelse_execution_statement : executional_statements",
"then_ifelse_execution_block : explicit_delimited_execution_block",
"else_execution_block : explicit_delimited_execution_block",
"else_execution_statement : executional_statements",
"if_statement : IF '(' if_cond ')' THEN then_if_execution_statement END_IF '.'",
"if_statement : IF '(' if_cond ')' THEN then_if_execution_block END_IF '.'",
"if_statement : IF '(' if_cond ')' THEN then_if_execution_statement END_IF",
"if_statement : IF '(' if_cond ')' THEN then_if_execution_statement error '.'",
"if_statement : IF '(' if_cond ')' THEN END_IF '.'",
"if_statement : IF '(' if_cond ')' then_if_execution_statement END_IF '.'",
"if_statement : IF '(' if_cond THEN then_if_execution_statement END_IF '.'",
"if_statement : IF '(' ')' THEN then_if_execution_statement END_IF '.'",
"if_statement : IF if_cond ')' THEN then_if_execution_statement END_IF '.'",
"if_statement : '(' if_cond ')' THEN then_if_execution_statement END_IF '.'",
"if_statement : IF '(' if_cond ')' THEN then_if_execution_block END_IF",
"if_statement : IF '(' if_cond ')' THEN then_if_execution_block error '.'",
"if_statement : IF '(' if_cond ')' then_if_execution_block END_IF '.'",
"if_statement : IF '(' if_cond THEN then_if_execution_block END_IF '.'",
"if_statement : IF '(' ')' THEN then_if_execution_block END_IF '.'",
"if_statement : IF if_cond ')' THEN then_if_execution_block END_IF '.'",
"if_statement : '(' if_cond ')' THEN then_if_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement error '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond ')' then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'",
"if_else_statement : '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block error '.'",
"if_else_statement : IF '(' if_cond ')' THEN ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond ')' then_ifelse_execution_statement ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'",
"if_else_statement : IF if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'",
"if_else_statement : '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement error '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE END_IF '.'",
"if_else_statement : IF '(' if_cond ')' then_ifelse_execution_block ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'",
"if_else_statement : '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF",
"if_else_statement : IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block error '.'",
"if_else_statement : IF '(' if_cond ')' then_ifelse_execution_block ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' if_cond THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'",
"if_else_statement : IF '(' ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'",
"if_else_statement : IF if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'",
"if_else_statement : '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'",
"explicit_delimited_execution_block : BEGIN execution_block END '.'",
"explicit_delimited_execution_block : BEGIN execution_block END",
"explicit_delimited_execution_block : BEGIN execution_block error '.'",
"explicit_delimited_execution_block : BEGIN END '.'",
"explicit_delimited_execution_block : execution_block END '.'",
"execution_block : execution_block executional_statements",
"execution_block : executional_statements",
"assignation_statement : LET ID '=' expression '.'",
"assignation_statement : ID '=' expression '.'",
"assignation_statement : LET ID '=' expression",
"assignation_statement : LET ID '=' '.'",
"assignation_statement : LET ID expression '.'",
"assignation_statement : LET '=' expression '.'",
"assignation_statement : ID '=' expression",
"assignation_statement : ID '=' '.'",
"assignation_statement : '=' expression '.'",
"assignation_statement : ID '=' '(' expression ')' '.'",
"assignation_statement : LET ID '=' '(' expression ')' '.'",
"out_statement : OUT '(' STRING_CONST ')' '.'",
"out_statement : OUT '(' STRING_CONST ')'",
"out_statement : OUT '(' STRING_CONST '.'",
"out_statement : OUT '(' ')' '.'",
"out_statement : OUT STRING_CONST ')' '.'",
"out_statement : '(' STRING_CONST ')' '.'",
"switch_id : ID",
"switch_statement : SWITCH '(' switch_id ')' '{' cases_rule '}' '.'",
"switch_statement : SWITCH '(' switch_id ')' '{' cases_rule '}'",
"switch_statement : SWITCH '(' switch_id ')' '{' cases_rule error '.'",
"switch_statement : SWITCH '(' switch_id ')' '{' '}' '.'",
"switch_statement : SWITCH '(' switch_id ')' cases_rule '}' '.'",
"switch_statement : SWITCH '(' switch_id '{' cases_rule '}' '.'",
"switch_statement : SWITCH '(' ')' '{' cases_rule '}' '.'",
"switch_statement : SWITCH switch_id ')' '{' cases_rule '}' '.'",
"switch_statement : '(' switch_id ')' '{' cases_rule '}' '.'",
"cases_rule : cases_rule case_rule",
"cases_rule : case_rule",
"number_switch_case : NUMERIC_CONST",
"case_rule : CASE number_switch_case ':' explicit_delimited_execution_block",
"case_rule : CASE number_switch_case ':' executional_statements",
"case_rule : CASE number_switch_case ':' '.'",
"case_rule : CASE number_switch_case explicit_delimited_execution_block",
"case_rule : CASE ':' explicit_delimited_execution_block",
"case_rule : number_switch_case ':' explicit_delimited_execution_block",
"case_rule : CASE number_switch_case executional_statements",
"case_rule : CASE ':' executional_statements",
"case_rule : number_switch_case ':' executional_statements",
"condition : expression EQUAL expression",
"condition : expression '>' expression",
"condition : expression '<' expression",
"condition : expression GREATER_EQUAL expression",
"condition : expression LESS_EQUAL expression",
"condition : expression NOT_EQUAL expression",
"expression : expression_rule",
"expression_rule : expression '+' term",
"expression_rule : expression '-' term",
"expression_rule : term",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : NUMERIC_CONST",
"factor : '-' NUMERIC_CONST",
"factor : UL_F '(' expression_rule ')'",
"factor : UL_F '(' ')'",
"factor : UL_F expression_rule ')'",
};

//#line 1482 "gramatica.y"

private Lexer lexer;
private SymbolTable symbolTable;
private IntermediateCodeManager codeManager;
private int errorCounter = 0;
private int lineNumber = 0;
private int checkpoint = 0;
private int currentRuleError = 0;
private int validStatements = 0;
private boolean hasTokens = false;

//Lista que mantiene los identificadores a medida que los encuentra en las reglas.

/**
 * Clase de utilidad que sirve para almacenar dos valores en una lista.
 * Se utiliza para almacenar un identificador y una linea en caso de que haya sido redefinido.
 */

public class Pair<L,R> {

    private L l;
    private R r;
    public Pair(L l, R r){
        this.l = l;
        this.r = r;
    }
    public L getL(){ return l; }
    public R getR(){ return r; }
    public void setL(L l){ this.l = l; }
    public void setR(R r){ this.r = r; }
}

private List<Pair<String, Integer>> declaredVars = new ArrayList<>();
private String type = "";

private void declareVars(String type) {
	for (Pair<String, Integer> pair : this.declaredVars) {
		if (!this.symbolTable.isDeclared(pair.getL()))
			this.symbolTable.setDeclared(pair.getL(), type);
		else
			this.yyerror(ParserConstants.ERR_REDECLARED_VAR + pair.getL(), pair.getR(), true);
	}
	this.declaredVars = new ArrayList<>();
	this.type = "";
}

public void setArtifacts(Lexer lexer, SymbolTable symbolTable, IntermediateCodeManager codeManager) {
  	this.lexer = lexer;
  	this.symbolTable = symbolTable;
  	this.codeManager = codeManager;
}

void yyerror(String s) {
	if (!s.equals("syntax error")) { //Ignore yacc default error.
		this.errorCounter++;
	  	System.out.println("Error de sintaxis cerca de la línea " + this.lineNumber + ": " + s);
		this.currentRuleError++;
	}
}

void yyerror(String s, int lineNo) {
	this.errorCounter++;
  	System.out.println("Error de sintaxis en la línea " + lineNo + ": " + s);
	this.currentRuleError++;
}

void yyerror(String s, int lineNo, boolean isSemantic) {
	this.errorCounter++;
  	System.out.println("Error semántico en la línea " + lineNo + ": " + s);
	this.currentRuleError++;
}

private int yylex() {
  	Token token = this.lexer.getNextToken();
    if (token != null) {
    	this.hasTokens = true; //Al menos un token se consumió.
        this.yylval = new ParserVal(token.getLexeme());
        this.yylval.begin_line = this.lexer.getLine();
        this.yylval.end_line = this.lexer.getLine();
        this.yylval.positionInSymbolTable = token.getPositionInSymbolTable();
        this.lineNumber = this.lexer.getLine();
        return token.getIdToken();
    }
    return -1;
}

public int getErrorCount() {
	return this.errorCounter;
}

public void parse() {
	yyparse();
}
//#line 915 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 84 "gramatica.y"
{
			/*Elimino los elementos de la tabla de símbolos sin referencias.*/
			this.symbolTable.clean();
			/*Fin de compilación.*/
			if ((this.errorCounter == 0) && (this.validStatements == 0) && (this.lexer.getErrors() == 0) && this.hasTokens) {
				/*Hay al menos un token consumido pero no se detectó ninguna sentencia. Este caso se da cuando se inserta un sólo token en el programa pero no hay '.' entonces la gramática no puede ver ese caso.*/
				this.yyerror("No se reconoció ninguna sentencia válida pero se consumieron tokens.", 1); /*Se asume la línea 1.			*/
			}
			System.out.println("La compilación ha finalizado con " + this.errorCounter + " errores sintácticos y "+this.lexer.getErrors()+" errores léxicos.");
	        System.out.println("______________________________________________________________");

			if(this.errorCounter > 0 || this.lexer.getErrors() > 0)
				this.codeManager.setParsingErrors();
		}
break;
case 3:
//#line 102 "gramatica.y"
{
			this.validStatements++;
		}
break;
case 4:
//#line 106 "gramatica.y"
{
			this.validStatements++;
		}
break;
case 7:
//#line 112 "gramatica.y"
{
			this.yyerror("Al menos un error encontrado en una sentencia cercana.");
		}
break;
case 8:
//#line 121 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;
		if (currentRuleError == 0) {
			System.out.println("Se reconoció una sentencia declarativa en la línea "+this.lineNumber);
			/*Marco como declaradas las variables en la tabla de símbolos.*/
			this.declareVars(val_peek(0).temporalType);
		}
		this.currentRuleError = 0;
	}
break;
case 9:
//#line 135 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			yyval.temporalType = val_peek(1).sval;

		}
break;
case 10:
//#line 144 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 11:
//#line 151 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_VAR_TYPE_EXPECTED, val_peek(1).end_line);
		}
break;
case 12:
//#line 158 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SEMICOLON_EXPECTED, val_peek(2).end_line);
		}
break;
case 13:
//#line 165 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_VAR_LIST_EXPECTED, val_peek(2).begin_line);
		}
break;
case 14:
//#line 175 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			this.declaredVars.add(new Pair<String, Integer>(val_peek(0).sval, val_peek(0).begin_line));
		}
break;
case 15:
//#line 182 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			this.declaredVars.add(new Pair<String, Integer>(val_peek(0).sval, val_peek(2).begin_line));
		}
break;
case 16:
//#line 190 "gramatica.y"
{
			yyval.begin_line = val_peek(1).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_COMMA_EXPECTED, val_peek(0).end_line);
		}
break;
case 17:
//#line 197 "gramatica.y"
{
			yyval.begin_line = val_peek(1).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IDENTIFIER_EXPECTED, val_peek(1).begin_line);
		}
break;
case 18:
//#line 207 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 19:
//#line 212 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 20:
//#line 222 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció una sentencia if en la línea "+ val_peek(0).begin_line);
			}
			this.currentRuleError = 0;
		}
break;
case 21:
//#line 231 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció una sentencia if-else en la línea "+val_peek(0).begin_line);
			}
			this.currentRuleError = 0;
		}
break;
case 22:
//#line 240 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció una sentencia de asignación en la línea "+val_peek(0).begin_line);
			}
			this.currentRuleError = 0;
		}
break;
case 23:
//#line 249 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció la sentencia out en la línea "+val_peek(0).begin_line);
			}
			this.currentRuleError = 0;
		}
break;
case 24:
//#line 258 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció la sentencia switch en la línea "+val_peek(0).begin_line);
			}
			this.currentRuleError = 0;
		}
break;
case 25:
//#line 271 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;

		ThirdBF third = new ThirdBF(); /*Condición.*/
		this.codeManager.addBFThird(third);
		/*this.codeManager.setThenQuantityStatements(1);*/
		yyval.stackPosition = new StackReference(this.codeManager.previousThird());
		third.setValue1(yyval.stackPosition);
	}
break;
case 26:
//#line 285 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;

		yyval.stackPosition = new StackReference(this.codeManager.currentThird());
		this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
	}
break;
case 27:
//#line 296 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;

		yyval.stackPosition = new StackReference(this.codeManager.currentThird());
		this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
	}
break;
case 28:
//#line 307 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line = val_peek(0).end_line;

			Third third = new ThirdBI();
			this.codeManager.addBIThird(third);

			yyval.stackPosition = new StackReference(this.codeManager.currentThird());
			this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
		}
break;
case 29:
//#line 322 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;

		Third third = new ThirdBI();
		this.codeManager.addBIThird(third);

		yyval.stackPosition = new StackReference(this.codeManager.currentThird());
		this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
	}
break;
case 30:
//#line 336 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;
		this.codeManager.completeBI(Integer.valueOf(this.codeManager.currentThird()) + 1);
		this.codeManager.markLast();
	}
break;
case 31:
//#line 346 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;
		this.codeManager.completeBI(Integer.valueOf(this.codeManager.currentThird()) + 1);
		this.codeManager.markLast();
	}
break;
case 32:
//#line 356 "gramatica.y"
{
			yyval.begin_line = val_peek(7).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 33:
//#line 361 "gramatica.y"
{
			yyval.begin_line = val_peek(7).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 34:
//#line 367 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 35:
//#line 374 "gramatica.y"
{
			yyval.begin_line = val_peek(7).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, val_peek(2).end_line);
		}
break;
case 36:
//#line 381 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, val_peek(2).end_line);
		}
break;
case 37:
//#line 388 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, val_peek(3).end_line);
		}
break;
case 38:
//#line 395 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(4).end_line);
		}
break;
case 39:
//#line 402 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, val_peek(5).end_line);
		}
break;
case 40:
//#line 409 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(6).end_line);
		}
break;
case 41:
//#line 416 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, val_peek(6).begin_line);
		}
break;
case 42:
//#line 424 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 43:
//#line 431 "gramatica.y"
{
			yyval.begin_line = val_peek(7).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, val_peek(2).end_line);
		}
break;
case 44:
//#line 438 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, val_peek(3).end_line);
		}
break;
case 45:
//#line 445 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(4).end_line);
		}
break;
case 46:
//#line 452 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, val_peek(5).end_line);
		}
break;
case 47:
//#line 459 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(6).end_line);
		}
break;
case 48:
//#line 466 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, val_peek(6).begin_line);
		}
break;
case 49:
//#line 476 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 50:
//#line 481 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 51:
//#line 486 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 52:
//#line 491 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 53:
//#line 497 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 54:
//#line 504 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, val_peek(2).end_line);
		}
break;
case 55:
//#line 511 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, val_peek(2).end_line);
		}
break;
case 56:
//#line 518 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, val_peek(4).end_line);
		}
break;
case 57:
//#line 525 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, val_peek(5).end_line);
		}
break;
case 58:
//#line 532 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(6).end_line);
		}
break;
case 59:
//#line 539 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, val_peek(7).end_line);
		}
break;
case 60:
//#line 546 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(8).end_line);
		}
break;
case 61:
//#line 553 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, val_peek(8).begin_line);
		}
break;
case 62:
//#line 561 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 63:
//#line 568 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, val_peek(2).end_line);
		}
break;
case 64:
//#line 575 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, val_peek(4).end_line);
		}
break;
case 65:
//#line 582 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, val_peek(5).end_line);
		}
break;
case 66:
//#line 589 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(6).end_line);
		}
break;
case 67:
//#line 596 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, val_peek(7).end_line);
		}
break;
case 68:
//#line 603 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(8).end_line);
		}
break;
case 69:
//#line 610 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, val_peek(8).begin_line);
		}
break;
case 70:
//#line 618 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 71:
//#line 625 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, val_peek(2).end_line);
		}
break;
case 72:
//#line 632 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, val_peek(2).end_line);
		}
break;
case 73:
//#line 639 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, val_peek(5).end_line);
		}
break;
case 74:
//#line 646 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(0).end_line);
		}
break;
case 75:
//#line 653 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, val_peek(7).end_line);
		}
break;
case 76:
//#line 660 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(8).end_line);
		}
break;
case 77:
//#line 667 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, val_peek(8).begin_line);
		}
break;
case 78:
//#line 677 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 79:
//#line 684 "gramatica.y"
{
			yyval.begin_line = val_peek(9).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, val_peek(2).end_line);
		}
break;
case 80:
//#line 691 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, val_peek(5).end_line);
		}
break;
case 81:
//#line 698 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(6).end_line);
		}
break;
case 82:
//#line 705 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, val_peek(7).end_line);
		}
break;
case 83:
//#line 712 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(8).end_line);
		}
break;
case 84:
//#line 719 "gramatica.y"
{
			yyval.begin_line = val_peek(8).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, val_peek(8).begin_line);
		}
break;
case 85:
//#line 729 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 86:
//#line 735 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 87:
//#line 742 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_EXPECTED, val_peek(2).end_line);
		}
break;
case 88:
//#line 749 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, val_peek(2).end_line);
		}
break;
case 89:
//#line 756 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_BEGIN_EXPECTED, val_peek(2).begin_line);
		}
break;
case 90:
//#line 766 "gramatica.y"
{
			yyval.begin_line = val_peek(1).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 91:
//#line 771 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 92:
//#line 779 "gramatica.y"
{
			yyval.begin_line = val_peek(4).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			/*Semántica.*/

			String inf_type = val_peek(1).temporalType;

			if (this.symbolTable.isDeclared(val_peek(3).sval)) {
				if (this.symbolTable.getLastDeclaredType(val_peek(3).sval).equals(inf_type)) {
					this.yyerror(ParserConstants.ERR_REDECLARED_VAR, val_peek(4).begin_line, true);
					this.currentRuleError++;
				}
				else {
					this.symbolTable.appendId(val_peek(3).sval, val_peek(1).temporalType);
					/*Asigno la expresión.*/
					yyval.firstOperand = (val_peek(3).stackPosition != null) ? val_peek(3).stackPosition : val_peek(3).sval;
					yyval.secondOperand = (val_peek(1).stackPosition != null) ? val_peek(1).stackPosition : val_peek(1).sval;
					Third third = new ThirdAssignation(yyval.firstOperand, yyval.secondOperand, this.symbolTable, inf_type, val_peek(1).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
			}
			else {
				this.declaredVars.add(new Pair<String, Integer>(val_peek(3).sval,val_peek(3).begin_line));
				this.declareVars(val_peek(1).temporalType);
				/*Asigno la expresión.*/
				yyval.firstOperand = (val_peek(3).stackPosition != null) ? val_peek(3).stackPosition : val_peek(3).sval;
				yyval.secondOperand = (val_peek(1).stackPosition != null) ? val_peek(1).stackPosition : val_peek(1).sval;
				Third third = new ThirdAssignation(yyval.firstOperand, yyval.secondOperand, this.symbolTable, inf_type, val_peek(1).isConverted);
				this.codeManager.addThird(third);
				yyval.stackPosition = new StackReference(this.codeManager.currentThird());
			}
		}
break;
case 93:
//#line 814 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			/*Semántica.*/
			if (!this.symbolTable.isDeclared(val_peek(3).sval)) {
				this.yyerror(ParserConstants.ERR_NOT_DECLARED_VAR + "'"+ val_peek(3).sval +"'", val_peek(3).begin_line, true);
				this.currentRuleError++;
			}
			else {
				if (val_peek(1).temporalType != null) {
					if (this.symbolTable.getType(val_peek(3).sval).equals(val_peek(1).temporalType)) {
						yyval.firstOperand = (val_peek(3).stackPosition != null) ? val_peek(3).stackPosition : val_peek(3).sval;
						yyval.secondOperand = (val_peek(1).stackPosition != null) ? val_peek(1).stackPosition : val_peek(1).sval;
						Third third = new ThirdAssignation(yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(1).temporalType, val_peek(1).isConverted);
						this.codeManager.addThird(third);
						yyval.stackPosition = new StackReference(this.codeManager.currentThird());
					}
					else {
						this.yyerror(ParserConstants.ERR_ASSIGN_TYPES, val_peek(3).begin_line, true);
						this.currentRuleError++;
					}
				}
			}
		}
break;
case 94:
//#line 841 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 95:
//#line 848 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RIGHT_EXPR_EXPECTED, val_peek(1).end_line);
		}
break;
case 96:
//#line 855 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_ASSIGN_EXPECTED, val_peek(2).end_line);
		}
break;
case 97:
//#line 862 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LEFT_ID_EXPECTED, val_peek(3).end_line);
		}
break;
case 98:
//#line 869 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).begin_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 99:
//#line 876 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RIGHT_EXPR_EXPECTED, val_peek(1).end_line);
		}
break;
case 100:
//#line 883 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LEFT_ID_EXPECTED, val_peek(2).begin_line);
		}
break;
case 101:
//#line 890 "gramatica.y"
{
			yyval.begin_line = val_peek(5).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_UL2F_EXPECTED, val_peek(4).end_line);
		}
break;
case 102:
//#line 897 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_UL2F_EXPECTED, val_peek(4).end_line);
		}
break;
case 103:
//#line 908 "gramatica.y"
{
			yyval.begin_line = val_peek(4).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			Third third = new ThirdOut(val_peek(2).sval, val_peek(2).positionInSymbolTable);
			this.codeManager.addThird(third);
			yyval.stackPosition = new StackReference(this.codeManager.currentThird());
		}
break;
case 104:
//#line 918 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 105:
//#line 925 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(1).end_line);
		}
break;
case 106:
//#line 932 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(2).end_line);
		}
break;
case 107:
//#line 939 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(3).end_line);
		}
break;
case 108:
//#line 946 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_OUT_EXPECTED, val_peek(3).begin_line);
		}
break;
case 109:
//#line 956 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line = val_peek(0).end_line;

			if (!this.symbolTable.isDeclared(val_peek(0).sval)) {
				this.yyerror(ParserConstants.ERR_NOT_DECLARED_VAR + "'"+ val_peek(0).sval +"'", val_peek(0).begin_line, true);
				this.currentRuleError++;
			}

			this.codeManager.addSwitchDisc(val_peek(0).sval);
		}
break;
case 110:
//#line 970 "gramatica.y"
{
			yyval.begin_line = val_peek(7).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			this.codeManager.popSwitchDisc();
			this.codeManager.completeAllBI(Integer.valueOf(this.codeManager.currentThird()) + 1);
			this.codeManager.removeLastBI();
		}
break;
case 111:
//#line 980 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, val_peek(0).end_line);
		}
break;
case 112:
//#line 987 "gramatica.y"
{
			yyval.begin_line = val_peek(7).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RBRACE_EXPECTED, val_peek(2).end_line);
		}
break;
case 113:
//#line 994 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CASES_EXPECTED, val_peek(2).end_line);
		}
break;
case 114:
//#line 1001 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LBRACE_EXPECTED, val_peek(3).end_line);
		}
break;
case 115:
//#line 1008 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, val_peek(4).end_line);
		}
break;
case 116:
//#line 1015 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_ID_SWITCH, val_peek(5).end_line);
		}
break;
case 117:
//#line 1022 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(6).end_line);
		}
break;
case 118:
//#line 1029 "gramatica.y"
{
			yyval.begin_line = val_peek(6).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SWITCH_EXPECTED, val_peek(6).begin_line);
		}
break;
case 119:
//#line 1039 "gramatica.y"
{
			yyval.begin_line = val_peek(1).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			Third third = new ThirdBI();
			this.codeManager.addBIThird(third);
			yyval.stackPosition = new StackReference(this.codeManager.currentThird());
			
			yyval.stackPosition = new StackReference(this.codeManager.currentThird());
			this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
		}
break;
case 120:
//#line 1051 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			Third third = new ThirdBI();
			this.codeManager.addBIThird(third);
			yyval.stackPosition = new StackReference(this.codeManager.currentThird());
			this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
		}
break;
case 121:
//#line 1064 "gramatica.y"
{
		yyval.begin_line = val_peek(0).begin_line;
		yyval.end_line = val_peek(0).end_line;

		if (this.symbolTable.getType(val_peek(0).sval).equals(this.symbolTable.getType(this.codeManager.getSwitchDisc()))) {
			Third condition = new ThirdComparison("==", this.codeManager.getSwitchDisc(), val_peek(0).sval, this.symbolTable, this.symbolTable.getType(val_peek(0).sval), false, false);
			this.codeManager.addThird(condition);
			ThirdBF third = new ThirdBF();
			this.codeManager.addBFThird(third);
			yyval.stackPosition = new StackReference(this.codeManager.previousThird());
			third.setValue1(yyval.stackPosition);
		}
		else
			this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(0).begin_line, true);
	}
break;
case 122:
//#line 1082 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 123:
//#line 1087 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
		}
break;
case 124:
//#line 1093 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_BLOCK_EXPECTED, val_peek(1).end_line);
		}
break;
case 125:
//#line 1100 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SEMICOLON_EXPECTED, val_peek(1).end_line);
		}
break;
case 126:
//#line 1107 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONSTANT_EXPECTED, val_peek(2).end_line);
		}
break;
case 127:
//#line 1114 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CASE_EXPECTED, val_peek(2).begin_line);
		}
break;
case 128:
//#line 1121 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SEMICOLON_EXPECTED, val_peek(1).end_line);
		}
break;
case 129:
//#line 1128 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONSTANT_EXPECTED, val_peek(2).end_line);
		}
break;
case 130:
//#line 1135 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CASE_EXPECTED, val_peek(2).begin_line);
		}
break;
case 131:
//#line 1145 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					Third third = new ThirdComparison("==", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 132:
//#line 1164 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					Third third = new ThirdComparison(">", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 133:
//#line 1183 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					Third third = new ThirdComparison("<", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 134:
//#line 1202 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					Third third = new ThirdComparison(">=", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 135:
//#line 1221 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					Third third = new ThirdComparison("<=", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 136:
//#line 1240 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					Third third = new ThirdComparison("<>", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}	
		}
break;
case 137:
//#line 1263 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.sval = val_peek(0).sval;
			yyval.stackPosition = val_peek(0).stackPosition;
			yyval.temporalType = val_peek(0).temporalType;
			yyval.isConverted = val_peek(0).isConverted;
		}
break;
case 138:
//#line 1275 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.isConverted = (val_peek(2).isConverted || val_peek(0).isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("+", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addArithThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
					yyval.temporalType = val_peek(2).temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 139:
//#line 1297 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.isConverted = (val_peek(2).isConverted || val_peek(0).isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("-", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addArithThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
					yyval.temporalType = val_peek(2).temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 140:
//#line 1319 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.stackPosition = val_peek(0).stackPosition;
			yyval.temporalType = val_peek(0).temporalType;
			yyval.isConverted = val_peek(0).isConverted;
		}
break;
case 141:
//#line 1330 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.isConverted = (val_peek(2).isConverted || val_peek(0).isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("*", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addArithThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
					yyval.temporalType = val_peek(2).temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 142:
//#line 1352 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.isConverted = (val_peek(2).isConverted || val_peek(0).isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if (val_peek(2).temporalType != null && val_peek(0).temporalType != null) {
				if (val_peek(2).temporalType.equals(val_peek(0).temporalType)) {
					yyval.firstOperand = (val_peek(2).stackPosition != null) ? val_peek(2).stackPosition : val_peek(2).sval;
					yyval.secondOperand = (val_peek(0).stackPosition != null) ? val_peek(0).stackPosition : val_peek(0).sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("/", yyval.firstOperand, yyval.secondOperand, this.symbolTable, val_peek(2).temporalType, val_peek(2).isConverted, val_peek(0).isConverted);
					this.codeManager.addArithThird(third);
					yyval.stackPosition = new StackReference(this.codeManager.currentThird());
					yyval.temporalType = val_peek(2).temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, val_peek(2).begin_line, true);
					this.currentRuleError++;
				}
			}
		}
break;
case 143:
//#line 1374 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			yyval.stackPosition = val_peek(0).stackPosition;
			yyval.temporalType = val_peek(0).temporalType;
			yyval.isConverted = val_peek(0).isConverted;
		}
break;
case 144:
//#line 1385 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).end_line;

			if (!this.symbolTable.isDeclared(val_peek(0).sval)) {
				this.yyerror(ParserConstants.ERR_NOT_DECLARED_VAR + "'"+ val_peek(0).sval +"'", val_peek(0).begin_line, true);
				this.currentRuleError++;
			}
			else {
				yyval.temporalType = this.symbolTable.getType(val_peek(0).sval);
			}

		}
break;
case 145:
//#line 1399 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line   = val_peek(0).begin_line;

			Symbol symbol = this.symbolTable.getSymbol(val_peek(0).sval);
			symbol.increaseRef();

			yyval.temporalType = this.symbolTable.getType(val_peek(0).sval);

		}
break;
case 146:
//#line 1410 "gramatica.y"
{
			yyval.begin_line = val_peek(0).begin_line;
			yyval.end_line = val_peek(0).end_line;

			Symbol symbol = this.symbolTable.getSymbol(val_peek(0).sval);

			yyval.temporalType = this.symbolTable.getType(val_peek(0).sval);

			if (symbol != null) {
				if(symbol.getType().toLowerCase().equals("ulong")) {
					this.yyerror(ParserConstants.ERR_OUT_OF_BOUND_ULONG, val_peek(0).begin_line);
					this.currentRuleError++;
				}
				else {
					/*Me fijo si ya está la constante de punto flotante negativa. */
					Symbol negativeSymbol = this.symbolTable.getSymbol(('-'+val_peek(0).sval));
					if(negativeSymbol != null) {
						/*Existe. Actualizo la referencia sumando 1.*/
						negativeSymbol.increaseRef();
						/*Resto a la constante positiva una referencia ya que si está la negativa, también la positiva.*/
						symbol.decreaseRef();
					}
					else {
						/*La constante está como positiva en la tabla de símbolos, aún no se creo la misma en negativo.*/
						this.symbolTable.copyEntry(val_peek(0).sval, ('-'+val_peek(0).sval));
						/*Resto una referencia a la constante positiva.*/
						symbol.decreaseRef();
					}
				}
			}
		}
break;
case 147:
//#line 1442 "gramatica.y"
{
			yyval.begin_line = val_peek(3).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			if (val_peek(1).temporalType != null && !val_peek(1).temporalType.toLowerCase().equals("float")) {
				/*Si se realiza bien, terminará siendo de tipo float.*/
				yyval.temporalType = "float";
				yyval.firstOperand = (val_peek(1).stackPosition != null) ? val_peek(1).stackPosition : val_peek(1).sval;
				Third third = new ThirdConversion(yyval.firstOperand, val_peek(1).temporalType, this.symbolTable);
				this.codeManager.addThird(third);
				yyval.stackPosition = new StackReference(this.codeManager.currentThird());
				yyval.isConverted = true;
			}
			else
				this.yyerror(ParserConstants.ERR_CONVERSION_FLOAT, val_peek(1).begin_line, true);

		}
break;
case 148:
//#line 1464 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_EXPRESSION_EXPECTED, val_peek(1).end_line);
		}
break;
case 149:
//#line 1471 "gramatica.y"
{
			yyval.begin_line = val_peek(2).begin_line;
			yyval.end_line   = val_peek(0).end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, val_peek(2).end_line);
		}
break;
//#line 2635 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
