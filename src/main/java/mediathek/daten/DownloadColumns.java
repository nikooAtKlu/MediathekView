package mediathek.daten;


public enum DownloadColumns implements Column
{
    
    
    NR(0,"Nr"),
    FILMNR(1,"Filmnr"),
    ABO(2,"Abo"),
    SENDER(3,"Sender"),
    THEMA(4,"Thema"),
    TITEL(5,"Titel"),
    BUTTON_START(6,""),
    BUTTON_DEL(7,""),
    FORTSCHRITT(8,"Fortschritt"),
    RESTZEIT(9,"Restzeit"),
    GESCHWINDIGKEIT(10,"Geschwindigkeit"),
    GROESSE(11,"Größe [MB]"),
    DATUM(12,"Datum"),
    ZEIT(13,"Zeit"),
    DAUER(14,"Dauer"),
    HD(15,"HD"),
    UT(16,"UT"),
    PAUSE(17,"Pause"),
    GEO(18,"Geo"),
    URL_FILM(19,"Url Film"),
    URL_HIST(20,"Url History"),
    URL(21,"Url"),
    URL_RTMP(22,"Url RTMP"),
    URL_UT(23,"Url Untertitel"),
    PROGRAMMSET(24,"Programmset"),
    PROGRAMM(25,"Programm"),
    PROGRAMMAUFRUF(26,"Programmaufruf"),
    PROGRAMMAUFRUF_ARR(27,"Programmaufruf Array"),
    RESTART(28,"Restart"),
    DATEINAME(29,"Dateiname"),
    PFAD(30,"Pfad"),
    PFAD_DATEINAME(31,"Pfad-Dateiname"),
    ART(32,"Art"),
    QUELLE(33,"Quelle"),
    ZURUECKGESTELLT(34,"Zurückgestellt"),
    INFODATEI(35,"Infodatei"),
    SPOTLIGHT(36,"Spotlight"),
    UNTERTITEL(37,"Untertitel"),
    REMOTE_DOWNLOAD(38,"Remote Download"),
    REF(39,"Ref");

    private int id;
    private String name;

    DownloadColumns(int aId, String aName)
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
