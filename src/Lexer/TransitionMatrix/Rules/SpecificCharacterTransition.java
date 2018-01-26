package Lexer.TransitionMatrix.Rules;

public class SpecificCharacterTransition implements TransitionRule {

    private char character;

    public SpecificCharacterTransition(char character) {
        this.character = character;
    }

    @Override
    public boolean canPerform(char nextChar) {
        return (this.character == Character.toLowerCase(nextChar));
    }
}
