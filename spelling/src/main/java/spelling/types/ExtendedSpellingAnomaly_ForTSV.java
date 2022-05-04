

/* First created by JCasGen Wed May 04 13:50:20 CEST 2022 */
package spelling.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.cas.StringList;
import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Wed May 04 14:11:59 CEST 2022
 * XML source: /Users/mariebexte/Coding/Projects/ltl-spelling/spelling/src/main/resources/desc/type/Spelling.xml
 * @generated */
public class ExtendedSpellingAnomaly_ForTSV extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(ExtendedSpellingAnomaly_ForTSV.class);
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
  protected ExtendedSpellingAnomaly_ForTSV() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public ExtendedSpellingAnomaly_ForTSV(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public ExtendedSpellingAnomaly_ForTSV(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public ExtendedSpellingAnomaly_ForTSV(JCas jcas, int begin, int end) {
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
  //* Feature: suggestions

  /** getter for suggestions - gets 
   * @generated
   * @return value of the feature 
   */
  public String getSuggestions() {
    if (ExtendedSpellingAnomaly_ForTSV_Type.featOkTst && ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeat_suggestions == null)
      jcasType.jcas.throwFeatMissing("suggestions", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeatCode_suggestions);}
    
  /** setter for suggestions - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setSuggestions(String v) {
    if (ExtendedSpellingAnomaly_ForTSV_Type.featOkTst && ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeat_suggestions == null)
      jcasType.jcas.throwFeatMissing("suggestions", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    jcasType.ll_cas.ll_setStringValue(addr, ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeatCode_suggestions, v);}    
   
    
  //*--------------*
  //* Feature: best_candidate

  /** getter for best_candidate - gets 
   * @generated
   * @return value of the feature 
   */
  public String getBest_candidate() {
    if (ExtendedSpellingAnomaly_ForTSV_Type.featOkTst && ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeat_best_candidate == null)
      jcasType.jcas.throwFeatMissing("best_candidate", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeatCode_best_candidate);}
    
  /** setter for best_candidate - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setBest_candidate(String v) {
    if (ExtendedSpellingAnomaly_ForTSV_Type.featOkTst && ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeat_best_candidate == null)
      jcasType.jcas.throwFeatMissing("best_candidate", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    jcasType.ll_cas.ll_setStringValue(addr, ((ExtendedSpellingAnomaly_ForTSV_Type)jcasType).casFeatCode_best_candidate, v);}    
  }

    