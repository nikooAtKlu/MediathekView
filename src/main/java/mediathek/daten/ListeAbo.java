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
package mediathek.daten;

import de.mediathekview.mlib.daten.Film;
import de.mediathekview.mlib.daten.ListeFilme;
import de.mediathekview.mlib.tool.*;
import mediathek.config.Daten;
import mediathek.config.MVConfig;
import mediathek.gui.dialog.DialogEditAbo;
import mediathek.tool.Filter;
import mediathek.tool.FormatterUtil;
import mediathek.tool.MVMessageDialog;
import mediathek.tool.TModelAbo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@SuppressWarnings("serial")
public class ListeAbo extends LinkedList<DatenAbo>
{
    private static final Logger LOG = LogManager.getLogger(ListeAbo.class);
    private final Daten daten;
    private final Map<Integer, DatenAbo> aboMap;
    private static final String[] LEER = {""};

    public ListeAbo(Daten ddaten)
    {
        daten = ddaten;
        aboMap = new HashMap<>();
    }

    private int nr;

    public boolean addAbo(String aboName)
    {
        return addAbo(aboName, "", "", "");
    }

    public boolean addAbo(String aboname, String filmSender, String filmThema, String filmTitel)
    {
        int min;
        try
        {
            min = Integer.parseInt(MVConfig.get(MVConfig.Configs.SYSTEM_ABO_MIN_SIZE));
        } catch (Exception ex)
        {
            min = 0;
            MVConfig.add(MVConfig.Configs.SYSTEM_ABO_MIN_SIZE, "0");
        }
        return addAbo(filmSender, filmThema, filmTitel, "", "", min, true/*min*/, aboname);
    }

    public boolean addAbo(String filmSender, String filmThema, String filmTitel, String filmThemaTitel, String irgendwo, int mindestdauer, boolean min, String namePfad)
    {
        //abo anlegen, oder false wenns schon existiert
        boolean ret = false;
        namePfad = FilenameUtils.replaceLeerDateiname(namePfad, false /*nur ein Ordner*/,
                Boolean.parseBoolean(MVConfig.get(MVConfig.Configs.SYSTEM_USE_REPLACETABLE)),
                Boolean.parseBoolean(MVConfig.get(MVConfig.Configs.SYSTEM_ONLY_ASCII)));
        DatenAbo datenAbo = new DatenAbo(namePfad /* name */, filmSender, filmThema, filmTitel, filmThemaTitel, irgendwo, mindestdauer, min, namePfad, "");
        DialogEditAbo dialogEditAbo = new DialogEditAbo(Daten.getInstance().getMediathekGui(), true, daten, datenAbo, false /*onlyOne*/);
        dialogEditAbo.setVisible(true);
        if (dialogEditAbo.ok)
        {
            if (!aboExistiertBereits(datenAbo))
            {
                MVConfig.add(MVConfig.Configs.SYSTEM_ABO_MIN_SIZE, datenAbo.arr[DatenAbo.ABO_MINDESTDAUER]); // als Vorgabe merken
                addAbo(datenAbo);
                aenderungMelden();
                sort();
                ret = true;
            } else
            {
                MVMessageDialog.showMessageDialog(null, "Abo existiert bereits", "Abo anlegen", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        return ret;
    }

    public void addAbo(DatenAbo datenAbo)
    {
        // die Änderung an der Liste wird nicht gemeldet!!
        // für das Lesen der Konfig-Datei beim Programmstart
        ++nr;
        datenAbo.nr = nr;
        if (datenAbo.arr[DatenAbo.ABO_NAME].isEmpty())
        {
            // Downloads ohne "Aboname" sind manuelle Downloads
            datenAbo.arr[DatenAbo.ABO_NAME] = "Abo_" + nr;
        }
        datenAbo.setMindestDauerMinuten();
        if (datenAbo.arr[DatenAbo.ABO_MIN].isEmpty())
        {
            //zum Erhalt der alten Funktionalität
            datenAbo.arr[DatenAbo.ABO_MIN] = Boolean.TRUE.toString();
        }
        datenAbo.min = Boolean.parseBoolean(datenAbo.arr[DatenAbo.ABO_MIN]);
        super.add(datenAbo);
    }

    public void aboLoeschen(DatenAbo abo)
    {
        if (abo != null)
        {
            this.remove(abo);
            aenderungMelden();
        }
    }

    public void aenderungMelden()
    {
        // Filmliste anpassen
        setAboFuerFilm(Daten.getInstance().getListeFilme(), true /*aboLoeschen*/);
        Listener.notify(Listener.EREIGNIS_LISTE_ABOS, ListeAbo.class.getSimpleName());
    }

    public DatenAbo getAboNr(int i)
    {
        return this.get(i);
    }

    public void sort()
    {
        Collections.sort(this);
    }

    public void addObjectData(TModelAbo model, String sender)
    {
        model.setRowCount(0);
        Object[] object = new Object[DatenAbo.MAX_ELEM];
        for (DatenAbo datenAbo : this)
        {
            if (sender.isEmpty() || sender.equals(datenAbo.arr[AboColumns.SENDER.getId()]))
            {
                for (AboColumns col : AboColumns.values())
                {
                    switch (col)
                    {
                        case NR:
                            object[col.getId()] = datenAbo.nr;
                        case DAUER:
                            object[col.getId()] = datenAbo.mindestdauerMinuten;
                        case LETZTES_ABO:
                            object[col.getId()] = getDatumForObject(datenAbo.arr[DatenAbo.ABO_DOWN_DATUM]);
                        case AKTIV:
                            object[col.getId()] = Boolean.valueOf(datenAbo.aboIstEingeschaltet());
                        case MIN_MAX:
                            object[col.getId()] = datenAbo.min ? "min" : "max";
                        case NAME:
                            if (!DatenAbo.anzeigen(col.getId()))
                            {
                                object[col.getId()] = "";
                            } else
                            {
                                final int id = col.getId();
                                object[id] = datenAbo.arr[id];
                            }
                            break;
                        default:
                            final int id = col.getId();
                            object[id] = datenAbo.arr[id];
                            break;
                    }
                }
            }
            model.addRow(object);
        }
    }

    public LocalDateTime getDatumForObject(String datum)
    {
        if (datum.isEmpty())
        {
            return LocalDateTime.now();
        } else
        {
            try
            {
                return LocalDateTime.parse(datum, FormatterUtil.FORMATTER_ddMMyyyy);
            } catch (DateTimeParseException dateTimeParseException)
            {
                LOG.debug(String.format("Das Datum für ein Abo-Objekt konnte nicht geparsed werden: \"%s\"", datum));
                return LocalDateTime.now();
            }
        }
    }

    public ArrayList<String> getPfade()
    {
        // liefert eine Array mit allen Pfaden
        ArrayList<String> pfade = new ArrayList<>();
        for (DatenAbo abo : this)
        {
            String s = abo.arr[DatenAbo.ABO_ZIELPFAD];
            if (!pfade.contains(s))
            {
                pfade.add(abo.arr[DatenAbo.ABO_ZIELPFAD]);
            }
        }
        GermanStringSorter sorter = GermanStringSorter.getInstance();
        pfade.sort(sorter);
        return pfade;
    }

    private boolean aboExistiertBereits(DatenAbo abo)
    {
        // true wenn es das Abo schon gibt
        for (DatenAbo datenAbo : this)
        {
            if (Filter.aboExistiertBereits(datenAbo, abo))
            {
                return true;
            }
        }
        return false;
    }

    public DatenAbo getAboFuerFilm_schnell(Film film, boolean laengePruefen)
    {
        // da wird nur in der Filmliste geschaut, ob in "DatenFilm" ein Abo eingetragen ist
        // geht schneller, "getAboFuerFilm" muss aber vorher schon gelaufen sein!!
        if (aboMap.containsKey(film.hashCode()))
        {
            final DatenAbo abo = aboMap.get(film.hashCode());
            if (laengePruefen)
            {
                if (!Filter.laengePruefen(abo.mindestdauerMinuten, film.getDuration().getSeconds(), abo.min))
                {
                    return null;
                }
            }
            return abo;
        } else
        {
            return null;
        }
    }

    private void deleteAboInFilm(Film aFilm)
    {
        // für jeden Film Abo löschen
        aboMap.remove(aFilm.hashCode());
    }

    private void createAbo(DatenAbo abo)
    {
        if (abo.arr[DatenAbo.ABO_TITEL].isEmpty())
        {
            abo.titel = LEER;
        } else
        {
            abo.titel = Filter.isPattern(abo.arr[DatenAbo.ABO_TITEL])
                    ? new String[]{abo.arr[DatenAbo.ABO_TITEL]} : abo.arr[DatenAbo.ABO_TITEL].toLowerCase().split(",");
        }
        if (abo.arr[DatenAbo.ABO_THEMA_TITEL].isEmpty())
        {
            abo.thema = LEER;
        } else
        {
            abo.thema = Filter.isPattern(abo.arr[DatenAbo.ABO_THEMA_TITEL])
                    ? new String[]{abo.arr[DatenAbo.ABO_THEMA_TITEL]} : abo.arr[DatenAbo.ABO_THEMA_TITEL].toLowerCase().split(",");
        }
        if (abo.arr[DatenAbo.ABO_IRGENDWO].isEmpty())
        {
            abo.irgendwo = LEER;
        } else
        {
            abo.irgendwo = Filter.isPattern(abo.arr[DatenAbo.ABO_IRGENDWO])
                    ? new String[]{abo.arr[DatenAbo.ABO_IRGENDWO]} : abo.arr[DatenAbo.ABO_IRGENDWO].toLowerCase().split(",");
        }
    }

    /**
     * Assign found abo to the film objects.
     * Time-intensive procedure!
     *
     * @param film assignee
     */
    private void assignAboToFilm(Film film)
    {
        final DatenAbo foundAbo = this.stream().filter(abo
                -> Filter.filterAufFilmPruefen(abo.arr[DatenAbo.ABO_SENDER], abo.arr[DatenAbo.ABO_THEMA],
                abo.titel,
                abo.thema,
                abo.irgendwo,
                abo.mindestdauerMinuten,
                abo.min,
                film, false)).findFirst().orElse(null);

        if (foundAbo != null)
        {
            aboMap.put(film.hashCode(), foundAbo);
            /*if (!Filter.laengePruefen(foundAbo.mindestdauerMinuten, film.getDuration().getSeconds(), foundAbo.min)) {
                // dann ist der Film zu kurz
                aboMap.put(film.hashCode(),foundAbo);
            } else {
                film.abo = foundAbo;
            }*/
            //Nicklas2721: Konnte keine Stelle finden an der "FILM_ABO_NAME" angezeigt wird => Information wird nicht mehr verwendet.
        } else
        {
            deleteAboInFilm(film);
        }
    }

    public void setAboFuerFilm(ListeFilme listeFilme, boolean aboLoeschen)
    {
        // hier wird tatsächlich für jeden Film die Liste der Abos durchsucht
        // braucht länger

        Duration.counterStart("Abo in Filmliste eintragen");

        if (this.isEmpty() && aboLoeschen)
        {
            listeFilme.parallelStream().forEach(this::deleteAboInFilm);
            return;
        }

        // leere Abos löschen, die sind Fehler
        this.stream().filter((datenAbo) -> (datenAbo.isEmpty())).forEach(this::remove);

        // und jetzt erstellen
        forEach(this::createAbo);

        // das kostet die Zeit!!
        listeFilme.parallelStream().forEach(this::assignAboToFilm);

        // und jetzt wieder löschen
        forEach(datenAbo ->
        {
            datenAbo.titel = LEER;
            datenAbo.thema = LEER;
            datenAbo.irgendwo = LEER;
        });

        Duration.counterStop("Abo in Filmliste eintragen");
    }

    public boolean hasAbo(final Film aFilm)
    {
        return aboMap.containsKey(aFilm);
    }
}
