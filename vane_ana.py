#!/usr/bin/env python2.7
import tweetstream
import re
import urllib2
import ConfigParser
import simplejson
import sys
import time
from datetime import datetime
from unshorten import *
from string import punctuation

# Force unicode behaviour:
reload(sys)
sys.setdefaultencoding('utf-8')

# Load stopwords, and extend with tags.
stopwords = open('common-english-words.txt', 'r').read().split(',')
stopwords.append('')
stopwords.append('olympic')
stopwords.append('olympics')
stopwords.append('london2012')
stopwords.append('rt')
stopwords.append('x')
stopwords.append('amp')

# fetch the url
tweetlogfile = sys.argv[1]
tweetlog = open(tweetlogfile, 'r')
N = 40
words = {}
tags = {}
links = {}
last_word = ""
twotal = 0
first_d = None
last_d = None
#
for line in tweetlog:
    tweet = eval(line)
    twotal += 1
    # Words
    for word in tweet['text'].split():
        word = word.strip(punctuation).lower()
        if word not in stopwords:
            words[word] = words.get(word, 0) + 1
            if last_word != "":
                combo_word = last_word+"-"+word
                words[combo_word] = words.get(combo_word, 0) + 2
        last_word = word
    # TAGS
    twags = re.findall(r'#(\S+)', tweet["text"])
    for tag in twags:
        tag = tag.lower()
        if tag not in stopwords:
            tags[tag] = tags.get(tag, 0) + 1
    # URLs
    urls = re.findall(r'(https?://[^\s\"]+)', tweet["text"])
    if len(urls) == 0:
        urls = [""]
    for url in urls:
    	#lurl = unshorten_url(url)
        lurl = url
        if lurl != "":
            links[lurl] = links.get(lurl, 0) + 1
    # Dates
    d = datetime.strptime( tweet['created_at'], '%a %b %d %H:%M:%S +0000 %Y')
    date = d.strftime('%d/%m/%Y %H:%M:%S')
    #print tweet['created_at']
    if last_d != None:
        pass
        #print last_d, d, (d-last_d), ( max([d,last_d]) - min([d,last_d]) )
    if first_d == None:
        first_d = d
    last_d = d


results = {}

results['total'] = twotal
results['timestamp'] = int(time.mktime((last_d -  first_d).timetuple()))

print simplejson.dumps(results)


results['words'] = {}
top_words = sorted(words.items(), key=lambda item: item[1], reverse=True)[:N]
for word, frequency in top_words:
    results['words'][word] = frequency

results['tags'] = {}
top_tags = sorted(tags.items(), key=lambda item: item[1], reverse=True)[:N]
for tag, frequency in top_tags:
    results['tags'][tag] = frequency

results['urls'] = {}
top_links = sorted(links.items(), key=lambda item: item[1], reverse=True)[:N]
for url, frequency in top_links:
    #results['urls'][url] = frequency
    #lurl = unshorten_url(url)
    title, lurl = get_url_title(url)
    title = title.replace("\n","")
    results['urls'][url] = {}
    results['urls'][url]['lurl'] = lurl
    results['urls'][url]['title'] = title

print simplejson.dumps(results)
