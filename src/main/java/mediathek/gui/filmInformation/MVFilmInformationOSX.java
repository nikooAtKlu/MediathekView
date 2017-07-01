package mediathek.gui.filmInformation;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.net.URISyntaxException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;

import org.jdesktop.swingx.JXHyperlink;

import de.mediathekview.mlib.daten.Film;
import mediathek.config.Icons;
import mediathek.daten.ColumnManagerFactory;
import mediathek.daten.FilmColumns;
import mediathek.gui.actions.UrlHyperlinkAction;
import mediathek.gui.tools.NotScrollingCaret;
import mediathek.tool.BeobMausUrl;

/**
 * Display the current film information as a utility window.
 */
public class MVFilmInformationOSX implements IFilmInformation {

    private JDialog hudDialog = null;
    private JXHyperlink lblUrlThemaField;
    private JXHyperlink lblUrlSubtitle;
    private JTextArea textAreaBeschreibung;
    private JLabel jLabelFilmNeu;
    private JLabel jLabelFilmHD;
    private JLabel jLabelFilmUT;
    private JFrame parent = null;
    private final JLabel[] labelArrNames = new JLabel[FilmColumns.values().length];
    private final JTextField[] txtArrCont = new JTextField[FilmColumns.values().length];
    //private Film aktFilm = new Film();
    private Film aktFilm;
    private static final ImageIcon ja_sw_16 = Icons.ICON_DIALOG_EIN_SW;

    private void createDialog(JFrame parent) {
        hudDialog = new JDialog(parent);
        hudDialog.setTitle("Filminformation");
        hudDialog.setResizable(true);
        hudDialog.setType(Window.Type.UTILITY);
        hudDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public MVFilmInformationOSX(JFrame owner) {
        parent = owner;

        createDialog(owner);
        final Border emptyBorder = BorderFactory.createEmptyBorder(0, 0, 0, 0);
        //for (int i = 0; i < DatenFilm.MAX_ELEM; ++i) {
        for(FilmColumns colum : FilmColumns.values()) {
            final JLabel lbl = new JLabel(colum.getName() + ':');
            lbl.setHorizontalAlignment(SwingConstants.RIGHT);
            labelArrNames[colum.getId()] = lbl;

            final JTextField tf = new JTextField("");
            tf.setEditable(false);
            tf.setBorder(emptyBorder);
            txtArrCont[colum.getId()] = tf;
        }

        hudDialog.setContentPane(buildLayout());
        hudDialog.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        calculateHudPosition();
    }

    final private static int DEFAULT_WIDTH = 600;
    final private static int DEFAULT_HEIGHT = 450;

    private JComponent buildLayout() {
        JPanel panel = new JPanel();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        lblUrlThemaField = new JXHyperlink();
        try {
            lblUrlThemaField.setAction(new UrlHyperlinkAction(parent, ""));
        } catch (URISyntaxException ignored) {
        }
        lblUrlThemaField.addMouseListener(new BeobMausUrl(lblUrlThemaField));

        lblUrlSubtitle = new JXHyperlink();
        try {
            lblUrlSubtitle.setAction(new UrlHyperlinkAction(parent, ""));
        } catch (URISyntaxException ignored) {
        }
        lblUrlSubtitle.addMouseListener(new BeobMausUrl(lblUrlSubtitle));
        textAreaBeschreibung = new JTextArea();
        textAreaBeschreibung.setLineWrap(true);
        textAreaBeschreibung.setWrapStyleWord(true);
        textAreaBeschreibung.setRows(4);
        textAreaBeschreibung.setCaret(new NotScrollingCaret());

        jLabelFilmNeu = new JLabel();
        jLabelFilmNeu.setVisible(false);
        jLabelFilmNeu.setIcon(ja_sw_16);

        jLabelFilmHD = new JLabel();
        jLabelFilmHD.setVisible(false);
        jLabelFilmHD.setIcon(ja_sw_16);

        jLabelFilmUT = new JLabel();
        jLabelFilmUT.setVisible(false);
        jLabelFilmUT.setIcon(ja_sw_16);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 10, 4, 10);
        c.weighty = 0;
        panel.setLayout(gridbag);
        int zeile = 0;
        for (int i = 0; i < labelArrNames.length; ++i) {
            FilmColumns col = ColumnManagerFactory.getInstance().getFilmColumnById(i);
            if (col.equals(FilmColumns.URL)
                    || col.equals(FilmColumns.FILM_ABSPIELEN)
                    || col.equals(FilmColumns.FILM_AUFZEICHNEN)) {
                continue;
            }
            c.gridy = zeile;
            addComponentWithLayoutConstraints(i, gridbag, c, panel);
            ++zeile;
        }

        // zum zusammenschieben
        c.weightx = 0;
        c.gridx = 0;
        c.weighty = 2;
        c.gridy = zeile;
        JLabel label = new JLabel();
        gridbag.setConstraints(label, c);
        panel.add(label);
        return panel;
    }

    private void addComponentWithLayoutConstraints(int i, GridBagLayout gridbag, GridBagConstraints c, JPanel panel) {
        c.gridx = 0;
        c.weightx = 0;
        gridbag.setConstraints(labelArrNames[i], c);
        panel.add(labelArrNames[i]);
        c.gridx = 1;
        c.weightx = 10;
        switch (ColumnManagerFactory.getInstance().getFilmColumnById(i)) {
            case WEBSEITE:
                gridbag.setConstraints(lblUrlThemaField, c);
                panel.add(lblUrlThemaField);
                break;
//            case SUBTITEL:
//                gridbag.setConstraints(lblUrlSubtitle, c);
//                panel.add(lblUrlSubtitle);
//                break;
            case BESCHREIBUNG:
                gridbag.setConstraints(textAreaBeschreibung, c);
                panel.add(textAreaBeschreibung);
                break;
            case NEU:
                gridbag.setConstraints(jLabelFilmNeu, c);
                panel.add(jLabelFilmNeu);
                break;
            case HD:
                gridbag.setConstraints(jLabelFilmHD, c);
                panel.add(jLabelFilmHD);
                break;
            case UT:
                gridbag.setConstraints(jLabelFilmUT, c);
                panel.add(jLabelFilmUT);
                break;
            default:
                gridbag.setConstraints(txtArrCont[i], c);
                panel.add(txtArrCont[i]);
                break;
        }
    }

    private void calculateHudPosition() {
        final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        final DisplayMode dm = gd.getDisplayMode();
        hudDialog.setLocation(dm.getWidth() - DEFAULT_WIDTH, 0);
    }

    @Override
    public void showInfo() {
        updateTextFields();
        hudDialog.setVisible(true);
    }

    @Override
    public boolean isVisible() {
        return hudDialog.isVisible();
    }

    @Override
    public void updateCurrentFilm(Film film) {
        aktFilm = film;
        if (isVisible()) {
            updateTextFields();
        }
    }

    private void clearAllFields() {
        for (JTextField aTxtArrCont : txtArrCont) {
            aTxtArrCont.setText("");
        }
        textAreaBeschreibung.setText(" ");
        lblUrlThemaField.setText("");
        lblUrlSubtitle.setText("");
        jLabelFilmNeu.setVisible(false);
        jLabelFilmHD.setVisible(false);
        jLabelFilmUT.setVisible(false);
    }

    private void updateTextFields() {
        if (aktFilm == null) {
            clearAllFields();
        } else {
            for (int i = 0; i < txtArrCont.length; ++i)
            {
                txtArrCont[i].setText(ColumnManagerFactory.getInstance().getFilmColumnById(i).getName());
            }
            if (aktFilm.getBeschreibung().isEmpty())
            {
                // sonst müsste die Größe gesetzt werden
                textAreaBeschreibung.setText(" ");
            } else
            {
                textAreaBeschreibung.setText(aktFilm.getBeschreibung());
            }
            lblUrlThemaField.setText(aktFilm.getWebsite().toString());
            lblUrlSubtitle.setText(aktFilm.getSubtitles().iterator().hasNext() ? aktFilm.getSubtitles().iterator().next().toString() : "");
            jLabelFilmNeu.setVisible(aktFilm.isNeu());
            jLabelFilmHD.setVisible(aktFilm.hasHD());
            jLabelFilmUT.setVisible(aktFilm.hasUT());
        }
        hudDialog.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        //Whenever there is a change event, reset HUD info to nothing
    }
}
