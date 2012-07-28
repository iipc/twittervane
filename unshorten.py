import httplib
import urlparse

def unshorten_url(url):
  try:
    print "Resolving...",url
    parsed = urlparse.urlparse(url)
    h = httplib.HTTPConnection(parsed.netloc)
    if parsed.query != "":
        resource = "{}?{}".format(parsed.path, parsed.query) 
    else:
        resource = parsed.path
    h.request('HEAD', resource )
    response = h.getresponse()
    if response.status/100 == 3 and response.getheader('Location'):
      new_url = response.getheader('Location')
      print response.status,"New URL: ",new_url
      if url == new_url:
          return url
      return unshorten_url(response.getheader('Location')) # changed to process chains of short urls
    else:
        return url
  except:
    return url
