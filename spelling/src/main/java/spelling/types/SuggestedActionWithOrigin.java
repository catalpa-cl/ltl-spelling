

/* First created by JCasGen Thu Apr 28 10:12:32 CEST 2022 */
package spelling.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SuggestedAction;


/** 
 * Updated by JCasGen Thu Apr 28 10:19:33 CEST 2022
 * XML source: /Users/mariebexte/Coding/Projects/ltl-spelling/spelling/src/main/resources/desc/type/Spelling.xml
 * @generated */
public class SuggestedActionWithOrigin extends SuggestedAction {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(SuggestedActionWithOrigin.class);
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
  protected SuggestedActionWithOrigin() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public SuggestedActionWithOrigin(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public SuggestedActionWithOrigin(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public SuggestedActionWithOrigin(JCas jcas, int begin, int end) {
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
  //* Feature: methodThatGeneratedThisSuggestion

  /** getter for methodThatGeneratedThisSuggestion - gets 
   * @generated
   * @return value of the feature 
   */
  public String getMethodThatGeneratedThisSuggestion() {
    if (SuggestedActionWithOrigin_Type.featOkTst && ((SuggestedActionWithOrigin_Type)jcasType).casFeat_methodThatGeneratedThisSuggestion == null)
      jcasType.jcas.throwFeatMissing("methodThatGeneratedThisSuggestion", "spelling.types.SuggestedActionWithOrigin");
    return jcasType.ll_cas.ll_getStringValue(addr, ((SuggestedActionWithOrigin_Type)jcasType).casFeatCode_methodThatGeneratedThisSuggestion);}
    
  /** setter for methodThatGeneratedThisSuggestion - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setMethodThatGeneratedThisSuggestion(String v) {
    if (SuggestedActionWithOrigin_Type.featOkTst && ((SuggestedActionWithOrigin_Type)jcasType).casFeat_methodThatGeneratedThisSuggestion == null)
      jcasType.jcas.throwFeatMissing("methodThatGeneratedThisSuggestion", "spelling.types.SuggestedActionWithOrigin");
    jcasType.ll_cas.ll_setStringValue(addr, ((SuggestedActionWithOrigin_Type)jcasType).casFeatCode_methodThatGeneratedThisSuggestion, v);}    
  }

    