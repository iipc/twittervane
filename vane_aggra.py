#!/usr/bin/env python2.7
import tweetstream
import re
import urllib2
import ConfigParser
import simplejson
import sys
import os
import glob
import time
from datetime import datetime
from unshorten import *
from string import punctuation

# Force unicode behaviour:
reload(sys)
sys.setdefaultencoding('utf-8')

# Go through JSON summary files
totals = {}
totals['key'] = "Total Tweets"
totals['values'] = []

rate = {}
rate['key'] = "Tweets per Second"
rate['values'] = []

stopwords = { 'getgluehd', 'getgluehd!', 'olympics.', 'olympics2012', '2012londonolympics', 'olympics,', 
'london', 'london2012.', 'teamusa!', 'olympics!', 'bbc2012', 'olympic2012'} 
#{ 'watching', 'i\'m watching', 'to watch', 'one', 'i\'m', 'games', '2012', 'sport', 'now', 'go', 'i love' }

# All Words
all_words = {}
wovertime = {}

for file in glob.glob("json/tweetlog.*-??_??.json"):
    tws = simplejson.load(open(file))
    tstamp = tws['timestamp']
    for tag in tws['tags']:
        tws['words'][tag] = tws.get( tag, 0) + tws['tags'][tag]
    wovertime[tstamp] = tws
    # Totals
    totals['values'].append([tws['timestamp'],tws['total']])
    rate['values'].append([tws['timestamp'],"%.2f" % round(tws['total']/3600.0, 2) ])
    for word in tws['tags']:
        if word not in stopwords:
            all_words[word] = all_words.get( word, 0) + tws['tags'][word]

f = open("dashboard/twotal.json","w")
f.write( simplejson.dumps([totals]) )
f.close()

f = open("dashboard/twrate.json","w")
f.write( simplejson.dumps([rate]) )
f.close()

f = open("dashboard/twotals.json","w")
f.write( simplejson.dumps([totals, rate]) )
f.close()

top_words = sorted(all_words.items(), key=lambda item: item[1], reverse=True)[0:30]
wdata_all = []
for word, count in top_words:
    wdata = {}
    wdata['key'] = word
    wdata['values'] = []
    for tstamp in sorted(wovertime):
        tws = wovertime[tstamp]
        wdata['values'].append( [tstamp, tws['words'].get(word,0) ])
    wdata_all.append(wdata)

f = open("dashboard/wordstreams.json","w")
f.write( simplejson.dumps(wdata_all) )
f.close()
