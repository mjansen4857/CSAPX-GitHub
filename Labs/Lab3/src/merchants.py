"""
CSAPX - Lab 3

This program is used to demonstrate the speed difference between finding the median value in a data set
using quick sort and quick select

Author: Michael Jansen
"""
import collections
import random
import time
import sys

"""
Person:
    name (str): The name of the merchant
    location (int): The location of the merchant
"""
Merchant = collections.namedtuple('Merchant', ('name', 'location'))

def main():
    """
    The main function - reads the given data file and finds the median given the specified method
    :return: None
    """
    if len(sys.argv) == 2:
        filename = sys.argv[1]
        search_type = 'fast'
    elif len(sys.argv) == 3:
        filename = sys.argv[2]
        search_type = sys.argv[1]
    else:
        print('Invalid Inputs! Usage: $ python3 merchants.py [slow|fast] input-file')
        return

    print('Search type: ' + search_type)
    merchants = read_merchants(filename)
    print('Number of merchants: ' + str(len(merchants)))

    start_time = time.time()

    if search_type == 'fast':
        # Find median with quick select
        k = len(merchants)//2
        median = quick_select(merchants, k)
    else:
        # Find median with quick sort
        sorted_merchants = quick_sort(merchants)
        median = sorted_merchants[len(sorted_merchants)//2]

    elapsed_time = time.time() - start_time

    # Sum the distances to all merchants from the median
    distances_sum = 0
    for merchant in merchants:
        distances_sum += abs(merchant.location - median.location)

    print('Elapsed time: ' + str(elapsed_time) + ' seconds')
    print('Optimal store location: ' + str(median))
    print('Sum of distances: ' + str(distances_sum))

def read_merchants(filename):
    """
    Reads a data set of merchants and adds them to a list
    :param filename: The file containing the merchant data
    :return: A list of merchants
    """
    merchants = list()
    with open(filename) as f:
        for line in f:
            fields = line.split()
            merchants.append(Merchant(fields[0], int(fields[1])))
    return merchants

def quick_select(data, k):
    """
    Implementation of the quick select algorithm to find the kth merchant in a list
    :param data: The list of merchants
    :param k: The desired index if the list was sorted (the median in this program)
    :return: The kth merchant
    """
    pivot = data[random.randint(0, len(data) - 1)]
    smaller, equal, larger = _partition(data, pivot)
    count = len(equal)
    m = len(smaller)
    if m <= k < m + count:
        return pivot
    elif m > k:
        return quick_select(smaller, k)
    else:
        return quick_select(larger, k - m - count)

def quick_sort(data):
    """
    Implementation of the quick sort algorithm to sort a list of merchants
    :param data: The list of merchants
    :return: Sorted list of merchants
    """
    if len(data) == 0:
        return []
    else:
        pivot = data[0]
        less, equal, greater = _partition(data, pivot)
        return quick_sort(less) + equal + quick_sort(greater)


def _partition(data, pivot):
    """
    Partitions a list into 3 other lists: less than, equal to, and greater than the pivot.
    Used in quick_sort and quick_select
    :param data: The list of merchants to partition
    :param pivot: The merchant to pivot around
    :return: The 3 partitions
    """
    less, equal, greater = [], [], []
    for element in data:
        if element.location < pivot.location:
            less.append(element)
        elif element.location > pivot.location:
            greater.append(element)
        else:
            equal.append(element)
    return less, equal, greater

# Only run the main function if this script is not imported
if __name__ == '__main__':
    main()
