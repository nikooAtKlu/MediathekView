package mediathek.daten;

import java.util.*;

public class ColumnManagerFactory
{
    private static ColumnManagerFactory instance;

    public static ColumnManagerFactory getInstance()
    {
        if (instance == null) instance = new ColumnManagerFactory();
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

    public Collection<Column> getPSetColumns()
    {
        return Arrays.asList(PSetColumns.values());
    }

    public Collection<Column> getProgColumns()
    {
        return Arrays.asList(ProgColumns.values());
    }

    public Collection<Column> getMediaDbColumns()
    {
        return Arrays.asList(MediaDbColumns.values());
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public Column getById(int aId, Collection<Column> aColumns)
    {
        for (Column column : aColumns)
        {
            if (aId == column.getId())
            {
                return column;
            }
        }
        return null;
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public FilmColumns getFilmColumnById(int aId)
    {
        return (FilmColumns) getById(aId, getFilmColumns());
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public DownloadColumns getDownloadColumnById(int aId)
    {
        return (DownloadColumns) getById(aId, getDownloadColumns());
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public AboColumns getAboColumnById(int aId)
    {
        return (AboColumns) getById(aId, getAboColumns());
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public PSetColumns getPSetColumnById(int aId)
    {
        return (PSetColumns) getById(aId, getPSetColumns());
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public ProgColumns getProgColumnById(int aId)
    {
        return (ProgColumns) getById(aId, getProgColumns());
    }

    /**
     * Returns the Column with the given ID.
     *
     * @param aId The id.
     * @return The Column with the given ID or null when no Column found.
     */
    public MediaDbColumns getMediaDbColumnById(int aId)
    {
        return (MediaDbColumns) getById(aId, getMediaDbColumns());
    }

    public String[] getColumnAllNames(Collection<Column> aCollumns)
    {
        return aCollumns.stream().map(Column::getName).toArray(String[]::new);
    }

    public String[] getFilmColumnAllNames()
    {
        return getColumnAllNames(getFilmColumns());
    }

    public String[] getAboColumnAllNames()
    {
        return getColumnAllNames(getAboColumns());
    }

    public String[] getDownloadColumnAllNames()
    {
        return getColumnAllNames(getDownloadColumns());
    }

    public String[] getPSetColumnAllNames()
    {
        return getColumnAllNames(getPSetColumns());
    }

    public String[] getProgColumnAllNames()
    {
        return getColumnAllNames(getProgColumns());
    }

    public String[] getMediaDbColumnAllNames()
    {
        return getColumnAllNames(getMediaDbColumns());
    }

    public List<Column> getInvisibleDownloadColumns()
    {
        return Arrays.asList(new Column[]{DownloadColumns.BUTTON_START, DownloadColumns.BUTTON_DEL, DownloadColumns.REF});
    }

    public List<Column> getInvisibleFilmColumns()
    {
        return new ArrayList<>();
    }

    public List<Column> getInvisibleAboColumns()
    {
        return new ArrayList<>();
    }

    public List<Column> getInvisiblePsetColumns()
    {
        return new ArrayList<>();
    }

    public List<Column> getInvisibleProgColumns()
    {
        return new ArrayList<>();
    }

    public List<Column> getInvisibleMediaDbColumns()
    {
        return new ArrayList<>();
    }
}
