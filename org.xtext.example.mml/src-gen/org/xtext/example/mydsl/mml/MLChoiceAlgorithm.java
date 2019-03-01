/**
 * generated by Xtext 2.16.0
 */
package org.xtext.example.mydsl.mml;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>ML Choice Algorithm</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.xtext.example.mydsl.mml.MLChoiceAlgorithm#getFramework <em>Framework</em>}</li>
 *   <li>{@link org.xtext.example.mydsl.mml.MLChoiceAlgorithm#getAlgorithm <em>Algorithm</em>}</li>
 * </ul>
 *
 * @see org.xtext.example.mydsl.mml.MmlPackage#getMLChoiceAlgorithm()
 * @model
 * @generated
 */
public interface MLChoiceAlgorithm extends EObject
{
  /**
   * Returns the value of the '<em><b>Framework</b></em>' attribute.
   * The literals are from the enumeration {@link org.xtext.example.mydsl.mml.FrameworkLang}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Framework</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Framework</em>' attribute.
   * @see org.xtext.example.mydsl.mml.FrameworkLang
   * @see #setFramework(FrameworkLang)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getMLChoiceAlgorithm_Framework()
   * @model
   * @generated
   */
  FrameworkLang getFramework();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.MLChoiceAlgorithm#getFramework <em>Framework</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Framework</em>' attribute.
   * @see org.xtext.example.mydsl.mml.FrameworkLang
   * @see #getFramework()
   * @generated
   */
  void setFramework(FrameworkLang value);

  /**
   * Returns the value of the '<em><b>Algorithm</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Algorithm</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Algorithm</em>' containment reference.
   * @see #setAlgorithm(MLAlgorithm)
   * @see org.xtext.example.mydsl.mml.MmlPackage#getMLChoiceAlgorithm_Algorithm()
   * @model containment="true"
   * @generated
   */
  MLAlgorithm getAlgorithm();

  /**
   * Sets the value of the '{@link org.xtext.example.mydsl.mml.MLChoiceAlgorithm#getAlgorithm <em>Algorithm</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Algorithm</em>' containment reference.
   * @see #getAlgorithm()
   * @generated
   */
  void setAlgorithm(MLAlgorithm value);

} // MLChoiceAlgorithm
