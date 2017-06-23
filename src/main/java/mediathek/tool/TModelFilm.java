/*    
 *    MediathekView
 *    Copyright (C) 2008   W. Xaver
 *    W.Xaver[at]googlemail.com
 *    http://zdfmediathk.sourceforge.net/
 *    
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package mediathek.tool;

import de.mediathekview.mlib.daten.Film;
import de.mediathekview.mlib.tool.Datum;
import mediathek.daten.ColumnManagerFactory;
import mediathek.daten.DownloadColumns;
import mediathek.daten.FilmColumns;

@SuppressWarnings("serial")
public class TModelFilm extends TModel {
    private final Class<?>[] types;

    public TModelFilm(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        //TODO: Nicklas kontrolle
        types = new Class<?>[FilmColumns.values().length];
        for (FilmColumns enumi : FilmColumns.values()) {
        	int i = enumi.getId();
        	if(i == DownloadColumns.REF.getId()) {
        		//TODO: Nicklas kontrolle
        		//types[i] = DatenFilm.class;
        		types[i] = Film.class;
        		break;
        	}
        	switch (ColumnManagerFactory.getInstance().getFilmColumnById(i)) {
	            case NR:
	                types[i] = Integer.class;
	                break;
	            case DATUM:
	                types[i] = Datum.class;
	                break;
	            case GROESSE:
	                types[i] = MVFilmSize.class;
	                break;
//	            case DownloadColumns.REF.getId():
//	                //types[i] = DatenFilm.class;
//	                break;
	            default:
	                types[i] = String.class;
	        }
	    }
	
//        for (int i = 0; i < DatenFilm.MAX_ELEM; ++i) {
//            switch (i) {
//                case FilmColumns.NR.getId():
//                    types[i] = Integer.class;
//                    break;
//                case FilmColumns.DATUM.getId():
//                    types[i] = Datum.class;
//                    break;
//                case FilmColumns.GROESSE.getId():
//                    types[i] = MVFilmSize.class;
//                    break;
//                case DownloadColumns.REF.getId():
//                    types[i] = DatenFilm.class;
//                    break;
//                default:
//                    types[i] = String.class;
//            }
//        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
    }
}
