import logging
from logging import handlers
import time

logHandler = handlers.TimedRotatingFileHandler("logtest",when="S")
logFormatter = logging.Formatter('%(message)s')
logHandler.setFormatter( logFormatter )
logger = logging.getLogger( 'MyLogger' )
logger.addHandler( logHandler )
logger.setLevel( logging.INFO )

for k in range(15):
    logger.info("Line %d" %  k)
    time.sleep(0.1)
