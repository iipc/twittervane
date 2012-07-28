import httplib
import urlparse

def unshorten_url(url):
  try:
    parsed = urlparse.urlparse(url)
    h = httplib.HTTPConnection(parsed.netloc)
    resource = parsed.path
    if parsed.query != "":
        resource += "?" + parsed.query
    h.request('HEAD', resource )
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
