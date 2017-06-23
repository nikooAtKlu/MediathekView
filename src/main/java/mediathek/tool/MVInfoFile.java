/*
 * MediathekView
 * Copyright (C) 2014 W. Xaver
 * W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mediathek.tool;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;

import de.mediathekview.mlib.daten.Film;
import de.mediathekview.mlib.daten.Qualities;
import de.mediathekview.mlib.tool.FilenameUtils;
import de.mediathekview.mlib.tool.Log;
import de.mediathekview.mlib.tool.SysMsg;
import mediathek.config.Daten;
import mediathek.config.MVConfig;
import mediathek.daten.DatenDownload;
import mediathek.daten.DatenPset;
import mediathek.daten.FilmColumns;
import mediathek.daten.ListePset;
import mediathek.gui.dialog.DialogZiel;

public class MVInfoFile {
	
	private static final String INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE = ":      ";

    public static void writeInfoFile(JFrame paFrame, Daten daten, Film film) {
        String titel = film.getTitel();
        titel = FilenameUtils.replaceLeerDateiname(titel, false /*pfad*/,
                Boolean.parseBoolean(MVConfig.get(MVConfig.Configs.SYSTEM_USE_REPLACETABLE)),
                Boolean.parseBoolean(MVConfig.get(MVConfig.Configs.SYSTEM_ONLY_ASCII)));
        String pfad = "";
        ListePset lp = Daten.listePset.getListeSpeichern();
        if (!lp.isEmpty()) {
            DatenPset p = lp.get(0);
            pfad = p.getZielPfad();
        }
        if (pfad.isEmpty()) {
            pfad = GuiFunktionen.getStandardDownloadPath();
        }
        if (titel.isEmpty()) {
        	//TODO: Nicklas kontrolle
            //titel = film.arr[DatenFilm.FILM_SENDER].replace(" ", "-") + ".txt";
            titel = film.getSender().toString().replace(" ", "-") + ".txt";
        } else {
            titel = titel + ".txt";
        }
        pfad = GuiFunktionen.addsPfad(pfad, titel);
        DialogZiel dialog = new DialogZiel(paFrame, true, pfad, "Infos speichern");
        dialog.setVisible(true);
        if (!dialog.ok) {
            return;
        }

        Path path = Paths.get(dialog.ziel);
        
        try (BufferedWriter br = new BufferedWriter(new OutputStreamWriter(new DataOutputStream(Files.newOutputStream(path))))) {
            br.write(FilmColumns.SENDER + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + film.getSender());
            br.write("\n");
            br.write(FilmColumns.THEMA + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + film.getThema());
            br.write("\n\n");
            br.write(FilmColumns.TITEL + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + film.getTitel());
            br.write("\n\n");
            br.write(FilmColumns.DATUM + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + film.getTime().format(FormatterUtil.FORMATTER_ddMMyyyy));
            br.write("\n");
            br.write(FilmColumns.ZEIT + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + film.getTime().format(FormatterUtil.FORMATTER_HHmmss));
            br.write("\n");
            br.write(FilmColumns.DAUER + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + film.getDuration());
            br.write("\n");
            //br.write(DatenDownload.COLUMN_NAMES[DatenDownload.DOWNLOAD_GROESSE] + ":  " + film.arr[DatenFilm.FILM_GROESSE]);
            br.write(FilmColumns.GROESSE + ":  " + film.getFileSize(Qualities.NORMAL)); // Welche Qualitie
            br.write("\n\n");

            br.write(FilmColumns.WEBSEITE + "\n");
            br.write(film.getWebsite().toString());
            br.write("\n\n");

            br.write(FilmColumns.URL + "\n");
            br.write(film.getUrl(Qualities.NORMAL).toString());
            br.write("\n\n");
            //TODO: Nicklas kann wohl weg?
//            if (!film.arr[DatenFilm.FILM_URL_RTMP].isEmpty()) {
//                br.write(DatenFilm.COLUMN_NAMES[DatenFilm.FILM_URL_RTMP] + '\n');
//                br.write(film.arr[DatenFilm.FILM_URL_RTMP]);
//                br.write("\n\n");
//            }

            int anz = 0;
            for (String s : film.getBeschreibung().split(" ")) {
                anz += s.length();
                br.write(s + ' ');
                if (anz > 50) {
                    br.write("\n");
                    anz = 0;
                }
            }
            br.write("\n\n");
            br.flush();
        } catch (IOException ex) {
            Log.errorLog(632656214, dialog.ziel);
        }
    }

    public static void writeInfoFile(DatenDownload datenDownload) {
        SysMsg.sysMsg(new String[]{"Infofile schreiben nach: ", datenDownload.arr[DatenDownload.DOWNLOAD_ZIEL_PFAD]});

        new File(datenDownload.arr[DatenDownload.DOWNLOAD_ZIEL_PFAD]).mkdirs();
        Path path = Paths.get(datenDownload.getFileNameWithoutSuffix() + ".txt");
        try (DataOutputStream dos = new DataOutputStream(Files.newOutputStream(path));
             OutputStreamWriter osw = new OutputStreamWriter(dos);
             BufferedWriter br = new BufferedWriter(osw)) {
            if (datenDownload.film != null) {
                br.write(FilmColumns.SENDER + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + datenDownload.film.getSender());
                br.write("\n");
                br.write(FilmColumns.THEMA + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + datenDownload.film.getThema());
                br.write("\n\n");
                br.write(FilmColumns.TITEL + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + datenDownload.film.getTitel());
                br.write("\n\n");
                br.write(FilmColumns.DATUM + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + datenDownload.film.getTime().format(FormatterUtil.FORMATTER_ddMMyyyy));
                br.write("\n");
                br.write(FilmColumns.ZEIT + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + datenDownload.film.getTime().format(FormatterUtil.FORMATTER_HHmmss));
                br.write("\n");
                br.write(FilmColumns.DAUER + INFOFILE_KEY_VALUE_TRENNZEICHENKETTTE + datenDownload.film.getDuration());
                br.write("\n");
                br.write(DatenDownload.COLUMN_NAMES[DatenDownload.DOWNLOAD_GROESSE] + ":  " + datenDownload.mVFilmSize);
                br.write("\n\n");

                br.write(FilmColumns.WEBSEITE + "\n");
                br.write(datenDownload.film.getWebsite().toString());
                br.write("\n\n");
            }

            br.write(DatenDownload.COLUMN_NAMES[DatenDownload.DOWNLOAD_URL] + '\n');
            br.write(datenDownload.arr[DatenDownload.DOWNLOAD_URL]);
            br.write("\n\n");
            if (!datenDownload.arr[DatenDownload.DOWNLOAD_URL_RTMP].isEmpty()
                    && !datenDownload.arr[DatenDownload.DOWNLOAD_URL_RTMP].equals(datenDownload.arr[DatenDownload.DOWNLOAD_URL])) {
                br.write(DatenDownload.COLUMN_NAMES[DatenDownload.DOWNLOAD_URL_RTMP] + '\n');
                br.write(datenDownload.arr[DatenDownload.DOWNLOAD_URL_RTMP]);
                br.write("\n\n");
            }

            if (datenDownload.film != null) {
                int anz = 0;
                //TODO: Nicklas kontrolle
                //for (String s : datenDownload.film.arr[DatenFilm.FILM_BESCHREIBUNG].split(" ")) {
                for (String s : datenDownload.film.getBeschreibung().split(" ")) {
                    anz += s.length();
                    br.write(s + ' ');
                    if (anz > 50) {
                        br.write("\n");
                        anz = 0;
                    }
                }
            }
            br.write("\n\n");
            br.flush();
            SysMsg.sysMsg(new String[]{"Infofile", "  geschrieben"});
        } catch (IOException ex) {
            Log.errorLog(975410369, datenDownload.arr[DatenDownload.DOWNLOAD_ZIEL_PFAD_DATEINAME]);
        }
    }

}
