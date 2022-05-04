
/* First created by JCasGen Wed May 04 13:50:20 CEST 2022 */
package spelling.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Wed May 04 14:11:59 CEST 2022
 * @generated */
public class ExtendedSpellingAnomaly_ForTSV_Type extends Annotation_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = ExtendedSpellingAnomaly_ForTSV.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("spelling.types.ExtendedSpellingAnomaly_ForTSV");
 
  /** @generated */
  final Feature casFeat_suggestions;
  /** @generated */
  final int     casFeatCode_suggestions;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getSuggestions(int addr) {
        if (featOkTst && casFeat_suggestions == null)
      jcas.throwFeatMissing("suggestions", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    return ll_cas.ll_getStringValue(addr, casFeatCode_suggestions);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setSuggestions(int addr, String v) {
        if (featOkTst && casFeat_suggestions == null)
      jcas.throwFeatMissing("suggestions", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    ll_cas.ll_setStringValue(addr, casFeatCode_suggestions, v);}
    
  
 
  /** @generated */
  final Feature casFeat_best_candidate;
  /** @generated */
  final int     casFeatCode_best_candidate;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getBest_candidate(int addr) {
        if (featOkTst && casFeat_best_candidate == null)
      jcas.throwFeatMissing("best_candidate", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    return ll_cas.ll_getStringValue(addr, casFeatCode_best_candidate);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setBest_candidate(int addr, String v) {
        if (featOkTst && casFeat_best_candidate == null)
      jcas.throwFeatMissing("best_candidate", "spelling.types.ExtendedSpellingAnomaly_ForTSV");
    ll_cas.ll_setStringValue(addr, casFeatCode_best_candidate, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public ExtendedSpellingAnomaly_ForTSV_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_suggestions = jcas.getRequiredFeatureDE(casType, "suggestions", "uima.cas.String", featOkTst);
    casFeatCode_suggestions  = (null == casFeat_suggestions) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_suggestions).getCode();

 
    casFeat_best_candidate = jcas.getRequiredFeatureDE(casType, "best_candidate", "uima.cas.String", featOkTst);
    casFeatCode_best_candidate  = (null == casFeat_best_candidate) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_best_candidate).getCode();

  }
}



    