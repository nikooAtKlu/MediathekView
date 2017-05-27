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
package mediathek.controller;

import de.mediathekview.mlib.tool.Functions;
import de.mediathekview.mlib.tool.GermanStringSorter;
import de.mediathekview.mlib.tool.Log;

import java.net.URI;

public class MVUsedUrl implements Comparable<MVUsedUrl> {
    public static final String[] title = {"Datum", "Thema", "Titel", "Url"};
    private static final GermanStringSorter sorter = GermanStringSorter.getInstance();
    private final static String TRENNER = "  |###|  ";
    private final static String PAUSE = " |#| ";
    public static final int DATUM_TABLE_COLUMNNUMBER = 0;
    public static final int THEMA_TABLE_COLUMNNUMBER = 1;
    public static final int TITLE_TABLE_COLUMNNUMBER = 2;
    public static final int URL_TABLE_COLUMNNUMBER = 3;


    private String dateAsText;
    private String thema;
    private String filmTitle;
    private URI url;

    public MVUsedUrl(String aDate, String aThema, String aFilmTitle, URI aUrl) {
        dateAsText = aDate;
        thema= aThema;
        filmTitle = aFilmTitle;
        url = aUrl;
    }

    public static String getUsedUrl(String date, String thema, String title, URI url) {
        return date + PAUSE
                + Functions.textLaenge(25, putzen(thema), false /* mitte */, false /*addVorne*/) + PAUSE
                + Functions.textLaenge(40, putzen(title), false /* mitte */, false /*addVorne*/) + TRENNER
                + url + '\n';
    }

    public String getUsedUrl() {
        return dateAsText + PAUSE
                + Functions.textLaenge(25, putzen(thema), false /* mitte */, false /*addVorne*/) + PAUSE
                + Functions.textLaenge(40, putzen(filmTitle), false /* mitte */, false /*addVorne*/) + TRENNER
                + url + '\n';
    }

    public static MVUsedUrl getUrlAusZeile(String zeile) {
        // 29.05.2014 |#| Abendschau                |#| Patenkind trifft Groß                     |###|  http://cdn-storage.br.de/iLCpbHJGNLT6NK9HsLo6s61luK4C_2rc5U1S/_-OS/5-8y9-NP/5bb33365-038d-46f7-914b-eb83fab91448_E.mp4
        URI url = null;
        String thema = "", titel = "", datum = "";
        int a1;
        try {
            if (zeile.contains(TRENNER)) {
                //neues Logfile-Format
                a1 = zeile.lastIndexOf(TRENNER);
                a1 += TRENNER.length();
                url = new URI(zeile.substring(a1).trim());
                // titel
                titel = zeile.substring(zeile.lastIndexOf(PAUSE) + PAUSE.length(), zeile.lastIndexOf(TRENNER)).trim();
                datum = zeile.substring(0, zeile.indexOf(PAUSE)).trim();
                thema = zeile.substring(zeile.indexOf(PAUSE) + PAUSE.length(), zeile.lastIndexOf(PAUSE)).trim();
            } else {
                url = new URI(zeile);
            }
        } catch (Exception ex) {
            Log.errorLog(398853224, ex);
        }
        return new MVUsedUrl(datum, thema, titel, url);
    }

    public static String getHeaderString() {
        return Functions.textLaenge(40, "Titel", false /* mitte */, false /*addVorne*/)
                + "    " + Functions.textLaenge(25, "Thema", false /* mitte */, false /*addVorne*/)
                + "    " + Functions.textLaenge(10, "Datum", false /* mitte */, false /*addVorne*/)
                + "    " + "Url";
    }

    public String getString() {
        return Functions.textLaenge(40, filmTitle, false /* mitte */, false /*addVorne*/)
                + "    " + Functions.textLaenge(25, thema, false /* mitte */, false /*addVorne*/)
                + "    " + (dateAsText.isEmpty() ? "          " : dateAsText)
                + "    " + url;
    }

    public URI getUrl() {
        return url;
    }

    public String getDateAsText()
    {
        return dateAsText;
    }

    public String getThema()
    {
        return thema;
    }

    public String getFilmTitle()
    {
        return filmTitle;
    }


    @Override
    public int compareTo(MVUsedUrl arg0) {
        return sorter.compare(filmTitle, arg0.filmTitle);
    }

    private static String putzen(String s) {
        s = s.replace("\n", "");
        s = s.replace("|", "");
        s = s.replace(TRENNER, "");
        return s;
    }
}
