
/* First created by JCasGen Thu Apr 28 10:12:32 CEST 2022 */
package spelling.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.SuggestedAction_Type;

/** 
 * Updated by JCasGen Thu Apr 28 10:19:33 CEST 2022
 * @generated */
public class SuggestedActionWithOrigin_Type extends SuggestedAction_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = SuggestedActionWithOrigin.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("spelling.types.SuggestedActionWithOrigin");
 
  /** @generated */
  final Feature casFeat_methodThatGeneratedThisSuggestion;
  /** @generated */
  final int     casFeatCode_methodThatGeneratedThisSuggestion;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getMethodThatGeneratedThisSuggestion(int addr) {
        if (featOkTst && casFeat_methodThatGeneratedThisSuggestion == null)
      jcas.throwFeatMissing("methodThatGeneratedThisSuggestion", "spelling.types.SuggestedActionWithOrigin");
    return ll_cas.ll_getStringValue(addr, casFeatCode_methodThatGeneratedThisSuggestion);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setMethodThatGeneratedThisSuggestion(int addr, String v) {
        if (featOkTst && casFeat_methodThatGeneratedThisSuggestion == null)
      jcas.throwFeatMissing("methodThatGeneratedThisSuggestion", "spelling.types.SuggestedActionWithOrigin");
    ll_cas.ll_setStringValue(addr, casFeatCode_methodThatGeneratedThisSuggestion, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public SuggestedActionWithOrigin_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_methodThatGeneratedThisSuggestion = jcas.getRequiredFeatureDE(casType, "methodThatGeneratedThisSuggestion", "uima.cas.String", featOkTst);
    casFeatCode_methodThatGeneratedThisSuggestion  = (null == casFeat_methodThatGeneratedThisSuggestion) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_methodThatGeneratedThisSuggestion).getCode();

  }
}



    