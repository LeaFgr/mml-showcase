/**
 * generated by Xtext 2.15.0
 */
package org.xtext.example.mydsl.mml.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.xtext.example.mydsl.mml.FormulaItem;
import org.xtext.example.mydsl.mml.MmlPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Formula Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.mml.impl.FormulaItemImpl#getColumn <em>Column</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.impl.FormulaItemImpl#getColName <em>Col Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FormulaItemImpl extends MinimalEObjectImpl.Container implements FormulaItem
{
  /**
   * The default value of the '{@link #getColumn() <em>Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColumn()
   * @generated
   * @ordered
   */
  protected static final int COLUMN_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getColumn() <em>Column</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColumn()
   * @generated
   * @ordered
   */
  protected int column = COLUMN_EDEFAULT;

  /**
   * The default value of the '{@link #getColName() <em>Col Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColName()
   * @generated
   * @ordered
   */
  protected static final String COL_NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getColName() <em>Col Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getColName()
   * @generated
   * @ordered
   */
  protected String colName = COL_NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FormulaItemImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return MmlPackage.Literals.FORMULA_ITEM;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getColumn()
  {
    return column;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColumn(int newColumn)
  {
    int oldColumn = column;
    column = newColumn;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MmlPackage.FORMULA_ITEM__COLUMN, oldColumn, column));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getColName()
  {
    return colName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setColName(String newColName)
  {
    String oldColName = colName;
    colName = newColName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MmlPackage.FORMULA_ITEM__COL_NAME, oldColName, colName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case MmlPackage.FORMULA_ITEM__COLUMN:
        return getColumn();
      case MmlPackage.FORMULA_ITEM__COL_NAME:
        return getColName();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MmlPackage.FORMULA_ITEM__COLUMN:
        setColumn((Integer)newValue);
        return;
      case MmlPackage.FORMULA_ITEM__COL_NAME:
        setColName((String)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case MmlPackage.FORMULA_ITEM__COLUMN:
        setColumn(COLUMN_EDEFAULT);
        return;
      case MmlPackage.FORMULA_ITEM__COL_NAME:
        setColName(COL_NAME_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case MmlPackage.FORMULA_ITEM__COLUMN:
        return column != COLUMN_EDEFAULT;
      case MmlPackage.FORMULA_ITEM__COL_NAME:
        return COL_NAME_EDEFAULT == null ? colName != null : !COL_NAME_EDEFAULT.equals(colName);
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (column: ");
    result.append(column);
    result.append(", colName: ");
    result.append(colName);
    result.append(')');
    return result.toString();
  }

} //FormulaItemImpl