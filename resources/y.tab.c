#ifndef lint
static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";
#endif
#define YYBYACC 1
#line 2 "gramatica.y"
	package Parser;

  	import Lexer.*;
    import Lexer.SymbolTable.SymbolTable;
#line 11 "y.tab.c"
#define ID 501
#define STRING_CONST 502
#define GREATER_EQUAL 503
#define LESS_EQUAL 504
#define EQUAL 505
#define NOT_EQUAL 506
#define NUMERIC_CONST 507
#define IF 1000
#define THEN 1001
#define ELSE 1002
#define BEGIN 1003
#define END 1004
#define END_IF 1005
#define OUT 1006
#define FLOAT 1007
#define ULONG 1008
#define SWITCH 1009
#define CASE 1010
#define LET 1011
#define STRING 1012
#define UL_F 1013
#define YYERRCODE 256
short yylhs[] = {                                        -1,
    0,    0,    1,    1,    1,    1,    2,    2,    2,    2,
    2,    6,    2,    4,    4,    4,    4,    5,    5,    3,
    3,    3,    7,    7,    7,    7,    7,    7,    7,    7,
    7,    7,    7,    7,    7,    7,    7,    7,    7,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,    8,    8,    8,    8,
    8,    8,    8,    8,    8,    8,   11,   12,    9,   10,
   10,   10,   10,   10,   10,   13,   13,   13,   14,   14,
   14,   15,   15,
};
short yylen[] = {                                         2,
    0,    1,    1,    1,    2,    2,    4,    3,    3,    3,
    3,    0,    5,    1,    3,    2,    2,    1,    1,    1,
    1,    1,    8,    8,    7,    8,    7,    7,    7,    7,
    7,    7,    7,    8,    7,    7,    7,    7,    7,   10,
   10,   10,   10,    9,   10,    9,    9,    9,    9,    9,
    9,    9,    9,   10,    9,    9,    9,    9,    9,    9,
    9,   10,    9,    9,    9,    9,    9,    9,    9,   10,
   11,    9,    9,    9,    9,    9,    4,    2,    5,    3,
    3,    3,    3,    3,    3,    3,    3,    1,    3,    3,
    1,    1,    1,
};
short yydefred[] = {                                      0,
   14,    0,    0,    0,    0,    0,    0,    0,    3,    4,
    0,   20,   21,   22,   92,   93,    0,    0,    0,    0,
   91,    0,   19,   18,    0,   17,    0,    5,    6,   16,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   11,    0,    9,
    0,   15,   10,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   89,   90,    0,    0,
    7,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   79,    0,    0,   13,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   78,    0,    0,   30,    0,    0,   37,    0,
    0,   29,    0,    0,   36,    0,    0,   27,    0,    0,
    0,    0,    0,    0,    0,    0,   28,    0,    0,   35,
    0,    0,   31,    0,    0,   38,    0,    0,   32,    0,
    0,   39,   77,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   26,    0,    0,    0,   23,   34,    0,
    0,    0,   24,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   50,   58,   66,   74,   49,
   57,   65,   73,   47,   55,   46,    0,    0,    0,    0,
   63,    0,    0,    0,    0,   48,   56,   64,   72,   51,
   59,   67,   75,   52,   60,   68,   76,   45,   40,   54,
   41,   62,   42,    0,   70,   43,   71,
};
short yydgoto[] = {                                       7,
    8,    9,   10,   11,   25,   54,   12,   13,   14,   18,
   75,   87,   19,   20,   21,
};
short yysindex[] = {                                    -38,
    0,  -32, -463, -873, -459, -398,    0,  -38,    0,    0,
   16,    0,    0,    0,    0,    0,  -17,    6,   27,  -16,
    0,    3,    0,    0,    5,    0,   47,    0,    0,    0,
  -10, -408,   64, -885,  -34, -880, -398, -398, -398, -398,
 -398, -398, -398, -398, -398, -398, -398,    0, -865,    0,
   83,    0,    0,   89,  -11,  -11,  -25,  -11,   57,   57,
   57,   57,   57,   57,  -16,  -16,    0,    0,   69,  -11,
    0,   95,    0, -950, -939, -934, -926,  -37, -924, -907,
 -872, -857,    0, -843, -832,    0,  -40,  -11,   97,  -11,
  118,  -11,  120,  -11,  122,  -11,  126, -251, -245,  -11,
  130,  -11,  133,  -11,  135,  -11,  136,  -11,  138,  -11,
  139,  140,    0, -818, -817,    0, -816, -815,    0, -813,
 -812,    0, -810, -809,    0, -808, -807,    0,  153,  -21,
  154,  155,  -12,  156, -802, -801,    0, -800, -799,    0,
 -798, -797,    0, -796, -795,    0, -794, -793,    0, -792,
 -791,    0,    0,  169,  170,  171,  172,  173,  174,  175,
  176,  177,  178,    0,  179, -243, -242,    0,    0,  180,
 -240, -234,    0,  181,  182,  183,  184,  185,  186,  187,
  188,  189,  190,  191,  192,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  193,  194,  195,  196,
    0,  197,  198,  -28,  199,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  200,    0,    0,    0,
};
short yyrindex[] = {                                    247,
    0,    0,    0,    0,    0,    0,    0,  248,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   -6,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  201,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    4,    0,    0,    0,    0,    0,    0,    0,  -31,  -29,
  -20,  -18,  -14,   -7,   39,   51,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    1,    0,    0,    9,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   17,    0,   25,
    0,    0,   33,    0,   46,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
short yygindex[] = {                                      0,
    0,  241,   50,    0,   19,    0,    0,    0,    0,   26,
   61,    0,   85,  131,  132,
};
#define YYTABLESIZE 1057
short yytable[] = {                                       6,
   25,    6,    6,    8,  129,    5,   57,   17,   33,   83,
  132,   84,  197,  199,    6,  202,   44,  225,    6,    4,
   80,  204,   85,   34,   53,   45,   81,    6,    6,   33,
   46,   27,   61,   82,   88,   50,   88,   22,   88,   88,
   25,   26,   35,    8,   25,   69,   36,    8,   33,   51,
   48,   88,   33,   88,   89,   88,   44,   29,   25,   32,
   44,    8,   90,   47,   53,   91,   33,   92,   53,   43,
   93,   44,   61,   31,   44,   94,   61,  100,   95,   86,
  101,   86,   53,   86,   86,   69,   42,   49,   41,   69,
   61,   87,   52,   87,  102,   87,   87,  103,   86,   43,
   86,   44,   15,   69,   74,   76,   79,   81,   16,   53,
   87,   43,   87,   44,   83,   55,   77,   80,   82,   84,
   58,   59,   60,   61,   62,   63,   64,   98,   71,  104,
   85,   69,  105,   23,   24,   70,  113,  114,   99,  117,
   86,  120,  116,  123,  106,  126,   72,  107,  115,  135,
  118,  138,  121,  141,  124,  144,  127,  147,  108,  150,
  136,  109,  139,  119,  142,  122,  145,  125,  148,  110,
  151,  128,  111,   65,   66,  137,   67,   68,  140,  166,
  143,  146,  171,  149,  152,  153,  154,  155,  156,  157,
  167,  158,  159,  172,  160,  161,  162,  163,  164,  168,
  169,  173,  174,  175,  176,  177,  178,  179,  180,  181,
  182,  183,  184,  185,  186,  187,  188,  189,  190,  191,
  192,  193,  194,  195,  196,  201,  206,  207,  208,  209,
  210,  211,  212,  213,  214,  215,  216,  217,  218,  219,
  220,  221,  222,  223,  226,  227,    1,    2,   28,    0,
    0,    0,    0,    0,    0,    0,   25,    0,   12,    0,
    0,    0,    0,    0,   33,    0,    0,    0,    0,    0,
    0,    0,   44,    0,    0,    0,    0,    0,    0,    0,
   53,    0,    0,    0,    0,    0,    0,    0,   61,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   69,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    1,    0,    0,    0,    0,    0,   15,    0,
    0,    0,    0,    0,   16,    0,    0,    0,    0,    0,
    0,    0,    0,   15,    0,    0,    0,    0,    0,   16,
    0,    0,    0,    0,    0,    0,   88,   88,   88,   88,
    0,   25,    0,    0,    8,    0,    0,    0,    0,   33,
    0,    0,    0,    0,    0,    0,   30,   44,    0,    0,
    0,    0,    0,    0,    0,   53,    0,    0,    0,   37,
   38,   39,   40,   61,    0,    0,    0,    0,    0,    0,
    0,   86,   86,   86,   86,    0,   69,    0,    0,    0,
    0,    0,    0,   87,   87,   87,   87,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  130,    0,    0,  131,    0,    0,  133,    0,    0,  134,
    0,  198,  200,    0,  203,    0,    0,    0,    0,    0,
  205,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    2,
    0,    2,    2,  112,   96,   73,   56,   97,    0,   83,
    3,   84,    3,    3,    2,   78,  224,   73,    2,    0,
   80,   73,   85,  165,    0,    3,   81,    2,    2,    3,
   73,   73,  170,   82,   88,    0,   23,   24,    3,    3,
   25,    0,   25,    8,   25,   25,    0,    0,   33,    0,
   33,   25,   33,   33,    8,    0,   44,    0,   44,   33,
   44,   44,   23,   24,   53,    0,   53,   44,   53,   53,
    0,    0,   61,    0,   61,   53,   61,   61,    0,   86,
    0,    0,    0,   61,    0,   69,    0,   69,    0,   69,
   69,   87,    0,    0,    0,    0,   69,
};
short yycheck[] = {                                      40,
    0,   40,   40,    0,  256,   44,   41,   40,    0,   41,
  256,   41,  256,  256,   40,  256,    0,   46,   40,   58,
   41,  256,   41,   41,    0,   42,   41,   40,   40,   11,
   47,    6,    0,   41,   41,   46,   43,  501,   45,   46,
   40,  501,   17,   40,   44,    0,   41,   44,   40,   31,
   46, 1002,   44,   60, 1005,   62,   40,    8,   58,   44,
   44,   58, 1002,   61,   40, 1005,   58, 1002,   44,   43,
 1005,   45,   40,   58,   58, 1002,   44, 1002, 1005,   41,
 1005,   43,   58,   45,   46,   40,   60,   41,   62,   44,
   58,   41,  501,   43, 1002,   45,   46, 1005,   60,   43,
   62,   45,  501,   58,   55,   56,   57,   58,  507,   46,
   60,   43,   62,   45,   46, 1001,   56,   57,   58,   70,
 1001,   37,   38,   39,   40,   41,   42,   78,   46, 1002,
   70,   47, 1005, 1007, 1008, 1001,   87,   88,   78,   90,
   46,   92,   46,   94, 1002,   96,   58, 1005,   88,  100,
   90,  102,   92,  104,   94,  106,   96,  108, 1002,  110,
  100, 1005,  102,   46,  104,   46,  106,   46,  108, 1002,
  110,   46, 1005,   43,   44,   46,   45,   46,   46,  130,
   46,   46,  133,   46,   46,   46, 1005, 1005, 1005, 1005,
  130, 1005, 1005,  133, 1005, 1005, 1005, 1005,   46,   46,
   46,   46, 1005, 1005, 1005, 1005, 1005, 1005, 1005, 1005,
 1005, 1005, 1005, 1005,   46,   46,   46,   46,   46,   46,
   46,   46,   46,   46,   46,   46,   46,   46,   46,   46,
   46,   46,   46,   46,   46,   46,   46,   46,   46,   46,
   46,   46,   46,   46,   46,   46,    0,    0,    8,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,   58,   -1,
   -1,   -1,   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  256,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  256,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  501,   -1,   -1,   -1,   -1,   -1,  501,   -1,
   -1,   -1,   -1,   -1,  507,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,  501,   -1,   -1,   -1,   -1,   -1,  507,
   -1,   -1,   -1,   -1,   -1,   -1,  503,  504,  505,  506,
   -1,  501,   -1,   -1,  501,   -1,   -1,   -1,   -1,  501,
   -1,   -1,   -1,   -1,   -1,   -1,  501,  501,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  501,   -1,   -1,   -1,  503,
  504,  505,  506,  501,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  503,  504,  505,  506,   -1,  501,   -1,   -1,   -1,
   -1,   -1,   -1,  503,  504,  505,  506,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
 1002,   -1,   -1, 1005,   -1,   -1, 1002,   -1,   -1, 1005,
   -1, 1005, 1005,   -1, 1005,   -1,   -1,   -1,   -1,   -1,
 1005,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1, 1000,
   -1, 1000, 1000, 1004, 1002, 1003, 1001, 1005,   -1, 1001,
 1011, 1001, 1011, 1011, 1000, 1001, 1005, 1003, 1000,   -1,
 1001, 1003, 1001, 1005,   -1, 1011, 1001, 1000, 1000, 1011,
 1003, 1003, 1005, 1001, 1001,   -1, 1007, 1008, 1011, 1011,
 1000,   -1, 1002, 1000, 1004, 1005,   -1,   -1, 1000,   -1,
 1002, 1011, 1004, 1005, 1011,   -1, 1000,   -1, 1002, 1011,
 1004, 1005, 1007, 1008, 1000,   -1, 1002, 1011, 1004, 1005,
   -1,   -1, 1000,   -1, 1002, 1011, 1004, 1005,   -1, 1001,
   -1,   -1,   -1, 1011,   -1, 1000,   -1, 1002,   -1, 1004,
 1005, 1001,   -1,   -1,   -1,   -1, 1011,
};
#define YYFINAL 7
#ifndef YYDEBUG
#define YYDEBUG 0
#endif
#define YYMAXTOKEN 1013
#if YYDEBUG
char *yyname[] = {
"end-of-file",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,"'('","')'","'*'","'+'","','","'-'","'.'","'/'",0,0,0,0,0,0,0,0,0,0,
"':'",0,"'<'","'='","'>'",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,"ID","STRING_CONST","GREATER_EQUAL","LESS_EQUAL","EQUAL",
"NOT_EQUAL","NUMERIC_CONST",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,"IF","THEN","ELSE","BEGIN",
"END","END_IF","OUT","FLOAT","ULONG","SWITCH","CASE","LET","STRING","UL_F",
};
char *yyrule[] = {
"$accept : program",
"program :",
"program : statements",
"statements : declarative_statements",
"statements : executional_statements",
"statements : statements declarative_statements",
"statements : statements executional_statements",
"declarative_statements : var_list ':' type '.'",
"declarative_statements : var_list ':' type",
"declarative_statements : var_list ':' '.'",
"declarative_statements : var_list type '.'",
"declarative_statements : ':' type '.'",
"$$1 :",
"declarative_statements : var_list type $$1 ':' '.'",
"var_list : ID",
"var_list : var_list ',' ID",
"var_list : var_list ID",
"var_list : ',' ID",
"type : ULONG",
"type : FLOAT",
"executional_statements : if_statement",
"executional_statements : if_else_statement",
"executional_statements : assignation_statement",
"if_statement : IF '(' condition ')' THEN executional_statements END_IF '.'",
"if_statement : IF '(' condition ')' THEN explicit_delimited_execution_block END_IF '.'",
"if_statement : IF '(' condition ')' THEN executional_statements END_IF",
"if_statement : IF '(' condition ')' THEN executional_statements error '.'",
"if_statement : IF '(' condition ')' THEN END_IF '.'",
"if_statement : IF '(' condition ')' executional_statements END_IF '.'",
"if_statement : IF '(' condition THEN executional_statements END_IF '.'",
"if_statement : IF '(' ')' THEN executional_statements END_IF '.'",
"if_statement : IF condition ')' THEN executional_statements END_IF '.'",
"if_statement : '(' condition ')' THEN executional_statements END_IF '.'",
"if_statement : IF '(' condition ')' THEN explicit_delimited_execution_block END_IF",
"if_statement : IF '(' condition ')' THEN explicit_delimited_execution_block error '.'",
"if_statement : IF '(' condition ')' explicit_delimited_execution_block END_IF '.'",
"if_statement : IF '(' condition THEN explicit_delimited_execution_block END_IF '.'",
"if_statement : IF '(' ')' THEN explicit_delimited_execution_block END_IF '.'",
"if_statement : IF condition ')' THEN explicit_delimited_execution_block END_IF '.'",
"if_statement : '(' condition ')' THEN explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE executional_statements END_IF",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE executional_statements error '.'",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition ')' executional_statements ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition THEN executional_statements ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' ')' THEN executional_statements ELSE executional_statements END_IF '.'",
"if_else_statement : IF condition ')' THEN executional_statements ELSE executional_statements END_IF '.'",
"if_else_statement : '(' condition ')' THEN executional_statements ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE explicit_delimited_execution_block END_IF",
"if_else_statement : IF '(' condition ')' THEN executional_statements ELSE explicit_delimited_execution_block error '.'",
"if_else_statement : IF '(' condition ')' THEN ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition ')' executional_statements ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition THEN executional_statements ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' ')' THEN executional_statements ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF condition ')' THEN executional_statements ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : '(' condition ')' THEN executional_statements ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE executional_statements END_IF",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE executional_statements error '.'",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE END_IF '.'",
"if_else_statement : IF '(' condition ')' explicit_delimited_execution_block ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition THEN explicit_delimited_execution_block ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' ')' THEN explicit_delimited_execution_block ELSE executional_statements END_IF '.'",
"if_else_statement : IF condition ')' THEN explicit_delimited_execution_block ELSE executional_statements END_IF '.'",
"if_else_statement : '(' condition ')' THEN explicit_delimited_execution_block ELSE executional_statements END_IF '.'",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block error '.'",
"if_else_statement : IF '(' condition ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block error END_IF '.'",
"if_else_statement : IF '(' condition ')' explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' condition THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF '(' ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : IF condition ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF '.'",
"if_else_statement : '(' condition ')' THEN explicit_delimited_execution_block ELSE explicit_delimited_execution_block END_IF '.'",
"explicit_delimited_execution_block : BEGIN execution_block END '.'",
"execution_block : execution_block executional_statements",
"assignation_statement : LET ID '=' expression '.'",
"condition : expression EQUAL expression",
"condition : expression '>' expression",
"condition : expression '<' expression",
"condition : expression GREATER_EQUAL expression",
"condition : expression LESS_EQUAL expression",
"condition : expression NOT_EQUAL expression",
"expression : expression '+' term",
"expression : expression '-' term",
"expression : term",
"term : term '*' factor",
"term : term '/' factor",
"term : factor",
"factor : ID",
"factor : NUMERIC_CONST",
};
#endif
#ifndef YYSTYPE
typedef int YYSTYPE;
#endif
#define yyclearin (yychar=(-1))
#define yyerrok (yyerrflag=0)
#ifdef YYSTACKSIZE
#ifndef YYMAXDEPTH
#define YYMAXDEPTH YYSTACKSIZE
#endif
#else
#ifdef YYMAXDEPTH
#define YYSTACKSIZE YYMAXDEPTH
#else
#define YYSTACKSIZE 500
#define YYMAXDEPTH 500
#endif
#endif
int yydebug;
int yynerrs;
int yyerrflag;
int yychar;
short *yyssp;
YYSTYPE *yyvsp;
YYSTYPE yyval;
YYSTYPE yylval;
short yyss[YYSTACKSIZE];
YYSTYPE yyvs[YYSTACKSIZE];
#define yystacksize YYSTACKSIZE
#line 231 "gramatica.y"

	private Lexer lexer;
	private SymbolTable symbolTable;
	private int errorCounter = 0;

	public void setArtifacts(Lexer lexer, SymbolTable symbolTable) {
  		this.lexer = lexer;
  		this.symbolTable = symbolTable;
  	}

  	void yyerror(String s) {
  		System.out.println("Error de sintaxis en la línea " + this.lexer.getLine() + ": " + s);
		this.errorCounter++;
	}

  	private int yylex() {
  		Token token = this.lexer.getNextToken();
        if (token != null) {

          this.yylval = new ParserVal(token.getLexeme());
          //this.yylineno = this.lexer.getLineNumber();
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
#line 553 "y.tab.c"
#define YYABORT goto yyabort
#define YYACCEPT goto yyaccept
#define YYERROR goto yyerrlab
int
yyparse()
{
    register int yym, yyn, yystate;
#if YYDEBUG
    register char *yys;
    extern char *getenv();

    if (yys = getenv("YYDEBUG"))
    {
        yyn = *yys;
        if (yyn >= '0' && yyn <= '9')
            yydebug = yyn - '0';
    }
#endif

    yynerrs = 0;
    yyerrflag = 0;
    yychar = (-1);

    yyssp = yyss;
    yyvsp = yyvs;
    *yyssp = yystate = 0;

yyloop:
    if (yyn = yydefred[yystate]) goto yyreduce;
    if (yychar < 0)
    {
        if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, reading %d (%s)\n", yystate,
                    yychar, yys);
        }
#endif
    }
    if ((yyn = yysindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: state %d, shifting to state %d (%s)\n",
                    yystate, yytable[yyn],yyrule[yyn]);
#endif
        if (yyssp >= yyss + yystacksize - 1)
        {
            goto yyoverflow;
        }
        *++yyssp = yystate = yytable[yyn];
        *++yyvsp = yylval;
        yychar = (-1);
        if (yyerrflag > 0)  --yyerrflag;
        goto yyloop;
    }
    if ((yyn = yyrindex[yystate]) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
    {
        yyn = yytable[yyn];
        goto yyreduce;
    }
    if (yyerrflag) goto yyinrecovery;
#ifdef lint
    goto yynewerror;
#endif
yynewerror:
    yyerror("syntax error");
#ifdef lint
    goto yyerrlab;
#endif
yyerrlab:
    ++yynerrs;
yyinrecovery:
    if (yyerrflag < 3)
    {
        yyerrflag = 3;
        for (;;)
        {
            if ((yyn = yysindex[*yyssp]) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: state %d, error recovery shifting\
 to state %d\n", *yyssp, yytable[yyn]);
#endif
                if (yyssp >= yyss + yystacksize - 1)
                {
                    goto yyoverflow;
                }
                *++yyssp = yystate = yytable[yyn];
                *++yyvsp = yylval;
                goto yyloop;
            }
            else
            {
#if YYDEBUG
                if (yydebug)
                    printf("yydebug: error recovery discarding state %d\n",
                            *yyssp);
#endif
                if (yyssp <= yyss) goto yyabort;
                --yyssp;
                --yyvsp;
            }
        }
    }
    else
    {
        if (yychar == 0) goto yyabort;
#if YYDEBUG
        if (yydebug)
        {
            yys = 0;
            if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
            if (!yys) yys = "illegal-symbol";
            printf("yydebug: state %d, error recovery discards token %d (%s)\n",
                    yystate, yychar, yys);
        }
#endif
        yychar = (-1);
        goto yyloop;
    }
yyreduce:
#if YYDEBUG
    if (yydebug)
        printf("yydebug: state %d, reducing by rule %d (%s)\n",
                yystate, yyn, yyrule[yyn]);
#endif
    yym = yylen[yyn];
    yyval = yyvsp[1-yym];
    switch (yyn)
    {
case 2:
#line 39 "gramatica.y"
{
			System.out.println("La compilación ha finalizado con " + this.errorCounter + " errores.");
		}
break;
case 8:
#line 57 "gramatica.y"
{
			this.yyerror("se esperaba un '.' al finalizar la sentencia declarativa.");
		}
break;
case 9:
#line 61 "gramatica.y"
{
			this.yyerror("se esperaba el tipo de las variables en al sentencia declarativa.");
		}
break;
case 10:
#line 65 "gramatica.y"
{
			this.yyerror("se esperaba un ':' en la sentencia declarativa.");
		}
break;
case 11:
#line 69 "gramatica.y"
{
			this.yyerror("se esperaba una lista de variables en la sentencia declarativa.");
		}
break;
case 12:
#line 73 "gramatica.y"
{
			this.yyerror("se esperaba un '.' al finalizar la sentencia declarativa.");
			this.yyerror("se esperaba un ':' en la sentencia declarativa.");
		}
break;
case 13:
#line 78 "gramatica.y"
{
			this.yyerror("se esperaba una lista de variables en la sentencia declarativa.");
			this.yyerror("se esperaba el tipo de las variables en al sentencia declarativa.");			
		}
break;
case 16:
#line 89 "gramatica.y"
{
			this.yyerror("se esperaba ',' entre cada identificador.");
		}
break;
case 17:
#line 93 "gramatica.y"
{
			this.yyerror("se esperaba un identificador de variable antes de la ','.");
		}
break;
case 20:
#line 107 "gramatica.y"
{
			System.out.println("Se reconoció una sentencia if.");
		}
break;
case 21:
#line 111 "gramatica.y"
{
			System.out.println("Se reconoció una sentencia if-else.");
		}
break;
case 22:
#line 115 "gramatica.y"
{
			System.out.println("Se reconoció una sentencia de asignación.");
		}
break;
#line 767 "y.tab.c"
    }
    yyssp -= yym;
    yystate = *yyssp;
    yyvsp -= yym;
    yym = yylhs[yyn];
    if (yystate == 0 && yym == 0)
    {
#if YYDEBUG
        if (yydebug)
            printf("yydebug: after reduction, shifting from state 0 to\
 state %d\n", YYFINAL);
#endif
        yystate = YYFINAL;
        *++yyssp = YYFINAL;
        *++yyvsp = yyval;
        if (yychar < 0)
        {
            if ((yychar = yylex()) < 0) yychar = 0;
#if YYDEBUG
            if (yydebug)
            {
                yys = 0;
                if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
                if (!yys) yys = "illegal-symbol";
                printf("yydebug: state %d, reading %d (%s)\n",
                        YYFINAL, yychar, yys);
            }
#endif
        }
        if (yychar == 0) goto yyaccept;
        goto yyloop;
    }
    if ((yyn = yygindex[yym]) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn];
    else
        yystate = yydgoto[yym];
#if YYDEBUG
    if (yydebug)
        printf("yydebug: after reduction, shifting from state %d \
to state %d\n", *yyssp, yystate);
#endif
    if (yyssp >= yyss + yystacksize - 1)
    {
        goto yyoverflow;
    }
    *++yyssp = yystate;
    *++yyvsp = yyval;
    goto yyloop;
yyoverflow:
    yyerror("yacc stack overflow");
yyabort:
    return (1);
yyaccept:
    return (0);
}
