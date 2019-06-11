->RefactoringNumber->90971<-MoveField(com.googlecode.ant_deb_task.Deb.Description;net.sourceforge.ganttproject.task.dependency.TaskDependencySliceAsDependee;[_synopsis];[])
<-endRefactoring marker->
package net.sourceforge.ganttproject.task.dependency;

import net.sourceforge.ganttproject.task.Task;

/**
 * Created by IntelliJ IDEA. User: bard
 */
public class TaskDependencySliceAsDependee extends TaskDependencySliceImpl {
    public TaskDependency[] toArray() {
        return getDependencyCollection().getDependenciesAsDependee(getTask());
    }

    public TaskDependencySliceAsDependee(Task task,
            TaskDependencyCollection dependencyCollection) {
        super(task, dependencyCollection);
    }
}
