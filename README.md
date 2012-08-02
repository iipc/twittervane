Twittervane
===========

Dependencies
------------

    easy_install tweetstream
    easy_install simplejson

Ideas
=====

Using an hourly log file.

Lessons?

To Do:
- Outputs as Python dict(), should use JSON (original JSON if possible).
- Make adding tags easy.
- Autosuggest or add strongly co-located tags.
- Support 302 in: easy_install urlclean lxml BeautifulSoup

Useful Commands:
- nohup python2.7 vane.py &
- tail -f tweetlog | pv -l -i10 -r -b >/dev/null



