/*
 * MediathekView
 * Copyright (C) 2013 W. Xaver
 * W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package mediathek.tool;

import mediathek.config.MVConfig;
import mediathek.daten.Column;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class BeobTableHeader extends MouseAdapter
{
    private final Collection<Column> invisibleColumns;
    private final Collection<Column> allColumns;
    //rechhte Maustaste in der Tabelle

    private MVTable tabelle;
    private LinkedHashMap<Column, Boolean> columns;
    private LinkedHashMap<Column, JButton> buttons;
    private LinkedHashMap<Column,JCheckBoxMenuItem> box;
    private boolean icon = false;
    MVConfig.Configs configs;

    public BeobTableHeader(MVTable tabelle, Collection<Column> aColumns, Collection<Column> aColumnsNotToShow, boolean icon, MVConfig.Configs configs)
    {
        this.tabelle = tabelle;
        this.icon = icon;
        this.configs = configs;

        box = new LinkedHashMap<>();
        buttons = new LinkedHashMap<>();
        columns = new LinkedHashMap<>();
        for (Column column : aColumns)
        {
            columns.put(column, aColumnsNotToShow == null || aColumnsNotToShow.isEmpty() || !aColumnsNotToShow.contains(column));
        }
        invisibleColumns = aColumnsNotToShow;
        allColumns = aColumns;
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
        if (arg0.isPopupTrigger())
        {
            showMenu(arg0);
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0)
    {
        if (arg0.isPopupTrigger())
        {
            showMenu(arg0);
        }
    }


    private void showMenu(MouseEvent evt)
    {
        JPopupMenu jPopupMenu = new JPopupMenu();
        // Spalten ein-ausschalten
        for (Column coulumn : columns.keySet())
        {
            if (!columns.get(coulumn))
            {
                continue;
            }
            final JCheckBoxMenuItem neueCheckbox = new JCheckBoxMenuItem(coulumn.getName());
            box.put(coulumn,neueCheckbox);
            neueCheckbox.setSelected(columns.get(coulumn));
            neueCheckbox.addActionListener(e -> setSpalten());
            jPopupMenu.add(neueCheckbox);
        }
        // jetzt evtl. noch die Button
        if (!buttons.isEmpty())
        {
            //##Trenner##
            jPopupMenu.addSeparator();
            //##Trenner##
            final JCheckBoxMenuItem item2 = new JCheckBoxMenuItem("Button anzeigen");
            item2.setSelected(columns.get(buttons.keySet().iterator().next())); //entweder alle oder keiner!
            item2.addActionListener(e ->
            {
                for (JButton button : buttons.values())
                {
                    button.setSelected(item2.isSelected());
                }
            });
            jPopupMenu.add(item2);
        }
        if (icon)
        {
            //##Trenner##
            jPopupMenu.addSeparator();
            final JCheckBoxMenuItem item3 = new JCheckBoxMenuItem("Icons anzeigen");
            item3.setSelected(tabelle.iconAnzeigen);
            item3.addActionListener(e ->
            {
                tabelle.iconAnzeigen = item3.isSelected();
                setSpalten();
            });
            jPopupMenu.add(item3);
            final JCheckBoxMenuItem item2 = new JCheckBoxMenuItem("kleine Icons anzeigen");
            item2.setSelected(tabelle.iconKlein);
            if (!tabelle.iconAnzeigen)
            {
                item2.setEnabled(false);
            } else
            {
                item2.addActionListener(e ->
                {
                    tabelle.iconKlein = item2.isSelected();
                    setSpalten();
                });
            }
            jPopupMenu.add(item2);
        }
        //##Trenner##
        jPopupMenu.addSeparator();
        // Tabellenspalten umbrechen
        JCheckBoxMenuItem itemBr = new JCheckBoxMenuItem("Zeilen umbrechen");
        itemBr.setSelected(tabelle.lineBreak);
        itemBr.addActionListener(e ->
        {
            tabelle.lineBreak = itemBr.isSelected();
            MVConfig.add(configs, Boolean.toString(itemBr.isSelected()));
            setSpalten();
        });
        jPopupMenu.add(itemBr);

        //##Trenner##
        jPopupMenu.addSeparator();
        // Tabellenspalten zurücksetzen
        JMenuItem item1 = new JMenuItem("Spalten zurücksetzen");
        item1.addActionListener(e -> tabelle.resetTabelle());
        jPopupMenu.add(item1);
        //anzeigen
        jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }


    private void setSpalten()
    {
        for (Column boxKey : box.keySet())
        {
                columns.replace(boxKey,box.get(boxKey).isSelected());
        }
        tabelle.spaltenEinAus();
        tabelle.setHeight();
    }


}
