# !/bin/sh

python3 languagetool_correction.py it-IT LanguageTool_Sentences/CItA_sentences.tsv
python3 languagetool_correction.py it-IT LanguageTool_Sentences/MERLIN-IT_sentences.tsv
python3 languagetool_correction.py en-US LanguageTool_Sentences/TOEFL_sentences.tsv
python3 languagetool_correction.py de-DE LanguageTool_Sentences/SkaLa_sentences.tsv
python3 languagetool_correction.py de-DE LanguageTool_Sentences/MERLIN-DE_sentences.tsv
python3 languagetool_correction.py de-DE LanguageTool_Sentences/Litkey_sentences.tsv