#!/bin/sh
cd /home/anjackson/git/twittervane
/usr/local/bin/python2.7 vane_ana.py
/usr/local/bin/python2.7 vane_aggra.py
git checkout gh-pages
git add json/tweetlog.2012-*.json
git commit -a -m "Auto-update from cron."
git push origin gh-pages
