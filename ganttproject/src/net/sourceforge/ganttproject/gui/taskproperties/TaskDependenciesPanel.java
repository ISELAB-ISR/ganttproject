/* LICENSE: GPL2
Copyright (C) 2010 Dmitry Barashev

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/
package net.sourceforge.ganttproject.gui.taskproperties;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import net.sourceforge.ganttproject.gui.AbstractTableAndActionsComponent;
import net.sourceforge.ganttproject.task.Task;
import net.sourceforge.ganttproject.task.TaskManager;
import net.sourceforge.ganttproject.task.dependency.TaskDependency;
import net.sourceforge.ganttproject.task.dependency.TaskDependencyConstraint;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishFinishConstraintImpl;
import net.sourceforge.ganttproject.task.dependency.constraint.FinishStartConstraintImpl;
import net.sourceforge.ganttproject.task.dependency.constraint.StartFinishConstraintImpl;
import net.sourceforge.ganttproject.task.dependency.constraint.StartStartConstraintImpl;

import org.jdesktop.jdnc.JNTable;
import org.jdesktop.swing.decorator.AlternateRowHighlighter;
import org.jdesktop.swing.decorator.Highlighter;
import org.jdesktop.swing.decorator.HighlighterPipeline;

/**
 * @author dbarashev (Dmitry Barashev)
 */
public class TaskDependenciesPanel {
    private static TaskDependencyConstraint[] CONSTRAINTS = new TaskDependencyConstraint[] {
        new FinishStartConstraintImpl(), new FinishFinishConstraintImpl(),
        new StartFinishConstraintImpl(), new StartStartConstraintImpl() };

    private static TaskDependency.Hardness[] HARDNESS = new TaskDependency.Hardness[] {
        TaskDependency.Hardness.STRONG, TaskDependency.Hardness.RUBBER
    };

    private Task myTask;
    private DependencyTableModel myModel;
    private JNTable myTable;

    public JPanel getComponent() {
        myModel = new DependencyTableModel(myTask);
        myTable = new JNTable(myModel);
        myTable.setPreferredVisibleRowCount(10);
        myTable.setHighlighters(new HighlighterPipeline(new Highlighter[] {
                AlternateRowHighlighter.floralWhite,
                AlternateRowHighlighter.quickSilver }));
        myTable.getTable().setSortable(false);
        setUpPredecessorComboColumn(
                DependencyTableModel.MyColumn.TASK_NAME.getTableColumn(myTable.getTable()),
                myTable.getTable());
        setUpTypeComboColumn(DependencyTableModel.MyColumn.CONSTRAINT_TYPE.getTableColumn(myTable.getTable()));
        setUpHardnessColumnEditor(DependencyTableModel.MyColumn.HARDNESS.getTableColumn(myTable.getTable()));
        AbstractTableAndActionsComponent<TaskDependency> tableAndActions =
            new AbstractTableAndActionsComponent<TaskDependency>(myTable.getTable()) {
                @Override
                protected void onAddEvent() {
                    myTable.getTable().editCellAt(
                            myModel.getRowCount(), DependencyTableModel.MyColumn.TASK_NAME.ordinal());
                }

                @Override
                protected void onDeleteEvent() {
                    myModel.delete(myTable.getTable().getSelectedRows());
                }

                @Override
                protected void onSelectionChanged() {
                }
        };

        JPanel result = new JPanel(new BorderLayout());
        result.add(tableAndActions.getActionsComponent(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(myTable);
        result.add(scrollPane, BorderLayout.CENTER);
        return result;
    }

    public void init(Task task) {
        myTask = task;
    }

    public void commit() {
        myModel.commit();
    }

    private Task getTask() {
        return myTask;
    }

    protected void setUpPredecessorComboColumn(TableColumn predecessorColumn, final JTable predecessorTable) {
        final JComboBox comboBox = new JComboBox();
        Task[] possiblePredecessors = getTaskManager().getAlgorithmCollection()
                .getFindPossibleDependeesAlgorithm().run(getTask());
        for (int i = 0; i < possiblePredecessors.length; i++) {
            Task next = possiblePredecessors[i];
            comboBox.addItem(new DependencyTableModel.TaskComboItem(next));
        }

        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (predecessorTable.getEditingRow() != -1) {
                    DependencyTableModel.TaskComboItem selectedItem =
                        (DependencyTableModel.TaskComboItem) comboBox.getSelectedItem();
                    if (selectedItem != null) {
                        predecessorTable.setValueAt(selectedItem,
                                predecessorTable.getEditingRow(), 0);
                        predecessorTable.setValueAt(TaskDependenciesPanel.CONSTRAINTS[0],
                                predecessorTable.getEditingRow(), 2);
                    }
                }
            }
        });
        comboBox.setEditable(false);
        predecessorColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private TaskManager getTaskManager() {
        return getTask().getManager();
    }

    private void setUpTypeComboColumn(TableColumn typeColumn) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(TaskDependenciesPanel.CONSTRAINTS);
        JComboBox comboBox = new JComboBox(model);
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(false);
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    private void setUpHardnessColumnEditor(TableColumn hardnessColumn) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(TaskDependenciesPanel.HARDNESS);
        JComboBox comboBox = new JComboBox(model);
        comboBox.setSelectedIndex(0);
        comboBox.setEditable(false);
        hardnessColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }
}
