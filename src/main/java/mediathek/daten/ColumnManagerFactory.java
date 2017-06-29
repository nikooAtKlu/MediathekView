package mediathek.daten;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ColumnManagerFactory {
    private static ColumnManagerFactory instance;
    
    public static ColumnManagerFactory getInstance()
    {
        if(instance == null) instance = new ColumnManagerFactory();
        return instance;
    }
    
    private ColumnManagerFactory()
    {
        super();
    }
    
    
    public Collection<Column> getFilmColumns()
    {
        return Arrays.asList(FilmColumns.values());
    }
    
    public Collection<Column> getDownloadColumns()
    {
        return Arrays.asList(DownloadColumns.values());
    }
    
    public Collection<Column> getAboColumns()
    {
        return Arrays.asList(AboColumns.values());
    }
    
    /**
     * Returns the Column with the given ID.
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public Column getById(int aId, Collection<Column> aColumns)
    {
        for(Column column : aColumns)
        {
            if(aId == column.getId())
            {
                return column;
            }
        }
        return null;
    }
    
    /**
     * Returns the Column with the given ID.
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public FilmColumns getFilmColumnById(int aId)
    {
        return (FilmColumns) getById(aId,getFilmColumns());
    }
    
    /**
     * Returns the Column with the given ID.
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public Column getDownloadColumnById(int aId)
    {
        return getById(aId,getDownloadColumns());
    }
    
    /**
     * Returns the Column with the given ID.
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public Column getAboColumnById(int aId)
    {
        return getById(aId,getAboColumns());
    }
    
    public String[] getFilmColumnAllNames() {
    	String[] names = new String[FilmColumns.values().length];
    	for(FilmColumns colum : FilmColumns.values()) {
    		names[colum.getId()] = colum.getName();
    	}
		return names;
    }
    
    public String[] getAboColumnAllNames() {
    	String[] names = new String[AboColumns.values().length];
    	for(AboColumns colum : AboColumns.values()) {
    		names[colum.getId()] = colum.getName();
    	}
		return names;
    }
    
    public String[] getDownloadColumnAllNames() {
    	String[] names = new String[DownloadColumns.values().length];
    	for(DownloadColumns colum : DownloadColumns.values()) {
    		names[colum.getId()] = colum.getName();
    	}
		return names;
    }
    
    public List<Column> getInvisibleDownloadColumns() {
		return Arrays.asList(new Column[]{DownloadColumns.BUTTON_START, DownloadColumns.BUTTON_DEL, DownloadColumns.REF});
	}
    
    public List<Column> getInvisibleFilmColumns() {
		return null;
	}
    
    public List<Column> getInvisibleAboColumns() {
		return null;
	}
}
