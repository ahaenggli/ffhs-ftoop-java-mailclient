import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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

	private JLabel lbl_Status = new JLabel("");
	private JLabel EmpfAbs = new JLabel("Empfaenger/Absender:");

	private boolean isAW = false;
	private boolean isWG = false;

	private MailStruktur MailDaten = new MailStruktur();
	private JPanel alles;

	/*
	 * 0 = nichts (gesperrt); 1 = senden (ungesperrt); 2 = Antworten; 3 =
	 * Weiterleiten; 4 = Schliessen;
	 */

	private int Aktion = 0;

	public void setlbl_Status(String neu, Color fg) {
		if (neu != null)
			lbl_Status.setText(neu);
		lbl_Status.setForeground(fg);
		lbl_Status.updateUI();
	}

	/**
	 * Setzt Aktion neu
	 * 
	 * @param neu
	 *            Neue Zahl fuer Aktion
	 */
	public void setAktion(int neu) {
		this.Aktion = neu;
	}

	/**
	 * Ermittelt die Aktion
	 * 
	 * @return Aktion als Zahl
	 */
	public int getAktion() {
		return Aktion;
	}

	/**
	 * Aendere Aktion
	 * 
	 * @param act
	 *            Zahl 0-4
	 */
	public void changeAction(final int act) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setAktion(act);
				doAction();
			}
		});
	}

	/**
	 * Versuche eine Nachricht zu senden, bei Fehler kommt roter Text in GUI
	 * 
	 */
	public void sendeNachricht() {
		String e = Empfaenger.getText().trim();
		String b = Betreff.getText().trim();
		String n = Nachricht.getText().trim();

		if (!e.isEmpty() && !b.isEmpty() && !n.isEmpty()) {

			MailStruktur mail = new MailStruktur(new Date(), Configuration.getsmtpUser(), b, n, null, null);
			mail.setEmpfaenger(e);
			mail.setBCC(Blindkopie.getText().trim());
			mail.setCC(Kopie.getText().trim());

			new SwingWorker<Boolean, Object>() {
				SendMail mailSender = new SendMail();

				@Override
				protected Boolean doInBackground() throws Exception {
					setlbl_Status("Versuche das Mail zu senden...", Color.BLACK);
					changeAction(0);
					return Boolean.valueOf(mailSender.send(mail));
				}

				@Override
				protected void done() {
					if (mailSender.getErfolg()) {
						//System.out.println("hier");
						MailHandler.addMailToFolder(mail, Configuration.getGesendet());
						dispose();
					} else {
						setlbl_Status(mailSender.getFehlerText(), Color.RED);
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
	 *            Mail-Inhalt entweder leer (neues Mail) oder mit bestehenden
	 *            Daten gefuellt
	 * @param action
	 *            0 = nichts (gesperrt); 1 = senden (ungesperrt)
	 */
	public MailClient_MailFenster(MailStruktur mailStruktur, int action) {
		super();
		this.MailDaten = mailStruktur;

		// label Empaenger oder Absender?
		if (Aktion == 1) {
			Empfaenger.setText(mailStruktur.getEmpfaenger());
		} else {
			Empfaenger.setText(mailStruktur.getAbsender());
		}

		// Spezialfall Postausgang
		if (Aktion == 0 && null != mailStruktur.getHerkunft()
				&& mailStruktur.getHerkunft().parent() == Configuration.getGesendet()) {
			Empfaenger.setText(mailStruktur.getEmpfaenger());
		}

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
				sendeNachricht();

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

		alles.add(macheLabelZuKomponente(EmpfAbs, Empfaenger));
		alles.add(macheLabelZuKomponente("Kopie:", Kopie));
		alles.add(macheLabelZuKomponente("Blindkopie:", Blindkopie));
		alles.add(macheLabelZuKomponente("Betreff:", Betreff));

		Nachricht.setPreferredSize(new Dimension(350, 200));

		JScrollPane spEditor = new JScrollPane(Nachricht, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		spEditor.setPreferredSize(new Dimension(350, 200));

		alles.add(macheLabelZuKomponente("Nachricht:", spEditor));// new
																	// JScrollPane(Nachricht)));
		alles.add(lbl_Status);

		add(alles);
		doAction();
		// so viele Fenster wie man m�chte erlauben und Parent nicht sperren
		setModal(false);
		// Fenster sichtbar machen
		setVisible(true);
	}

	/**
	 * Mach was, je nach Aufrufart
	 */
	public void doAction() {
		boolean setter = false;

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

		// label Empaenger oder Absender?
		if (Aktion == 1 || isAW || isWG) {
			EmpfAbs.setText("Empfaenger:");
		} else {
			EmpfAbs.setText("Absender:");
		}

		// Spezialfall Postausgang
		if (Aktion == 0 && null != this.MailDaten.getHerkunft()
				&& this.MailDaten.getHerkunft().parent() == Configuration.getGesendet()) {
			EmpfAbs.setText("Empfaenger:");
		}

		EmpfAbs.updateUI();

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

		// System.out.println("Aktion neu: " + Aktion);

		repaint();
	}

	/**
	 * Mache Label aus Text und f�ge es mit Komponente hinzu
	 * 
	 * @param label
	 *            Text fuer Label
	 * @param comp
	 *            Komponente, die belabelt werden soll
	 * @return JPane mit Label und Ursprungskomponente
	 */
	private static JPanel macheLabelZuKomponente(String label, Component comp) {
		label.trim();

		JLabel lbl = new JLabel(label + " ", SwingConstants.LEFT);
		lbl.setName("lbl_" + label.replace(":", ""));

		return macheLabelZuKomponente(lbl, comp);

	}

	/**
	 * Fasse bestehendes JLabel und Component zusammen
	 * 
	 * @param label
	 *            Label fuer Beschriftung
	 * @param comp
	 *            Komponente, die belabelt werden soll
	 * @return JPane mit Label und Ursprungskomponente
	 */
	private static JPanel macheLabelZuKomponente(JLabel label, Component comp) {
		JPanel p = new JPanel();
		label.setHorizontalAlignment(SwingConstants.LEFT);
		p.setLayout(new BorderLayout());
		p.add(label, BorderLayout.PAGE_START);
		p.add(comp, BorderLayout.CENTER);
		return p;

	}
}
