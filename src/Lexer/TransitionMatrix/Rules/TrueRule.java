package Lexer.TransitionMatrix.Rules;

/**
 * Created by tomi on 27/09/17.
 */
public class TrueRule implements TransitionRule {

    @Override
    public boolean canPerform(char nextChar) {
        return true;
    }
}
