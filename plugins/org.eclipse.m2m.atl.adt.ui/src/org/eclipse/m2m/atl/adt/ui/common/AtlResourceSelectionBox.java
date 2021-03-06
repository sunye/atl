/**
 * <copyright>
 *
 * Copyright (c) 2002-2007 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *   Obeo - code cleanup and tweaking for use within ATL
 *
 * </copyright>
 *
 * $Id: AtlResourceSelectionBox.java,v 1.2 2009/10/30 17:19:02 wpiers Exp $
 */
package org.eclipse.m2m.atl.adt.ui.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.presentation.EcoreEditorPlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.ui.action.LoadResourceAction.LoadResourceDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * Provide dialogs to get EMF metamodels URIs or path. This class originally came from plugin
 * <code>org.eclipse.emf.ecore.editor</code>.
 * 
 * @author <a href="mailto:william.piers@obeo.fr">William Piers</a>
 */
public class AtlResourceSelectionBox extends LoadResourceDialog {

	private Shell shell;

	private Set<EPackage> registeredPackages = new LinkedHashSet<EPackage>();

	/**
	 * Creates the dialogs.
	 * 
	 * @param parent
	 *            the parent shell
	 */
	public AtlResourceSelectionBox(Shell parent) {
		super(parent);
		this.shell = parent;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.edit.ui.action.LoadResourceAction.LoadResourceDialog#processResource(org.eclipse.emf.ecore.resource.Resource)
	 */
	@Override
	protected boolean processResource(Resource resource) {
		// Put all static package in the package registry.
		//
		ResourceSet resourceSet = domain.getResourceSet();
		if (!resourceSet.getResources().contains(resource)) {
			Registry packageRegistry = resourceSet.getPackageRegistry();
			for (EPackage ePackage : getAllPackages(resource)) {
				packageRegistry.put(ePackage.getNsURI(), ePackage);
				registeredPackages.add(ePackage);
			}
		}
		return true;
	}

	public Set<EPackage> getRegisteredPackages() {
		return registeredPackages;
	}

	private Collection<EPackage> getAllPackages(Resource resource) {
		List<EPackage> result = new ArrayList<EPackage>();
		for (TreeIterator<?> j = new EcoreUtil.ContentTreeIterator<Object>(resource.getContents()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<? extends EObject> getEObjectChildren(EObject eObject) {
				return eObject instanceof EPackage ? ((EPackage)eObject).getESubpackages().iterator()
						: Collections.<EObject> emptyList().iterator();
			}
		}; j.hasNext();) {
			Object content = j.next();
			if (content instanceof EPackage) {
				result.add((EPackage)content);
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.ui.dialogs.ResourceDialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		Composite buttonComposite = (Composite)composite.getChildren()[0];
		Button browseRegisteredPackagesButton = new Button(buttonComposite, SWT.PUSH);
		browseRegisteredPackagesButton.setText(EcoreEditorPlugin.INSTANCE
				.getString("_UI_BrowseRegisteredPackages_label")); //$NON-NLS-1$
		prepareBrowseRegisteredPackagesButton(browseRegisteredPackagesButton);

		FormData data = new FormData();
		Control[] children = buttonComposite.getChildren();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(children[0], -CONTROL_OFFSET);
		browseRegisteredPackagesButton.setLayoutData(data);

		uriField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = uriField.getText();
				if (text.contains(" ")) { //$NON-NLS-1$
					String[] uris = text.split(" "); //$NON-NLS-1$
					if (uris.length > 0) {
						uriField.setText(uris[uris.length - 1]);
					}
				}
			}
		});

		return composite;
	}

	private void prepareBrowseRegisteredPackagesButton(Button browseRegisteredPackagesButton) {
		browseRegisteredPackagesButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				RegisteredPackageDialog dialog = new RegisteredPackageDialog(getShell());
				if (dialog.open() == Dialog.OK) {
					uriField.setText(dialog.getResultAsString());
				}
			}
		});
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.ui.dialogs.ResourceDialog#getURIText()
	 */
	@Override
	public String getURIText() {
		String res = super.getURIText();
		if (res != null && res.contains(" ")) { //$NON-NLS-1$
			res = res.split(" ")[0]; //$NON-NLS-1$
		}
		return res;
	}

	/**
	 * Sets the dialog text.
	 * 
	 * @param text
	 *            the text
	 */
	public void setText(String text) {
		uriField.setText(text);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.jface.window.Window#getShell()
	 */
	@Override
	public Shell getShell() {
		return shell;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.emf.common.ui.dialogs.ResourceDialog#isMulti()
	 */
	@Override
	protected boolean isMulti() {
		return false;
	}

}
