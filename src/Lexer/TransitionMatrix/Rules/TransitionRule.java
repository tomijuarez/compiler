package Lexer.TransitionMatrix.Rules;

public interface TransitionRule {

    boolean canPerform(char nextChar);
}
