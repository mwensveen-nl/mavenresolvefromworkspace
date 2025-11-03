package nl.mwensveen.eclipse.mrdfw.job;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class MRDFWSettingsJob extends Job {

   private boolean resolveFromWorkspace;

   public MRDFWSettingsJob(boolean resolveFromWorkspace) {
      super("Update Project Settings for Maven Resource Dependencies from Workspace");
      this.resolveFromWorkspace = resolveFromWorkspace;
   }

   @Override
   protected IStatus run(IProgressMonitor monitor) {
      try {
         IWorkspace workspace = ResourcesPlugin.getWorkspace();
         IProject[] projects = workspace.getRoot().getProjects();

         monitor.beginTask("Updating project settings for Maven Resource Dependencies from Workspace",
               projects.length);
         MRDFWProjectUpdater updater = new MRDFWProjectUpdater();
         for (IProject project : projects) {
            if (monitor.isCanceled()) {
               return Status.CANCEL_STATUS;
            }

            if (project.isOpen()) {
               // Example: modify project settings here
               updater.processProject(project, resolveFromWorkspace);
            }

            monitor.worked(1);
         }

      } catch (Exception e) {
         return new Status(IStatus.ERROR, "nl.mwensveen.eclipse.plugins.mrdfw-plugin",
               "Failed to update settings Maven Resource Dependencies from Workspace", e);
      } finally {
         monitor.done();
      }
      return Status.OK_STATUS;
   }

}