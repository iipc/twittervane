#!/usr/bin/env python2.7
import tweetstream
import re
import urllib2
import ConfigParser
import simplejson
import sys
from datetime import datetime
from unshorten import unshorten_url

# Force unicode behaviour:
reload(sys)
sys.setdefaultencoding('utf-8')

# fetch the url
tweetlogfile = "tweetlog"
tweetlog = open(tweetlogfile, 'r')
for line in tweetlog:
    tweet = eval(line)
    tags = re.findall(r'#(\S+)', tweet["text"])
    urls = re.findall(r'(https?://\S+)', tweet["text"])
    #Fri, 20 Jul 2012 08:17:09 +0000
    d = datetime.strptime( tweet['created_at'], '%a %b %d %H:%M:%S +0000 %Y')
    date = d.strftime('%d/%m/%Y %H:%M:%S')
    if len(urls) == 0:
        urls = [""]
    for url in urls:
        print "\"{}\", \"{}\", \"{}\", \"{}\", \"@{}\", \"{}\"".format( 
        	date, unshorten_url(url), ','.join(tags), tweet['text'], "", tweet['id'])

