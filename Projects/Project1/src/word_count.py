"""
CSAPX Project 1 - Activity 1: Word Counting

This program takes a word and a unigram dataset then returns the total uses of that word
across all years

Author: Michael Jansen
"""
import sys
import argparse
import file_reader

def main():
    """
    The main method. Prints the total occurrences of a given word
    across all years
    :return: None
    """
    # Get the required arguments using argparse
    parser = argparse.ArgumentParser()
    parser.add_argument('word', help='a word to display the total occurrences of')
    parser.add_argument('filename', help='a comma separated value unigram file')
    args = parser.parse_args()

    word = args.word
    filename = args.filename

    # Read the given file
    words = file_reader.read_file(filename)

    # Print the occurrences of the word if it is found in the data
    if word in words:
        total = 0
        for data in words[word]:
            total += data.count
        print(f'{word}: {total}')
    else:
        sys.stderr.write(f'ERROR: {word} does not appear!')
        sys.exit(-1)

if __name__ == '__main__':
    main()
