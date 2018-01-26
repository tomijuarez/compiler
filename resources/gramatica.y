%{
	package Parser;

  	import Lexer.*;
    import Lexer.SymbolTable.SymbolTable;
    import Lexer.SymbolTable.Symbol;
    import Parser.IntermediateCode.*;
    import java.util.ArrayList;
    import java.util.List;

    class ParserConstants {
    	//Parser errors.
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
    
    	//Errores semánticos.
    	public static final String ERR_NOT_DECLARED_VAR = "no se ha declarado la variable ";
    	public static final String ERR_ASSIGN_TYPES 	= "el identificador del lado izquierdo debe ser del mismo tipo que la expresión del lado derecho en una asignación.";
    	public static final String ERR_REDECLARED_VAR   = "ya se ha declarado la variable ";
    	public static final String ERR_NOT_EQUAL_TYPES  = "se debe operar únicamente con operadores del mismo tipo ";
    	public static final String ERR_CONVERSION_FLOAT = "no es posible convertir una expresión de tipo float a float.";

    }
%}
      
      
//Listado de Token en el mismo orden que estan declarados en el lexico.
%token ID 				501
%token STRING_CONST 	502
%token GREATER_EQUAL	503
%token LESS_EQUAL		504
%token EQUAL			505
%token NOT_EQUAL		506
%token NUMERIC_CONST	507
%token IF 				1000
%token THEN 			1001
%token ELSE 			1002
%token BEGIN 			1003
%token END 				1004
%token END_IF 			1005
%token OUT 				1006
%token FLOAT 			1007
%token ULONG 			1008
%token SWITCH 			1009
%token CASE 			1010
%token LET  			1011
%token STRING 			1012
%token UL_F 			1013

%%

program:
		/*BLANK*/
	|	statements
		{
			//Elimino los elementos de la tabla de símbolos sin referencias.
			this.symbolTable.clean();
			//Fin de compilación.
			if ((this.errorCounter == 0) && (this.validStatements == 0) && (this.lexer.getErrors() == 0) && this.hasTokens) {
				//Hay al menos un token consumido pero no se detectó ninguna sentencia. Este caso se da cuando se inserta un sólo token en el programa pero no hay '.' entonces la gramática no puede ver ese caso.
				this.yyerror("No se reconoció ninguna sentencia válida pero se consumieron tokens.", 1); //Se asume la línea 1.			
			}
			System.out.println("La compilación ha finalizado con " + this.errorCounter + " errores sintácticos y "+this.lexer.getErrors()+" errores léxicos.");
	        System.out.println("______________________________________________________________");

			if(this.errorCounter > 0 || this.lexer.getErrors() > 0)
				this.codeManager.setParsingErrors();
		}
	;

statements:
		declarative_statements
		{
			this.validStatements++;
		}
	|	executional_statements
		{
			this.validStatements++;
		}
	|	statements declarative_statements
	|	statements executional_statements
	|	error '.'
		{
			this.yyerror("Al menos un error encontrado en una sentencia cercana.");
		}
	;

/*De acá en más se describe la gramática de las sentencias declarativas.*/

declarative_statements:
	variables_declaration_statement
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;
		if (currentRuleError == 0) {
			System.out.println("Se reconoció una sentencia declarativa en la línea "+this.lineNumber);
			//Marco como declaradas las variables en la tabla de símbolos.
			this.declareVars($1.temporalType);
		}
		this.currentRuleError = 0;
	}
;

variables_declaration_statement:
		var_list ':' type '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;

			$$.temporalType = $3.sval;

		}
		/*De acá en más se describe la gramática de error.*/
	|	var_list ':' type 
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $3.end_line);
		}
	|	var_list ':' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_VAR_TYPE_EXPECTED, $2.end_line);
		}
	|	var_list type '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SEMICOLON_EXPECTED, $1.end_line);
		}
	|	':' type '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_VAR_LIST_EXPECTED, $1.begin_line);
		}
	;

var_list:
		ID
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;

			this.declaredVars.add(new Pair<String, Integer>($1.sval, $1.begin_line));
		}
	|	var_list ',' ID
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;

			this.declaredVars.add(new Pair<String, Integer>($3.sval, $1.begin_line));
		}
		/*De acá en más se describe la gramática de error.*/
	|	var_list ID
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $2.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_COMMA_EXPECTED, $2.end_line);
		}
	|	',' ID
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $2.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IDENTIFIER_EXPECTED, $1.begin_line);
		}
	;	

type:
		ULONG
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
		}
	|	FLOAT
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
		}
	;

/*De acá en más se describe la gramática de las sentencias de ejecución.*/

executional_statements:
		if_statement
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció una sentencia if en la línea "+ $1.begin_line);
			}
			this.currentRuleError = 0;
		}
	|	if_else_statement
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció una sentencia if-else en la línea "+$1.begin_line);
			}
			this.currentRuleError = 0;
		}
	|	assignation_statement
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció una sentencia de asignación en la línea "+$1.begin_line);
			}
			this.currentRuleError = 0;
		}
	|	out_statement
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció la sentencia out en la línea "+$1.begin_line);
			}
			this.currentRuleError = 0;
		}
	|	switch_statement
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			if (this.currentRuleError == 0) {
				System.out.println("Se reconoció la sentencia switch en la línea "+$1.begin_line);
			}
			this.currentRuleError = 0;
		}
	;

/*indirección de la condición de if para facilitar la generación de código.*/
if_cond:
	condition
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;

		ThirdBF third = new ThirdBF(); //Condición.
		this.codeManager.addBFThird(third);
		//this.codeManager.setThenQuantityStatements(1);
		$$.stackPosition = new StackReference(this.codeManager.previousThird());
		third.setValue1($$.stackPosition);
	}
;

then_if_execution_statement:
	executional_statements
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;

		$$.stackPosition = new StackReference(this.codeManager.currentThird());
		this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
	}
;

then_if_execution_block:
	explicit_delimited_execution_block
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;

		$$.stackPosition = new StackReference(this.codeManager.currentThird());
		this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
	}
;

then_ifelse_execution_statement:
		executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line = $1.end_line;

			Third third = new ThirdBI();
			this.codeManager.addBIThird(third);

			$$.stackPosition = new StackReference(this.codeManager.currentThird());
			this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
		}
		
;

then_ifelse_execution_block:
	explicit_delimited_execution_block
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;

		Third third = new ThirdBI();
		this.codeManager.addBIThird(third);

		$$.stackPosition = new StackReference(this.codeManager.currentThird());
		this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
	}
;

else_execution_block:
	explicit_delimited_execution_block
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;
		this.codeManager.completeBI(Integer.valueOf(this.codeManager.currentThird()) + 1);
		this.codeManager.markLast();
	}
;

else_execution_statement:
	executional_statements 
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;
		this.codeManager.completeBI(Integer.valueOf(this.codeManager.currentThird()) + 1);
		this.codeManager.markLast();
	}
;

if_statement:
		IF '(' if_cond ')' THEN then_if_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $8.end_line;
		}
	|	IF '(' if_cond ')' THEN then_if_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $8.end_line;
		}
	/*De acá en más se describe la gramática de error con una sola sentencia de ejecución en el cuerpo del if.*/
	|	IF '(' if_cond ')' THEN then_if_execution_statement END_IF
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $7.end_line);
		}
	|	IF '(' if_cond ')' THEN then_if_execution_statement error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $8.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, $6.end_line);
		}
	|	IF '(' if_cond ')' THEN END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, $5.end_line);
		}
	|	IF '(' if_cond ')' then_if_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, $4.end_line);
		}
	|	IF '(' if_cond THEN then_if_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	IF '(' ')' THEN then_if_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, $2.end_line);
		}
	|	IF if_cond ')' THEN then_if_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' if_cond ')' THEN then_if_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, $1.begin_line);
		}
	/*De acá en más se describe la gramática de error con un bloque de sentencias de ejecución en el cuerpo del if.*/
	|	IF '(' if_cond ')' THEN then_if_execution_block END_IF
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $7.end_line);
		}
	|	IF '(' if_cond ')' THEN then_if_execution_block error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $8.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, $6.end_line);
		}
	|	IF '(' if_cond ')' then_if_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, $4.end_line);
		}
	|	IF '(' if_cond THEN then_if_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	IF '(' ')' THEN then_if_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, $2.end_line);
		}
	|	IF if_cond ')' THEN then_if_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' if_cond ')' THEN then_if_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, $1.begin_line);
		}
	;

if_else_statement:
		IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
		}
	/*De acá en más se describe la gramática de error con una sentencia de ejecución en el cuerpo del if y una sola sentencia de ejecución en el cuerpo del else.*/
	|	IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $9.end_line);
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, $8.end_line);
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, $7.end_line);
		}
	|	IF '(' if_cond ')' THEN ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, $5.end_line);
		}
	|	IF '(' if_cond ')' then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, $4.end_line);
		}
	|	IF '(' if_cond THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	IF '(' ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, $2.end_line);
		}
	|	IF if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, $1.begin_line);
		}
	/*De acá en más se describe la gramática de error con una sentencia de ejecución en el cuerpo del if y un bloque de sentencias de ejecución en el cuerpo del else.*/	
	|	IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $9.end_line);
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, $8.end_line);
		}
	|	IF '(' if_cond ')' THEN ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, $5.end_line);
		}
	|	IF '(' if_cond ')' then_ifelse_execution_statement ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, $4.end_line);
		}
	|	IF '(' if_cond THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	IF '(' ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, $2.end_line);
		}
	|	IF if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' if_cond ')' THEN then_ifelse_execution_statement ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, $1.begin_line);
		}
	/*De acá en más se describe la gramática de error con un bloque de sentencias de ejecución en el cuerpo del if y una sola sentencia de ejecución en el cuerpo del else.*/
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $9.end_line);
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, $8.end_line);
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, $7.end_line);
		}
	|	IF '(' if_cond ')' then_ifelse_execution_block ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, $4.end_line);
		}
	|	IF '(' if_cond THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $9.end_line);
		}
	|	IF '(' ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, $2.end_line);
		}
	|	IF if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_statement END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, $1.begin_line);
		}
	/* De acá en más se describe la gramática de error con un bloque de sentencias de ejecución en el cuerpo del if 
	 * y un bloque de sentencias de ejecución en el cuerpo del else.
	 */	
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $9.end_line);
		}
	|	IF '(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $10.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_IF_EXPECTED, $8.end_line);
		}
	|	IF '(' if_cond ')' then_ifelse_execution_block ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_THEN_EXPECTED, $4.end_line);
		}
	|	IF '(' if_cond THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	IF '(' ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONDITION_EXPECTED, $2.end_line);
		}
	|	IF if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' if_cond ')' THEN then_ifelse_execution_block ELSE else_execution_block END_IF '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $9.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_IF_EXPECTED, $1.begin_line);
		}
	;

explicit_delimited_execution_block:
		BEGIN execution_block END '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
		}
		/*De acá en más se describe la gramática de error.*/
	|	BEGIN execution_block END
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $3.end_line);
		}
	|	BEGIN execution_block error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_END_EXPECTED, $2.end_line);
		}
	|	BEGIN END '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_EXPECTED, $1.end_line);
		}
	|	execution_block END '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_BEGIN_EXPECTED, $1.begin_line);
		}
	;

execution_block:
		execution_block executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $2.end_line;
		}
	|	executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
		}
	;

assignation_statement:
		LET ID '=' expression '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $5.end_line;

			/*Semántica.*/

			String inf_type = $4.temporalType;

			if (this.symbolTable.isDeclared($2.sval)) {
				if (this.symbolTable.getLastDeclaredType($2.sval).equals(inf_type)) {
					this.yyerror(ParserConstants.ERR_REDECLARED_VAR, $1.begin_line, true);
					this.currentRuleError++;
				}
				else {
					this.symbolTable.appendId($2.sval, $4.temporalType);
					//Asigno la expresión.
					$$.firstOperand = ($2.stackPosition != null) ? $2.stackPosition : $2.sval;
					$$.secondOperand = ($4.stackPosition != null) ? $4.stackPosition : $4.sval;
					Third third = new ThirdAssignation($$.firstOperand, $$.secondOperand, this.symbolTable, inf_type, $4.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
			}
			else {
				this.declaredVars.add(new Pair<String, Integer>($2.sval,$2.begin_line));
				this.declareVars($4.temporalType);
				//Asigno la expresión.
				$$.firstOperand = ($2.stackPosition != null) ? $2.stackPosition : $2.sval;
				$$.secondOperand = ($4.stackPosition != null) ? $4.stackPosition : $4.sval;
				Third third = new ThirdAssignation($$.firstOperand, $$.secondOperand, this.symbolTable, inf_type, $4.isConverted);
				this.codeManager.addThird(third);
				$$.stackPosition = new StackReference(this.codeManager.currentThird());
			}
		}
	|	ID '=' expression '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;

			/*Semántica.*/
			if (!this.symbolTable.isDeclared($1.sval)) {
				this.yyerror(ParserConstants.ERR_NOT_DECLARED_VAR + "'"+ $1.sval +"'", $1.begin_line, true);
				this.currentRuleError++;
			}
			else {
				if ($3.temporalType != null) {
					if (this.symbolTable.getType($1.sval).equals($3.temporalType)) {
						$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
						$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
						Third third = new ThirdAssignation($$.firstOperand, $$.secondOperand, this.symbolTable, $3.temporalType, $3.isConverted);
						this.codeManager.addThird(third);
						$$.stackPosition = new StackReference(this.codeManager.currentThird());
					}
					else {
						this.yyerror(ParserConstants.ERR_ASSIGN_TYPES, $1.begin_line, true);
						this.currentRuleError++;
					}
				}
			}
		}
		/*De acá en más se describe la gramática de error.*/
	|	LET ID '=' expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $4.end_line);
		}
	|	LET ID '=' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RIGHT_EXPR_EXPECTED, $3.end_line);
		}
	|	LET ID expression '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_ASSIGN_EXPECTED, $2.end_line);
		}
	|	LET '=' expression '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LEFT_ID_EXPECTED, $1.end_line);
		}
	|	ID '=' expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.begin_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $3.end_line);
		}
	|	ID '=' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RIGHT_EXPR_EXPECTED, $2.end_line);
		}
	|	'=' expression '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LEFT_ID_EXPECTED, $1.begin_line);
		}
	|	ID '=' '(' expression ')' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $6.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_UL2F_EXPECTED, $2.end_line);
		}
	|	LET ID '=' '(' expression ')' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_UL2F_EXPECTED, $3.end_line);
		}
		/*No se puede tratar acá el error ID expression '.' porque genera conflictos con la lista de declaración de variables sin ',' ya que la expresión puede ser también una identificador.*/
	;

out_statement:
		OUT '(' STRING_CONST ')' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $5.end_line;

			Third third = new ThirdOut($3.sval, $3.positionInSymbolTable);
			this.codeManager.addThird(third);
			$$.stackPosition = new StackReference(this.codeManager.currentThird());
		}
		/*De acá en más se describe la gramática de error.*/
	|	OUT '(' STRING_CONST ')'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $4.end_line);
		}
	|	OUT '(' STRING_CONST '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	OUT '(' ')' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $2.end_line);
		}
	|	OUT STRING_CONST ')' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' STRING_CONST ')' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_OUT_EXPECTED, $1.begin_line);
		}
	;

switch_id:
		ID
		{
			$$.begin_line = $1.begin_line;
			$$.end_line = $1.end_line;

			if (!this.symbolTable.isDeclared($1.sval)) {
				this.yyerror(ParserConstants.ERR_NOT_DECLARED_VAR + "'"+ $1.sval +"'", $1.begin_line, true);
				this.currentRuleError++;
			}

			this.codeManager.addSwitchDisc($1.sval);
		}

switch_statement:
		SWITCH '(' switch_id ')' '{' cases_rule '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $8.end_line;

			this.codeManager.popSwitchDisc();
			this.codeManager.completeAllBI(Integer.valueOf(this.codeManager.currentThird()) + 1);
			this.codeManager.removeLastBI();
		}
		/*De acá en más se describe la gramática de error.*/
	|	SWITCH '(' switch_id ')' '{' cases_rule '}'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_DOT_EXPECTED, $7.end_line);
		}
	|	SWITCH '(' switch_id ')' '{' cases_rule error '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $8.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RBRACE_EXPECTED, $6.end_line);
		}
	|	SWITCH '(' switch_id ')' '{' '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CASES_EXPECTED, $5.end_line);
		}
	|	SWITCH '(' switch_id ')' cases_rule '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LBRACE_EXPECTED, $4.end_line);
		}
	|	SWITCH '(' switch_id '{' cases_rule '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_RPAREN_EXPECTED, $3.end_line);
		}
	|	SWITCH '(' ')' '{' cases_rule '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_ID_SWITCH, $2.end_line);
		}
	|	SWITCH switch_id ')' '{' cases_rule '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
	|	'(' switch_id ')' '{' cases_rule '}' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $7.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SWITCH_EXPECTED, $1.begin_line);
		}
	;

cases_rule:
		cases_rule case_rule
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $2.end_line;

			Third third = new ThirdBI();
			this.codeManager.addBIThird(third);
			$$.stackPosition = new StackReference(this.codeManager.currentThird());
			
			$$.stackPosition = new StackReference(this.codeManager.currentThird());
			this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
		}
	|	case_rule
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;

			Third third = new ThirdBI();
			this.codeManager.addBIThird(third);
			$$.stackPosition = new StackReference(this.codeManager.currentThird());
			this.codeManager.completeBF(Integer.valueOf(this.codeManager.currentThird()) + 1);
		}
	;

number_switch_case:
	NUMERIC_CONST
	{
		$$.begin_line = $1.begin_line;
		$$.end_line = $1.end_line;

		if (this.symbolTable.getType($1.sval).equals(this.symbolTable.getType(this.codeManager.getSwitchDisc()))) {
			Third condition = new ThirdComparison("==", this.codeManager.getSwitchDisc(), $1.sval, this.symbolTable, this.symbolTable.getType($1.sval), false, false);
			this.codeManager.addThird(condition);
			ThirdBF third = new ThirdBF();
			this.codeManager.addBFThird(third);
			$$.stackPosition = new StackReference(this.codeManager.previousThird());
			third.setValue1($$.stackPosition);
		}
		else
			this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
	}

case_rule:
		CASE number_switch_case ':' explicit_delimited_execution_block
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
		}
	|	CASE number_switch_case ':' executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
		}
		/*De acá en más se describe la gramática de error.*/
	|	CASE number_switch_case ':' '.'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_STATEMENT_BLOCK_EXPECTED, $3.end_line);
		}
	|	CASE number_switch_case explicit_delimited_execution_block
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SEMICOLON_EXPECTED, $2.end_line);
		}
	|	CASE ':' explicit_delimited_execution_block
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONSTANT_EXPECTED, $1.end_line);
		}
	|	number_switch_case ':' explicit_delimited_execution_block
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CASE_EXPECTED, $1.begin_line);
		}
	|	CASE number_switch_case executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_SEMICOLON_EXPECTED, $2.end_line);
		}
	|	CASE ':' executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CONSTANT_EXPECTED, $1.end_line);
		}
	|	number_switch_case ':' executional_statements
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_CASE_EXPECTED, $1.begin_line);
		}
	;

condition:
		expression EQUAL expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					Third third = new ThirdComparison("==", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	expression '>' expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					Third third = new ThirdComparison(">", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	expression '<' expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					Third third = new ThirdComparison("<", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	| 	expression GREATER_EQUAL expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					Third third = new ThirdComparison(">=", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	expression LESS_EQUAL expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					Third third = new ThirdComparison("<=", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	expression NOT_EQUAL expression
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					Third third = new ThirdComparison("<>", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}	
		}

	;

expression:
		expression_rule
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			$$.sval = $1.sval;
			$$.stackPosition = $1.stackPosition;
			$$.temporalType = $1.temporalType;
			$$.isConverted = $1.isConverted;
		}
		;

expression_rule:
		expression '+' term
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			$$.isConverted = ($1.isConverted || $3.isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("+", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addArithThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
					$$.temporalType = $1.temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	expression '-' term
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			$$.isConverted = ($1.isConverted || $3.isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("-", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addArithThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
					$$.temporalType = $1.temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	| 	term
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			$$.stackPosition = $1.stackPosition;
			$$.temporalType = $1.temporalType;
			$$.isConverted = $1.isConverted;
		}
	;

term:
		term '*' factor
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			$$.isConverted = ($1.isConverted || $3.isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("*", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addArithThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
					$$.temporalType = $1.temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	term '/' factor
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			$$.isConverted = ($1.isConverted || $3.isConverted);

			/*Seteo de variables para generación de código intermedio*/
			if ($1.temporalType != null && $3.temporalType != null) {
				if ($1.temporalType.equals($3.temporalType)) {
					$$.firstOperand = ($1.stackPosition != null) ? $1.stackPosition : $1.sval;
					$$.secondOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
					ThirdArithmeticOperation third = new ThirdArithmeticOperation("/", $$.firstOperand, $$.secondOperand, this.symbolTable, $1.temporalType, $1.isConverted, $3.isConverted);
					this.codeManager.addArithThird(third);
					$$.stackPosition = new StackReference(this.codeManager.currentThird());
					$$.temporalType = $1.temporalType;
				}
				else {
					this.yyerror(ParserConstants.ERR_NOT_EQUAL_TYPES, $1.begin_line, true);
					this.currentRuleError++;
				}
			}
		}
	|	factor
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;
			$$.stackPosition = $1.stackPosition;
			$$.temporalType = $1.temporalType;
			$$.isConverted = $1.isConverted;
		}
	;

factor:
		ID
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.end_line;

			if (!this.symbolTable.isDeclared($1.sval)) {
				this.yyerror(ParserConstants.ERR_NOT_DECLARED_VAR + "'"+ $1.sval +"'", $1.begin_line, true);
				this.currentRuleError++;
			}
			else {
				$$.temporalType = this.symbolTable.getType($1.sval);
			}

		}
	|	NUMERIC_CONST
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $1.begin_line;

			Symbol symbol = this.symbolTable.getSymbol($1.sval);
			symbol.increaseRef();

			$$.temporalType = this.symbolTable.getType($1.sval);

		}
	|	'-' NUMERIC_CONST
		{
			$$.begin_line = $2.begin_line;
			$$.end_line = $2.end_line;

			Symbol symbol = this.symbolTable.getSymbol($2.sval);

			$$.temporalType = this.symbolTable.getType($2.sval);

			if (symbol != null) {
				if(symbol.getType().toLowerCase().equals("ulong")) {
					this.yyerror(ParserConstants.ERR_OUT_OF_BOUND_ULONG, $2.begin_line);
					this.currentRuleError++;
				}
				else {
					//Me fijo si ya está la constante de punto flotante negativa. 
					Symbol negativeSymbol = this.symbolTable.getSymbol(('-'+$2.sval));
					if(negativeSymbol != null) {
						//Existe. Actualizo la referencia sumando 1.
						negativeSymbol.increaseRef();
						//Resto a la constante positiva una referencia ya que si está la negativa, también la positiva.
						symbol.decreaseRef();
					}
					else {
						//La constante está como positiva en la tabla de símbolos, aún no se creo la misma en negativo.
						this.symbolTable.copyEntry($2.sval, ('-'+$2.sval));
						//Resto una referencia a la constante positiva.
						symbol.decreaseRef();
					}
				}
			}
		}
	|	UL_F '(' expression_rule ')'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $4.end_line;
			if ($3.temporalType != null && !$3.temporalType.toLowerCase().equals("float")) {
				//Si se realiza bien, terminará siendo de tipo float.
				$$.temporalType = "float";
				$$.firstOperand = ($3.stackPosition != null) ? $3.stackPosition : $3.sval;
				Third third = new ThirdConversion($$.firstOperand, $3.temporalType, this.symbolTable);
				this.codeManager.addThird(third);
				$$.stackPosition = new StackReference(this.codeManager.currentThird());
				$$.isConverted = true;
			}
			else
				this.yyerror(ParserConstants.ERR_CONVERSION_FLOAT, $3.begin_line, true);

		}
		/*De acá en más se describe la gramática de error.*/
		
		/*La gramática de error para el caso de la falta de ) no se puede realizar acá
		debido a que no hay un caracter de sincronización y se comería el resto de
		las reglas.*/
	|	UL_F '(' ')'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_EXPRESSION_EXPECTED, $2.end_line);
		}
	|	UL_F expression_rule ')'
		{
			$$.begin_line = $1.begin_line;
			$$.end_line   = $3.end_line;
			this.currentRuleError++;
			this.yyerror(ParserConstants.ERR_LPAREN_EXPECTED, $1.end_line);
		}
		/*No se puede cubrir aquí el caso en el que falte UL_F debido a que se comería los casos que fallen antes de una expresión dentro de paréntesis.*/
	;


%%

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