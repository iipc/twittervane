#!/usr/bin/env python
import gspread
import ConfigParser

config = ConfigParser.ConfigParser()
config.read("config.ini")
guser = config.get("google", "user")
gpw = config.get("google", "pw")

# Login with your Google account
gc = gspread.login(guser, gpw)

# Open a worksheet from spreadsheet
ss = gc.open("CRISP Data Collection Form")
#wks = ss.sheet1 # Submissions sheet
wks = ss.get_worksheet(1) # Tweets sheet

# Get all values from column. Column and row indexes start from one
first_col = wks.col_values(2)

erow = len(first_col) + 1

for url in first_col:
    print url


wks.update_cell(erow, 2, 'http://test/')

