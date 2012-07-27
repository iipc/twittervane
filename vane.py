#!/usr/bin/env python
import tweetstream
import re
import httplib
import urlparse
import urllib2
import ConfigParser
import simplejson
import sys
from datetime import datetime

# Force unicode behaviour:
reload(sys)
sys.setdefaultencoding('utf-8')


#>>> words = ["opera", "firefox", "safari"]
#>>> people = [123,124,125]
#>>> locations = ["-122.75,36.8", "-121.75,37.8"]
#>>> with tweetstream.FilterStream("username", "password", track=words,
#...                               follow=people, locations=locations) as stream


def unshorten_url(url):
  try:
    #print "Resolving...",url
    parsed = urlparse.urlparse(url)
    h = httplib.HTTPConnection(parsed.netloc)
    h.request('HEAD', parsed.path)
    response = h.getresponse()
    if response.status/100 == 3 and response.getheader('Location'):
        new_url = response.getheader('Location')
        if url == new_url:
          return url
        return unshorten_url(response.getheader('Location')) # changed to process chains of short urls
    else:
        return url
  except:
        return url

def tweet_stream():
    config = ConfigParser.ConfigParser()
    config.read("config.ini")
    twitter_user = config.get("twitter", "user")
    twitter_pw = config.get("twitter", "pw")

    words = ["jubilee", "olympic"]
    locations = ["-10.0,50.0", "5.0,65.0"]
    try:
        #with tweetstream.SampleStream(, ) as stream:
        with tweetstream.FilterStream(twitter_user, twitter_pw, track=words, locations=locations) as stream:
            for tweet in stream:
                print tweet
                if tweet.has_key("user"):
                    print tweet["text"]
                    for url in tweet["entities"]["urls"]:
                        print "TURL",unshorten_url(url["expanded_url"])
                    print "RURL",re.findall(r'(https?://\S+)', tweet["text"])
                    print "Got tweet from %-16s\t( tweet %d, rate %.1f tweets/sec)" % (
                        tweet["user"]["screen_name"], stream.count, stream.rate )
    except tweetstream.ConnectionError, e:
        print "Disconnected from twitter. Reason:", e.reason


tweet_stream()

