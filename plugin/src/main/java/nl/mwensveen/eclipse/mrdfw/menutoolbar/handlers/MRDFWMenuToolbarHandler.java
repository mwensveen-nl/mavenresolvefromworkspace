package nl.mwensveen.eclipse.mrdfw.menutoolbar.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import nl.mwensveen.eclipse.mrdfw.menutoolbar.job.MRDFWSettingsJob;

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

		new MRDFWSettingsJob(resolveFromWorkspace).schedule();

		return null;
	}

}
