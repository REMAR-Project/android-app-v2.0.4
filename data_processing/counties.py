#!/usr/bin/env python

import csv
import simplejson as json
import io

if __name__=='__main__':
    data = {}
    with io.open('counties.csv', 'r', encoding='utf8') as f:
        reader = csv.DictReader(f)
        for row in reader:
            data[row['S'][1:]] = data.get(row['S'][1:], []) + [row['M']]
        for key in data.keys():
            data[key].sort()
        # Regions
        print(json.dumps(sorted(list(data.keys())), ensure_ascii=False))
        # Region counties
        #print(json.dumps(data, ensure_ascii=False))
