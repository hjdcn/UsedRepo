package weibo4j;
import weibo4j.WeiboResponse;
import weibo4j.org.json.JSONException;
import weibo4j.org.json.JSONObject;

public class OAuthVerifier extends WeiboResponse {

	/**
	 * 
	 */
	private String verifier;
	private static final long serialVersionUID = -569223095256483181L;
	
	public OAuthVerifier(JSONObject json){
    	if(json!=null){
            try {
				verifier = json.getString("oauth_verifier");
			} catch (JSONException e) {
				e.printStackTrace();
			}
        }
	}
	
	public String getVerifier() {
		return verifier;
	}
}
