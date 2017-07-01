package mediathek.daten;


public enum MediaDbColumns implements Column
{

    NAME(0,"Name"),
    PFAD(1,"Pfad"),
    GROESSE_MB(2,"Größe [MB]"),
    EXTERN(3,"Extern");

    private int id;
    private String name;

    MediaDbColumns(int aId, String aName)
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
