

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
public class KnownWord extends Annotation {
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(KnownWord.class);
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
  protected KnownWord() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param addr low level Feature Structure reference
   * @param type the type of this Feature Structure 
   */
  public KnownWord(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public KnownWord(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public KnownWord(JCas jcas, int begin, int end) {
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
  //* Feature: pathToDictItWasFoundIn

  /** getter for pathToDictItWasFoundIn - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPathToDictItWasFoundIn() {
    if (KnownWord_Type.featOkTst && ((KnownWord_Type)jcasType).casFeat_pathToDictItWasFoundIn == null)
      jcasType.jcas.throwFeatMissing("pathToDictItWasFoundIn", "spelling.types.KnownWord");
    return jcasType.ll_cas.ll_getStringValue(addr, ((KnownWord_Type)jcasType).casFeatCode_pathToDictItWasFoundIn);}
    
  /** setter for pathToDictItWasFoundIn - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPathToDictItWasFoundIn(String v) {
    if (KnownWord_Type.featOkTst && ((KnownWord_Type)jcasType).casFeat_pathToDictItWasFoundIn == null)
      jcasType.jcas.throwFeatMissing("pathToDictItWasFoundIn", "spelling.types.KnownWord");
    jcasType.ll_cas.ll_setStringValue(addr, ((KnownWord_Type)jcasType).casFeatCode_pathToDictItWasFoundIn, v);}    
  }

    