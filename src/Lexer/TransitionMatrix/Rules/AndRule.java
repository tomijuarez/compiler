package Lexer.TransitionMatrix.Rules;

public class AndRule implements TransitionRule {

    private TransitionRule r1;
    private TransitionRule r2;

    public AndRule(TransitionRule r1, TransitionRule r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    public boolean canPerform(char nextChar) {
        return (this.r1.canPerform(nextChar) && this.r2.canPerform(nextChar));
    }

}
