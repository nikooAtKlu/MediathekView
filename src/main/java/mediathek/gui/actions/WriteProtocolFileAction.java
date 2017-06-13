package mediathek.gui.actions;

import static de.mediathekview.mlib.tool.Functions.getCompileDate;
import static de.mediathekview.mlib.tool.Functions.getJavaVersion;
import static de.mediathekview.mlib.tool.Functions.getPathJar;
import static de.mediathekview.mlib.tool.Functions.getProgVersion;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import de.mediathekview.mlib.tool.Log;
import de.mediathekview.mlib.tool.SysMsg;
import mediathek.config.Daten;
import mediathek.config.Konstanten;
import mediathek.config.MVConfig;
import mediathek.gui.dialog.DialogZiel;
import mediathek.tool.FormatterUtil;
import mediathek.tool.GuiFunktionen;

@SuppressWarnings("serial")
public class WriteProtocolFileAction extends AbstractAction {
    private Component owner = null;
    private static final String TITEL = "Protokoll erstellen";

    public WriteProtocolFileAction(Component owner) {
        super("Protokolldatei erstellen...");
        this.owner = owner;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //FIXME replace DialogZiel mit was sinnvollem!
        DialogZiel dialog = new DialogZiel((JFrame) owner, true, GuiFunktionen.getHomePath() + File.separator + "Mediathek.log", "Logdatei speichern");
        dialog.setVisible(true);
        if (!dialog.ok) {
            return;
        }

        try {
            final Path logFilePath = Paths.get(dialog.ziel);
            writeLogFile(logFilePath, Daten.getSettingsDirectory_String(), Daten.listePset.getListProg(), MVConfig.getAll());
            JOptionPane.showMessageDialog(owner, "Die Datei wurde erfolgreich geschrieben.", TITEL, JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(owner, "Die Datei konnte nicht geschrieben werden.", TITEL, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void writeLogFile(Path logFilePath, String settingsDir, ArrayList<String> progs, String[][] configs) throws IOException {
        try (OutputStream os = Files.newOutputStream(logFilePath);
             OutputStreamWriter osw = new OutputStreamWriter(os);
             BufferedWriter bw = new BufferedWriter(osw)) {
            bw.write("");
            bw.newLine();

            bw.write("___  ___         _ _       _   _          _    _   _ _               ");
            bw.newLine();
            bw.write("|  \\/  |        | (_)     | | | |        | |  | | | (_)              ");
            bw.newLine();
            bw.write("| .  . | ___  __| |_  __ _| |_| |__   ___| | _| | | |_  _____      __");
            bw.newLine();
            bw.write("| |\\/| |/ _ \\/ _` | |/ _` | __| '_ \\ / _ \\ |/ / | | | |/ _ \\ \\ /\\ / /");
            bw.newLine();
            bw.write("| |  | |  __/ (_| | | (_| | |_| | | |  __/   <\\ \\_/ / |  __/\\ V  V / ");
            bw.newLine();
            bw.write("\\_|  |_/\\___|\\__,_|_|\\__,_|\\__|_| |_|\\___|_|\\_\\\\___/|_|\\___| \\_/\\_/  ");
            bw.newLine();
            bw.write("");
            bw.newLine();
            bw.write("");
            bw.newLine();

            bw.write("#####################################################");
            bw.newLine();
            bw.write("Erstellt: " + LocalDateTime.now().format(FormatterUtil.FORMATTER_ddMMyyyyHHmm));
            bw.newLine();
            bw.write("#####################################################");
            bw.newLine();
            bw.newLine();
            bw.write(Konstanten.PROGRAMMNAME + ' ' + getProgVersion().toString());
            bw.newLine();
            bw.write("Compiled: " + getCompileDate());
            bw.newLine();
            bw.newLine();
            bw.write("=====================================================");
            bw.newLine();
            bw.write("Java");
            bw.newLine();
            String[] java = getJavaVersion();
            for (String ja : java) {
                bw.write(ja);
                bw.newLine();
            }
            bw.newLine();
            bw.write("=====================================================");
            bw.newLine();
            bw.write("Betriebssystem: " + System.getProperty("os.name"));
            bw.newLine();
            bw.write("Bs-Version:     " + System.getProperty("os.version"));
            bw.newLine();
            bw.write("Bs-Architektur: " + System.getProperty("os.arch"));
            bw.newLine();
            bw.newLine();
            bw.write("=====================================================");
            bw.newLine();
            bw.write("Programmpfad: " + getPathJar());
            bw.newLine();
            bw.write("Verzeichnis Einstellungen: " + settingsDir);
            bw.newLine();
            bw.newLine();
            bw.write("=====================================================");
            bw.newLine();
            final long totalMem = Runtime.getRuntime().totalMemory();
            bw.write("totalMemory: " + totalMem / (1000L * 1000L) + " MB");
            bw.newLine();
            final long maxMem = Runtime.getRuntime().maxMemory();
            bw.write("maxMemory: " + maxMem / (1000L * 1000L) + " MB");
            bw.newLine();
            final long freeMem = Runtime.getRuntime().freeMemory();
            bw.write("freeMemory: " + freeMem / (1000L * 1000L) + " MB");
            bw.newLine();
            bw.newLine();
            bw.newLine();

            //
            bw.write("#####################################################");
            bw.newLine();
            bw.write("## Programmeinstellungen ##########################");
            bw.newLine();
            bw.write("#####################################################");
            bw.newLine();
            bw.newLine();
            for (String[] s : configs) {
                if (!s[1].isEmpty()) {
                    bw.write(s[0] + '\t' + s[1]);
                    bw.newLine();
                }
            }
            bw.newLine();
            bw.newLine();
            bw.newLine();
            bw.newLine();
            //
            bw.write("#####################################################");
            bw.newLine();
            bw.write("## Programmsets ##################################");
            bw.newLine();
            bw.write("#####################################################");
            bw.newLine();
            bw.newLine();
            for (String s : progs) {
                bw.write(s);
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            bw.newLine();
            bw.newLine();
            //
            bw.write("#####################################################");
            bw.newLine();
            bw.write("## Systemmeldungen ##################################");
            bw.newLine();
            bw.write("#####################################################");
            bw.newLine();
            bw.newLine();
            bw.write(SysMsg.textSystem.toString());
            bw.newLine();
            bw.newLine();
            bw.newLine();
            bw.newLine();
            //
            bw.write("#####################################################");
            bw.newLine();
            bw.write("## Programmausgabe ##################################");
            bw.newLine();
            bw.write("#####################################################");
            bw.newLine();
            bw.newLine();
            bw.write(SysMsg.textProgramm.toString());
            bw.newLine();
            bw.newLine();
            bw.newLine();
            bw.newLine();
            //
            bw.write("#########################################################");
            bw.newLine();
            bw.write("## Fehlermeldungen                                       ");
            bw.newLine();
            ArrayList<String> retList = Log.printErrorMsg();
            for (String s : retList) {
                bw.write(s);
                bw.newLine();
            }
            retList.clear();
            bw.newLine();
            bw.newLine();
        } catch (IOException ex) {
            Log.errorLog(319865493, ex);
            throw ex;
        }
    }
}
