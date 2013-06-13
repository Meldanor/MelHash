/*
 * Copyright (C) 2013 Kilian Gaertner
 * 
 * Dieser Quelltext ist Open Source und kann von jedem verwendet werden, der 
 * folgende Bedingung einhält:
 * Jeder, der den Quelltext, ob in Teilen oder komplett,nutzt, muss dem Inhabenden
 * des Copyrights eine Pizza spendieren, sollte derjenige dem Inhabenden des Copyrights
 * begegnen.
 */

package de.meldanor.melhash.probe;

public class AlternateSquareProbe implements Probe {

    private int i = 0;

    @Override
    public int next() {
        ++i;
        if (i % 2 == 0)
            return i * i;
        else
            return i * i * -1;
    }

    @Override
    public int cur() {
        if (i % 2 == 0)
            return i * i;
        else
            return i * i * -1;
    }

}
