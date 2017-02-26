import java.awt.Dimension;

import javax.swing.JFrame;

public class Main
{
    public static void main(String[] args)
    {
	JFrame frame = new JFrame();

	int width = 1440;
	int height = 960;

	frame.setPreferredSize(new Dimension(width, height));
	frame.setMaximumSize(new Dimension(width, height));
	frame.setMinimumSize(new Dimension(width, height));
	
<<<<<<< HEAD
		frame.setPreferredSize(new Dimension(width, height));
		frame.setMaximumSize(new Dimension(width, height));
		frame.setMinimumSize(new Dimension(width, height));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Draw());
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
=======
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(new Debug());
	frame.setResizable(false);
	frame.pack();
	frame.setVisible(true);
>>>>>>> 8d5f6b669efe8c594799c82abca68c2b3a100183
    }
}
