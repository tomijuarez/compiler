package Lexer.TransitionMatrix.Rules;

public class EOFRule implements TransitionRule {

    public EOFRule(){
        //BLANK.
    }

    @Override
    public boolean canPerform(char nextChar) {
        return (nextChar=='\u001a');
    }
}
