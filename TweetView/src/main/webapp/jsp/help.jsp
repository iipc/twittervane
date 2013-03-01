<%@ include file="header.jsp" %>
    <div id="main"> <a name="TemplateInfo"></a>
      <h1>Help</h1>
      <p>
      Here is a list of points to keep in mind when using Twittervane:</p>
		<ol>
			<li>Read the user guide: <a target="_new" href="./docs/TwitterVane User Manual v1.1.pdf">TwitterVane User Manual v1.1.pdf</a></li>
			<li>Use specific search terms in your collections.</li>
			<p>
			For example, the search term <b>"News"</b> is too broad (most incoming Tweets would be allocated to this collection)
			</p>
			<p>
			A good example of a search term is: <b>"Pope Benedict XVI"</b>
			</p>
			<li>Hash tags (#) are stripped from search terms so entering a hash tag will not affect the Tweets selected.  Also, the search terms are lower-cased before they are applied to incoming Tweets.</li>
			<li>If Tweets are no longer being allocated to your collection, ensure that the start and end dates for the collection are still valid.</li>
			<li>URLs are displayed in their expanded form if available, or their unexpanded form if not, unless otherwise stated (eg: see the <b>Top URL By Collection</b> report).</li>
			<li>The Tweet stream is not filtered by language, so your search terms will be matched to Tweets of any language.</li>
		</ol>
    </div>
<%@ include file="footer.jsp" %>