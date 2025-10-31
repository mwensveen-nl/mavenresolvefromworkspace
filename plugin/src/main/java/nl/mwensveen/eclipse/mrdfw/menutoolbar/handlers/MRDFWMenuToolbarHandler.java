package nl.mwensveen.eclipse.mrdfw.menutoolbar.handlers;

import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IMavenProjectRegistry;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.m2e.core.project.ResolverConfiguration;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class MRDFWMenuToolbarHandler extends AbstractHandler {
	private static final ILog LOG = Platform.getLog(Platform.getBundle("nl.mwensveen.eclipse.plugins.mrdfw-plugin"));

	public MRDFWMenuToolbarHandler() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		PopupDialog popupDialog = new PopupDialog(window.getShell());
		popupDialog.open();
		int returnCode = popupDialog.getReturnCode();
		if (PopupDialog.CANCEL == returnCode) {
			return null;
		}
		boolean resolveFromWorkspace = PopupDialog.SET_TRUE_ID == returnCode;
		LOG.info("Setting all Maven Resource Dependencies from Workspace to %s".formatted(resolveFromWorkspace));

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		Arrays.stream(workspace.getRoot().getProjects()).sequential().filter(p -> p.isOpen())
				.forEach(p -> processProject(p, resolveFromWorkspace));

		return null;
	}

	private void processProject(IProject project, boolean resolveFromWorkspace) {
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
