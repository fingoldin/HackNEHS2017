import javax.swing.*;
import java.awt.Color;
import java.awt.Container;
import java.util.concurrent.TimeUnit;


public class gif_render_ex extends JPanel{
	public static void main(String args[]) /*throws MalformedURLException*/
	{
		JFrame window = new JFrame("Graphics Demo");
		gif_render_ex panel = new gif_render_ex();
		window.setBounds(0, 0, 900, 800);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setBackground(Color.GREEN);
		panel.setLayout(null);

		Icon icon = new ImageIcon("supernatural.gif");
		JLabel label = new JLabel(icon);

		window.setVisible(true);

		Container c = window.getContentPane();

		for (int x = 10, y = 10; x<500; x++, y+=2)
		{
			label.setBounds(x, y, 300, 300);
			panel.add(label);
			c.add(panel);
			System.out.println("hai");
			try        
			{
				TimeUnit.MILLISECONDS.sleep(50);
			} 
			catch(InterruptedException ex) 
			{
			}
		}
	}
}




// Links
// https://stackoverflow.com/questions/2935232/show-animated-gif
