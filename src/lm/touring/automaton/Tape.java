package lm.touring.automaton;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tape {
	public static final String EMPTY_SYMBOL = "B";
	public static int EMPTY_BUFFOR_SIZE;  //la mitad de la cinta (donde empieza el cabezal)
	private List<String> tape;
	private int headIndex;
	
	public Tape(String palabra) {
            this.EMPTY_BUFFOR_SIZE = 0;
            this.headIndex = EMPTY_BUFFOR_SIZE;
            
            //String palabra = "110011";
		//String[] emptyValues = new String[EMPTY_BUFFOR_SIZE * 2 + 1];   //tamaño para que el cabezal esté en la mitad
                
                
		//Arrays.fill(emptyValues, 1,5, EMPTY_SYMBOL); //Se llena un array con puro símbolos vacíos
                
                StringBuilder sb = new StringBuilder();
                sb.append(EMPTY_SYMBOL+EMPTY_SYMBOL);
                sb.append(palabra);
                sb.append(EMPTY_SYMBOL+EMPTY_SYMBOL);
                
                String[] simbolos = sb.toString().split("");
                
                
		tape = new LinkedList<>(Arrays.asList(simbolos));    //cinta
	}
	
	public void moveLeft() {
		if (headIndex <= EMPTY_BUFFOR_SIZE) {
			tape.add(0, EMPTY_SYMBOL);
		} else {
			--headIndex;
		}
	}
	
	public void moveRight() {
		++headIndex;
		if (headIndex >= tape.size() - EMPTY_BUFFOR_SIZE) {
			tape.add(EMPTY_SYMBOL);
		}
	}
	
	public String read() {
		return tape.get(headIndex);
	}
	
	public void write(String symbolToWrite) {
		tape.set(headIndex, symbolToWrite);
	}
	
	@Override
	public String toString() {
		String value = "|";
		
		for (int i = 0; i < tape.size(); i++) {
			if (i == headIndex)
				value += "|";
			value += tape.get(i) + "|";
			if (i == headIndex)
				value += "|";
		}
		
		return value;
	}
	
	
	public List<String> getSymbolList() {
		return tape;
	}
	
	public int getCurrentHeadPosition() {
		return headIndex;
	}
}
