package lm.touring;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import lm.touring.automaton.Automaton;

public class GUI extends JFrame {
	private static final long serialVersionUID = -8900010760721989010L;
	private JButton nextStepButton = new JButton("Next step");
	private JButton playButton = new JButton("Play");
	private JLabel currentStateLabel = new JLabel();
	private JPanel canvasPanel;
	private Automaton automaton;
	private boolean isPlaying = false;

	public GUI() {
		automaton = new Automaton.AutomatonBuilder()
				.transitionTable(new String[][] {
					{"q1,B,R", "q2,B,R", "q6,B,R"},
					{"q1,0,R", "q1,1,R", "q3,-,L"},
					{"q2,0,R", "q2,1,R", "q4,-,L"},
					{"q5,B,L", "q3,-,-", "q6,B,R"},
                                        {"q4,-,-", "q5,B,L", "q6,B,R"},
                                        {"q5,0,L", "q5,1,L", "q0,B,R"},
					{"q6,-,R", "q6,-,R", "q0,-,R"}
				})
				.states("q0", "q1", "q2", "q3", "q4", "q5", "q6")
				.alphabet("0", "1", "B")
				.finalStates("q6")
				.build();
		
		initUI();
	}

	private void initUI() {
		setTitle("Touring");
		setSize(1100, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel mainPanel = new JPanel(new BorderLayout());
		getContentPane().add(mainPanel);

		JPanel panelButtons = new JPanel();
		panelButtons.add(nextStepButton);
		panelButtons.add(playButton);
		
		mainPanel.add(panelButtons, BorderLayout.NORTH);
		
		canvasPanel = new AutomatonPanel(automaton);
		mainPanel.add(canvasPanel, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.add(currentStateLabel);
		mainPanel.add(bottomPanel, BorderLayout.SOUTH);
		
		
		nextStepButton.addActionListener((ActionEvent e) -> {
			automaton.transition();
			updateStateLabel();
		});
		
		playButton.addActionListener((ActionEvent e) -> {
			if (isPlaying) {
				isPlaying = false;
				playButton.setText("Play");
				nextStepButton.setEnabled(true);
			} else {
				isPlaying = true;
				playButton.setText("Pause");
				nextStepButton.setEnabled(false);
			}
		});
		
		initAutoplayLoop();
		updateStateLabel();
	}
	
	private void initAutoplayLoop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (isPlaying) {
						automaton.transition();
						updateStateLabel();
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	private void updateStateLabel() {
		currentStateLabel.setText(automaton.getStateHistory().toString() + " => " + automaton.getCurrentState());
                
                List<String> simbolos = automaton.getTape().getSymbolList();
                String estado_actual = automaton.getCurrentState();
                int bandera=1;
                
                if ("q6".equals(estado_actual)){
                    // Obtenemos un Iterador y recorremos la lista.
                    Iterator iter = simbolos.iterator();
                    while (iter.hasNext()){
                        Object aux=iter.next();
                        if (!"B".equals(aux)){
                            bandera=0;
                            //System.out.println(iter.next());
                            }
                    }
                    if (bandera==1){
                        JOptionPane.showMessageDialog(null, "Se acepta la palabra");
                         System.exit(0);
                    }
                    
                }
                
                
        }

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			GUI gui = new GUI();
			gui.setVisible(true);
		});
	}
}
