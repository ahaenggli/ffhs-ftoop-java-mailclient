import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;

/**
 * Mailfenster fuer Nachrichten lesen, antworten, weiterleiten, neu/senden
 * 
 * @author ahaen
 *
 */
public class MailClient_MailFenster extends JDialog {

	private static final long serialVersionUID = -4699496676863809052L;
	private JTextField Empfaenger = new JTextField(30);
	private JTextField Kopie = new JTextField(30);
	private JTextField Blindkopie = new JTextField(30);
	private JTextField Betreff = new JTextField(30);
	private JEditorPane Nachricht = new JEditorPane();

	private JButton Sendenbutton = new JButton("Senden");
	private JButton Antworten = new JButton("Antworten");
	private JButton Weiterleiten = new JButton("Weiterleiten");

	private JLabel err = new JLabel("");
	
	private boolean isAW = false;
	private boolean isWG = false;

	private JPanel alles;

	/**
	 * 0 = nichts (gesperrt) 
	 * 1 = senden (leer) 
	 * 2 = Antworten 
	 * 3 = Weiterleiten 
	 * 4 = Löschen
	 */
	
	private int Aktion = 0;

	/**
	 * Aendere Aktion
	 * 
	 * @param act
	 *            Zahl 0-4
	 */
	public void changeAction(final int act) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				Aktion = act;
				doAction();
			}
		});
	}

	/**
	 * Versuche eine Nachricht zu senden, bei Fehler kommt roter Text in GUI
	 * 
	 * @throws BackingStoreException
	 */
	private void sendeNachricht() throws BackingStoreException {
		String e = Empfaenger.getText().trim();
		String b = Betreff.getText().trim();
		String n = Nachricht.getText().trim();

		if (!e.isEmpty() && !b.isEmpty() && !n.isEmpty()) {

			
			
			
			MailStruktur mail = new MailStruktur(new Date(), Configuration.getsmtpUser(), b, n, null, null);
			mail.setEmpfaenger(e);
			mail.setBCC(Blindkopie.getText().trim());
			mail.setCC(Kopie.getText().trim());

			
			
			new SwingWorker<Object, Object>() {
				SendMail mailSender = new SendMail();

				@Override
				protected Object doInBackground() throws Exception {
					err.setText("Versuche das Mail zu senden...");
					err.setForeground(Color.black);
					
					changeAction(0);
					return mailSender.send(mail);
				}

				@Override
				protected void done() {
					if (mailSender.getErfolg()) {
						System.out.println("hier");
						MailHandler.addMailToFolder(mail, Configuration.getGesendet());
						dispose();
					} else {						
						err.setText(mailSender.getFehlerText());
						err.setForeground(Color.red);
						changeAction(1);
					}

			}
				}.execute();																		
		}
	}



	/**
	 * Konstruktor
	 * 
	 * @param mailStruktur
	 */
	public MailClient_MailFenster(MailStruktur mailStruktur, int action) {
		super();

		Empfaenger.setText(mailStruktur.getEmpfaenger());

		Betreff.setText(mailStruktur.getBetreff());
		Nachricht.setText(mailStruktur.getNachricht());
		Kopie.setText(mailStruktur.getCC());
		Blindkopie.setText(mailStruktur.getBCC());
		
		Aktion = action;

		setTitle("Nachricht");
		setSize(400, 500);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		alles = new JPanel();

		Sendenbutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					sendeNachricht();
				} catch (BackingStoreException e1) {

				}
			}
		});

		alles.add(Sendenbutton);

		Antworten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeAction(2);
			}
		});

		alles.add(Antworten);

		Weiterleiten.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				changeAction(3);
			}
		});

		alles.add(Weiterleiten);

		alles.add(macheLabelZuKomponente("Empfaenger / Absender:", Empfaenger));
		alles.add(macheLabelZuKomponente("Kopie:", Kopie));
		alles.add(macheLabelZuKomponente("Blindkopie:", Blindkopie));
		alles.add(macheLabelZuKomponente("Betreff:", Betreff));

		Nachricht.setPreferredSize(new Dimension(350, 200));

		JScrollPane spEditor = new JScrollPane(Nachricht, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		spEditor.setPreferredSize(new Dimension(350, 200));

		alles.add(macheLabelZuKomponente("Nachricht:", spEditor));// new
																	// JScrollPane(Nachricht)));
		alles.add(err);
		
		add(alles);
		doAction();
		// so viele Fenster wie man möchte erlauben und Parent nicht sperren
		setModal(false);
		// Fenster sichtbar machen
		setVisible(true);
	}

	/**
	 * Mach was, je nach Aufrufart
	 */
	private void doAction() {
		boolean setter = false;

		// ((JLabel) getComponentByName("lbl_Empfaenger")).setText("Empfänger");
		if (Aktion == 0) {
			setter = false;
		} else if (Aktion == 1) {
			setter = true;

		} else if (Aktion == 2) {
			setter = true;
			if (!isAW)
				Betreff.setText("AW: " + Betreff.getText());
			isAW = true;
		} else if (Aktion == 3) {
			setter = true;
			if (!isWG)
				Betreff.setText("WG: " + Betreff.getText());
			isWG = true;
			Empfaenger.setText("");

		} else if (Aktion == 4) {

			dispose();

		} else
			setter = false;

		Empfaenger.setEnabled(setter);
		Kopie.setEnabled(setter);
		Blindkopie.setEnabled(setter);

		Nachricht.setEnabled(setter);
		Betreff.setEnabled(setter);
		Sendenbutton.setEnabled(setter);

		if (Aktion == 1) {
			Antworten.setEnabled(false);
			Weiterleiten.setEnabled(false);
		}

		repaint();
	}

	/**
	 * Fuege Label direkt zu Komponente hinzu
	 * 
	 * @param label
	 * @param comp
	 * @return
	 * JPane mit Label und Ursprungskomponente
	 */
	private JPanel macheLabelZuKomponente(String label, Component comp) {
		label.trim();

		JPanel p = new JPanel();
		JLabel lbl = new JLabel(label + " ", JLabel.LEFT);
		lbl.setName("lbl_" + label.replace(":", ""));

		p.setLayout(new BorderLayout());
		p.add(lbl, BorderLayout.PAGE_START);
		p.add(comp, BorderLayout.CENTER);

		return p;

	}

}
