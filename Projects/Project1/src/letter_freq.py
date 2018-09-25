"""
CSAPX Project 1 - Activity 2: Letter Frequency

This program takes a unigram dataset then shows the frequency of each letter over
every year in standard output and/or through matplotlib

Author: Michael Jansen
"""
import argparse
import file_reader
import matplotlib.pyplot as plt
import numpy as np

def main():
    """
    The main function. Calculates the frequency of all letters and displays it
    through the chosen method
    :return: None
    """
    # Get the required arguments with argparse
    parser = argparse.ArgumentParser()
    parser.add_argument('-o', '--output', help='display letter frequencies to standard output', action='store_true')
    parser.add_argument('-p', '--plot', help='plot letter frequencies using matplotlib', action='store_true')
    parser.add_argument('filename', help='a comma separated value unigram file')
    args = parser.parse_args()

    # Read the given file
    words = file_reader.read_file(args.filename)

    # Create a dictionary containing all 26 letters
    letters = {}
    for i in range(97, 123):
        letters[chr(i)] = 0.0

    # Add the count of each letter to the dictionary/add to the total
    total = 0
    for word in words:
        for letter in word:
            for data in words[word]:
                letters[letter] += data.count
                total += data.count

    # Convert letter count to the percentage of the total
    for letter in letters:
        letters[letter] /= total

    # Print the frequencies to the console if specified
    if args.output:
        for letter in letters:
            print(f'{letter}: {letters[letter]}')

    # Display a plot of the frequencies if specified
    if args.plot:
        keys = letters.keys()
        values = letters.values()
        y_pos = np.arange(len(keys))
        plt.bar(y_pos, values, align='center')
        plt.xticks(y_pos, keys)
        plt.xlabel('Letter')
        plt.ylabel('Frequency')
        plt.title('Letter Frequency: ' + args.filename)
        plt.show()

if __name__ == '__main__':
    main()
