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

package pixelitor.menus;

import pixelitor.Build;
import pixelitor.Composition;
import pixelitor.ImageComponents;
import pixelitor.layers.Layers;
import pixelitor.tools.AbstractBrushTool;
import pixelitor.tools.Tools;
import pixelitor.utils.Dialogs;

import javax.swing.*;
import java.awt.Shape;
import java.awt.event.ActionEvent;

import static pixelitor.ImageComponents.getActiveComp;

/**
 * Static methods for managing the selection actions
 */
public final class SelectionActions {

    private static final Action cropAction = new AbstractAction("Crop") {
        @Override
        public void actionPerformed(ActionEvent e) {
            ImageComponents.selectionCropActiveImage();
        }
    };

    private static final Action deselectAction = new AbstractAction("Deselect") {
        @Override
        public void actionPerformed(ActionEvent e) {
            getActiveComp().get().deselect(true);
        }
    };

    private static final Action invertSelectionAction = new AbstractAction("Invert Selection") {
        @Override
        public void actionPerformed(ActionEvent e) {
            getActiveComp().get().invertSelection();
        }
    };

    private static final Action traceWithBrush = new TraceAction("Stroke with Current Brush", Tools.BRUSH);
    private static final Action traceWithEraser = new TraceAction("Stroke with Current Eraser", Tools.ERASER);

    static {
        setEnabled(false, null);
    }

    private SelectionActions() {
    }

    public static void setEnabled(boolean b, Composition comp) {
        assert SwingUtilities.isEventDispatchThread();

        if (Build.CURRENT.isRobotTest()) {
            if (comp != null) {
                boolean hasSelection = comp.hasSelection();
                if (hasSelection != b) {
                    String name = comp.getName();
                    throw new IllegalStateException("composition " + name +
                            ": hasSelection = " + hasSelection + ", b = " + b);
                }
            }
        }

        cropAction.setEnabled(b);
        traceWithBrush.setEnabled(b);
        traceWithEraser.setEnabled(b);
        deselectAction.setEnabled(b);
        invertSelectionAction.setEnabled(b);
    }

    public static boolean areEnabled() {
        return cropAction.isEnabled();
    }

    public static Action getCropAction() {
        return cropAction;
    }

    public static Action getTraceWithBrush() {
        return traceWithBrush;
    }

    public static Action getTraceWithEraser() {
        return traceWithEraser;
    }

    public static Action getDeselectAction() {
        return deselectAction;
    }

    public static Action getInvertSelectionAction() {
        return invertSelectionAction;
    }

    private static class TraceAction extends AbstractAction {
        private final AbstractBrushTool brushTool;

        private TraceAction(String name, AbstractBrushTool brushTool) {
            super(name);
            this.brushTool = brushTool;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!Layers.activeIsImageLayer()) {
                Dialogs.showNotImageLayerDialog();
                return;
            }

            getActiveComp()
                    .flatMap(Composition::getSelection)
                    .ifPresent(selection -> {
                        Shape shape = selection.getShape();
                        if (shape != null) {
                            brushTool.trace(getActiveComp().get(), shape);
                        }
                    });
        }
    }
}
