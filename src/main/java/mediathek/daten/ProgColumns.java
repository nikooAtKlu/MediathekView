package mediathek.daten;


public enum ProgColumns implements Column
{

    BESCHREIBUNG(0,"Beschreibung"),
    ZIELDATEINAME(1,"Zieldateiname"),
    PROGRAMM(2,"Programm"),
    SCHALTER(3,"Schalter"),
    PRAEFIX(4,"Präfix"),
    SUFFIX(5,"Suffix"),
    RESTART(6,"Restart"),
    DOWNLOADMANAGER(7,"Downloadmanager");

    private int id;
    private String name;

    ProgColumns(int aId, String aName)
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
