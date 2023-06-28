import sys
import math
import numpy as np
import numexpr as ne

def evaluate(input):
    return ne.evaluate(input)

if __name__ == '__main__':
    input = sys.argv[1]
    res = ne.evaluate(input)
    print(res)