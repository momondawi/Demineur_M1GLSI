import javax.swing.JFrame;

public class Frame extends JFrame{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Frame (String title) {
		this.setVisible(true);
		this.setSize(1280,720);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
		this.setLocationRelativeTo(null);
	
		this.setTitle(title);	
	}
}

