package mediathek.daten;

/**
 * Created by nicklas on 06.06.17.
 */
public enum FilmCoulumns
{
    NR(0,"Nr"),
    SENDER(1,"Sender"),
    THEMA(2,"Thema"), TITEL(3,"Titel"),
            FILM_ABSPIELEN(4,""), FILM_AUFZEICHNEN(5,""), DATUM(6,"Datum"), ZEIT(7,"Zeit"), DAUER(8,"Dauer"), GROESSE(9,"Größe [MB]")
    , HD(10,"HD"), UT(11,"UT"),
            BESCHREIBUNG(12,"Beschreibung"), GEO(13,"Geo"), URL(14,"Url"), ABO(15,"Abo");

    private int id;
    private String name;

    FilmCoulumns(int aId, String aName)
    {
        id=aId;
        name=aName;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    /**
     * Returns the Column with the given ID.
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public static FilmCoulumns getById(int aId)
    {
        for(FilmCoulumns filmColumn : FilmCoulumns.values())
        {
            if(aId == filmColumn.getId())
            {
                return filmColumn;
            }
        }
        return null;
    }
}
