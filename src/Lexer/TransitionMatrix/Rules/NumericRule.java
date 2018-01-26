package Lexer.TransitionMatrix.Rules;

public class NumericRule implements TransitionRule {

    public NumericRule() {

    }

    @Override
    public boolean canPerform(char nextChar) {
        return Character.isDigit(nextChar);
    }
}
