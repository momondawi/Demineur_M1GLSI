import javax.swing.*;


public class Main {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		new Demineur(16,30,99,3);
	}
}
