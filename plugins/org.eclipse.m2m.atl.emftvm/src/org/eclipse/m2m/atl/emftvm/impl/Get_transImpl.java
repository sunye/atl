/*******************************************************************************
 * Copyright (c) 2011 Vrije Universiteit Brussel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Dennis Wagelaar, Vrije Universiteit Brussel - initial API and
 *         implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.m2m.atl.emftvm.impl;


import org.eclipse.emf.ecore.EClass;
import org.eclipse.m2m.atl.emftvm.EmftvmPackage;
import org.eclipse.m2m.atl.emftvm.Get_trans;
import org.eclipse.m2m.atl.emftvm.Opcode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Get trans</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class Get_transImpl extends FieldInstructionImpl implements Get_trans {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected Get_transImpl() {
		super();
		opcode = Opcode.GET_TRANS;
		stackConsumption = 1;
		stackProduction = 1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmftvmPackage.Literals.GET_TRANS;
	}

} //Get_transImpl