package Lexer.TransitionMatrix.Rules;

public class CharRule implements TransitionRule {

    public CharRule() {
        /*BLANK*/
    }

    @Override
    public boolean canPerform(char nextChar) {
        return (Character.isLetter(nextChar) && !(Character.toLowerCase(nextChar) == 'e'));
    }
}
