package mediathek.gui.filmInformation;

import javax.swing.event.ChangeListener;
import de.mediathekview.mlib.daten.Film;

/**
 * Display the current film information
 */
public interface IFilmInformation extends ChangeListener {

    void showInfo();

    boolean isVisible();

    void updateCurrentFilm(Film film);

}
