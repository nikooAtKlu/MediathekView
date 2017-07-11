/*    
 *    MediathekView
 *    Copyright (C) 2008   W. Xaver
 *    W.Xaver[at]googlemail.com
 *    http://zdfmediathk.sourceforge.net/
 *    
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mediathek.filmlisten;

import java.util.ArrayList;
import java.util.List;

import de.mediathekview.mlib.daten.Film;
import de.mediathekview.mlib.daten.ListeFilme;
import de.mediathekview.mlib.daten.Qualities;
import mediathek.config.Daten;
import mediathek.daten.ColumnManagerFactory;
import mediathek.daten.FilmColumns;
import mediathek.tool.Filter;
import mediathek.tool.MVTable;
import mediathek.tool.TModel;
import mediathek.tool.TModelFilm;

public class GetModelTabFilme {
    private static final String THEMA_LIVE = "Livestream";

    public static synchronized void getModelTabFilme(ListeFilme listeFilme, Daten ddaten, MVTable table,
            String filterSender, String filterThema, String filterTitel, String filterThemaTitel, String filterIrgendwo,
            int laenge, boolean min, boolean keineAbos, boolean kGesehen, boolean nurHd, boolean nurUt, boolean live, boolean nurNeue) {
        // Model für die Tabelle Filme zusammenbauen
        if (listeFilme.isEmpty()) {
            // wenn die Liste leer ist, dann Tschüss
            table.setModel(new TModelFilm(new Object[][]{}, ColumnManagerFactory.getInstance().getFilmColumnAllNames()));
            return;
        }
        // dann ein neues Model anlegen
        TModel tModel = new TModelFilm(new Object[][]{}, ColumnManagerFactory.getInstance().getFilmColumnAllNames());
        if (filterSender.isEmpty() && filterThema.isEmpty() && filterTitel.isEmpty() && filterThemaTitel.isEmpty() && filterIrgendwo.isEmpty() && laenge == 0
                && !keineAbos && !kGesehen && !nurHd && !nurUt && !live && !nurNeue) {
            // dann ganze Liste laden
            addObjectDataTabFilme(listeFilme, tModel);
        } else {
            // Titel
            String[] arrTitel;
            if (Filter.isPattern(filterTitel)) {
                arrTitel = new String[]{filterTitel};
            } else {
                arrTitel = filterTitel.split(",");
                for (int i = 0; i < arrTitel.length; ++i) {
                    arrTitel[i] = arrTitel[i].trim().toLowerCase();
                }
            }
            // ThemaTitel
            String[] arrThemaTitel;
            if (Filter.isPattern(filterThemaTitel)) {
                arrThemaTitel = new String[]{filterThemaTitel};
            } else {
                arrThemaTitel = filterThemaTitel.split(",");
                for (int i = 0; i < arrThemaTitel.length; ++i) {
                    arrThemaTitel[i] = arrThemaTitel[i].trim().toLowerCase();
                }
            }
            // Irgendwo
            String[] arrIrgendwo;
            if (Filter.isPattern(filterIrgendwo)) {
                arrIrgendwo = new String[]{filterIrgendwo};
            } else {
                arrIrgendwo = filterIrgendwo.split(",");
                for (int i = 0; i < arrIrgendwo.length; ++i) {
                    arrIrgendwo[i] = arrIrgendwo[i].trim().toLowerCase();
                }
            }
            for (Film film : listeFilme) {
                if (nurNeue) {
                    if (!film.isNeu()) {
                        continue;
                    }
                }
                if (live) {
                    if (!film.getThema().equals(THEMA_LIVE)) {
                        continue;
                    }
                }
                if (nurHd) {
                    if (!film.hasHD()) {
                        continue;
                    }
                }
                if (nurUt) {
                    if (!film.hasUT()) {
                        continue;
                    }
                }
                if (keineAbos) {
                    if (!ddaten.getListeAbo().hasAbo(film)) {
                        continue;
                    }
                }
                if (kGesehen) {
                    if (ddaten.history.urlPruefen(film.getUrl(Qualities.NORMAL))) {
                        continue;
                    }
                }
                if (Filter.filterAufFilmPruefen(filterSender, filterThema, arrTitel, arrThemaTitel, arrIrgendwo, laenge, min, film, true /*länge nicht prüfen*/)) {
                    addObjectDataTabFilme(tModel, film);
                }
            }
            // listeFilme.stream().filter((DatenFilm film) -> Filter.filterAufFilmPruefen(filterSender,
            //      filterThema, arrTitel, arrThemaTitel, arrIrgendwo, laenge, film, true /*länge nicht prüfen*/)).forEach(f -> addObjectDataTabFilme(tModel, f));

        }
        table.setModel(tModel);
    }

    //===================================
    // private
    //===================================
    private static void addObjectDataTabFilme(ListeFilme listefilme, TModel tModel) {
        if (!listefilme.isEmpty()) {
            for (Film film : listefilme) {
                addObjectDataTabFilme(tModel, film);
            }
        }
    }

    private static void addObjectDataTabFilme(TModel tModel, Film film) {
        List<Object> objects = new ArrayList<>();
        objects.add(film.getUuid());
        objects.add(film.getTime());
        objects.add(film.getFileSize(Qualities.NORMAL));
        objects.add(film);
        objects.add(film.isNeu() ? "1" : "0");
        objects.add(film.hasHD() ? "1" : "0");
        objects.add(film.hasUT() ? "1" : "0");

        tModel.addRow(objects.toArray());
    }
}
