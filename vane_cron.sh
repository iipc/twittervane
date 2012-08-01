#!/bin/sh
cd /home/anjackson/git/crisp
/usr/local/bin/python2.7 vane_ana.py
git add json/tweetlog.2012-*.json
git commit -m "Auto-update from cron."
git push
