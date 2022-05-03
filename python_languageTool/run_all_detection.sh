#!/bin/sh

python3 languagetool_detection.py it-IT LanguageTool_Sentences/CItA_sentences.tsv
python3 languagetool_detection.py it-IT LanguageTool_Sentences/MERLIN-IT_sentences.tsv
python3 languagetool_detection.py de-DE LanguageTool_Sentences/Litkey_sentences.tsv
python3 languagetool_detection.py de-DE LanguageTool_Sentences/MERLIN-DE_sentences.tsv
python3 languagetool_detection.py de-DE LanguageTool_Sentences/SkaLa_sentences.tsv
python3 languagetool_detection.py en-US LanguageTool_Sentences/TOEFL_sentences.tsv