/*
 * Copyright 2015 Laszlo Balazs-Csiki
 *
 * This file is part of Pixelitor. Pixelitor is free software: you
 * can redistribute it and/or modify it under the terms of the GNU
 * General Public License, version 3 as published by the Free
 * Software Foundation.
 *
 * Pixelitor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pixelitor. If not, see <http://www.gnu.org/licenses/>.
 */

package pixelitor.tools.brushes;

import java.util.ArrayList;
import java.util.List;

/**
 * The settings of a dabs brush.
 * This must be a separate class from the main dabs brush class
 * because the settings are shared between different symmetry-instances
 * of the same brush type
 */
public class DabsBrushSettings {
    private AngleSettings angleSettings;
    private SpacingStrategy spacingStrategy;
    private final List<DabsBrush> brushes = new ArrayList<>(4);

    public DabsBrushSettings(AngleSettings angleSettings, SpacingStrategy spacingStrategy) {
        this.angleSettings = angleSettings;
        this.spacingStrategy = spacingStrategy;
    }

    public void changeSpacing(SpacingStrategy spacingStrategy) {
        this.spacingStrategy = spacingStrategy;
        notifyBrushes();
    }

    public void changeAngleSettings(AngleSettings angleSettings) {
        this.angleSettings = angleSettings;
        notifyBrushes();
    }

    public boolean isAngleAware() {
        return angleSettings.isAngleAware();
    }

    public AngleSettings getAngleSettings() {
        return angleSettings;
    }

    public SpacingStrategy getSpacingStrategy() {
        return spacingStrategy;
    }

    private void notifyBrushes() {
        for (DabsBrush brush : brushes) {
            brush.settingsChanged();
        }
    }

    public void registerBrush(DabsBrush brush) {
        brushes.add(brush);

        assert brushes.size() <= 4;
    }

    public void unregisterBrush(DabsBrush brush) {
        brushes.remove(brush);
    }

}