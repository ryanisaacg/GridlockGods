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
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new Debug());
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
    }
}
