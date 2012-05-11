/*
GanttProject is an opensource project management tool. License: GPL3
Copyright (C) 2011-2012 Dmitry Barashev, GanttProject Team

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 3
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package net.sourceforge.ganttproject.export;

import java.awt.Component;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.gui.options.model.GPOptionGroup;
import net.sourceforge.ganttproject.io.GanttCSVExport;
import net.sourceforge.ganttproject.language.GanttLanguage;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ExporterToCSV extends ExporterBase {
  /** List of available/associated extensions */
  private static String[] FILE_EXTENSIONS = new String[] { "csv" };

  @Override
  public String getFileTypeDescription() {
    return GanttLanguage.getInstance().getText("impex.csv.description");
  }

  @Override
  public GPOptionGroup getOptions() {
    return null;
  }

  @Override
  public List<GPOptionGroup> getSecondaryOptions() {
    return null;
  }

  @Override
  public Component getCustomOptionsUI() {
    return null;
  }

  @Override
  public String getFileNamePattern() {
    return FILE_EXTENSIONS[0];
  }

  @Override
  protected ExporterJob[] createJobs(final File outputFile, List<File> resultFiles) {
    ExporterJob job = createCVSExportJob(outputFile);
    return new ExporterJob[] { job };
  }

  private ExporterJob createCVSExportJob(final File outputFile) {
    ExporterJob result = new ExporterJob("Export project") {
      @Override
      protected IStatus run() {
        try {
          outputFile.createNewFile();
          // TODO Fix this ugly hack!! Ie make the settings available in a proper way
          GanttCSVExport exporter = new GanttCSVExport(getProject(),
              ((GanttProject) getProject()).getGanttOptions().getCSVOptions());
          exporter.save(new FileOutputStream(outputFile));
        } catch (IOException e) {
          getUIFacade().showErrorDialog(e);
          return Status.CANCEL_STATUS;
        }
        return Status.OK_STATUS;
      }
    };
    return result;
  }

  @Override
  public String proposeFileExtension() {
    return FILE_EXTENSIONS[0];
  }

  @Override
  public String[] getFileExtensions() {
    return FILE_EXTENSIONS;
  }
}
