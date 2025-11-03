package nl.mwensveen.eclipse.mrdfw.menutoolbar.job;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectRegistry;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ResolverConfiguration;

public class MRDFWProjectUpdater {
	private static final ILog LOG = Platform.getLog(Platform.getBundle("nl.mwensveen.eclipse.plugins.mrdfw-plugin"));

	void processProject(IProject project, boolean resolveFromWorkspace) {
		IMavenProjectRegistry registry = MavenPlugin.getMavenProjectRegistry();
		IProjectConfigurationManager configManager = MavenPlugin.getProjectConfigurationManager();
		IMavenProjectFacade facade = registry.getProject(project);

		if (facade != null) {
			ResolverConfiguration config = facade.getResolverConfiguration();
			config.setResolveWorkspaceProjects(resolveFromWorkspace);
			configManager.setResolverConfiguration(project, config);
			try {
				configManager.updateProjectConfiguration(project, new org.eclipse.core.runtime.NullProgressMonitor());
			} catch (CoreException e) {
				LOG.error("Cannot set maven resolve from workspace for project", e);
			}
			LOG.info("  --> " + project.getName() + " " + config.isResolveWorkspaceProjects());
		}
	}
}
