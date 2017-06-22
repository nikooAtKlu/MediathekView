package mediathek.daten;

/**
 * Created by nicklas on 06.06.17.
 */
public enum AboColumns implements Column
{
    NR(0,"Nr"),
    (1,"aktiv"),
    NAME(2,"Name"),
    SENDER(3,"Sender"),
    THEMA(4,"Thema"), 
    TITEL(5,"Titel"),
    THEMA_TITEL(6,"Thema-Titel"), 
    IRGENDWO(7,"Irgendwo"), 
    DAUER(8,"Dauer"), 
    MIN_MAX(9,"min/max"), 
    ZIELPFAD(10,"Zielpfad"), 
    LETZTES_ABO(11,"letztes Abo"),
    PROGRAMMSET(12,"Programmset");

    private int id;
    private String name;

    FilmCoulumns(int aId, String aName)
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
