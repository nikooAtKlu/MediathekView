package mediathek.daten;


public enum PSetColumns implements Column
{


    SETNAME(0,"Setname"),
    PRAEFIX(1,"Präfix"),
    SUFFIX(2,"Suffix"),
    FARBE(3,"Farbe"),
    ZIELPFAD(4,"Zielpfad"),
    ZIELDATEINAME(5,"Zieldateiname"),
    THEMA_ANLEGEN(6,"Thema anlegen"),
    ABSPIELEN(7,"Abspielen"),
    SPEICHERN(8,"Speichern"),
    BUTTON(9,"Button"),
    ABO(10,"Abo"),
    LAENGE(11,"Länge"),
    LAENGE_FELD(12,"Länge Feld"),
    MAX_LAENGE(13,"max Länge"),
    MAX_LAENGE_FELD(14,"max Länge Feld"),
    AUFLOESUNG(15,"Auflösung"),
    ADDON(16,"AddOn"),
    BESCHREIBUNG(17,"Beschreibung"),
    URL_INFO(18,"Url Info"),
    INFODATEI(19,"Infodatei"),
    SPOTLIGHT(20,"Spotlight"),
    UNTERTITEL(21,"Untertitel");

    private int id;
    private String name;

    PSetColumns(int aId, String aName)
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
