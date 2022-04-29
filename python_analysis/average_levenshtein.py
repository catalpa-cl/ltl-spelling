"""
compute average Damerau-Levenshtein distance of misspelled words for each corpus
"""

from nltk.metrics.distance import edit_distance
import xml.etree.ElementTree as ET
import os
from statistics import mean, stdev

corpora_path = "corpora_spelling"

for file in sorted(os.listdir(corpora_path)):
    
    if not file.endswith(".xml"):continue
    tree = ET.parse(corpora_path + "/" + file)
    corpus = tree.getroot()

    distances = []

    merges = 0
    distances_without_merge = []
    empty_errors = 0

    for text in corpus.findall("text"):
        for error in text.findall("error"):
            
            orig = error.text
            target = error.get("correct")
            if orig == None or target == None:
                empty_errors += 1
                continue
            distance = edit_distance(orig, target, transpositions=True)
            if "merge" in error.get("type"):
                merges += 1
            else:
                distances_without_merge.append(distance)
            distances.append(distance)

            #if distance == 0: print(orig, target)

    distances_greater_3 = len([dist for dist in distances_without_merge if dist > 3])

    print("Corpus", corpus.get("name"))
    print("avg. Levenshtein all errors", round(mean(distances),2), "SD:", round(stdev(distances), 2), sep="\t")
    print("avg. Levenshtein without merge errors", round(mean(distances_without_merge), 2), "SD:", round(stdev(distances_without_merge), 2), sep="\t")
    print("Proportion of Errors with Levenshtein > 3 (excl. merge):", round(distances_greater_3/len(distances_without_merge),2))
    print("Number of errors (total):", len(distances))
    print("Number of errors without merge:", len(distances_without_merge))
    print("Additional number of empty errors (formal problem):", empty_errors)
    print("Number of merge errors:", merges)
    print("Proportion of merge errors:", round(merges/(len(distances)),3))
    print()
