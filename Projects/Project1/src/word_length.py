"""
CSAPX Project 1 - Activity 4: Average Word Length

This program takes a unigram dataset then shows the average word length
over the given period of time through standard output and/or matplotlib

Author: Michael Jansen
"""
import argparse
import file_reader
import matplotlib.pyplot as plt
import numpy as np
import sys
import collections

"""
YearData:
    total_length (int): the total length of the total words used in a year
    total_words (int): the total number of words used in a year
"""
YearData = collections.namedtuple('YearData', ('total_length', 'total_words'))

def main():
    """
    The main function. Finds the average word length over the given year range and displays
    it through the console and/or matplotlib
    :return: None
    """
    # Get the required arguments with argparse
    parser = argparse.ArgumentParser()
    parser.add_argument('-o', '--output', help='display the average word lengths over years', action='store_true')
    parser.add_argument('-p', '--plot', help='plot the average word lengths over years', action='store_true')
    parser.add_argument('start', help='the starting year range', type=int)
    parser.add_argument('end', help='the ending year range', type=int)
    parser.add_argument('filename', help='a comma separated value unigram file')
    args = parser.parse_args()

    # Store each argument as a variable
    start_year = args.start
    end_year = args.end
    filename = args.filename
    output = args.output
    plot = args.plot

    if start_year > end_year:
        sys.stderr.write('ERROR: start year must be less than or equal to end year')
        sys.exit(-1)

    # Read the given file
    words = file_reader.read_file(filename)

    # Add each word's data to a years dict if it is within the given year range
    years = {}
    for word in words:
        for data in words[word]:
            if start_year <= data.year <= end_year:
                if data.year in years:
                    last = years[data.year]
                    years[data.year] = YearData(last.total_length + (len(word) * data.count),
                                                last.total_words + data.count)
                else:
                    years[data.year] = YearData(len(word) * data.count, data.count)

    # Convert the total_length and total_words into an average length
    # If the output argument is specified, print that average to the console
    for year in range(start_year, end_year + 1):
        years[year] = years[year].total_length / years[year].total_words
        if output:
            print(f'{year}: {years[year]}')

    # If the plot argument is specified, plot the average word length over the given range
    if plot:
        data = []
        for year in range(start_year, end_year + 1):
            data.append(years[year])
        plt.plot(np.arange(start_year, end_year + 1), data)
        plt.title(f'Average word lengths from {start_year} to {end_year}: {filename}')
        plt.xlabel('Year')
        plt.ylabel('Average word length')
        plt.show()

if __name__ == '__main__':
    main()
