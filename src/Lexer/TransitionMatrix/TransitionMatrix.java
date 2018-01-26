package Lexer.TransitionMatrix;

import Lexer.LexerConstants;
import Lexer.Token;
import Lexer.WrapperCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransitionMatrix {

    private Map<Integer, List<Transition>> transitions = new HashMap<>();
    private Token token;

    public TransitionMatrix() {
        //Blank.
    }

    public void cleanToken() {
        this.token = null;
    }

    public void addTransition(int currentState, Transition transition) {
        List<Transition> transitionList;

        transitionList = (this.transitions.get(currentState) == null)
                ? new ArrayList<>()
                : this.transitions.get(currentState);

        transitionList.add(transition);
        transitions.put(currentState, transitionList);
    }

    private Transition getValidTransition(int from, char nextCharacter) {
        Transition validTransition = null;

        for (Transition transition : this.transitions.get(from)) {
            if (transition.canPerformTransition(nextCharacter)) {
                validTransition = transition;
                break;
            }
        }
        return validTransition;
    }

    public Token getToken() {
        return this.token;
    }

    public int performTransition(int from, String buffer, char nextCharacter, WrapperCode code) {
        Transition validTransition = this.getValidTransition(from, nextCharacter);

        if (validTransition != null) {
            int nextState = validTransition.performTransition(code, buffer, nextCharacter);
            this.token = validTransition.getToken();
            return nextState;
        }
        return LexerConstants.INVALID_TRANSITION;
    }
}
