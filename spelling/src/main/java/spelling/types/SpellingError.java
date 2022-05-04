

/* First created by JCasGen Thu Apr 28 10:12:32 CEST 2022 */
package spelling.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed May 04 14:11:59 CEST 2022
 * XML source: /Users/mariebexte/Coding/Projects/ltl-spelling/spelling/src/main/resources/desc/type/Spelling.xml
 * @generated */
public class SpellingError extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SpellingError.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected SpellingError() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public SpellingError(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SpellingError(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SpellingError(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: correction

  /** getter for correction - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCorrection() {
    if (SpellingError_Type.featOkTst && ((SpellingError_Type)jcasType).casFeat_correction == null)
      jcasType.jcas.throwFeatMissing("correction", "spelling.types.SpellingError");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SpellingError_Type)jcasType).casFeatCode_correction);}
    
  /** setter for correction - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCorrection(String v) {
    if (SpellingError_Type.featOkTst && ((SpellingError_Type)jcasType).casFeat_correction == null)
      jcasType.jcas.throwFeatMissing("correction", "spelling.types.SpellingError");
    jcasType.ll_cas.ll_setStringValue(addr, ((SpellingError_Type)jcasType).casFeatCode_correction, v);}    
   
    
  //*--------------*
  //* Feature: errorType

  /** getter for errorType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getErrorType() {
    if (SpellingError_Type.featOkTst && ((SpellingError_Type)jcasType).casFeat_errorType == null)
      jcasType.jcas.throwFeatMissing("errorType", "spelling.types.SpellingError");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SpellingError_Type)jcasType).casFeatCode_errorType);}
    
  /** setter for errorType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setErrorType(String v) {
    if (SpellingError_Type.featOkTst && ((SpellingError_Type)jcasType).casFeat_errorType == null)
      jcasType.jcas.throwFeatMissing("errorType", "spelling.types.SpellingError");
    jcasType.ll_cas.ll_setStringValue(addr, ((SpellingError_Type)jcasType).casFeatCode_errorType, v);}    
  }

    