package uk.bl.wap.crowdsourcing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import twitter4j.TwitterStream;

public class Util {
	
	public static TwitterStream twitterStream = null;
	
	public static List<String> checkListShort = Arrays.asList("http://0rz.tw","http://1link.in","http://1url.com","http://2.gp","http://2big.at","http://2tu.us"
			 ,"http://3.ly","http://307.to","http://4ms.me","http://4sq.com","http://4url.cc","http://6url.com","http://7.ly","http://a.gg","http://a.nf","http://aa.cx","http://abcurl.net"
			 ,"http://ad.vu","http://adf.ly","http://adjix.com","http://afx.cc","http://all.fuseurl.com","http://alturl.com","http://amzn.to","http://ar.gy","http://arst.ch"
			 ,"http://atu.ca","http://azc.cc","http://b23.ru","http://b2l.me","http://bacn.me","http://bcool.bz","http://binged.it","http://bizj.us","http://bloat.me","http://bravo.ly"
			 ,"http://bsa.ly","http://budurl.com","http://canurl.com","http://chilp.it","http://chzb.gr","http://cl.lk","http://cl.ly","http://clck.ru","http://cli.gs","http://cliccami.info"
			 ,"http://clickthru.ca","http://clop.in","http://conta.cc","http://cort.as","http://cot.ag","http://crks.me","http://ctvr.us","http://cutt.us","http://dai.ly","http://decenturl.com"
			 ,"http://dfl8.me","http://digbig.com","http://digg.com","http://disq.us","http://dld.bz","http://dlvr.it","http://do.my","http://doiop.com","http://dopen.us","http://easyuri.com"
			 ,"http://easyurl.net","http://eepurl.com","http://eweri.com","http://fa.by","http://fav.me","http://fb.me","http://fbshare.me","http://ff.im","http://fff.to","http://fire.to"
			 ,"http://firsturl.de","http://firsturl.net","http://flic.kr","http://flq.us","http://fly2.ws","http://fon.gs","http://freak.to","http://fuseurl.com","http://fuzzy.to"
			 ,"http://fwd4.me","http://fwib.net","http://g.ro.lt","http://gizmo.do","http://gl.am","http://go.9nl.com","http://go.ign.com","http://go.usa.gov","http://goo.gl"
			 ,"http://goshrink.com","http://gurl.es","http://hex.io","http://hiderefer.com","http://hmm.ph","http://href.in","http://hsblinks.com","http://htxt.it","http://huff.to","http://hulu.com"
			 ,"http://hurl.me","http://hurl.ws","http://icanhaz.com","http://idek.net","http://ilix.in","http://is.gd","http://its.my","http://ix.lt","http://j.mp","http://jijr.com"
			 ,"http://kl.am","http://klck.me","http://korta.nu","http://krunchd.com","http://l9k.net","http://lat.ms","http://liip.to","http://liltext.com","http://linkbee.com"
			 ,"http://linkbun.ch","http://liurl.cn","http://ln-s.net","http://ln-s.ru","http://lnk.gd","http://lnk.ms","http://lnkd.in","http://lnkurl.com","http://lru.jp","http://lt.tl"
			 ,"http://lurl.no","http://macte.ch","http://mash.to","http://merky.de","http://migre.me","http://miniurl.com","http://minurl.fr","http://mke.me","http://moby.to","http://moourl.com"
			 ,"http://mrte.ch2","http://myloc.me","http://myurl.in","http://n.pr","http://nbc.co","http://nblo.gs","http://nn.nf","http://not.my","http://notlong.com","http://nsfw.in"
			 ,"http://nutshellurl.com","http://nxy.in","http://nyti.ms","http://o-x.fr","http://oc1.us","http://om.ly","http://omf.gdv","http://omoikane.net","http://on.cnn.com"
			 ,"http://on.mktw.net","http://onforb.es","http://orz.se","http://ow.ly","http://ping.fm","http://pli.gs","http://pnt.me","http://politi.co","http://post.ly","http://pp.gg"
			 ,"http://profile.to","http://ptiturl.com","http://pub.vitrue.com","http://qlnk.net","http://qte.me","http://qu.tc","http://qy.fi","http://r.im","http://rb6.me","http://read.bi"
			 ,"http://readthis.ca","http://reallytinyurl.com","http://redir.ec","http://redirects.ca","http://redirx.com","http://retwt.me","http://ri.ms","http://rickroll.it","http://riz.gd"
			 ,"http://rt.nu","http://ru.ly","http://rubyurl.com","http://rurl.org","http://rww.tw","http://s4c.in","http://s7y.us","http://safe.mn","http://sameurl.com","http://sdut.us"
			 ,"http://shar.es","http://shink.de","http://shorl.com","http://short.ie","http://short.to","http://shortlinks.co.ukv","http://shorturl.com","http://shout.to","http://show.my"
			 ,"http://shrinkify.com","http://shrinkr.com","http://shrt.fr","http://shrt.st","http://shrten.com","http://shrunkin.com","http://simurl.com","http://slate.me","http://smallr.com"
			 ,"http://smsh.me","http://smurl.name","http://sn.im","http://snipr.com","http://snipurl.com","http://snurl.com","http://sp2.ro","http://spedr.com","http://srnk.net","http://srs.li"
			 ,"http://starturl.com","http://su.pr","http://surl.co.uk","http://surl.hu","http://t.cn","http://t.co","http://t.lh.com","http://ta.gd","http://tbd.ly","http://tcrn.ch"
			 ,"http://tgr.me","http://tgr.ph","http://tighturl.com","http://tiniuri.com","http://tiny.cc","http://tiny.ly","http://tiny.pl","http://tinylink.in","http://tinyuri.ca"
			 ,"http://tinyurl.com","http://tk.","http://tl.gd","http://tmi.me","http://tnij.org","http://tnw.to","http://tny.com","http://to.","http://to.ly","http://togoto.us","http://totc.us"
			 ,"http://toysr.us","http://tpm.ly","http://tr.im","http://tra.kz","http://trunc.it","http://twhub.com","http://twirl.at","http://twitclicks.com","http://twitterurl.net","http://twitterurl.org"
			 ,"http://twiturl.de","http://twurl.cc","http://twurl.nl","http://u.mavrev.com","http://u.nu","http://u76.org","http://ub0.cc","http://ulu.lu","http://updating.me","http://ur1.ca2"
			 ,"http://url.az","http://url.co.uk","http://url.iev","http://url360.me","http://url4.eu","http://urlborg.comvurlbrief.com","http://urlcover.com","http://urlcut.com","http://urlenco.de"
			 ,"http://urli.nl","http://urls.im","http://urlshorteningservicefortwitter.com","http://urlx.ie","http://urlzen.com","http://usat.ly","http://use.my","http://vb.ly","http://vgn.am"
			 ,"http://vl.am","http://vm.lc","http://w55.de","http://wapo.st","http://wapurl.co.uk","http://wipi.es","http://wp.mev","http://x.vu","http://xr.com","http://xrl.in","http://xrl.us"
			 ,"http://xurl.es","http://xurl.jp","http://y.ahoo.it","http://yatuc.com","http://ye.pe","http://yep.it","http://yfrog.comv","http://yhoo.it","http://yiyd.com","http://youtu.be"
			 ,"http://yuarel.com","http://z0p.de","http://zi.ma","http://zi.mu","http://zipmyurl.com","http://zud.me","http://zurl.ws","http://zz.gd","http://zzang.kr","http://reut.rs","http://gu.com"
			 ,"http://bbc.in","http://jrnl.ie","http://ind.pn","http://zite.to");
	
	public static boolean isShortenedUrl(String urlString) {
		boolean result = false;
		
		for (String checklistshort : checkListShort) {
			if (urlString.startsWith(checklistshort)) {
				result = true;
			}
		}
				
		return result;
	}
	
	/**
	 * Returns a <code>URL</code> as a <code>String</code>
	 * @param url the <code>URL</code> instance
	 * @return The <code>URL</code> as a <code>String</code>
	 * @throws Exception
	 */
	public static String getText(URL url) throws Exception {
        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                    connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            response.append(inputLine);

        in.close();

        return response.toString();
    }
}


