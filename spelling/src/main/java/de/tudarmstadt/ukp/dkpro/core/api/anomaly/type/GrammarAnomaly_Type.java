
/* First created by JCasGen Thu Apr 28 10:17:13 CEST 2022 */
package de.tudarmstadt.ukp.dkpro.core.api.anomaly.type;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;

/** 
 * Updated by JCasGen Thu Apr 28 10:19:32 CEST 2022
 * @generated */
public class GrammarAnomaly_Type extends Anomaly_Type {
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = GrammarAnomaly.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.tudarmstadt.ukp.dkpro.core.api.anomaly.type.GrammarAnomaly");



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public GrammarAnomaly_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

  }
}



    