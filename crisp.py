import tweetstream
import re


# This is for Py2k.  For Py3k, use http.client and urllib.parse instead, and
# use // instead of / for the division
import httplib
import urlparse

def unshorten_url(url):
  try:
    print "Resolving...",url
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

words = ["jubilee", "olympics"]
locations = ["-10.0,50.0", "5.0,65.0"]
try:
    #with tweetstream.SampleStream("anjacks0n", "twit-4anj") as stream:
    with tweetstream.FilterStream("anjacks0n", "twit-4anj",locations=locations) as stream:
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
