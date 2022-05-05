# DKPro Spelling


DKPro Spelling is a highly configurable spellchecking application.</br>
It is language-invariant: To process any language, at least a tokenizer and dictionary are required.</br>
A named entity recognizer and (unigram) language model are likely to improve results.

Resources included in this repository are:
* [Corpora](spelling/src/main/resources/corpora):
  * The English, German and Czech [MERLIN](https://merlin-platform.eu) corpora and the German [Litkey corpus](https://www.linguistics.rub.de/litkeycorpus/index.html) in the [LeSpell format](#lespell-error-annotation-format).
  * [Scripts](https://github.com/ltl-ude/ltl-spelling/blob/master/data_prep) to convert the Italian [CItA](http://www.italianlp.it/resources/cita-corpus-italiano-di-apprendenti-l1/) and English [TOEFL-Spell](https://github.com/EducationalTestingService/TOEFL-Spell) corpora into the LeSpell format.
* [Dictionaries](spelling/src/main/resources/dictionaries) for English, German, Italian and Czech and a [script](spelling/src/main/resources/dictionaries/hunspell/GetHunspellDict.java) to generate a `.txt` dictionary from hunspell `.dic` and `.aff` files.
* Phonetic versions (`*_phoneme_map.txt`) of the above dictionaries and a [script](https://github.com/ltl-ude/ltl-spelling/blob/master/spelling/src/main/java/utils/GraphemeDictionaryToPhonemeMap.java) to create a phonetic dictionary from a given .txt dictionary.
* [Keyboard distance files](https://github.com/ltl-ude/ltl-spelling/tree/master/spelling/src/main/resources/matrixes) for English, German, Italian and Czech.
* [Unigram language models](https://github.com/ltl-ude/ltl-spelling/tree/master/spelling/src/main/resources/language_models) for English, German and Italian (see [here](spelling/src/main/java/experiments/ErrorCorrectionExperiments_unigramReranking.java) for an example on how to incorporate unigram frequencies to rerank correction candidates).

See an example end-to-end pipeline [here](spelling/src/main/java/experiments/DKPro_Spellcheck.java) and one that employs the different candidate correction methods [here](spelling/src/main/java/experiments/ErrorCorrectionExperiments_web1tReranking.java).
For easy access we also provide jars for the default configuration (see [User Mode](#user-mode)).

<img src="https://github.com/ltl-ude/ltl-spelling/blob/master/pipeline_overview.png" width="900">

Before you use phonetic spellchecking on a new corpus, please pre-generate phonetic representations of misspellings/out-of-dictionary words in it as shown [here](https://github.com/ltl-ude/ltl-spelling/blob/master/spelling/src/main/java/experiments/CollectMisspellingPhonemes.java) and place copies of the `*_phoneme_map.txt` [dictionaries](spelling/src/main/resources/dictionaries) as well as any custom phonetic dictionaries in the respective language folders [here](spelling/src/main/resources/corpora/misspelling_phonemes).

## Setup
For Web 1T reranking to work, set `WEB1T` system variable to point to the location of web1t (`export WEB1T="PATH_TO_WEB1T"`).
In this folder you need subfolders `/en`, `/de`, `/it`, `/cz` for the respective languages.

You may have to unzip some of the [dictionaries](spelling/src/main/resources/dictionaries).

## LeSpell Error Annotation Format

```xml
<corpus name="EXAMPLE_NAME"><text id="EXAMPLE_ID" lang="en">
  We mark <error correct="misspellings" type="typo">mispellings</error> as shown in this example.
</text></corpus>
```

## User Mode
#### Supports English (en), German (de), Italian (it) and Czech (cz).</br> Requires WEB1T system variable to be set.

Use the tool in its default configuration:
Generate correction candidates based on Damerau-Levenshtein Distance, rerank them using Web 1T trigrams.

Spellcheck a `.txt` file.</br>
Outputs `.tsv` with ranked list of corrections.</br>
*To run an example: java -jar DKPro_Spellcheck.jar de src/main/resources/corpora/test_de.txt*
```
java -jar DKPro_Spellcheck.jar [LANGUAGE] [PATH_TO_TXT]
```

Evaluate error correction on a corpus annotated with errors (in the [LeSpell XML format](#lespell-error-annotation-format)).</br>
Outputs recall@k and lists of words that are corrected correctly/incorrectly.</br>
*To run an example: java -jar DKPro_Spellcheck_EvaluateCorrection.jar cz src/main/resources/corpora/merlin-CZ_spelling.xml*
```
java -jar DKPro_Spellcheck_EvaluateCorrection.jar [LANGUAGE] [PATH_TO_XML]
```

## Cite
Bexte, M., Laarmann-Quante, R., Horbach, A., & Zesch, T. (2022, to appear). LeSpell - A Multi-Lingual Benchmark Corpus of Spelling Errors to Develop Spellchecking Methods for Learner Language. In Proceedings of the 13th International Conference on Language Resources and Evaluation
