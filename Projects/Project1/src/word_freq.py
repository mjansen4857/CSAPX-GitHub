"""
CSAPX Project 1 - Activity 3: Word Frequency

This program takes a unigram dataset then shows the frequency of each word over
every year in standard output and/or through matplotlib

Author: Michael Jansen
"""
import argparse
import file_reader
import matplotlib.pyplot as plt
import numpy as np
import sys
import collections

"""
WordFrequency:
    word (str): The word
    frequency (int): The frequency of the word
"""
WordFrequency = collections.namedtuple('WordFrequency', ('word', 'frequency'))

def main():
    """
    The main function. Finds the total frequency of all words and displays it
    through the console and/or matplotlib
    :return: None
    """
    # Get the required arguments with argparse
    parser = argparse.ArgumentParser()
    parser.add_argument('-o', '--output', help='display the top OUTPUT (#) ranked words by number of occurrences',
                        type=int)
    parser.add_argument('-p', '--plot', help='plot the word rankings from top to bottom based on occurrences',
                        action='store_true')
    parser.add_argument('word', help='a word to display the overall ranking of')
    parser.add_argument('filename', help='a comma separated value unigram file')
    args = parser.parse_args()

    # Store each argument as a variable
    word = args.word
    filename = args.filename
    output_num = args.output
    plot = args.plot

    # Read the given file
    words = file_reader.read_file(filename)

    if word in words:
        # Create a list containing WordFrequency tuples sorted by the frequency
        frequencies = list()
        for entry in words:
            total = 0
            for data in words[entry]:
                total += data.count
            frequencies.append(WordFrequency(entry, total))
        frequencies = sorted(frequencies, key=lambda tup: tup.frequency, reverse=True)

        # Find the rank of the given word and print it to the console
        rank = 0
        for i in range(0, len(frequencies)):
            if frequencies[i].word == word:
                rank = i + 1
                print(f'{word} is ranked #{rank}')
                break

        # If the output arg was specified, print the top OUTPUT ranked words
        if output_num:
            for i in range(0, output_num):
                print(f'#{i + 1}: {frequencies[i].word} -> {frequencies[i].frequency}')

        # If the plot argument was used, create a log-log plot of the word frequencies
        # and mark the given word as a star
        if plot:
            values = []
            for i in range(0, len(frequencies)):
                values.append(frequencies[i].frequency)
            plt.loglog(np.arange(1, len(values) + 1), values)
            plt.plot(rank, values[rank-1], marker='*')
            plt.annotate(xy=[rank, values[rank-1]], s=word)
            plt.title(f'Word Frequencies: {filename}')
            plt.xlabel(f'Rank of word("{word}" is rank {rank})')
            plt.ylabel('Frequency')
            plt.show()
    else:
        sys.stderr.write(f'ERROR: {word} does not appear in {filename}')
        sys.exit(-1)

if __name__ == '__main__':
    main()
