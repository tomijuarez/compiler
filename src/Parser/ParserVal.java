package Parser;//#############################################
//## file: Parser.java
//## Generated by Byacc/j
//#############################################

import Parser.IntermediateCode.StackReference;

/**
 * BYACC/J Semantic Value for parser: Parser
 * This class provides some of the functionality
 * of the yacc/C 'union' directive
 */
public class ParserVal
{
/**
 * integer value of this 'union'
 */
public int ival;

/**
 * double value of this 'union'
 */
public double dval;

/**
 * string value of this 'union'
 */
public String sval;

/**
 * object value of this 'union'
 */
public Object obj;

/**
 * Utilidades para la generación de código intermedio.
 */
public Object firstOperand;
public Object secondOperand;
public StackReference stackPosition;
public String temporalType;
public boolean isConverted = false;
    public int positionInSymbolTable = 0;

public int begin_line;
public int end_line;
//#############################################
//## C O N S T R U C T O R S
//#############################################
/**
 * Initialize me without a value
 */
public ParserVal()
{
}
/**
 * Initialize me as an int
 */
public ParserVal(int val)
{
  ival=val;
}

/**
 * Initialize me as a double
 */
public ParserVal(double val)
{
  dval=val;
}

/**
 * Initialize me as a string
 */
public ParserVal(String val)
{
  sval=val;
}

/**
 * Initialize me as an Object
 */
public ParserVal(Object val)
{
  obj=val;
}
}//end class

//#############################################
//## E N D    O F    F I L E
//#############################################
