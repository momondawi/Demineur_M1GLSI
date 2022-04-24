import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;


public class Demineur extends JFrame implements MouseListener, WindowListener, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panneauHaut = new JPanel();
	private JPanel panneauJeux = new JPanel();
	private GridBagLayout layoutPanneauJeux = new GridBagLayout();
	private Segment affMines = new Segment(); 
	private Segment affTemps = new Segment();
	private Border borderPanneaux;
	private JMenuBar menu = new JMenuBar();
	private JMenu partie = new JMenu("Partie");
	private JCheckBox pause = new JCheckBox("Pause");
	private JMenuItem menuNouveau = new JMenuItem("Nouveau");
	JCheckBoxMenuItem menuDebutant = new JCheckBoxMenuItem("Debutant");
	JCheckBoxMenuItem menuIntermediaire = new JCheckBoxMenuItem( "Moyen");
	JCheckBoxMenuItem menuExpert = new JCheckBoxMenuItem("Expert");
	private BoxLayout layoutPanneauHaut = new BoxLayout(panneauHaut,BoxLayout.LINE_AXIS);
	private Component box2; 
	private Component box3;
	private Component box1;
	private Component box4;

	int nDrapeau = 0; 
	private int nMines; 
	private int LARGEUR; 
	private int HAUTEUR; 
	private int nCases; 
	DeminCase[][] jeux;
	private String mines; 
	private int[][] casesSelectionnees = new int[8][2];
	private Temps temps = new Temps(affTemps); 
	private int TYPE;

	public Demineur(int hauteur, int largeur, int mines, int type) {
    
		HAUTEUR = hauteur;
		LARGEUR = largeur;
		nCases = HAUTEUR * LARGEUR;
		nMines = mines;
		TYPE = type;
		jeux = new DeminCase[HAUTEUR][LARGEUR];

		for (int i = 0; i < HAUTEUR; i++) {
			for (int j = 0; j < LARGEUR; j++) {
				jeux[i][j] = new DeminCase();
			}
		}

		if (type == 1) menuDebutant.setSelected(true);
		if (type == 2) menuIntermediaire.setSelected(true);
		if (type == 3) menuExpert.setSelected(true);

		
		nouveau();

		try {
			jbInit();
			this.setVisible(true);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void nouveau() {
		temps.cancel(); 
		nDrapeau = 0; 
		nCases = HAUTEUR * LARGEUR;
		affMines.setValeur(nMines);
		affTemps.setValeur(0);
		panneauJeux.setVisible(true);
		pause.setSelected(false);
		mines = "";
		for (int i = 0; i < nMines; i++) mines = mines + "1";
		while (mines.length() < HAUTEUR * LARGEUR) {
			int i = (int) (Math.random() * (mines.length() + 1));
			mines = mines.substring(0, i) + "0" + mines.substring(i);
		}

		for (int i = 0; i < HAUTEUR; i++) {
			for (int j = 0; j < LARGEUR; j++) {
				jeux[i][j].reset();
				jeux[i][j].removeMouseListener(this); 
				jeux[i][j].addMouseListener(this); 
				if (mines.charAt(i * LARGEUR + j) == '1') {
					jeux[i][j].setMine(true);
				}
			}
		}
		repaint();

		for (int i = 0; i < HAUTEUR; i++) {
			for (int j = 0; j < LARGEUR; j++) {
				if (!jeux[i][j].isMine()) {
					int n = 0;
					try {
						if (jeux[i - 1][j - 1].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i - 1][j].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i - 1][j + 1].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i][j - 1].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i][j + 1].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i + 1][j - 1].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i + 1][j].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					try {
						if (jeux[i + 1][j + 1].isMine()) n++;
					}
					catch (java.lang.ArrayIndexOutOfBoundsException e) {}
					jeux[i][j].setChiffre(n); 
				}
			}
		}
	}

	private void jbInit() throws Exception {
		borderPanneaux = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 156));
		box2 = Box.createGlue(); 
		box3 = Box.createGlue();
		box1 = Box.createHorizontalStrut(8);
		box1.setSize(5, 50);
		box4 = Box.createHorizontalStrut(8);
		box4.setSize(5, 50);

		this.addWindowListener(this);

		int tailleX = LARGEUR * 16 + 20; 
		int tailleY = HAUTEUR * 16 + 20;
		if (tailleX < 160) tailleX = 150; 

		this.setSize(tailleX + 6, tailleY + 50 + 23 + 25);
		this.setTitle("Demineur");
		this.setResizable(false);

		menuNouveau.addActionListener(this);
    	menuDebutant.addActionListener(this);
    	menuIntermediaire.addActionListener(this);
    	menuExpert.addActionListener(this);
    	partie.add(menuNouveau);
    	partie.add(new JSeparator());
    	partie.add(menuDebutant);
    	partie.add(menuIntermediaire);
    	partie.add(menuExpert);
    	menu.setBorderPainted(false);
    	menu.add(partie);
    	pause.setOpaque(false);
    	pause.setFocusPainted(false);
    	pause.addActionListener(this);
    	menu.add(pause);
    	this.setJMenuBar(menu);

    	affMines.setMaximumSize(new Dimension(49, 27));
    	affTemps.setMaximumSize(new Dimension(49, 27));
    	panneauHaut.setBorder(borderPanneaux);
    	panneauHaut.setPreferredSize(new Dimension(450, 50));
    	panneauHaut.setLayout(layoutPanneauHaut);
	    panneauJeux.setBorder(borderPanneaux);
	    panneauJeux.setPreferredSize(new Dimension(tailleX, tailleY));
	    panneauJeux.setLayout(layoutPanneauJeux);
	    affMines.setValeur(nMines);
	    affTemps.setValeur(0);
	    panneauHaut.add(box1, null);
	    panneauHaut.add(affMines, null);
	    panneauHaut.add(box2, null);
	    panneauHaut.add(box3, null);
	    panneauHaut.add(affTemps, null);
	    panneauHaut.add(box4, null);
    	this.getContentPane().add(panneauHaut, BorderLayout.NORTH);
    	this.getContentPane().add(panneauJeux, BorderLayout.CENTER);

    	Graphisme gr = new Graphisme(this.getGraphicsConfiguration());

    	for (int i = 0; i < HAUTEUR; i++) {
    		for (int j = 0; j < LARGEUR; j++) {
    			jeux[i][j].setGraphisme(gr); 
    			panneauJeux.add(jeux[i][j], new GridBagConstraints(j, i, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    		}
    	}
	}

	public int[] caseClic(int x, int y) {
		int OFFSETX = (int) jeux[0][0].getX() + 3;
		int OFFSETY = (int) jeux[0][0].getY() + 22;
		int posx = -1, posy = -1;
		if (x - OFFSETX >= 0) posx = (x - OFFSETX) / 16;
		if (posx >= LARGEUR) posx = -1;
		if (y - OFFSETY >= 0 && posx != -1) posy = (y - OFFSETY) / 16;
		if (posy >= HAUTEUR) posy = -1;
		if (posy == -1) posx = -1;
		int[] retour = {posx, posy};
		return retour;
	}

	public void mouseClicked(MouseEvent e) {}

	public void mousePressed(MouseEvent e) {
		try {
			int x = (int) ( (JPanel) e.getSource()).getLocation().getX() + e.getX() +3; 
			int y = (int) ( (JPanel) e.getSource()).getLocation().getY() + e.getY() +22;
			int[] coord = caseClic(x, y); 
			
			if (e.getButton() == e.BUTTON3 && coord[1] != -1 && coord[0] != -1) {
				int temp = jeux[coord[1]][coord[0]].getEtat();
				switch (temp) {
				case 0: 
					jeux[coord[1]][coord[0]].setEtat(2);
					nDrapeau++;
					affMines.setValeur(nMines - nDrapeau);
					break;
				case 2: 
					jeux[coord[1]][coord[0]].setEtat(3);
					nDrapeau--;
					affMines.setValeur(nMines - nDrapeau);
					break;
				case 3: 
					jeux[coord[1]][coord[0]].setEtat(0);
					break;
				}
				jeux[coord[1]][coord[0]].repaint();
			}

			y = coord[1];
			x = coord[0];
			if (e.getButton() == e.BUTTON1 && x != -1 && y != -1 && jeux[y][x].getEtat() == 1 && jeux[y][x].getChiffre() != 0) {
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 2; j++) {
						casesSelectionnees[i][j] = -1;
					}
				}
				try {
					if (jeux[y - 1][x - 1].getEtat() == 0) {
						jeux[y - 1][x - 1].setSelected(true);
						casesSelectionnees[0][0] = y - 1;
						casesSelectionnees[0][1] = x - 1;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y - 1][x].getEtat() == 0) {
						jeux[y - 1][x].setSelected(true);
						casesSelectionnees[1][0] = y - 1;
						casesSelectionnees[1][1] = x;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y - 1][x + 1].getEtat() == 0) {
						jeux[y - 1][x + 1].setSelected(true);
						casesSelectionnees[2][0] = y - 1;
						casesSelectionnees[2][1] = x + 1;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y][x - 1].getEtat() == 0) {
						jeux[y][x - 1].setSelected(true);
						casesSelectionnees[3][0] = y;
						casesSelectionnees[3][1] = x - 1;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y][x + 1].getEtat() == 0) {
						jeux[y][x + 1].setSelected(true);
						casesSelectionnees[4][0] = y;
						casesSelectionnees[4][1] = x + 1;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y + 1][x - 1].getEtat() == 0) {
						jeux[y + 1][x - 1].setSelected(true);
						casesSelectionnees[5][0] = y + 1;
						casesSelectionnees[5][1] = x - 1;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y + 1][x].getEtat() == 0) {
						jeux[y + 1][x].setSelected(true);
						casesSelectionnees[6][0] = y + 1;
						casesSelectionnees[6][1] = x;
					}
				}
				catch (Exception exc) {}
				try {
					if (jeux[y + 1][x + 1].getEtat() == 0) {
						jeux[y + 1][x + 1].setSelected(true);
						casesSelectionnees[7][0] = y + 1;
						casesSelectionnees[7][1] = x + 1;
					}
				}
				catch (Exception exc) {}
			}
		}
		catch (java.lang.ClassCastException ex) {} 
	}

	public void mouseReleased(MouseEvent e) {

		if (nCases == HAUTEUR * LARGEUR && e.getButton() == e.BUTTON1) {
			temps.cancel();
			temps = new Temps(affTemps);
			temps.start();
		}

		try {
			int x = (int) ( (JPanel) e.getSource()).getLocation().getX() + e.getX() +3; 
			int y = (int) ( (JPanel) e.getSource()).getLocation().getY() + e.getY() +22;
			int[] coord = caseClic(x, y); 
			if (coord[0] != -1 && coord[1] != -1) {
				y = coord[1];
				x = coord[0];
				if (e.getButton() == e.BUTTON1) { 
					decouvre(y, x);
					repaint();
				}
				jeux[y][x].setSelected(false); 
				try {
					jeux[casesSelectionnees[0][0]][casesSelectionnees[0][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[1][0]][casesSelectionnees[1][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[2][0]][casesSelectionnees[2][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[3][0]][casesSelectionnees[3][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[4][0]][casesSelectionnees[4][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[5][0]][casesSelectionnees[5][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[6][0]][casesSelectionnees[6][1]].setSelected(false);
				}
				catch (Exception exc) {}
				try {
					jeux[casesSelectionnees[7][0]][casesSelectionnees[7][1]].setSelected(false);
				}
				catch (Exception exc) {}
			}
		}
		catch (java.lang.ClassCastException ex) {} 
	}

	public void mouseEntered(MouseEvent e) {}

	public void mouseExited(MouseEvent e) {}

	void boutonNouveau_actionPerformed(ActionEvent e) {
		if (!pause.isSelected()) nouveau();
	}

	public void decouvre(int y, int x) {
		
		if ( (jeux[y][x].getEtat() == 0 || jeux[y][x].getEtat() == 3) && !jeux[y][x].isMine()) {
			nCases--;
			jeux[y][x].setEtat(1); 
			if (jeux[y][x].getChiffre() == 0) { 
				decouvrirPartiel1(x - 1, y - 1);
				decouvrirPartiel1(x - 1, y);
				decouvrirPartiel1(x - 1, y + 1);
				decouvrirPartiel1(x, y - 1);
				decouvrirPartiel1(x, y + 1);
				decouvrirPartiel1(x + 1, y - 1);
				decouvrirPartiel1(x + 1, y);
				decouvrirPartiel1(x + 1, y + 1);
			}
		}
		else if (jeux[y][x].getEtat() == 1 && jeux[y][x].getChiffre() != 0) {
			int n = 0; 
			if (decouvrirPartiel2(x - 1, y - 1)) n++;
			if (decouvrirPartiel2(x - 1, y)) n++;
			if (decouvrirPartiel2(x - 1, y + 1)) n++;
			if (decouvrirPartiel2(x, y - 1)) n++;
			if (decouvrirPartiel2(x, y + 1)) n++;
			if (decouvrirPartiel2(x + 1, y - 1)) n++;
			if (decouvrirPartiel2(x + 1, y)) n++;
			if (decouvrirPartiel2(x + 1, y + 1)) n++;

			if (n == jeux[y][x].getChiffre()) { 
				if (decouvrirPartiel3(x - 1, y - 1)) decouvre(y - 1, x - 1);
				if (decouvrirPartiel3(x - 1, y)) decouvre(y, x - 1);
				if (decouvrirPartiel3(x - 1, y + 1)) decouvre(y + 1, x - 1);
				if (decouvrirPartiel3(x, y - 1)) decouvre(y - 1, x);
				if (decouvrirPartiel3(x, y + 1)) decouvre(y + 1, x);
				if (decouvrirPartiel3(x + 1, y - 1)) decouvre(y - 1, x + 1);
				if (decouvrirPartiel3(x + 1, y)) decouvre(y, x + 1);
        	if (decouvrirPartiel3(x + 1, y + 1)) decouvre(y + 1, x + 1);
			}
		}
		else if ( (jeux[y][x].getEtat() == 0 || jeux[y][x].getEtat() == 3) && jeux[y][x].isMine()) {
			temps.cancel(); 
			jeux[y][x].setEtat(4);
			for (int i = 0; i < HAUTEUR; i++) {
				for (int j = 0; j < LARGEUR; j++) {
					jeux[i][j].removeMouseListener(this); 
					jeux[i][j].setBlocked(true);
					if (! (y == i && x == j) && mines.charAt(i * LARGEUR + j) == '1' && jeux[i][j].getEtat() != 2)
						jeux[i][j].setEtat(5); 
				}
			}
			for (int i = 0; i < HAUTEUR; i++) {
				for (int j = 0; j < LARGEUR; j++) {
					if (jeux[i][j].getEtat() == 2 && !jeux[i][j].isMine()) jeux[i][j].setEtat(6);
				}
			}
		}
		if (nCases == nMines && !jeux[0][0].isBlocked()) {
			temps.cancel(); 
			affMines.setValeur(0);
			for (int i = 0; i < HAUTEUR; i++) {
				for (int j = 0; j < LARGEUR; j++) {
					jeux[i][j].removeMouseListener(this);
					jeux[i][j].setBlocked(true);
					if (jeux[i][j].isMine()) jeux[i][j].setEtat(2); 
				}
			}
		}
	}
	public void decouvrirPartiel1(int x, int y) {
		if (x >= 0 && y >= 0 && x < LARGEUR && y < HAUTEUR) {
			if (jeux[y][x].getEtat() == 0 && jeux[y][x].getChiffre() != 0) {
				jeux[y][x].setEtat(1);
				nCases--;
			}
			if (jeux[y][x].getEtat() == 0 && jeux[y][x].getChiffre() == 0)
				decouvre(y, x); 
		}
	}
	public boolean decouvrirPartiel2(int x, int y) {
		if (x >= 0 && y >= 0 && x < LARGEUR && y < HAUTEUR) {
			if (jeux[y][x].getEtat() == 2)
				return true;
		}
		return false;
	}
	public boolean decouvrirPartiel3(int x, int y) {
		if (x >= 0 && y >= 0 && x < LARGEUR && y < HAUTEUR) {
			if (jeux[y][x].getEtat() == 0 || jeux[y][x].getEtat() == 3)
				return true;
		}
		return false;
	}

	public void windowOpened(WindowEvent e) {}

	public void windowClosing(WindowEvent e) {
		temps.stop(); 
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {}

	public void windowIconified(WindowEvent e) {
		try {
			temps.suspend();
		} 
		catch (Exception esc) {}
	}

	public void windowDeiconified(WindowEvent e) {
		try {
			temps.resume();
		}
		catch (Exception esc) {}
	}

	public void windowActivated(WindowEvent e) {}

	public void windowDeactivated(WindowEvent e) {}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuNouveau) nouveau();
		else if (e.getSource() == menuDebutant && TYPE != 1) {
			this.dispose(); 
			System.gc();
			if (TYPE == 1) menuDebutant.setSelected(true);
			@SuppressWarnings("unused")
			Demineur demineur = new Demineur(9, 9, 10, 1); 
		}
		else if (e.getSource() == menuDebutant && !menuDebutant.isSelected())
			menuDebutant.setSelected(true);
		else if (e.getSource() == menuIntermediaire && TYPE != 2) {
			this.dispose(); 
			System.gc();
			if (TYPE == 2) menuIntermediaire.setSelected(true);
			@SuppressWarnings("unused")
			Demineur demineur = new Demineur(16, 16, 40, 2);
		}
		else if (e.getSource() == menuIntermediaire && !menuIntermediaire.isSelected()) menuIntermediaire.setSelected(true);
		else if (e.getSource() == menuExpert && TYPE != 3) {
			this.dispose(); 
			System.gc();
			if (TYPE == 3) menuExpert.setSelected(true);
			@SuppressWarnings("unused")
			Demineur demineur = new Demineur(16, 30, 99, 3);
		}
		else if (e.getSource() == menuExpert && TYPE != 4) menuExpert.setSelected(true);
		
		else if (e.getSource() == pause) {
			if (pause.isSelected()) {
				panneauJeux.setVisible(false);
				temps.suspend();
			}
			else {
				panneauJeux.setVisible(true);
				temps.resume();
			}
		}
	}
}
