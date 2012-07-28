import httplib
import urlparse

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
