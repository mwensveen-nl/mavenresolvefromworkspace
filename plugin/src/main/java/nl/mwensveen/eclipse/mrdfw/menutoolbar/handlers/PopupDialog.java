package nl.mwensveen.eclipse.mrdfw.menutoolbar.handlers;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PopupDialog extends Dialog {
	public static final int SET_TRUE_ID = IDialogConstants.CLIENT_ID + 2;
	public static final int SET_FALSE_ID = IDialogConstants.CLIENT_ID + 1;

	/**
	 * Create the dialog.
	 *
	 * @param parentShell
	 */
	public PopupDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 *
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		GridLayout gridLayout = (GridLayout) container.getLayout();
		gridLayout.numColumns = 3;

		Label textLabel = new Label(container, SWT.WRAP | SWT.CENTER);
		GridData gd_textLabel = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_textLabel.widthHint = 315;
		gd_textLabel.heightHint = 64;
		textLabel.setLayoutData(gd_textLabel);
		textLabel.setText("Resolve maven dependencies from the workspace?");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 *
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		Button button = createButton(parent, SET_TRUE_ID, "Yes", false);
		button.setToolTipText("Resolve maven dependencies from the workspace");
		button = createButton(parent, SET_FALSE_ID, "No", true);
		button.setToolTipText("Do not resolve maven dependencies from the workspace");
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(400, 162);
	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (SET_FALSE_ID == buttonId || SET_TRUE_ID == buttonId) {
			setReturnCode(buttonId);
			close();
		} else {
			super.buttonPressed(buttonId);
		}
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Maven Resolve Dependencies from Workspace");
	}
}
