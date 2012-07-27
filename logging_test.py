import logging
from logging import handlers

logHandler = handlers.TimedRotatingFileHandler("logfile",when="midnight")
#logFormatter = logging.Formatter('%(asctime)s %(message)s')
logFormatter = logging.Formatter('%(message)s')
logHandler.setFormatter( logFormatter )
logger = logging.getLogger( 'MyLogger' )
logger.addHandler( logHandler )
logger.setLevel( logging.INFO )

for k in range(5):
    logger.info("Line %d" %  k)
