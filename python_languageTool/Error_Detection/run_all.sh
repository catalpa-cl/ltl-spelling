#!/bin/sh

python3 languagetool_detection.py it-IT data/CItA_sentences.tsv
python3 languagetool_detection.py it-IT data/MERLIN-IT_sentences.tsv
python3 languagetool_detection.py de-DE data/Litkey_sentences.tsv
python3 languagetool_detection.py de-DE data/MERLIN-DE_sentences.tsv
python3 languagetool_detection.py de-DE data/SkaLa_sentences.tsv
python3 languagetool_detection.py en-US data/TOEFL_sentences.tsv