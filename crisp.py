#!/bin/env python
import tweetstream
import re


# This is for Py2k.  For Py3k, use http.client and urllib.parse instead, and
# use // instead of / for the division
import httplib
import urlparse
import urllib2
import ConfigParser
import simplejson

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

# fetch the url
url = "http://search.twitter.com/search.json?q=%40dpref&rpp=100"
json = urllib2.urlopen(url).read()
tweets = simplejson.loads(json)
for tweet in tweets['results']:
    tags = re.findall(r'#(\S+)', tweet["text"])
    urls = re.findall(r'(https?://\S+)', tweet["text"])
    if len(urls) == 0:
        urls = [""]
    for url in urls:
        print "\"{}\", \"{}\", \"{}\", \"{}\", \"@{}\"".format( tweet['created_at'], unshorten_url(url), ','.join(tags), tweet['text'], tweet['from_user'])

def tweet_stream():
    config = ConfigParser.ConfigParser()
    config.read("config.ini")
    twitter_user = config.get("twitter", "user")
    twitter_pw = config.get("twitter", "pw")

    print twitter_user, twitter_pw

    words = ["jubilee", "olympics"]
    locations = ["-10.0,50.0", "5.0,65.0"]
    try:
        #with tweetstream.SampleStream(, ) as stream:
        with tweetstream.FilterStream(twitter_user, twitter_pw, locations=locations) as stream:
            for tweet in stream:
                if tweet.has_key("user"):
                    print tweet["text"]
                    for url in tweet["entities"]["urls"]:
                        print "TURL",unshorten_url(url["expanded_url"])
                    print "RURL",re.findall(r'(https?://\S+)', tweet["text"])
                    print "Got tweet from %-16s\t( tweet %d, rate %.1f tweets/sec)" % (
                        tweet["user"]["screen_name"], stream.count, stream.rate )
    except tweetstream.ConnectionError, e:
        print "Disconnected from twitter. Reason:", e.reason
