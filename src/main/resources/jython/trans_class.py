#!/usr/bin/env python

import sys
import os.path
from sys import argv
from string import *

if len(argv) < 2:
    print "Usage: %s training_file [testing_file]" % (argv[0])
    sys.exit(1)

train = argv[1]
assert os.path.exists(train), "training_file not found."

do_test = 0
if len(argv) >= 3:
    test = argv[2]
    assert os.path.exists(test), "testing_file not found."
    do_test = 1

new_class = []


def build_new_file(input1, file_name):

    out_file = open(file_name, "w+")
    in_file = open(input1, "r")
    for line in in_file:
        spline = split(line)

        labels = []
        if spline[0].find(':') == -1:
            labels = split(spline[0], ',')
            labels.sort()

        if labels not in new_class:
            new_class.append(labels)

        if len(labels) == 0:
            out_file.write("%s %s\n" % (new_class.index(labels), join(spline)))
        else:
            out_file.write("%s %s\n" % (new_class.index(labels), join(spline[1:])))
    in_file.close()
    out_file.close()


def main():
    build_new_file(train, "output/TmpSet/tmp_train")
    print "Number of training classes (sets of labels) is %s" % len(new_class)
    sys.stdout.flush()

    out_class = open("output/TmpSet/tmp_class", "w")
    for cl in new_class:
        out_class.write("%s\n" % join(map(lambda(num): ("%s" % num), cl), ","))
    out_class.close()

    if do_test == 1:
        build_new_file(test, "output/TmpSet/tmp_test")


main()