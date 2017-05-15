import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JTextField;
import javax.swing.JTextPane;

import javax.swing.WindowConstants;

public class aMailClientGUI_MailFenster extends JDialog {

	private JTextField to = new JTextField(30), cc = new JTextField(30), bcc = new JTextField(30), subject = new JTextField(30);
    private JTextPane content = new JTextPane();
    private JButton send;
    private JPanel alles;
          
    public aMailClientGUI_MailFenster(String Betreff, String Text){
    	super();

		subject.setText(Betreff);
		content.setText(Text);
		
		setTitle("Einstellungen");
		setSize(450, 400);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		alles = new JPanel();
		
		   
		   alles.add(createComponentWithLabel("to", to));
		   alles.add(createComponentWithLabel("cc", cc));
		   alles.add(createComponentWithLabel("bcc", bcc));
		   alles.add(createComponentWithLabel("subject", subject));
		   
		   
		   alles.setBackground(Color.cyan);
		   //content = ;
		   content.setPreferredSize(new Dimension(375, 200));
		   send = new JButton("Send");
		        //send.addActionListener(new SendListener());
		        alles.add(content);
		        alles.add(send);
		    add(alles);
		// Wir lassen unseren Dialog anzeigen
		setModal(false);
		setVisible(true);

	}
	
	
	private JPanel createComponentWithLabel(String label, Component comp) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(new JLabel(label, JLabel.RIGHT), BorderLayout.WEST);
        p.add(comp, BorderLayout.CENTER);
        return p;
    }
	
}
