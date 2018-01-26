package Lexer.TransitionMatrix;

import Lexer.LexerConstants;
import Lexer.Token;
import Lexer.TransitionMatrix.SemanticActions.SemanticAction;
import Lexer.TransitionMatrix.Rules.TransitionRule;
import Lexer.WrapperCode;

public class Transition {
    private TransitionRule rule;
    private SemanticAction action;
    private int nextState;
    private Token token;

    public Transition(TransitionRule rule, SemanticAction action, int nextState) {
        this.rule = rule;
        this.action = action;
        this.nextState = nextState;
    }

    public boolean canPerformTransition(char nextChar) {
        return this.rule.canPerform(nextChar);
    }

    public Token getToken() {
        return this.token;
    }


    public int performTransition(WrapperCode code, String buffer, char nextChar) {
        if (rule.canPerform(nextChar)) {
            this.token = this.action.execute(code,buffer);
            return this.nextState;
        }
        return LexerConstants.INVALID_TRANSITION;
    }

}
