->RefactoringNumber->99699<-ExtractSuperClass(net.sourceforge.ganttproject.action.project.ProjectMenu;Class_13;[];[getProjectSettingsAction])
<-endRefactoring marker->
/*
 * Created on 26.09.2005
 */
package net.sourceforge.ganttproject.action.project;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import net.sourceforge.ganttproject.GanttProject;
import net.sourceforge.ganttproject.IGanttProject;
import net.sourceforge.ganttproject.action.GPAction;
import net.sourceforge.ganttproject.export.ExportFileAction;
import net.sourceforge.ganttproject.gui.options.SettingsDialog2;
import net.sourceforge.ganttproject.importer.ImportFileAction;

public class ProjectMenu {
    private NewProjectAction myNewProjectAction;
    private OpenProjectAction myOpenProjectAction;
    private SaveProjectAction mySaveProjectAction;
    private SaveProjectAsAction mySaveProjectAsAction;
    private OpenURLAction myOpenURLAction;
    private ExitAction myExitAction;
    private GPAction mySaveURLAction;
    private GPAction myPrintAction;
    private ImportFileAction myImportFileAction;
    private ExportFileAction myExportFileAction;
    private Action myProjectSettingsAction;

    public ProjectMenu(final GanttProject mainFrame) {
        myProjectSettingsAction = new GPAction("projectProperties") {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsDialog2 settingsDialog = new SettingsDialog2(
                    mainFrame.getProject(), mainFrame.getUIFacade(), "settings.project.pageOrder");
                settingsDialog.show();
            }
        };
        myNewProjectAction = new NewProjectAction(mainFrame);
        myOpenProjectAction = new OpenProjectAction(mainFrame);
        mySaveProjectAction =new SaveProjectAction(mainFrame);
        mySaveProjectAsAction =new SaveProjectAsAction(mainFrame);
        myOpenURLAction = new OpenURLAction(mainFrame);
        mySaveURLAction = new SaveURLAction(mainFrame);
        myPrintAction = new PrintAction(mainFrame);
        myExitAction = new ExitAction(mainFrame);
        myImportFileAction = new ImportFileAction(mainFrame.getUIFacade(), mainFrame);
        myExportFileAction = new ExportFileAction(
                mainFrame.getUIFacade(), (IGanttProject)mainFrame, mainFrame.getGanttOptions());

    }
    public GPAction getNewProjectAction() {
        return myNewProjectAction;
    }
    public GPAction getOpenProjectAction() {
        return myOpenProjectAction;
    }
    public GPAction getSaveProjectAction() {
        return mySaveProjectAction;
    }
    public GPAction getSaveProjectAsAction() {
        return mySaveProjectAsAction;
    }
    public GPAction getOpenURLAction() {
        return myOpenURLAction;
    }
    public GPAction getExitAction() {
        return myExitAction;
    }
    public GPAction getSaveURLAction() {
        return mySaveURLAction;
    }
    public GPAction getPrintAction() {
        return myPrintAction;
    }
    public Action getImportFileAction() {
        return myImportFileAction;
    }
    public Action getExportFileAction() {
        return myExportFileAction;
    }
    public Action getProjectSettingsAction() {
        return myProjectSettingsAction;
    }
}
