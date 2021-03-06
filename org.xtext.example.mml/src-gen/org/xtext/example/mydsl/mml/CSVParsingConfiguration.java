/**
 * generated by Xtext 2.16.0
 */
package org.xtext.example.mydsl.mml;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>CSV Parsing Configuration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.mml.CSVParsingConfiguration#getSep <em>Sep</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.mydsl.mml.MmlPackage#getCSVParsingConfiguration()
 * @model
 * @generated
 */
public interface CSVParsingConfiguration extends EObject
{
  /**
   * Returns the value of the '<em><b>Sep</b></em>' attribute.
   * The literals are from the enumeration {@link org.xtext.example.mydsl.mml.CSVSeparator}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sep</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sep</em>' attribute.
   * @see org.xtext.example.mydsl.mml.CSVSeparator
   * @see #setSep(CSVSeparator)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getCSVParsingConfiguration_Sep()
   * @model
   * @generated
   */
  CSVSeparator getSep();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.CSVParsingConfiguration#getSep <em>Sep</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sep</em>' attribute.
   * @see org.xtext.example.mydsl.mml.CSVSeparator
   * @see #getSep()
   * @generated
   */
  void setSep(CSVSeparator value);

} // CSVParsingConfiguration
