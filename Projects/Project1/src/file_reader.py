"""
CSAPX - Project 1

This module is used across all programs to read a given unigram dataset and return
a dictionary of all words in the file

Author: Michael Jansen
"""
import sys
import os.path
import collections

"""
WordData:
    year (str): The year the word appeared
    count (int): The occurrences of the word in that year
"""
WordData = collections.namedtuple('WordData', ('year', 'count'))

def read_file(filename):
    """
    Reads a unigram dataset file and returns a dictionary of all words
    :param filename: The file to read
    :return: A dictionary of all words in the file mapped to their data
    """
    if os.path.isfile(filename):
        words = {}
        with open(filename) as f:
            for line in f:
                fields = line.split(', ')
                word = fields[0]
                year = int(fields[1])
                count = int(fields[2])
                if word not in words:
                    words[word] = list()
                words[word].append(WordData(year, count))
        return words
    else:
        sys.stderr.write(f'ERROR: {filename} does not exist!')
        sys.exit(-1)
