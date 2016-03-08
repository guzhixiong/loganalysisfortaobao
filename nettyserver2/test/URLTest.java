import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.wangcheng.dc.IConstants;
 


public class URLTest {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub

		String url = " 2010-09-08 14:03:30  一朵omo果一 119.165.52.211 Mozilla/4.0+(compatible;+MSIE+6.0;+Windows+NT+5.1;+SV1;+.NET+CLR+2.0.50727) http://item.taobao.com/item.htm?id=5436276944&taomi=8aR2LQR6GJQd%2B3eWmtuMFdGGeeyeuk48GMkvJkHM%2BAlAtHbAXVPLBKnWAzOb1%2Ft%2Fp8pD254WL1RagMZM7StaMqym%2FLIhfRfL52Q9sDJbQSmeayDoZUNbFBYZUkkG0EtOVwwJEDYTPAxfGvm1BcpVKD%2FhHulnQRGwwk6dAWURxwerYEb6uOuAd5YjiY8nJUWlTYrZsUIsRNRqbZYKIyfE&ref=http%3A%2F%2Fsearch8.taobao.com%2Fbrowse%2F16-50044612%2Fn-g%2Cnrxtcpjsgqycm3dpgi6tenbqezxhipjr----------7-------g%2Cnvwv6mjuguydonzqgvptaxzq----------------16--------------------g%2Cojsxgzlsozsv64dsnfrwkwzshewdeok5---g%2Cwd6nhsq-------2-------b--40-grid-coefp-0-all-50044612.htm%3Fqinfo%3D%2525B0%2525FC%2525D3%2525CA%2525030%252504%252504%2525042.993500%25253A5.270000%25253A1.032500%25253A0.547000%25253A0.771000%25253A0.569974%25253A5.261000%26istk%3D5%26advsort%3Dadvtaobao%26mlrscore%3D50000697%253A52491%253B50011277%253A52491%253B162105%253A104188%253B50008898%253A0%253B162116%253A0%253B50000671%253A381399%253B162205%253A0%253B162203%253A0%253B162202%253A0%253B50007068%253A0%253B162201%253A0%253B162207%253A0%253B162104%253A0%253B50010850%253A104188%253B1623%253A0%253B50008897%253A0%253B162103%253A0%253B50013196%253A0%253B162404%253A0%253B50008901%253A0%26f%3Da%26alhlog%3Ds8-property&ali_trackid=2:mm_14507705_0_0:116345003_2_1228116098 http://search8.taobao.com/browse/16-50044612/n-g,nrxtcpjsgqycm3dpgi6tenbqezxhipjr----------7-------g,nvwv6mjuguydonzqgvptaxzq----------------16--------------------g,ojsxgzlsozsv64dsnfrwkwzshewdeok5---g,wd6nhsq-------2-------b--40-grid-coefp-0-all-50044612.htm?qinfo=%25B0%25FC%25D3%25CA%25030%2504%2504%25042.993500%253A5.270000%253A1.032500%253A0.547000%253A0.771000%253A0.569974%253A5.261000&istk=5&advsort=advtaobao&mlrscore=50000697%3A52491%3B50011277%3A52491%3B162105%3A104188%3B50008898%3A0%3B162116%3A0%3B50000671%3A381399%3B162205%3A0%3B162203%3A0%3B162202%3A0%3B50007068%3A0%3B162201%3A0%3B162207%3A0%3B162104%3A0%3B50010850%3A104188%3B1623%3A0%3B50008897%3A0%3B162103%3A0%3B50013196%3A0%3B162404%3A0%3B50008901%3A0&f=a&alhlog=s8-property - 满百包邮韩版女装秋季条纹拼接棉麻连身裙C58-淘宝网";
		System.out.println(url.length());
		
		String content = "2010-09-08 13:44:55 尚客茶品旗舰店 180.171.75.33 Mozilla/5.0+(Windows;+U;+Windows+NT+5.1;+en-US)+AppleWebKit/531.0+(KHTML,+like+Gecko)+Chrome/3.0.195.0+Safari/531.0+SE+2.X http://sumcl.taobao.com/search-cat-57302985-220448760-tcK5+suuufuy6A==.htm http://sumcl.taobao.com/search-cat-57302985-213974927-yMjC9MnMxrc=.htm -";
	
		StringTokenizer st = new StringTokenizer(content," ");

		String logDate = st.nextToken();
		String logTime = st.nextToken();

		String uid = st.nextToken();

		String ip = st.nextToken();
		String agent = st.nextToken();

		url = st.nextToken();

		// url = "http://item.taobao.com/item.htm?id=1697993631";

		String referer = st.nextToken();

		String pid = st.nextToken();
		
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);
		Map<String, List<String>> params = queryStringDecoder.getParameters();

		List<String> pidParams = params.get(IConstants.LogNames.PARAM_PID);

		if (pidParams != null && !pidParams.isEmpty()) {
			pid = pidParams.get(0);			
		}
		
		System.out.println(pidParams);
		
		//String title = st.nextToken();
		
		System.out.println(URLDecoder.decode("%0A%09%09%09%09%09%09%09%E5%B0%9A%E5%AE%A2%E7%BB%BF%E8%8C%B6-%E5%B0%9A%E5%AE%A2%E8%8C%B6%E5%93%81%E6%97%97%E8%88%B0%E5%BA%97-%E6%B7%98%E5%AE%9D%E7%BD%91%0A%09%09%09%09","utf-8"));
		
	}

}
