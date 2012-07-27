#!/usr/bin/env python
import tweetstream
import re
import httplib
import urlparse
import urllib2
import ConfigParser
import simplejson
import sys
import time
from datetime import datetime

# Force unicode behaviour:
reload(sys)
sys.setdefaultencoding('utf-8')


#>>> words = ["opera", "firefox", "safari"]
#>>> people = [123,124,125]
#>>> locations = ["-122.75,36.8", "-121.75,37.8"]
#>>> with tweetstream.FilterStream("username", "password", track=words,
#...                               follow=people, locations=locations) as stream


config = ConfigParser.ConfigParser()
config.read("config.ini")
twitter_user = config.get("twitter", "user")
twitter_pw = config.get("twitter", "pw")

def tweet_stream():
    # To track:
    words = ["olympic", "olympics", "olympian", "olympiad"]
    # UK bounds:
    #locations = ["-10.0,50.0", "5.0,65.0"]
    try:
        #with tweetstream.SampleStream(, ) as stream:
        #with tweetstream.FilterStream(twitter_user, twitter_pw, track=words, locations=locations) as stream:
        with tweetstream.FilterStream(twitter_user, twitter_pw, track=words) as stream:
            for tweet in stream:
                print tweet
    
    except tweetstream.ConnectionError, e:
        print "ERROR: Disconnected from twitter. Reason:", e.reason

while True:
    tweet_stream()
    #time.sleep(0.01)

