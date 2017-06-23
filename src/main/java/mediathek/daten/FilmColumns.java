package mediathek.daten;

/**
 * Created by nicklas on 06.06.17.
 */
public enum FilmColumns implements Column
{
    NR(0,"Nr"),
    SENDER(1,"Sender"),
    THEMA(2,"Thema"), TITEL(3,"Titel"),
            FILM_ABSPIELEN(4,""), FILM_AUFZEICHNEN(5,""), DATUM(6,"Datum"), ZEIT(7,"Zeit"), DAUER(8,"Dauer"), GROESSE(9,"Größe [MB]")
    , HD(10,"HD"), UT(11,"UT"),
            BESCHREIBUNG(12,"Beschreibung"), GEO(13,"Geo"), URL(14,"Url"), ABO(15,"Abo");

    private int id;
    private String name;

    FilmColumns(int aId, String aName)
    {
        id=aId;
        name=aName;
    }

    @Override
    public int getId()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
