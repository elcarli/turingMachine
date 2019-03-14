package lm.touring.automaton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;
import lm.touring.frmPrincipal;

public class Automaton {
	public static String[] alphabet;
	private String[][] transitionTable;
	private String[] states;
	private String[] finalStates;
	private String initialState;
	private String currentState;
	private List<String> stateHistory = new ArrayList<>();
	private Tape tape;

	private Automaton(AutomatonBuilder builder) {
		alphabet = builder.alphabet;
		transitionTable = builder.transitionTable;
		states = builder.states;
		finalStates = builder.finalStates;
		initialState = builder.initialState;
		initAutomaton();
	}
	
	public void transition() {
		stateHistory.add(currentState);
		String readSymbol = tape.read();
                System.out.println("readSymbol="+readSymbol);
		
                if (frmPrincipal.alfabeto.indexOf(readSymbol)!=-1){ //si el simbolo leído pertenece al alfabeto
                    readSymbol="@";
                }
                    
		int stateIndex = getStateIndex(currentState);   //estado actual
                
                System.out.println("stateIndex="+stateIndex);
                
                
		int symbolIndex = getSymbolIndex(readSymbol);   //simbolo leido
                System.out.println("symbolIndex="+symbolIndex);
                
                //si el símbolo leído NO pertenece al alfabeto
                if (symbolIndex==-1)
                {
                    JOptionPane.showMessageDialog(null, "La máquina se detuvo, un caracter no pertenece al alfabeto");
                    System.exit(0);
                }
                
                                
		String[] nextStateActions = transitionTable[stateIndex][symbolIndex].split(",");
		System.out.println("nextStateActions="+nextStateActions[0]+"  "+nextStateActions[1]+"  "+nextStateActions[2]);
                
                System.out.println("el nuevo SIMBOLO BUSCADO será el siguiente: "+frmPrincipal.simBuscado); 
                
                //Si estoy en q2    y   me dirijo al estado q3
                if ("q2".equals(currentState) && "q3".equals(nextStateActions[0])){
                    
                    System.out.println(frmPrincipal.simBuscado); 
                    
                    //si el símbolo final NO es igual al buscado
                    if (!tape.read().equals(frmPrincipal.simBuscado)){
                    JOptionPane.showMessageDialog(null, "No es palíndromo");
                    System.exit(0);
                    }
                    
                    frmPrincipal.posBuscado++;
                    System.out.println("la posicion del buscado: "+frmPrincipal.posBuscado);
                    
                    //el nuevo alfabeto será el siguiente caracter de la cadena y B
                    frmPrincipal.simBuscado=String.valueOf(frmPrincipal.palabra.charAt(frmPrincipal.posBuscado));
                    
                    
                    
                }
                    
                
		currentState = nextStateActions[0]; //contiene el estado al que salta
                
                System.out.println("currentState="+currentState);
                
                
                
                
		
		String symbolToWrite = nextStateActions[1]; //contiene el símbolo que se escribirá
		System.out.println("symbolToWrite="+symbolToWrite);
                System.out.println("-----------------------");
                System.out.println("-----------------------");
                
		if (!symbolToWrite.equals("-")) {   //si el símbolo se desea cambiar
			tape.write(symbolToWrite);
		}
		
		String headDirection = nextStateActions[2]; //contiene la dirección de lectura
		if (headDirection.equals("L")) {
			tape.moveLeft();
		}
		
		if (headDirection.equals("R")) {
			tape.moveRight();
		}
	}
	
	public Tape getTape() {
		return tape;
	}
	
	public boolean isInFinalState() {
		return Arrays.asList(finalStates).indexOf(currentState) >= 0;
	}
	
	public String getCurrentState() {
		return currentState;
	}
	
	public void reset() {
		initAutomaton();
	}
	
	public String[] getAlphabet() {
		return alphabet;
	}
        
        public void setAlphabet(String [] nuevo){
            this.alphabet=nuevo;
        }

	public String[][] getTransitionTable() {
		return transitionTable;
	}

	public String[] getStates() {
		return states;
	}

	public String[] getFinalStates() {
		return finalStates;
	}

	public String getInitialState() {
		return initialState;
	}
	
	public List<String> getStateHistory() {
		return stateHistory;
	}
	
	@Override
	public String toString() {
		return tape.toString();
	}

	private void initAutomaton() {
		tape = new Tape(frmPrincipal.palabra);
		currentState = initialState;
		stateHistory.clear();
	}
	
	private int getStateIndex(String state) {
		return Arrays.asList(states).indexOf(state);
	}
	
	private int getSymbolIndex(String symbol) {
		return Arrays.asList(alphabet).indexOf(symbol);
	}
	
	
	public static class AutomatonBuilder {
		private String[] alphabet;
		private String[][] transitionTable;
		private String[] states;
		private String[] finalStates = {};
		private String initialState;
		
		public AutomatonBuilder alphabet(final String... alphabet) {
			this.alphabet = alphabet;
			return this;
		}
		
		public AutomatonBuilder transitionTable(final String[][] transitionTable) {
			this.transitionTable = transitionTable;
			return this;
		}
		
		public AutomatonBuilder states(final String... states) {
			this.states = states;
			return this;
		}
		
		public AutomatonBuilder finalStates(final String... finalStates) {
			this.finalStates = finalStates;
			return this;
		}
		
		public AutomatonBuilder initialState(final String initialState) {
			this.initialState = initialState;
			return this;
		}
		
		public Automaton build() {
			checkTransitionTableProvided();
			this.states = this.states != null ? this.states : getDefaultRange(this.transitionTable.length);
			this.initialState = this.initialState != null ? this.initialState : this.states[0];
			this.alphabet = this.alphabet != null ? this.alphabet : getDefaultRange(this.transitionTable[0].length);
			checkFinalStatesInStatesList();
			checkInitialStateInStatesList();
			checkAlphabetAndStatesEqualsTransitionTableDimmensions();
			checkTransitionTableValid();
			return new Automaton(this);
		}
		
		private void checkTransitionTableProvided() {
			if (transitionTable == null || transitionTable[0] == null) {
				throw new IllegalStateException("Transition table must be provided and cannot be empty.");
			}
		}
		
		private String[] getDefaultRange(int count) {
			String[] states = new String[count];
			for (int i = 0; i < count; i++) {
				states[i] = "" + i;
			}
			return states;
		}
		
		private void checkFinalStatesInStatesList() {
			for (String state : finalStates) {
				if (Arrays.asList(states).indexOf(state) < 0) {
					throw new IllegalArgumentException("Final state " + state + " must be present on states list.");
				}
			}
		}
		
		private void checkInitialStateInStatesList() {
			if (Arrays.asList(states).indexOf(initialState) < 0) {
				throw new IllegalArgumentException("Initial state " + initialState + " must be present on states list.");
			}
		}
		
		private void checkAlphabetAndStatesEqualsTransitionTableDimmensions() {
			if (transitionTable.length != states.length || transitionTable[0].length != alphabet.length) {
				throw new IllegalArgumentException("Transition table dimensions do not correspond to symbols or states count.");
			}
		}
		
		private void checkTransitionTableValid() {
			for (String row[] : transitionTable) {
				for (String stateRow : row) {
					String[] parts = stateRow.split(",");
					if (parts.length != 3) {
						throw new IllegalArgumentException("Transition table element: " + stateRow + " is not in required format \"state,symbol_to_write,head_direction\", eg. \"q2,1,L\"");
					}
					
					String state = parts[0];
					if (!isStateInStatesList(state)) {
						throw new IllegalArgumentException("State " + state + " from transition table must be present on states list.");
					}
					
					String symbolToWrite = parts[1];
					if (!isSymbolInAlphabet(symbolToWrite) && !symbolToWrite.equals("-")) {
						throw new IllegalArgumentException("Symbol to write: " + state + " is not in alphabet or - symbol (which means do not write anything)");
					}
					
					String headDirection = parts[2];
					if (!headDirection.equals("L") && !headDirection.equals("R") && !headDirection.equals("-")) {
						throw new IllegalArgumentException("Head direction: " + state + " must be one of L, R or - (do nothing)");
					}
				}
			}
		}
		
		private boolean isStateInStatesList(String state) {
			return (Arrays.asList(states).indexOf(state) >= 0);
		}
		
		private boolean isSymbolInAlphabet(String symbol) {
			return (Arrays.asList(alphabet).indexOf(symbol) >= 0);
		}
	}
}
