/* Aakash Sethi and Slavik Trach
 * DubHacks 2014
 * 
 * RequestNode tracks all HTTP GET requests and stores the name of the website
 * requested, the time it was requested, and the next request
 */

public class RequestNode {
	public String website;
	public String timestamp;
	public RequestNode next;
	
	public RequestNode() {
		this(null, null, null);
	}
	
	public RequestNode(String website, String timestamp) {
		this(website, timestamp, null);
	}
	
	public RequestNode(String website, String timestamp, RequestNode next) {
		this.website = website;
		this.timestamp = timestamp;
		this.next = next;
	}
	
}